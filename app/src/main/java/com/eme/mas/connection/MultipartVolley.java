package com.eme.mas.connection;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.Volley;
import com.eme.mas.connection.multipart.MultiPartStack;


/**
 * KVolley is one custom implement of Volley{#Volley},KVolley use
 * {@link NoCache} instead of {@link com.android.volley.toolbox.DiskBasedCache}.
 */
public class MultipartVolley extends Volley {

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "kvolley";

    private static MyBasicNetwork network;

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack   An {@link HttpStack} to use for the network, or null for
     *                default.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, Cache cache, HttpStack stack) {
        // File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        String userAgent = "kvolley/0";
        VolleyLog.setTag("KVolley");
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (NameNotFoundException e) {
        }
        // CookieStore is just an interface, you can implement it and do things like
        // save the cookies to disk or what ever.
//        CookieStore cookieStore = new MyCookieStore(WonderWorld.mConext);
//        CookieManager manager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL );
//        CookieHandler.setDefault(manager);

        // Optionally, you can just use the default CookieManager
        //CookieManager manager = new CookieManager();
        //CookieHandler.setDefault(manager);

        if (stack == null) {

            stack = new MultiPartStack();
        }

        network = new MyBasicNetwork(stack);

        RequestQueue queue;
        if (cache == null) {
            queue = new RequestQueue(new NoCache(), network, 5);
        } else {
            queue = new RequestQueue(cache, network);
        }
        queue.start();

        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, Cache cache) {
        return newRequestQueue(context, cache, null);
    }

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null, null);
    }

//    public static void clearCookie() {
//        network.clearCookie();
//    }
}
