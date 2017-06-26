package com.eme.mas.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eme.mas.R;
import com.eme.mas.model.entity.MyAddressBo;

import java.util.List;

/**
 * Created by zulei on 16/7/29.
 */
public class MyAddressAdapter extends BaseQuickAdapter<MyAddressBo> {

    public MyAddressAdapter(List<MyAddressBo> list) {
        super( R.layout.item_my_address, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, MyAddressBo myAddressBo) {
        //设置子控件点击事件
        helper.addOnClickListener(R.id.ll_item_my_address_delete);
        helper.addOnClickListener(R.id.ll_item_my_address_edit);


        //子控件赋值
        helper.setText(R.id.tv_item_my_address_name,myAddressBo.getName());
        helper.setText(R.id.tv_item_my_address_phone,myAddressBo.getMobile());
        helper.setText(R.id.tv_item_my_address_area,myAddressBo.getAreaPath());
        helper.setText(R.id.tv_item_my_address_address,myAddressBo.getAddress());


    }

}

