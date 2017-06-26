package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by AllenCoder on 2016/8/03.
 * A convenience class to extend when you only want to OnItemChildClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 **/

public abstract class OnItemChildClickListener extends SimpleClickListener {

    public OnItemChildClickListener() {

    }
    public OnItemChildClickListener(Mode mode) {
        super(mode);
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(RecyclerView.Adapter adapter,View parent, View view, int position) {
        SimpleOnItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemChildLongClick(RecyclerView.Adapter adapter, View view, int position) {

    }

    public  abstract void SimpleOnItemChildClick(RecyclerView.Adapter adapter, View view, int position);

}
