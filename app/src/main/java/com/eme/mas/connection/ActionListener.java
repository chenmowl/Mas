package com.eme.mas.connection;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * Use one listener to handle response.<br/>
 * 合并两个接口,统一处理
 */
public interface ActionListener<T> extends Listener<T>, ErrorListener {
    /**
     * 处理异常
     */
    @Override
    public void onErrorResponse(VolleyError error);

    /**
     * 正常返回
     */
    @Override
    public void onResponse(T response);

}
