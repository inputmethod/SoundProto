package com.typany.network;

/**
 * Created by wujian on 11/9/2017.
 */

public class Response<ResultType> {
    public ResultType body;
    private boolean succuss = false;
    private boolean done = false;
    public String errorMessage;
    public int progress = 0;

    public static<ResultType> Response<ResultType> success(final ResultType data) {
        return new Response<ResultType>(true, true, null, 100, data);
    }

    public static<ResultType> Response<ResultType> error(final String errMsg) {
        return new Response<ResultType>(false, true, errMsg, 0, null);
    }

    public static<ResultType> Response<ResultType> progress(final int currentProgress) {
       return new Response<ResultType>(false, false, null, currentProgress, null);
    }

    public boolean isSuccessful() {
        return succuss;
    }

    public boolean done() {
        return done;
    }

    private Response(final boolean succuss, final boolean done, final String errorMessage,
                     final int progress, final ResultType body) {
        this.body = body;
        this.progress = progress;
        this.errorMessage = errorMessage;
        this.done = done;
        this.succuss = succuss;
    }
}
