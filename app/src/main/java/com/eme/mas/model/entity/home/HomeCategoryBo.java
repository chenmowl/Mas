package com.eme.mas.model.entity.home;

/**
 * HomeFragment页面的分类基类
 *
 * Created by dijiaoliang on 17/1/12.
 */
public class HomeCategoryBo {

//            "id": "1001",
//            "image": "",
//            "name": "葡萄酒"

    private String id;
    private String image;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
