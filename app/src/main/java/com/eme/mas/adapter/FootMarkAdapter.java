package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.data.sql.DataRow;
import com.eme.mas.environment.KConfig;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 商品详情页面
 * <p>
 * 足迹适配器
 * <p>
 * Created by dijiaoliang on 16/7/21.
 */
public class FootMarkAdapter extends WBaseAdapter<DataRow> {


//    private int text_color_normal;
//    private int text_color_selecte;

    public FootMarkAdapter(Context context, List<DataRow> list, int itemLayout) {
        super(context, list, itemLayout);
//        text_color_normal = context.getResources().getColor(R.color.text_color_bar);
//        text_color_selecte = context.getResources().getColor(R.color.main_color_red);
    }

    @Override
    public void getItemView(int position, View convertView) {
        //// TODO: 16/8/9 初始化item布局
        DataRow row = getItem(position);
        SimpleDraweeView sdv = ViewHolder.get(convertView, R.id.sdv_product_pic);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_pro_name);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_pro_price);
        String imageUrl=row.getString("product_image_url");
        sdv.setImageURI(UriUtil.getImage(KConfig.HOST_URL + imageUrl));
        tv_name.setText(row.getString("product_name"));
        tv_price.setText(row.getString("product_price"));
    }

}
