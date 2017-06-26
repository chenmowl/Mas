package com.eme.mas.connection;

import android.content.Context;

import com.android.volley.RequestQueue;

public enum ConnectionManager {

    INSTANCE {

        private RequestQueue mRequestQueue;
        private RequestQueue mMultipartQueue;

        @Override
        public RequestQueue getRequestQueue(Context context) {

            if (mRequestQueue == null) {
                mRequestQueue = KVolley.newRequestQueue(context);
            }

            return mRequestQueue;
        }

        @Override
        public RequestQueue getMultipartQueue(Context context) {

            if (mMultipartQueue == null) {
                mMultipartQueue = MultipartVolley.newRequestQueue(context);
            }

            return mMultipartQueue;
        }
    };

    public abstract RequestQueue getRequestQueue(Context context);

    public abstract RequestQueue getMultipartQueue(Context context);
}
