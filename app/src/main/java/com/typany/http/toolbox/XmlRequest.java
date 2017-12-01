package com.typany.http.toolbox;

import com.typany.http.NetworkResponse;
import com.typany.http.ParseError;
import com.typany.http.Request;
import com.typany.http.Response;
import com.typany.http.Response.ErrorListener;
import com.typany.http.Response.Listener;
import com.typany.http.VolleyError;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by liuzhuang on 2016/3/3.
 */
public class XmlRequest extends Request<XmlPullParser> {

    protected /*final */Listener<XmlPullParser> mListener;

    public XmlRequest(int method, String url, Listener<XmlPullParser> listener,
                      ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public XmlRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
        try {
            String xmlString = new String(response.data,
                    "utf-8");
//                    HttpHeaderParser.parseCharset(response.headers));
            response.headers.put(HTTP.CONTENT_TYPE,
                    response.headers.get("content-type"));
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));
            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(XmlPullParser response) {
        mListener.onResponse(response);
        if(mErrorListener != null){
            mErrorListener = null;
        }
        mListener = null;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if(mListener !=null) {
            mListener = null;
        }
        if(mErrorListener != null) {
            mErrorListener = null;
        }
    }

    private Priority mPriority;

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    public Priority getPriority() {
        return mPriority == null ? Priority.NORMAL : mPriority;
    }
}