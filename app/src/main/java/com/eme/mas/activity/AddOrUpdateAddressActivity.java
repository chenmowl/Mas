package com.eme.mas.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.ILocation;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.MapItem;
import com.eme.mas.model.entity.MyAddressBo;
import com.eme.mas.utils.AMapUtil;
import com.eme.mas.utils.KeyboardUtils;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.PhoneUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 新增/修改地址
 * Created by zulei on 16/7/28.
 */
@WLayout(layoutId = R.layout.activity_add_address)
public class AddOrUpdateAddressActivity extends BaseActivity implements AMapLocationListener, LocationSource, AMap.OnCameraChangeListener,
        GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {

    private final static String TAG = AddOrUpdateAddressActivity.class.getSimpleName();

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.tv_add_address_receipt_address)
    TextView tvAddAddressReceiptAddress;
    @Bind(R.id.et_add_address_receipt_detail)
    EditText etAddAddressReceiptDetail;
    @Bind(R.id.et_add_address_receipt_men)
    EditText etAddAddressReceiptMen;
    @Bind(R.id.et_add_address_receipt_phone)
    EditText etAddAddressReceiptPhone;
    @Bind(R.id.ib_add_address_receipt_address_delete)
    ImageButton ibAddAddressReceiptAreaDelete;
    @Bind(R.id.ib_add_address_receipt_detail_delete)
    ImageButton ibAddAddressReceiptDetailDelete;
    @Bind(R.id.ib_add_address_receipt_men_delete)
    ImageButton ibAddAddressReceiptMenDelete;
    @Bind(R.id.ib_add_address_receipt_phone_delete)
    ImageButton ibAddAddressReceiptPhoneDelete;
    @Bind(R.id.mv_aa_map)
    MapView mapView;
    @Bind(R.id.sv_aa)
    ScrollView svAa;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    ILocation location;
    private int flag;
    private MyAddressBo myAddressBo;
    private String receiptArea, receiptDetail, receiptName, receiptPhone;

    private LatLonPoint pointLn;
    private GeocodeSearch geocoderSearch;
    private float preZoom;
    private AMap aMap;
    private Double geoLat, geoLng;
    private LatLng saveLatLng;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mapLocation;
    private boolean flagRun;
    private String provinceID = "";//预留
    private String cityID;
    private String areaID;
    private String positionXY;
    private ArrayList<MapItem> items;
    private boolean mapTouch;


    private PoiSearch.Query query;

    private final static int ADDRESS_ADD = 3001;
    private final static int ADDRES_EDIT = 3002;
    private final static int TO_SEARCH = 4001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        ButterKnife.unbind(this);
        ((BaseImpl) location).cancelRequestByTag(TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
        initMap();
    }

    private void initMap() {
        items = new ArrayList<MapItem>();
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        if (flag == ADDRES_EDIT && !flagRun) {
            //延时定位
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Log.i("info", "1秒1秒1秒1秒后定位");
                    setPosition(myAddressBo.getLocation());
                    flagRun = true;
                }
            }, 1000);

        }

    }

    private void initView() {
        barRightTitle.setText(R.string.cancel);
    }

    private void initData() {
        location = mController.getLocation(this);
        flag = getIntent().getIntExtra(WValue.FLAG, WValue.ZERO);
        flagRun = false;
        mapTouch = false;

        if (flag == ADDRESS_ADD) {
            barTitle.setText(R.string.my_address_title_add);
        } else {
            barTitle.setText(R.string.my_address_edit);
            myAddressBo = getIntent().getParcelableExtra("model_address");
            cityID = myAddressBo.getCity_id();
            areaID = myAddressBo.getArea_id();
            positionXY = myAddressBo.getLocation();
            if (null != myAddressBo) {
                receiptArea = myAddressBo.getAreaPath();
                if (!TextUtils.isEmpty(receiptArea)) {
                    tvAddAddressReceiptAddress.setText(receiptArea);
                    ibAddAddressReceiptAreaDelete.setVisibility(View.VISIBLE);
                }
                receiptDetail = myAddressBo.getAddress();
                if (!TextUtils.isEmpty(receiptDetail)) {
                    etAddAddressReceiptDetail.setText(receiptDetail);
                    ibAddAddressReceiptDetailDelete.setVisibility(View.VISIBLE);
                }
                receiptName = myAddressBo.getName();
                if (!TextUtils.isEmpty(receiptName)) {
                    etAddAddressReceiptMen.setText(receiptName);
                    ibAddAddressReceiptMenDelete.setVisibility(View.VISIBLE);
                }
                receiptPhone = myAddressBo.getMobile();
                if (!TextUtils.isEmpty(receiptPhone)) {
                    etAddAddressReceiptPhone.setText(receiptPhone);
                    ibAddAddressReceiptPhoneDelete.setVisibility(View.VISIBLE);
                }
            }

        }

    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        //aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //aMap.getUiSettings().setZoomControlsEnabled(false);//不显示缩放和扩大按钮。
        //aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        //aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器

        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_point));// 设置小点的图标
        locationStyle.strokeColor(Color.argb(50, 1, 1, 100));// 设置圆形的边框颜色
        locationStyle.radiusFillColor(Color.argb(50, 255, 60, 0));// 设置圆形的填充颜色
        locationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(locationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        preZoom = (float) 17.5;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(preZoom));//高德地图的缩放级别是在3-20 之间,默认为17。5.随手势的改变动态改变
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {//解决外套ScrollView的焦点问题
            @Override
            public void onTouch(MotionEvent arg0) {
                mapTouch = true;
                svAa.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    @OnClick(R.id.tv_add_address_receipt_address)
    public void toSearch() {
        Intent intentSearch = new Intent(this, SearchPlaceActivity.class);
        intentSearch.putExtra("place", tvAddAddressReceiptAddress.getText().toString());
        intentSearch.putExtra("pointLn",pointLn);
        startActivityForResult(intentSearch, TO_SEARCH);
    }

    @OnClick({R.id.back, R.id.bar_right_title})
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_add_address_done)
    public void done() {

        //AMapLocation l = AMapUtil.getInstance(this).getLocation();
        KeyboardUtils.closeInputMethod(this);

        if (TextUtils.isEmpty(receiptArea)) {
            ToastUtil.shortToast(this, R.string.mar_hint_input_receipt_area);
            return;
        }
        if (TextUtils.isEmpty(receiptDetail)) {
            ToastUtil.shortToast(this, R.string.mar_hint_input_receipt_detail);
            return;
        }
        if (TextUtils.isEmpty(receiptName)) {
            ToastUtil.shortToast(this, R.string.mar_hint_input_receipt_name);
            return;
        }
        if (TextUtils.isEmpty(receiptPhone)) {
            ToastUtil.shortToast(this, R.string.mar_hint_input_receipt_phone);
            return;
        }
        if (!PhoneUtil.isMobileNumber(receiptPhone)) {
            ToastUtil.shortToast(this, R.string.toast_phone_format_wrong);
            return;
        }

        if (NetworkStatusUtil.isNetworkConnected(AddOrUpdateAddressActivity.this)) {
            if (flag == ADDRESS_ADD) {
                isHideLayer(llAvLoadingTransparent44,false);
                //新增地址
                if (null == mapLocation) {
                    location.addNewAddress("", "", "", receiptArea, receiptDetail, receiptName, receiptPhone, "", TAG);
                } else {
                    location.addNewAddress(provinceID, cityID, areaID, receiptArea, receiptDetail, receiptName, receiptPhone, positionXY, TAG);
                }

            } else {
                isHideLayer(llAvLoadingTransparent44,false);
                //编辑地址
                if (null == mapLocation) {
                    location.editMyAddress(myAddressBo.getAddress_id(), "", "", "", receiptArea, receiptDetail, receiptName, receiptPhone, "", TAG);
                } else {
                    location.editMyAddress(myAddressBo.getAddress_id(), provinceID, cityID, areaID, receiptArea, receiptDetail, receiptName, receiptPhone, positionXY, TAG);
                }
            }
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }

    }


    @OnClick({R.id.ib_add_address_receipt_address_delete, R.id.ib_add_address_receipt_detail_delete,
            R.id.ib_add_address_receipt_men_delete, R.id.ib_add_address_receipt_phone_delete})
    public void etDelete(View view) {
        switch (view.getId()) {
            case R.id.ib_add_address_receipt_address_delete:
                receiptArea = "";
                tvAddAddressReceiptAddress.setText("");
                //etAddAddressReceiptArea.requestFocus();
                ibAddAddressReceiptAreaDelete.setVisibility(View.INVISIBLE);
                break;
            case R.id.ib_add_address_receipt_detail_delete:
                receiptDetail = "";
                etAddAddressReceiptDetail.setText("");
                etAddAddressReceiptDetail.requestFocus();
                ibAddAddressReceiptDetailDelete.setVisibility(View.GONE);
                break;
            case R.id.ib_add_address_receipt_men_delete:
                receiptName = "";
                etAddAddressReceiptMen.setText("");
                etAddAddressReceiptMen.requestFocus();
                ibAddAddressReceiptMenDelete.setVisibility(View.GONE);
                break;
            case R.id.ib_add_address_receipt_phone_delete:
                receiptPhone = "";
                etAddAddressReceiptPhone.setText("");
                etAddAddressReceiptPhone.requestFocus();
                ibAddAddressReceiptPhoneDelete.setVisibility(View.GONE);
                break;
        }
    }

//    @OnTextChanged(R.id.et_add_address_receipt_address)
//    public void etAreaChanged(CharSequence text) {
//        receiptArea = text.toString();
//        if (TextUtils.isEmpty(receiptArea)) {
//            ibAddAddressReceiptAreaDelete.setVisibility(View.INVISIBLE);
//        } else {
//            ibAddAddressReceiptAreaDelete.setVisibility(View.VISIBLE);
//        }
//    }

    @OnTextChanged(R.id.et_add_address_receipt_detail)
    public void etAddressChanged(CharSequence text) {
        receiptDetail = text.toString();
        if (TextUtils.isEmpty(receiptDetail)) {
            ibAddAddressReceiptDetailDelete.setVisibility(View.GONE);
        } else {
            ibAddAddressReceiptDetailDelete.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(R.id.et_add_address_receipt_men)
    public void etManChanged(CharSequence text) {
        receiptName = text.toString();
        if (TextUtils.isEmpty(receiptName)) {
            ibAddAddressReceiptMenDelete.setVisibility(View.GONE);
        } else {
            ibAddAddressReceiptMenDelete.setVisibility(View.VISIBLE);
        }
    }

    @OnTextChanged(R.id.et_add_address_receipt_phone)
    public void etPhoneChanged(CharSequence text) {
        receiptPhone = text.toString();
        if (TextUtils.isEmpty(receiptPhone)) {
            ibAddAddressReceiptPhoneDelete.setVisibility(View.GONE);
        } else {
            ibAddAddressReceiptPhoneDelete.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        if (mListener != null && location != null) {
            if (location != null && location.getErrorCode() == 0) {
                if (null != location.getCity() && !"".equals(location.getCity())) {
                    AMapUtil.getInstance(MasApplication.getInstance()).setCurrentCityName(location.getCity());
                }
                mapLocation = location;
                AMapUtil.getInstance(MasApplication.getInstance()).isGpsForResult = true;
                geoLat = location.getLatitude();
                geoLng = location.getLongitude();
                saveLatLng = new LatLng(geoLat, geoLng);
                pointLn = new LatLonPoint(geoLat, geoLng);
                mListener.onLocationChanged(location);// 显示系统小点
//                receiptArea = mapLocation.getAddress();
                receiptArea = mapLocation.getPoiName();
                cityID = mapLocation.getCityCode();
                areaID = mapLocation.getAdCode();
                positionXY = TextUtils.concat(String.valueOf(geoLng),",",String.valueOf(geoLat)).toString();//经纬度 字符串类型
                deactivate();

                updateView();

            } else {
                String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                Log.i("AmapErr", errText);
            }
        }
    }

    private void updateView() {
        if (flag == ADDRESS_ADD) {
            etAddAddressReceiptDetail.setText("");
        }
        if (!TextUtils.isEmpty(receiptArea)) {
            tvAddAddressReceiptAddress.setText(receiptArea);
            ibAddAddressReceiptAreaDelete.setVisibility(View.VISIBLE);
        } else {
            ibAddAddressReceiptAreaDelete.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();

        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 对正在移动地图事件回调
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 对移动地图结束事件回调
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (mapTouch) {
            pointLn = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
            doSearchQuery();
//            geocoderSearch = new GeocodeSearch(this);
//            geocoderSearch.setOnGeocodeSearchListener(this);
//            //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系   
//            RegeocodeQuery query = new RegeocodeQuery(pointLn, 200, GeocodeSearch.AMAP);
//            geocoderSearch.getFromLocationAsyn(query);
            mapTouch = false;
        }
    }


    private void setPosition(String xy) {
        if (!TextUtils.isEmpty(xy)) {
            String[] latLng = xy.split(",");
            LatLng po = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(po));
        }

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
        poiSearch.setBound(new PoiSearch.SearchBound(pointLn, 200));//设置周边搜索的中心点以及半径
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
                        PoiItem item=poiItems.get(0);
                        pointLn=item.getLatLonPoint();
                        geoLat = pointLn.getLatitude();
                        geoLng = pointLn.getLongitude();
                        saveLatLng = new LatLng(geoLat, geoLng);
                        receiptArea = item.getTitle();
                        cityID = item.getCityCode();
                        areaID = item.getAdCode();
                        positionXY = TextUtils.concat(String.valueOf(geoLng),",",String.valueOf(geoLat)).toString();//经纬度 字符串类型
                        updateView();
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



    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        if (null != result) {
            String message = result.getMsg();
            ToastUtil.shortToast(this, message);
//            String action = result.getAction();
//            if (WAction.ADD_NEW_ADDRESS.equals(action)) {
//
//            }

        }
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        if (null != result) {
            String action = result.getAction();
            if (WAction.ADD_NEW_ADDRESS.equals(action)) {
                ToastUtil.shortToast(this, R.string.toast_add_address_ok);
                finish();
            }
            if (WAction.EDIT_ADDRESS.equals(action)) {
                ToastUtil.shortToast(this, R.string.toast_edit_address_ok);
                finish();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == TO_SEARCH) {
                Bundle bundle = data.getExtras();
                tvAddAddressReceiptAddress.setText(bundle.getString("name"));
                receiptArea = bundle.getString("name");
                //etAddAddressReceiptDetail.setText(bundle.getString("detail"));
                //receiptDetail = bundle.getString("detail");
                cityID = bundle.getString("cityCode");
                areaID = bundle.getString("areCode");
                positionXY = bundle.getString("location");
                setPosition(positionXY);

            }
            //刷新本页面

        }

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            RegeocodeAddress geoAddress = regeocodeResult.getRegeocodeAddress();
            cityID = geoAddress.getCityCode();
            areaID = geoAddress.getAdCode();
            positionXY = TextUtils.concat(String.valueOf(pointLn.getLongitude()),",",String.valueOf(pointLn.getLatitude())).toString();//经纬度 字符串类型
//            List list = geoAddress.getAois();
//            if(null!=list && list.size()!=0){
//                receiptArea = geoAddress.getAois().get(0).getAoiName();
//            }else{
//                receiptArea = geoAddress.getFormatAddress();
//            }
            receiptArea = geoAddress.getFormatAddress();
            tvAddAddressReceiptAddress.setText(receiptArea);
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

}
