package com.eme.mas.model.entity;

/**
 * 分类页面
 *
 * 分类规格基类
 *
 * Created by dijiaoliang on 16/4/27.
 */
public class SpecBo {


//            "id": "3b9f1460cfa841018da1297bb1bc56bd",
//            "name": "葡萄酒",
//            "parentid": "0"

    private String id;
    private String name;
    private String parentid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
