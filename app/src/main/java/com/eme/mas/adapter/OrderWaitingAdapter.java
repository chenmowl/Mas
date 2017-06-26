package com.eme.mas.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 等待收货列表适配器
 * Created by zulei on 16/8/6.
 */
public class OrderWaitingAdapter extends WBaseAdapter<MyOrderBo> {
    private Handler mHandler;
    private int flag;
    private int color_main_color_red;
    private int color_hint_text_gray;

    public OrderWaitingAdapter(Context context, List<MyOrderBo> list, int itemLayout, Handler mHandler,int flag) {
        super(context, list, itemLayout);
        this.mHandler = mHandler;
        color_main_color_red = ContextCompat.getColor(context, R.color.main_color_red);
        color_hint_text_gray = ContextCompat.getColor(context, R.color.hint_text_gray);
    }

    public OrderWaitingAdapter(Context context, List<MyOrderBo> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    public void setFlagAndNotify(int flag) {
        this.flag = flag;
        notifyDataSetChanged();
    }

    @Override
    public void getItemView(final int position, View convertView) {
        TextView tvCount = ViewHolder.get(convertView, R.id.tv_item_wf_count);
        TextView tvPrice = ViewHolder.get(convertView, R.id.tv_item_wf_price);
        TextView tvID = ViewHolder.get(convertView, R.id.tv_item_wf_id);
        SimpleDraweeView icon = ViewHolder.get(convertView, R.id.sdv_item_wf_icon);

        ImageView ivRefreshIcon = ViewHolder.get(convertView, R.id.iv_item_wf_refresh_icon);
        TextView tvRefreshText = ViewHolder.get(convertView, R.id.tv_item_wf_refresh_text);
        if (position == flag) {
            ivRefreshIcon.setImageResource(R.mipmap.order_shuaxin);
            tvRefreshText.setTextColor(color_main_color_red);
        } else {
            ivRefreshIcon.setImageResource(R.mipmap.order_shuaxin_normal);
            tvRefreshText.setTextColor(color_hint_text_gray);
        }


        final MyOrderBo myOrderBo = getList().get(position);

        LinearLayout llRefreshMap = ViewHolder.get(convertView, R.id.ll_item_wf_refresh_map);
        llRefreshMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = myOrderBo;
                msg.arg1 = position;
                mHandler.sendMessage(msg);
            }
        });


        tvID.setText(myOrderBo.getOrder_number());
        List<MyOrderBo.ProductListBean> listProduct = myOrderBo.getProduct_list();
        if (listProduct.size() >= 1) {
            //icon.setVisibility(View.VISIBLE);
            icon.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(0).getProduct_image_url()));
        }
        tvCount.setText("共计" + listProduct.size() + "件");
        tvPrice.setText(myOrderBo.getTotalGoodsPrice());



    }

    @Override
    public int getCount() {
        if (null == getList()) {
            return 0;
        } else {
            return getList().size();
        }


    }
}
