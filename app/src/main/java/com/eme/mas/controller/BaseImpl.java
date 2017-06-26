package com.eme.mas.controller;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.eme.mas.MasApplication;
import com.eme.mas.connection.ActionListener;
import com.eme.mas.connection.ConnectionManager;
import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.multipart.ActionRequest;
import com.eme.mas.connection.multipart.MultiPartStringRequest;
import com.eme.mas.model.Result;
import com.eme.mas.utils.LogUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luokaiwen on 15/5/8.
 * <p/>
 * 基础实现类
 */
public class BaseImpl {

    ConnectionManager mConnectionManager;
    RequestQueue mRequestQueue;
    RequestQueue mRequestQueueMultipart;
    WeakReference<IActionListener> iActionListener;//弱引用包装请求监听

    DefaultRetryPolicy mDefaultRetryPolicy;//超时时间的相关类

    public BaseImpl(IActionListener iActionListener) {
        mConnectionManager = ConnectionManager.INSTANCE;
        mRequestQueue = MasApplication.mRequestQueue;
        mRequestQueueMultipart = MasApplication.mMultipartQueue;
        this.iActionListener = new WeakReference<IActionListener>(iActionListener);

        mDefaultRetryPolicy = new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    }

    protected void request(final String action, HashMap<String, String> params, String tag) {
        ActionRequest actionRequest = new ActionRequest(action, params, new ActionListener<Result>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Result result = new Result();
                result.setAction(action);
                if(iActionListener.get() != null){
                    iActionListener.get().onActionFail(result);
                }

            }

            @Override
            public void onResponse(Result response) {
                if (response != null) {
                    if (response.getSuccess().equals("true")) {
                        LogUtil.e("code", response.getCode());
//                        iActionListener.onActionSucc(response);
                        if(iActionListener.get() != null){
                            iActionListener.get().onActionSucc(response);
                        }

                    } else {
//                        iActionListener.onActionFail(response);
                        if(iActionListener.get() != null){
                            iActionListener.get().onActionFail(response);
                        }

                    }
                } else {
                    Result result = new Result();
                    result.setAction(action);
//                    iActionListener.onActionFail(result);
                    if(iActionListener.get() != null){
                        iActionListener.get().onActionFail(result);
                    }

                }
            }
        });

        actionRequest.setRetryPolicy(mDefaultRetryPolicy);//设置超时时间

        mRequestQueue.add(actionRequest).setTag(tag);
    }

    protected void requestMultipart(String action, final HashMap<String, String> params, final HashMap<String, File> fileParams, String tag) {

        MultiPartStringRequest actionRequest = new MultiPartStringRequest(action, params, new ActionListener<Result>() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Result result = new Result();
                result.setAction("NetError");

//                ResultState resultState = new ResultState();
//                resultState.setErrcode("99999");
//                resultState.setStateMessage("");
                //TODO  后续需要处理
                //result.setState(resultState);

//                result.setErrcode("99999");

//                iActionListener.onActionFail(result);
                if(iActionListener.get() != null){
                    iActionListener.get().onActionFail(result);
                }

            }

            @Override
            public void onResponse(Result response) {

                if (response.getSuccess().equals("true")) {
                    LogUtil.e("code", response.getCode());
//                    iActionListener.onActionSucc(response);
                    if(iActionListener.get() != null){
                        iActionListener.get().onActionSucc(response);
                    }

                } else {
//                    iActionListener.onActionFail(response);
                    if(iActionListener.get() != null){
                        iActionListener.get().onActionFail(response);
                    }

                }

            }
        }) {
            @Override
            public Map<String, File> getFileUploads() {
                return fileParams;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }
        };

        mRequestQueueMultipart.add(actionRequest).setTag(tag);
    }


    /**
     * 通过tag取消指定的请求
     *
     * @param tag
     */
    public void cancelRequestByTag(String tag) {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }


}
