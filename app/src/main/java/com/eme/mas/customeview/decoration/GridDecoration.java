package com.eme.mas.customeview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eme.mas.MasApplication;
import com.eme.mas.R;

/**
 * Recycleriew条目间距设置类
 * <p/>
 * Created by dijiaoliang on 16/9/18.
 */
public class GridDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridDecoration() {
//        this.mSpace = Util.dip2px(MasApplication.getInstance(), space);
        this.mSpace = MasApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.home_grid_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            if (parent.getChildPosition(view) == 0 || parent.getChildPosition(view) == 1) {
                outRect.set(mSpace, mSpace, 0, mSpace);
            } else {
                outRect.set(mSpace, 0, 0, mSpace);
            }
        } else {
            if (parent.getChildPosition(view) == 0 || parent.getChildPosition(view) == 1) {
                outRect.set(mSpace, mSpace, mSpace, mSpace);
            } else {
                outRect.set(mSpace, 0, mSpace, mSpace);
            }
        }
//        super.getItemOffsets(outRect, view, parent, state);

    }
}
