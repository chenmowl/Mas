package com.eme.mas.controller;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.customeInterface.IUserInfo;

import java.util.HashMap;

/**
 * Created by zulei on 16/7/26.
 */
public class UserInfoImpl extends BaseImpl implements IUserInfo {

    public UserInfoImpl(IActionListener iActionListener) {
        super(iActionListener);
    }


    @Override
    public void getUserInfo(String tag) {
        request(WAction.GET_USER_INFO,null,tag);
    }

    /**
     *
     * @param phoneNumber
     * @param sceneTag   获取验证码的业务场景   1、登录 ==> login  2、设置密码  ==> setPWD 3、修改密码  ==> updateMobile 4、绑定新手机 ==> BindNewMobile 5、修改密码==>updatePWD
     * @param tag
     */
    @Override
    public void getVerCode(String phoneNumber,String sceneTag,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("tag", sceneTag);
        request(WAction.GET_VERCODE,params,tag);
    }

    /**
     * 验证验证码
     * @param phoneNumber
     * @param activationNumber
     * @param sceneTag
     * @param tag
     */
    @Override
    public void checkVerCode(String phoneNumber, String activationNumber, String sceneTag, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("activationNumber", activationNumber);
        params.put("tag", sceneTag);
        request(WAction.CHECK_VERCODE,params,tag);
    }

    @Override
    public void bindPhone(String phoneNumber,String newPhoneNumber,String activationNumber,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("newPhoneNumber", newPhoneNumber);
        params.put("activationNumber", activationNumber);
        request(WAction.BIND_PHONE,params,tag);
    }

    @Override
    public void setLoginPassword(String newPassword, String phoneNumber,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("newPassword", newPassword);
        params.put("phoneNumber", phoneNumber);
        request(WAction.SET_LOGIN_PASSWORD,params,tag);
    }

    @Override
    public void getCoupon(String tag) {
        request(WAction.GET_COUPON,null,tag);
    }

    @Override
    public void setUserInfo(@Nullable String nickname,@Nullable String sex,@Nullable String birthday,@Nullable String photostr, String tag) {
        HashMap<String, String> params=null;
        if(!TextUtils.isEmpty(nickname)){
            if(params == null){
                params=new HashMap<>();
            }
            params.put("nickname", nickname);
        }
        if(!TextUtils.isEmpty(sex)){
            if(params == null){
                params=new HashMap<>();
            }
            params.put("sex", sex);
        }
        if(!TextUtils.isEmpty(birthday)){
            if(params == null){
                params=new HashMap<>();
            }
            params.put("birthday", birthday);
        }
        if(!TextUtils.isEmpty(photostr)){
            if(params == null){
                params=new HashMap<>();
            }
            params.put("photostr", photostr);
        }
        request(WAction.SET_USER_INFO,params,tag);
    }

    @Override
    public void vercodeLogin(String phoneNumber,String activationNumber,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("activationNumber", activationNumber);
        request(WAction.VERIFY_LOGIN, params, tag);
    }

    //  密码登录
    @Override
    public void passwordLogin(String phoneNumber,String password,String tag){
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNumber);
        params.put("password", password);
        request(WAction.PASSWORD_LOGIN, params, tag);
    }
}
