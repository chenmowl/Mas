package com.eme.mas.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.eme.mas.MainActivity;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.activity.BrandActivity;
import com.eme.mas.activity.CategoryActivity;
import com.eme.mas.activity.ProDetailActivity;
import com.eme.mas.activity.SearchActivity;
import com.eme.mas.activity.SearchResultActivity;
import com.eme.mas.adapter.HomeHotGoodsAdapter;
import com.eme.mas.adapter.HomePhoneVipAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.HomeAddCartInterface;
import com.eme.mas.controller.customeInterface.HomeAddCartNormalInterface;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.BadgeView;
import com.eme.mas.customeview.IncludeGridView;
import com.eme.mas.customeview.convenentbanner.ConvenHolderView;
import com.eme.mas.customeview.dialog.AirlinesDialog;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.data.sql.DataRow;
import com.eme.mas.data.sql.DataTable;
import com.eme.mas.data.sql.QueryBuilder;
import com.eme.mas.environment.KConfig;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.RefreshCartEvent;
import com.eme.mas.model.AddCartResult;
import com.eme.mas.model.CartListResult;
import com.eme.mas.model.CategoryResult;
import com.eme.mas.model.HomeRecResult;
import com.eme.mas.model.HomeResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.CartBo;
import com.eme.mas.model.entity.CategorySpecVo;
import com.eme.mas.model.entity.home.HomeBannerBo;
import com.eme.mas.model.entity.home.HomeBrandBo;
import com.eme.mas.model.entity.home.HomeCategoryBo;
import com.eme.mas.model.entity.home.HomeHotGoodsBo;
import com.eme.mas.model.entity.home.HomeMobileVipBo;
import com.eme.mas.model.entity.home.HomeRecListVo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.eme.mas.utils.UriUtil;
import com.eme.mas.utils.Util;
import com.eme.mas.zxing.CaptureActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshCompatibleScrollView;
import com.handmark.pulltorefresh.library.extras.CompatibleScrollView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 首页面第一模块
 * <p>
 * Created by dijiaoliang on 16/7/15.
 */
@WLayout(layoutId = R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<CompatibleScrollView>, HomeAddCartInterface {

    private final static String TAG = HomeFragment.class.getSimpleName();


    @Bind(R.id.banner_top)
    ConvenientBanner bannerTop;
    @Bind(R.id.sv_scroll)
    PullToRefreshCompatibleScrollView svScroll;
    @Bind(R.id.horlv)
    RecyclerView horlv;
    @Bind(R.id.banner_middle)
    ConvenientBanner bannerMiddle;
    @Bind(R.id.sdv_hot01)
    SimpleDraweeView sdvHot01;
    @Bind(R.id.tv_pro_name01)
    TextView tvProName01;
    @Bind(R.id.tv_pro_price01)
    TextView tvProPrice01;
    @Bind(R.id.add01)
    ImageView add01;
    @Bind(R.id.sdv_hot02)
    SimpleDraweeView sdvHot02;
    @Bind(R.id.tv_pro_name02)
    TextView tvProName02;
    @Bind(R.id.tv_pro_price02)
    TextView tvProPrice02;
    @Bind(R.id.add02)
    ImageView add02;
    @Bind(R.id.sdv_hot03)
    SimpleDraweeView sdvHot03;
    @Bind(R.id.tv_pro_name03)
    TextView tvProName03;
    @Bind(R.id.tv_pro_price03)
    TextView tvProPrice03;
    @Bind(R.id.add03)
    ImageView add03;
    @Bind(R.id.sdv_hot04)
    SimpleDraweeView sdvHot04;
    @Bind(R.id.tv_pro_name04)
    TextView tvProName04;
    @Bind(R.id.tv_pro_price04)
    TextView tvProPrice04;
    @Bind(R.id.add04)
    ImageView add04;
    @Bind(R.id.sdv_hot05)
    SimpleDraweeView sdvHot05;
    @Bind(R.id.tv_pro_name05)
    TextView tvProName05;
    @Bind(R.id.tv_pro_price05)
    TextView tvProPrice05;
    @Bind(R.id.add05)
    ImageView add05;
    @Bind(R.id.tv_key01)
    TextView tvKey01;
    @Bind(R.id.tv_key02)
    TextView tvKey02;
    @Bind(R.id.tv_key03)
    TextView tvKey03;
    @Bind(R.id.tv_key04)
    TextView tvKey04;
    @Bind(R.id.tv_key05)
    TextView tvKey05;
    @Bind(R.id.tv_key06)
    TextView tvKey06;
    @Bind(R.id.gv_hot)
    IncludeGridView gvHot;
    @Bind(R.id.rl_title)
    LinearLayout rlTitle;
    @Bind(R.id.ll_search)
    LinearLayout llSearch;
    @Bind(R.id.ic_search)
    ImageView icSearch;
    @Bind(R.id.et_search)
    TextView etSearch;
    @Bind(R.id.ic_scan)
    ImageView icScan;
    @Bind(R.id.sdv_brand01)
    SimpleDraweeView sdvBrand01;
    @Bind(R.id.tv_brand_name_01)
    TextView tvBrandName01;
    @Bind(R.id.sdv_brand02)
    SimpleDraweeView sdvBrand02;
    @Bind(R.id.tv_brand_name_02)
    TextView tvBrandName02;
    @Bind(R.id.sdv_brand03)
    SimpleDraweeView sdvBrand03;
    @Bind(R.id.tv_brand_name_03)
    TextView tvBrandName03;
    @Bind(R.id.sdv_brand_04)
    SimpleDraweeView sdvBrand04;
    @Bind(R.id.tv_brand_name_04)
    TextView tvBrandName04;
    @Bind(R.id.sdv_brand_05)
    SimpleDraweeView sdvBrand05;
    @Bind(R.id.tv_brand_name_05)
    TextView tvBrandName05;
    @Bind(R.id.sdv_brand_06)
    SimpleDraweeView sdvBrand06;
    @Bind(R.id.tv_brand_name_06)
    TextView tvBrandName06;
    @Bind(R.id.sdv_brand_07)
    SimpleDraweeView sdvBrand07;
    @Bind(R.id.tv_brand_name_07)
    TextView tvBrandName07;
    @Bind(R.id.sdv_brand_08)
    SimpleDraweeView sdvBrand08;
    @Bind(R.id.tv_brand_name_08)
    TextView tvBrandName08;
    @Bind(R.id.ib_top)
    ImageButton ibTop;
    @Bind(R.id.bg_home)
    RelativeLayout bgHome;
    @Bind(R.id.rl_pro01)
    RelativeLayout rlPro01;
    @Bind(R.id.rl_pro02)
    RelativeLayout rlPro02;
    @Bind(R.id.rl_pro03)
    RelativeLayout rlPro03;
    @Bind(R.id.rl_pro04)
    RelativeLayout rlPro04;
    @Bind(R.id.rl_pro05)
    RelativeLayout rlPro05;
    @Bind(R.id.rl_cart_add_loading)
    RelativeLayout rlCartAddLoading;
    @Bind(R.id.ll_brand01)
    LinearLayout llBrand01;
    @Bind(R.id.ll_brand02)
    LinearLayout llBrand02;
    @Bind(R.id.ll_brand03)
    LinearLayout llBrand03;
    @Bind(R.id.ll_brand04)
    LinearLayout llBrand04;
    @Bind(R.id.ll_brand05)
    LinearLayout llBrand05;
    @Bind(R.id.ll_brand06)
    LinearLayout llBrand06;
    @Bind(R.id.ll_brand07)
    LinearLayout llBrand07;
    @Bind(R.id.ll_brand08)
    LinearLayout llBrand08;
    @Bind(R.id.iv_category_1)
    ImageView ivCategory1;
    @Bind(R.id.tv_category_name_1)
    TextView tvCategoryName1;
    @Bind(R.id.ll_one)
    LinearLayout llOne;
    @Bind(R.id.iv_category_2)
    ImageView ivCategory2;
    @Bind(R.id.tv_category_name_2)
    TextView tvCategoryName2;
    @Bind(R.id.ll_two)
    LinearLayout llTwo;
    @Bind(R.id.iv_category_3)
    ImageView ivCategory3;
    @Bind(R.id.tv_category_name_3)
    TextView tvCategoryName3;
    @Bind(R.id.ll_three)
    LinearLayout llThree;
    @Bind(R.id.iv_category_4)
    ImageView ivCategory4;
    @Bind(R.id.tv_category_name_4)
    TextView tvCategoryName4;
    @Bind(R.id.ll_four)
    LinearLayout llFour;

    private IProduct mProController;//商品控制器
    private ICart mCartController;//购物车控制器

    private List<HomeMobileVipBo> mobileVipData;//手机专享数据
    //    private HomeMobileVipAdapter vipAdapter;//手机专享适配器
    private HomePhoneVipAdapter vipAdapter;//手机专享适配器

    private List<HomeBannerBo> bannerData;

    private List<HomeBannerBo> brandBannerData;

    private CompatibleScrollView innerCompatibleScrollView;//pulltorefresh内嵌的内置ScrollView

    private int start_num = 0;//页码
    private int page_size = 6;//一页的条目个数
    private int count = 0;//控制图示的显示次数
    private String isHasMore = WValue.TRUE_STR;//热门推荐还有更多
    private HomeHotGoodsAdapter mRecAdapter;
    private List<HomeHotGoodsBo> recData;//热门推荐列表数据

    private DataTable wineTable;//酒规格的容器

    private HomeMobileVipBo mHomeVipProductBo;//添加购物车的
    private HomeHotGoodsBo mProductBo;

    private boolean isPullDown;//是否是下拉刷新

    /**
     * 要添加购物车的控件
     */
    private ImageView animView;

    private BadgeView mBadgeView;

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

        mBadgeView = ((MainActivity) getActivity()).getBadgeView();
        ibTop.setVisibility(View.GONE);
        rlTitle.setBackgroundColor(Color.argb(0, 255, 255, 255));
        mProController = mController.getProduct(this);//初始化控制器
        mCartController = mController.getIAccount(this);//初始化控制器

        /**对ScrollView进行参数设定*/
        svScroll.setMode(PullToRefreshBase.Mode.BOTH);//上拉、下拉设定
        svScroll.setOnRefreshListener(this);
        innerCompatibleScrollView = svScroll.getRefreshableView();
        innerCompatibleScrollView.setOnMyScrollChangeListener(new CompatibleScrollView.onMyScrollChangeListener() {
            @Override
            public void onScrollChange() {
                rlTitle.setVisibility(View.VISIBLE);
                changeTitleBar();
            }
        });
        svScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int value = svScroll.getScrollY();
                if (value < -Util.dip2px(getActivity(), 58)) {
                    rlTitle.setVisibility(View.GONE);
                }
                return false;
            }
        });

        //初始化手机专享
        if (mobileVipData == null) mobileVipData = new ArrayList<>();
        if (vipAdapter == null)
            vipAdapter = new HomePhoneVipAdapter(getActivity(), mobileVipData, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horlv.setLayoutManager(mLayoutManager);
        horlv.setAdapter(vipAdapter);
        vipAdapter.setOnRecyclerClickListener(new HomePhoneVipAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                toProDetailActivity(mobileVipData.get(position).getGoodsSkuId());

            }
        });

        //初始化banner
        if (bannerData == null) {
            bannerData = new ArrayList<>();
        }
        bannerTop.setPages(new CBViewHolderCreator<ConvenHolderView>() {
            @Override
            public ConvenHolderView createHolder() {
                return new ConvenHolderView();
            }
        }, bannerData).setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_selected})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                    }
                }).setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (brandBannerData == null) {
            brandBannerData = new ArrayList<>();
        }
        bannerMiddle.setPages(new CBViewHolderCreator<ConvenHolderView>() {
            @Override
            public ConvenHolderView createHolder() {
                return new ConvenHolderView();
            }
        }, brandBannerData).setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_selected})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                    }
                }).setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //热门推荐的列表初始化
        if (recData == null) recData = new ArrayList<>();
        if (mRecAdapter == null)
            mRecAdapter = new HomeHotGoodsAdapter(getActivity(), recData, R.layout.item_grid_product, new HomeAddCartNormalInterface() {
                @Override
                public void addCart(final ImageView iv_checkbox, final HomeHotGoodsBo productBo) {
                    iv_checkbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addCartNormal(productBo, iv_checkbox);
                        }
                    });
                }
            });
        gvHot.setAdapter(mRecAdapter);

        getHomeData();

        if (MasApplication.getInstance().isInitCategory()) {
            //获取分类信息
            String sql = "select * from wine_category";
            wineTable = new QueryBuilder(sql).executeDataTable();
        } else {
            //未初始化的话要先初始化后
            if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                mProController.getProductCategory(TAG);
            }
        }
        if (isLogin()) {
            if (MasApplication.getInstance().isInitCart()) {
                //TODO 更新badgeview的数值
                EventBus.getDefault().post(new RefreshCartEvent());
            } else {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    //获取购物车数据
                    isHideLayer(rlCartAddLoading, false);
                    mCartController.getCartList(TAG);
                } else {
                    isHideLayer(rlCartAddLoading, true);
                }
            }
        }

        // FIXME: 17/1/19  这里设置登陆状态为未登录  (用于调试)
        SPBase.setSPBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY,true);
    }

    /**
     * 跳转商品详情
     *
     * @param product_id
     */
    private void toProDetailActivity(String product_id) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            Intent intent = new Intent(getActivity(), ProDetailActivity.class);
            intent.putExtra(WValue.PRODUCT_ID, product_id);
            startActivity(intent);
        } else {
            ToastUtil.shortToast(getActivity(), R.string.net_error);
        }
    }


    /**
     * 请求home数据
     */
    private void getHomeData() {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            mProController.getHomeData(TAG);
        } else {
            ToastUtil.shortToast(getActivity(), R.string.net_error);
        }
    }


    /**
     * 更新home页面
     *
     * @param homeBean
     */
    private void updateHome(HomeResult.HomeBean homeBean) {
        if (homeBean != null) {
            //更新主轮播图
            List<HomeBannerBo> tempBannerBos = homeBean.getBannerVo();
            if (tempBannerBos != null && tempBannerBos.size() != 0) {
                bannerData.clear();
                bannerData.addAll(tempBannerBos);
                bannerTop.notifyDataSetChanged();
            }
            //分类信息
            List<HomeCategoryBo> tempCate=homeBean.getGoodsCategory();
            if(tempCate!=null && tempCate.size()!=0){
                for (int i = 0,size=tempCate.size(); i < size; i++) {
                    HomeCategoryBo categoryBo=tempCate.get(i);
                    switch (i){
                        case 0:
                            Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,categoryBo.getImage()))
                                    .error(R.mipmap.home_spirit) //在图像加载失败时显示
                                    .into(ivCategory1);
                            tvCategoryName1.setText(categoryBo.getName());
                            llOne.setOnClickListener(new WineOnClickListener(categoryBo.getId(),categoryBo.getName()));
                            break;
                        case 1:
                            Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,categoryBo.getImage()))
                                    .error(R.mipmap.home_wine) //在图像加载失败时显示
                                    .into(ivCategory2);
                            tvCategoryName2.setText(categoryBo.getName());
                            llTwo.setOnClickListener(new WineOnClickListener(categoryBo.getId(),categoryBo.getName()));
                            break;
                        case 2:
                            Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,categoryBo.getImage()))
                                    .error(R.mipmap.home_beer) //在图像加载失败时显示
                                    .into(ivCategory3);
                            tvCategoryName3.setText(categoryBo.getName());
                            llThree.setOnClickListener(new WineOnClickListener(categoryBo.getId(),categoryBo.getName()));
                            break;
                        case 3:
                            Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,categoryBo.getImage()))
                                    .error(R.mipmap.home_foreign) //在图像加载失败时显示
                                    .into(ivCategory4);
                            tvCategoryName4.setText(categoryBo.getName());
                            llFour.setOnClickListener(new WineOnClickListener(categoryBo.getId(),categoryBo.getName()));
                            break;
                    }
                }
            }

            //手机专享
            List<HomeMobileVipBo> tempVipData = homeBean.getMobileVip();
            if (tempVipData != null && tempVipData.size() != 0) {
                mobileVipData.clear();
                mobileVipData.addAll(tempVipData);
                vipAdapter.notifyDataSetChanged();
            }
            //手机专享轮播图
            List<HomeBannerBo> tempPhoneVipBanners = homeBean.getAdvertisementImg();
            if (tempPhoneVipBanners != null && tempPhoneVipBanners.size() != 0) {
                brandBannerData.clear();
                brandBannerData.addAll(tempPhoneVipBanners);
                bannerMiddle.notifyDataSetChanged();
                if (brandBannerData.size() <= 1) {
                    if (bannerMiddle.isTurning()) {
                        bannerMiddle.stopTurning();
                    }
                    bannerMiddle.setPageIndicator(new int[]{R.drawable.dot_transparent, R.drawable.dot_transparent}).setCanLoop(false);
                } else {
                    bannerMiddle.setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_selected}).setCanLoop(true);
                    if (!bannerMiddle.isTurning()) {
                        bannerMiddle.startTurning(2000);
                    }
                }
            }
            //品牌汇
            List<HomeBrandBo> tempBrandList = homeBean.getBrandCollection();
            if (tempBrandList != null && tempBrandList.size() != 0) {
                int size_brand = tempBrandList.size();
                HomeBrandBo brandBo;
                for (int h = 0; h < size_brand; h++) {
                    brandBo = tempBrandList.get(h);
                    switch (h) {
                        case 0:
                            sdvBrand01.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName01.setText(brandBo.getBrandName());
                            llBrand01.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 1:
                            sdvBrand02.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName02.setText(brandBo.getBrandName());
                            llBrand02.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 2:
                            sdvBrand03.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName03.setText(brandBo.getBrandName());
                            llBrand03.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 3:
                            sdvBrand04.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName04.setText(brandBo.getBrandName());
                            llBrand04.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 4:
                            sdvBrand05.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName05.setText(brandBo.getBrandName());
                            llBrand05.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 5:
                            sdvBrand06.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName06.setText(brandBo.getBrandName());
                            llBrand06.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 6:
                            sdvBrand07.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName07.setText(brandBo.getBrandName());
                            llBrand07.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                        case 7:
                            sdvBrand08.setImageURI(UriUtil.getImage(KConfig.HOST_URL + brandBo.getImageurl()));
                            tvBrandName08.setText(brandBo.getBrandName());
                            llBrand08.setOnClickListener(new BrandOnClickListener(brandBo.getBrandId(), brandBo.getBrandName()));
                            break;
                    }
                }
            }

            //热门推荐
            List<HomeHotGoodsBo> tempRecPros = homeBean.getHotGoods();
            if (tempRecPros != null && tempRecPros.size() > 0) {
                int size = tempRecPros.size();
                for (int i = 0; i < size; i++) {
                    final HomeHotGoodsBo tempBo = tempRecPros.get(i);
                    switch (i) {
                        case 0:
                            sdvHot01.setImageURI(UriUtil.getImage(KConfig.HOST_URL + tempBo.getGoodsImage()));
                            tvProName01.setText(tempBo.getGoodsName());
                            tvProPrice01.setText(tempBo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                            add01.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCartNormal(tempBo, add01);
                                }
                            });
                            rlPro01.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toProDetailActivity(tempBo.getSkuId());
                                }
                            });
                            break;
                        case 1:
                            sdvHot02.setImageURI(UriUtil.getImage(KConfig.HOST_URL + tempBo.getGoodsImage()));
                            tvProName02.setText(tempBo.getGoodsName());
                            tvProPrice02.setText(tempBo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                            add02.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCartNormal(tempBo, add02);
                                }
                            });
                            rlPro02.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toProDetailActivity(tempBo.getSkuId());
                                }
                            });
                            break;
                        case 2:
                            sdvHot03.setImageURI(UriUtil.getImage(KConfig.HOST_URL + tempBo.getGoodsImage()));
                            tvProName03.setText(tempBo.getGoodsName());
                            tvProPrice03.setText(tempBo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                            add03.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCartNormal(tempBo, add03);
                                }
                            });
                            rlPro03.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toProDetailActivity(tempBo.getSkuId());
                                }
                            });
                            break;
                        case 3:
                            sdvHot04.setImageURI(UriUtil.getImage(KConfig.HOST_URL + tempBo.getGoodsImage()));
                            tvProName04.setText(tempBo.getGoodsName());
                            tvProPrice04.setText(tempBo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                            add04.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCartNormal(tempBo, add04);
                                }
                            });
                            rlPro04.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toProDetailActivity(tempBo.getSkuId());
                                }
                            });
                            break;
                        case 4:
                            sdvHot05.setImageURI(UriUtil.getImage(KConfig.HOST_URL + tempBo.getGoodsImage()));
                            tvProName05.setText(tempBo.getGoodsName());
                            tvProPrice05.setText(tempBo.getSalePrice().setScale(2, BigDecimal.ROUND_DOWN).toString());
                            add05.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCartNormal(tempBo, add05);
                                }
                            });
                            rlPro05.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toProDetailActivity(tempBo.getSkuId());
                                }
                            });
                            break;
                    }
                }
            }
            //热门推荐热搜词
            List<String> hotWords = homeBean.getHotWords();
            if (hotWords != null && hotWords.size() != 0) {
                int hotSize = hotWords.size();
                for (int j = 0; j < hotSize; j++) {
                    switch (j) {
                        case 0:
                            if (!TextUtils.isEmpty(hotWords.get(j))) {
                                tvKey01.setText(hotWords.get(j));
                                tvKey01.setOnClickListener(new KeyOnClickListener(hotWords.get(j)));
                            }
                            break;
                        case 1:
                            if (!TextUtils.isEmpty(hotWords.get(j))) {
                                tvKey02.setText(hotWords.get(j));
                                tvKey02.setOnClickListener(new KeyOnClickListener(hotWords.get(j)));
                            }
                            break;
                        case 2:
                            if (!TextUtils.isEmpty(hotWords.get(j))) {
                                tvKey03.setText(hotWords.get(j));
                                tvKey03.setOnClickListener(new KeyOnClickListener(hotWords.get(j)));
                            }
                            break;
                        case 3:
                            if (!TextUtils.isEmpty(hotWords.get(j))) {
                                tvKey04.setText(hotWords.get(j));
                                tvKey04.setOnClickListener(new KeyOnClickListener(hotWords.get(j)));
                            }
                            break;
                        case 4:
                            if (!TextUtils.isEmpty(hotWords.get(j))) {
                                tvKey05.setText(hotWords.get(j));
                                tvKey05.setOnClickListener(new KeyOnClickListener(hotWords.get(j)));
                            }
                            break;
                        case 5:
                            if (!TextUtils.isEmpty(hotWords.get(j))) {
                                tvKey06.setText(hotWords.get(j));
                                tvKey06.setOnClickListener(new KeyOnClickListener(hotWords.get(j)));
                            }
                            break;
                    }
                }
            }

        }

        //处理上拉加载的事务
        if (!WValue.TRUE_STR.equals(isHasMore)) isHasMore = WValue.TRUE_STR;
            count = 0;
        if (PullToRefreshBase.Mode.PULL_FROM_START == svScroll.getMode()) {
            svScroll.setMode(PullToRefreshBase.Mode.BOTH);
        }
        if (isPullDown) {
            isPullDown = !isPullDown;
            rlTitle.setVisibility(View.VISIBLE);
            rlTitle.setBackgroundColor(Color.argb(0, 255, 255, 255));
            icSearch.setImageResource(R.mipmap.home_search_w);
            icScan.setImageResource(R.mipmap.home_richscan_w);
            etSearch.setHintTextColor(Color.WHITE);
        }
        svScroll.onRefreshComplete();
        if (start_num != 0) start_num = 0;

        recData.clear();
        mRecAdapter.notifyDataSetChanged();


    }

    /**
     * 品牌条目点击监听
     */
    private class BrandOnClickListener implements View.OnClickListener {

        private String id;
        private String brandName;

        public BrandOnClickListener(String id, String brandName) {
            this.id = id;
            this.brandName = brandName;
        }

        @Override
        public void onClick(View v) {
            /**判断网络状态*/
            if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                if (!TextUtils.isEmpty(id)) {
                    Intent intent = new Intent(getActivity(), BrandActivity.class);
                    intent.putExtra("search_name", id);
                    intent.putExtra("brand_name", brandName);
                    startActivity(intent);
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.noBrandId);
                }
            } else {
                ToastUtil.shortToast(getActivity(), R.string.net_error);
            }
        }
    }

    /**
     * 关键字点击监听
     */
    private class KeyOnClickListener implements View.OnClickListener {

        private String key;

        public KeyOnClickListener(String key) {
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            /**判断网络状态*/
            if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                if (!TextUtils.isEmpty(key)) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("search_name", key);
                    startActivity(intent);
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.noSearchContent);
                }
            } else {
                ToastUtil.shortToast(getActivity(), R.string.net_error);
            }
        }
    }

    /**
     * 酒分类按钮点击监听
     */
    private class WineOnClickListener implements View.OnClickListener {

        private String id;
        private String wineName;

        public WineOnClickListener(String id,String wineName) {
            this.id = id;
            this.wineName = wineName;
        }

        @Override
        public void onClick(View v) {
            /**判断网络状态*/
            if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(wineName)) {
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    intent.putExtra("wine_name", wineName);
                    intent.putExtra("wine_id", id);
                    startActivity(intent);
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.category_error);
                }
            } else {
                ToastUtil.shortToast(getActivity(), R.string.net_error);
            }
        }
    }


    private double alphe = 200.0;
    private int bannerHeight = 0;

    /**
     * 改变标题栏的alpha状态
     */
    private void changeTitleBar() {
        int scrollHeight = innerCompatibleScrollView.getScrollY();

        if (bannerHeight == 0) {
            bannerHeight = Util.dip2px(getActivity(), 70);
        }
        double alpha = scrollHeight / (double) bannerHeight * alphe;
        if (alpha >= alphe)//防止惯性滑动引起的误差
        {
            alpha = alphe;
            icSearch.setImageResource(R.mipmap.home_search_gray);
            icScan.setImageResource(R.mipmap.home_richscan_n);
            etSearch.setHintTextColor(getResources().getColor(R.color.cart_tv_gray));
        }
        if (alpha >= 0.0 && alpha < alphe) {
            icSearch.setImageResource(R.mipmap.home_search_w);
            icScan.setImageResource(R.mipmap.home_richscan_w);
            etSearch.setHintTextColor(Color.WHITE);
        }
        if (alpha < 0.0) {
            return;
        }

        rlTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
//        llSearch.setBackgroundColor(Color.argb((int) alpha, 229, 229, 229));


        //控制回到顶部的按钮的显示隐藏
        if (Util.getScreenHeight() < scrollHeight) {
            ibTop.setVisibility(View.VISIBLE);
        } else {
            ibTop.setVisibility(View.GONE);
        }

    }

    /**
     * 更新热门推荐列表数据
     *
     * @param recListData
     */
    private void updateHomeRec(List<HomeHotGoodsBo> recListData) {
        start_num++;
        if (recData != null) recData.addAll(recListData);
        if (mRecAdapter != null) mRecAdapter.notifyDataSetChanged();
        if (svScroll.isRefreshing()) svScroll.onRefreshComplete();

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<CompatibleScrollView> refreshView) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            isPullDown = true;
            mProController.getHomeData(TAG);
        } else {
            ToastUtil.shortToast(getActivity(), "当前网络不可用");
            rlTitle.setBackgroundColor(Color.argb(1, 255, 255, 255));
            icSearch.setImageResource(R.mipmap.home_search_w);
            icScan.setImageResource(R.mipmap.home_richscan_w);
            etSearch.setHintTextColor(Color.WHITE);
            svScroll.onRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<CompatibleScrollView> refreshView) {
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            if (WValue.TRUE_STR.equals(isHasMore)) {
                isPullDown = false;
//                position = gv_guess.getVerticalScrollbarPosition();
//                if (mLikeList != null) {
//                    View v = gv_guess.getChildAt(0);
//                    top = (v == null) ? 0 : v.getTop();
//                }
//                SPBase.setSPIn(getActivity(), SpConstant.MAIN_PAGE_FILE_NAME, SpConstant.X_KEY, position);
//                SPBase.setSPIn(getActivity(), SpConstant.MAIN_PAGE_FILE_NAME, SpConstant.Y_KEY, top);
                mProController.getHomeRecData(String.valueOf(start_num+1),String.valueOf(page_size),TAG);
            } else {
                if (count == 0) {
                    ToastUtil.shortToast(getActivity(), R.string.toast_no_goods);
                    count++;
                }
                svScroll.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                svScroll.onRefreshComplete();
//                rlTitle.setBackgroundColor(Color.argb(1, 255, 255, 255));
            }
        } else {
            ToastUtil.shortToast(getActivity(),R.string.net_error);
            svScroll.onRefreshComplete();
//            rlTitle.setBackgroundColor(Color.argb(1, 255, 255, 255));
        }
    }


    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;


    /**
     * ★★★★★把商品添加到购物车的动画效果★★★★★
     *
     * @param iv
     */
    private void addCart(ImageView iv) {
        if (!mBadgeView.isShown()) mBadgeView.show();
        int size = Util.dip2px(getActivity(), 22);
        BadgeView cart = ((MainActivity) getActivity()).getBadgeView();
        final RelativeLayout llMain = ((MainActivity) getActivity()).getMainBg();
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(getActivity());
        goods.setImageDrawable(iv.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        llMain.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        llMain.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        cart.getLocationInWindow(endLoc);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0];
        float startY = startLoc[1] - parentLocation[1];

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + cart.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(WValue.TIME_ADDCART);
        // 匀速线性插值器
//        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                llMain.removeView(goods);
                EventBus.getDefault().post(new RefreshCartEvent());
                ToastUtil.shortToast(getActivity(), "添加购物车成功");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @OnClick({R.id.ll_search, R.id.ll_home_scan, R.id.ib_call, R.id.ib_top})
    void click(View v) {
        DataRow row;
        Intent intent;
        String wineId;
        String wineName;
        switch (v.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.ib_call:
                final AirlinesDialog csDialog = new AirlinesDialog();
                csDialog.setOnCallListener(new AirlinesDialog.OnCallListener() {
                    @Override
                    public void toAirline() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "10086-1"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        csDialog.cancel();
                    }

                    @Override
                    public void toAfterSale() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "10086-2"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        csDialog.cancel();
                    }
                });
                csDialog.showDialog(getActivity(), null);

//                startActivity(new Intent(getActivity(), ProScanActivity.class));
                break;
            case R.id.ib_top:
                innerCompatibleScrollView.scrollTo(0, 0);
//                innerCompatibleScrollView.smoothScrollTo(0,0);
//                svScroll.scrollTo(0,0);
                break;
            case R.id.ll_home_scan:
                startActivity(new Intent(getActivity(), CaptureActivity.class));
//                startActivity(new Intent(mContext, ProDetailActivity.class));
                break;
//            case R.id.ll_spirit:
//                if (wineTable != null && wineTable.getRowCount() > 0) {
//                    row = wineTable.get(0);
//                    wineId = (String) row.get(0);
//                    wineName = (String) row.get(1);
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    intent.putExtra("wine_name", wineName);
//                    intent.putExtra("wine_id", wineId);
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.ll_wine:
//                if (wineTable != null && wineTable.getRowCount() > 0) {
//                    row = wineTable.get(1);
//                    wineId = (String) row.get(0);
//                    wineName = (String) row.get(1);
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    intent.putExtra("wine_name", wineName);
//                    intent.putExtra("wine_id", wineId);
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.ll_foreign:
//                if (wineTable != null && wineTable.getRowCount() > 0) {
//                    row = wineTable.get(2);
//                    wineId = (String) row.get(0);
//                    wineName = (String) row.get(1);
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    intent.putExtra("wine_name", wineName);
//                    intent.putExtra("wine_id", wineId);
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.ll_coupon:
//                if (wineTable != null && wineTable.getRowCount() > 0) {
//                    row = wineTable.get(3);
//                    wineId = (String) row.get(0);
//                    wineName = (String) row.get(1);
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    intent.putExtra("wine_name", wineName);
//                    intent.putExtra("wine_id", wineId);
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(getActivity(), CategoryActivity.class);
//                    startActivity(intent);
//                }
//                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerTop.startTurning(2000);
        if (brandBannerData.size() <= 1) {
            if (bannerMiddle.isTurning()) {
                bannerMiddle.stopTurning();
            }
            bannerMiddle.setPageIndicator(new int[]{R.drawable.dot_transparent, R.drawable.dot_transparent}).setCanLoop(false);
        } else {
            bannerMiddle.setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_selected}).setCanLoop(true);
            if (!bannerMiddle.isTurning()) {
                bannerMiddle.startTurning(2000);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerTop.isTurning()) bannerTop.stopTurning();
        if (bannerMiddle.isTurning()) bannerMiddle.stopTurning();
    }


    /**
     * 手机专享添加购物车
     *
     * @param iv_checkbox
     * @param productBo
     */
    @Override
    public void addCart(final ImageView iv_checkbox, final HomeMobileVipBo productBo) {
        iv_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    if (isLogin()) {
                        mProductBo = null;
                        mHomeVipProductBo = productBo;
                        isHideLayer(rlCartAddLoading, false);
                        animView = iv_checkbox;
                        mCartController.addToCart(productBo.getGoodsSkuId(), productBo.getGoodsSkuId(), String.valueOf(1), WValue.PHONE_VIP, TAG);
                    } else {
                        login();//跳转登录页面
                    }
                } else {
                    ToastUtil.shortToast(getActivity(), "当前网络不可用");
                }
            }
        });
    }

    /**
     * 普通商品添加购物车
     *
     * @param productBo
     */
    private void addCartNormal(HomeHotGoodsBo productBo, ImageView v) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            if (isLogin()) {
                mProductBo = productBo;
                mHomeVipProductBo = null;
                animView = v;
                isHideLayer(rlCartAddLoading, false);
                mCartController.addToCart(productBo.getSkuId(), productBo.getSkuId(), String.valueOf(1), WValue.PHONE_NORMAL, TAG);
            } else {
                login();
            }
        } else {
            ToastUtil.shortToast(getActivity(), R.string.net_error);
        }
    }

    @OnItemClick(R.id.gv_hot)
    void itemclick(int position) {
        toProDetailActivity(recData.get(position).getSkuId());
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        String msg=result.getMsg();
        switch (result.getAction()) {
            case WAction.HOME_FRAGMENT_DATA:
                if(TextUtils.isEmpty(msg)) msg="页面更新失败";
                break;
            case WAction.ADD_TO_SHOPPING_CART:
                if(TextUtils.isEmpty(msg)) msg="添加失败";
                break;
        }
        ToastUtil.shortToast(getActivity(), msg);
        if (isPullDown) {
            isPullDown = !isPullDown;
            rlTitle.setVisibility(View.VISIBLE);
            rlTitle.setBackgroundColor(Color.argb(0, 255, 255, 255));
            icSearch.setImageResource(R.mipmap.home_search_w);
            icScan.setImageResource(R.mipmap.home_richscan_w);
            etSearch.setHintTextColor(Color.WHITE);
        }
        if (svScroll.isRefreshing()) svScroll.onRefreshComplete();
        isHideLayer(rlCartAddLoading, true);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        String action = result.getAction();
        if (WAction.HOME_FRAGMENT_DATA.equals(action)) {
            HomeResult.HomeBean homeBean = ((HomeResult) result).getData();
            if (homeBean != null) {
                updateHome(homeBean);
            } else {
                if (isPullDown) {
                    isPullDown = !isPullDown;
                    rlTitle.setVisibility(View.VISIBLE);
                    rlTitle.setBackgroundColor(Color.argb(0, 255, 255, 255));
                    icSearch.setImageResource(R.mipmap.home_search_w);
                    icScan.setImageResource(R.mipmap.home_richscan_w);
                    etSearch.setHintTextColor(Color.WHITE);
                    recData.clear();
                    mRecAdapter.notifyDataSetChanged();
                }
                svScroll.onRefreshComplete();
                if (start_num != 0) start_num = 0;
            }
        } else if (WAction.HOME_FRAGMENT_RECDATA.equals(action)) {
            HomeRecListVo homeRecBean = ((HomeRecResult) result).getData();
            List<HomeHotGoodsBo> tempList = homeRecBean.getResults();
            if (tempList != null && tempList.size() != 0) {
                updateHomeRec(tempList);
            } else {
                //TODO 处理热门推荐数据为零情况
            }
            isHasMore = homeRecBean.getHasMore().trim();
        } else if (WAction.CATEGORY_ACTIVITY_DATA.equals(action)) {
            List<CategorySpecVo> bean = ((CategoryResult) result).getData();
            if (bean != null) {
                SqlUtil.clearCategoryData();
                SqlUtil.initCategoryData(bean);
                MasApplication.getInstance().setIsInitCategory(true);
                String sql = "select * from wine_category";
                wineTable = new QueryBuilder(sql).executeDataTable();
            } else {
                MasApplication.getInstance().setIsInitCategory(false);
            }
        } else if (WAction.ADD_TO_SHOPPING_CART.equals(action)) {
            String cartId = ((AddCartResult) result).getData();
            if (cartId != null) {
                //保存购物车数据到数据库
                if (mProductBo != null) {
//                    SqlUtil.addCartData(cartId, mProductBo);
                } else if (mHomeVipProductBo != null) {
//                    SqlUtil.addCartData(cartId, mHomeVipProductBo);
                }
                addCart(animView);
            }
        } else if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
            List<CartBo> cart_list = ((CartListResult) result).getData();
            if (cart_list != null) {
                //把购物车数据存储到数据库
                SqlUtil.initCartData(cart_list);
                MasApplication.getInstance().setIsInitCart(true);
//                List<DataRow> tempDrList = SqlUtil.getCartData();
//                int count = 0;
//                for(DataRow row:tempDrList){
//                    count=count+Integer.parseInt((String)row.get("product_num"));
//                }
                EventBus.getDefault().post(new RefreshCartEvent());
            } else {
                MasApplication.getInstance().setIsInitCart(false);
            }
        }
        isHideLayer(rlCartAddLoading, true);
        if (svScroll.isRefreshing()) svScroll.onRefreshComplete();
    }
}