package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.model.entity.MyAddressBo;

import java.util.List;

/**
 * 分类页面的酒分类适配器
 * <p>
 * Created by dijiaoliang on 16/7/21.
 */
public class MyCommonAddressAdapter extends WBaseAdapter<MyAddressBo> {
    public MyCommonAddressAdapter(Context context, List<MyAddressBo> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    public void getItemView(int position, View convertView) {
        String area = getList().get(position).getAreaPath();
        String address = getList().get(position).getAddress();

        TextView tvArea = ViewHolder.get(convertView, R.id.tv_name);
        tvArea.setText(area);

        TextView tvAddress = ViewHolder.get(convertView, R.id.tv_address);
        tvArea.setText(address);
    }

}
