package com.eme.mas.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by luokaiwen on 15/4/28.
 * <p/>
 * 适配器基类
 */
public abstract class WBaseAdapter<T> extends BaseAdapter {

    private List<T> mList;
    public LayoutInflater mInflater;
    private final int itemLayout;

    public WBaseAdapter(Context context, List<T> list, int itemLayout) {
        this.mInflater = LayoutInflater.from(context);
        this.setList(list);
        this.itemLayout = itemLayout;
    }

    @Override
    public int getCount() {
        return getList().size();
    }

    @Override
    public T getItem(int position) {
        return getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(itemLayout, null);
        }
        getItemView(position, convertView);
        return convertView;
    }

    /**
     * example: ImageView bananaView = ViewHolder.get(convertView,R.id.banana);<br/>
     *
     * @param position
     * @param convertView
     */
    public abstract void getItemView(int position, View convertView);

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
    }

    public static class ViewHolder {

        @SuppressWarnings("unchecked")
        public static <V extends View> V get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (V) childView;
        }
    }
}
