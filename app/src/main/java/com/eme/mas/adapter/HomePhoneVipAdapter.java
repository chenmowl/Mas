package com.eme.mas.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.controller.customeInterface.HomeAddCartInterface;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.home.HomeMobileVipBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Fragment页面
 * 手机专享模块的适配器
 * <p/>
 * Created by dijiaoliang on 16/8/2.
 */
public class HomePhoneVipAdapter extends RecyclerView.Adapter<HomePhoneVipAdapter.ViewHolder> {

    private HomeAddCartInterface mAddCartInterface;//添加购物车

    private List<HomeMobileVipBo> mList;

    private LayoutInflater mInflate;

    public HomePhoneVipAdapter(Context context, List<HomeMobileVipBo> list, HomeAddCartInterface mAddCartInter) {
        mList = list;
        this.mAddCartInterface = mAddCartInter;
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_home_mobilevip, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HomeMobileVipBo productBo = mList.get(position);
        holder.tv_pro_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.sdv_pro.setImageURI(UriUtil.getImage(KConfig.HOST_URL + productBo.getGoodsImg()));
        holder.tv_pro_name.setText(productBo.getGoodsName());
        holder.tv_vip_price.setText(productBo.getSalePrice());
        holder.tv_pro_price.setText(productBo.getLabelPrice());
        mAddCartInterface.addCart(holder.add, productBo);

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

//    @Override
//    public void onClick(View v) {
//        mItemClickListener.onItemClick(v);
//    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView sdv_pro;
        public TextView tv_pro_name;
        public TextView tv_vip_price;
        public TextView tv_pro_price;
        public ImageView add;

        public ViewHolder(View view) {
            super(view);
            add = (ImageView) view.findViewById(R.id.add);
            tv_pro_price = (TextView) view.findViewById(R.id.tv_pro_price);
            tv_vip_price = (TextView) view.findViewById(R.id.tv_vip_price);
            tv_pro_name = (TextView) view.findViewById(R.id.tv_pro_name);
            sdv_pro = (SimpleDraweeView) view.findViewById(R.id.sdv_pro);
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
