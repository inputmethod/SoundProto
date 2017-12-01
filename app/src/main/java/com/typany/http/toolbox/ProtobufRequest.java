package com.typany.http.toolbox;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.typany.http.NetworkResponse;
import com.typany.http.Request;
import com.typany.http.Response;

/**
 * Created by liuyan on 17-11-7.
 */

public class ProtobufRequest<T extends MessageLite> extends Request<T> {

    private Parser<T> mParser;
    private Response.Listener<T> mResponseListener;
    /**
     * Create a Request object for protobuf data
     * @param method GET / POST
     * @param url  r
     * @param listener
     */

    public ProtobufRequest(int method, String url, Response.ErrorListener listener, Parser<T> parser) {
        super(method, url, listener);
        mParser = parser;
    }

    public ProtobufRequest(int method, String url, Response.Listener<T> respListener, Response.ErrorListener errListener, Parser<T> parser) {
        this(method, url, errListener, parser);
        mResponseListener = respListener;
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        T msg = null;
        try {
            msg = mParser.parseFrom(response.data);
        } catch (InvalidProtocolBufferException e) {
        }
        return Response.success(msg, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(T response) {
        mResponseListener.onResponse(response);
    }
}
