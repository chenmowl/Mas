package com.eme.mas.demo_recycle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.baoyz.widget.PullRefreshLayout;
import com.chad.library.adapter.base.BaseQuickSwipeAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.swipe.SwipeLayout;
import com.chad.library.adapter.base.swipe.util.Attributes;
import com.eme.mas.R;
import com.eme.mas.activity.BaseActivity;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.model.Result;
import com.eme.mas.utils.ToastUtil;
import com.eme.mas.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

/**
 * 集成上下拉刷新，侧滑删除的RecycleView Demo
 * 若无侧滑，分页，可参考MyAddressActivity.java
 * auth:zulei
 * date:2016-9-5
 */
@WLayout(layoutId = R.layout.activity_test)
public class TestActivity extends BaseActivity {

    @Bind(R.id.rv_test_list)
    RecyclerView rvTestList;
    @Bind(R.id.prl_test)
    PullRefreshLayout prlTest;

    private TestAdapter testAdapter;
    private IOrder iOrder;
    private List<String> list;

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
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        ViewUtil.setStatusBarColor(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        //设置布局管理
        rvTestList.setLayoutManager(new GridLayoutManager(this,2));
        //设置添加删除item动画效果,引用jp.wasabeef:recyclerview-animators:2.2.3
        rvTestList.setItemAnimator(new OvershootInLeftAnimator());
        //设置下拉刷新图标样式
        prlTest.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        prlTest.setColor(Color.GRAY);

    }

    private void initData() {

        list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");

        testAdapter = new TestAdapter(list);

        //设置item加载动画显示模式，true只显示1次，false每次都显示
        testAdapter.isFirstOnly(true);
        //设置item显示动画
        testAdapter.openLoadAnimation(BaseQuickSwipeAdapter.ALPHAIN);
        //设置侧滑显示模式,Single 同时只允许一个item可滑动,Multiple 则允许多个
        testAdapter.setMode(Attributes.Mode.Single);
        //设置item数据大于n条时，显示加载更多的footview
        testAdapter.openLoadMore(5);

        //设置空页面,显示时无需主动调用，无数据时自动加载
        //View viewNoData = getLayoutInflater().inflate(R.layout.layout_no_data_full, (ViewGroup) recyclerView.getParent(), false);
        //testAdapter.setNoDataView(viewNoData);

        //设置网络异常页面,显示时需手动调用showNetErrView
        //View viewNetErr = getLayoutInflater().inflate(R.layout.no_network_loading_full, (ViewGroup) recyclerView.getParent(), false);
        //testAdapter.setNoDataView(viewNetErr);


        rvTestList.setAdapter(testAdapter);
        //设置recycleview滚动监听
        rvTestList.addOnScrollListener(onScrollListener);

    }

    private void initListener() {
        //recycleview条目与子控件点击事件
        //method1:OnItemClickListener(OnItemClickListener.Mode.Swipe)
        //method2:OnItemClickListener()  --OnItemClickListener.Mode.Normal
        rvTestList.addOnItemTouchListener(new OnItemClickListener(OnItemClickListener.Mode.Swipe) {
            @Override
            public void SimpleOnItemClick(RecyclerView.Adapter adapter, View view, int position) {
                BaseQuickSwipeAdapter ad = (BaseQuickSwipeAdapter) adapter;
                SwipeLayout swipeLayout = (SwipeLayout) view;
                if(ad.isOpen(swipeLayout.getPosition())){
                    ad.closeAllItems();
                }else{
                    ToastUtil.shortToast(TestActivity.this,"OnItemClick-"+position);
                }
            }
            @Override
            public void onItemChildClick(RecyclerView.Adapter adapter, View parent,View view, int position) {
                super.onItemChildClick(adapter,parent, view, position);
                switch (view.getId()) {
                    case R.id.btn_iop_do:
                        ToastUtil.shortToast(TestActivity.this,"onItemChildClick-btn-"+position);
                        break;
                    case R.id.delete:
                        testAdapter.remove(position);
                        ToastUtil.shortToast(TestActivity.this,"delete-btn-"+position);
                        break;
                    default:
                        break;
                }
            }
        });


        //下拉刷新
        prlTest.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prlTest.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prlTest.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        //上拉加载，如不需要，则不写即可
        testAdapter.setOnLoadMoreListener(new BaseQuickSwipeAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //完成
                        /*mQuickAdapter.loadComplete();
                        if (notLoadingView == null) {
                            notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) mRecyclerView.getParent(), false);
                        }
                        mQuickAdapter.addFooterView(notLoadingView);*/

                        //失败
                        //orderHistoryAdapter.showLoadMoreFailedView();


                        //成功，若数据为空，自动加载空页面数据，
                        //mQuickAdapter.addData(xxx);
                        //flag++;

                        if(!err){
                            testAdapter.showLoadMoreFailedView();
                            err = !err;
                        }else{
                            List<String> l = new ArrayList<String>();
                            l.add("10");
                            l.add("11");
                            l.add("12");
                            testAdapter.addData(l);String a="1";
                            testAdapter.loadComplete();
                        }


                    }
                }, 1000);
            }
        });

    }

    private boolean err = false;


    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
            testAdapter.closeAllItems();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
            //testAdapter.closeAllItems();
        }
    };


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        //isHideLayer(llAvLoading,true);
        //显示网络异常页面
        testAdapter.showNetErrView();

    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        //isHideLayer(llAvLoading,true);
        if (null != result) {
            String action = result.getAction();


        }

    }


}
