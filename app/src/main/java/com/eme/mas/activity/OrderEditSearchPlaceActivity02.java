package com.eme.mas.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.MyCommonAddressAdapter;
import com.eme.mas.adapter.SearchPlaceAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.ILocation;
import com.eme.mas.customeview.IncludeListView;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.MyAddressResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.MyAddressBo;
import com.eme.mas.utils.AMapUtil;
import com.eme.mas.utils.KeyboardUtils;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zulei on 2016/8/3.
 */
@WLayout(layoutId = R.layout.activity_order_edit_search_place)
public class OrderEditSearchPlaceActivity02 extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private final static String TAG = OrderEditSearchPlaceActivity02.class.getSimpleName();

    @Bind(R.id.tv_oesp_common_address)
    TextView tvOespCommonAddress;
    @Bind(R.id.lv_oesp_common_address)
    IncludeListView lvOespCommonAddress;
    @Bind(R.id.ib_search_place_etdelete)
    ImageView ivPlotDelete;
    @Bind(R.id.lv_search_polygon)
    IncludeListView lvSearchPolygon;
    @Bind(R.id.et_key_word)
    EditText etKeyWord;

    private ArrayList<PoiItem> items;
    private String keyWord = "";
    private PoiSearch.Query query;
    private SearchPlaceAdapter adapter;
    private List<MyAddressBo> myAddressBoList;
    private MyCommonAddressAdapter myCommonAddressAdapter;
    private ILocation iLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl)iLocation).cancelRequestByTag(TAG);
    }

    private void initListener() {
        lvSearchPolygon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem item = (PoiItem) adapterView.getAdapter().getItem(i);
                Bundle bundle = new Bundle();
                bundle.putString(WValue.FLAG, WValue.MAPITEM);
                bundle.putParcelable(WValue.MAPITEM,item);
                getIntent().putExtras(bundle);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        lvOespCommonAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyAddressBo item = myCommonAddressAdapter.getList().get(i);
                Bundle bundle = new Bundle();
                bundle.putString(WValue.FLAG, WValue.MYADDRESSBO);
                bundle.putParcelable(WValue.MYADDRESSBO,item);
                getIntent().putExtras(bundle);
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        ivPlotDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeyWord.setText("");
                ivPlotDelete.setVisibility(View.GONE);
            }
        });
        lvSearchPolygon.setOnTouchListener(new View.OnTouchListener() {//滑动时自动收缩键盘
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                KeyboardUtils.hide(OrderEditSearchPlaceActivity02.this, etKeyWord);
                return false;
            }
        });

        etKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (NetworkStatusUtil.isNetworkConnected(OrderEditSearchPlaceActivity02.this)) {
                    keyWord = etKeyWord.getText().toString();
                    if (!keyWord.equals("")) {
                        items.clear();
                        ivPlotDelete.setVisibility(View.VISIBLE);
                        doSearchQuery();
                    } else {
                        //Toast.makeText(SearchPlaceActivity.this, "121212", Toast.LENGTH_SHORT).show();
                        ivPlotDelete.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });// 添加文本输入框监听事件

    }

    @OnClick(R.id.btn_search_place_cancel)
    public void cancel() {
        finish();
    }

    private void initView() {
        items = new ArrayList();
        adapter = new SearchPlaceAdapter(items);
        lvSearchPolygon.setAdapter(adapter);

    }

    private void initData() {
        //keyWord = getIntent().getStringExtra("place");
        //etKeyWord.setText(keyWord);
        doSearchQuery();

        iLocation = mController.getLocation(this);
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            iLocation.getMyAddressList("",TAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }

    }


    /**
     * poi关键字搜索
     */
    protected void doSearchQuery() {

        String type = AMapUtil.getInstance(MasApplication.getInstance()).getPoiType();
        String currentCity = AMapUtil.getInstance(MasApplication.getInstance()).getCurrentCityName();
        String key;
        if(TextUtils.isEmpty(keyWord)){
            key = "北京";
        }else{
            key = keyWord;
        }
        query = new PoiSearch.Query(key, type, currentCity);
        query.setCityLimit(false);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    PoiResult poiResult = result;
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        items.clear();
                        items.addAll(poiItems);
                        adapter.notifyDataSetChanged();
                    } else {
                        //模糊搜索无结果不展示提示信息
                    }
                }
            }
        } else if (rCode == 27) {
            ToastUtil.shortToast(this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.shortToast(this, R.string.error_key);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d+,\\d{1,3}\\.\\d+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        if (null != result) {
            String action = result.getAction();
            if (WAction.GET_ADDRESS.equals(action)) {
                MyAddressResult myAddressResult = (MyAddressResult) result;
                myAddressBoList = myAddressResult.getData();
                if(null!=myAddressBoList){
                    myCommonAddressAdapter = new MyCommonAddressAdapter(this,myAddressBoList,R.layout.item_order_edit_search_place);
                    lvOespCommonAddress.setAdapter(myCommonAddressAdapter);
                }

            }

        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
    }
}
