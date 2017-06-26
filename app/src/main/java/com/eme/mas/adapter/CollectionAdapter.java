package com.eme.mas.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickSwipeAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eme.mas.R;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.ProductBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 我的收藏
 * auth:zulei
 * date:2016-9-7
 */
public class CollectionAdapter extends BaseQuickSwipeAdapter<ProductBo> {

    private int validBlackColor,validRedColor,invalidGrayColor,validBgWhite,invalidBgGray;

    public CollectionAdapter(Context context, List<ProductBo> list) {
        super( R.layout.item_collection, list);
        validBlackColor = ContextCompat.getColor(context,R.color.text_color_bar);
        validRedColor = ContextCompat.getColor(context,R.color.main_color_red);
        invalidGrayColor = ContextCompat.getColor(context,R.color.cart_spec_tv);
        validBgWhite = ContextCompat.getColor(context,R.color.white);
        invalidBgGray = ContextCompat.getColor(context,R.color.activity_bg);


    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBo productBo) {

        //给子控件添加点击事件
        helper.addOnClickListener(R.id.iv_ic_add);
        helper.addOnClickListener(R.id.tv_ic_delete);

        //子控件初始化
        //LinearLayout llItem = helper.getView(R.id.ll_ic_item);
        SimpleDraweeView sdvIcon = helper.getView(R.id.sdv_product_pic);
        TextView tvInvalid = helper.getView(R.id.tv_ic_invalid_good);
        TextView tvName = helper.getView(R.id.tv_ic_name);
        TextView tvSpec = helper.getView(R.id.tv_ic_spec);
        TextView tvPrice = helper.getView(R.id.tv_ic_price);
        ImageView ivPhoneOnly = helper.getView(R.id.iv_ic_only_phone);
        ImageView ivAdd = helper.getView(R.id.iv_ic_add);

        String channel = productBo.getProduct_channel(); //1普通，2手机专享
        if("2".equals(channel)){
            ivPhoneOnly.setVisibility(View.VISIBLE);
        }else{
            ivPhoneOnly.setVisibility(View.GONE);
        }
        String goodShow = productBo.getGoods_show(); //1有效，0无效
        if ("1".equals(goodShow)) {
            //有效商品
            //llItem.setBackgroundColor(validBgWhite);
            helper.setBackgroundColor(R.id.ll_ic_item,validBgWhite);
            tvInvalid.setVisibility(View.GONE);
            tvName.setTextColor(validBlackColor);
            tvPrice.setTextColor(validRedColor);
            ivPhoneOnly.setImageResource(R.mipmap.cart_iv_phone);
            ivAdd.setVisibility(View.VISIBLE);

        } else {
            //无效商品
            //llItem.setBackgroundColor(invalidBgGray);
            helper.setBackgroundColor(R.id.ll_ic_item,invalidBgGray);
            tvInvalid.setVisibility(View.VISIBLE);
            tvName.setTextColor(invalidGrayColor);
            tvPrice.setTextColor(invalidGrayColor);
            ivPhoneOnly.setImageResource(R.mipmap.cart_iv_phone); //FIXME 改成灰色图标
            ivAdd.setVisibility(View.GONE);

        }

        //子控件赋值
        sdvIcon.setImageURI(UriUtil.getImage(KConfig.HOST_URL + productBo.getProduct_image_url()));
        tvName.setText(productBo.getProduct_name());
        tvSpec.setText(productBo.getSubtitle());
        tvPrice.setText(TextUtils.concat("¥",productBo.getProduct_price()));


    }

    //绑定swipe
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.l_swipe;
    }
}
