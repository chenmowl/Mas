package com.eme.mas.data;

import android.text.TextUtils;

import com.eme.mas.data.sql.DataRow;
import com.eme.mas.data.sql.DataTable;
import com.eme.mas.data.sql.QueryBuilder;
import com.eme.mas.data.sql.Transaction;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.entity.CartBo;
import com.eme.mas.model.entity.CategorySpecVo;
import com.eme.mas.model.entity.ProductBo;
import com.eme.mas.model.entity.SpecBo;
import com.eme.mas.model.entity.SpecVo;
import com.eme.mas.model.entity.home.HomeVipProductBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dijiaoliang on 16/8/1.
 */
public class SqlUtil {

    /**
     * 初始化分类数据
     *
     * @param bean
     */
    public static void initCategoryData(List<CategorySpecVo> bean) {
        Transaction trans = new Transaction();
        String insertCategoryOne = "insert into wine_category(id,name) values(?,?)";
        String insertBrand = "insert into wine_brand(id,name,parentid) values(?,?,?)";
        String insertFlave = "insert into wine_flavor(id,name,parentid) values(?,?,?)";
        String insertPlace = "insert into wine_place(id,name,parentid) values(?,?,?)";
        String insertPrice = "insert into wine_price(id,name,parentid) values(?,?,?)";
        List<SpecVo> specVos;
        List<SpecBo> specBos;
        String parentID;
        for (CategorySpecVo categoryVo : bean) {
            trans.add(new QueryBuilder(insertCategoryOne, categoryVo.getId(), categoryVo.getName()));
            specVos = categoryVo.getList();
            parentID = categoryVo.getId();
            for (SpecVo specVo : specVos) {
                specBos = specVo.getList();
                switch (specVo.getName()) {
                    case "品牌":
                        for (SpecBo bo : specBos) {
                            trans.add(new QueryBuilder(insertBrand, bo.getId(), bo.getName(), parentID));
                        }
                        break;
                    case "香型":
                        for (SpecBo bo : specBos) {
                            trans.add(new QueryBuilder(insertFlave, bo.getId(), bo.getName(), parentID));
                        }
                        break;
                    case "产地":
                        for (SpecBo bo : specBos) {
                            trans.add(new QueryBuilder(insertPlace, bo.getId(), bo.getName(), parentID));
                        }
                        break;
                    case "价格":
                        for (SpecBo bo : specBos) {
                            trans.add(new QueryBuilder(insertPrice, bo.getId(), bo.getName(), parentID));
                        }
                        break;
                }
            }
        }
        trans.commit();
    }


    /**
     * 清空分类数据
     */
    public static void clearCategoryData() {
        Transaction trans = new Transaction();
        String deleteCategoryOne = "delete from wine_category";
        String deleteBrand = "delete from wine_brand";
        String deleteFlave = "delete from wine_flavor";
        String deletePlace = "delete from wine_place";
        String deletePrice = "delete from wine_price";
        trans.add(new QueryBuilder(deleteCategoryOne));
        trans.add(new QueryBuilder(deleteBrand));
        trans.add(new QueryBuilder(deleteFlave));
        trans.add(new QueryBuilder(deletePlace));
        trans.add(new QueryBuilder(deletePrice));
        trans.commit();
//        trans.commit(MasApplication.getInstance());
    }


    /**
     * 初始化购物车数据
     *
     * @param bean
     */
    public static void initCartData(List<CartBo> bean) {

//        private final static String DB_CREATE_TABLE_SHOPPING_CART = "create table if not exists shopping_cart(id int primary key,cart_id varchar(100),product_id varchar(100),spec_id varchar(100),product_name varchar(100),product_price varchar(50),integral varchar(50),product_num varchar(50),image_url varchar(100),product_channel int,isShow int,isInvalid int)";
        Transaction trans = new Transaction();
        String insertCart = "insert into shopping_cart(cart_id,product_id ,spec_id,product_name,product_price ,integral ,product_num ,image_url ,product_channel ,isSelected ,goods_show) values(?,?,?,?,?,?,?,?,?,?,?)";
//        QueryBuilder builder = new QueryBuilder(context, insertCart, "", "", "", "", "", "", "", "", "", "", "");
        for (CartBo cartBo : bean) {
            trans.add(new QueryBuilder(insertCart, cartBo.getCart_id(), cartBo.getProduct_id(), cartBo.getSpec_id(), cartBo.getProduct_name(), cartBo.getProduct_price(), cartBo.getIntegral(),
                    cartBo.getProduct_num(), cartBo.getProduct_image_url(), cartBo.getProduct_channel(), cartBo.getIsSelected(), cartBo.getGoods_show()));
        }
        trans.commit();
    }

    /**
     * 添加一条购物车数据
     */
    public static void addCartData(String cartId, HomeVipProductBo bo) {

        String query = "select * from shopping_cart where cart_id=?";
        DataTable queryData = new QueryBuilder(query, cartId).executeDataTable();
        if (queryData.getRowCount() != 0) {
            //更新购物车
            int proNum = Integer.parseInt((String) queryData.get(0).get("product_num"));
            proNum++;
            String update = "update shopping_cart set product_num=? where cart_id=?";
            new QueryBuilder(update, String.valueOf(proNum), cartId).executeNoQuery();
        } else {
            //插入购物车
            ProductBo proBo = bo.getProductDetail();
            String insertCart = "insert into shopping_cart(cart_id,product_id ,spec_id,product_name,product_price ,integral ,product_num ,image_url ,product_channel ,isSelected ,goods_show) values(?,?,?,?,?,?,?,?,?,?,?)";
            QueryBuilder builder = new QueryBuilder(insertCart, "", "", "", "", "", "", "", "", "", "", "");
            builder.set(0, cartId);
            builder.set(1, proBo.getProduct_id());
            builder.set(2, proBo.getSpec_id());
            builder.set(3, proBo.getProduct_name());
            builder.set(4, bo.getPhone_price());
            builder.set(5, proBo.getIntegral());
            builder.set(6, "1");
            builder.set(7, proBo.getProduct_image_url());
            builder.set(8, proBo.getProduct_channel());
            builder.set(9, "0");
            builder.set(10, proBo.getGoods_show());
            builder.executeNoQuery();
        }

    }

    /**
     * 添加一条购物车数据
     */
    public static void addCartData(String cartId, ProductBo bo) {

        String query = "select * from shopping_cart where cart_id=?";
        DataTable queryData = new QueryBuilder(query, cartId).executeDataTable();
        if (queryData.getRowCount() != 0) {
            //更新购物车
            int proNum = Integer.parseInt((String) queryData.get(0).get("product_num"));
            proNum++;
            String update = "update shopping_cart set product_num=? where cart_id=?";
            new QueryBuilder(update, String.valueOf(proNum), cartId).executeNoQuery();
        } else {
            //插入购物车
            String insertCart = "insert into shopping_cart(cart_id,product_id ,spec_id,product_name,product_price ,integral ,product_num ,image_url ,product_channel ,isSelected ,goods_show) values(?,?,?,?,?,?,?,?,?,?,?)";
            QueryBuilder builder = new QueryBuilder(insertCart, "", "", "", "", "", "", "", "", "", "", "");
            builder.set(0, cartId);
            builder.set(1, bo.getProduct_id());
            builder.set(2, bo.getSpec_id());
            builder.set(3, bo.getProduct_name());
            builder.set(4, bo.getProduct_price());
            builder.set(5, bo.getIntegral());
            builder.set(6, "1");
            builder.set(7, bo.getProduct_image_url());
            builder.set(8, bo.getProduct_channel());
            builder.set(9, "0");
            builder.set(10, bo.getGoods_show());
            builder.executeNoQuery();
        }

    }

    /**
     * 获取数据库中购物车数据
     */
    public static List<DataRow> getCartData() {
        List<DataRow> mDrList = new ArrayList<>();
        String sql7 = "select * from shopping_cart";
        DataTable dt = new QueryBuilder(sql7).executeDataTable();
        int count = dt.getRowCount();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                mDrList.add(dt.get(i));
            }
        }
        return mDrList;
    }

    /**
     * 清空购物车数据
     */
    public static void clearCartData() {
        Transaction trans = new Transaction();
        String sql7 = "delete from shopping_cart";
        new QueryBuilder(sql7).executeNoQuery();
        trans.commit();
    }


    /**
     * 获取购物车数量
     *
     * @return
     */
    public static int getCartCount() {
        List<DataRow> tempDrList = SqlUtil.getCartData();
        int count = 0;
        for (DataRow row : tempDrList) {
            count = count + Integer.parseInt((String) row.get("product_num"));
        }
        return count;
    }

    /**
     * 删除指定cartId的数据
     * @param cartIds
     * @return
     */
    public static void deleteCartData(String cartIds) {
        if(TextUtils.isEmpty(cartIds)){
            return;
        }
        Transaction trans = new Transaction();
        String sql = "delete from shopping_cart where cart_id=?";
        String[] arr=cartIds.split(WValue.COMMA);
        int j=arr.length;
        for(int i=0;i<j;i++){
            trans.add(new QueryBuilder(sql,arr[i]));
        }
        trans.commit();
    }


    /**
     * 足迹里添加一条商品数据
     *
     * @return
     */
    public static void addToFootmark(ProductBo bo) {
        String query = "select * from footmark where product_id=?";
        DataTable queryData = new QueryBuilder(query, bo.getProduct_id()).executeDataTable();
        if (queryData.getRowCount() != 0) {
            //更新足迹
            String update = "update footmark set product_name=?,product_price=?,product_image_url=? where product_id=?";
            new QueryBuilder(update, bo.getProduct_name(), bo.getProduct_price(), bo.getProduct_image_url(), bo.getProduct_id()).executeNoQuery();
        } else {
            //插入足迹
            String insertFoot = "insert into footmark(product_id,product_name,product_price,product_image_url) values(?,?,?,?)";
            QueryBuilder builder = new QueryBuilder(insertFoot, "", "", "", "");
            builder.set(0, bo.getProduct_id());
            builder.set(1, bo.getProduct_name());
            builder.set(2, bo.getProduct_price());
            builder.set(3, bo.getProduct_image_url());
            builder.executeNoQuery();
        }
    }

    /**
     * 获取数据库中足迹数据
     */
    public static List<DataRow> getFootmarkData() {
        List<DataRow> mDrList = new ArrayList<>();
        String sql7 = "select * from footmark";
        DataTable dt = new QueryBuilder(sql7).executeDataTable();
        int count = dt.getRowCount();
        if (count != 0) {
            for (int i = 0; i < count; i++) {
                mDrList.add(dt.get(i));
            }
        }
        return mDrList;
    }



}
