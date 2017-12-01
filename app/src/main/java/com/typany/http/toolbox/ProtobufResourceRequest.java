package com.typany.http.toolbox;

import android.arch.lifecycle.MutableLiveData;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.typany.http.NetworkResponse;
import com.typany.network.Response;

/**
 * Created by liuyan on 17-11-13.
 */

public class ProtobufResourceRequest<T extends MessageLite> extends ResourceRequest<T> {

    private final Parser<T> mMsgParser;
    public ProtobufResourceRequest(final int method, final String url,
                                   final MutableLiveData<Response<T>> observerble, final Parser<T> parser) {
        super(method, url, observerble);
        this.mMsgParser = parser;
    }

    @Override
    protected com.typany.http.Response<T> parseNetworkResponse(NetworkResponse response) {
        T data = null;
        try {
            data = mMsgParser.parseFrom(response.data);
        } catch (InvalidProtocolBufferException e) {
        }
        return com.typany.http.Response.<T>success(data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
