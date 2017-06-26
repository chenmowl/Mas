package com.eme.mas.model.entity;

import java.util.List;

/**
 * 分类页面
 * <p/>
 * 分类规格二级实体类
 * <p/>
 * Created by dijiaoliang on 16/4/27.
 */
public class CategorySpecVo {


    private String id;
    private String name;
    private String parentid;
    private List<SpecVo> list;

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

    public List<SpecVo> getList() {
        return list;
    }

    public void setList(List<SpecVo> list) {
        this.list = list;
    }
}
