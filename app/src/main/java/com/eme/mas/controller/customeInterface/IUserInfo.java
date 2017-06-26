package com.eme.mas.controller.customeInterface;

/**
 * Created by zulei on 16/7/26.
 */
public interface IUserInfo {

    /**
     * 获取用户信息
     */
    void  getUserInfo(String tag);

    /**
     * 提交用户信息
     */
    void  setUserInfo(String nickname,String sex,String birthday,String photostr,String tag);

    /**
     * 获取验证码
     */
    void  getVerCode(String phoneNumber,String sceneTag,String tag);

    /**
     * 验证验证码
     */
    void  checkVerCode(String phoneNumber,String activationNumber,String sceneTag,String tag);

    /**
     * 绑定手机
     */
    void  bindPhone(String phoneNumber,String newPhoneNumber,String activationNumber,String tag);

    /**
     * 个人中心设置登录密码
     */
    void  setLoginPassword(String newPassword,String phoneNumber,String tag);

    /**
     * 获取购物车优惠券
     */
    void  getCoupon(String tag);

    /**
     * 验证码登录
     */
    void vercodeLogin(String phoneNumber,String activationNumber,String tag);

    /**
     * 密码登录
     */
    void passwordLogin(String phoneNum,String password,String tag);

}
