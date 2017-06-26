package com.eme.mas.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.customeview.CircleImageView;
import com.eme.mas.customeview.dialog.ChangeHeadPhotoDialog;
import com.eme.mas.customeview.dialog.ChangeSexDialog;
import com.eme.mas.customeview.dialog.SetBirthdayDialog;
import com.eme.mas.environment.KConfig;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.Result;
import com.eme.mas.model.SetUserinfoResult;
import com.eme.mas.model.entity.UserInfo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.TimeUtils;
import com.eme.mas.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人资料页面
 * Created by zulei on 16/7/25.
 */
@WLayout(layoutId = R.layout.activity_personalinfo)
public class PersonalInfoActivity extends BaseActivity {

    private final static String TAG = PersonalInfoActivity.class.getSimpleName();

    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.tv_personalinfo_nick)
    TextView tvPersonalinfoNick;
    @Bind(R.id.tv_personalinfo_sex)
    TextView tvPersonalinfoSex;
    @Bind(R.id.tv_personalinfo_birth)
    TextView tvPersonalinfoBirth;
    @Bind(R.id.tv_personalinfo_phone)
    TextView tvPersonalinfoPhone;
    @Bind(R.id.siv_personalinfo_headphoto)
    CircleImageView sivPersonalinfoHeadphoto;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;
    private Bitmap bitmap;
    private String birthDay, sex, imageUrl;
    private int updateObj;  //updateObj指代更新项

    private static final int TO_CHANGENICK = 1002;
    private static final int CAMERA = 2001;
    private static final int PHOTO = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) iUserInfo).cancelRequestByTag(TAG);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        tvBarTitle.setText(R.string.personalinfo_title);
        arrowBarTitle.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);
        showUserInfoFromLocal();
//        long s=664560000000l;
//        ToastUtil.shortToast(this,TimeUtils.milliseconds2String(s,new SimpleDateFormat(WValue.FORMAT_DATE_ONE)));
    }

    /**
     * 从本地取用户的信息。
     */
    private void showUserInfoFromLocal() {
        tvSetContent(tvPersonalinfoNick, UserInfo.getInstance().getNickname());

        birthDay = UserInfo.getInstance().getBirthday();
        if (!TextUtils.isEmpty(birthDay) && !TextUtils.equals(WValue.NULL_LOWER_CASE,birthDay)&& !TextUtils.equals(WValue.NULL_UPPER_CASE,birthDay)) {
            birthDay=TimeUtils.milliseconds2String(Long.parseLong(birthDay),new SimpleDateFormat(WValue.FORMAT_DATE_ONE,Locale.getDefault()));
        }
        tvSetContent(tvPersonalinfoBirth, birthDay);

        sex = UserInfo.getInstance().getGender();
        if (TextUtils.isEmpty(sex)) {
            sex = WValue.SEX_SECRET;
        }
        if (WValue.SEX_FEMALE.equals(sex)) {
            tvSetContent(tvPersonalinfoSex, getString(R.string.female));
        } else if (WValue.SEX_MALE.equals(sex)) {
            tvSetContent(tvPersonalinfoSex, getString(R.string.male));
        } else if (WValue.SEX_SECRET.equals(sex)) {
            tvSetContent(tvPersonalinfoSex, getString(R.string.secret));
        }

        imageUrl = UserInfo.getInstance().getImageUrl();
        Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,imageUrl))
                    .error(R.mipmap.common_tab_my_n) //在图像加载失败时显示
                    .into(sivPersonalinfoHeadphoto);

        tvSetContent(tvPersonalinfoPhone, UserInfo.getInstance().getPhone());

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.rl_personalinfo_photo, R.id.rl_personalinfo_nick, R.id.rl_personalinfo_sex,
            R.id.rl_personalinfo_brith, R.id.rl_personalinfo_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_personalinfo_photo:
                final ChangeHeadPhotoDialog mDialog = new ChangeHeadPhotoDialog();
                mDialog.setOnHeadChangeListener(new ChangeHeadPhotoDialog.OnHeadChangeListener() {
                    @Override
                    public void takePic() {
                        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(camera, CAMERA);
                        mDialog.cancel();
                    }

                    @Override
                    public void takeFromAlbum() {
                        try {
                            Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(picture, PHOTO);

//                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                            intent.setType("image/*");
//                            Intent wrapperIntent=Intent.createChooser(intent, null);
//                            startActivityForResult(wrapperIntent, PHOTO);

                        } catch (Exception e) {
                            ToastUtil.shortToast(PersonalInfoActivity.this, R.string.camera_error);
                        }
                        mDialog.cancel();
                    }
                });

                mDialog.showDialog(this, null);
                break;
            case R.id.rl_personalinfo_nick:
                Intent intent = new Intent(this, PersonalInfoChangeNickActivity.class);
                startActivityForResult(intent, TO_CHANGENICK);
                break;
            case R.id.rl_personalinfo_sex:
                final ChangeSexDialog csDialog = new ChangeSexDialog();
                csDialog.setOnSexChangeListener(new ChangeSexDialog.OnSexChangeListener() {

                    @Override
                    public void changeMale() {
                        sex = WValue.SEX_MALE;
                        requestUpdate(WValue.UPDATE_OBJ_SEX);
                        csDialog.cancel();
                    }

                    @Override
                    public void changeFemale() {
                        sex = WValue.SEX_FEMALE;
                        requestUpdate(WValue.UPDATE_OBJ_SEX);
                        csDialog.cancel();

                    }
                });
                csDialog.showDialog(this, null);
                break;
            case R.id.rl_personalinfo_brith:

                final SetBirthdayDialog sbDialog;
                if( TextUtils.isEmpty(birthDay) || TextUtils.equals(WValue.NULL_LOWER_CASE,birthDay) || TextUtils.equals(WValue.NULL_UPPER_CASE,birthDay)) {
                    sbDialog = new SetBirthdayDialog(this);
                } else {
                    sbDialog = new SetBirthdayDialog(this, birthDay.substring(0, 4), birthDay.substring(5, 7),
                            birthDay.substring(8, 10));
                }
                sbDialog.setOnBirthChangeListener(new SetBirthdayDialog.OnBirthChangeListener() {
                    @Override
                    public void changeBirthday(String birthday) {
                        birthDay = birthday;
                        requestUpdate(WValue.UPDATE_OBJ_BIRTHDAY);
                        sbDialog.cancel();
                    }
                });
                sbDialog.showDialog(this, null);

                break;
            case R.id.rl_personalinfo_phone:
                Intent intentBind=new Intent(PersonalInfoActivity.this, BindPhoneActivity.class);
                intentBind.putExtra("flag", WValue.VERIFY_MOBILE);
                startActivity(intentBind);
                break;
        }
    }

    /**
     * 更新单个项的请求方法
     * @param flag
     */
    private void requestUpdate(int flag){
        switch(flag){
            case WValue.UPDATE_OBJ_SEX:
                if (NetworkStatusUtil.isNetworkConnected(PersonalInfoActivity.this)) {
                    isHideLayer(llAvLoadingTransparent44,false);
                    updateObj=WValue.UPDATE_OBJ_SEX;//标示当前更新的对象
                    iUserInfo.setUserInfo(null,sex,null,null,TAG);
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
                }
                break;
            case WValue.UPDATE_OBJ_BIRTHDAY:
                if (NetworkStatusUtil.isNetworkConnected(PersonalInfoActivity.this)) {
                    isHideLayer(llAvLoadingTransparent44,false);
                    updateObj=WValue.UPDATE_OBJ_BIRTHDAY;//标示当前更新的对象
                    iUserInfo.setUserInfo(null,null,TimeUtils.milliseconds2String(TimeUtils.string2Milliseconds(birthDay,new SimpleDateFormat(WValue.FORMAT_DATE_ONE, Locale.getDefault())),new SimpleDateFormat(WValue.FORMAT_DATE_TWO, Locale.getDefault())),null,TAG);
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
                }
                break;
        }
    }


    /**
     * 对获取到的图像资源进行了压缩处理
     * @param b
     */
    private void getBitmap(Bitmap b) {
        int width = b.getWidth();
        int height = b.getHeight();
        final float scale = getResources().getDisplayMetrics().density;
        float sx = (40 * scale + 0.5f) / width;
        float sy = (40 * scale + 0.5f) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        bitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
    }

    private void tvSetContent(TextView textView, String string) {
        if (!TextUtils.isEmpty(string) && !TextUtils.equals(WValue.NULL_LOWER_CASE,string)&& !TextUtils.equals(WValue.NULL_UPPER_CASE,string)) {
            textView.setText(string);
            textView.setTextColor(ContextCompat.getColor(this, R.color.status_text_gray));
        } else {
            textView.setText(R.string.notset);
            textView.setTextColor(ContextCompat.getColor(this, R.color.main_color_blue));
        }

    }

    /**
     * 提交用户头像信息
     */
    private void commitHeadPhoto() {
        if (NetworkStatusUtil.isNetworkConnected(PersonalInfoActivity.this)) {
            String mImage = "";
            if (null != bitmap) { //把用户图像回传给我的界面。
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] bytes = bStream.toByteArray();
                mImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            }
            if(!TextUtils.isEmpty(mImage)){
                isHideLayer(llAvLoadingTransparent44,false);
                updateObj=WValue.UPDATE_OBJ_HEADER_PHOTO;//标示当前更新的对象
                iUserInfo.setUserInfo(null, null, null, mImage, TAG);
            }else{
                ToastUtil.shortToast(PersonalInfoActivity.this, R.string.get_photo_error);
            }
        } else {
            ToastUtil.shortToast(PersonalInfoActivity.this, R.string.net_error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == CAMERA && data.getExtras() != null) {
                Bitmap b = data.getParcelableExtra("data");
                getBitmap(b);
                commitHeadPhoto();//上传头像
            } else if (requestCode == PHOTO) {
                //调用系统相册返回的图像结果
                Uri uri = data.getData();
                Bitmap b = null;
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    b = BitmapFactory.decodeFile(picturePath);
                    cursor.close();
                    getBitmap(b);
                    commitHeadPhoto();//上传头像
                } catch (Exception e) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (null != cursor) {
                        if (null != cursor.getString(1)) {
                            String imgPath = cursor.getString(1); // 图片文件路径
                            b = BitmapFactory.decodeFile(imgPath);//缺点：在某些手机上会找不到路径
                        }
                        cursor.close();
                        getBitmap(b);
                        commitHeadPhoto();//上传头像
                    }
                }
            } else if (requestCode == TO_CHANGENICK) {
                tvSetContent(tvPersonalinfoNick, data.getStringExtra("nickName"));
            }
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        String message = result.getMsg();
        if(TextUtils.isEmpty(message)){
            message=getText(R.string.toast_update_failue).toString();
        }
        ToastUtil.shortToast(this, message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch(updateObj){
            case WValue.UPDATE_OBJ_SEX:
                UserInfo.getInstance().setGender(sex);
                if ("0".equals(sex)) {
                    tvSetContent(tvPersonalinfoSex, getString(R.string.female));
                } else if ("1".equals(sex)) {
                    tvSetContent(tvPersonalinfoSex, getString(R.string.male));
                } else if ("2".equals(sex)) {
                    tvSetContent(tvPersonalinfoSex, getString(R.string.secret));
                }
                tvPersonalinfoSex.setTextColor(ContextCompat.getColor(PersonalInfoActivity.this,R.color.status_text_gray));
                break;
            case WValue.UPDATE_OBJ_BIRTHDAY:
                UserInfo.getInstance().setBirthday(String.valueOf(TimeUtils.string2Milliseconds(birthDay,new SimpleDateFormat(WValue.FORMAT_DATE_ONE, Locale.getDefault()))));
                tvSetContent(tvPersonalinfoBirth, birthDay);
                break;
            case WValue.UPDATE_OBJ_HEADER_PHOTO:
                imageUrl= ((SetUserinfoResult) result).getData().getUrl();
                UserInfo.getInstance().setImageUrl(imageUrl);
                UserInfo.getInstance().Save();
                Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,imageUrl))
                        .error(R.mipmap.common_tab_my_n) //在图像加载失败时显示
                        .into(sivPersonalinfoHeadphoto);
                break;
        }
        isHideLayer(llAvLoadingTransparent44,true);
        ToastUtil.shortToast(this, R.string.toast_update_success);
    }
}
