package com.eme.mas.model;

import com.eme.mas.model.entity.UserInfoBo;

/**
 * 个人信息实体类
 * Created by zulei on 16/8/4.
 */
public class UserInfoResult extends Result {

    private UserInfoBo data;

    public UserInfoBo getData() {
        return data;
    }

    public void setData(UserInfoBo data) {
        this.data = data;
    }
}
