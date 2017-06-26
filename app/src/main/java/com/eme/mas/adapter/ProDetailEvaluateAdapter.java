package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.entity.GoodsEvaluateContentBo;
import com.eme.mas.utils.ListUtil;
import com.eme.mas.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 商品详情页的评价列表适配器(评价的Fragment)
 * <p>
 * Created by dijiaoliang on 16/8/8.
 */
public class ProDetailEvaluateAdapter extends WBaseAdapter<GoodsEvaluateContentBo> {

    public ProDetailEvaluateAdapter(Context context, List<GoodsEvaluateContentBo> list, int itemLayout) {
        super(context, list, itemLayout);
    }

    @Override
    public void getItemView(int position, View convertView) {
        SimpleDraweeView sdv_head = ViewHolder.get(convertView, R.id.sdv_head);
        RatingBar rating_bar_evaluate = ViewHolder.get(convertView, R.id.rating_bar_evaluate);
        TextView tv_number_name = ViewHolder.get(convertView, R.id.tv_number_name);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_evaluate_content = ViewHolder.get(convertView, R.id.tv_evaluate_content);
        TextView tv_response = ViewHolder.get(convertView, R.id.tv_response);
        GoodsEvaluateContentBo proEvaluateBo = getItem(position);
        if (proEvaluateBo != null) {
            //头像
//            sdv_head.setImageURI(UriUtil.getImage(KConfig.HOST_URL + proEvaluateBo.get));
            //用户等级
            float starStep;
            if(StringUtil.isEmpty(proEvaluateBo.getStarScore())){
                starStep=0f;
            }else{
                starStep=Float.valueOf(proEvaluateBo.getStarScore());
            }
            rating_bar_evaluate.setStepSize(starStep);
            //用户名
            switch (proEvaluateBo.getMemberId()){
                case WValue.ZERO_STR:
                    //匿名用户
                    tv_number_name.setText(R.string.goods_anonymous);
                    break;
                default:
                    //非匿名用户
                    tv_number_name.setText(StringUtil.isEmpty(proEvaluateBo.getMemberId())?WValue.STRING_EMPTY:proEvaluateBo.getMemberId());
                    break;
            }
            //评价时间
            tv_time.setText(proEvaluateBo.getCreateTime());
            //评价内容
            tv_evaluate_content.setText(proEvaluateBo.getEvaluateContent());

            //客服回复
            List<GoodsEvaluateContentBo.EvaluateReply> evaluateReply=proEvaluateBo.getEvaluateReply();
            if(!ListUtil.isEmpty(evaluateReply)){
                tv_response.setVisibility(View.VISIBLE);
                tv_response.setText(evaluateReply.get(0).getReplyContent());
            }else{
                tv_response.setVisibility(View.GONE);
            }
        }
    }

}
