package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.entity.GoodsEvaluateContentBo;
import com.eme.mas.utils.StringUtil;

import java.util.List;

/**
 * 商品详情页的评价列表适配器
 * <p>
 * Created by dijiaoliang on 16/8/8.
 */
public class ProDetailRateAdapter extends WBaseAdapter<GoodsEvaluateContentBo> {

    public ProDetailRateAdapter(Context context, List<GoodsEvaluateContentBo> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    public void getItemView(int position, View convertView) {
        RatingBar star_bar = ViewHolder.get(convertView, R.id.star_bar);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_phone_number = ViewHolder.get(convertView, R.id.tv_phone_number);
        TextView tv_rate_content = ViewHolder.get(convertView, R.id.tv_rate_content);
        GoodsEvaluateContentBo proEvaluateBo = getItem(position);
        if(proEvaluateBo!=null){
            star_bar.setNumStars(5);
            star_bar.setRating(5f);
            float starStep;
            if(StringUtil.isEmpty(proEvaluateBo.getStarScore())){
                starStep=0f;
            }else{
                starStep=Float.valueOf(proEvaluateBo.getStarScore());
            }
            star_bar.setStepSize(starStep);
            tv_time.setText(StringUtil.isEmpty(proEvaluateBo.getCreateTime())? WValue.STRING_EMPTY:proEvaluateBo.getCreateTime());
            switch (proEvaluateBo.getMemberId()){
                case WValue.ZERO_STR:
                    //匿名用户
                    tv_phone_number.setText(R.string.goods_anonymous);
                    break;
                default:
                    //非匿名用户
                    tv_phone_number.setText(StringUtil.isEmpty(proEvaluateBo.getMemberId())?WValue.STRING_EMPTY:proEvaluateBo.getMemberId());
                    break;
            }
            tv_rate_content.setText(StringUtil.isEmpty(proEvaluateBo.getEvaluateContent())?WValue.STRING_EMPTY:proEvaluateBo.getEvaluateContent());
        }
    }

    @Override
    public int getCount() {
        int count = getList().size();
        count = count > 2 ? 2 : count;
        return count;
    }
}
