package com.rohindh.trivia.controller;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Appcontroller extends Application {
    public  static final String TAG = Appcontroller.class.getSimpleName();
    private static Appcontroller instance;
    private RequestQueue requestQueue;

    public static synchronized Appcontroller getInstance() {
//        if (instance == null) {
//            instance = new Appcontroller(context);
//        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag) ? tag : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }
}

