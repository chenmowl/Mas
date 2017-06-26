package com.eme.mas.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.SearchResultAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.MListView;
import com.eme.mas.data.sp.SpSearch;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.AddCartResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.home.HomeHotGoodsBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.owater.library.CircleTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 品牌详情页
 * <p/>
 * Created by dijiaoliang on 16/7/25.
 */
@WLayout(layoutId = R.layout.activity_brand)
public class BrandActivity extends BaseCartActivity implements PullToRefreshBase.OnRefreshListener{

    private final static String TAG = BrandActivity.class.getSimpleName();
    @Bind(R.id.bar_back)
    ImageView barBack;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_cline)
    ImageView barCline;
    @Bind(R.id.tv_colligate)
    TextView tvColligate;
    @Bind(R.id.tv_sales)
    TextView tvSales;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.iv_price)
    ImageView ivPrice;
    @Bind(R.id.rl_price)
    RelativeLayout rlPrice;
    @Bind(R.id.cline)
    ImageView cline;
    @Bind(R.id.gv_pro)
    PullToRefreshGridView gvPro;
    @Bind(R.id.iv_cart)
    ImageView ivCart;
    @Bind(R.id.rl_search_empty)
    RelativeLayout rlSearchEmpty;
    @Bind(R.id.rl_bg)
    View rlBg;
    @Bind(R.id.ll_delete)
    LinearLayout llDelete;
    @Bind(R.id.iv_all)
    ImageView ivAll;
    @Bind(R.id.ll_all)
    LinearLayout llAll;
    @Bind(R.id.cartList)
    MListView cartList;
    @Bind(R.id.dialog)
    LinearLayout dialog;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.btn_balance)
    Button btnBalance;
    @Bind(R.id.bt)
    RelativeLayout bt;
    @Bind(R.id.ctv_badge)
    CircleTextView ctvBadge;
    @Bind(R.id.rl_anim)
    RelativeLayout rlAnim;
    @Bind(R.id.avi)
    AVLoadingIndicatorView avi;
    @Bind(R.id.rl_cart_add_loading)
    RelativeLayout rlCartAddLoading;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    @Bind(R.id.iv_empty)
    ImageView ivEmpty;
    @Bind(R.id.btn_stroll)
    Button btnStroll;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;

    private HomeHotGoodsBo mProductBo;//添加购物车临时变量
    /**
     * 要添加购物车的控件
     */
    private ImageView animView;
    private ICart mCartController;//购物车控制器


    private int start_num = 1;//页码
    private String hasMore;//是否还有更多
    private boolean isLoadMore;//是否加载更多
    private String order_condition;//目前排序条件
    private int color_selecte;
    private int color_normal;
    private String search_name,search_id;

    private IProduct mProController;
    private List<HomeHotGoodsBo> searchResultData;//搜索结果数据
    private SearchResultAdapter mSearchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        mCartController = mController.getIAccount(this);//初始化控制器
        mProController = mController.getProduct(this);//初始化控制器
        color_selecte = getResources().getColor(R.color.main_color_red);
        color_normal = getResources().getColor(R.color.text_color_bar);
        searchResultData = new ArrayList<>();
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        search_id = getIntent().getStringExtra("search_name");
        search_name = getIntent().getStringExtra("brand_name");
        barTitle.setText(search_name);
        order_condition = WValue.WEIGHT;
        tvColligate.setTextColor(Color.RED);
        tvSales.setTextColor(Color.BLACK);
        tvPrice.setTextColor(Color.BLACK);
        gvPro.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gvPro.setOnRefreshListener(this);
//        mSearchResultAdapter = new SearchResultAdapter(this, searchResultData, R.layout.item_grid_product,new HomeAddCartNormalInterface() {
//            @Override
//            public void addCart(final ImageView iv_checkbox, final HomeHotGoodsBo productBo) {
//                iv_checkbox.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addCartNormal(productBo,iv_checkbox);
//                    }
//                });
//            }
//        });
        gvPro.setAdapter(mSearchResultAdapter);
        search(false);

    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        //上拉刷新的操作
        search(true);
    }


    @Override
    public void initCartView() {
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        btn_cart = (RelativeLayout) findViewById(R.id.bt);
        ll_dialog = (LinearLayout) findViewById(R.id.dialog);
        iv_all = (ImageView) findViewById(R.id.iv_all);
        bg = findViewById(R.id.rl_bg);
        ctv_badge = (CircleTextView) findViewById(R.id.ctv_badge);
        btn_balance = (Button) findViewById(R.id.btn_balance);
        mListView = (MListView) findViewById(R.id.cartList);
    }

    /**
     * 普通商品添加购物车
     *
     * @param productBo
     */
    private void addCartNormal(HomeHotGoodsBo productBo, ImageView v) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            if (isLogin()) {
                mProductBo = productBo;
                animView = v;
                isHideLayer(rlCartAddLoading, false);
                mCartController.addToCart(productBo.getSkuId(), productBo.getSkuId(), String.valueOf(1), WValue.PHONE_NORMAL, TAG);
            } else {
                login();
            }
        } else {
            ToastUtil.shortToast(this, "当前网络不可用");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) mProController).cancelRequestByTag(TAG);
    }

    /**
     * 搜索指定商品数据
     *
     * @param isPullRefresh
     */
    private void search(boolean isPullRefresh) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(BrandActivity.this)) {
            if (isPullRefresh) {
                if ("true".equals(hasMore)) {
                    if (isLoadMore == false) isLoadMore = true;
                    mProController.getSearchResult(search_id,String.valueOf(start_num),"6","1","0", TAG);
                } else {
                    ToastUtil.shortToast(BrandActivity.this, "无更多商品");
                    if (gvPro.isRefreshing()) gvPro.onRefreshComplete();
                }
            } else {
                //TODO  这里是执行搜索，同时保存搜索纪录
                if (start_num > 1 || start_num == 0) start_num = 1;
                isHideLayer(rlCartAddLoading, false);
                isHideLayer(rlSearchEmpty, true);
                isHideLayer(rlNoNetwork, true);
                mProController.getSearchResult(search_id,String.valueOf(start_num),"6","1","0", TAG);
                SpSearch.saveSearchRecord(search_name);
            }
        } else {
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlSearchEmpty, true);
            isHideLayer(rlNoNetwork, false);
            ToastUtil.shortToast(BrandActivity.this, R.string.net_error);
            if (gvPro.isRefreshing()) gvPro.onRefreshComplete();
        }
    }


    @OnClick({R.id.tv_colligate, R.id.tv_sales, R.id.rl_price, R.id.back, R.id.btn_stroll})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tv_colligate:
                if (WValue.WEIGHT.equals(order_condition)) {
                    //相同状态不做操作
                } else {
                    if (WValue.PRICE_DOWN.equals(order_condition) || WValue.PRICE_UP.equals(order_condition)) {
                        ivPrice.setImageResource(R.mipmap.sort_normal);
                    }
                    tvColligate.setTextColor(color_selecte);
                    tvSales.setTextColor(color_normal);
                    tvPrice.setTextColor(color_normal);
                    order_condition = WValue.WEIGHT;

                    search(false);
                }
                ivPrice.setImageResource(R.mipmap.sort_normal);
                break;
            case R.id.tv_sales:
                if (WValue.SALE.equals(order_condition)) {
                    //相同状态不做操作
                } else {
                    if (WValue.PRICE_DOWN.equals(order_condition) || WValue.PRICE_UP.equals(order_condition)) {
                        ivPrice.setImageResource(R.mipmap.sort_normal);
                    }
                    tvColligate.setTextColor(color_normal);
                    tvSales.setTextColor(color_selecte);
                    tvPrice.setTextColor(color_normal);
                    order_condition = WValue.SALE;

                    search(false);
                }
                ivPrice.setImageResource(R.mipmap.sort_normal);
                break;
            case R.id.rl_price:
                if (WValue.PRICE_DOWN.equals(order_condition)) {
                    order_condition = WValue.PRICE_UP;
                    ivPrice.setImageResource(R.mipmap.sort_above);
                    //TODO 这里加载价格升序的数据

                } else if (WValue.PRICE_UP.equals(order_condition)) {
                    order_condition = WValue.PRICE_DOWN;
                    ivPrice.setImageResource(R.mipmap.sort_below);
                    //TODO 这里加载价格降序的数据

                } else {
                    tvColligate.setTextColor(Color.BLACK);
                    tvSales.setTextColor(Color.BLACK);
                    tvPrice.setTextColor(Color.RED);
                    order_condition = WValue.PRICE_DOWN;
                    //TODO 这里加载价格降序的数据
                    ivPrice.setImageResource(R.mipmap.sort_below);
                }
                search(false);
                break;
            case R.id.btn_stroll:
                search(false);
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        switch (result.getAction()) {
            case WAction.SEARCH_RESULT_ACTIVITY_PROS:
                if (isLoadMore) {
                    isLoadMore = false;
                    gvPro.onRefreshComplete();
                }
                break;
            case WAction.ADD_TO_SHOPPING_CART:
                ToastUtil.shortToast(this, "添加失败");
                break;
        }
        isHideLayer(rlCartAddLoading, true);
        isHideLayer(rlSearchEmpty, true);
        isHideLayer(rlNoNetwork, false);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        String action = result.getAction();
        if (WAction.SEARCH_RESULT_ACTIVITY_PROS.equals(action)) {
//            SearchResult hotWordResult = (SearchResult) result;
//            SearchResultVo data = hotWordResult.getData();
//            hasMore = data.getHasMore().trim();
//            List<HomeHotGoodsBo> tempProList = data.getProduct_list();
//            if (data != null && tempProList.size() != 0) {
//                if (!isLoadMore) {
//                    searchResultData.clear();
//                    if (start_num != 0) start_num = 0;
//                    searchResultData.addAll(tempProList);
//                    mSearchResultAdapter.notifyDataSetChanged();
//                    if (gvPro.isRefreshing()) gvPro.onRefreshComplete();
//                } else {
//                    searchResultData.addAll(tempProList);
//                    mSearchResultAdapter.notifyDataSetChanged();
//                    gvPro.onRefreshComplete();
//                    start_num++;
//                    isLoadMore = false;
//                }
//                isHideLayer(rlSearchEmpty, true);
//            } else {
//                isHideLayer(rlSearchEmpty, false);
//                ToastUtil.shortToast(this, "搜索无商品");
//            }
//            isHideLayer(rlCartAddLoading, true);
//            isHideLayer(rlNoNetwork, true);
        } else if (WAction.ADD_TO_SHOPPING_CART.equals(action)) {
            String cartId = ((AddCartResult) result).getData();
            if (cartId != null) {
                //保存购物车数据到数据库
                if (mProductBo != null) {
//                    SqlUtil.addCartData(cartId, mProductBo);
                }
                addCart(animView);
            }
        }
    }
}
