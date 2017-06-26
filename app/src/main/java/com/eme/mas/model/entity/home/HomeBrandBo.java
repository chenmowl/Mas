package com.eme.mas.model.entity.home;

/**
 * home品牌的基类
 * <p>
 * Created by dijiaoliang on 16/6/12.
 */
public class HomeBrandBo {

//            "brandId": "",
//            "brandName": "五粮液",
//            "imageurl": ""

    private String brandId;
    private String brandName;
    private String imageurl;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
