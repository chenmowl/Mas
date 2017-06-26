package com.eme.mas.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.eme.mas.R;

import java.util.ArrayList;


/**
 * Created by simiao on 2015/5/20.
 */
public class SearchPlaceAdapter extends BaseAdapter {

    private ArrayList<PoiItem> items;
    public SearchPlaceAdapter(ArrayList<PoiItem> items) {
        super();
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_place, null);
            viewHold = new ViewHold();
            viewHold.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHold.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        PoiItem item = (PoiItem) getItem(position);

        if(null!=item){
            if(null!=item.getTitle()){
                viewHold.tvName.setText(item.getTitle());
            }

            viewHold.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            if(!TextUtils.isEmpty(item.getCityName())){
                viewHold.tvAddress.setText(item.getCityName()+item.getSnippet());
            }else {
                viewHold.tvAddress.setText(item.getSnippet());
            }

        }
        return convertView;
    }

    class ViewHold {
        TextView tvName;
        TextView tvAddress;
    }

}
