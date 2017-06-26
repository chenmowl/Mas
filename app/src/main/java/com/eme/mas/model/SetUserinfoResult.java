package com.eme.mas.model;

/**
 * 设置用户信息bean
 *
 * Created by dijiaoliang on 16/11/3.
 */
public class SetUserinfoResult extends Result {


    /**
     * url : /upload/img/avatar/1478138003350.jpg
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
