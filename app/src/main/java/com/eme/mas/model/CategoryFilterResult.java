package com.eme.mas.model;

import java.util.List;

/**
 *
 * 分类筛选项信息实体(分类页)
 *
 * Created by dijiaoliang on 17/1/22.
 */
public class CategoryFilterResult extends Result {


    /**
     * success : true
     * data : {"attributeCondition":[{"characteristicName":"品牌","characteristicValue":["威士忌"],"id":10004}],"categoryCondition":[{"id":1002,"imageurl":"12313123","isDefault":true,"name":"白酒"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<AttributeConditionBean> attributeCondition;
        private List<CategoryConditionBean> categoryCondition;

        public List<AttributeConditionBean> getAttributeCondition() {
            return attributeCondition;
        }

        public void setAttributeCondition(List<AttributeConditionBean> attributeCondition) {
            this.attributeCondition = attributeCondition;
        }

        public List<CategoryConditionBean> getCategoryCondition() {
            return categoryCondition;
        }

        public void setCategoryCondition(List<CategoryConditionBean> categoryCondition) {
            this.categoryCondition = categoryCondition;
        }

        public static class AttributeConditionBean {
            /**
             * characteristicName : 品牌
             * characteristicValue : ["威士忌"]
             * id : 10004
             */

            private String characteristicName;
            private String id;
            private List<String> characteristicValue;

            public String getCharacteristicName() {
                return characteristicName;
            }

            public void setCharacteristicName(String characteristicName) {
                this.characteristicName = characteristicName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<String> getCharacteristicValue() {
                return characteristicValue;
            }

            public void setCharacteristicValue(List<String> characteristicValue) {
                this.characteristicValue = characteristicValue;
            }

//            public static class
        }

        public static class CategoryConditionBean {
            /**
             * id : 1002
             * imageurl : 12313123
             * isDefault : true
             * name : 白酒
             */

            private String id;
            private String imageurl;
            private String isDefault;
            private String name;

            public void setId(String id) {
                this.id = id;
            }

            public String getImageurl() {
                return imageurl;
            }

            public void setImageurl(String imageurl) {
                this.imageurl = imageurl;
            }

            public String getId() {
                return id;
            }

            public String getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(String isDefault) {
                this.isDefault = isDefault;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
