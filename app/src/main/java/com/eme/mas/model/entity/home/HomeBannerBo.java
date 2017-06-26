package com.eme.mas.model.entity.home;

/**
 * HomeFragment页
 * <p>
 * banner基类
 * <p>
 * Created by dijiaoliang on 16/7/27.
 */
public class HomeBannerBo {

//            "id": "100013",
//            "imageurl": "",
//            "linkUrl": "",
//            "menu": 1,
//            "name": "首页轮播图"

    private String id;
    private String imageurl;
    private String linkUrl;
    private String menu;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
