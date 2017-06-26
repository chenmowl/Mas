package com.eme.mas.model.entity;

import com.eme.mas.model.entity.home.HomeHotGoodsBo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情二级实体
 *
 * Created by dijiaoliang on 17/1/18.
 */
public class GoodsDetailVo {

    private String skuId;//商品skuId
    private String skuName;//商品sku名称
    private String goodsId;//商品Id
    private String goodsName;//商品名称
    private String goodsImage;//商品封面图
    private List<String> moreImage;//商品多图url列表
    private List<GoodsLabelShowBo> goodsLabel;// 标签列表

    private BigDecimal marketPrice;  //市场价
    private BigDecimal salePrice;	//售价
    private String isFavorite;//是否为当前用户所收藏

    private Integer saleQuantity;//总销量

    private List<SkuAttributesSaleBo> saleAttibute;//商品销售属性
    private String sendTime;//派送时间

    private ScoreFlagBo scoreFlag;  //总评论数，好评数，怀评数，中评数及好评率
    private List<GoodsEvaluateContentBo> goodsEvaluate; //商品评价列表

    private List<HomeHotGoodsBo> hotGoods;//热门推荐商品

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public List<String> getMoreImage() {
        return moreImage;
    }

    public void setMoreImage(List<String> moreImage) {
        this.moreImage = moreImage;
    }

    public List<GoodsLabelShowBo> getGoodsLabel() {
        return goodsLabel;
    }

    public Integer getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(Integer saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public void setGoodsLabel(List<GoodsLabelShowBo> goodsLabel) {
        this.goodsLabel = goodsLabel;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public List<SkuAttributesSaleBo> getSaleAttibute() {
        return saleAttibute;
    }

    public void setSaleAttibute(List<SkuAttributesSaleBo> saleAttibute) {
        this.saleAttibute = saleAttibute;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public ScoreFlagBo getScoreFlag() {
        return scoreFlag;
    }

    public void setScoreFlag(ScoreFlagBo scoreFlag) {
        this.scoreFlag = scoreFlag;
    }

    public List<GoodsEvaluateContentBo> getGoodsEvaluate() {
        return goodsEvaluate;
    }

    public void setGoodsEvaluate(List<GoodsEvaluateContentBo> goodsEvaluate) {
        this.goodsEvaluate = goodsEvaluate;
    }

    public List<HomeHotGoodsBo> getHotGoods() {
        return hotGoods;
    }

    public void setHotGoods(List<HomeHotGoodsBo> hotGoods) {
        this.hotGoods = hotGoods;
    }
}
