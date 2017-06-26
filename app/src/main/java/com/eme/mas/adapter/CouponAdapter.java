package com.eme.mas.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eme.mas.R;
import com.eme.mas.model.entity.CouponBo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单优惠券适配器
 * Created by zulei on 16/8/10.
 */
public class CouponAdapter extends BaseQuickAdapter<CouponBo> {

    private int color_coupon_purple;
    private int color_coupon_yellow;
    private int color_coupon_green;

    public CouponAdapter(Context context,List<CouponBo> list) {
        super( R.layout.item_coupon, list);
        color_coupon_purple= ContextCompat.getColor(context,R.color.coupon_purple);
        color_coupon_yellow=ContextCompat.getColor(context,R.color.coupon_yellow);
        color_coupon_green=ContextCompat.getColor(context,R.color.coupon_green);
    }


    @Override
    protected void convert(BaseViewHolder helper, CouponBo item) {

        ImageView ivTitleBg = helper.getView(R.id.iv_item_coupon_title_bg);
        TextView tvPrice = helper.getView(R.id.tv_item_coupon_price);
        TextView tvValidTime = helper.getView(R.id.tv_item_coupon_valid_time);
        TextView tvType = helper.getView( R.id.tv_item_coupon_type);
        TextView tvHint = helper.getView(R.id.tv_item_coupon_hint);
        TextView tvUnit = helper.getView(R.id.tv_item_coupon_unit);
        ImageView ivBia = helper.getView(R.id.iv_item_count_bia);

        int useRangeProductType = item.getUseRangeProductType(); //优惠券的使用范围：1指定商品2指定商品分类3全部商品

        if(useRangeProductType == 1){
            ivTitleBg.setBackgroundResource(R.color.coupon_purple);
            tvPrice.setTextColor(color_coupon_purple);
            tvUnit.setTextColor(color_coupon_purple);
        }
        if(useRangeProductType == 2){
            ivTitleBg.setBackgroundResource(R.color.coupon_yellow);
            tvPrice.setTextColor(color_coupon_yellow);
            tvUnit.setTextColor(color_coupon_yellow);

        }
        if(useRangeProductType == 3){
            ivTitleBg.setBackgroundResource(R.color.coupon_green);
            tvPrice.setTextColor(color_coupon_green);
            tvUnit.setTextColor(color_coupon_green);
        }
        tvPrice.setText(item.getUseDiscountAmount()+"");
        if(item.getUseDiscountAmount()>999){
            tvPrice.setTextSize(40);
        }else if(item.getUseDiscountAmount()>99){
            tvPrice.setTextSize(50);
        }else{
            tvPrice.setTextSize(60);
        }

        tvType.setText(item.getCouponName());
        tvHint.setText("全场满"+item.getUseLimitAmount()+"元可用");

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy.MM.dd");
        Date start = new Date(item.getUseStartTime());
        Date end = new Date(item.getUseEndTime());
        tvValidTime.setText(sdf.format(start)+"-"+sdf.format(end));

        String enable = item.getEnable();//1-可用，0-不可用
        if("1".equals(enable)){
            ivBia.setVisibility(View.GONE);
        }else{
            ivBia.setVisibility(View.VISIBLE);
        }

    }

}
