package com.eme.mas.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.CategoryAdapter;
import com.eme.mas.adapter.SearchResultAdapter;
import com.eme.mas.adapter.SpecAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.MListView;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.data.sql.DataRow;
import com.eme.mas.data.sql.DataTable;
import com.eme.mas.data.sql.QueryBuilder;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.AddCartResult;
import com.eme.mas.model.CategoryFilterResult;
import com.eme.mas.model.CategoryResult;
import com.eme.mas.model.CategorySearchResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.CategorySearchVo;
import com.eme.mas.model.entity.CategorySpecVo;
import com.eme.mas.model.entity.SearchGoodsBo;
import com.eme.mas.model.entity.home.HomeHotGoodsBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.StringUtil;
import com.eme.mas.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.owater.library.CircleTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 分类页面
 * <p>
 * Created by dijiaoliang on 16/7/20.
 */
@WLayout(layoutId = R.layout.activity_category)
public class CategoryActivity extends BaseCartActivity implements AdapterView.OnItemClickListener, View.OnClickListener, PullToRefreshBase.OnRefreshListener {

    private final static String TAG = CategoryActivity.class.getSimpleName();

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.cline)
    ImageView cline;
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
    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.tv_brand)
    TextView tvBrand;
    @Bind(R.id.rl_brand)
    RelativeLayout rlBrand;
    @Bind(R.id.iv_brand)
    ImageView ivBrand;
    @Bind(R.id.ll_brand)
    LinearLayout llBrand;
    @Bind(R.id.tv_brand_content)
    TextView tvBrandContent;
    @Bind(R.id.tv_odor)
    TextView tvOdor;
    @Bind(R.id.iv_odor)
    ImageView ivOdor;
    @Bind(R.id.ll_odor)
    LinearLayout llOdor;
    @Bind(R.id.tv_odor_content)
    TextView tvOdorContent;
    @Bind(R.id.rl_odor)
    RelativeLayout rlOdor;
    @Bind(R.id.tv_proarea)
    TextView tvProarea;
    @Bind(R.id.iv_proarea)
    ImageView ivProarea;
    @Bind(R.id.ll_proarea)
    LinearLayout llProarea;
    @Bind(R.id.tv_proarea_content)
    TextView tvProareaContent;
    @Bind(R.id.rl_proarea)
    RelativeLayout rlProarea;
    @Bind(R.id.tv_price_range)
    TextView tvPriceRange;
    @Bind(R.id.iv_price_range)
    ImageView ivPriceRange;
    @Bind(R.id.ll_price_range)
    LinearLayout llPriceRange;
    @Bind(R.id.tv_price_range_content)
    TextView tvPriceRangeContent;
    @Bind(R.id.rl_price_range)
    RelativeLayout rlPriceRange;
    @Bind(R.id.gv_category_result)
    PullToRefreshGridView gvCategoryResult;
    @Bind(R.id.rl_cart_add_loading)
    RelativeLayout rlCartAddLoading;


    private List<SearchGoodsBo> cateSearchData;
    private SearchResultAdapter mCateAdapter;

    private IProduct mProController;//商品控制器

    private PopupWindow mPop;//规格按钮弹出框popupwindow
    private PopupWindow mCatePop;//标题栏弹出框popupwindow

    private View pop_spec;//规格按钮弹出框控件
    private View pop_cate;//标题栏弹出框控件

    private SpecAdapter specAadapter;//规格适配器
    private CategoryAdapter cateAadapter;//分类适配器
    private List<DataRow> specData;//规格数据
    private List<CategoryFilterResult.DataBean.CategoryConditionBean> cateData;//分类数据
    private DataTable wineTable;//酒的容器
    private DataTable specTable;//酒规格的容器
    private String cate_name;//当前酒的名称
    private String cate_id;//当前酒类id

    private String specFlag;

    private List<DataRow> specBrand;//选中的规格数据
    private List<DataRow> specFlaver;
    private List<DataRow> specPlace;
    private List<DataRow> specPrice;


    /**
     * 排序的状态参数
     */
    private int start_num = 1;//页码
    private String hasMore;//是否还有更多
    private boolean isLoadMore;//是否加载更多
    private String order_condition;//目前排序条件

    private int tv_color_normal;
    private int tv_color_select;

    /**
     * 要添加购物车的控件
     */
    private ImageView animView;
    private HomeHotGoodsBo mProductBo;


    private String conditionId,conditionName;//主分类id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        ((BaseImpl) mProController).cancelRequestByTag(TAG);
        super.onDestroy();
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
        rl_anim = (RelativeLayout) findViewById(R.id.rl_anim);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        mProController = mController.getProduct(this);//初始化控制器
        tv_color_normal = getResources().getColor(R.color.text_color_bar);
        tv_color_select = getResources().getColor(R.color.main_color_red);

        specBrand = new ArrayList<>();
        specFlaver = new ArrayList<>();
        specPlace = new ArrayList<>();
        specPrice = new ArrayList<>();

        cateSearchData = new ArrayList();
    }

    @Override
    public void afterContentView() {
        super.afterContentView();

        conditionId=getIntent().getStringExtra("wine_id");
        conditionName=getIntent().getStringExtra("wine_name");
        if(!StringUtil.isEmpty(conditionName)){
            tvBarTitle.setText(conditionName);//设置标题
        }

        if (start_num != 1) start_num = 1;
        order_condition = WValue.WEIGHT;
        tvColligate.setTextColor(Color.RED);
        tvSales.setTextColor(Color.BLACK);
        tvPrice.setTextColor(Color.BLACK);
        gvCategoryResult.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        gvCategoryResult.setOnRefreshListener(this);

        arrowBarTitle.setImageResource(R.mipmap.arrow_spec_normal);

        mCateAdapter = new SearchResultAdapter(this, cateSearchData, R.layout.item_grid_product, null);
        gvCategoryResult.setAdapter(mCateAdapter);

        gvCategoryResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toProDetailActivity(cateSearchData.get(position).getSkuId());
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        pop_spec = inflater.inflate(R.layout.pop_spec, null);
        pop_spec.findViewById(R.id.bg_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPop.isShowing()) mPop.dismiss();
            }
        });
        if (specData == null) {
            specData = new ArrayList<>();
        }
        GridView gv_spec = (GridView) pop_spec.findViewById(R.id.gv_spec);
        if (specAadapter == null) {
            specAadapter = new SpecAdapter(this, specData, R.layout.item_spec);
        }
        gv_spec.setAdapter(specAadapter);
        gv_spec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_spec = (TextView) view.findViewById(R.id.tv_spec_name);
                DataRow row_spec = specData.get(position);
                if (tv_spec.isSelected()) {
                    tv_spec.setSelected(false);
                    tv_spec.setTextColor(tv_color_normal);
//                    specData.remove(row_spec);
                    specAadapter.deleteSpecRow(row_spec);
                } else {
                    tv_spec.setSelected(true);
                    tv_spec.setTextColor(tv_color_select);
                    specAadapter.addSpecRow(row_spec);
                }

            }
        });
        pop_spec.findViewById(R.id.btn_reset).setOnClickListener(this);
        pop_spec.findViewById(R.id.btn_confirm).setOnClickListener(this);

        pop_cate = inflater.inflate(R.layout.pop_cate_title, null);
        pop_cate.findViewById(R.id.bg_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatePop.isShowing()) mCatePop.dismiss();
            }
        });
        if (cateData == null) {
            cateData = new ArrayList<>();
        }
        GridView gv_cate = (GridView) pop_cate.findViewById(R.id.gv_cate);
        if (cateAadapter == null) {
            cateAadapter = new CategoryAdapter(this, cateData, R.layout.item_cate);
        }
        gv_cate.setAdapter(cateAadapter);
        gv_cate.setOnItemClickListener(this);

        /**初始化规格弹出框*/
        mPop = new PopupWindow(pop_spec, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPop.setAnimationStyle(R.style.Popup_Animation);
        mPop.setBackgroundDrawable(new ColorDrawable(0));
//                mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switch (specFlag) {
                    case WValue.BRAND:
                        rlBrand.setSelected(false);
                        tvBrand.setTextColor(tv_color_normal);
                        ivBrand.setImageResource(R.mipmap.arrow_spec_normal);
                        tvBrandContent.setSelected(false);
                        tvBrandContent.setTextColor(tv_color_select);
                        break;
                    case WValue.FLAVOR:
                        rlOdor.setSelected(false);
                        tvOdor.setTextColor(tv_color_normal);
                        ivOdor.setImageResource(R.mipmap.arrow_spec_normal);
                        tvOdorContent.setSelected(false);
                        tvOdorContent.setTextColor(tv_color_select);
                        break;
                    case WValue.PLACE:
                        rlProarea.setSelected(false);
                        tvProarea.setTextColor(tv_color_normal);
                        ivProarea.setImageResource(R.mipmap.arrow_spec_normal);
                        tvProareaContent.setSelected(false);
                        tvProareaContent.setTextColor(tv_color_select);
                        break;
                    case WValue.PRICE:
                        rlPriceRange.setSelected(false);
                        tvPriceRange.setTextColor(tv_color_normal);
                        ivPriceRange.setImageResource(R.mipmap.arrow_spec_normal);
                        tvPriceRangeContent.setSelected(false);
                        tvPriceRangeContent.setTextColor(tv_color_select);
                        break;
                }
            }
        });

        /**初始化酒分类弹出框*/
        mCatePop = new PopupWindow(pop_cate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mCatePop.setAnimationStyle(R.style.Popup_Animation);
        mCatePop.setBackgroundDrawable(new ColorDrawable(0));
//                mPop.setBackgroundDrawable(new BitmapDrawable());
        mCatePop.setOutsideTouchable(true);
        mCatePop.setFocusable(true);
        mCatePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                arrowBarTitle.setImageResource(R.mipmap.arrow_spec_normal);
            }
        });


//        /**
//         * 判断数据库是否初始化，如果没有请求分类信息，初始化数据库
//         */
//        if (!MasApplication.getInstance().isInitCategory()) {
//            /**判断网络状态*/
//            if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
//                isHideLayer(rlCartAddLoading, false);
//                mProController.getProductCategory(TAG);
//            }
//        } else {
//            //获取分类入口，初始化界面
//            cate_name = getIntent().getStringExtra("wine_name");
//            cate_id = getIntent().getStringExtra("wine_id");
//            if (cate_name == null || TextUtils.isEmpty(cate_name) || cate_id == null || TextUtils.isEmpty(cate_id)) {
//                //请求分类搜索数据
//                String sql = "select * from wine_category";
//                wineTable = new QueryBuilder(sql).executeDataTable();
////                currentWineId = (String) wineTable.get(0).get(0);
//                if (wineTable.getRowCount() >= 1) {
//                    cate_id = (String) wineTable.get(0).get(0);
//                    cate_name = (String) wineTable.get(0).get(1);
//                }
//                tvBarTitle.setText(cate_name);
//            } else {
////                currentWineId=cate_id;
//                tvBarTitle.setText(cate_name);
//            }
//            requestCategorySearchData(false);
//        }

        requestCategoryFilter();//请求筛选项数据

    }

    /**
     * 获取分类筛选项信息
     */
    private void requestCategoryFilter(){

        if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
            isHideLayer(rlCartAddLoading, false);
            mProController.getCategoryFilter(conditionId,TAG);
        }else{
            ToastUtil.shortToast(this,R.string.net_error);
        }
    }


    /**
     * 请求分类搜索数据
     *
     * @param loadMore
     */
    private void requestCategorySearchData(boolean loadMore) {
        if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
            isLoadMore = loadMore;
            isHideLayer(rlCartAddLoading, false);
            if (specBrand.size() == 0 && specFlaver.size() == 0 && specPlace.size() == 0 && specPrice.size() == 0) {
                if (cate_id != null && order_condition != null) {
                    if (isLoadMore) {
                        mProController.getCategorySearchData(cate_id, order_condition, String.valueOf(start_num + 1), TAG);
                    } else {
                        mProController.getCategorySearchData(cate_id, order_condition, String.valueOf(start_num), TAG);
                    }
                } else {
                    if (gvCategoryResult.isRefreshing()) {
                        gvCategoryResult.onRefreshComplete();
                    }
                }
            } else {
                StringBuilder builder = new StringBuilder();
                if (specBrand.size() != 0) {
                    for (DataRow row : specBrand) {
                        builder.append(row.getString("id")).append(",");
                    }
                }
                if (specFlaver.size() != 0) {
                    for (DataRow row : specFlaver) {
                        builder.append(row.getString("id")).append(",");
                    }
                }
                if (specPlace.size() != 0) {
                    for (DataRow row : specPlace) {
                        builder.append(row.getString("id")).append(",");
                    }
                }
                if (specPrice.size() != 0) {
                    for (DataRow row : specPrice) {
                        builder.append(row.getString("id")).append(",");
                    }
                }
                String cateids = builder.deleteCharAt(builder.length() - 1).toString();
                if (isLoadMore) {
                    mProController.getCategorySearchData(cateids, order_condition, String.valueOf(start_num + 1), TAG);
                } else {
                    mProController.getCategorySearchData(cateids, order_condition, String.valueOf(start_num), TAG);
                }
            }
        } else {
            if (gvCategoryResult.isRefreshing()) {
                gvCategoryResult.onRefreshComplete();
            }
            isHideLayer(rlCartAddLoading, true);
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    /**
     * 跳转商品详情
     *
     * @param product_id
     */
    private void toProDetailActivity(String product_id) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            Intent intent = new Intent(this, ProDetailActivity.class);
            intent.putExtra("product_id", product_id);
            startActivity(intent);
        } else {
            ToastUtil.shortToast(this, R.string.net_error);
        }
    }


    @OnClick({R.id.back, R.id.rl_brand, R.id.rl_odor, R.id.rl_proarea, R.id.rl_price_range, R.id.ll_bar_title, R.id.tv_colligate, R.id.tv_sales, R.id.rl_price,R.id.search})
    void click(View v) {
        int count;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_brand:
                updateSpec(WValue.BRAND);
                break;
            case R.id.rl_odor:
                updateSpec(WValue.FLAVOR);
                break;
            case R.id.rl_proarea:
                updateSpec(WValue.PLACE);
                break;
            case R.id.rl_price_range:
                updateSpec(WValue.PRICE);
                break;
            case R.id.ll_bar_title:
                if (cateData.size() == 0) {
                    if (wineTable == null || wineTable.getRowCount() == 0) {
                        String sql = "select * from wine_category";
                        wineTable = new QueryBuilder(sql).executeDataTable();
                    }
                    count = wineTable.getRowCount();
                    for (int i = 0; i < count; i++) {
//                        cateData.add(wineTable.get(i));
                    }
                }
                cateAadapter.notifyDataSetChanged();
                mCatePop.showAsDropDown(barCline);
                mCatePop.update();
                arrowBarTitle.setImageResource(R.mipmap.arrow_spec_pressed);
                break;
            case R.id.tv_colligate:
                if (WValue.WEIGHT.equals(order_condition)) {
                    //相同状态不做操作
                } else {
                    if (WValue.PRICE_DOWN.equals(order_condition) || WValue.PRICE_UP.equals(order_condition)) {
                        ivPrice.setImageResource(R.mipmap.sort_normal);
                    }
                    tvColligate.setTextColor(tv_color_select);
                    tvSales.setTextColor(tv_color_normal);
                    tvPrice.setTextColor(tv_color_normal);
                    order_condition = WValue.WEIGHT;

//                    search(false);
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
                    tvColligate.setTextColor(tv_color_normal);
                    tvSales.setTextColor(tv_color_select);
                    tvPrice.setTextColor(tv_color_normal);
                    order_condition = WValue.SALE;

//                    search(false);
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
//                search(false);
                break;
            case R.id.search:
                startActivity(new Intent(this,SearchActivity.class));
                break;
        }
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
                iCart.addToCart(productBo.getSkuId(), productBo.getSkuId(), String.valueOf(1), WValue.PHONE_NORMAL, TAG);
            } else {
                login();
            }
        } else {
            ToastUtil.shortToast(this, R.string.net_error);
        }
    }


    /**
     * 更新规格数据
     *
     * @param spec
     */
    private void updateSpec(String spec) {
        specFlag = spec;
        switch (spec) {
            case WValue.BRAND:
                String sqlBrand = "select * from wine_brand where parentid=?";
                specTable = new QueryBuilder(sqlBrand, cate_id).executeDataTable();
                rlBrand.setSelected(true);
                tvBrand.setTextColor(Color.WHITE);
                ivBrand.setImageResource(R.mipmap.arrow_spec_white);
                tvBrandContent.setSelected(true);
                tvBrandContent.setTextColor(Color.WHITE);
                specAadapter.setSpecRowData(specBrand);
                break;
            case WValue.FLAVOR:
                String sqlFlavor = "select * from wine_flavor where parentid=?";
                specTable = new QueryBuilder(sqlFlavor, cate_id).executeDataTable();
                rlOdor.setSelected(true);
                tvOdor.setTextColor(Color.WHITE);
                ivOdor.setImageResource(R.mipmap.arrow_spec_white);
                tvOdorContent.setSelected(true);
                tvOdorContent.setTextColor(Color.WHITE);
                specAadapter.setSpecRowData(specFlaver);
                break;
            case WValue.PLACE:
                String sqlPlace = "select * from wine_place where parentid=?";
                specTable = new QueryBuilder(sqlPlace, cate_id).executeDataTable();
                rlProarea.setSelected(true);
                tvProarea.setTextColor(Color.WHITE);
                ivProarea.setImageResource(R.mipmap.arrow_spec_white);
                tvProareaContent.setSelected(true);
                tvProareaContent.setTextColor(Color.WHITE);
                specAadapter.setSpecRowData(specPlace);
                break;
            case WValue.PRICE:
                String sqlPrice = "select * from wine_price where parentid=?";
                specTable = new QueryBuilder(sqlPrice, cate_id).executeDataTable();
                rlPriceRange.setSelected(true);
                tvPriceRange.setTextColor(Color.WHITE);
                ivPriceRange.setImageResource(R.mipmap.arrow_spec_white);
                tvPriceRangeContent.setSelected(true);
                tvPriceRangeContent.setTextColor(Color.WHITE);
                specAadapter.setSpecRowData(specPrice);
                break;
        }

        int count = specTable.getRowCount();
        specData.clear();
        for (int i = 0; i < count; i++) {
            specData.add(specTable.get(i));
        }
        specAadapter.notifyDataSetChanged();
        mPop.showAsDropDown(cline);
        mPop.update();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String currentCateId = cateData.get(position).getString("id");
//        if (currentCateId.equals(cate_id)) {
//            mCatePop.dismiss();
//        } else {
//            tvBrandContent.setText("");
//            tvBrandContent.setVisibility(View.GONE);
//            tvOdorContent.setText("");
//            tvOdorContent.setVisibility(View.GONE);
//            tvProareaContent.setText("");
//            tvProareaContent.setVisibility(View.GONE);
//            tvPriceRangeContent.setText("");
//            tvPriceRangeContent.setVisibility(View.GONE);
//            specBrand.clear();
//            specPrice.clear();
//            specPlace.clear();
//            specFlaver.clear();
////            cate_name = cateData.get(position).getString(1);
            tvBarTitle.setText(cateData.get(position).getName());
////            cate_id = (String) cateData.get(position).get("id");
//            mCatePop.dismiss();
//        }
        //TODO 发送请求商品数据
//        requestCategorySearchData(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                StringBuilder builder;
                switch (specFlag) {
                    case WValue.BRAND:
                        specBrand.clear();
                        specBrand.addAll(specAadapter.getSpecRowData());
                        int visiableBrand = tvBrandContent.getVisibility();
                        if (specBrand.size() != 0) {
                            builder = new StringBuilder();
                            for (DataRow row : specBrand) {
                                builder.append("、" + row.get(1));
                            }
                            builder.deleteCharAt(0);
                            if (visiableBrand == View.GONE) {
                                tvBrandContent.setVisibility(View.VISIBLE);
                            }
                            tvBrandContent.setText(builder);
                        } else {
                            if (visiableBrand == View.VISIBLE) {
                                tvBrandContent.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case WValue.FLAVOR:
                        specFlaver.clear();
                        specFlaver.addAll(specAadapter.getSpecRowData());
                        int visiableFlavor = tvOdorContent.getVisibility();
                        if (specFlaver.size() != 0) {
                            builder = new StringBuilder();
                            for (DataRow row : specFlaver) {
                                builder.append("、" + row.get(1));
                            }
                            builder.deleteCharAt(0);
                            if (visiableFlavor == View.GONE) {
                                tvOdorContent.setVisibility(View.VISIBLE);
                            }
                            tvOdorContent.setText(builder);
                        } else {
                            if (visiableFlavor == View.VISIBLE) {
                                tvOdorContent.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case WValue.PLACE:
                        specPlace.clear();
                        specPlace.addAll(specAadapter.getSpecRowData());
                        int visiablePlace = tvProareaContent.getVisibility();
                        if (specPlace.size() != 0) {
                            builder = new StringBuilder();
                            for (DataRow row : specPlace) {
                                builder.append("、" + row.get(1));
                            }
                            builder.deleteCharAt(0);
                            if (visiablePlace == View.GONE) {
                                tvProareaContent.setVisibility(View.VISIBLE);
                            }
                            tvProareaContent.setText(builder);
                        } else {
                            if (visiablePlace == View.VISIBLE) {
                                tvProareaContent.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case WValue.PRICE:
                        specPrice.clear();
                        specPrice.addAll(specAadapter.getSpecRowData());
                        int visiablePrice = tvPriceRangeContent.getVisibility();
                        if (specPrice.size() != 0) {
                            builder = new StringBuilder();
                            for (DataRow row : specPrice) {
                                builder.append("、" + row.get(1));
                            }
                            builder.deleteCharAt(0);
                            if (visiablePrice == View.GONE) {
                                tvPriceRangeContent.setVisibility(View.VISIBLE);
                            }
                            tvPriceRangeContent.setText(builder);
                        } else {
                            if (visiablePrice == View.VISIBLE) {
                                tvPriceRangeContent.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
                specAadapter.notifyDataSetChanged();
                mPop.dismiss();
                requestCategorySearchData(false);
                break;
            case R.id.btn_reset:
                switch (specFlag) {
                    case WValue.BRAND:
                        specBrand.clear();
                        if (tvBrandContent.getVisibility() == View.VISIBLE) {
                            tvBrandContent.setVisibility(View.GONE);
                        }
                        break;
                    case WValue.FLAVOR:
                        specFlaver.clear();
                        if (tvOdorContent.getVisibility() == View.VISIBLE) {
                            tvOdorContent.setVisibility(View.GONE);
                        }
                        break;
                    case WValue.PLACE:
                        specPlace.clear();
                        if (tvProareaContent.getVisibility() == View.VISIBLE) {
                            tvProareaContent.setVisibility(View.GONE);
                        }
                        break;
                    case WValue.PRICE:
                        specPrice.clear();
                        if (tvPriceRangeContent.getVisibility() == View.VISIBLE) {
                            tvPriceRangeContent.setVisibility(View.GONE);
                        }
                        break;
                }
                specAadapter.deleteAllSpecRow();
                specAadapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        requestCategorySearchData(true);
    }


    /**
     * 更新界面中的分类信息和筛选项信息
     * @param dataBean
     */
    private void updateCondition(CategoryFilterResult.DataBean dataBean) {
        if(dataBean==null){
            ToastUtil.shortToast(this,R.string.toast_category_filter_empty);
            return;
        }

        // TODO 更新一级分类的数据
        if(cateData.size()!=0){
            cateData.clear();
        }
        cateData.addAll(dataBean.getCategoryCondition());
        cateAadapter.notifyDataSetChanged();

        // TODO: 17/2/3  更新二级分类的数据


    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        switch (result.getAction()) {
            case WAction.ADD_TO_SHOPPING_CART:
                ToastUtil.shortToast(MasApplication.getInstance(), "添加失败");
                break;
        }
        isHideLayer(rlCartAddLoading, true);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        String action = result.getAction();
        if (WAction.CATEGORY_ACTIVITY_DATA.equals(action)) {
            List<CategorySpecVo> bean = ((CategoryResult) result).getData();
            if (bean != null) {
                SqlUtil.initCategoryData(bean);
                String sql = "select * from wine_category";
                wineTable = new QueryBuilder(sql).executeDataTable();
                //初始化酒de
                cate_name = getIntent().getStringExtra("cate_name");
                if (cate_name == null || WValue.STRING_EMPTY.equals(cate_name)) {
                    cate_id = (String) wineTable.get(0).get(0);
                    cate_name = (String) wineTable.get(0).get(1);
                    tvBarTitle.setText(cate_name);
                } else {
                    int count = wineTable.getRowCount();
                    for (int i = 0; i < count; i++) {
                        if (cate_name.equals(wineTable.get(i).get(1))) {
                            cate_id = (String) wineTable.get(i).get(0);
                            tvBarTitle.setText(cate_name);
                        }
                    }
                }
                requestCategorySearchData(false);
            } else {
            }
        } else if (WAction.CATEGORY_ACTIVITY_SEARCH_DATA.equals(action)) {
            if (gvCategoryResult.isRefreshing()) gvCategoryResult.onRefreshComplete();
            CategorySearchVo bean = ((CategorySearchResult) result).getData();
            if (bean != null) {
                hasMore = bean.getHasMore();
                if (!isLoadMore) {
                    cateSearchData.clear();
                }
//                cateSearchData.addAll(bean.getProduct_list());
//                mCateAdapter.notifyDataSetChanged();
                isHideLayer(rlCartAddLoading, true);
            } else {
            }
        } else if (WAction.ADD_TO_SHOPPING_CART.equals(action)) {
            String cartId = ((AddCartResult) result).getData();
            if (cartId != null) {
                //保存购物车数据到数据库
                if (mProductBo != null) {
//                    SqlUtil.addCartData(cartId, mProductBo);
                }
                addCart(animView);
                isHideLayer(rlCartAddLoading, true);
            }
        }

        switch (result.getAction()){
            case WAction.CATEGORY_FILTER_DATA:
                //获取分类的筛选项信息，然后更新界面
                updateCondition(((CategoryFilterResult)result).getData());
                break;
        }

    }
}
