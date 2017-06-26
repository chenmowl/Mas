package com.eme.mas.model.entity;

/**
 * Created by dijiaoliang on 16/4/27.
 */
public class CartBo {


    private String product_id;
    private String spec_id;
    private String product_name;
    private String product_price;
    private String integral;
    private String product_num;
    private String product_image_url;
    private String image_url;
    private String isSelected="0";//是否被选中
    private String goods_show;//是否无效
    private String cart_id;//购物车id
    private String product_channel;//商品渠道 1 是手机普通 2 是手机专享

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getProduct_channel() {
        return product_channel;
    }

    public void setProduct_channel(String product_channel) {
        this.product_channel = product_channel;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getGoods_show() {
        return goods_show;
    }

    public void setGoods_show(String goods_show) {
        this.goods_show = goods_show;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }
}
