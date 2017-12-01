package com.typany.network;


import static com.typany.network.StatefulResource.Status.ERROR;
import static com.typany.network.StatefulResource.Status.LOADING;
import static com.typany.network.StatefulResource.Status.SUCCESS;

/**
 * Created by wujian on 11/8/2017.
 */

//a generic class that describes a data with a status
public class StatefulResource<T> {
    public enum Status {SUCCESS, ERROR, LOADING}
    public final Status status;
    public final T data;
    public final String message;
    public int progress = 0;
    private StatefulResource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    private StatefulResource(Status status, int progress) {
        this.status = status;
        this.progress = progress;
        this.data = null;
        this.message = "";
    }

    public static <T> StatefulResource<T> success(T data) {
        return new StatefulResource<>(SUCCESS, data, null);
    }

    public static <T> StatefulResource<T> error(String msg, T data) {
        return new StatefulResource<>(ERROR, data, msg);
    }

    public static <T> StatefulResource<T> loading(T data) {
        return new StatefulResource<>(LOADING, data, null);
    }

    public static <T> StatefulResource<T> loading(int progress) {
        return new StatefulResource<>(LOADING, progress);
    }
}