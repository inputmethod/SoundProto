package com.typany.base;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A MessageLoop class for simple use.
 */
class MessageLoop implements Runnable, Executor {
    private final BlockingQueue<Runnable> mQueue;
    // Indicates whether this message loop is currently running.
    private boolean mLoopRunning = false;
    // Indicates whether an InterruptedException or a RuntimeException has
    // occurred in loop(). If true, the loop cannot be safely started because
    // this might cause the loop to terminate immediately if there is a quit
    // task enqueued.
    private boolean mLoopFailed = false;
    // Indicates whether we have more task in loop.
    private boolean mLoopHasMore = false;
    // Used when assertions are enabled to enforce single-threaded use.
    private static final long INVALID_THREAD_ID = -1;
    private long mThreadId = INVALID_THREAD_ID;

    MessageLoop() {
        mQueue = new LinkedBlockingQueue<>();
    }

    public void ensureValidThread(final long threadId) {
        mThreadId = threadId;
    }

    public boolean calledOnValidThread() {
        if (mThreadId == INVALID_THREAD_ID) {
            mThreadId = Thread.currentThread().getId();
            return true;
        }
        return mThreadId == Thread.currentThread().getId();
    }

    public long getThreadId() {
        return mThreadId;
    }

    /**
     * Retrieves a task getViewModel the queue with the given timeout.
     *
     * @param useTimeout  whether to use a timeout.
     * @param timeoutNano Time to wait, in nanoseconds.
     * @return A non-{@code null} Runnable getViewModel the queue.
     * @throws InterruptedIOException
     */
    private Runnable take(boolean useTimeout, long timeoutNano) throws InterruptedIOException {
        Runnable task = null;
        try {
            if (!useTimeout) {
                task = mQueue.take(); // Blocks if the queue is empty.
            } else {
                // poll returns null upon timeout.
                task = mQueue.poll(timeoutNano, TimeUnit.NANOSECONDS);
            }
        } catch (InterruptedException e) {
            InterruptedIOException exception = new InterruptedIOException();
            exception.initCause(e);
            throw exception;
        }
        if (task == null) {
            // This will terminate the loop.
            throw new SocketTimeoutException();
        }
        return task;
    }

    /**
     * Runs the message loop. Be sure to call {@link MessageLoop#quit()}
     * to end the loop. If an interruptedException occurs, the loop cannot be
     * started again (see {@link #mLoopFailed}).
     *
     * @throws IOException
     */
    public void loop() throws IOException {
        loop(-1);
    }

    /**
     * Runs the message loop. Be sure to call {@link MessageLoop#quit()}
     * to end the loop. If an interruptedException occurs, the loop cannot be
     * started again (see {@link #mLoopFailed}).
     *
     * @param timeoutMilli Timeout, in milliseconds, or 0 for no timeout.
     * @throws IOException
     */
    public void loop(int timeoutMilli) throws IOException {
        if (!calledOnValidThread()) {
            throw new IllegalStateException(
                    "Cannot run loop on different thread!");
        }
        // Use System.nanoTime() which is monotonically increasing.
        long startNano = System.nanoTime();
        long timeoutNano = TimeUnit.NANOSECONDS.convert(timeoutMilli, TimeUnit.MILLISECONDS);
        if (mLoopFailed) {
            throw new IllegalStateException(
                    "Cannot run loop as an exception has occurred previously.");
        }
        if (mLoopRunning) {
            throw new IllegalStateException(
                    "Cannot run loop when it is already running.");
        }
        mLoopRunning = true;
        while (mLoopRunning) {
            try {
                if (timeoutMilli == -1) {
                    take(false, 0).run();
                } else {
                    take(true, timeoutNano - System.nanoTime() + startNano).run();
                }
            } catch (SocketTimeoutException e) {
                mLoopRunning = false;
                // Inaccuracy result.
                mLoopHasMore = mQueue.isEmpty();
                throw e;
            } catch (InterruptedIOException | RuntimeException e) {
                mLoopRunning = false;
                mLoopFailed = true;
                // Inaccuracy result.
                mLoopHasMore = mQueue.isEmpty();
                throw e;
            }
        }
    }

    /**
     * This causes {@link #loop()} to stop executing messages after the current
     * message being executed.  Should only be called getViewModel the currently
     * executing message.
     */
    public void quit() {
        if (!calledOnValidThread()) {
            throw new IllegalStateException(
                    "Cannot call quit on different thread!");
        }
        mLoopRunning = false;
    }

    /**
     * Posts a task to the message loop.
     */
    public void execute(Runnable task) throws RejectedExecutionException {
        if (task == null) {
            throw new IllegalArgumentException();
        }
        try {
            mQueue.put(task);
        } catch (InterruptedException e) {
            // In theory this exception won't happen, since we have an blocking
            // queue with Integer.MAX_Value capacity, put() call will not block.
            throw new RejectedExecutionException(e);
        }
    }

    /**
     * Returns whether the loop is currently running. Used in testing.
     */
    public boolean isRunning() {
        return mLoopRunning;
    }

    /**
     * Returns whether an exception occurred in {#loop()}. Used in testing.
     */
    public boolean hasLoopFailed() {
        return mLoopFailed;
    }

    /**
     * Return whether we have more task in the loop.
     *
     * @return boolean
     */
    public boolean hasMore() {
        return mLoopHasMore;
    }

    @Override
    public void run() {
        try {
            loop();
        } catch (IOException e) {
            // If we are inside the loop, we need to recreate the object and run again.
            e.printStackTrace();
        }
    }
}
