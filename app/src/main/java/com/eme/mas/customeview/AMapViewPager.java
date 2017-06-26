package com.eme.mas.customeview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AMapViewPager extends ViewPager {

    private boolean canScroll;

    public AMapViewPager(Context context) {
        super(context);
        canScroll = false;
    }

    public AMapViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        canScroll = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canScroll) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canScroll) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        String s = v.getClass().getName();
        if(v.getClass().getName().equals("com.amap.api.maps2d.MapView")) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    public void toggleLock() {
        canScroll = !canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public boolean isCanScroll() {
        return canScroll;
    }

}
