package com.eme.mas.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 订单售后适配器
 *
 * Created by dijiaoliang on 16/10/28.
 */
public class OrderAfterSaleAdapter extends RecyclerView.Adapter<OrderAfterSaleAdapter.ViewHolder>  implements View.OnClickListener{
    public List<MyOrderBo.ProductListBean> datas;
    private int resId;
    private boolean showAll;

    public OrderAfterSaleAdapter(List datas, int resId) {
        this.datas = datas;
        this.resId = resId;
    }

    public void setDataAndNotify(List datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void openHideList(boolean showAll) {
        this.showAll = showAll;
        notifyDataSetChanged();
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resId,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MyOrderBo.ProductListBean good = datas.get(position);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(good);

        viewHolder.name.setText(good.getProduct_name());
        viewHolder.price.setText(good.getProduct_price());
        viewHolder.amount.setText(good.getProduct_num());
        viewHolder.icon.setImageURI(UriUtil.getImage(KConfig.HOST_URL + good.getProduct_image_url()));

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,specs,price,amount;
        public SimpleDraweeView icon;
        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.tv_item_oeg_name);
            specs = (TextView) view.findViewById(R.id.tv_item_oeg_specs);
            price = (TextView) view.findViewById(R.id.tv_item_oeg_price);
            amount = (TextView) view.findViewById(R.id.tv_item_oeg_amount);
            icon = (SimpleDraweeView) view.findViewById(R.id.sdv_item_oeg_icon);
        }
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, MyOrderBo.ProductListBean data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(MyOrderBo.ProductListBean)v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}