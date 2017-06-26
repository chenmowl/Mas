package com.handmark.pulltorefresh.library.extras;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 *  自定义ScrollView可以监听滑动事件
 *
 * Created by z on 2016/6/2.
 */
public class CompatibleScrollView extends ScrollView {

    private onMyScrollChangeListener mOnMyScrollChangeListener;

    public CompatibleScrollView(Context context) {
        super(context);
    }

    public CompatibleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatibleScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
//        if(t + getHeight() >=  computeVerticalScrollRange()){
//            //ScrollView滑动到底部了
//            mOnMyScrollChangeListener.scrollBottom();
//        }
//        if(t+getHeight()<=t){
//            mOnMyScrollChangeListener.onScrollChange();
//        }
        super.onScrollChanged(l, t, oldl, oldt);
        mOnMyScrollChangeListener.onScrollChange();
    }

//
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
//    }

    public void setOnMyScrollChangeListener(onMyScrollChangeListener onMyScrollChangeListener){
        this.mOnMyScrollChangeListener = onMyScrollChangeListener;
    }

    public interface onMyScrollChangeListener {
        public void onScrollChange();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }

}
