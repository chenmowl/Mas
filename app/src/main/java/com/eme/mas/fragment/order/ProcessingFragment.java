package com.eme.mas.fragment.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.activity.EvaluateActivity;
import com.eme.mas.activity.OrderDetailActivity;
import com.eme.mas.activity.PayOnlineActivity;
import com.eme.mas.adapter.OrderProcessingAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.OrderCountEvent;
import com.eme.mas.eventbus.SwitchMainFragmentEvent;
import com.eme.mas.fragment.BaseFragment;
import com.eme.mas.model.MyOrderResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

/**
 * 等待处理模块
 * Created by zulei on 16/8/5.
 */
@WLayout(layoutId = R.layout.fragment_order_processing)
public class ProcessingFragment extends BaseFragment {

    @Bind(R.id.ll_av_loading_transparent_full)
    LinearLayout llAvLoading;
    @Bind(R.id.rv_fop_goods)
    RecyclerView rvFopGoods;
    @Bind(R.id.prl_fop)
    PullRefreshLayout prlFop;

    private OrderProcessingAdapter orderProcessingAdapter;
    private IOrder iOrder;
    private List<MyOrderBo> list;
    private int page;//页数
    private boolean hadMore;//是否有更多商品
    private boolean isLoadMore;
    private boolean isFirstLoad;
    private View viewNoData,viewNetErr,loadOverView;


    private final static String TAG = "ProcessingFragment";
    private final static int ITEM_MAX = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ((BaseImpl) iOrder).cancelRequestByTag(TAG);
    }

    private void initView() {
        rvFopGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFopGoods.setItemAnimator(new OvershootInLeftAnimator());
        prlFop.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        prlFop.setColor(Color.GRAY);

        viewNoData = getActivity().getLayoutInflater().inflate(R.layout.layout_no_data_full, (ViewGroup) rvFopGoods.getParent(), false);
        viewNoData.findViewById(R.id.rl_no_data).setVisibility(View.VISIBLE);
        Button btnScrollNoData = (Button)viewNoData.findViewById(R.id.btn_stroll_nodata);
        btnScrollNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataOnline(1,true);
            }
        });

        viewNetErr = getActivity().getLayoutInflater().inflate(R.layout.no_network_loading_full, (ViewGroup) rvFopGoods.getParent(), false);
        viewNetErr.findViewById(R.id.rl_no_network).setVisibility(View.VISIBLE);
        Button btnScroll = (Button)viewNetErr.findViewById(R.id.btn_stroll);
        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataOnline(1,true);
            }
        });
    }

    private void initListener() {
        rvFopGoods.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(RecyclerView.Adapter adapter, View view, int position) {
                OrderProcessingAdapter ad = (OrderProcessingAdapter) adapter;
                if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("status", ad.getItem(position).getOrder_status());
                    intent.putExtra("orderID", ad.getItem(position).getOrder_id());
                    startActivity(intent);
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), "当前网络不可用");
                }
            }
            @Override
            public void onItemChildClick(final RecyclerView.Adapter adapter, View parent, View view, final int position) {
                super.onItemChildClick(adapter,parent, view, position);
                final BaseQuickAdapter ad = (BaseQuickAdapter) adapter;
                final MyOrderBo myOrderBo = (MyOrderBo) ad.getItem(position);
                final String orderStatus = myOrderBo.getOrder_status();
                switch (view.getId()) {
                    //40 50 已签收，未评价
                    //10 待付款
                    //80 100 售后处理中
                    case R.id.btn_iop_to_evaluate:
                        if("40".equals(orderStatus)||"50".equals(orderStatus)){
                            Intent intent = new Intent(getActivity(), EvaluateActivity.class);
                            intent.putExtra("MyOrderBo",myOrderBo);
                            getActivity().startActivity(intent);
                        }


                        break;
                    case R.id.btn_iop_rihgt_btn:
                        if("40".equals(orderStatus)||"50".equals(orderStatus)){
                            //FIXME 添加购物车，目前没有spec_id
                            EventBus.getDefault().post(new SwitchMainFragmentEvent(WValue.MAIN_FRAGMENT_CART));
                        }
                        if("10".equals(orderStatus)) {
                            String payWayCode = myOrderBo.getPay_way_code(); //1在线支付 2货到付款
                            if ("1".equals(payWayCode)) {
                                Intent intentPay = new Intent(getActivity(),PayOnlineActivity.class);
                                getActivity().startActivity(intentPay);
                            }else{
                                //FIXME 添加购物车，目前没有spec_id
                                EventBus.getDefault().post(new SwitchMainFragmentEvent(WValue.MAIN_FRAGMENT_CART));
                            }
                        }
                        if("80".equals(orderStatus) || "100".equals(orderStatus)){
                            //FIXME 添加购物车，目前没有spec_id
                            EventBus.getDefault().post(new SwitchMainFragmentEvent(WValue.MAIN_FRAGMENT_CART));
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //下拉刷新
        prlFop.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                page = 1;
                getDataOnline(page,false);
            }
        });

        //上拉加载，如不需要，则不写即可
        orderProcessingAdapter.setOnLoadMoreListener(loadMoreListener);
    }


    BaseQuickAdapter.RequestLoadMoreListener loadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            isLoadMore = true;
            getDataOnline(page,false);
        }
    };

    private void initData() {
        isHideLayer(llAvLoading,true);
        isLoadMore = false;
        isFirstLoad = true;
        page = 1;
        iOrder = mController.getOrder(this);
        list = new ArrayList();

        orderProcessingAdapter = new OrderProcessingAdapter(list);
        orderProcessingAdapter.isFirstOnly(true);
        orderProcessingAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        orderProcessingAdapter.openLoadMore(ITEM_MAX);
        orderProcessingAdapter.setNoDataView(viewNoData);
        orderProcessingAdapter.setNetErrView(viewNetErr);
        rvFopGoods.setAdapter(orderProcessingAdapter);

        getDataOnline(page,true);
    }

    private void setLoadOverView(){
        if(loadOverView == null){
            loadOverView = getActivity().getLayoutInflater().inflate(R.layout.layout_load_over, (ViewGroup) rvFopGoods.getParent(), false);
        }
        int footViewCount = orderProcessingAdapter.getFooterLayoutCount();
        if(footViewCount==0){
            orderProcessingAdapter.addFooterView(loadOverView);
        }
        orderProcessingAdapter.setOnLoadMoreListener(null);
    }

    public void getDataOnline(int page,boolean showAV) {
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            if(showAV){
                isHideLayer(llAvLoading, false);
            }
            iOrder.orderQuery(page+"", "20", TAG);//0 历史订单 10 待收货 20 待处理
        } else {
            isHideLayer(llAvLoading,true);
            updateNetErrView();
        }
    }

    private void updateNetErrView(){
        prlFop.setRefreshing(false);
        if (isLoadMore) {
            //上拉
            orderProcessingAdapter.showLoadMoreFailedView();
        } else {
            //下拉
            orderProcessingAdapter.showNetErrView();
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoading,true);
        updateNetErrView();

    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoading, true);

        if (null != result) {
            String action = result.getAction();
            if (WAction.ORDER_QUERY.equals(action)) {
                if(!isFirstLoad){
                    EventBus.getDefault().post(new OrderCountEvent());
                }
                isFirstLoad = false;

                MyOrderResult myOrderResult = (MyOrderResult) result;
                //hadMore = "1".equals(myOrderResult.getData().getHasMore()); //FIXME
                List<MyOrderBo> bos = myOrderResult.getData();
                hadMore = bos.size()%10 == 0 ? true:false;

                if (isLoadMore) {
                    //上拉
                    orderProcessingAdapter.loadComplete();
                    orderProcessingAdapter.addData(bos);
                } else {
                    //下拉
                    prlFop.setRefreshing(false);
                    orderProcessingAdapter.getData().clear();
                    orderProcessingAdapter.addData(bos);

                }

                if(hadMore){
                    page++;
                    orderProcessingAdapter.removeFooterView(loadOverView);
                    orderProcessingAdapter.openLoadMore(ITEM_MAX);
                    if (null == orderProcessingAdapter.getOnLoadMoreListener()) {
                        orderProcessingAdapter.setOnLoadMoreListener(loadMoreListener);
                    }
                }else{
                    if(orderProcessingAdapter.getData().size()>ITEM_MAX){
                        setLoadOverView();
                    }
                }
            }
        }
    }
}
