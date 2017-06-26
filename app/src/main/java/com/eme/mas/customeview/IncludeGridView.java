package com.eme.mas.customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by lvhonghe on 14/11/29.
 */
public class IncludeGridView extends GridView {

    public IncludeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IncludeGridView(Context context) {
        super(context);
    }

    public IncludeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
