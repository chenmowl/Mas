package com.eme.mas.controller.customeInterface;

/**
 * Created by zhangxiaoming on 16/6/6.
 */
public interface IProduct {

    /**
     * 热搜词
     */
    void getHotWords(String tag);

    /**
     * 获取搜索结果
     */
    void getSearchResult(String condition,String pageNo,String pageSize,String sortField,String order, String tag);

    /**
     * 获取homefragment页面数据
     */
    void getHomeData(String tag);

    /**
     * 获取homefragment页面的热门推荐数据
     */
    void getHomeRecData(String page_no,String pageSize, String tag);

    /**
     * 获取商品分类数据(分类页面)
     */
    void getProductCategory(String tag);

    /**
     * 商品筛选项数据(分类页面)
     */
    void getCategoryFilter(String categoryId, String tag);

    /**
     * 商品分类搜索结果数据(分类页面)
     */
    void getCategorySearchData(String gcIds, String sort, String page_no, String tag);

    /**
     * 商品详情接口 （商品详情页）
     */
    void getProductDetailData(String skuId, String tag);

    /**
     * 评价列表接口(商品详情)
     */
    void evaluateListData(String skuId, String pageNo,String pageSize, String tag);

    /**
     * 评价商品列表接口（商品详情页）
     */
    void getProductEvaluateListData(String goodsId, String pageNo, String tag);

    /**
     * 商品评价各项指标接口（商品详情页）
     */
    void getProductEvaluateScoreData(String goodsId, String tag);

    /**
     * 商品详情 热门推荐商品接口（商品详情页）
     */
    void getProductDetailHotData(String product_id, String tag);

    /**
     * 收藏商品（商品详情页）
     */
    void collecteGoods(String skuId, String tag);

    /**
     * 收藏商品（商品详情页）
     */
    void collecteProduct(String product_id, String tag);

    /**
     * 取消收藏商品（商品详情页）
     */
    void cancelCollecteProduct(String product_id, String tag);

    /**
     * 获取收藏夹数据
     */
    void getCollectionList(String page_no, String tag);

    /**
     * 商品评价接口
     */
    void setEvaluate(String params,String tag);

}
