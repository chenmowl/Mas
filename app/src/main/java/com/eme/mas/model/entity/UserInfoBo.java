package com.eme.mas.model.entity;

/**
 * Created by Nick on 16/8/4.
 */
public class UserInfoBo {

    /**
     * birthday : null
     * sex : 2
     * isSet : 1
     * nickname :
     * email :
     * deposit : null
     * member_rank_id : 铜牌会员
     * user_id : 1469755761457
     * imageurl : /upload/img/avatar/01.jpg
     * mobile : 18614029122
     */

    private String birthday;
    private String deposit;
    private String email;
    private String imageurl;
    private String is_set;
    private Object member_rank_id;
    private String mobile;
    private String nickname;
    private String sex;
    private String user_id;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getIs_set() {
        return is_set;
    }

    public void setIs_set(String is_set) {
        this.is_set = is_set;
    }

    public Object getMember_rank_id() {
        return member_rank_id;
    }

    public void setMember_rank_id(Object member_rank_id) {
        this.member_rank_id = member_rank_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
