package com.eme.mas.model.entity;

/**
 * 商品销售属性
 *
 * Created by dijiaoliang on 17/1/18.
 */
public class SkuAttributesSaleBo {

    private String id;
    private String skuId;
    private String attributeNo; //属性编号
    private String saleName;    //销售属性名称
    private String saleValue;	//销售属性值

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getAttributeNo() {
        return attributeNo;
    }

    public void setAttributeNo(String attributeNo) {
        this.attributeNo = attributeNo;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(String saleValue) {
        this.saleValue = saleValue;
    }
}
