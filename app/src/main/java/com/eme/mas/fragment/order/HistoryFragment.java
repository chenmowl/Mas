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
import com.eme.mas.activity.OrderDetailActivity;
import com.eme.mas.adapter.OrderHistoryAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.customeview.dialog.DeleteDialog;
import com.eme.mas.eventbus.OrderCountEvent;
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
 * 历史订单模块
 * Created by zulei on 16/8/5.
 */
@WLayout(layoutId = R.layout.fragment_order_history)
public class HistoryFragment extends BaseFragment{

    @Bind(R.id.ll_av_loading_transparent_full)
    LinearLayout llAvLoading;
    @Bind(R.id.rv_foh_goods)
    RecyclerView rvFohGoods;
    @Bind(R.id.prl_foh)
    PullRefreshLayout prlFoh;

    private OrderHistoryAdapter orderHistoryAdapter;
    private View viewNoData,viewNetErr,loadOverView;

    private IOrder iOrder;
    private List<MyOrderBo> list;

    private int page;//页数
    private boolean hadMore;//是否有更多商品
    private boolean isLoadMore;
    private boolean isFirstLoad;
//    private SwipeLayout deleteParent;
    private int deletePosition;

    private final static String TAG = "HistoryFragment";
    private final static int ITEM_MAX = 5;

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
        ((BaseImpl) iOrder).cancelRequestByTag(TAG);
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

    private void initView() {
        rvFohGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFohGoods.setItemAnimator(new OvershootInLeftAnimator());
        prlFoh.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        prlFoh.setColor(Color.GRAY);

        viewNoData = getActivity().getLayoutInflater().inflate(R.layout.layout_no_data_full, (ViewGroup) rvFohGoods.getParent(), false);
        viewNoData.findViewById(R.id.rl_no_data).setVisibility(View.VISIBLE);
        Button btnScrollNoData = (Button)viewNoData.findViewById(R.id.btn_stroll_nodata);
        btnScrollNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataOnline(1,true);
            }
        });

        viewNetErr = getActivity().getLayoutInflater().inflate(R.layout.no_network_loading_full, (ViewGroup) rvFohGoods.getParent(), false);
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

        rvFohGoods.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(RecyclerView.Adapter adapter, View view, int position) {
                OrderHistoryAdapter ad = (OrderHistoryAdapter) adapter;
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
                switch (view.getId()) {
                    case R.id.btn_ioh_rihgt_btn:
                        ToastUtil.shortToast(getActivity(), "再次购买");
                        break;
                    case R.id.ib_ioh_delete:
                        final DeleteDialog dDialog = new DeleteDialog();
                        deletePosition = position;
                        dDialog.setDeleteListener(new DeleteDialog.OnDeleteListener() {
                            @Override
                            public void cancel() {
                                dDialog.cancel();
                            }

                            @Override
                            public void ok() {
                                deleteHistoryOrder(myOrderBo.getOrder_id());
                                dDialog.cancel();
                            }
                        });
                        dDialog.showDialog(getActivity(),null);
                        break;
                    default:
                        break;
                }
            }
        });

        //下拉刷新
        prlFoh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadMore = false;
                page = 1;
                getDataOnline(page,false);
            }
        });

        //上拉加载，如不需要，则不写即可
        orderHistoryAdapter.setOnLoadMoreListener(loadMoreListener);

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

        orderHistoryAdapter = new OrderHistoryAdapter(list);
        orderHistoryAdapter.isFirstOnly(true);
        orderHistoryAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        orderHistoryAdapter.openLoadMore(ITEM_MAX);
        //设置空数据页面
        orderHistoryAdapter.setNoDataView(viewNoData);
        //设置网络异常页面
        orderHistoryAdapter.setNetErrView(viewNetErr);
        rvFohGoods.setAdapter(orderHistoryAdapter);

        getDataOnline(page,true);

    }

    public void getDataOnline(int page,boolean showAV) {
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            if(showAV){
                isHideLayer(llAvLoading, false);
            }
            iOrder.orderQuery(page + "", "0", TAG);//0 历史订单 10 待收货 20 待处理
        } else {
            isHideLayer(llAvLoading,true);
            updateNetErrView();
        }
    }

    private void deleteHistoryOrder(String orderId){
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            isHideLayer(llAvLoading,false);
            iOrder.orderDelete(orderId,TAG);
        } else {
            ToastUtil.shortToast(getActivity(), R.string.net_error);
        }
    }

    private void setLoadOverView(){
        if(loadOverView == null){
            loadOverView = getActivity().getLayoutInflater().inflate(R.layout.layout_load_over, (ViewGroup) rvFohGoods.getParent(), false);
        }
        int footViewCount = orderHistoryAdapter.getFooterLayoutCount();
        if(footViewCount==0){
            orderHistoryAdapter.addFooterView(loadOverView);
        }
        orderHistoryAdapter.setOnLoadMoreListener(null);
    }

    private void updateNetErrView(){
        prlFoh.setRefreshing(false);
        if (isLoadMore) {
            //上拉
            orderHistoryAdapter.showLoadMoreFailedView();
        } else {
            //下拉
            orderHistoryAdapter.showNetErrView();
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoading,true);
        switch (result.getAction()) {
            case WAction.ORDER_QUERY:
                updateNetErrView();
                break;
            case WAction.ORDER_DELETE:
                ToastUtil.shortToast(MasApplication.getInstance(), "删除失败，请稍后重试");
                break;
        }



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
                    orderHistoryAdapter.loadComplete();
                    orderHistoryAdapter.addData(bos);
                } else {
                    //下拉
                    prlFoh.setRefreshing(false);
                    orderHistoryAdapter.getData().clear();
                    orderHistoryAdapter.addData(bos);
                }

                if(hadMore){
                    page++;
                    orderHistoryAdapter.removeFooterView(loadOverView);
                    orderHistoryAdapter.openLoadMore(ITEM_MAX);
                    if (null == orderHistoryAdapter.getOnLoadMoreListener()) {
                        orderHistoryAdapter.setOnLoadMoreListener(loadMoreListener);
                    }
                }else{
                    if(orderHistoryAdapter.getData().size()>ITEM_MAX){
                        setLoadOverView();
                    }
                }
            }

            if (WAction.ORDER_DELETE.equals(action)) {
                orderHistoryAdapter.remove(deletePosition);
                if(hadMore){
                    if(orderHistoryAdapter.getData().size()<=ITEM_MAX){
                        isLoadMore = true;
                        getDataOnline(1,false);
                    }
                }

            }

        }

    }
}
