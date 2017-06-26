package com.eme.mas.model.entity;

import java.util.List;

/**
 * 分类页面
 *
 * 分类规格二级实体类
 *
 * Created by dijiaoliang on 16/4/27.
 */
public class SpecVo {

    private String id;
    private String name;
    private String parentid;
    private List<SpecBo> list;

    public List<SpecBo> getList() {
        return list;
    }

    public void setList(List<SpecBo> list) {
        this.list = list;
    }

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
