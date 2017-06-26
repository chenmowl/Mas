package com.eme.mas.adapter;

import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.controller.customeInterface.AddCartHotGoodInterface;
import com.eme.mas.environment.KConfig;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.entity.home.HomeHotGoodsBo;
import com.eme.mas.utils.StringUtil;
import com.eme.mas.utils.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情页 热门推荐的适配器
 * <p/>
 * Created by dijiaoliang on 16/8/17.
 */
public class ProDetailHotAdapter extends PagerAdapter {

    private List<View> vList;//控件集合
    private List<HomeHotGoodsBo> mProData;
    private AddCartHotGoodInterface mAddCartInterface;//添加购物车的回调

    public ProDetailHotAdapter(List<View> vList, List<HomeHotGoodsBo> proData, AddCartHotGoodInterface addCartInterface) {
        this.vList = vList;
        this.mProData = proData;
        mAddCartInterface = addCartInterface;
    }

    @Override
    public int getCount() {
        return vList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //实例化初始化一个页卡
        View v = vList.get(position);
        RelativeLayout rl_pro = (RelativeLayout) v.findViewById(R.id.rl_pro);
        RelativeLayout rl_pro02 = (RelativeLayout) v.findViewById(R.id.rl_pro02);
        RelativeLayout rl_pro03 = (RelativeLayout) v.findViewById(R.id.rl_pro03);
        SimpleDraweeView sdv_pro = (SimpleDraweeView) v.findViewById(R.id.sdv_pro);
        TextView tv_pro_name = (TextView) v.findViewById(R.id.tv_pro_name);
        TextView tv_vip_price = (TextView) v.findViewById(R.id.tv_vip_price);
        TextView tv_pro_price = (TextView) v.findViewById(R.id.tv_pro_price);
        tv_pro_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
        RelativeLayout add = (RelativeLayout) v.findViewById(R.id.add);
        SimpleDraweeView sdv_pro02 = (SimpleDraweeView) v.findViewById(R.id.sdv_pro02);
        TextView tv_pro_name02 = (TextView) v.findViewById(R.id.tv_pro_name02);
        TextView tv_vip_price02 = (TextView) v.findViewById(R.id.tv_vip_price02);
        TextView tv_pro_price02 = (TextView) v.findViewById(R.id.tv_pro_price02);
        tv_pro_price02.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
        RelativeLayout add02 = (RelativeLayout) v.findViewById(R.id.add02);
        SimpleDraweeView sdv_pro03 = (SimpleDraweeView) v.findViewById(R.id.sdv_pro03);
        TextView tv_pro_name03 = (TextView) v.findViewById(R.id.tv_pro_name03);
        TextView tv_vip_price03 = (TextView) v.findViewById(R.id.tv_vip_price03);
        TextView tv_pro_price03 = (TextView) v.findViewById(R.id.tv_pro_price03);
        tv_pro_price03.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
        RelativeLayout add03 = (RelativeLayout) v.findViewById(R.id.add03);
        int size = mProData.size();
        HomeHotGoodsBo bo;
        switch (position) {
            case 0:
                if (size > 0) {
                    bo = mProData.get(0);
                    sdv_pro.setImageURI(UriUtil.getImage(KConfig.HOST_URL + bo.getGoodsImage()));
                    tv_pro_name.setText(StringUtil.isEmpty(bo.getSkuName())?WValue.STRING_EMPTY:bo.getSkuName());
                    tv_vip_price.setText("¥ " + bo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    tv_pro_price.setText("¥ " + bo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    mAddCartInterface.addCart(add, bo,rl_pro);
                }
                if (size > 1) {
                    bo = mProData.get(1);
                    sdv_pro02.setImageURI(UriUtil.getImage(KConfig.HOST_URL + bo.getGoodsImage()));
                    tv_pro_name02.setText(StringUtil.isEmpty(bo.getSkuName())?WValue.STRING_EMPTY:bo.getSkuName());
                    tv_vip_price02.setText("¥ " + bo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    tv_pro_price02.setText("¥ " + bo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    mAddCartInterface.addCart(add02, bo,rl_pro02);
                }
                if (size > 2) {
                    bo = mProData.get(2);
                    sdv_pro03.setImageURI(UriUtil.getImage(KConfig.HOST_URL + bo.getGoodsImage()));
                    tv_pro_name03.setText(StringUtil.isEmpty(bo.getSkuName())?WValue.STRING_EMPTY:bo.getSkuName());
                    tv_vip_price03.setText("¥ " + bo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    tv_pro_price03.setText("¥ " + bo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    mAddCartInterface.addCart(add03, bo,rl_pro03);
                }
                break;
            case 1:
                if (size > 3) {
                    bo = mProData.get(3);
                    sdv_pro.setImageURI(UriUtil.getImage(KConfig.HOST_URL + bo.getGoodsImage()));
                    tv_pro_name.setText(StringUtil.isEmpty(bo.getSkuName())?WValue.STRING_EMPTY:bo.getSkuName());
                    tv_vip_price.setText("¥ " + bo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    tv_pro_price.setText("¥ " + bo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    mAddCartInterface.addCart(add, bo,rl_pro);
                }
                if (size > 4) {
                    bo = mProData.get(4);
                    sdv_pro02.setImageURI(UriUtil.getImage(KConfig.HOST_URL + bo.getGoodsImage()));
                    tv_pro_name02.setText(StringUtil.isEmpty(bo.getSkuName())?WValue.STRING_EMPTY:bo.getSkuName());
                    tv_vip_price02.setText("¥ " + bo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    tv_pro_price02.setText("¥ " + bo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    mAddCartInterface.addCart(add02, bo,rl_pro02);
                }
                if (size > 5) {
                    bo = mProData.get(5);
                    sdv_pro03.setImageURI(UriUtil.getImage(KConfig.HOST_URL + bo.getGoodsImage()));
                    tv_pro_name03.setText(StringUtil.isEmpty(bo.getSkuName())?WValue.STRING_EMPTY:bo.getSkuName());
                    tv_vip_price03.setText("¥ " + bo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    tv_pro_price03.setText("¥ " + bo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                    mAddCartInterface.addCart(add03, bo,rl_pro03);
                }
                break;
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //销毁一个页卡
        container.removeView(vList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
