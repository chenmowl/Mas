package com.eme.mas.customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by lvhonghe on 14/11/29.
 */
public class IncludeListView extends ListView {

    public IncludeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IncludeListView(Context context) {
        super(context);
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
