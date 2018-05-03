package com.fitness.module;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Prade on 3/31/2018.
 */

public class VolleyQueue {

    private static VolleyQueue instance;
    private static Context context;
    private RequestQueue requestQueue;

    private VolleyQueue(Context context) {
        VolleyQueue.context = context;
        requestQueue = queue();
    }

    public static synchronized VolleyQueue instance(Context context) {
        if (instance == null) {
            instance = new VolleyQueue(context);
        }
        return instance;
    }

    private RequestQueue queue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void add(Request<T> req) {
        queue().add(req);
    }
}