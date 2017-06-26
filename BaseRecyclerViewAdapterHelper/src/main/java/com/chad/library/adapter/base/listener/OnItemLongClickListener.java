package com.chad.library.adapter.base.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {

    public OnItemLongClickListener() {

    }
    public OnItemLongClickListener(Mode mode) {
        super(mode);
    }


    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(RecyclerView.Adapter adapter, View view, int position) {
        SimpleOnItemLongClick( adapter,  view,  position);
    }

    @Override
    public void onItemChildClick(RecyclerView.Adapter adapter,View parent, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(RecyclerView.Adapter adapter, View view, int position) {
    }
    public abstract void SimpleOnItemLongClick(RecyclerView.Adapter adapter, View view, int position);
}
