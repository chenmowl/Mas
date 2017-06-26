package com.eme.mas.model;

/**
 * 收藏商品的实体类(详情页)
 *
 * Created by dijiaoliang on 17/1/20.
 */
public class GoodsCollectionResult extends Result {


    /**
     * success : true
     * data : {"key":"0","value":"取消收藏"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * key : 0
         * value : 取消收藏
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
