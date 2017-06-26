package com.eme.mas.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.controller.customeInterface.HomeAddCartInterface;
import com.eme.mas.model.entity.MessageBo;

import java.util.List;

/**
 * MessageDetailActivity页面
 * 消息适配器
 * <p>
 * Created by dijiaoliang on 16/8/2.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private HomeAddCartInterface mAddCartInterface;//添加购物车

    private List<MessageBo> mList;

    private LayoutInflater mInflater;

    public MessageAdapter(Context context, List<MessageBo> list, HomeAddCartInterface mAddCartInter) {
        mList = list;
        this.mAddCartInterface = mAddCartInter;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_message_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageBo productBo = mList.get(position);
//        holder.tv_pro_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.sdv_pro.setImageURI(UriUtil.getImage(KConfig.HOST_URL + productBo.getProductDetail().getProduct_image_url()));
//        holder.tv_pro_name.setText(productBo.getProductDetail().getProduct_name());
//        holder.tv_vip_price.setText(productBo.getPhone_price());
//        holder.tv_pro_price.setText(productBo.getProductDetail().getProduct_price());
//        mAddCartInterface.addCart(holder.add, productBo);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cv_message;
        public TextView tv_time;
        public TextView tv_info;

        public ViewHolder(View view) {
            super(view);
            cv_message = (CardView) view.findViewById(R.id.cv_message);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_info = (TextView) view.findViewById(R.id.tv_info);
        }
    }

    /**
     * 这个方法是用来判断多类型条目的集合列表
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
