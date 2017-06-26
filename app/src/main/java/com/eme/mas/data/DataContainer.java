package com.eme.mas.data;

import java.util.HashMap;

/**
 * Created by dijiaoliang on 16/6/28.
 */
public class DataContainer {

    /**
     * 商品规格数据集合
     */
    private static HashMap<String, String> mHashMap = new HashMap<>();

    /**
     * 商品数量
     */
    private static int count;

    /**
     * 商品名称
     */
    private static String name="";

    /**
     * 商品spec_id
     */
    private static String spec_id="";

    /**
     * 商品product_id
     */
    private static String product_id="";

    /**
     * image_url
     */
    private static String image_url="";

    /**
     * 商品价格
     */
    private static String price="";

    /**
     * 商品积分
     */
    private static String integral="";

    public static String getPrice() {
        return price;
    }

    public static void setPrice(String price) {
        DataContainer.price = price;
    }

    public static String getIntegral() {
        return integral;
    }

    public static void setIntegral(String integral) {
        DataContainer.integral = integral;
    }

    public static String getImage_url() {
        return image_url;
    }

    public static void setImage_url(String image_url) {
        DataContainer.image_url = image_url;
    }

    public static String getProduct_id() {
        return product_id;
    }

    public static void setProduct_id(String product_id) {
        DataContainer.product_id = product_id;
    }

    public static String getSpec_id() {
        return spec_id;
    }

    public static void setSpec_id(String spec_id) {
        DataContainer.spec_id = spec_id;
    }

    public static HashMap<String, String> getmHashMap() {
        return mHashMap;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        DataContainer.count = count;
    }

    public static void setmHashMap(HashMap<String, String> mHashMap) {
        DataContainer.mHashMap = mHashMap;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        DataContainer.name = name;
    }
}
