package com.eme.mas.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;

import java.util.List;

/**
 * Created by zulei on 16/8/8.
 */
public class EvaluateGoodsAdapter extends RecyclerView.Adapter<EvaluateGoodsAdapter.ViewHolder> { // implements View.OnClickListener
    private List<MyOrderBo.ProductListBean> datas;
    private int resId;
    private String[] mContents;
    private float[] mStars;

    public EvaluateGoodsAdapter( List<MyOrderBo.ProductListBean> datas, int resId) {
        this.datas = datas;
        this.resId = resId;
        this.mContents = new String[datas.size()];
        this.mStars = new float[datas.size()];
        for(int i=0;i<mStars.length;i++){
            mStars[i] = 5.0f;
        }
    }

    public String[] getmContents(){
        return mContents;
    }

    public float[] getmStars(){
        return mStars;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resId,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        //view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(datas.get(position));

        MyOrderBo.ProductListBean goods =  datas.get(position);
        viewHolder.sdvIcon.setImageURI(UriUtil.getImage(KConfig.HOST_URL + goods.getProduct_image_url()));
        viewHolder.name.setText(goods.getProduct_name());
        viewHolder.star.setStar(5);

        viewHolder.star.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                mStars[position] = ratingCount;
            }
        });

        viewHolder.content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContents[position] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //解决edittext无法滚动问题
        viewHolder.content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }
        });

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        if(null == datas){
            return 0;
        }else{
            return datas.size();

        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdvIcon;
        private TextView name;
        private RatingBar star;
        private EditText content;
        public ViewHolder(View view){
            super(view);
            sdvIcon = (SimpleDraweeView) view.findViewById(R.id.sdv_item_evaluate_icon);
            name = (TextView) view.findViewById(R.id.tv_item_evaluate_name);
            star = (RatingBar) view.findViewById(R.id.rbar_item_evaluate_evaluate);
            content = (EditText) view.findViewById(R.id.et_item_evaluate_comment);
        }
    }

}