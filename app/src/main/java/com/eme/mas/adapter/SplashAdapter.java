package com.eme.mas.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * splash页面的适配器
 * <p>
 * Created by dijiaoliang on 16/6/14.
 */
public class SplashAdapter extends PagerAdapter {

    private List<View> mList;

    public SplashAdapter(List<View> vList) {
        this.mList = vList;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        container.removeView(mList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(mList.get(position));
        return mList.get(position);
    }
}
