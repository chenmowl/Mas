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
import android.widget.ListView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.SearchPlaceAdapter;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.environment.WValue;
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
 * Created by simiao on 2015/5/20.
 */
@WLayout(layoutId = R.layout.activity_search_place)
public class SearchPlaceActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {


    @Bind(R.id.ib_search_place_etdelete)
    ImageView ivPlotDelete;
    @Bind(R.id.lv_search_polygon)
    ListView lvSearchPolygon;
    @Bind(R.id.et_key_word)
    EditText etKeyWord;


    private ArrayList<PoiItem> items;
    private String keyWord = "";
    private PoiSearch.Query query;
    private SearchPlaceAdapter adapter;
    private LatLonPoint pointLn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        lvSearchPolygon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem item = (PoiItem) adapterView.getAdapter().getItem(i);
                Bundle bundle = new Bundle();
                bundle.putString("detail", item.getSnippet());
                bundle.putString("name", item.getTitle());
                bundle.putString("cityCode", item.getCityCode());
                bundle.putString("areCode", item.getAdCode());
                bundle.putString("location", TextUtils.concat(String.valueOf(item.getLatLonPoint().getLatitude()),",",String.valueOf(item.getLatLonPoint().getLongitude())).toString());
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
                KeyboardUtils.hide(SearchPlaceActivity.this, etKeyWord);
                return false;
            }
        });

        etKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (NetworkStatusUtil.isNetworkConnected(SearchPlaceActivity.this)) {
                    keyWord = etKeyWord.getText().toString();
                    if (!keyWord.equals("")) {
                        items.clear();
                        ivPlotDelete.setVisibility(View.VISIBLE);
                        searchQuery();
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
        //ivPlotDelete.setVisibility(View.VISIBLE);
        items = new ArrayList();
        adapter = new SearchPlaceAdapter(items);
        lvSearchPolygon.setAdapter(adapter);
        hasResult();

    }

    private void initData() {
        //keyWord = getIntent().getStringExtra("place");
        //etKeyWord.setText(keyWord);
        pointLn=getIntent().getParcelableExtra("pointLn");
        doSearchQuery();
    }


    /**
     * poi关键字搜索
     */
    protected void doSearchQuery() {
        String type = AMapUtil.getInstance(MasApplication.getInstance()).getPoiType();
        String currentCity = AMapUtil.getInstance(MasApplication.getInstance()).getCurrentCityName();
//        String key;
//        if(TextUtils.isEmpty(keyWord)){
//            key = "北京";
//        }else{
//            key = keyWord;
//        }
        query = new PoiSearch.Query(WValue.STRING_EMPTY, type, currentCity);
        query.setCityLimit(false);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        if(pointLn!=null&& 0!=pointLn.getLatitude() &&0!=pointLn.getLongitude()){
            poiSearch.setBound(new PoiSearch.SearchBound(pointLn, 200));//设置周边搜索的中心点以及半径
        }
        poiSearch.searchPOIAsyn();

    }

    /**
     * poi关键字搜索
     */
    protected void searchQuery() {
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
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        items.addAll(poiItems);
                    } else if (suggestionCities != null && suggestionCities.size() > 0) {// ToDo无数据隐藏listview，有历史记录时，可以再次展示历史记录
                        showSuggestCity(suggestionCities);
                    }
                }
            }
        } else if (rCode == 27) {
            ToastUtil.shortToast(this, R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.shortToast(this, R.string.error_key);
        }

        if (items.size() > 0) {
            hasResult();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void hasResult() {
        try {
//            adapter.setData(items);
            adapter.notifyDataSetChanged();
        } catch (Exception ignore) {
        }

    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d+,\\d{1,3}\\.\\d+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String information = getString(R.string.recommend_city) + "\n";
        for (int i = 0; i < cities.size(); i++) {
            information += R.string.city_name + cities.get(i).getCityName() + R.string.city_area_code + cities.get(i).getCityCode() + R.string.city_code
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.shortToast(this, information);
    }



}
