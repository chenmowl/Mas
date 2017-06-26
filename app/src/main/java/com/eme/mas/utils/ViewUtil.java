package com.eme.mas.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.eme.mas.R;
import com.eme.mas.customeview.SystemBarTintManager;

/**
 * Created by dijiaoliang on 16/6/7.
 */
public class ViewUtil {
    //private static View viewAV;
    /**
     * 得到的高度是px值
     *
     * @param view
     * @return
     */
    public static int measureHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取listview中item高度
     *在android系统版本在17级以下（包含17的时候）。使用measure会出现NULL异常情况，这个是一个BUG。
     * 原因是在RelativeLayout的控件使用在含有scrolling的时候，该含有scrolling的控件中计算空间大小的时候，
     * 没有使用MeasureSpec mode UNSPECIFIED的布局方式在RelativeLayout。
     * 自定义的控件则会尽可能的使用AT_MOST 来替换对齐方式。
     * 如果你想解决这个问题有2个方法：
     * 1.讲SDK的目标版本升级
     * 2.将需要使用RelativeLayout的上层包一个LinearLayout即可
     */
    public static int measureItemHeight(ListView listView, BaseAdapter adapter) {
        View listItem = adapter.getView(0, null, listView);
        listItem.measure(0, 0); // 计算子项View 的宽高
        int oneItemHeight = listItem.getMeasuredHeight() + listView.getDividerHeight();
        return oneItemHeight;
    }

    /**
     * 设置状态栏颜色
     * @param activity
     */
    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, activity);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.activity_bg);//通知栏所需颜色
    }

    public static void setStatusBarColorTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, activity);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.fulltransparent);//通知栏所需颜色
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on,Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 显示转菊花
     */
    /*public static View showProgressGray(Activity activity){
        viewZZ = LayoutInflater.from(activity).inflate(R.layout.layout_zhezhao_gray, null);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(viewZZ, param);
        return  viewZZ;
    }*/

    /*public static View showProgressAVTransparent(Activity activity){
        if(null!=viewAV){
            deleteProgressAV(viewAV);
        }
        viewAV = LayoutInflater.from(activity).inflate(R.layout.network_loading_av_bg_transparent_44, null);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            param.setMargins(0, Util.dip2px(activity, 20), 0, 0);
        }
        activity.addContentView(viewAV, param);
        return  viewAV;
    }

    public static View showProgressAVNotTransparent(Activity activity){
        if(null!=viewAV){
            deleteProgressAV(viewAV);
        }
        viewAV = LayoutInflater.from(activity).inflate(R.layout.network_loading_av_bg_nottransparent_44, null);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            param.setMargins(0, Util.dip2px(activity, 20), 0, 0);
        }
        activity.addContentView(viewAV, param);
        return  viewAV;
    }*/

    /*public static View showProgress(Activity activity){
        viewZZ = LayoutInflater.from(activity).inflate(R.layout.network_loading, null);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.setMargins(0, Util.dip2px(activity, 0), 0, Util.dip2px(activity,49));

        activity.addContentView(viewZZ, param);
        return viewZZ;
    }*/

    /**
     * 删除转菊花
     */
    /*public static void deleteProgressAV(View viewAV){
        try {
            if(viewAV!=null){
                ViewGroup vg = (ViewGroup) viewAV.getParent();
                vg.removeView(viewAV);
            }
        }catch (Exception e){

        }

    }*/



}
