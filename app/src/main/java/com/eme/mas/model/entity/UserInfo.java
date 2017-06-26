package com.eme.mas.model.entity;


import com.eme.mas.MasApplication;
import com.eme.mas.utils.file.FileUtil;
import com.eme.mas.utils.file.Path;


public class UserInfo {
    private String nickname ;
    private String image ;
    private String imageUrl;
    private String gender ;
    private String birthday;
    private String email;
    private String memberRank ;
    private String deposit ;
    private String useId ;
    private String phone;

    private String loginPassword;
    private String payPassword;


    public static UserInfo instance;
    public static final String IMAGE_PATH= MasApplication.getInstance().getPath();
    public static final String USER_ID_PATH= Path.appendedString(MasApplication.getInstance().getPath(),"userId");
    public static final String IMAGE_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"image");
    public static final String IMAGE_URL_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"imageUrl");
    public static final String GENDER_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"gender");
    public static final String BIRTHDAY_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"birthday");
    public static final String NICK_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"nickname");
    public static final String EMAIL_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"email");
    public static final String MEMBER_RANK_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"memberRank");
    public static final String DEPOSIT_FILE=Path.appendedString(MasApplication.getInstance().getPath(),"userId");
    public static final String USER_ID_PHONE = Path.appendedString(MasApplication.getInstance().getPath(),"phone");

    public static final String USER_LOGIN_PASSWORD = Path.appendedString(MasApplication.getInstance().getPath(),"loginPassword");;
    public static final String USER_PAY_PASSWORD = Path.appendedString(MasApplication.getInstance().getPath(),"payPassword");;
    public UserInfo() {}





//    public String GetImgFile() {
//        return IMAGE_FILE;
//    }



    public void Save() {
        if (image != null && !image.equals("")) {
            FileUtil.writeUTF8String(IMAGE_FILE, image);
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            FileUtil.writeUTF8String(IMAGE_URL_FILE, imageUrl);
        }
        FileUtil.writeUTF8String(GENDER_FILE, gender);
        FileUtil.writeUTF8String(BIRTHDAY_FILE, birthday);
        FileUtil.writeUTF8String(NICK_FILE, nickname);
        FileUtil.writeUTF8String(EMAIL_FILE, email);
        FileUtil.writeUTF8String(MEMBER_RANK_FILE, memberRank);
        FileUtil.writeUTF8String(DEPOSIT_FILE, deposit);
        FileUtil.writeUTF8String(USER_ID_PATH,useId);
        FileUtil.writeUTF8String(USER_ID_PHONE,phone);

        FileUtil.writeUTF8String(USER_LOGIN_PASSWORD,loginPassword);
        FileUtil.writeUTF8String(USER_PAY_PASSWORD,payPassword);
    }

    public void Load() {
        image = FileUtil.readUTF8String(IMAGE_FILE);
        imageUrl = FileUtil.readUTF8String(IMAGE_URL_FILE);
        gender = FileUtil.readUTF8String(GENDER_FILE);
        birthday = FileUtil.readUTF8String(BIRTHDAY_FILE);
        nickname = FileUtil.readUTF8String(NICK_FILE);
        email =FileUtil.readUTF8String(EMAIL_FILE);
        memberRank =FileUtil.readUTF8String(MEMBER_RANK_FILE);
        useId =FileUtil.readUTF8String(USER_ID_PATH);
        deposit =FileUtil.readUTF8String(DEPOSIT_FILE);
        phone =FileUtil.readUTF8String(USER_ID_PHONE);

        loginPassword = FileUtil.readUTF8String(USER_LOGIN_PASSWORD);
        payPassword = FileUtil.readUTF8String(USER_PAY_PASSWORD);
    }

//    public void deletePhone(){
//        FileUtil.deleteFile(USER_ID_PHONE);
//    }

    public static UserInfo getInstance() {
        if (instance == null) {
            Class var0 = UserInfo.class;
            synchronized (UserInfo.class) {
                if (instance == null) {
                    instance = new UserInfo();
                }
            }
        }

        return instance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }


//    public void setValue(UserInfoDataResult userInfo) {
//        birthday =userInfo.getBirthday();
//        gender =userInfo.getSex();
//        email =userInfo.getEmail();
//        nickname =userInfo.getNickname();
//        useId =userInfo.getUser_id();
//        memberRank =userInfo.getMember_rank_id();
//        imageUrl =userInfo.getImageUrl();
//        deposit =userInfo.getDeposit();
//        phone=userInfo.getMobile();
//        if(gender.equals("1")){
//            gender="男";
//        }else if(gender.equals("0")){
//            gender="女";
//        }else if(gender.equals("2")){
//            gender="保密";
//        }
//        Save();//保存在本地
//    }
}
