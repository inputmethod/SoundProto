package com.typany.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;


/**
 * A special FutureTask to post callback back to calling thread.
 *
 * @param <V> The type of parameter.
 */
public class FutureReplyTask<V> extends FutureTask<V> {

    /** The underlying callable; nulled out after running */
    private Callback<V> mCallback = null;
    private Executor mExecutor = null;

    public FutureReplyTask(Callable<V> callable, Callback<V> callback, Executor executor) {
        super(callable);
        if (callback == null || executor == null)
            throw new NullPointerException();
        this.mCallback = callback;
        this.mExecutor = executor;
    }

    @Override
    protected void done() {
        try {
            final V result = get();
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Callback<V> c = mCallback;
                    if (c != null) {
                        try {
                            c.callback(result);
                        } catch (Throwable ex) {
                            setException(ex);
                        }
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            // There is something wrong with the execution.
            setException(e);
        }
    }
}
