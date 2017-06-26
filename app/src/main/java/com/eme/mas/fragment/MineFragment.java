package com.eme.mas.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.activity.AssistCenterActiviy;
import com.eme.mas.activity.CollectionActivity;
import com.eme.mas.activity.CouponActivity;
import com.eme.mas.activity.MessageCenterActivity;
import com.eme.mas.activity.MyAddressActivity;
import com.eme.mas.activity.PayOnlineActivity;
import com.eme.mas.activity.PersonalInfoActivity;
import com.eme.mas.activity.SettingActivity;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.customeview.CircleImageView;
import com.eme.mas.customeview.ObservableScrollView;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.environment.KConfig;
import com.eme.mas.model.Result;
import com.eme.mas.model.UserInfoResult;
import com.eme.mas.model.entity.UserInfo;
import com.eme.mas.model.entity.UserInfoBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人模块
 * Created by zulei on 16/7/25.
 */
@WLayout(layoutId = R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    private final static String TAG = MineFragment.class.getSimpleName();

    @Bind(R.id.tv_mine_loginorreg)
    TextView tvMineLoginorreg;
    @Bind(R.id.iv_mine_headbg)
    ImageView ivMineHeadbg;
    @Bind(R.id.osv_mine)
    ObservableScrollView osvMine;
    @Bind(R.id.rl_mine_headbg)
    RelativeLayout rlMineHeadbg;
    @Bind(R.id.siv_mine_headphoto)
    CircleImageView sivMineHeadphoto;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        ((BaseImpl) iUserInfo).cancelRequestByTag(TAG);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iUserInfo=mController.getUserInfo(this);
        initListener();
        initData();
    }

    private void initListener() {
        osvMine.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

                int height = ivMineHeadbg.getHeight();
                int hh = rlMineHeadbg.getHeight();

                if (y <= height * 4 && y >= 0) {
                    float scale = (float) y / height / 4;
                    float alpha = (255 * scale);
                    ivMineHeadbg.setBackgroundColor(Color.argb((int) alpha, 0xe6, 0x17, 0x17));
                } else if (y > height) {
                    ivMineHeadbg.getBackground().setAlpha(255);
                }
                if (y < hh - height) {
                    ivMineHeadbg.setVisibility(View.GONE);
                } else {
                    ivMineHeadbg.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void initData() {
        if (isLogin()) {
            if(!TextUtils.isEmpty(UserInfo.getInstance().getUseId())){
                //用户已经登陆的话需要更新用户信息
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    isHideLayer(llAvLoadingTransparent44,false);
                    iUserInfo.getUserInfo(TAG);
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
                }
            }else{
                SPBase.setSPBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME,SpConstant.LOGIN_KEY,false);
                tvMineLoginorreg.setText(R.string.mine_loginorreg);
                sivMineHeadphoto.setImageResource(R.mipmap.common_tab_my_n);
            }
        } else {
            tvMineLoginorreg.setText(R.string.mine_loginorreg);
            sivMineHeadphoto.setImageResource(R.mipmap.common_tab_my_n);
        }
    }

    private void updateView() {
        String nickName = UserInfo.getInstance().getNickname();
        String phone = UserInfo.getInstance().getPhone();
        String imageUrl = UserInfo.getInstance().getImageUrl();
        if (TextUtils.isEmpty(nickName)) {
            tvMineLoginorreg.setText(phone);
        } else {
            tvMineLoginorreg.setText(nickName);
        }
        Glide.with(this).load(TextUtils.concat(KConfig.HOST_URL,imageUrl))
                .error(R.mipmap.common_tab_my_n) //在图像加载失败时显示
                .into(sivMineHeadphoto);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isLogin()) {
            updateView();
        } else {
            tvMineLoginorreg.setText(R.string.mine_loginorreg);
            sivMineHeadphoto.setImageResource(R.mipmap.common_tab_my_n);
        }
    }

    @OnClick({R.id.ib_mine_setting, R.id.tv_mine_loginorreg, R.id.siv_mine_headphoto,R.id.rl_mine_czk, R.id.rl_mine_yhq, R.id.rl_mine_address,
            R.id.rl_mine_collection, R.id.rl_mine_message, R.id.rl_mine_help, R.id.rl_mine_about,R.id.rl_mine_gift})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_mine_setting:
                if (verifyLogin()) {
                    Intent intentSetting = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intentSetting);
                }
                break;
            case R.id.siv_mine_headphoto:
                if (verifyLogin()) {
                    Intent intentPInfo = new Intent(getActivity(), PersonalInfoActivity.class);
                    startActivity(intentPInfo);
                }
                break;
            case R.id.rl_mine_czk:
                if (verifyLogin()) {
                    Intent intentSear = new Intent(getActivity(), PayOnlineActivity.class);
                    startActivity(intentSear);
                    //ToastUtil.shortToast(getActivity(), "储值卡");
                }
                break;
            case R.id.rl_mine_yhq:
                if (verifyLogin()) {
                    Intent intent = new Intent(getActivity(), CouponActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_mine_address:
                if (verifyLogin()) {
                    Intent intentMyAddress = new Intent(getActivity(), MyAddressActivity.class);
                    startActivity(intentMyAddress);
                }
                break;
            case R.id.rl_mine_collection:
                if (verifyLogin()) {
                    startActivity(new Intent(getActivity(), CollectionActivity.class));
                }
                break;
            case R.id.rl_mine_message:
                if (verifyLogin()) {
                    startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                }
                break;
            case R.id.rl_mine_help:
                startActivity(new Intent(getActivity(), AssistCenterActiviy.class));
                break;
            case R.id.rl_mine_about:
                ToastUtil.shortToast(getActivity(), "关于");


//                String aaa = "replace into wine_category(id,name) values(?,?)";
//                new QueryBuilder(aaa, "1", "1").executeNoQuery();
//
//                String c1 = "select * from wine_category";
//                DataTable dt999 = new QueryBuilder(c1).executeDataTable();
//                int ccccc = dt999.getRowCount();
//
//
//                String ssss = "replace into wine_category(id,name) values(?,?)";
//                Transaction trans2 = new Transaction();
//                for (int i = 0; i < 10; i++) {
//                    QueryBuilder queryBuilder = new QueryBuilder(ssss, "", "");
//                    queryBuilder.set(0, "abc" + i);
//                    queryBuilder.set(1, "aabc" + i);
//                    trans2.add(queryBuilder);
//                }
//                trans2.commit();
//
//
//                SQLiteDatabase d1 = new SQLiteHelper(getActivity()).getWritableDatabase();
//                SQLiteDatabase d2 = new SQLiteHelper(MasApplication.getInstance()).getWritableDatabase();
//
//
//                String sql7 = "select * from wine_category";
//                DataTable dt7 = new QueryBuilder(sql7).executeDataTable();
//                int aa = dt7.getRowCount();
//
//
//                Transaction trans3 = new Transaction();
//                for (int i = 0; i < 10; i++) {
//                    QueryBuilder queryBuilder = new QueryBuilder(ssss, "", "");
//                    queryBuilder.set(0, "abcaaa" + i);
//                    queryBuilder.set(1, "aabaaac" + i);
//                    trans3.add(queryBuilder);
//                }
//                trans3.commit();

//                String sql8 = "select * from shopping_cart";
//                DataTable dt8 = new QueryBuilder(sql8).executeDataTable();
//                int bb = dt8.getRowCount();
//                String abb = "1";


                break;
            case R.id.rl_mine_gift:
                if (verifyLogin()) {
                    ToastUtil.shortToast(getActivity(), "邀请好友");
                }

                break;
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        //判断code定位错误信息
//        String message = result.getMsg();
//        ToastUtil.shortToast(getActivity(), message);
        SPBase.setSPBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME,SpConstant.LOGIN_KEY,false);
        tvMineLoginorreg.setText(R.string.mine_loginorreg);
        sivMineHeadphoto.setImageResource(R.mipmap.common_tab_my_n);
        isHideLayer(llAvLoadingTransparent44,true);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch (result.getAction()){
            case WAction.GET_USER_INFO:
                UserInfoResult userInfoResult = (UserInfoResult) result;
                UserInfoBo userInfoBo = userInfoResult.getData();

                UserInfo.getInstance().setImageUrl(userInfoBo.getImageurl());
                UserInfo.getInstance().setNickname(userInfoBo.getNickname());
                UserInfo.getInstance().setGender(userInfoBo.getSex());
                UserInfo.getInstance().setBirthday(userInfoBo.getBirthday());
                UserInfo.getInstance().setPhone(userInfoBo.getMobile());
                UserInfo.getInstance().setUseId(userInfoBo.getUser_id());
                UserInfo.getInstance().Save();

                updateView();
                break;
        }
        isHideLayer(llAvLoadingTransparent44,true);
    }
}
