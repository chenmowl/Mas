package com.eme.mas.model;

import com.eme.mas.model.entity.home.HomeBannerBo;
import com.eme.mas.model.entity.home.HomeBrandBo;
import com.eme.mas.model.entity.home.HomeCategoryBo;
import com.eme.mas.model.entity.home.HomeHotGoodsBo;
import com.eme.mas.model.entity.home.HomeMobileVipBo;

import java.util.List;

/**
 * HomeFragment实体类（HomeFragment页）
 * <p>
 * Created by dijiaoliang on 16/7/27.
 */
public class HomeResult extends Result {


    private HomeBean data;

    public HomeBean getData() {
        return data;
    }

    public void setData(HomeBean data) {
        this.data = data;
    }

    public static class HomeBean {

        private List<HomeBannerBo> bannerVo;//banner集合

        private List<HomeBannerBo> advertisementImg;//品牌汇banner集合

        private List<HomeBrandBo> brandCollection;//brand集合

        private List<HomeCategoryBo> goodsCategory;//分类信息集合

        private List<HomeHotGoodsBo> hotGoods;//商品推荐集合

        private List<String> hotWords;//热搜词集合

        private List<HomeMobileVipBo> mobileVip;//手机专享集合

        public List<HomeBannerBo> getBannerVo() {
            return bannerVo;
        }

        public void setBannerVo(List<HomeBannerBo> bannerVo) {
            this.bannerVo = bannerVo;
        }

        public List<HomeBannerBo> getAdvertisementImg() {
            return advertisementImg;
        }

        public void setAdvertisementImg(List<HomeBannerBo> advertisementImg) {
            this.advertisementImg = advertisementImg;
        }

        public List<HomeBrandBo> getBrandCollection() {
            return brandCollection;
        }

        public void setBrandCollection(List<HomeBrandBo> brandCollection) {
            this.brandCollection = brandCollection;
        }

        public List<HomeCategoryBo> getGoodsCategory() {
            return goodsCategory;
        }

        public void setGoodsCategory(List<HomeCategoryBo> goodsCategory) {
            this.goodsCategory = goodsCategory;
        }

        public List<HomeHotGoodsBo> getHotGoods() {
            return hotGoods;
        }

        public void setHotGoods(List<HomeHotGoodsBo> hotGoods) {
            this.hotGoods = hotGoods;
        }

        public List<String> getHotWords() {
            return hotWords;
        }

        public void setHotWords(List<String> hotWords) {
            this.hotWords = hotWords;
        }

        public List<HomeMobileVipBo> getMobileVip() {
            return mobileVip;
        }

        public void setMobileVip(List<HomeMobileVipBo> mobileVip) {
            this.mobileVip = mobileVip;
        }
    }
}
