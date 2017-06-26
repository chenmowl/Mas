package com.eme.mas.model.entity.home;

/**
 * home页面手机专享的banner基类
 * <p>
 * Created by dijiaoliang on 16/6/12.
 */
public class HomePhoneVipBannerBo {

//            "id": "2",
//            "weburl": "http://localhost:8080/leimingtech-front/#",
//            "name": "大广告2",
//            "imageurl": "/upload/img/adv/1465958565457.jpg",
//            "type": "0""


    private String id;
    private String weburl;
    private String name;
    private String imageurl;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
