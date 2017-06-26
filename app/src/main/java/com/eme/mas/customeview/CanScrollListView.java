package com.eme.mas.customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Nick on 16/7/29.
 */
public class CanScrollListView extends ListView{
    public CanScrollListView(Context context) {
        super(context);
    }

    public CanScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,

                                   int scrollY, int scrollRangeX, int scrollRangeY,

                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,

                scrollRangeX, scrollRangeY, maxOverScrollX,

                200, isTouchEvent);

    }

}
