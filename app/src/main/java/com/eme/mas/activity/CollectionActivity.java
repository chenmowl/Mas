package com.eme.mas.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.chad.library.adapter.base.BaseQuickSwipeAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.swipe.SwipeLayout;
import com.chad.library.adapter.base.swipe.util.Attributes;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.CollectionAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.MListView;
import com.eme.mas.model.CollectionResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.ProductBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.owater.library.CircleTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

/**
 * 我的收藏
 * Created by zulei on 16/9/7.
 */
@WLayout(layoutId = R.layout.activity_collection)
public class CollectionActivity extends BaseCartActivity {

    private final static String TAG = CollectionActivity.class.getSimpleName();

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.rv_collection_list)
    RecyclerView rvCollectionList;
    @Bind(R.id.prl_collection)
    PullRefreshLayout prlCollection;
    @Bind(R.id.ll_av_loading_transparent_full)
    LinearLayout llAvLoading;

    private CollectionAdapter collectionAdapter;
    private IProduct mProController;
    private List<ProductBo> collectionBoList;
    private int page;//页数
    private boolean hadMore;//是否有更多商品
    private boolean isLoadMore; //区分上下拉
    private View viewNoData,viewNetErr,loadOverView;

    private int deletePosition;

    private final static int ITEM_MAX = 5;

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
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
        initListener();
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
    protected void onResume() {
        super.onResume();
        getCollectionData(1,true);
    }

    private void initView() {
        barTitle.setText(R.string.collection_title);

        rvCollectionList.setLayoutManager(new LinearLayoutManager(this));
        rvCollectionList.setItemAnimator(new OvershootInLeftAnimator());
        prlCollection.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        prlCollection.setColor(Color.GRAY);


        viewNoData = getLayoutInflater().inflate(R.layout.collection_no_goods, (ViewGroup) rvCollectionList.getParent(), false);

        viewNetErr = getLayoutInflater().inflate(R.layout.no_network_loading_full, (ViewGroup) rvCollectionList.getParent(), false);
        viewNetErr.findViewById(R.id.rl_no_network).setVisibility(View.VISIBLE);
        Button btnScroll = (Button)viewNetErr.findViewById(R.id.btn_stroll);
        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCollectionData(1,true);
            }
        });

    }

    private void initListener() {
        //recycleview条目与子控件点击事件
        //method1:OnItemClickListener(OnItemClickListener.Mode.Swipe)
        //method2:OnItemClickListener()  --OnItemClickListener.Mode.Normal
        rvCollectionList.addOnItemTouchListener(new OnItemClickListener(OnItemClickListener.Mode.Swipe) {
            @Override
            public void SimpleOnItemClick(RecyclerView.Adapter adapter, View view, int position) {
                BaseQuickSwipeAdapter ad = (BaseQuickSwipeAdapter) adapter;
                SwipeLayout swipeLayout = (SwipeLayout) view;
                if(ad.isOpen(swipeLayout.getPosition())){
                    ad.closeAllItems();
                }else{
                    toProductDetail(((ProductBo)ad.getItem(position)).getProduct_id());
                }
            }
            @Override
            public void onItemChildClick(RecyclerView.Adapter adapter, View parent,View view, int position) {
                super.onItemChildClick(adapter,parent, view, position);
                BaseQuickSwipeAdapter ad = (BaseQuickSwipeAdapter) adapter;
                ProductBo productBo = (ProductBo) ad.getItem(position);
                switch (view.getId()) {
                    case R.id.iv_ic_add:
                        ToastUtil.shortToast(CollectionActivity.this,"add"+position);
                        break;
                    case R.id.tv_ic_delete:
                        SwipeLayout swipeLayout = (SwipeLayout) parent;
                        SwipeLayout.Status status = swipeLayout.getOpenStatus();
                        if (status == SwipeLayout.Status.Close) {
                            toProductDetail(((ProductBo)ad.getItem(position)).getProduct_id());
                        }else{
                            deletePosition = position;
                            cancelCollecteProduct(productBo.getProduct_id());
                        }

                        break;
                    default:
                        break;
                }
            }
        });


        //下拉刷新
        prlCollection.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                page = 1;
                getCollectionData(page,false);
            }
        });

        //上拉加载，如不需要，则不写即可
        collectionAdapter.setOnLoadMoreListener(loadMoreListener);

    }

    BaseQuickSwipeAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickSwipeAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            isLoadMore = true;
            getCollectionData(page,false);
        }
    };


    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
            collectionAdapter.closeAllItems();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
            //testAdapter.closeAllItems();
        }
    };

    private void initData() {
        isHideLayer(llAvLoading,true);
        isLoadMore = false;
        page = 1;
        mProController = mController.getProduct(this);
        collectionBoList = new ArrayList();

        collectionAdapter = new CollectionAdapter(this,collectionBoList);
        collectionAdapter.isFirstOnly(true);
        collectionAdapter.openLoadAnimation(BaseQuickSwipeAdapter.ALPHAIN);
        collectionAdapter.setMode(Attributes.Mode.Single);
        collectionAdapter.openLoadMore(ITEM_MAX);
        //设置空数据页面
        collectionAdapter.setNoDataView(viewNoData);
        //设置网络异常页面
        collectionAdapter.setNetErrView(viewNetErr);
        rvCollectionList.setAdapter(collectionAdapter);
        rvCollectionList.addOnScrollListener(onScrollListener);

    }


    private void setLoadOverView(){
        if(loadOverView == null){
            loadOverView = getLayoutInflater().inflate(R.layout.layout_load_over, (ViewGroup) rvCollectionList.getParent(), false);
        }
        int footViewCount = collectionAdapter.getFooterLayoutCount();
        if(footViewCount==0){
            collectionAdapter.addFooterView(loadOverView);
        }
        collectionAdapter.setOnLoadMoreListener(null);
    }

    private void getCollectionData(int page,boolean showAV){
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            if(showAV){
                isHideLayer(llAvLoading, false);
            }
            mProController.getCollectionList(String.valueOf(page),TAG);
        } else {
            isHideLayer(llAvLoading,true);
            updateNetErrView();
        }
    }


    private void toProductDetail(String product_id){
        if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
            Intent intent = new Intent(CollectionActivity.this, ProDetailActivity.class);
            intent.putExtra("product_id",product_id);
            startActivity(intent);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), "当前网络不可用");
        }
    }

    private void cancelCollecteProduct(String product_id){
        if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
            isHideLayer(llAvLoading,false);
            mProController.cancelCollecteProduct(product_id,TAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    private void updateNetErrView(){
        prlCollection.setRefreshing(false);
        if (isLoadMore) {
            //上拉
            collectionAdapter.showLoadMoreFailedView();
        } else {
            //下拉
            collectionAdapter.showNetErrView();
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoading,true);
        switch (result.getAction()) {
            case WAction.GET_COLLECTION_DATA:
                updateNetErrView();
                break;
            case WAction.CANCEL_COLLECTE_PRODUCT:
                ToastUtil.shortToast(MasApplication.getInstance(), "删除收藏失败");
                break;
        }

    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoading,true);
        switch (result.getAction()) {
            case WAction.GET_COLLECTION_DATA:
                //FIXME 有个隐藏的BUG,在网络不好的情况，连续进行上拉下拉。
                CollectionResult collectionResult = (CollectionResult) result;
                hadMore = "1".equals(collectionResult.getData().getHasMore());
                List<ProductBo> bos = collectionResult.getData().getProductList();
                if (isLoadMore) {
                    //上拉
                    collectionAdapter.loadComplete();
                    collectionAdapter.addData(bos);
                } else {
                    //下拉
                    prlCollection.setRefreshing(false);
                    collectionAdapter.getData().clear();
                    collectionAdapter.addData(bos);
                }

                if(hadMore){
                    page++;
                    collectionAdapter.removeFooterView(loadOverView);
                    collectionAdapter.openLoadMore(ITEM_MAX);
                    if (null == collectionAdapter.getOnLoadMoreListener()) {
                        collectionAdapter.setOnLoadMoreListener(loadMoreListener);
                    }
                }else{
                    if(collectionAdapter.getData().size()>3){
                        setLoadOverView();
                    }
                }
                break;

            case WAction.CANCEL_COLLECTE_PRODUCT:
                collectionAdapter.remove(deletePosition);
                if(hadMore){
                   if(collectionAdapter.getData().size()<=ITEM_MAX){
                       isLoadMore = true;
                       getCollectionData(1,false);
                   }
                }
                break;
        }
    }

    private void getTestData() {
        ProductBo bo = new ProductBo();
        bo.setProduct_name("82年雪碧");
        bo.setSubtitle("500ml");
        bo.setProduct_price("888.88");
        bo.setGoods_show("1");
        bo.setProduct_channel("2");

        //list.add(bo);

        ProductBo bo2 = new ProductBo();
        bo2.setProduct_name("83年雪碧");
        bo2.setSubtitle("200ml");
        bo2.setProduct_price("222.22");
        bo2.setGoods_show("2");
        bo2.setProduct_channel("2");

        //list.add(bo2);


    }


}