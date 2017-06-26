package com.eme.mas.model.entity.home;

import com.eme.mas.model.entity.ProductBo;

/**
 * home页面的商品实体类
 * <p>
 * Created by dijiaoliang on 16/6/12.
 */
public class HomeVipProductBo {
    /**
     * product_id : fefa6c417e9d47b6a91de8699437d937
     * product_price : 899.00
     * web_url : null
     * product_name : 茅台酒
     * all_spec : null
     * product_stock_percent : null
     * is_coupon : 0
     * product_integral : 11
     * specs_code : null
     * product_criticism_num : 1
     * old_product_price : null
     * integral : null
     * product_image_url : /upload/img/store/0/1469078419098.jpg
     * goods_show : null
     * old_product_integral : null
     * product_images : ["/upload/img/store/0/1469078419098.jpg"]
     * coupon_info : null
     * product_info : null
     */

    private ProductBo productDetail;
    /**
     * productDetail : {"product_id":"fefa6c417e9d47b6a91de8699437d937","product_price":"899.00","web_url":null,"product_name":"茅台酒","all_spec":null,"product_stock_percent":null,"is_coupon":"0","product_integral":"11","specs_code":null,"product_criticism_num":"1","old_product_price":null,"integral":null,"product_image_url":"/upload/img/store/0/1469078419098.jpg","goods_show":null,"old_product_integral":null,"product_images":["/upload/img/store/0/1469078419098.jpg"],"coupon_info":null,"product_info":null}
     * phone_price : 112
     * imageurl : null
     * product_amount : 211
     */

    private String phone_price;
    private String imageurl;
    private String product_amount;

    public ProductBo getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductBo productDetail) {
        this.productDetail = productDetail;
    }

    public String getPhone_price() {
        return phone_price;
    }

    public void setPhone_price(String phone_price) {
        this.phone_price = phone_price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(String product_amount) {
        this.product_amount = product_amount;
    }
}
