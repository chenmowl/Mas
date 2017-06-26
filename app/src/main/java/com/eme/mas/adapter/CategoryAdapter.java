package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.CategoryFilterResult;

import java.util.List;


/**
 * 分类页面的酒分类适配器
 * <p/>
 * Created by dijiaoliang on 16/7/21.
 */
public class CategoryAdapter extends WBaseAdapter<CategoryFilterResult.DataBean.CategoryConditionBean> {

//    private String wine = "";

    private int text_color_normal;
    private int text_color_selecte;

    public CategoryAdapter(Context context, List<CategoryFilterResult.DataBean.CategoryConditionBean> list, int itemLayout) {
        super(context, list, itemLayout);
        text_color_normal = context.getResources().getColor(R.color.text_color_bar);
        text_color_selecte = context.getResources().getColor(R.color.main_color_red);
    }

    @Override
    public void getItemView(int position, View convertView) {
        CategoryFilterResult.DataBean.CategoryConditionBean cate = getList().get(position);
        TextView tv_cate_name = ViewHolder.get(convertView, R.id.tv_cate_name);
        tv_cate_name.setText(cate.getName());
        if(WValue.TRUE_STR.equals(cate.getIsDefault())){
            tv_cate_name.setTextColor(text_color_selecte);
        }else{
            tv_cate_name.setTextColor(text_color_normal);
        }
//        if (wine.equals(cate.getName())) {
//            tv_cate_name.setTextColor(text_color_selecte);
//        } else {
//            tv_cate_name.setTextColor(text_color_normal);
//        }
    }

    /**
     * 设置选中的位置
     *
     * @param wineName
     */
//    public void setWineName(String wineName) {
//        if (wineName != null)
//            this.wine = wineName;
//    }
}
