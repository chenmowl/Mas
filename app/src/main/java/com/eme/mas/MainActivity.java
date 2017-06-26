package com.eme.mas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.activity.LoginActivity;
import com.eme.mas.activity.OrderShareActivity;
import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.Controller;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.customeview.BadgeView;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.RefreshCartEvent;
import com.eme.mas.eventbus.SwitchMainFragmentEvent;
import com.eme.mas.fragment.BaseFragment;
import com.eme.mas.fragment.CartFragment;
import com.eme.mas.fragment.HomeFragment;
import com.eme.mas.fragment.MineFragment;
import com.eme.mas.fragment.OrderFragment;
import com.eme.mas.model.CartListResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.CartBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.eme.mas.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 应用首页面,包含底部tab和tab对应的碎片
 */
public class MainActivity extends FragmentActivity implements IActionListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Bind(R.id.frame_content)
    FrameLayout frameContent;
    @Bind(R.id.image_home)
    ImageView imageHome;
    @Bind(R.id.tv_home)
    TextView tvHome;
    @Bind(R.id.layout_home)
    RelativeLayout layoutHome;
    @Bind(R.id.image_cart)
    ImageView imageCart;
    @Bind(R.id.tv_cart)
    TextView tvCart;
    @Bind(R.id.layout_cart)
    RelativeLayout layoutCart;
    @Bind(R.id.image_order)
    ImageView imageOrder;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    @Bind(R.id.layout_order)
    RelativeLayout layoutOrder;
    @Bind(R.id.image_mine)
    ImageView imageMine;
    @Bind(R.id.tv_mine)
    TextView tvMine;
    @Bind(R.id.layout_mine)
    RelativeLayout layoutMine;
    @Bind(R.id.ll_cart)
    LinearLayout llCart;
    @Bind(R.id.rl_main)
    RelativeLayout rlMain;

    private ICart iCart;

    private BadgeView badgeView;
    /**
     * 当前选中的底部控件id
     */
    private int selectViewId;

    private BaseFragment fragHome, fragCart, fragOrder, fragMine, currentFrag;//这是首页的碎片页面
    private TextView currentTextView;
    private ImageView currentImageView;
    private int color_select, color_normal;

    private final static int TO_ORDER = 4001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_tabmain);
        ViewUtil.setStatusBarColor(this);
        ButterKnife.bind(this);
        afterSetContent();
        EventBus.getDefault().register(this);
    }

    private void afterSetContent() {
        iCart = new Controller().getIAccount(this);//初始化控制器
        color_select = getResources().getColor(R.color.main_color_red);
        color_normal = getResources().getColor(R.color.text_color_bar);

        fragHome = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_content, fragHome, "home");
        fragmentTransaction.commit();
        currentFrag = fragHome;

        /**初始化底部按钮的状态*/
        layoutHome.setSelected(true);
        imageHome.setSelected(true);
        layoutOrder.setSelected(false);
        imageOrder.setSelected(false);
        layoutCart.setSelected(false);
        imageCart.setSelected(false);
        layoutMine.setSelected(false);
        imageMine.setSelected(false);
        currentTextView = tvHome;
        currentImageView = imageHome;
        tvHome.setTextColor(color_select);
        selectViewId = R.id.layout_home;

        //初始化购物车标签控件
        badgeView = new BadgeView(this, llCart);
        badgeView.setTextSize(10);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        /**
         * 判断传来的标签
         */
        switchFragment(getIntent().getIntExtra(WValue.FLAG,WValue.MAIN_FRAGMENT_NONE));
        switch (getIntent().getIntExtra("dialog_share",WValue.ZERO)){
            case WValue.ONE:
                startActivity(new Intent(this, OrderShareActivity.class));
                break;
        }
    }


    /**
     * 刷新badgeview的数值
     *
     * @param event
     */
    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshCartEvent event) {
        int count = SqlUtil.getCartCount();
        if (0 == count) {
            if (badgeView.isShown()) badgeView.hide();
        } else {
            if (!badgeView.isShown()) badgeView.show();
        }
        badgeView.setText(String.valueOf(count));

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchEvent(SwitchMainFragmentEvent event) {
        int flag = event.getFlag();
        switchFragment(flag);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ((BaseImpl) iCart).cancelRequestByTag(TAG);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()) {
            if (MasApplication.getInstance().isInitCart()) {
                //TODO 获取数据库中的购物车数据
                int count = SqlUtil.getCartCount();
                badgeView.setText(String.valueOf(count));
                if (WValue.ZERO == count) {
                    if (badgeView.isShown()) badgeView.hide();
                } else {
                    if (!badgeView.isShown()) badgeView.show();
                }

            } else {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(MainActivity.this)) {
                    //获取购物车数据
                    iCart.getCartList(TAG);
                } else {
                    if (badgeView.isShown()) badgeView.hide();
                }
            }
        } else {
            badgeView.setText("0");
            if (badgeView.isShown()) badgeView.hide();
            MasApplication.getInstance().setIsInitCart(false);
        }
    }

    /**
     * @return
     */

    public BadgeView getBadgeView() {
        return badgeView;
    }

    public RelativeLayout getMainBg() {
        return rlMain;
    }


    private long mExitTime = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - mExitTime > 3000) {
                ToastUtil.shortToast(this, "再按一次退出程序");
                //退出程序时，清空上次选择的优惠券信息。
                SPBase.setContent(this, SpConstant.CHOICE_COUPON_FILE_NAME, SpConstant.COUPON_ID_KEY, "");
                SPBase.setContent(this, SpConstant.CHOICE_COUPON_FILE_NAME, SpConstant.COUPON_PRICE_KEY, "");
                mExitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @OnClick({R.id.layout_home, R.id.layout_order, R.id.layout_cart, R.id.layout_mine})
    void layoutClick(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.layout_home:
                switchFragment(WValue.MAIN_FRAGMENT_HOME);
                break;
            case R.id.layout_cart:
                switchFragment(WValue.MAIN_FRAGMENT_CART);
                break;
            case R.id.layout_order:
                if (verifyLogin(TO_ORDER)) {
                    switchFragment(WValue.MAIN_FRAGMENT_ORDER);
                }
                break;
            case R.id.layout_mine:
                switchFragment(WValue.MAIN_FRAGMENT_MINE);
                break;
        }
    }


    /**
     * 判断是否登录了
     *
     * @return true：用户已登录 false：用户未登录
     */
    public boolean isLogin() {
        return SPBase.getBoolean(this, SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY);
    }

    /**
     * 跳转到登录页面
     */
    public void login(int code) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, code);
    }

    /**
     * 如果用户没有登录显示用户登录页面
     */
    public boolean verifyLogin(int code) {
        if (!isLogin()) {
            login(code);
            return false;
        } else {
            return true;
        }
    }


    public void switchFragment(int flag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (flag){
            case WValue.MAIN_FRAGMENT_HOME:
                if (selectViewId != R.id.layout_home) {
                    if (fragHome == null) fragHome = new HomeFragment();
                    if (!fragHome.isAdded()) {
                        fragmentTransaction.hide(currentFrag).add(R.id.frame_content, fragHome).commit(); // 隐藏当前的
                    } else {
                        fragmentTransaction.hide(currentFrag).show(fragHome).commit(); // 隐藏当前的fragment，显示下一个
                    }
                    currentFrag = fragHome;
                    selectViewId = R.id.layout_home;
                    currentImageView.setSelected(false);
                    imageHome.setSelected(true);
                    currentTextView.setTextColor(color_normal);
                    tvHome.setTextColor(color_select);
                    currentTextView = tvHome;
                    currentImageView = imageHome;
                }

                break;
            case WValue.MAIN_FRAGMENT_CART:
                if (selectViewId != R.id.layout_cart) {
                    if (fragCart == null) fragCart = new CartFragment();
                    if (!fragCart.isAdded()) {
                        fragmentTransaction.hide(currentFrag).add(R.id.frame_content, fragCart).commit(); // 隐藏当前的
                    } else {
                        fragmentTransaction.hide(currentFrag).show(fragCart).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
                    }
                    currentFrag = fragCart;

                    selectViewId = R.id.layout_cart;
                    currentImageView.setSelected(false);
                    imageCart.setSelected(true);
                    currentTextView.setTextColor(color_normal);
                    tvCart.setTextColor(color_select);
                    currentTextView = tvCart;
                    currentImageView = imageCart;
                }

                break;
            case WValue.MAIN_FRAGMENT_ORDER:

                if (selectViewId != R.id.layout_order) {
                    if (fragOrder == null) fragOrder = new OrderFragment();
                    if (!fragOrder.isAdded()) {
                        fragmentTransaction.hide(currentFrag).add(R.id.frame_content, fragOrder).commit(); // 隐藏当前的
                    } else {
                        fragmentTransaction.hide(currentFrag).show(fragOrder).commit(); // 隐藏当前的fragment，显示下一个
                    }
                    currentFrag = fragOrder;

                    selectViewId = R.id.layout_order;
                    currentImageView.setSelected(false);
                    imageOrder.setSelected(true);
                    currentTextView.setTextColor(color_normal);
                    tvOrder.setTextColor(color_select);
                    currentTextView = tvOrder;
                    currentImageView = imageOrder;
                }

                break;
            case WValue.MAIN_FRAGMENT_MINE:
                if (selectViewId != R.id.layout_mine) {
                    if (fragMine == null) fragMine = new MineFragment();
                    if (!fragMine.isAdded()) {
                        fragmentTransaction.hide(currentFrag).add(R.id.frame_content, fragMine).commit(); // 隐藏当前的
                    } else {
                        fragmentTransaction.hide(currentFrag).show(fragMine).commit(); // 隐藏当前的fragment，显示下一个
                    }
                    currentFrag = fragMine;

                    selectViewId = R.id.layout_mine;
                    currentImageView.setSelected(false);
                    imageMine.setSelected(true);
                    currentTextView.setTextColor(color_normal);
                    tvMine.setTextColor(color_select);
                    currentTextView = tvMine;
                    currentImageView = imageMine;
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TO_ORDER) {
                switchFragment(WValue.MAIN_FRAGMENT_ORDER);
            }
        }
    }


    @Override
    public void onActionFail(Result result) {
        String action = result.getAction();
        switch (action) {
            case WAction.GET_SHOPPING_CART_DATA:
                MasApplication.getInstance().setIsInitCart(false);
                if (badgeView.isShown()) badgeView.hide();
                break;
        }
    }

    /**
     * 请求成功回调
     *
     * @param result
     */
    @Override
    public void onActionSucc(Result result) {
        String action = result.getAction();
        if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
            List<CartBo> cart_list = ((CartListResult) result).getData();
            if (cart_list != null) {
                //把购物车数据存储到数据库
                SqlUtil.initCartData(cart_list);
                MasApplication.getInstance().setIsInitCart(true);
                int count = SqlUtil.getCartCount();
                badgeView.setText(String.valueOf(count));
                if (0 == count) {
                    if (badgeView.isShown()) badgeView.hide();
                } else {
                    if (!badgeView.isShown()) badgeView.show();
                }
            } else {
                MasApplication.getInstance().setIsInitCart(false);
                if (badgeView.isShown()) badgeView.hide();
            }
        }

    }

}
