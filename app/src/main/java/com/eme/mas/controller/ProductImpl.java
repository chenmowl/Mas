package com.eme.mas.controller;

import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.customeInterface.IProduct;

import java.util.HashMap;

/**
 * Created by zhangxiaoming on 16/6/6.
 */
public class ProductImpl extends BaseImpl implements IProduct {

    public ProductImpl(IActionListener iActionListener) {
        super(iActionListener);
    }

    /**
     * 获取热搜词
     */
    @Override
    public void getHotWords(String tag) {
        request(WAction.SEARCH_ACTIVITY_HOT_WORDS, null, tag);
    }

    /**
     * 获取搜索结果（搜索结果页）
     */
    @Override
    public void getSearchResult(String condition,String pageNo,String pageSize,String sortField,String order, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("condition", condition);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        params.put("sortField", sortField);
        params.put("order", order);
        request(WAction.SEARCH_RESULT_ACTIVITY_PROS, params, tag);
    }

    /**
     * 获取homefragment页面数据
     */
    @Override
    public void getHomeData(String tag) {
        request(WAction.HOME_FRAGMENT_DATA, null, tag);
    }


    /**
     * 获取homefragment页面的热门推荐数据
     */
    @Override
    public void getHomeRecData(String pageNo,String pageSize, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        request(WAction.HOME_FRAGMENT_RECDATA, params, tag);
    }


    /**
     * 获取商品分类数据(分类页面)
     */
    @Override
    public void getProductCategory(String tag) {
        request(WAction.CATEGORY_ACTIVITY_DATA, null, tag);
    }

    /**
     * 商品筛选项数据(分类页面)
     */
    @Override
    public void getCategoryFilter(String categoryId, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("categoryId", categoryId);
        request(WAction.CATEGORY_FILTER_DATA, params, tag);
    }

    /**
     * 商品分类搜索结果数据(分类页面)
     */
    @Override
    public void getCategorySearchData(String gcIds, String sort, String page_no, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("gcIds", gcIds);
        params.put("sort", sort);
        params.put("page_no", page_no);
        request(WAction.CATEGORY_ACTIVITY_SEARCH_DATA, params, tag);
    }


    /**
     * 商品详情接口
     */
    @Override
    public void getProductDetailData(String skuId, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("skuId", skuId);
        request(WAction.PRODUCT_DETAIL_ACTIVITY_DATA, params, tag);
    }

    /**
     * 评价列表接口(商品详情)
     */
    @Override
    public void evaluateListData(String skuId, String pageNo,String pageSize, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("skuId", skuId);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        request(WAction.EVALUATE_LIST_DATA, params, tag);
    }

    /**
     * 评价商品列表接口
     */
    @Override
    public void getProductEvaluateListData(String goodsId, String pageNo, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("goodsId", goodsId);
        params.put("pageNo", pageNo);
        request(WAction.PRODUCT_EVALUATE_LIST_DATA, params, tag);
    }


    /**
     * 商品评价各项指标接口
     */
    @Override
    public void getProductEvaluateScoreData(String goodsId, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("goodsId", goodsId);
        request(WAction.PRODUCT_EVALUATE_SCORE_DATA, params, tag);
    }


    /**
     * 商品详情 热门推荐商品接口（商品详情页）
     */
    @Override
    public void getProductDetailHotData(String product_id, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("product_id", product_id);
        request(WAction.PRODUCT_DETAIL_HOT_DATA, params, tag);
    }


    /**
     * 收藏商品（商品详情页）
     */
    @Override
    public void collecteGoods(String skuId, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("skuId", skuId);
        request(WAction.GOODS_COLLECTION, params, tag);
    }

    /**
     * 收藏商品（商品详情页）
     */
    @Override
    public void collecteProduct(String product_id, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("product_id", product_id);
        request(WAction.COLLECTE_PRODUCT, params, tag);
    }

    /**
     * 取消收藏商品（商品详情页）
     */
    @Override
    public void cancelCollecteProduct(String product_id, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("product_id", product_id);
        request(WAction.CANCEL_COLLECTE_PRODUCT, params, tag);
    }

    /**
     * 获取收藏夹商品列表
     */
    @Override
    public void getCollectionList(String page_no, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page_no", page_no);
        request(WAction.GET_COLLECTION_DATA, params, tag);
    }

    /**
     * 商品评价接口
     */
    @Override
    public void setEvaluate(String params, String tag) {
        HashMap<String, String> p = new HashMap<>();
        p.put("params", params);
        request(WAction.SET_EVALUATE, p, tag);
    }


}
