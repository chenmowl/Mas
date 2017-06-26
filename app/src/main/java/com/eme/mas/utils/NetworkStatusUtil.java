package com.eme.mas.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatusUtil {



    /**
     * 检测网络是否可用
     * @return
     */
    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager	mConnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (null == mConnManager)
                return false;
            NetworkInfo networkInfo = mConnManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

//	public static final int NETTYPE_WIFI = 0x01;
//	public static final int NETTYPE_CMWAP = 0x02;
//	public static final int NETTYPE_CMNET = 0x03;
//	public int getNetworkType() {
//		int netType = 0;
//
//		NetworkInfo networkInfo = mConnManager.getActiveNetworkInfo();
//		if (networkInfo == null) {
//			return netType;
//		}
//		int nType = networkInfo.getType();
//		if (nType == ConnectivityManager.TYPE_MOBILE) {
//			String extraInfo = networkInfo.getExtraInfo();
//			if(!extraInfo.isEmpty()){
//				if (extraInfo.toLowerCase().equals("cmnet")) {
//					netType = NETTYPE_CMNET;
//				} else {
//					netType = NETTYPE_CMWAP;
//				}
//			}
//		} else if (nType == ConnectivityManager.TYPE_WIFI) {
//			netType = NETTYPE_WIFI;
//		}
//		return netType;
//	}


}
