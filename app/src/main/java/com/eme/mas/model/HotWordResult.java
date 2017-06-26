package com.eme.mas.model;

import java.util.List;

/**
 * 热搜索的实体类（搜索页面）
 *
 * Created by dijiaoliang on 16/7/27.
 */
public class HotWordResult extends Result {


    /**
     * id :
     * name : 茅台
     */

    private List<HotWordBean> data;

    public List<HotWordBean> getData() {
        return data;
    }

    public void setData(List<HotWordBean> data) {
        this.data = data;
    }

    public static class HotWordBean {
        private String id;
        private String name;

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
    }


    @Override
    public String toString() {
        return "HotWordResult{" +
                "data=" + data +
                '}';
    }
}
