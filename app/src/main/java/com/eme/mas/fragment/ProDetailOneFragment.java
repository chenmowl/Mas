package com.eme.mas.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.eme.mas.R;
import com.eme.mas.activity.LoginActivity;
import com.eme.mas.activity.ProDetailActivity;
import com.eme.mas.adapter.ProDetailHotAdapter;
import com.eme.mas.adapter.ProDetailRateAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.AddCartHotGoodInterface;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.IncludeListView;
import com.eme.mas.customeview.convenentbanner.NetworkImageHolderView;
import com.eme.mas.customeview.dialog.ProShareDialog;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.LoadingEvent;
import com.eme.mas.model.AddCartResult;
import com.eme.mas.model.GoodsCollectionResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.GoodsDetailVo;
import com.eme.mas.model.entity.GoodsEvaluateContentBo;
import com.eme.mas.model.entity.GoodsLabelShowBo;
import com.eme.mas.model.entity.ScoreFlagBo;
import com.eme.mas.model.entity.SkuAttributesSaleBo;
import com.eme.mas.model.entity.home.HomeHotGoodsBo;
import com.eme.mas.utils.ListUtil;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.StringUtil;
import com.eme.mas.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情页第一部分：商品模块
 * <p/>
 * Created by dijiaoliang on 16/8/5.
 */
@WLayout(layoutId = R.layout.fragment_pro_detail_one)
public class ProDetailOneFragment extends BaseFragment implements ViewPager.OnPageChangeListener, AddCartHotGoodInterface {

    private final static String TAG = ProDetailOneFragment.class.getSimpleName();
    public final static int LOGIN_REQUEST_CODE = 1009;

    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.tv_product_name)
    TextView tvProductName;
    @Bind(R.id.tv_spec)
    TextView tvSpec;
    @Bind(R.id.iv_collection)
    ImageView ivCollection;
    @Bind(R.id.ll_collection)
    LinearLayout llCollection;
    @Bind(R.id.ll_share)
    LinearLayout llShare;
    @Bind(R.id.tv_pro_price)
    TextView tvProPrice;
    @Bind(R.id.tv_retail_price)
    TextView tvRetailPrice;
    @Bind(R.id.btn_add_cart)
    TextView btnAddCart;
    @Bind(R.id.tv_salesVolume)
    TextView tvSalesVolume;
    @Bind(R.id.tv_favorableRate)
    TextView tvFavorableRate;
    @Bind(R.id.ll_praise)
    LinearLayout llPraise;
    @Bind(R.id.lv_rate)
    IncludeListView lvRate;
    @Bind(R.id.btn_allevaluate)
    TextView btnAllevaluate;
    @Bind(R.id.tv_hot_title)
    TextView tvHotTitle;
    @Bind(R.id.vp_hot)
    ViewPager vpHot;
    @Bind(R.id.strip_hot01)
    View stripHot01;
    @Bind(R.id.strip_hot02)
    View stripHot02;
    @Bind(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @Bind(R.id.iv_pro_detail)
    ImageView ivProDetail;
    @Bind(R.id.tv_collection)
    TextView tvCollection;
    @Bind(R.id.tv_title_rate)
    TextView tvTitleRate;
    @Bind(R.id.tv_send_time)
    TextView tvSendTime;

    private IProduct mProController;//商品控制器
    private ICart mCartController;//购物车控制器

    private List<String> imageUrls;//banner轮播图资源数据

    private ProDetailRateAdapter mProDetailRateAdapter;//这是商品评价的适配器
    private List<GoodsEvaluateContentBo> evaluateData;


    private LoadingEvent openEvent;
    private LoadingEvent closeEvent;

    private int color_red;
    private int color_black;
    private int color_gray;

    /**
     * 热门推荐
     */
    private List<HomeHotGoodsBo> hotProData;
    private List<View> vList;
    private ProDetailHotAdapter mProDetailHotAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ((BaseImpl) mProController).cancelRequestByTag(TAG);
        ((BaseImpl) mCartController).cancelRequestByTag(TAG);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //这里做一些初始化操作
        imageUrls = new ArrayList<>();
        evaluateData = new ArrayList<>();
        hotProData = new ArrayList<>();
        vList = new ArrayList<>();
        mCartController = mController.getIAccount(this);//初始化控制器
        mProController = mController.getProduct(this);
        openEvent = new LoadingEvent("1");
        closeEvent = new LoadingEvent("0");
        color_black = getActivity().getResources().getColor(R.color.text_color_bar);
        color_red = getActivity().getResources().getColor(R.color.main_color_red);
        color_gray = getActivity().getResources().getColor(R.color.text_color_gray02);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProDetailRateAdapter = new ProDetailRateAdapter(getActivity(), evaluateData, R.layout.item_pro_detail_rate);
        lvRate.setAdapter(mProDetailRateAdapter);
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imageUrls);

        /**viewpager的初始化操作*/
        vList.add(LayoutInflater.from(getActivity()).inflate(R.layout.page_prodetail_hot, null));
        vList.add(LayoutInflater.from(getActivity()).inflate(R.layout.page_prodetail_hot, null));
        mProDetailHotAdapter = new ProDetailHotAdapter(vList, hotProData, this);
        vpHot.setOffscreenPageLimit(1);
        vpHot.setAdapter(mProDetailHotAdapter);
        vpHot.setCurrentItem(0);
        stripHot01.setBackgroundResource(R.color.main_color_red);
        stripHot02.setBackgroundResource(R.color.tv_bg_gray);
        vpHot.setOnPageChangeListener(this);

        tvRetailPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线

//        SPBase.setSPBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY, true); //TODO 设置登录状态(临时添加)
    }


    @OnClick({R.id.ll_share, R.id.ll_praise, R.id.btn_allevaluate})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ll_share:
                //分享弹出框
                final ProShareDialog csDialog = new ProShareDialog();
                csDialog.setOnShareListener(new ProShareDialog.OnShareListener() {
                    @Override
                    public void share() {

                    }
                });
                csDialog.showDialog(getActivity(), null);
                break;
            case R.id.ll_praise:
                ((ProDetailActivity) getActivity()).toEvaluateFrag();
                break;
            case R.id.btn_allevaluate:
                ((ProDetailActivity) getActivity()).toEvaluateFrag();
                break;
        }
    }

    /**
     * 更新商品信息
     *
     * @param productVo
     */
    public void updateProInfo(final GoodsDetailVo productVo) {
        if (productVo == null) {
            ToastUtil.shortToast(getActivity(), R.string.toast_error_goods);
            return;
        }
        //配送时间
        if(!StringUtil.isEmpty(productVo.getSendTime())){
            tvSendTime.setText(productVo.getSendTime());
        }
        //收藏按钮的状态
        if (SPBase.getBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY)) {
            String isCollection = productVo.getIsFavorite();
            if (WValue.TRUE_STR.equals(isCollection)) {
                llCollection.setSelected(true);
                tvCollection.setTextColor(color_red);
                tvCollection.setText(R.string.goods_collected);
            }
        } else {
            llCollection.setSelected(false);
            tvCollection.setTextColor(color_black);
            tvCollection.setText(R.string.goods_collection);
        }

        //判断标签
        List<GoodsLabelShowBo> labels = productVo.getGoodsLabel();
        if (labels != null && labels.size() != 0) {
            //处理标签操作
            for (GoodsLabelShowBo label : labels) {
//                GoodsLabelShowBo
            }
            //添加手机专享的icon
            //ProductUtils.addPhoneVipIcon(getActivity(), tvProductName, productBo.getProduct_name());
        } else {
            tvProductName.setText(productVo.getSkuName());
        }

        //规格属性
        List<SkuAttributesSaleBo> saleAttibute = productVo.getSaleAttibute();
        if (!ListUtil.isEmpty(saleAttibute)) {
            tvSpec.setText(saleAttibute.get(0).getSaleName() + saleAttibute.get(0).getSaleValue());
        } else {
            tvSpec.setText(WValue.STRING_EMPTY);
        }
        //价格
        tvRetailPrice.setText(TextUtils.concat("零售价¥", productVo.getMarketPrice().setScale(2, BigDecimal.ROUND_DOWN).toString()));
        tvRetailPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
        tvProPrice.setText(TextUtils.concat("¥", productVo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString()));
        if (imageUrls.size() != 0) {
            imageUrls.clear();
        }
        List<String> images = productVo.getMoreImage();
        String imageUrl = productVo.getGoodsImage();
        imageUrls.add(imageUrl);
        if (!ListUtil.isEmpty(images)) {
            imageUrls.addAll(images);
        }
        if (imageUrls != null) {
            imageUrls.addAll(imageUrls);
            convenientBanner.notifyDataSetChanged();
        }

        llCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    if (SPBase.getBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY)) {
                        String skuId=((ProDetailActivity)getActivity()).getProduct_id();
                        if(!StringUtil.isEmpty(skuId)){
                            mProController.collecteGoods(skuId,TAG);
                        }else{
                            ToastUtil.shortToast(getActivity(),R.string.toast_error_goods);
                        }
                    } else {
                        //TODO 跳转登录界面
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, LOGIN_REQUEST_CODE);
                    }
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.net_error);
                }
            }
        });

        //添加商品到购物车
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**判断网络状态*/
//                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
//                    mProductBo = productBo;
//                    EventBus.getDefault().post(openEvent);
//                } else {
//                    ToastUtil.shortToast(getActivity(), "当前网络不可用");
//                }
            }
        });

        //刷新好评率和评价数
        ScoreFlagBo scoreFlagBo = productVo.getScoreFlag();
        if (scoreFlagBo != null) {
            tvSalesVolume.setText("评价(" + scoreFlagBo.getTotalPoints() + ")");
            if (0 == scoreFlagBo.getFavorableRate().doubleValue()) {
                tvTitleRate.setVisibility(View.GONE);
                tvFavorableRate.setTextColor(color_gray);
                tvFavorableRate.setText(getText(R.string.goods_no_rate));
            } else {
                tvTitleRate.setVisibility(View.VISIBLE);
                tvFavorableRate.setTextColor(color_red);
                tvFavorableRate.setText(TextUtils.concat(String.valueOf(((int) scoreFlagBo.getFavorableRate().doubleValue() * 00)), "%"));

            }
        }

        //商品评价列表
        List<GoodsEvaluateContentBo> goodsEvaluateContentBos = productVo.getGoodsEvaluate();
        if (ListUtil.isEmpty(goodsEvaluateContentBos)) {
            if (evaluateData.size() != 0)
                evaluateData.clear();
            evaluateData.addAll(goodsEvaluateContentBos);
            mProDetailRateAdapter.notifyDataSetChanged();
        }
        //热门推荐
        List<HomeHotGoodsBo> hotGoods = productVo.getHotGoods();
        if (ListUtil.isEmpty(hotGoods)) {
            if (hotProData.size() != 0)
                hotProData.clear();
            hotProData.addAll(hotGoods);
            mProDetailHotAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                stripHot01.setBackgroundResource(R.color.main_color_red);
                stripHot02.setBackgroundResource(R.color.tv_bg_gray);
                break;
            case 1:
                stripHot02.setBackgroundResource(R.color.main_color_red);
                stripHot01.setBackgroundResource(R.color.tv_bg_gray);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private HomeHotGoodsBo mProductBo;//为了请求回调成功时保存数据库

    /**
     * 热门推荐的商品事件回调
     *
     * @param add
     * @param productBo
     */
    @Override
    public void addCart(RelativeLayout add, final HomeHotGoodsBo productBo, RelativeLayout rlPro) {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**判断网络状态*/
//                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
//                    mProductBo = productBo;
//                    mCartController.addToCart(productBo.getProduct_id(), productBo.getSpec_id(), String.valueOf(1), "1".equals(productBo.getProduct_channel()) ? WValue.PHONE_VIP : WValue.PHONE_NORMAL, TAG);
//                } else {
//                    ToastUtil.shortToast(getActivity(), R.string.net_error);
//                }
            }
        });
        rlPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            ((ProDetailActivity) getActivity()).getProData();
        }
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch (result.getAction()) {
            case WAction.ADD_TO_SHOPPING_CART:
                String cartId = ((AddCartResult) result).getData();
                if (cartId != null) {
                    //保存购物车数据到数据库
                    if (mProductBo != null) {
//                        SqlUtil.addCartData(cartId, mProductBo);
                        ToastUtil.shortToast(getActivity(), "添加购物车成功");
                        ((ProDetailActivity) getActivity()).updateBottomState();
                        EventBus.getDefault().post(closeEvent);
                    }
                }
                break;
            case WAction.CANCEL_COLLECTE_PRODUCT:
                ToastUtil.shortToast(getActivity(), "取消收藏");
                llCollection.setSelected(false);
                break;
            case WAction.COLLECTE_PRODUCT:
                ToastUtil.shortToast(getActivity(), "收藏商品");
                llCollection.setSelected(true);
                break;

            case WAction.GOODS_COLLECTION:
                //这里收藏或取消收藏成功，需要对界面进行更新
                GoodsCollectionResult.DataBean dataBean=((GoodsCollectionResult)result).getData();
                if(WValue.collection_confirm.equals(dataBean.getKey())){
                    llCollection.setSelected(true);
                    tvCollection.setTextColor(color_red);
                    tvCollection.setText(R.string.goods_collected);
                }else{
                    llCollection.setSelected(false);
                    tvCollection.setTextColor(color_black);
                    tvCollection.setText(R.string.goods_collection);
                }
                ToastUtil.shortToast(getActivity(),dataBean.getValue());
                break;

        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        String msg=WValue.STRING_EMPTY;
        switch (result.getAction()) {
            case WAction.ADD_TO_SHOPPING_CART:
                if(StringUtil.isEmpty(result.getMsg())){
                    msg=getText(R.string.toast_error_add_cart).toString();
                }
                EventBus.getDefault().post(closeEvent);//关闭Loading图
                break;
            case WAction.CANCEL_COLLECTE_PRODUCT:
//                ToastUtil.shortToast(getActivity(), "取消收藏失败");
                break;
            case WAction.COLLECTE_PRODUCT:
//                ToastUtil.shortToast(getActivity(), "收藏商品失败");
                break;
            case WAction.GOODS_COLLECTION:
                if(StringUtil.isEmpty(result.getMsg())){
                    msg=getText(R.string.toast_error_collection).toString();
                }
                break;
        }
        ToastUtil.shortToast(getActivity(), msg);
    }

}
