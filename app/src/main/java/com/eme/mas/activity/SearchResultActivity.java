package com.eme.mas.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.eme.mas.controller.customeInterface.SearchAddCartInterface;
import com.eme.mas.customeview.MListView;
import com.eme.mas.data.sp.SpSearch;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.FinishPageEvent;
import com.eme.mas.model.Result;
import com.eme.mas.model.SearchResult;
import com.eme.mas.model.entity.SearchGoodsBo;
import com.eme.mas.model.entity.SearchResultVo;
import com.eme.mas.utils.KeyboardUtils;
import com.eme.mas.utils.ListUtil;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.StringUtil;
import com.eme.mas.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.owater.library.CircleTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索结果页
 * <p/>
 * Created by dijiaoliang on 16/7/25.
 */
@WLayout(layoutId = R.layout.activity_search_result)
public class SearchResultActivity extends BaseCartActivity implements PullToRefreshBase.OnRefreshListener {

    private final static String TAG = SearchResultActivity.class.getSimpleName();

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.iv_price)
    ImageView ivPrice;
    @Bind(R.id.rl_price)
    RelativeLayout rlPrice;
    @Bind(R.id.gv_pro)
    PullToRefreshGridView gvPro;
    @Bind(R.id.tv_colligate)
    TextView tvColligate;
    @Bind(R.id.tv_sales)
    TextView tvSales;
    @Bind(R.id.rl_search_empty)
    RelativeLayout rlSearchEmpty;
    @Bind(R.id.rl_cart_add_loading)
    RelativeLayout rlCartAddLoading;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;

    private int pageNum;//页码
    private int pageSize = WValue.PAGE_SIZE;//每页条目
    private boolean hasMore;//是否还有更多
    private boolean isLoadMore;//是否加载更多
    private String sortField;//目前排序条件
    private String order;//正序倒序
    private int color_select;
    private int color_normal;
    private String search_name;

    private IProduct mProController;
    private List<SearchGoodsBo> searchResultData;//搜索结果数据
    private SearchResultAdapter mSearchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) mProController).cancelRequestByTag(TAG);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        pageNum = 0;
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        mProController = mController.getProduct(this);//初始化控制器
        mCartController = mController.getIAccount(this);//初始化控制器
        search_name = getIntent().getStringExtra("search_name");
        etSearch.setText(search_name);//把搜索内容填入搜索框

        color_select = getResources().getColor(R.color.main_color_red);
        color_normal = getResources().getColor(R.color.text_color_bar);
        searchResultData = new ArrayList<>();

        sortField = WValue.SEARCH_UNSPECIFIED;
        order = WValue.SEARCH_UNSPECIFIED;
        tvColligate.setTextColor(color_select);
        tvSales.setTextColor(color_normal);
        tvPrice.setTextColor(color_normal);

        gvPro.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gvPro.setOnRefreshListener(this);
        mSearchResultAdapter = new SearchResultAdapter(this, searchResultData, R.layout.item_grid_product, new SearchAddCartInterface() {
            @Override
            public void addCart(final ImageView iv_checkbox, final SearchGoodsBo productBo) {
                iv_checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCartNormal(productBo, iv_checkbox);
                    }
                });
            }
        });

        gvPro.setAdapter(mSearchResultAdapter);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (KeyboardUtils.isShown(SearchResultActivity.this)) {
                        KeyboardUtils.hide(SearchResultActivity.this, SearchResultActivity.this.getCurrentFocus());
                    }
                    search_name = etSearch.getText().toString().trim();
                    search(false);
                    return true;
                }
                return false;
            }
        });
        search(false);
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


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        //上拉刷新的操作
        search(true);
        if (hasMore) {
            search(true);
        } else {
            gvPro.onRefreshComplete();
            ToastUtil.shortToast(this, R.string.toast_no_goods);
        }
    }

    private SearchGoodsBo mProductBo;
    /**
     * 要添加购物车的控件
     */
    private ImageView animView;
    private ICart mCartController;//购物车控制器

    /**
     * 普通商品添加购物车
     *
     * @param productBo
     */
    private void addCartNormal(SearchGoodsBo productBo, ImageView v) {
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
            ToastUtil.shortToast(this, R.string.net_error);
        }
    }


    /**
     * 搜索指定商品数据
     *
     * @param isLoadMore
     */
    private void search(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(SearchResultActivity.this)) {
            if (isLoadMore) {
                mProController.getSearchResult(search_name, String.valueOf(pageNum + 1), String.valueOf(pageSize), sortField, order, TAG);
//                ToastUtil.shortToast(SearchResultActivity.this, R.string.toast_no_goods);
//                if (gvPro.isRefreshing()) gvPro.onRefreshComplete();
            } else {
                //TODO  这里是执行搜索，同时保存搜索纪录
                isHideLayer(rlCartAddLoading, false);
                isHideLayer(rlSearchEmpty, true);
                isHideLayer(rlNoNetwork, true);
                mProController.getSearchResult(search_name, String.valueOf(1), String.valueOf(pageSize), sortField, order, TAG);
                SpSearch.saveSearchRecord(search_name);
            }
        } else {
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlSearchEmpty, true);
            isHideLayer(rlNoNetwork, false);
            ToastUtil.shortToast(SearchResultActivity.this, R.string.net_error);
            if (gvPro.isRefreshing()) gvPro.onRefreshComplete();
        }

    }


    @OnClick({R.id.tv_colligate, R.id.tv_sales, R.id.rl_price, R.id.back, R.id.btn_stroll})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tv_colligate:
                if (WValue.SEARCH_UNSPECIFIED.equals(sortField)) {
                    //相同状态不做操作
                } else {
                    if (WValue.PRICE_DOWN.equals(sortField) || WValue.PRICE_UP.equals(sortField)) {
                        ivPrice.setImageResource(R.mipmap.sort_normal);
                    }
                    tvColligate.setTextColor(color_select);
                    tvSales.setTextColor(color_normal);
                    tvPrice.setTextColor(color_normal);
                    sortField = WValue.SEARCH_UNSPECIFIED;
                    order=WValue.SEARCH_UNSPECIFIED;
                    search(false);
                }
                ivPrice.setImageResource(R.mipmap.sort_normal);
                break;
            case R.id.tv_sales:
                if (WValue.SEARCH_SALES.equals(sortField)) {
                    //相同状态不做操作
                } else {
                    if (WValue.PRICE_DOWN.equals(sortField) || WValue.PRICE_UP.equals(sortField)) {
                        ivPrice.setImageResource(R.mipmap.sort_normal);
                    }
                    tvColligate.setTextColor(color_normal);
                    tvSales.setTextColor(color_select);
                    tvPrice.setTextColor(color_normal);
                    sortField = WValue.SEARCH_SALES;
                    order=WValue.SEARCH_UNSPECIFIED;
                    search(false);
                }
                ivPrice.setImageResource(R.mipmap.sort_normal);
                break;
            case R.id.rl_price:
                if(WValue.SEARCH_PRICE.equals(sortField)){
                    switch (order){
                        case WValue.SEARCH_POSITIVE:
                            ivPrice.setImageResource(R.mipmap.sort_below);
                            order=WValue.SEARCH_REVERSE;
                            break;
                        case WValue.SEARCH_REVERSE:
                            ivPrice.setImageResource(R.mipmap.sort_above);
                            order=WValue.SEARCH_POSITIVE;
                            break;
                        default:
                            sortField=WValue.SEARCH_PRICE;
                            order=WValue.SEARCH_REVERSE;
                            ivPrice.setImageResource(R.mipmap.sort_below);
                            break;
                    }
                }else{
                    ivPrice.setImageResource(R.mipmap.sort_below);
                    tvColligate.setTextColor(Color.BLACK);
                    tvSales.setTextColor(Color.BLACK);
                    tvPrice.setTextColor(Color.RED);
                    sortField=WValue.SEARCH_PRICE;
                    order=WValue.SEARCH_REVERSE;
                }
                search(false);
                break;
            case R.id.btn_stroll:
                search(false);
                break;
            case R.id.back:
                EventBus.getDefault().post(new FinishPageEvent(WValue.EVENT_SEARCH));
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new FinishPageEvent(WValue.EVENT_SEARCH));
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        String msg = WValue.STRING_EMPTY;
        switch (result.getAction()) {
            case WAction.SEARCH_RESULT_ACTIVITY_PROS:
                if (isLoadMore) {
                    isLoadMore = false;
                    gvPro.onRefreshComplete();
                }
                if (StringUtil.isEmpty(result.getMsg())) {
                    msg = getText(R.string.toast_no_goods).toString();
                } else {
                    msg = result.getMsg();
                }
                break;
            case WAction.ADD_TO_SHOPPING_CART:
                if (StringUtil.isEmpty(result.getMsg())) {
                    msg = getText(R.string.toast_failure_add_goods).toString();
                } else {
                    msg = result.getMsg();
                }
                break;
        }
        ToastUtil.shortToast(this, msg);
        isHideLayer(rlCartAddLoading, true);
        isHideLayer(rlSearchEmpty, true);
        isHideLayer(rlNoNetwork, false);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        String action = result.getAction();
        if (WAction.SEARCH_RESULT_ACTIVITY_PROS.equals(action)) {
            SearchResult hotWordResult = (SearchResult) result;
            SearchResultVo data = hotWordResult.getData();
            if (data == null) {
                gvPro.onRefreshComplete();
                return;
            }
            hasMore = data.isHasMore();
            pageNum = data.getPageNo();

            List<SearchGoodsBo> tempProList = data.getResults();
            if (!ListUtil.isEmpty(tempProList)) {
                if (!isLoadMore) {
                    searchResultData.clear();
                }
                searchResultData.addAll(tempProList);
            }

            //是否展示空背景
            if (searchResultData.size() == 0) {
                isHideLayer(rlSearchEmpty, false);
                ToastUtil.shortToast(this, R.string.toast_search_no_goods);
            } else {
                isHideLayer(rlSearchEmpty, true);
                mSearchResultAdapter.notifyDataSetChanged();
            }
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlNoNetwork, true);
            //结束刷新加载布局
            if (gvPro.isRefreshing())
                gvPro.onRefreshComplete();
        }
    }
}
