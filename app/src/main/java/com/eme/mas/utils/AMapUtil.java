package com.eme.mas.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.eme.mas.R;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.model.entity.MapItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jone on 16/2/22.
 */
public class AMapUtil implements AMapLocationListener, PoiSearch.OnPoiSearchListener,
        GeocodeSearch.OnGeocodeSearchListener {

    private Context mContext;
  //  private SharedPreferences spLocation;
   // private SharedPreferences.Editor edi;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    public boolean isGpsForResult, isOpenGps;
    public static boolean isChangLocation;
    private GeocodeSearch geocoderSearch;
    private static volatile AMapUtil instance;
    private int count;
    private String preLocation, currentCityName, gpsName, keyWord, mPoiType;
    private Double geoLat, geoLng;
    private ArrayList<MapItem> items, keyWordItems;
    private PoiSearch.Query query;
    private AMapLocation location;
    private ArrayList<LatLng> AllLatLngs;

    public AMapUtil(Context context) {
        mContext = context;

    }


    public void start() {

        preLocation = SPBase.getContent(mContext, SpConstant.LOGIN_FILE_NAME,SpConstant.LOCATION_KEY);
//        edi = spLocation.edit();
        mlocationClient = new AMapLocationClient(mContext.getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mlocationClient.setLocationListener(this);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(this);
        items = new ArrayList<MapItem>();
        keyWordItems = new ArrayList<MapItem>();
        count = 0;
        isGpsForResult = false;

    }

    public static AMapUtil getInstance(Context mContext) {
        if (instance == null) {
            Class var0 = AMapUtil.class;
            synchronized (AMapUtil.class) {
                if (instance == null) {
                    instance = new AMapUtil(mContext.getApplicationContext());
                }
            }
        }

        return instance;
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }


    /**
     * 获取配送地址
     */
    public String getGpsName() {


        if (isGpsForResult) {

            if (null != gpsName) {
                return gpsName;
            } else {
                if (isChangLocation) {
                    gpsName = "";
                } else {
                    gpsName = SPBase.getContent(mContext,SpConstant.LOCATION_FILE_NAME,SpConstant.LOCATION_NAME_KEY);
                }
            }
        } else {
            gpsName = "";
        }
        return gpsName;

    }


    /**
     * 设置配送地址
     */

    public void setGpsName(String name) {
        this.gpsName = name;
    }


    /**
     * poi周边搜索
     */

    public void doSearchQuery(String poiTypes) {
        //查询新坐标附近的地址信息
        keyWord = "";
        query = new PoiSearch.Query(keyWord, poiTypes, "");
        PoiSearch poiSearch = new PoiSearch(mContext, query);

        if (isGpsForResult) {
            if(null!=geoLat && null!=geoLng){
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(geoLat, geoLng), 200));//设置周边搜索的中心点以及区域
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
                isChangLocation = false;
                items.clear();
            }
        }
    }

    /**
     * 关键字搜索
     */

    public void doSearchQueryForKeyWord(String poiTypes, String text) {
        keyWord = text;
        query = new PoiSearch.Query(keyWord, poiTypes, currentCityName);
        query.setCityLimit(false);
        PoiSearch poiSearch = new PoiSearch(mContext, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
        keyWordItems.clear();

    }

    /**
     * 获取当前的定位坐标是否发生改变
     */
    public boolean isCurrentGpsLocation() {
        return isChangLocation;
    }

    /**
     * 回调定位的结果
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (count == 0) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                String currentCity = aMapLocation.getCity();
                if (null != currentCity && !"".equals(currentCity)) {
                    setCurrentCityName(currentCity);
                }
                geoLat = aMapLocation.getLatitude();
                geoLng = aMapLocation.getLongitude();
                String strLocation = geoLng + "," + geoLat;
                isGpsForResult = true;

                if (!preLocation.equals(strLocation)) {
                    isChangLocation = true;
                    SPBase.setContent(mContext,SpConstant.LOCATION_FILE_NAME,SpConstant.LOCATION_KEY,strLocation);
                    SPBase.setContent(mContext,SpConstant.LOCATION_FILE_NAME,SpConstant.LOCATION_CITY_CODE,aMapLocation.getCityCode());
//                    edi.putString("location", strLocation);
//                    edi.putString("geoLat", geoLat + "");
//                    edi.putString("geoLng", geoLng + "");
//                    edi.commit();
                    //逆地理编码
//                    LatLonPoint latLonPoint = new LatLonPoint(geoLat, geoLng);
//                    getAddress(latLonPoint);
                    destroy();
                }
                location = aMapLocation;
                count++;
            } else if (aMapLocation.getErrorCode() == 12) {//缺少定位权限
                SPBase.setContent(mContext,SpConstant.LOCATION_FILE_NAME,SpConstant.LOCATION_KEY,"");
//                edi.putString("location", "");
                isGpsForResult = false;
                isOpenGps = true;
//                edi.commit();
            } else {//定位失败
                SPBase.setContent(mContext,SpConstant.LOCATION_FILE_NAME,SpConstant.LOCATION_KEY,"");
//                edi.putString("location", "");
                isGpsForResult = false;
//                edi.commit();
            }
        }


    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    PoiResult poiResult = result;
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        for (int i = 0; i < poiItems.size(); i++) {
                            if (i < 8) {
                                MapItem item = new MapItem();
                                item.setName(poiItems.get(i).getTitle());
                                item.setAddress(poiItems.get(i).getSnippet());
                                item.setStrCity(poiItems.get(i).getCityName());
                                String strLocation = poiItems.get(i).getLatLonPoint().getLongitude() + "," + poiItems.get(i).getLatLonPoint().getLatitude();

                                if (isNumeric(strLocation)) {
                                    item.setLocation(strLocation);
                                } else {
                                    item.setLocation("");
                                }
                                if (keyWord.equals("")) {
                                    items.add(item);
                                } else {
                                    keyWordItems.add(item);
                                }
                            }
                        }

                    } else if (suggestionCities != null && suggestionCities.size() > 0) {// ToDo无数据隐藏listview，有历史记录时，可以再次展示历史记录

                        showSuggestCity(suggestionCities);
                    }
                }

            } else if (rCode == 27) {
                ToastUtil.shortToast(mContext, R.string.error_network);
            } else if (rCode == 32) {
                ToastUtil.shortToast(mContext, R.string.error_key);
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 正则表达式的判断
     */
    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d+,\\d{1,3}\\.\\d+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public void destroy() {

        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }


    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                gpsName = result.getRegeocodeAddress().getNeighborhood();
                setGpsName(gpsName);
                SPBase.setContent(mContext,SpConstant.LOCATION_FILE_NAME,SpConstant.LOCATION_NAME_KEY,gpsName);
            } else {
                ToastUtil.shortToast(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.shortToast(mContext, R.string.error_network);
        }
    }


    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String information = mContext.getString(R.string.recommend_city) + "\n";
        for (int i = 0; i < cities.size(); i++) {
            information += R.string.city_name + cities.get(i).getCityName() + R.string.city_area_code + cities.get(i).getCityCode() + R.string.city_code
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.shortToast(mContext, information);

    }

    /**
     * 获取poi周边搜索地址信息
     */

    public ArrayList<MapItem> getItems() {

        return items;
    }

    /**
     * 获取poi关键字搜索地址信息
     */

    public ArrayList<MapItem> getItemsForKeyWord() {

        return keyWordItems;
    }

    /**
     * 设置poi类型
     */

    public void setPoiType(String poiType) {
        this.mPoiType = poiType;
    }

    /**
     * 获取poi类型
     */

    public String getPoiType() {

        if (null != mPoiType) {
            return mPoiType;
        } else {
            return "";
        }

    }

    /**
     * 获取定位成功后的ampLocation
     */

    public AMapLocation getLocation() {

        return location;
    }

    /**
     * 设置全国开通区域内的经纬度坐标的集合
     */

    public void setAllLatLngs(ArrayList<LatLng> allLatLngs) {
        if (null != AllLatLngs && AllLatLngs.size() > 0) {

            AllLatLngs.clear();
        }
        AllLatLngs = allLatLngs;
    }

    /**
     * 获取全国开通区域内的经纬度坐标的集合
     */
    public ArrayList<LatLng> getAllLatLngs() {

        return AllLatLngs;
    }


    public String getCurrentCityName() {

        if(null==currentCityName){
            currentCityName="北京市";
        }
        return currentCityName;
    }

    public void setCurrentCityName(String currentCityName) {
        this.currentCityName = currentCityName;
    }

}
