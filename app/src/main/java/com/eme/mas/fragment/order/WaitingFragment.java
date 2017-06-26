package com.eme.mas.fragment.order;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.activity.OrderDetailActivity;
import com.eme.mas.adapter.OrderWaitingAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.customeview.MListView;
import com.eme.mas.customeview.dialog.OrderContactDialog;
import com.eme.mas.eventbus.OrderCountEvent;
import com.eme.mas.fragment.BaseFragment;
import com.eme.mas.model.MyOrderResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.BrotherLocationBo;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.service.BrotherLocationService;
import com.eme.mas.utils.AMapUtil;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.eme.mas.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 等待收货模块
 * Created by zulei on 16/8/5.
 */

@WLayout(layoutId = R.layout.fragment_order_waiting)
public class WaitingFragment extends BaseFragment implements AMapLocationListener, LocationSource {

    private final static String TAG = WaitingFragment.class.getSimpleName();

    @Bind(R.id.mv_order_waiting_map)
    MapView mapView;
    @Bind(R.id.mlv_order_waiting)
    MListView mlvOrderWaiting;
    @Bind(R.id.ib_order_waiting_open)
    ImageButton ibOrderWaitingOpen;
    @Bind(R.id.rl_order_waiting_list)
    RelativeLayout rlOrderWaitingList;
    @Bind(R.id.tv_fow_shopper_name)
    TextView tvFowShopperName;
    @Bind(R.id.tv_fow_served_time)
    TextView tvFowServedTime;
    @Bind(R.id.tv_no_data_hint)
    TextView tvNoDataHint;
    @Bind(R.id.iv_no_data_pic)
    ImageView ivNoDataPic;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;
    @Bind(R.id.rl_order_waiting_content)
    RelativeLayout rlOrderWaitingContent;
    @Bind(R.id.ll_av_loading_not_transparent_full)
    LinearLayout llAvLoading;

    private boolean isOpen;
    //这个是动画的时间
    private long animTime = 300;
    //    public int y;
    //弹出框的高度
    private int measuredHeight;
    private static OrderWaitingAdapter waitingFragmentAdapter;
    //private BrotherLocationService brotherLocationService;
    private int oneItemHeight; //一个item的高度

    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mapLocation;
    private MarkerOptions markerBrother;
    private Marker marker;
    private LatLng latLngMy, latLngBrother, latLngBrother2;
    private IOrder iOrder;
    private List<MyOrderBo> list;
    private MyHandler mHandler;
    private int itemMapFlag;//记录第几个item在使用地图
    private boolean isDistributing;
    private boolean isFirstLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initHandler();
        initData();
        initMap();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        try {
            if (null != list || list.size() == 0) {
                MyOrderBo myOrderBo = list.get(0);
                String orderStatus = myOrderBo.getOrder_status();
                if ("20".equals(orderStatus)) {
                    Intent intent = new Intent(getActivity(), BrotherLocationService.class);
                    intent.putExtra("oid", myOrderBo.getOrder_id());
                    getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        try {
            getActivity().unbindService(conn);
        } catch (Exception e) {
        }
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ((BaseImpl) iOrder).cancelRequestByTag(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mapView) {
            mapView.onDestroy();
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    private void initView() {
        rlOrderWaitingContent.setVisibility(View.GONE);
    }

    private boolean moved = false;

    private void initListener() {
        mlvOrderWaiting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        moved = true;
                        if (isOpen) {
                            return false;
                        } else {
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        if (!isOpen && moved) {
                            moved = false;
                            return true;
                        } else {
                            moved = false;
                            return false;
                        }
                }
                return false;
            }
        });

        mlvOrderWaiting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                MyOrderBo myOrderBo = waitingFragmentAdapter.getItem(position);
                intent.putExtra("status", myOrderBo.getOrder_status());
                startActivity(intent);
            }
        });
    }

    private void initHandler() {
        mHandler = new MyHandler(this);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
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
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        float preZoom = (float) 17.5;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(preZoom));//高德地图的缩放级别是在3-20 之间,默认为17。5.随手势的改变动态改变
//        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {//解决外套ScrollView的焦点问题
//            @Override
//            public void onTouch(MotionEvent arg0) {
//                svAa.requestDisallowInterceptTouchEvent(true);
//            }
//        });
    }

    private void initData() {
        isFirstLoad = true;
        iOrder = mController.getOrder(this);
        getDataOnline();

        latLngBrother = new LatLng(Double.parseDouble("39.908004"), Double.parseDouble("116.465972"));
        latLngBrother2 = new LatLng(Double.parseDouble("39.907596"), Double.parseDouble("116.462265"));

        list = new ArrayList();
        waitingFragmentAdapter = new OrderWaitingAdapter(getActivity(), list, R.layout.item_order_waiting, mHandler, 0);
        mlvOrderWaiting.setAdapter(waitingFragmentAdapter);

    }

    private void updateView() {
        if (null != list && list.size() != 0) {
            if (list.size() <= 1) {
                ibOrderWaitingOpen.setVisibility(View.GONE);
            } else {
                ibOrderWaitingOpen.setVisibility(View.VISIBLE);
            }
            rlNoData.setVisibility(View.GONE);
            rlOrderWaitingContent.setVisibility(View.VISIBLE);
            waitingFragmentAdapter.notifyDataSetChanged();
            measuredHeight = ViewUtil.measureHeight(mlvOrderWaiting);
            oneItemHeight = ViewUtil.measureItemHeight(mlvOrderWaiting, waitingFragmentAdapter);
            rlOrderWaitingList.setTranslationY(measuredHeight - oneItemHeight);

            MyOrderBo myOrderBo = list.get(0);
            String orderStatus = myOrderBo.getOrder_status();
            if ("20".equals(orderStatus)) {
                Intent intent = new Intent(getActivity(), BrotherLocationService.class);
                intent.putExtra("oid", myOrderBo.getOrder_id());
                getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

            }


        } else {
            rlNoData.setVisibility(View.VISIBLE);
        }

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            BrotherLocationService brotherLocationService = ((BrotherLocationService.MsgBinder) service).getService();

            //注册回调接口来接收下载进度的变化
            brotherLocationService.setOnLocationListener(new BrotherLocationService.OnLocationListener() {
                @Override
                public void onLocation(BrotherLocationBo brotherLocationBo) {
                    Log.i("info", "小哥定位成功");
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = brotherLocationBo;
                    mHandler.sendMessage(msg);
                }
            });

        }
    };

    private void getDataOnline() {
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            isHideLayer(llAvLoading,false);
            iOrder.orderQuery("1", "10", TAG);//0 历史订单 10 待收货 20 待处理

        } else {
            ToastUtil.shortToast(getActivity(), R.string.net_error);
        }
    }

    @OnClick(R.id.rl_fow_contact)
    public void contact() {
        final OrderContactDialog ocDialog = new OrderContactDialog(isDistributing);
        ocDialog.setOnContactListener(new OrderContactDialog.OnContactListener() {
            @Override
            public void contactBrother() {
                ToastUtil.shortToast(getActivity(), "brother");
                ocDialog.cancel();
            }

            @Override
            public void contactBusiness() {
                ToastUtil.shortToast(getActivity(), "business");
                ocDialog.cancel();
            }
        });
        ocDialog.showDialog(getActivity(), null);

    }

    @OnClick({R.id.btn_stroll, R.id.btn_stroll_nodata})
    public void loadAgain() {
        rlNoNetwork.setVisibility(View.GONE);
        getDataOnline();
    }


    @OnClick(R.id.ib_order_waiting_open)
    public void onClick() {
        if (isOpen) {
            //mlvOrderWaiting.setSelection(itemMapFlag);
            mlvOrderWaiting.setSelection(0);
            ObjectAnimator o1 = ObjectAnimator.ofFloat(rlOrderWaitingList, "translationY", measuredHeight - oneItemHeight).setDuration(animTime);
            o1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ibOrderWaitingOpen.setImageResource(R.mipmap.shangla);
                }
            });
            o1.start();
            isOpen = false;
        } else {

            ObjectAnimator o2 = ObjectAnimator.ofFloat(rlOrderWaitingList, "translationY", 0).setDuration(animTime);
            o2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ibOrderWaitingOpen.setImageResource(R.mipmap.xiala);
                }
            });
            o2.start();
            isOpen = true;
        }
    }


    private void setBrotherPosition(LatLng latLngb) {
        if (null != latLngb && null != latLngMy) {
            float distance = AMapUtils.calculateLineDistance(latLngMy, latLngb);

            View viewMarker = null;
            if (markerBrother == null) {
                markerBrother = new MarkerOptions();
                markerBrother.draggable(true);//marker是否可移动

                viewMarker = LayoutInflater.from(getActivity()).inflate(R.layout.amap_marker, null);
                TextView tvMarkerDistance = (TextView) viewMarker.findViewById(R.id.tv_amap_marker_distance);
                tvMarkerDistance.setText("距您" + (int) distance + "米");


            } else {

                viewMarker = LayoutInflater.from(getActivity()).inflate(R.layout.amap_marker, null);
                TextView tvMarkerDistance = (TextView) viewMarker.findViewById(R.id.tv_amap_marker_distance);
                tvMarkerDistance.setText("距您" + (int) distance + "米");

                //marker.setPosition(latLngb);
            }

            markerBrother.position(latLngb);
            markerBrother.icon(BitmapDescriptorFactory.fromView(viewMarker));

            if (marker != null) marker.remove();
            marker = aMap.addMarker(markerBrother);

            //对称点
            double x = 2 * latLngMy.latitude - latLngb.latitude;
            double y = 2 * latLngMy.longitude - latLngb.longitude;
            LatLng sym = new LatLng(x, y);

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(latLngMy).include(latLngb).include(sym).build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            float currentZoom = aMap.getCameraPosition().zoom;
            aMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom - 1f));
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
                double geoLat = location.getLatitude();
                double geoLng = location.getLongitude();
                latLngMy = new LatLng(geoLat, geoLng);
                //LatLonPoint pointLn = new LatLonPoint(geoLat, geoLng);
                mListener.onLocationChanged(location);// 显示系统小点
                deactivate();

                setBrotherPosition(latLngBrother);

            } else {
                String errText = "定位失败," + location.getErrorCode() + ": " + location.getErrorInfo();
                Log.i("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
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

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    static class MyHandler extends Handler {
        WeakReference<WaitingFragment> mActivity;

        MyHandler(WaitingFragment activity) {
            mActivity = new WeakReference<WaitingFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WaitingFragment theFragment = mActivity.get();
            switch (msg.what) {
                case 1:
                    try {
                        theFragment.getActivity().unbindService(theFragment.conn);
                    } catch (Exception e) {

                    }

                    MyOrderBo myOrderBo = (MyOrderBo) msg.obj;
                    String orderStatus = myOrderBo.getOrder_status();
                    int p = msg.arg1;
                    theFragment.itemMapFlag = p;
                    waitingFragmentAdapter.setFlagAndNotify(theFragment.itemMapFlag);
                    //正在配送
                    if ("20".equals(orderStatus)) {
                        theFragment.isDistributing = true;
                        theFragment.tvFowShopperName.setText("大师兄");
                        //theFragment.setBrotherPosition(theFragment.latLngBrother);
                        //theFragment.initRoadData();
                        //theFragment.moveLooper();

                        Intent intent = new Intent(theFragment.getActivity(), BrotherLocationService.class);
                        intent.putExtra("oid", myOrderBo.getOrder_id());
                        theFragment.getActivity().bindService(intent, theFragment.conn, Context.BIND_AUTO_CREATE);
                    }

                    //正在出库
                    if ("30".equals(orderStatus)) {
                        theFragment.isDistributing = false;
                        theFragment.tvFowShopperName.setText("正在出库");
                        //theFragment.setBrotherPosition(theFragment.latLngBrother2);
                    }
                    break;
                case 2:
                    BrotherLocationBo brotherLocationBo = (BrotherLocationBo) msg.obj;
                    String location = brotherLocationBo.getCurrent_location();
                    if (TextUtils.isEmpty(location)) {
                        ToastUtil.shortToast(theFragment.getActivity(), "小哥位置获取失败");
                    } else {
                        ToastUtil.shortToast(theFragment.getActivity(), "小哥位置获取成功");
                        //FIXME 定位
                        //setBrotherPosition();

                    }
                    break;
            }
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        //isHideLayer(llAvLoading,true);
        //String message = result.getMsg();
        //ToastUtil.shortToast(getActivity(), message);
        rlNoNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        if(!isFirstLoad){
            EventBus.getDefault().post(new OrderCountEvent());
        }
        isFirstLoad = false;

        isHideLayer(llAvLoading,true);
        if (null != result) {
            String action = result.getAction();
            if (WAction.ORDER_QUERY.equals(action)) {
                MyOrderResult myOrderResult = (MyOrderResult) result;
                try {
                    List<MyOrderBo> myOrderBos = myOrderResult.getData();
                    list.clear();
                    list.addAll(myOrderBos);
                } catch (Exception e) {

                }
                updateView();

            }

        }

    }


//
//    /**
//     * marker平滑移动相关代码
//     */
//    private Marker mMoveMarker;
//    private Polyline mVirtureRoad;
//
//    private void initRoadData() {
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.add(latLngBrother);
//        polylineOptions.add(latLngBrother2);
//        polylineOptions.width(10);
//        polylineOptions.color(Color.RED);
//        mVirtureRoad = aMap.addPolyline(polylineOptions);
//        MarkerOptions markerOptions = new MarkerOptions();
//        //markerOptions.setFlat(true);
//        markerOptions.anchor(0.5f, 0.5f);
//        markerOptions.icon(BitmapDescriptorFactory
//                .fromResource(R.mipmap.zuobia0));
//        markerOptions.position(polylineOptions.getPoints().get(0));
//        mMoveMarker = aMap.addMarker(markerOptions);
//        mMoveMarker.setRotateAngle((float) getAngle(0));
//
//    }
//
//    /**
//     * 循环进行移动逻辑
//     */
//    public void moveLooper() {
//        new Thread() {
//
//            public void run() {
//                while (true) {
//                    for (int i = 0; i < mVirtureRoad.getPoints().size() - 1; i++) {
//
//
//                        LatLng startPoint = mVirtureRoad.getPoints().get(i);
//                        LatLng endPoint = mVirtureRoad.getPoints().get(i + 1);
//                        mMoveMarker
//                                .setPosition(startPoint);
//
//                        mMoveMarker.setRotateAngle((float) getAngle(startPoint,
//                                endPoint));
//
//                        double slope = getSlope(startPoint, endPoint);
//                        //是不是正向的标示（向上设为正向）
//                        boolean isReverse = (startPoint.latitude > endPoint.latitude);
//
//                        double intercept = getInterception(slope, startPoint);
//
//                        double xMoveDistance = isReverse ? getXMoveDistance(slope)
//                                : -1 * getXMoveDistance(slope);
//
//
//                        for (double j = startPoint.latitude;
//                             !((j > endPoint.latitude) ^ isReverse);
//
//                             j = j
//                                     - xMoveDistance) {
//                            LatLng latLng = null;
//                            if (slope != Double.MAX_VALUE) {
//                                latLng = new LatLng(j, (j - intercept) / slope);
//                            } else {
//                                latLng = new LatLng(j, startPoint.longitude);
//                            }
//                            mMoveMarker.setPosition(latLng);
//                            try {
//                                Thread.sleep(TIME_INTERVAL);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                }
//            }
//
//        }.start();
//    }
//
//
//    /**
//     * 根据点获取图标转的角度
//     */
//    private double getAngle(int startIndex) {
//        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
//            throw new RuntimeException("index out of bonds");
//        }
//        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
//        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
//        return getAngle(startPoint, endPoint);
//    }
//
//    /**
//     * 根据两点算取图标转的角度
//     */
//    private double getAngle(LatLng fromPoint, LatLng toPoint) {
//        double slope = getSlope(fromPoint, toPoint);
//        if (slope == Double.MAX_VALUE) {
//            if (toPoint.latitude > fromPoint.latitude) {
//                return 0;
//            } else {
//                return 180;
//            }
//        }
//        float deltAngle = 0;
//        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
//            deltAngle = 180;
//        }
//        double radio = Math.atan(slope);
//        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
//        return angle;
//    }
//
//    /**
//     * 根据点和斜率算取截距
//     */
//    private double getInterception(double slope, LatLng point) {
//
//        double interception = point.latitude - slope * point.longitude;
//        return interception;
//    }
//
//    /**
//     * 算取斜率
//     */
//    private double getSlope(int startIndex) {
//        if ((startIndex + 1) >= mVirtureRoad.getPoints().size()) {
//            throw new RuntimeException("index out of bonds");
//        }
//        LatLng startPoint = mVirtureRoad.getPoints().get(startIndex);
//        LatLng endPoint = mVirtureRoad.getPoints().get(startIndex + 1);
//        return getSlope(startPoint, endPoint);
//    }
//
//    /**
//     * 算斜率
//     */
//    private double getSlope(LatLng fromPoint, LatLng toPoint) {
//        if (toPoint.longitude == fromPoint.longitude) {
//            return Double.MAX_VALUE;
//        }
//        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
//        return slope;
//
//    }
//
//    private static final int TIME_INTERVAL = 80;
//    private static final double DISTANCE = 0.0001;
//
//    /**
//     * 计算x方向每次移动的距离
//     */
//    private double getXMoveDistance(double slope) {
//        if (slope == Double.MAX_VALUE) {
//            return DISTANCE;
//        }
//        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
//    }
}
