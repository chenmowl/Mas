package com.eme.mas.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.eme.mas.adapter.OrderEditGoodsAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.customeview.FullyLinearLayoutManager;
import com.eme.mas.customeview.dialog.PayModeDialog;
import com.eme.mas.customeview.dialog.ServedTimeDialog;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.RefreshCartEvent;
import com.eme.mas.model.CartListResult;
import com.eme.mas.model.OrderAmountPayableResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.SubmitOrderResult;
import com.eme.mas.model.entity.CartBo;
import com.eme.mas.model.entity.MyAddressBo;
import com.eme.mas.model.entity.SubmitOrderBo;
import com.eme.mas.utils.AMapUtil;
import com.eme.mas.utils.KeyboardUtils;
import com.eme.mas.utils.MathExtend;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单编辑
 * Created by zulei on 16/8/3.
 */
@WLayout(layoutId = R.layout.activity_order_edit)
public class OrderCommitActivity extends BaseActivity implements AMapLocationListener, LocationSource, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener  {

    private final static String TAG = OrderCommitActivity.class.getSimpleName();

    @Bind(R.id.ll_order_edit_open_surplus)
    LinearLayout llOrderEditOpenSurplus;
    @Bind(R.id.mv_aa_map)
    MapView mapView;
    @Bind(R.id.tv_order_edit_address)
    TextView tvOrderEditAddress;
    @Bind(R.id.sv_aa)
    ScrollView svAa;
    @Bind(R.id.et_order_edit_detail)
    EditText etOrderEditDetail;
    @Bind(R.id.et_order_edit_men)
    EditText etOrderEditMen;
    @Bind(R.id.et_order_edit_phone)
    EditText etOrderEditPhone;
    @Bind(R.id.tv_order_edit_open_surplus)
    TextView tvOrderEditOpenSurplus;
    @Bind(R.id.tv_order_edit_invoice_info)
    TextView tvOrderEditInvoiceInfo;
    @Bind(R.id.tv_order_edit_served_time)
    TextView tvOrderEditServedTime;
    @Bind(R.id.iv_order_edit_open_surplus)
    ImageView ivOrderEditOpenSurplus;
    @Bind(R.id.tv_order_edit_pay_mode)
    TextView tvOrderEditPayMode;
    @Bind(R.id.et_order_edit_remark)
    EditText etOrderEditRemark;
    @Bind(R.id.tv_order_detail_coupon)
    TextView tvOrderDetailCoupon;
    @Bind(R.id.tv_order_edit_goodsmoney)
    TextView tvOrderEditGoodsmoney;
    @Bind(R.id.tv_order_edit_finally_money)
    TextView tvOrderEditFinallyMoney;
    @Bind(R.id.tv_order_edit_benefit)
    TextView tvOrderEditBenefit;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.rv_order_edit_goods)
    RecyclerView rvOrderEditGoods;

    private OrderEditGoodsAdapter orderEditGoodsAdapter;
    private LatLonPoint pointLn;
    private float preZoom;
    private AMap aMap;
    private Double geoLat, geoLng;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mapLocation;
    private GeocodeSearch geocoderSearch;
    private PoiSearch.Query query;


    /**提交订单接口的请求参数*/
    private String cardIds;//购物车id
    private String couponID;//优惠券信息
    private String servedTime;//收货时间
    private String payMode; //支付方式：1在线支付，2货到付款
    private String positionXY, addressID, provinceName, cityName, areaName, areaPath, address, name, phone;//地址信息
    private boolean needInvoice;//是否需要发票
    private String invoiceType, invoiceContent, invoiceHead; //发票信息


    private boolean goodsListIsOpen;//商品列表收展的标识
    private boolean isCouponAmount;//商品列表收展的标识
    private boolean mapTouch;//区分是否拖动地图
    private String couponAmount;//优惠券金额
    private String totlaPrice;//订单金额

    private List<CartBo> cartBoList;//购物车数据

    private IOrder iOrder;
    private ICart iCart;

    private final static int TO_SEARCH = 3001;
    private final static int TO_INVOICE = 3002;
    private final static int TO_COUPON = 3003;
    private final static int TO_ADDRESS = 3004;

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
        ((BaseImpl) iOrder).cancelRequestByTag(TAG);
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
        initListener();
        initData();
    }

    private void initView() {
        barTitle.setText(R.string.order_edit_title);
        barRightTitle.setText(R.string.my_address_title);
        tvOrderDetailCoupon.setText(WValue.STRING_EMPTY);
        isCouponAmount=false;

        //创建默认的线性LayoutManager
        rvOrderEditGoods.setLayoutManager(new FullyLinearLayoutManager(this));
        rvOrderEditGoods.setNestedScrollingEnabled(false);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvOrderEditGoods.setHasFixedSize(true);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

    }

    private void initListener() {
        //设置逆地理编码监听
        if(geocoderSearch==null){
            geocoderSearch = new GeocodeSearch(this);
        }
        geocoderSearch.setOnGeocodeSearchListener(this);

        etOrderEditDetail.addTextChangedListener(new OrderCommitTextWatcher(WValue.EDIT_ADDRESS));
        etOrderEditMen.addTextChangedListener(new OrderCommitTextWatcher(WValue.EDIT_NAME));
        etOrderEditPhone.addTextChangedListener(new OrderCommitTextWatcher(WValue.EDIT_PHONE));
    }


    /**
     * 输入框文本变化监听
     */
    private class OrderCommitTextWatcher implements TextWatcher{

        private String flag;

        public OrderCommitTextWatcher(String flag) {
            this.flag=flag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (flag){
                case WValue.EDIT_ADDRESS:
                    address=s.toString().trim();
                    break;
                case WValue.EDIT_NAME:
                    name=s.toString().trim();
                    break;
                case WValue.EDIT_PHONE:
                    phone=s.toString().trim();
                    break;
            }
        }
    }

    private void initData() {
        cardIds = getIntent().getStringExtra("cartIds");
        mapTouch = false;
        iOrder = mController.getOrder(this);
        iCart = mController.getIAccount(this);
        servedTime = WValue.STRING_EMPTY;
        payMode = WValue.STRING_EMPTY;
        goodsListIsOpen = false;
        needInvoice = false;
        cartBoList = new ArrayList<>();
        totlaPrice = "0";
        couponAmount = "0";
        invoiceType=WValue.STRING_EMPTY;
        invoiceContent=WValue.STRING_EMPTY;
        invoiceHead=WValue.STRING_EMPTY;
        needInvoice=false;

        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoadingTransparent44,false);
            iCart.getCartList(TAG);
            iOrder.getPayAmount(cardIds,couponID,TAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }

    }

    private void updateList() {
        if (null != cartBoList && cartBoList.size() != 0) {
            if (cartBoList.size() <= 2) {
                llOrderEditOpenSurplus.setVisibility(View.GONE);
            }

            orderEditGoodsAdapter = new OrderEditGoodsAdapter(cartBoList, R.layout.item_order_edit_goods);
            rvOrderEditGoods.setAdapter(orderEditGoodsAdapter);

            for (int i = 0; i < cartBoList.size(); i++) {
                String pp = MathExtend.multiply(cartBoList.get(i).getProduct_price(), cartBoList.get(i).getProduct_num());
                totlaPrice = MathExtend.add(totlaPrice, pp);
            }

            tvOrderEditGoodsmoney.setText(totlaPrice);
            tvOrderEditFinallyMoney.setText(totlaPrice);

        }

    }

    private void setUpMap() {
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


    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.bar_right_title)
    public void toMyAddress() {
        Intent intent = new Intent(this, MyAddressActivity.class);
        intent.putExtra(WValue.FLAG, WValue.ORDER_COMMIT);
        startActivityForResult(intent,TO_ADDRESS);
    }

    @OnClick(R.id.rl_order_detail_coupon)
    public void toCoupon() {
        Intent intent = new Intent(this, OrderCouponActivity.class);
        intent.putExtra("cardIds", cardIds);
        startActivityForResult(intent, TO_COUPON);
    }

    @OnClick(R.id.tv_order_edit_address)
    public void toSearch() {
        Intent intentSearch = new Intent(this, OrderEditSearchPlaceActivity.class);
        intentSearch.putExtra("pointLn",pointLn);
        startActivityForResult(intentSearch, TO_SEARCH);
    }

    @OnClick(R.id.rl_order_edit_invoice)
    public void toInvoice() {
        Intent intentSearch = new Intent(this, InvoiceInfoActivity.class);
        //传递 上次存储的发票信息
        intentSearch.putExtra(WValue.INVOICE_TYPE,invoiceType);
        intentSearch.putExtra(WValue.INVOICE_HEAD,invoiceHead);
        intentSearch.putExtra(WValue.INVOICE_CONTENT,invoiceContent);
        startActivityForResult(intentSearch, TO_INVOICE);
    }

    @OnClick(R.id.rl_order_edit_served_time)
    public void openServedTimeDialog() {
        final ServedTimeDialog sbDialog = new ServedTimeDialog(this);
        sbDialog.setOnSexChangeListener(new ServedTimeDialog.OnTimeChangeListener() {
            @Override
            public void changeTime(String time, String timeAll) {
                tvOrderEditServedTime.setText(time + "送达");
                servedTime = timeAll;
            }

            @Override
            public void rightNow() {
                servedTime = WValue.AT_ONCE;//配送时间选择立即送达,请求时不用传参数
                tvOrderEditServedTime.setText(R.string.order_edit_title_served_now);

            }
        });

        sbDialog.showDialog(this, null);
    }

    @OnClick(R.id.rl_order_edit_paymode)
    public void toPayMode() {
        final PayModeDialog pmDialog = new PayModeDialog();
        pmDialog.setOnPayModeChangeListener(new PayModeDialog.OnPayModeChangeListener() {
            @Override
            public void online() {
                payMode = WValue.PAYMODE_ON_LINE;
                tvOrderEditPayMode.setText(R.string.pay_online);
                pmDialog.cancel();
            }

            @Override
            public void offline() {
                payMode = WValue.PAYMODE_OFF_LINE;
                tvOrderEditPayMode.setText(R.string.pay_offline);
                pmDialog.cancel();
            }
        });
        pmDialog.showDialog(this, null);
    }

    @OnClick(R.id.ll_order_edit_open_surplus)
    public void openSurplus() {

        orderEditGoodsAdapter.openHideList(!goodsListIsOpen);
        if (!goodsListIsOpen) {
            tvOrderEditOpenSurplus.setText(R.string.order_edit_title_close_surplus);
            ivOrderEditOpenSurplus.setImageResource(R.mipmap.shangla);
        } else {
            tvOrderEditOpenSurplus.setText(R.string.order_edit_title_open_surplus);
            ivOrderEditOpenSurplus.setImageResource(R.mipmap.xiala);
        }
        goodsListIsOpen = !goodsListIsOpen;

    }

    @OnClick(R.id.btn_order_edit_submit)
    public void orderSubmit() {
        KeyboardUtils.closeInputMethod(this);
        if (TextUtils.isEmpty(addressID) && TextUtils.isEmpty(positionXY)) {
            ToastUtil.shortToast(this, R.string.toast_edit_address);
            return;
        }
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(areaPath)) {
            ToastUtil.shortToast(this, R.string.toast_edit_address);
            return;
        }

        String name = etOrderEditMen.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.shortToast(this, R.string.toast_edit_receiver_name);
            return;
        }

        String phone = etOrderEditPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.shortToast(this, R.string.toast_edit_phone);
            return;
        }

        if(TextUtils.isEmpty(servedTime)){
            ToastUtil.shortToast(this, R.string.toast_select_delivery_time);
        }

        if (TextUtils.isEmpty(payMode)) {
            ToastUtil.shortToast(this, R.string.toast_select_payment_mode);
            return;
        }

        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoadingTransparent44,false);
            //TODO 提交订单
            iOrder.submitOrder(cardIds,payMode,WValue.ORDER_FROM,positionXY,addressID,provinceName,cityName,areaName,areaPath,address,name,phone,needInvoice?invoiceType:WValue.STRING_EMPTY,
                    needInvoice?invoiceHead:WValue.STRING_EMPTY,needInvoice?invoiceContent:WValue.STRING_EMPTY,null,WValue.AT_ONCE.equals(servedTime)?null:servedTime,etOrderEditRemark.getText().toString().trim(),TAG);
        } else {
            ToastUtil.shortToast(OrderCommitActivity.this, R.string.net_error);
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
                mListener.onLocationChanged(location);// 显示系统小点
                aMap.clear();

                geoLat = location.getLatitude();//纬度
                geoLng = location.getLongitude();//经度
                pointLn = new LatLonPoint(geoLat, geoLng);//坐标对象
                positionXY = TextUtils.concat(String.valueOf(geoLng),",",String.valueOf(geoLat)).toString();//经纬度 字符串类型
                provinceName=location.getProvince(); //省
                cityName=location.getCity();    //城市
                areaName=location.getDistrict();    //区
                areaPath=location.getPoiName();  //定位的详细地址
//                areaPath=location.getAddress();  //定位的详细地址

                deactivate();//取消定位
                updateView();//更新定位地址内容

            } else {
                String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                Log.i("AmapErr", errText);
            }
        }
    }

    private void updateView() {
        etOrderEditDetail.setText("");
        if (!TextUtils.isEmpty(areaPath)) {
            tvOrderEditAddress.setText(areaPath);
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
            //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系   
//            RegeocodeQuery query = new RegeocodeQuery(pointLn, 200, GeocodeSearch.AMAP);
//            geocoderSearch.getFromLocationAsyn(query);
            mapTouch = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode){
                case TO_SEARCH://搜索地址的回显
                    Bundle bundle = data.getExtras();
                    if(bundle!=null){
                        switch (bundle.getString(WValue.FLAG)){
                            case WValue.MAPITEM:
                                PoiItem poiItem=bundle.getParcelable(WValue.MAPITEM);
                                pointLn=poiItem.getLatLonPoint();//坐标对象
                                geoLat=pointLn.getLatitude();//纬度
                                geoLng=pointLn.getLongitude();//经度
                                positionXY = TextUtils.concat(String.valueOf(geoLng),",",String.valueOf(geoLat)).toString();//经纬度 字符串类型
                                provinceName=poiItem.getProvinceName(); //省
                                cityName=poiItem.getCityName();    //城市
                                areaName=poiItem.getAdName();   //区
                                areaPath=poiItem.getTitle();  //定位的地址
                                updateView();
                                break;
                            case WValue.MYADDRESSBO:
                                MyAddressBo myAddressBo=bundle.getParcelable(WValue.MYADDRESSBO);
                                setRequestParam(myAddressBo);
                                break;
                        }
                    }
                    setPosition(positionXY);//地图定位到新的中心点
                    break;
                case TO_INVOICE://发票的回显

                    invoiceType = data.getStringExtra(WValue.INVOICE_TYPE);
                    invoiceHead=data.getStringExtra(WValue.INVOICE_HEAD);
                    invoiceContent = data.getStringExtra(WValue.INVOICE_CONTENT);
                    if(!TextUtils.isEmpty(invoiceType) && !TextUtils.isEmpty(invoiceHead) && !TextUtils.isEmpty(invoiceContent)){
                        needInvoice = true;
                        tvOrderEditInvoiceInfo.setText(invoiceHead);
                    }else{
                        needInvoice = false;
                        tvOrderEditInvoiceInfo.setText(getText(R.string.order_edit_title_invoice_no));
                        invoiceType=WValue.STRING_EMPTY;
                        invoiceHead=WValue.STRING_EMPTY;
                        invoiceContent=WValue.STRING_EMPTY;
                    }

                    break;
                case TO_COUPON://优惠券的回显
                    if(data!=null){
                        if (NetworkStatusUtil.isNetworkConnected(this)) {
                            couponID = data.getStringExtra("id");
                            tvOrderDetailCoupon.setText(data.getStringExtra("couponName"));
                            isHideLayer(llAvLoadingTransparent44,false);
                            isCouponAmount=true;
                            iOrder.getPayAmount(cardIds,couponID,TAG);
                        } else {
                            couponID = WValue.STRING_EMPTY;
                            tvOrderDetailCoupon.setText(WValue.STRING_EMPTY);
                            ToastUtil.shortToast(MasApplication.getInstance(), R.string.toast_order_coupon_amount_failure);
                        }
                    }
                    break;
                case TO_ADDRESS://我的地址的回显
//                    MyAddressBo myAddressBo=data.getExtras().getParcelable(WValue.MYADDRESSBO);
                    MyAddressBo myAddressBo=data.getParcelableExtra(WValue.MYADDRESSBO);
                    setRequestParam(myAddressBo);
                    setPosition(positionXY);//地图定位到新的中心点
                    break;
            }
        }
    }

    /**
     * 设置提交订单接口的参数值
     * @param myAddressBo
     */
    private void setRequestParam(MyAddressBo myAddressBo) {
        String location=myAddressBo.getLocation();
        if(!TextUtils.isEmpty(location) && !WValue.NULL_LOWER_CASE.equals(location) && !WValue.NULL_UPPER_CASE.equals(location)){
            String[] arrs=location.split(",");
            pointLn=new LatLonPoint(Double.parseDouble(arrs[0]),Double.parseDouble(arrs[1]));
            geoLat=pointLn.getLatitude();//纬度
            geoLng=pointLn.getLongitude();//经度
            positionXY = location;//经纬度 字符串类型
        }
        provinceName=myAddressBo.getProvinceName(); //省
        cityName=myAddressBo.getCityName();    //城市
        areaName=myAddressBo.getAreaName();  //区
        areaPath=myAddressBo.getAreaPath();  //定位的详细地址
        address=myAddressBo.getAddress();
        name=myAddressBo.getName();
        phone=myAddressBo.getMobile();
        addressID=myAddressBo.getAddress_id();

        etOrderEditDetail.setText(address);
        etOrderEditMen.setText(name);
        etOrderEditPhone.setText(phone);
        tvOrderEditAddress.setText(areaPath);
    }


    private void setPosition(String xy) {
        if (!TextUtils.isEmpty(xy)) {
            String[] latLng = xy.split(",");
            LatLng po = new LatLng(Double.parseDouble(latLng[1]), Double.parseDouble(latLng[0]));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(po));
        }

    }

    /**
     * 逆地理编码(坐标转地址)
     * @param regeocodeResult
     * @param i
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //地图移动后,把数据更新
        if (i == 1000) {
            RegeocodeAddress geoAddress = regeocodeResult.getRegeocodeAddress();
            pointLn=regeocodeResult.getRegeocodeQuery().getPoint();//坐标对象
            geoLat=pointLn.getLatitude();//纬度
            geoLng=pointLn.getLongitude();//经度
            positionXY = TextUtils.concat(String.valueOf(geoLng),",",String.valueOf(geoLat)).toString();//经纬度 字符串类型
            provinceName=geoAddress.getProvince(); //省
            cityName=geoAddress.getCity();    //城市
            areaName=geoAddress.getDistrict();    //区
            areaPath=geoAddress.getFormatAddress();  //定位的详细地址
            tvOrderEditAddress.setText(areaPath);
        }

    }

    /**
     * 地理编码（地址转坐标）
     * @param geocodeResult
     * @param i
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

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
                        areaPath = item.getTitle();
                        positionXY = TextUtils.concat(String.valueOf(geoLng),",",String.valueOf(geoLat)).toString();//经纬度 字符串类型
                        provinceName=item.getProvinceName(); //省
                        cityName=item.getCityName();    //城市
                        areaName=item.getAdName();    //区

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
        String message = result.getMsg();
        switch (result.getAction()){
            case WAction.ORDER_AMOUNT_PAYABLE:
                if(isCouponAmount){
                    couponID = WValue.STRING_EMPTY;
                    tvOrderDetailCoupon.setText(WValue.STRING_EMPTY);
//                    tvOrderDetailCoupon.setText(getText(R.string.coupon_hint_not_use));
                    isCouponAmount=false;
                }
                if(TextUtils.isEmpty(message)){
                    message=getText(R.string.toast_order_amount_failure).toString().trim();
                }
                break;
            case WAction.ORDER_SUBMIT:
                if(TextUtils.isEmpty(message)){
                    message=getText(R.string.toast_order_commit_failure).toString().trim();
                }
                break;
        }
        ToastUtil.shortToast(this, message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch (result.getAction()){
            case WAction.ORDER_SUBMIT:
                isHideLayer(llAvLoadingTransparent44,true);
                SubmitOrderBo submitOrderBo=((SubmitOrderResult)result).getData();
                /**
                 * 1,删除本地购物车的数据
                 * 2,跳转页面
                 */
                SqlUtil.deleteCartData(cardIds);
                EventBus.getDefault().post(new RefreshCartEvent());
                if (WValue.PAYMODE_ON_LINE.equals(payMode)) {
                    Intent intent = new Intent(this, PayOnlineActivity.class);
                    intent.putExtra("order_id",submitOrderBo.getOrderId());
                    startActivity(intent);
                }
                if (WValue.PAYMODE_OFF_LINE.equals(payMode)) {
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra("orderID", submitOrderBo.getOrderId());//FIXME
                    startActivity(intent);
                }
                ToastUtil.shortToast(this, R.string.toast_order_commit_success);
                finish();
                break;
            case WAction.GET_SHOPPING_CART_DATA:
                try {
                    CartListResult cartListResult = (CartListResult) result;
                    for (int i = 0; i < cartListResult.getData().size(); i++) {
                        if (cardIds.contains(cartListResult.getData().get(i).getCart_id())) {
                            cartBoList.add(cartListResult.getData().get(i));
                        }
                    }
                } catch (Exception e) {

                }
                updateList();
                isHideLayer(llAvLoadingTransparent44,true);
                break;
            case WAction.ORDER_AMOUNT_PAYABLE:
                OrderAmountPayableResult.DataBean dataBean=((OrderAmountPayableResult)result).getData();
                if(null!=dataBean){
                    if(isCouponAmount){
                        isCouponAmount=false;
                        isHideLayer(llAvLoadingTransparent44,true);
                    }
                    tvOrderEditGoodsmoney.setText(dataBean.getGoodsAmount());
                    tvOrderEditFinallyMoney.setText(dataBean.getOrderAmount());
                    tvOrderEditBenefit.setText(dataBean.getDiscountAmount());
                }
                break;
        }
    }


}
