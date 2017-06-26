package com.eme.mas.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.OrderAfterSaleAdapter;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.customeview.FullyLinearLayoutManager;
import com.eme.mas.customeview.StretchScrollView;
import com.eme.mas.model.entity.MyOrderBo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 售后详情页面
 * <p>
 * Created by dijiaoliang on 16/10/28.
 */
@WLayout(layoutId = R.layout.activity_after_sale_detail)
public class AftersaleDetailActivity extends BaseActivity {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.rv_after_sale_goods)
    RecyclerView rvAfterSaleGoods;
    @Bind(R.id.scrollView)
    StretchScrollView scrollView;

    private OrderAfterSaleAdapter orderGoodsAdapter;
    private List<MyOrderBo.ProductListBean> goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        goods = new ArrayList<>();
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
        goods.add(new MyOrderBo.ProductListBean());
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText(R.string.order_detail_aftersale);
        barRightTitle.setText(R.string.order_detail_contact_airline);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAfterSaleGoods.setLayoutManager(manager);
        rvAfterSaleGoods.setNestedScrollingEnabled(false);
        rvAfterSaleGoods.setHasFixedSize(true);
        orderGoodsAdapter = new OrderAfterSaleAdapter(goods, R.layout.item_order_edit_goods);
        rvAfterSaleGoods.setAdapter(orderGoodsAdapter);
        scrollView.smoothScrollBy(0,0);
    }


    @OnClick({R.id.back, R.id.bar_right_title})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bar_right_title:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "10086-1"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
