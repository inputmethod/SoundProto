package com.typany.http.toolbox;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.typany.http.Request;
import com.typany.network.Response;

/**
 * Created by liuyan on 17-11-13.
 */

public class RequestUtil {
    public static<T extends MessageLite> LiveData<Response<T>> observalbeRequestProtobuf(String url, Context appContext, Parser<T> parser) {
        MutableLiveData<Response<T>> observable = new MutableLiveData<>();
        Request<T> req = new ProtobufResourceRequest<T>(Request.Method.GET, url, observable, parser);
        Volley.getQueue(appContext).add(req);
        return observable;
    }
}
