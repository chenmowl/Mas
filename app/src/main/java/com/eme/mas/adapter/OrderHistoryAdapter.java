package com.eme.mas.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eme.mas.R;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 历史订单
 * auth:zulei
 * date:2016-9-12
 */
public class OrderHistoryAdapter extends BaseQuickAdapter<MyOrderBo> {

    public OrderHistoryAdapter(List<MyOrderBo> list) {
        super( R.layout.item_order_history, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderBo myOrderBo) {
        //给子控件添加点击事件
        helper.addOnClickListener(R.id.btn_ioh_rihgt_btn); //TO_BUY_AGAIN
        helper.addOnClickListener(R.id.ib_ioh_delete); //TO_DELETE

        //子控件初始化
        TextView tvID = helper.getView(R.id.tv_ioh_id);
        TextView tvStatus = helper.getView(R.id.tv_ioh_status);
        TextView tvCount = helper.getView(R.id.tv_ioh_count);
        TextView tvMoney = helper.getView(R.id.tv_ioh_money);
        TextView tvPrice = helper.getView(R.id.tv_ic_price);
        SimpleDraweeView pic1 = helper.getView(R.id.sdv_ioh_pic1);
        SimpleDraweeView pic2 = helper.getView(R.id.sdv_ioh_pic2);
        SimpleDraweeView pic3 = helper.getView(R.id.sdv_ioh_pic3);


        pic1.setVisibility(View.GONE);
        pic2.setVisibility(View.GONE);
        pic3.setVisibility(View.GONE);

        String orderStatus = myOrderBo.getOrder_status();
        //已完成
        if("200".equals(orderStatus) || "40".equals(orderStatus) || "50".equals(orderStatus)){
            tvStatus.setText("已完成");
        }
        //已取消
        if("510".equals(orderStatus)){
            tvStatus.setText("已取消");
        }
        //超时未支付
        if("501".equals(orderStatus)){
            tvStatus.setText("超时未支付");
        }
        //售后处理完成
        if("102".equals(orderStatus)){
            tvStatus.setText("售后处理完成");
        }


        tvID.setText(myOrderBo.getOrder_number());

        List<MyOrderBo.ProductListBean> listProduct =  myOrderBo.getProduct_list();
        if(listProduct.size()>=1){
            pic1.setVisibility(View.VISIBLE);
            pic1.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(0).getProduct_image_url()));
        }

        if(listProduct.size()>=2){
            pic2.setVisibility(View.VISIBLE);
            pic2.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(1).getProduct_image_url()));
        }

        if(listProduct.size()>=3){
            pic3.setVisibility(View.VISIBLE);
            pic3.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(2).getProduct_image_url()));
        }
        tvCount.setText("共计"+listProduct.size()+"件");
        tvMoney.setText(myOrderBo.getTotalGoodsPrice());

    }

}
