package com.eme.mas.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.SearchHistoryAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.FlowGroupView;
import com.eme.mas.customeview.IncludeListView;
import com.eme.mas.data.sp.SpSearch;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.FinishPageEvent;
import com.eme.mas.model.HotWordResult;
import com.eme.mas.model.Result;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 搜索页面
 * <p/>
 * Created by dijiaoliang on 16/7/22.
 */
@WLayout(layoutId = R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    private final static String TAG = SearchActivity.class.getSimpleName();

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.flow_hot_word)
    FlowGroupView flowHotWord;
    @Bind(R.id.lv_search_history)
    IncludeListView lvSearchHistory;
    @Bind(R.id.llyt_search_history)
    LinearLayout llytSearchHistory;

    private IProduct mProController;

    private SearchHistoryAdapter mHistoryAdapter;
    private List<String> searchHistoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();

        mProController = mController.getProduct(this);//初始化控制器

        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(SearchActivity.this)) {
            mProController.getHotWords(TAG);
        } else {
            ToastUtil.shortToast(SearchActivity.this, R.string.net_error);
        }

        /**搜索历史初始化*/
        searchHistoryData = new ArrayList<>();

        mHistoryAdapter = new SearchHistoryAdapter(this, searchHistoryData, R.layout.item_search_history);
        lvSearchHistory.setAdapter(mHistoryAdapter);
        if (searchHistoryData.size() == 0) {
            llytSearchHistory.setVisibility(View.GONE);
        } else {
            llytSearchHistory.setVisibility(View.VISIBLE);
        }

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    SearchActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    String search_content = etSearch.getText().toString().trim();
                    search(search_content);
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * 添加热搜词
     *
     * @param list
     */
    private void addFlow(List<HotWordResult.HotWordBean> list) {
//        int margin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
//                .getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        final List<TextView> tList = new ArrayList<TextView>();
        HotWordResult.HotWordBean hotWordBean;
        int length = list.size();
        for (int j = 0; j < length; j++) {
            hotWordBean = list.get(j);
            final TextView tv = new TextView(SearchActivity.this);
            tv.setLayoutParams(params);
            tv.setTextSize(12f);
            tv.setText(hotWordBean.getName());
            tv.setTextColor(getResources().getColor(R.color.text_color_bar));
            tv.setBackgroundResource(R.drawable.btn_search_hot);
            tv.setPadding(20, 20, 20, 20);
//            tv.setTag(false);
//            tv.setId(Integer.parseInt(sizeItems.get(j).getSkuid()));
            tv.setTextColor(SearchActivity.this.getResources().getColor(
                    R.color.text_color_gray));
            tv.setGravity(Gravity.CENTER);
            tv.setOnClickListener(new HotWordListener(hotWordBean.getName()));
            tList.add(tv);
            flowHotWord.addView(tv);
            flowHotWord.requestLayout();
        }
    }


    /**
     * 热搜词的点击事件监听
     */
    private class HotWordListener implements View.OnClickListener {

        private String hot_word;

        public HotWordListener(String hot_word) {
            this.hot_word = hot_word;
        }

        @Override
        public void onClick(View v) {
            search(hot_word);
        }
    }

    @OnClick({R.id.cancel, R.id.ll_clear_history})
    void click(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cancel:
                finish();
                break;
            case R.id.ll_clear_history:
                searchHistoryData.clear();
                mHistoryAdapter.notifyDataSetChanged();
                llytSearchHistory.setVisibility(View.GONE);
                /**删除数据库中的搜索历史数据*/
                SpSearch.deleteSearchRecord();
                break;
        }

    }

    /**
     * 执行搜索
     *
     * @param search_word
     */
    private void search(String search_word) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(SearchActivity.this)) {
            if(!TextUtils.isEmpty(search_word)){
                /**跳转activity*/
                Intent intent = new Intent();
                intent.putExtra("search_name", search_word);
                intent.setClass(SearchActivity.this, SearchResultActivity.class);
                startActivity(intent);
                /**存储搜索记录*/
                SpSearch.saveSearchRecord(search_word);
            }else{
                ToastUtil.shortToast(SearchActivity.this, R.string.noSearchContent);
            }
        } else {
            ToastUtil.shortToast(SearchActivity.this, R.string.net_error);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mHistoryAdapter != null) {
            searchHistoryData.clear();
            List<String> tempData = SpSearch.getSearchRecord();
            if (tempData != null) {
                searchHistoryData.addAll(tempData);
            }
            if (searchHistoryData.size() == 0) {
                llytSearchHistory.setVisibility(View.GONE);
            } else {
                llytSearchHistory.setVisibility(View.VISIBLE);
                //显示搜索历史布局
                mHistoryAdapter.notifyDataSetChanged();
            }
        }
        etSearch.setText("");
    }

    @OnItemClick(R.id.lv_search_history)
    void itemClick(int position) {
        String search_word = searchHistoryData.get(position);
        search(search_word);
    }

    /**
     * 响应搜索结果页的post信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void finishSearch(FinishPageEvent event) {
        if(WValue.EVENT_SEARCH.equals(event.getTag())){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ((BaseImpl) mProController).cancelRequestByTag(TAG);
        super.onDestroy();
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        String action = result.getAction();
        if (WAction.SEARCH_ACTIVITY_HOT_WORDS.equals(action)) {
            HotWordResult hotWordResult = (HotWordResult) result;
            List<HotWordResult.HotWordBean> data = hotWordResult.getData();
            if (data != null && data.size() != 0) {
                addFlow(data);//添加热搜词
            }
        }
    }

}
