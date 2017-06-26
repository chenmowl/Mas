package com.eme.mas.adapter;


import android.view.View;
import android.widget.Button;
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
 * 待处理订单
 * auth:zulei
 * date:2016-9-12
 */
public class OrderProcessingAdapter extends BaseQuickAdapter<MyOrderBo> {

    public OrderProcessingAdapter(List<MyOrderBo> list) {
        super( R.layout.item_order_processing, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderBo myOrderBo) {
        //给子控件添加点击事件
        helper.addOnClickListener(R.id.btn_iop_to_evaluate);  
        helper.addOnClickListener(R.id.btn_iop_rihgt_btn);  
        //子控件初始化
        TextView tvID = helper.getView(R.id.tv_iop_id);
        TextView tvStatus = helper.getView(R.id.tv_iop_status);
        TextView tvCount = helper.getView(R.id.tv_iop_count);
        TextView tvMoney = helper.getView(R.id.tv_iop_money);
        Button btnToEvaluate = helper.getView(R.id.btn_iop_to_evaluate);
        Button btnRight = helper.getView(R.id.btn_iop_rihgt_btn);
        SimpleDraweeView sdv1 = helper.getView(R.id.sdv_iop_pic1);
        SimpleDraweeView sdv2 = helper.getView(R.id.sdv_iop_pic2);
        SimpleDraweeView sdv3 = helper.getView(R.id.sdv_iop_pic3);


        sdv1.setVisibility(View.GONE);
        sdv2.setVisibility(View.GONE);
        sdv3.setVisibility(View.GONE);

        String orderStatus = myOrderBo.getOrder_status();

        //已签收，未评价
        if("40".equals(orderStatus)||"50".equals(orderStatus)){
            tvStatus.setText("已签收，未评价");
            btnToEvaluate.setVisibility(View.VISIBLE);
            btnRight.setText("再次购买");
        }

        //待付款
        if("10".equals(orderStatus)){
            String payWayCode = myOrderBo.getPay_way_code(); //1在线支付 2货到付款
            if("1".equals(payWayCode)){
                tvStatus.setText("待付款");
                btnToEvaluate.setVisibility(View.GONE);
                btnRight.setText("去支付");
            }else{
                //FIXME 传的null 只能暂时这么处理
                tvStatus.setText("处理中(货到付款)");
                btnToEvaluate.setVisibility(View.GONE);
                btnRight.setText("再次购买");
            }

        }

        //售后处理中
        if("80".equals(orderStatus) || "100".equals(orderStatus)){
            tvStatus.setText("售后处理中");
            btnToEvaluate.setVisibility(View.GONE);
            btnRight.setText("再次购买");

        }


        tvID.setText(myOrderBo.getOrder_number());

        List<MyOrderBo.ProductListBean> listProduct =  myOrderBo.getProduct_list();
        if(listProduct.size()>=1){
            sdv1.setVisibility(View.VISIBLE);
            sdv1.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(0).getProduct_image_url()));
        }

        if(listProduct.size()>=2){
            sdv2.setVisibility(View.VISIBLE);
            sdv2.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(1).getProduct_image_url()));
        }

        if(listProduct.size()>=3){
            sdv3.setVisibility(View.VISIBLE);
            sdv3.setImageURI(UriUtil.getImage(KConfig.HOST_URL + listProduct.get(2).getProduct_image_url()));
        }

        tvCount.setText("共计"+listProduct.size()+"件");
        tvMoney.setText(myOrderBo.getTotalGoodsPrice());

    }

}
