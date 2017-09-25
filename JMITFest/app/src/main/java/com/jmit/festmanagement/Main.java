package com.jmit.festmanagement;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by arpitkh996 on 06-09-2016.
 */


import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jmit.festmanagement.utils.ExtHttpClientStack;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * Created by arpitkh996 on 06-09-2016.
 */

public class Main extends Application {
    private RequestQueue mRequestQueue;
    private static Main mInstance;
    public static synchronized Main getInstance() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();

        final ClientConnectionManager mClientConnectionManager = mDefaultHttpClient
                .getConnectionManager();
        final HttpParams mHttpParams = mDefaultHttpClient.getParams();
        final ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager(
                mHttpParams, mClientConnectionManager.getSchemeRegistry());

        mDefaultHttpClient = new DefaultHttpClient(
                mThreadSafeClientConnManager, mHttpParams);

        mDefaultHttpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        mDefaultHttpClient.setCookieStore(new BasicCookieStore());
        final HttpStack httpStack = new ExtHttpClientStack(
                mDefaultHttpClient); // using Apache Client

        this.mRequestQueue = Volley.newRequestQueue(
                getApplicationContext(), httpStack);


    }
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag("tag");
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll("tag");
    }

}
