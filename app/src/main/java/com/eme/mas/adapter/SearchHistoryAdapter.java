package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;

import java.util.List;

/**
 * 搜索历史适配器（搜索页面）
 * <p>
 * Created by dijiaoliang on 16/7/4.
 */
public class SearchHistoryAdapter extends WBaseAdapter<String> {


    public SearchHistoryAdapter(Context context, List<String> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    public void getItemView(final int position, View convertView) {
        TextView tv_history = ViewHolder.get(convertView, R.id.history_content);
        tv_history.setText(getList().get(position));
    }
}
