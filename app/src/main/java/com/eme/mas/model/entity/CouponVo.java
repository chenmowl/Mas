package com.eme.mas.model.entity;

import java.util.List;

/**
 * 优惠券二级实体类
 *
 * Created by dijiaoliang on 16/7/27.
 */
public class CouponVo {

    private List<CouponBo> list;
    private CountMap countMap;

    public List<CouponBo> getList() {
        return list;
    }

    public void setList(List<CouponBo> list) {
        this.list = list;
    }

    public CountMap getCountMap() {
        return countMap;
    }

    public void setCountMap(CountMap countMap) {
        this.countMap = countMap;
    }

    public static class CountMap {
        private int aboutToExpire;
        private int isEnable;

        public int getAboutToExpire() {
            return aboutToExpire;
        }

        public void setAboutToExpire(int aboutToExpire) {
            this.aboutToExpire = aboutToExpire;
        }

        public int getIsEnable() {
            return isEnable;
        }

        public void setIsEnable(int isEnable) {
            this.isEnable = isEnable;
        }
    }


}
