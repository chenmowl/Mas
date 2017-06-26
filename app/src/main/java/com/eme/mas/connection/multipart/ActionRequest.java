package com.eme.mas.connection.multipart;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.eme.mas.connection.ActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.WParser;
import com.eme.mas.model.Result;
import com.eme.mas.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luokaiwen on 15/4/27.
 * <p/>
 * 接口请求
 */
public class ActionRequest extends Request<Result> {

    protected final String TAG = getClass().getSimpleName();
    protected static final int DEFAULT_TIMEOUT_MS = 20000;
    protected static final int DEFAULT_MAX_RETRIES = 0;
    protected static final float DEFAULT_BACKOFF_MULT = 1.0f;
    protected final String action;
    protected final ActionListener<Result> listener;
    protected final Map<String, String> params;

    public ActionRequest(String action, Map<String, String> params, ActionListener<Result> listener, RetryPolicy retryPolicy) {
        super(WAction.getInstance().getMethod(action), WAction.getInstance().getUrl(action), listener);

        if (null == params) {
            params = new HashMap<String, String>();
        }

        this.action = action;
        this.listener = listener;
        this.params = params;
        setRetryPolicy(retryPolicy);
        LogUtil.log(action, (WAction.getInstance().getIndex(action)) + ": " + (WAction.getInstance().getMethod(action) == Method.GET ? "GET " : "POST ")
                + WAction.getInstance().getUrl(action) + parseParams(params));
    }

    public ActionRequest(String action, Map<String, String> params, ActionListener<Result> listener) {
        this(action, params, listener, new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> cookies = new HashMap<>();

//        cookies.put("Cookie", "JSESSIONID=aaaTYBlBIdENS9PVzqC1u");

        return super.getHeaders();
    }

    @Override
    protected Response<Result> parseNetworkResponse(NetworkResponse response) {


        //WonderWorld.get().checkSessionCookie(response.headers);
        Result parsed = null;
        String string;
        try {
            //string = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            string = new String(response.data, "utf-8");
            VolleyLog.v("orignal response is [%s]", string);
            LogUtil.e("fdas",string);

            parsed = WParser.getInstance().parse(string, WAction.getInstance().getResultType(action));
            parsed.setAction(action);

            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            string = new String(response.data);
            // VolleyLog.v("orignal response is [%s]", string);
        }

        boolean isShowResponseLog = true;

        if (!TextUtils.isEmpty(string)) {

            try {

                if (true){
                    if (isShowResponseLog) {
                        LogUtil.e(TAG, action);
                    }
                } else {
                    int length = string.length();

                    if (length > 2500) {
                        // 余数
                        int remainder = length % 2500;
                        int divisor = length / 2500;

                        if (remainder > 0) {
                            divisor += 1;
                        }

                        for (int i = 0; i < divisor; i++) {
                            if (length > (i + 1) * 2500) {
                                String stringOne = string.substring(i * 2500, (i + 1) * 2500);

                                if (isShowResponseLog) {
                                    LogUtil.log(action, (WAction.getInstance().getIndex(action)) + ": " + stringOne);
                                }
                            } else {
                                String stringOne = string.substring(i * 2500, length);
                                if (isShowResponseLog) {
                                    LogUtil.log(action, (WAction.getInstance().getIndex(action)) + ": " + stringOne);
                                }
                            }
                        }
                    } else {
                        if (isShowResponseLog) {
                            LogUtil.log(action, (WAction.getInstance().getIndex(action)) + ": " + string);
                        }
                    }
                }



                String cookie = response.headers.get("Set-Cookie");

//                if (WAction.LOGIN.equals(action)) {
//                    if (isShowResponseLog) {
//                        LogUtil.e(TAG, "登录了 cookie is " + cookie);
//                    }
//
//                    WonderWorld.setCookie(cookie);
//                }

                if (isShowResponseLog) {
                    LogUtil.e(TAG, "response cookie is " + cookie);
                }

                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                return Response.error(new ParseError(e));
            }
        }
        return null;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        switch (WAction.getInstance().getMethod(action)) {
            case Method.GET:
                return null;
            case Method.POST:
                return params;
            default:
                break;
        }
        return null;
    }

    @Override
    public String getUrl() {
        String url = WAction.getInstance().getUrl(action);
        int method = WAction.getInstance().getMethod(action);
        if (method == Method.GET) {
            if (params != null) {
                StringBuilder encodedParams = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    try {
                        encodedParams.append('&');
                        encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                        encodedParams.append('=');
                        encodedParams.append(URLEncoder.encode(entry.getValue(), getParamsEncoding()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (encodedParams.length() > 0) {
                    encodedParams=encodedParams.deleteCharAt(0);

                    url += "?" + encodedParams.toString();
                }
            }
        }
        return url;

    }

    @Override
    protected void deliverResponse(Result response) {
        listener.onResponse(response);
    }

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Deprecated
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @throws AuthFailureError
     * @deprecated Use {@link #getBody()}.
     */
    @Deprecated
    @Override
    public byte[] getPostBody() throws AuthFailureError {
        return getBody();
    }

    protected String parseParams(Map<String, String> params2) {
        if (params2 == null) {
            return "";
        }
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params2.entrySet()) {
            try {
                encodedParams.append('&');
                encodedParams.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                encodedParams.append('=');
                String value = entry.getValue();
                if (null == value) {
                    value = "";
                }
                encodedParams.append(URLEncoder.encode(value, getParamsEncoding()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        int method = WAction.getInstance().getMethod(action);
        return encodedParams.toString();
    }
}
