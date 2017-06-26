package com.eme.mas.customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.eme.mas.utils.Util;

/**
 * 固定最大高度的列表控件
 * <p>
 * Created by dijiaoliang on 16/8/1.
 */
public class MListView extends ListView {

    /**
     * listview高度
     */
    private int listViewHeight = 300;

    public int getListViewHeight() {
        return listViewHeight;
    }

    public void setListViewHeight(int listViewHeight) {
        this.listViewHeight = listViewHeight;
    }

    public MListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        listViewHeight = dm.heightPixels - Util.dip2px(context, 84);
    }

    public MListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        listViewHeight = (dm.heightPixels - Util.dip2px(context, 84)) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (listViewHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight,
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
