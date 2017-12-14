package com.typany.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;

/**
 * IMEThreadHandler
 * <p>
 * Utility functions for threads that are known by a browser-wide
 * name.  For example, there is one IO thread for the entire browser
 * process, and various pieces of code find it useful to retrieve a
 * pointer to the IO thread's message loop.
 * <p>
 * Invoke a task by thread ID:
 * <p>
 * IMEThreadHandler::PostTask(IMEThreadHandler::ID::IO, task);
 */
public class IMEThread {

    private static final String TAG = "base.IMEThreadHandler";

    public enum ID {
        // The main thread in the ime.
        UI,

        // This is the thread that interacts with the file system.
        FILE,

        // This is the thread that processes non-blocking IO, i.e. IPC and network.
        // Blocking IO should happen on other threads like DB, FILE
        // and CACHE depending on the usage.
        IO,

        // This is the thread that has looper with it.
        AD,

        // NOTE: do not add new threads here that are only used by a small number of
        // files. Instead you should just use a Thread class and pass its
        // task runner around. Named threads there are only for threads that
        // are used in many places.

        // This identifier does not represent a thread.  Instead it counts the
        // number of well-known threads.  Insert new well-known threads before this
        // identifier.
        ID_COUNT
    }

    // Main thread.
    private static MessageHandler smUIMessageLoop = null;

    // Worker threads.
    private static MessageLoop smFileMessageLoop = null;
    private static MessageLoop smIOMessageLoop = null;

    // Worker threads with loop.
    private static MessageHandler smADMessageLoop = null;

    private static HandlerThread smADHandlerThread = null;

    private static boolean smInitialized = false;

    private static class MessageHandler extends Handler implements Executor {

        private static final int SCHEDULED_WORK = 1;
        private MessageLoop mMessageLoop = new MessageLoop();
        private boolean mScheduleWorkCalled = false;

        private MessageHandler() {
            // Make sure we can pass unittest.
            super(Looper.getMainLooper());
            // Force messageLoop to have the right thread id.
            mMessageLoop.ensureValidThread(Thread.currentThread().getId());
        }

        private MessageHandler(final Looper looper) {
            // Make sure we can pass unittest.
            super(looper);
            // Force messageLoop to have the right thread id.
            mMessageLoop.ensureValidThread(looper.getThread().getId());
        }

        /**
         * Posts a task to the message loop.
         */
        public void execute(Runnable task) throws RejectedExecutionException {
            mMessageLoop.execute(task);
            scheduleWork();
        }

        public long getThreadId() {
            return mMessageLoop.getThreadId();
        }

        public void quit() {
            mMessageLoop.quit();
        }

        private void scheduleWork() {
            if (mScheduleWorkCalled)
                return;
            synchronized (MessageHandler.this) {
                if (!mScheduleWorkCalled) {
                    mScheduleWorkCalled = true;
                    sendMessage(obtainAsyncMessage(SCHEDULED_WORK));
                }
            }
        }

        @Override
        public void handleMessage(Message msg) {
            if (mMessageLoop != null) {
                mScheduleWorkCalled = false;
                try {
                    mMessageLoop.loop(0); // Dry all the messages.
/*
                    if (mMessageLoop.isHasMore())
                        scheduleWork();
*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Message obtainAsyncMessage(int what) {
            // Marking the message async provides fair Chromium task dispatch when
            // served by the Android UI thread's Looper, avoiding stalls when the
            // Looper has a sync barrier.
            Message msg = Message.obtain();
            msg.what = what;
            MessageCompat.setAsynchronous(msg, true);
            return msg;
        }

        /**
         * Abstraction utility class for marking a Message as asynchronous. Prior
         * to L MR1 the async Message API was hidden, and for such cases we fall
         * back to using reflection to obtain the necessary method.
         */
        private static class MessageCompat {
            /**
             * @See android.os.Message#setAsynchronous(boolean)
             */
            public static void setAsynchronous(Message message, boolean async) {
                IMPL.setAsynchronous(message, async);
            }

            interface MessageWrapperImpl {
                /**
                 * @See android.os.Message#setAsynchronous(boolean)
                 */
                public void setAsynchronous(Message message, boolean async);
            }

            static final MessageCompat.MessageWrapperImpl IMPL;

            static {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    IMPL = new MessageCompat.LollipopMr1MessageWrapperImpl();
                } else {
                    IMPL = new MessageCompat.LegacyMessageWrapperImpl();
                }
            }

            static class LollipopMr1MessageWrapperImpl implements MessageCompat.MessageWrapperImpl {
                @SuppressLint("NewApi")
                @Override
                public void setAsynchronous(Message msg, boolean async) {
                    msg.setAsynchronous(async);
                }
            }

            static class LegacyMessageWrapperImpl implements MessageCompat.MessageWrapperImpl {
                // Reflected API for marking a message as asynchronous.
                // Note: Use of this API is experimental and likely to evolve in the future.
                private Method mMessageMethodSetAsynchronous;

                LegacyMessageWrapperImpl() {
                    try {
                        Class<?> messageClass = Class.forName("android.os.Message");
                        mMessageMethodSetAsynchronous =
                                messageClass.getMethod("setAsynchronous", new Class[]{boolean.class});
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "Failed to find android.os.Message class", e);
                    } catch (NoSuchMethodException e) {
                        Log.e(TAG, "Failed to load Message.setAsynchronous method", e);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Exception while loading Message.setAsynchronous method", e);
                    }
                }

                @Override
                public void setAsynchronous(Message msg, boolean async) {
                    if (mMessageMethodSetAsynchronous == null) return;
                    // If invocation fails, assume this is indicative of future
                    // failures, and avoid log spam by nulling the reflected method.
                    try {
                        mMessageMethodSetAsynchronous.invoke(msg, async);
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, "Illegal access to async message creation, disabling.");
                        mMessageMethodSetAsynchronous = null;
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, "Illegal argument for async message creation, disabling.");
                        mMessageMethodSetAsynchronous = null;
                    } catch (InvocationTargetException e) {
                        Log.e(TAG, "Invocation exception during async message creation, disabling.");
                        mMessageMethodSetAsynchronous = null;
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Runtime exception during async message creation, disabling.");
                        mMessageMethodSetAsynchronous = null;
                    }
                }
            }
        }
    }

    /**
     * Initialize the message loop.
     *
     * @throws RuntimeException
     */
    public static void initialize() throws RuntimeException {
        if (smInitialized)
            return;
        synchronized (IMEThread.class) {
            if (Looper.getMainLooper() == null) {
                throw new RuntimeException("IMEThreadHandler should init on thread with looper!");
            }

            // UI thread is a little bit different.
            smUIMessageLoop = new MessageHandler();

            // Create the loop first, then the thread.
            smFileMessageLoop = new MessageLoop();
            smIOMessageLoop = new MessageLoop();
            new Thread(smFileMessageLoop, "FileThread").start();
            new Thread(smIOMessageLoop, "IOThread").start();

            smADHandlerThread = new HandlerThread("ADThread");
            smADHandlerThread.start();
            smADMessageLoop = new MessageHandler(smADHandlerThread.getLooper());

            smInitialized = true;
        }
    }

    /**
     * Post task to other thread.
     *
     * @param identifier Thread identifier, currently allow UI, FILE, IO.
     * @param runnable   Task that we need to call on target thread.
     * @return WeakReference<CancellableRunnable> for the posted task.
     */
    public static Future<?> postTask(ID identifier, Runnable runnable) throws RuntimeException {
        if (!smInitialized)
            throw new RuntimeException("IMEThreadHandler need initialize() before calling postTask");

        RunnableFuture<Void> ftask = new FutureTask<>(runnable, null);
        switch (identifier) {
            case UI:
                smUIMessageLoop.execute(ftask);
                break;

            case FILE:
                smFileMessageLoop.execute(ftask);
                break;

            case IO:
                smIOMessageLoop.execute(ftask);
                break;

            case AD:
                smADMessageLoop.execute(ftask);
                break;
        }

        return ftask;
    }

    /**
     * Post task to other thread.
     *
     * @param identifier Thread identifier, currently allow UI, FILE, IO.
     * @param callable   Task that we need to call on target thread.
     * @return WeakReference<CancellableRunnable> for the posted task.
     */
    public static <T> Future<T> postTaskAndReply(ID identifier, Callable<T> callable, Callback<T> reply)
            throws RuntimeException {
        if (!smInitialized)
            throw new RuntimeException("IMEThreadHandler need initialize() before calling postTaskAndReply!");

        Executor currentMessageLoop = null;
        long currThreadId = Thread.currentThread().getId();

        // Optimize it.
        if (currThreadId == smUIMessageLoop.getThreadId())
            currentMessageLoop = smUIMessageLoop;
        else if (currThreadId == smFileMessageLoop.getThreadId())
            currentMessageLoop = smFileMessageLoop;
        else if (currThreadId == smIOMessageLoop.getThreadId())
            currentMessageLoop = smIOMessageLoop;
        else if (currThreadId == smADHandlerThread.getThreadId())
            currentMessageLoop = smADMessageLoop;
        else
            throw new RuntimeException("IMEThreadHandler::postTaskAndReply must called on named thread!");

        RunnableFuture<T> ftask = new FutureReplyTask<>(callable, reply, currentMessageLoop);
        switch (identifier) {
            case UI:
                smUIMessageLoop.execute(ftask);
                break;

            case FILE:
                smFileMessageLoop.execute(ftask);
                break;

            case IO:
                smIOMessageLoop.execute(ftask);
                break;

            case AD:
                smADMessageLoop.execute(ftask);
        }

        return ftask;
    }

    /**
     * Check whether we are on the target thread loop.
     *
     * @param identifier Thread identifier, currently allow UI, FILE, IO.
     * @return boolean
     */
    public static boolean currentlyOn(ID identifier) throws RuntimeException {
        if (!smInitialized) {
            throw new RuntimeException("IMEThreadHandler need initialize() before calling currentlyOn");
        }

        long currThreadId = Thread.currentThread().getId();
        switch (identifier) {
            case UI:
                return smUIMessageLoop.getThreadId() == currThreadId;

            case FILE:
                return smFileMessageLoop.getThreadId() == currThreadId;

            case IO:
                return smIOMessageLoop.getThreadId() == currThreadId;

            case AD:
                return smADHandlerThread.getThreadId() == currThreadId;
        }

        return false;
    }

    /**
     * Uninitialize the message loop.
     *
     * @throws RuntimeException
     */
    public static void uninitialize() throws RuntimeException {
        if (!smInitialized)
            return;

        smUIMessageLoop.quit();

        final MessageLoop fileMessageLoopToDelete = smFileMessageLoop;
        smFileMessageLoop.execute(new Runnable() {
            @Override
            public void run() {
                fileMessageLoopToDelete.quit();
            }
        });

        final MessageLoop ioMessageLoopToDelete = smIOMessageLoop;
        smIOMessageLoop.execute(new Runnable() {
            @Override
            public void run() {
                ioMessageLoopToDelete.quit();
            }
        });

        final HandlerThread adHandlerThreadToDelete = smADHandlerThread;
        smADMessageLoop.execute(new Runnable() {
            @Override
            public void run() {
                adHandlerThreadToDelete.quit();
            }
        });

        smUIMessageLoop = null;
        smFileMessageLoop = null;
        smIOMessageLoop = null;
        smADMessageLoop = null;
        smADHandlerThread = null;

        smInitialized = false;
    }
}
