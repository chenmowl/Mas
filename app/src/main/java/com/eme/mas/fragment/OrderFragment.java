package com.eme.mas.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.customeview.AMapViewPager;
import com.eme.mas.eventbus.OrderCountEvent;
import com.eme.mas.fragment.order.HistoryFragment;
import com.eme.mas.fragment.order.ProcessingFragment;
import com.eme.mas.fragment.order.WaitingFragment;
import com.eme.mas.model.OrderCountResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.OrderCountBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单模块
 * Created by zulei on 16/8/5.
 */
@WLayout(layoutId = R.layout.fragment_order)
public class OrderFragment extends BaseFragment {

    private final static String TAG = OrderFragment.class.getSimpleName();

    @Bind(R.id.fragment_tabmain_indicator)
    FixedIndicatorView indicator;
    @Bind(R.id.fragment_tabmain_viewPager)
    AMapViewPager viewPager;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private WaitingFragment waitingFragment;
    private ProcessingFragment processingFragment;
    private HistoryFragment historyFragment;
    private int selectViewId;
    private String[] titleOrign;
    private ArrayList<String> titleNow;
    private MyAdapter myAdapter;

    private IOrder iOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
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
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initData();
    }


    private void initView() {
        back.setVisibility(View.GONE);
        barTitle.setText(R.string.order_title);
        titleOrign = new String[]{"待收货", "待处理", "历史订单"};
        titleNow = new ArrayList<>();
        titleNow.add(titleOrign[0]);
        titleNow.add(titleOrign[1]);
        titleNow.add(titleOrign[2]);

        iOrder = mController.getOrder(this);

        indicator.setScrollBar(new ColorBar(getActivity(), ContextCompat.getColor(getActivity(), R.color.main_color_red), 5));
        //设置颜色与过度
        //float unSelectSize = 16;
        //float selectSize = unSelectSize * 1.2f;
        int selectColor = ContextCompat.getColor(getActivity(), R.color.main_color_red);
        int unSelectColor = ContextCompat.getColor(getActivity(), R.color.hint_text_gray);
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor));
        /*
         * 设置缓存界面的个数，左右两边缓存界面的个数，不会被重新创建。 默认是1，表示左右两边
         * 相连的1个界面和当前界面都会被缓存住，比如切换到左边的一个界面，那个界面是不会重新创建的。
         */
        viewPager.setOffscreenPageLimit(2);
        // 设置page是否可滑动切换
        viewPager.setCanScroll(true);

        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        inflate = LayoutInflater.from(getActivity());

        // 注意这里 的FragmentManager 是 getChildFragmentManager(); 因为是在Fragment里面
        // 而在activity里面用FragmentManager 是 getSupportFragmentManager()

        myAdapter = new MyAdapter(getChildFragmentManager());
        indicatorViewPager.setAdapter(myAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    waitingFragment.loadAgain();
                }
                if (position == 1) {
                    processingFragment.getDataOnline(1, true);
                }
                if (position == 2) {
                    historyFragment.getDataOnline(1, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initData() {
        updateOrderCount();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void orderCount(OrderCountEvent event) {
        updateOrderCount();
    }

    private void updateOrderCount(){
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            iOrder.orderCount(TAG);
        }
    }

    private void initListener() {

    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return titleOrign.length;
        }


        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            SpannableString msp = new SpannableString(titleNow.get(position));
            if(position == 2){
                //历史订单
                msp.setSpan(new AbsoluteSizeSpan(14,true), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                msp.setSpan(new AbsoluteSizeSpan(12,true), 4, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                //待收货,待处理
                msp.setSpan(new AbsoluteSizeSpan(14,true), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                msp.setSpan(new AbsoluteSizeSpan(12,true), 3, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(msp);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            if (position == 0) {
                if (null == waitingFragment) {
                    waitingFragment = new WaitingFragment();
                }
                fragment = waitingFragment;
            }
            if (position == 1) {
                if (null == processingFragment) {
                    processingFragment = new ProcessingFragment();
                }

                fragment = processingFragment;
            }
            if (position == 2) {
                if (null == historyFragment) {
                    historyFragment = new HistoryFragment();
                }

                fragment = historyFragment;
            }

            //Bundle bundle = new Bundle();
            //fragment.setArguments(bundle);
            return fragment;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        switch (result.getAction()) {
            case WAction.ORDER_COUNT:
                break;
        }
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        if (null != result) {
            String action = result.getAction();
            if (WAction.ORDER_COUNT.equals(action)) {
                OrderCountResult orderCountResult = (OrderCountResult) result;
                OrderCountBo bo = orderCountResult.getData();
                int nr = bo.getReceiptNum();
                int np = bo.getPendingNum();
                int nh = bo.getHistoryNum();
                if(nr != 0){
                    titleNow.set(0,titleOrign[0]+" ("+bo.getReceiptNum()+")");
                }
                if(np != 0){
                    titleNow.set(1,titleOrign[1]+" ("+bo.getPendingNum()+")");
                }
                if(nh != 0){
                    titleNow.set(2,titleOrign[2]+" ("+bo.getHistoryNum()+")");
                }

                myAdapter.notifyDataSetChanged();
            }
        }
    }
}
