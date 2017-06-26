package com.eme.mas.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eme.mas.R;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.entity.OrderCouponBo;

import java.util.List;

/**
 * 订单优惠券适配器
 * Created by zulei on 16/8/10.
 */
public class OrderCouponAdapter extends BaseQuickAdapter<OrderCouponBo> {

    private int color_coupon_purple;
    private int color_coupon_yellow;
    private int color_coupon_green;

    public OrderCouponAdapter(Context context, List<OrderCouponBo> list) {
        super( R.layout.item_coupon, list);
        color_coupon_purple= ContextCompat.getColor(context,R.color.coupon_purple);
        color_coupon_yellow=ContextCompat.getColor(context,R.color.coupon_yellow);
        color_coupon_green=ContextCompat.getColor(context,R.color.coupon_green);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderCouponBo item) {
        if(item==null){
            return;
        }
        ImageView ivTitleBg = helper.getView(R.id.iv_item_coupon_title_bg);//顶部色彩条
        TextView tvPrice = helper.getView(R.id.tv_item_coupon_price);//优惠价格或折扣
        TextView tvUnit = helper.getView(R.id.tv_item_coupon_unit);//单位
        TextView tvType = helper.getView( R.id.tv_item_coupon_type);//优惠券类型名称
        TextView tvHint = helper.getView(R.id.tv_item_coupon_hint);//优惠券说明
        TextView tvValidTime = helper.getView(R.id.tv_item_coupon_valid_time);//开始截止时间
        TextView tvValidRule = helper.getView(R.id.tv_item_coupon_rule);//优惠券规则
        ImageView ivBia = helper.getView(R.id.iv_item_count_bia);   //如果优惠券不可用,显示此层

        switch (item.getActiveScene()){
            case WValue.COUPON_ACTIVE_SCENE_REGIST:
                ivTitleBg.setBackgroundResource(R.color.coupon_purple);
                tvPrice.setTextColor(color_coupon_purple);
                tvUnit.setTextColor(color_coupon_purple);
                tvType.setText(R.string.COUPON_NAME_REGIST);
                break;
            case WValue.COUPON_ACTIVE_SCENE_SHARE:
                ivTitleBg.setBackgroundResource(R.color.coupon_yellow);
                tvPrice.setTextColor(color_coupon_yellow);
                tvUnit.setTextColor(color_coupon_yellow);
                tvType.setText(R.string.COUPON_NAME_SHARE);
                break;
            case WValue.COUPON_ACTIVE_SCENE_SYSTEM:
                ivTitleBg.setBackgroundResource(R.color.coupon_green);
                tvPrice.setTextColor(color_coupon_green);
                tvUnit.setTextColor(color_coupon_green);
                tvType.setText(R.string.COUPON_NAME_SYSTEM);
                break;
            default:
                break;
        }
        switch (item.getCouponType()){
            case WValue.COUPON_TYPE_Z:
                tvUnit.setText(R.string.coupon_zhe);
                break;
            case WValue.COUPON_TYPE_Y:
                tvUnit.setText(R.string.order_edit_money_unit);
                break;
            case WValue.COUPON_TYPE_D:
                tvUnit.setText(R.string.order_edit_money_unit);
                break;
            case WValue.COUPON_TYPE_L:
                tvUnit.setText(R.string.order_edit_money_unit);
                break;
        }
        tvPrice.setText(item.getCouponAmount());
        tvHint.setText(TextUtils.concat("满",item.getConsumptionAmount(),"元可用"));
        tvValidTime.setText(TextUtils.concat(item.getStartTime(),"-",item.getEndTime()));
        tvValidRule.setText(item.getUseRule());
//        String enable = item.getEnable();//1-可用，0-不可用
//        if("1".equals(enable)){
//            ivBia.setVisibility(View.GONE);
//        }else{
//            ivBia.setVisibility(View.VISIBLE);
//        }

    }

}
