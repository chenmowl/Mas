package com.eme.mas.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.model.entity.DeliveryBo;

import java.util.List;

/**
 * 扫描结果页面，配送信息的适配器
 * <p/>
 * Created by dijiaoliang on 16/8/2.
 */
public class ProScanAdapter extends RecyclerView.Adapter<ProScanAdapter.ViewHolder> {


    private List<DeliveryBo> mList;

    private LayoutInflater mInflate;

    public ProScanAdapter(Context context, List<DeliveryBo> list) {
        mList = list;
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_scan_delivery, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DeliveryBo productBo = mList.get(position);
        int size = mList.size();
        if (size > 0 && (size - 1) == position) {
            holder.line_bottom.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title;
        public TextView tv_time;
        public TextView tv_info01;
        public TextView tv_info02;
        public View line_bottom;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_info01 = (TextView) view.findViewById(R.id.tv_info01);
            tv_info02 = (TextView) view.findViewById(R.id.tv_info02);
            line_bottom = (View) view.findViewById(R.id.line_bottom);
        }
    }

    private OnRecyclerItemClickListener mItemClickListener;//条目监听对象

    /**
     * 给条目设置点击事件
     *
     * @param itemClickListener
     */
    public void setOnRecyclerClickListener(OnRecyclerItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * 点击事件的回调接口类
     */
    public interface OnRecyclerItemClickListener {
        void onItemClick(View v, int position);
    }

}
