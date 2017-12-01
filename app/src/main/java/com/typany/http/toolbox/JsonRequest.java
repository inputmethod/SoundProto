/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.typany.http.toolbox;

import com.typany.http.NetworkResponse;
import com.typany.http.Request;
import com.typany.http.Response;
import com.typany.http.Response.ErrorListener;
import com.typany.http.Response.Listener;
import com.typany.http.VolleyError;

/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T> JSON type of response expected
 */
public abstract class JsonRequest<T> extends Request<T> {
    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private Listener<T> mListener;


    public JsonRequest(int method, String url, Listener<T> listener,
            ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
        mListener = null;
        if(mErrorListener != null) {
            mErrorListener = null;
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if(mListener != null) {
            mListener = null;
        }
        if(mErrorListener != null) {
            mErrorListener = null;
        }
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);


    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

}
