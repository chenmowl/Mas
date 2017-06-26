package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.controller.customeInterface.SearchAddCartInterface;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.SearchGoodsBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 搜索结果适配器
 * <p>
 * Created by dijiaoliang on 16/7/21.
 */
public class SearchResultAdapter extends WBaseAdapter<SearchGoodsBo> {

    private SearchAddCartInterface addCartNormalInterface;

    public SearchResultAdapter(Context context, List<SearchGoodsBo> list, int itemLayout, SearchAddCartInterface addCartNormalInterface) {
        super(context, list, itemLayout);
        this.addCartNormalInterface=addCartNormalInterface;
    }

    @Override
    public void getItemView(final int position, View convertView) {
        SearchGoodsBo productBo = getItem(position);
        SimpleDraweeView sdv_pro = ViewHolder.get(convertView, R.id.sdv_pro);
        TextView tv_pro_name = ViewHolder.get(convertView, R.id.tv_pro_name);
        TextView tv_pro_price = ViewHolder.get(convertView, R.id.tv_pro_price);
        ImageView add = ViewHolder.get(convertView, R.id.add);
        sdv_pro.setImageURI(UriUtil.getImage(KConfig.HOST_URL + productBo.getGoodsImage()));
        tv_pro_name.setText(productBo.getGoodsName());
        tv_pro_price.setText(productBo.getSalePrice());
        addCartNormalInterface.addCart(add,productBo);

    }
}
