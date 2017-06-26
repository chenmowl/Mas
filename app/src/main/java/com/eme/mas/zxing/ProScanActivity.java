package com.eme.mas.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eme.mas.R;
import com.eme.mas.activity.BaseActivity;
import com.eme.mas.activity.ProDetailActivity;
import com.eme.mas.adapter.ProScanAdapter;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.model.entity.DeliveryBo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品扫描结果
 * <p>
 * Created by dijiaoliang on 16/8/24.
 */
@WLayout(layoutId = R.layout.activity_pro_scan)
public class ProScanActivity extends BaseActivity {


    @Bind(R.id.rv_scan)
    RecyclerView rvScan;

    private List<DeliveryBo> mdeliveryData;

    private ProScanAdapter mProScanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        //1.数据集
        if (mdeliveryData == null) mdeliveryData = new ArrayList<>();
        mdeliveryData.add(new DeliveryBo());
        mdeliveryData.add(new DeliveryBo());
        mdeliveryData.add(new DeliveryBo());
        //2.适配器
        if (mProScanAdapter == null)
            mProScanAdapter = new ProScanAdapter(ProScanActivity.this, mdeliveryData);
        //3.layoutmanager布局管理器
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ProScanActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScan.setLayoutManager(mLayoutManager);
        rvScan.setAdapter(mProScanAdapter);
    }

    @OnClick({R.id.ll_back,R.id.ll_product})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_product:
                startActivity(new Intent(this, ProDetailActivity.class));
                break;
        }
    }
}
