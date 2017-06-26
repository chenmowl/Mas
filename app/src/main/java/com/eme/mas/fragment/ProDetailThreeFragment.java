package com.eme.mas.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.ProDetailEvaluateAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.IncludeListView;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.EvaluateResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.EvaluateVo;
import com.eme.mas.model.entity.GoodsEvaluateContentBo;
import com.eme.mas.model.entity.ScoreFlagBo;
import com.eme.mas.utils.ListUtil;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.StringUtil;
import com.eme.mas.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hedgehog.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品详情页第一部分:评价模块
 * <p>
 * Created by dijiaoliang on 16/8/5.
 */
@WLayout(layoutId = R.layout.fragment_pro_detail_three)
public class ProDetailThreeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2 {

    private final static String TAG = ProDetailThreeFragment.class.getSimpleName();

    @Bind(R.id.tv_favorableRate)
    TextView tvFavorableRate;
    @Bind(R.id.rating_bar_speed)
    RatingBar ratingBarSpeed;
    @Bind(R.id.rating_bar_attitude)
    RatingBar ratingBarAttitude;
    @Bind(R.id.tv_volume_value)
    TextView tvVolumeValue;
    @Bind(R.id.tv_evaluate_num)
    TextView tvEvaluateNum;
    @Bind(R.id.lv_evaluate)
    IncludeListView lvEvaluate;
    @Bind(R.id.scroll)
    PullToRefreshScrollView scroll;
    @Bind(R.id.tv_title_rate)
    TextView tvTitleRate;
    @Bind(R.id.ll_no_evaluate)
    LinearLayout llNoEvaluate;
    @Bind(R.id.rl_evaluate_data)
    RelativeLayout rlEvaluateData;

    private IProduct mProController;//商品控制器

    private int pageNum;
    private int pageSize=WValue.PAGE_SIZE;

    private boolean hasMore;
    private boolean isLoadMore;

    private String skuId;

    private int color_red;
    private int color_gray;


    private ProDetailEvaluateAdapter mProDetailEvaluateAdapter;
    private List<GoodsEvaluateContentBo> evaluateData;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProController = mController.getProduct(this);//初始化控制器
        skuId = getArguments().get(WValue.PRODUCT_ID).toString();
        pageNum = 0;
        hasMore = true;
        //颜色值
        color_red = getActivity().getResources().getColor(R.color.main_color_red);
        color_gray = getActivity().getResources().getColor(R.color.text_color_gray02);

        scroll.setMode(PullToRefreshBase.Mode.BOTH);
        scroll.setOnRefreshListener(this);
        //初始化操作
        evaluateData = new ArrayList<>();
        mProDetailEvaluateAdapter = new ProDetailEvaluateAdapter(getActivity(), evaluateData, R.layout.item_pro_detail_evaluate);
        lvEvaluate.setAdapter(mProDetailEvaluateAdapter);

        getProData(false);
    }

    /**
     * 获取数据
     */
    public void getProData(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            if (isLoadMore) {
                mProController.evaluateListData(skuId, String.valueOf(pageNum + 1), String.valueOf(pageSize), TAG);
            } else {
                mProController.evaluateListData(skuId, String.valueOf(1), String.valueOf(pageSize), TAG);
            }
        } else {
            ToastUtil.shortToast(getActivity(), R.string.net_error);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getProData(false);
//        scroll.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if(hasMore){
            getProData(true);
        }else{
            if(scroll.isRefreshing())
            scroll.onRefreshComplete();
            ToastUtil.shortToast(getActivity(),R.string.toast_no_goods);
        }
    }

    /**
     * 更新好评率、发货速度、服务态度、总销量
     *
     * @param scoreFlagBo
     */
    public void updateScore(ScoreFlagBo scoreFlagBo, int saleQuantity) {
        if (scoreFlagBo == null) {
            return;
        }
        //好评度
        if (0 == scoreFlagBo.getFavorableRate().doubleValue()) {
//            tvTitleRate.setVisibility(View.GONE);
            tvFavorableRate.setVisibility(View.GONE);
            tvTitleRate.setTextColor(color_gray);
            tvTitleRate.setText(getText(R.string.goods_no_rate));
        } else {
            tvFavorableRate.setVisibility(View.VISIBLE);
            tvTitleRate.setTextColor(color_red);
            tvTitleRate.setText(TextUtils.concat(String.valueOf(((int) scoreFlagBo.getFavorableRate().doubleValue() * 00)), "%"));

        }

        ratingBarSpeed.setmClickable(false);
        ratingBarSpeed.setStar(scoreFlagBo.getSpeedStarMean());
        ratingBarAttitude.setmClickable(false);
        ratingBarAttitude.setStar(scoreFlagBo.getServiceStarMean());
        tvVolumeValue.setText(TextUtils.concat(String.valueOf(saleQuantity), "件"));
        tvEvaluateNum.setText("评价(" + scoreFlagBo.getTotalPoints() + ")");
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        String msg = WValue.STRING_EMPTY;
        switch (result.getAction()) {
            case WAction.EVALUATE_LIST_DATA:
                if (!StringUtil.isEmpty(result.getMsg())) {
                    msg = result.getMsg();
                } else {
                    msg = getText(R.string.goods_get_evaluate_error).toString();
                }
                //结束刷新加载布局
                if(scroll.isRefreshing())
                scroll.onRefreshComplete();
                break;
        }
        ToastUtil.shortToast(getActivity(), msg);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch (result.getAction()) {
            case WAction.EVALUATE_LIST_DATA:
                //获取到信息更新页面
                EvaluateVo evaluateVo = ((EvaluateResult) result).getData();
                if (evaluateVo == null) {
                    if(scroll.isRefreshing())
                    scroll.onRefreshComplete();
                    return;
                }
                hasMore = evaluateVo.isHasMore();
                pageNum = evaluateVo.getPageNo();
                List<GoodsEvaluateContentBo> tempEvaluateData = evaluateVo.getResults();
                if (!ListUtil.isEmpty(tempEvaluateData)) {
                    if (!isLoadMore) {
                        evaluateData.clear();
                    }
                    evaluateData.addAll(tempEvaluateData);
                }
                //是否展示空背景
                if (evaluateData.size() == 0) {
                    rlEvaluateData.setVisibility(View.GONE);
                    llNoEvaluate.setVisibility(View.VISIBLE);

                    tvFavorableRate.setVisibility(View.GONE);
                    tvTitleRate.setTextColor(color_gray);
                    tvTitleRate.setText(getText(R.string.goods_no_rate));

                } else {
                    tvFavorableRate.setVisibility(View.VISIBLE);
                    tvTitleRate.setTextColor(color_red);

                    rlEvaluateData.setVisibility(View.VISIBLE);
                    mProDetailEvaluateAdapter.notifyDataSetChanged();
                    llNoEvaluate.setVisibility(View.GONE);
                }
                //结束刷新加载布局
                if(scroll.isRefreshing())
                scroll.onRefreshComplete();
                break;
        }
    }
}
