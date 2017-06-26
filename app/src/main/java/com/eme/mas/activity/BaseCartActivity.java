package com.eme.mas.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.CartDialogAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.CartInterface;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.MListView;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.data.sql.DataRow;
import com.eme.mas.data.sql.QueryBuilder;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.RefreshCartEvent;
import com.eme.mas.model.CartListResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.CartBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.eme.mas.utils.Util;
import com.eme.mas.utils.ViewUtil;
import com.owater.library.CircleTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 包含购物车模块的基类页面
 * <p>
 * Created by dijiaoliang on 16/8/1.
 */
public abstract class BaseCartActivity extends BaseActivity implements CartInterface {

    //全选按钮和删除按钮
    public LinearLayout ll_delete, ll_all;
    //购物车按钮
    public RelativeLayout btn_cart;
    //弹出框
    public LinearLayout ll_dialog;
    //双选控件
    public ImageView iv_all;
    //自定义的列表控件
    public MListView mListView;

    public TextView tv_total_price;
    //半透明背景框
    public View bg;
    //去结算按钮
    public Button btn_balance;

    public CircleTextView ctv_badge;//购物车数量标示控件

    public RelativeLayout rl_anim;

    //是否弹出
    boolean isOpen;
    //这个是动画的时间
    public long animTime = 300;
    //    public int y;
    //弹出框的高度
    int measuredHeight;
    int measuredWidth;
    //商品控制器
    protected IProduct mProController;
    protected ICart iCart;

    protected List<DataRow> mCartData;
    protected CartDialogAdapter mCartDialogAdapter;

    protected List<DataRow> deleteCartData;//批量删除购物车容器

    protected boolean isAdd;//添加和删除的标志
    protected int updaterPosition;//更新的位置
    protected boolean isSelectAll;//是否全部选中

    protected QueryBuilder builder = new QueryBuilder("update shopping_cart set product_num=? where cart_id=?", "", "");
    protected QueryBuilder builderDel = new QueryBuilder("delete from shopping_cart where cart_id=?", "");

    private final static String BASECARTTAG = "BaseCartActivity";

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        mProController = mController.getProduct(this);//初始化商品模块控制器
        iCart = mController.getIAccount(this);//初始化控制器
        if (deleteCartData == null) {
            deleteCartData = new ArrayList<>();
        }
        mCartData = new ArrayList<>();
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initCartView();
        mCartDialogAdapter = new CartDialogAdapter(MasApplication.getInstance(), mCartData, this);
        mListView.setAdapter(mCartDialogAdapter);
        bg.setAlpha(0f);
        measuredHeight = ViewUtil.measureHeight(ll_dialog);
        ll_dialog.setTranslationY(measuredHeight);
        measuredWidth = Util.dip2px(MasApplication.getInstance(), 50);

        //这里请求购物车数据完成初始化
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator.ofFloat(tv_total_price, "translationX", -measuredWidth, 0f).setDuration(animTime).start();
                ObjectAnimator.ofFloat(ll_dialog, "translationY", 0f, measuredHeight).setDuration(animTime).start();
                ObjectAnimator.ofFloat(btn_cart, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                ObjectAnimator.ofFloat(ctv_badge, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                ObjectAnimator.ofFloat(bg, "alpha", 1.0f, 0f).setDuration(animTime).start();
                if (bg.getVisibility() == View.VISIBLE) bg.setVisibility(View.GONE);
                isOpen = false;
            }
        });

        boolean isLogin = SPBase.getBoolean(MasApplication.getInstance(), SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY);
        if (isLogin) {
            //TODO 处理登陆逻辑
            if (MasApplication.getInstance().isInitCart()) {
                //TODO 获取数据库中的购物车数据
                List<DataRow> tempDrList = SqlUtil.getCartData();
                mCartData.clear();
                mCartData.addAll(tempDrList);
                mCartDialogAdapter.notifyDataSetChanged();
            } else {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
                    //获取购物车数据
                    iCart.getCartList(BASECARTTAG);
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), "当前网络不可用");
                }
            }
        } else {
            //TODO 未登录展示空背景

        }

        updateBottomState();

    }

    @Override
    protected void onDestroy() {
        ((BaseImpl) mProController).cancelRequestByTag(BASECARTTAG);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        updateBottomState();
        super.onResume();
    }

    /**
     * 判断底部购物车模块的状态，无商品置灰，有商品正常
     */
    public void updateBottomState() {
        List<DataRow> tempDrList = SqlUtil.getCartData();
        if (tempDrList != null && tempDrList.size() != 0) {
            hideCartCount(false);
            notifyCartList();
            btn_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen) {
                        ObjectAnimator.ofFloat(tv_total_price, "translationX", -measuredWidth, 0f).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(ll_dialog, "translationY", 0f, measuredHeight).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(btn_cart, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(ctv_badge, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(bg, "alpha", 1.0f, 0f).setDuration(animTime).start();
                        if (bg.getVisibility() == View.VISIBLE) bg.setVisibility(View.GONE);
                        isOpen = false;
                    } else {
                        ObjectAnimator.ofFloat(tv_total_price, "translationX", 0f, -measuredWidth).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(ll_dialog, "translationY", measuredHeight, 0f).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(btn_cart, "translationY", 0f, -measuredHeight).setDuration(animTime).start();
                        ObjectAnimator.ofFloat(ctv_badge, "translationY", 0f, -measuredHeight).setDuration(animTime).start();
                        if (bg.getVisibility() == View.GONE) bg.setVisibility(View.VISIBLE);
                        ObjectAnimator.ofFloat(bg, "alpha", 0f, 1.0f).setDuration(animTime).start();
                        isOpen = true;
                    }
                }
            });
            btn_cart.setBackgroundResource(R.mipmap.classify_cart);
            tv_total_price.setTextColor(getResources().getColor(R.color.main_color_red));
            btn_balance.setBackgroundResource(R.color.main_color_red);
            btn_balance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**判断网络状态*/
                    if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
                        if (isLogin()) {
                            StringBuilder builder = new StringBuilder();
                            for (DataRow row : mCartData) {
                                if ("1".equals(row.get("goods_show"))) {
                                    if ("1".equals(row.get("isSelected"))) {
                                        builder.append(row.get("cart_id")).append(",");
                                    }
                                }
                            }
                            if (builder.length() != 0) {
                                String cartIds = builder.deleteCharAt(builder.length() - 1).toString();
                                Intent intent = new Intent(BaseCartActivity.this, OrderCommitActivity.class);
                                intent.putExtra("cartIds", cartIds);
                                startActivity(intent);
                            } else {
                                ToastUtil.shortToast(MasApplication.getInstance(), "无选中购物车条目");
                            }

                            //关闭购物车
                            if (isOpen) {
                                ObjectAnimator.ofFloat(tv_total_price, "translationX", -measuredWidth, 0f).setDuration(animTime).start();
                                ObjectAnimator.ofFloat(ll_dialog, "translationY", 0f, measuredHeight).setDuration(animTime).start();
                                ObjectAnimator.ofFloat(btn_cart, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                                ObjectAnimator.ofFloat(ctv_badge, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                                ObjectAnimator.ofFloat(bg, "alpha", 1.0f, 0f).setDuration(animTime).start();
                                if (bg.getVisibility() == View.VISIBLE) bg.setVisibility(View.GONE);
                                isOpen = false;
                            }
                        }
                    } else {
                        ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
                    }
                }
            });
            refreshCartCount();
        } else {
            hideCartCount(true);
            btn_cart.setOnClickListener(null);
            btn_balance.setOnClickListener(null);
            btn_cart.setBackgroundResource(R.mipmap.classify_cart_nothing);
            tv_total_price.setTextColor(getResources().getColor(R.color.cart_tv_gray02));
            btn_balance.setBackgroundResource(R.color.cart_tv_gray02);
        }
    }

    /**
     * 初始化弹出框的列表数据
     */
    public void notifyCartList() {
        List<DataRow> tempDrList = SqlUtil.getCartData();
        if (mCartData != null && mCartData.size() != 0) mCartData.clear();
        mCartData.addAll(tempDrList);
        mCartDialogAdapter.notifyDataSetChanged();
        measuredHeight = ViewUtil.measureHeight(ll_dialog);
        ll_dialog.setTranslationY(measuredHeight);
    }

    /**
     * 初始化购物车控件，必须要做的操作
     */
    public abstract void initCartView();


    @Override
    public void callBackCartView(LinearLayout llReduce, LinearLayout llAdd, LinearLayout llCheckbox, final ImageView iv_checkbox, final int position) {
        final DataRow row = mCartData.get(position);
        llCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(mCartData.get(position).getString(9))) {
                    mCartData.get(position).set(9, "0");
                    iv_checkbox.setImageResource(R.mipmap.nochoice_default);
                } else {
                    mCartData.get(position).set(9, "1");
                    iv_checkbox.setImageResource(R.mipmap.choice_cart);
                }
                changeTopCheckBoxStatus();
                refreshCheckBox(false);
                refresh();
            }
        });
        llReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer size = Integer.parseInt((String) row.get("product_num"));
                if (size >= 2) {
                    size--;
                    isAdd = false;
                    updaterPosition = position;
                    updateCartData(row.getString("product_id"), row.getString("spec_id"), "1");
                } else if (size == 1) {
//                    ToastUtil.shortToast(mContext, "可左滑删除");
                } else {
                    row.set("product_num", "1");
                }
            }
        });
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdd = true;
                updaterPosition = position;
                updateCartData(row.getString("product_id"), row.getString("spec_id"), "1");
            }
        });
    }

    /**
     * 更改是否选择全部的状态
     */
    protected void changeTopCheckBoxStatus() {
        for (DataRow item : mCartData) {
            if ("1".equals(item.getString(10))) {
                if ("0".equals(item.getString(9))) {
                    isSelectAll = false;
                    return;
                }
            }
        }
        isSelectAll = true;
        return;
    }

    /**
     * 更改界面中checkbox的状态
     *
     * @param isChangeBoxs
     */
    protected void refreshCheckBox(boolean isChangeBoxs) {
        if (isChangeBoxs) {
            if (isSelectAll) {
                //改变所有checkbox按钮状态
                for (DataRow item : mCartData) {
                    if ("1".equals(item.getString(10))) {
                        if ("0".equals(item.getString(9))) {
                            item.set(9, "1");
                        }
                    }

                }
            } else {
                for (DataRow item : mCartData) {
                    if ("1".equals(item.getString(10))) {
                        if ("1".equals(item.getString(9))) {
                            item.set(9, "0");
                        }
                    }
                }
            }
            mCartDialogAdapter.notifyDataSetChanged();
        } else {
            //改变顶部checkbox状态
            if (isSelectAll) {
                //全选
                iv_all.setImageResource(R.mipmap.choice_cart);
            } else {
                //非全选
                iv_all.setImageResource(R.mipmap.nochoice_default);
            }
        }
    }

    /**
     * 判断是否有可选商品
     *
     * @return
     */
    protected boolean isHasValidPro() {
        for (DataRow item : mCartData) {
            if ("1".equals(item.getString(10))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新价格
     */
    protected void refresh() {
        int count = 0;
        double totalPrice = 0.00;
        for (DataRow item : mCartData) {
            if ("1" == item.getString("isSelected")) {
                double price = Double.valueOf(item.getString("product_price"));
                int number = Integer.valueOf(item.getString("product_num"));
                totalPrice += price * number;
                count++;
            }
        }

//        if (count == goodsList.size()) {
//            ivCheckAll.setImageResource(R.mipmap.cart_selected);
//            tvAllDelete.setVisibility(View.VISIBLE);
//        } else {
//            ivCheckAll.setImageResource(R.mipmap.cart_unselected);
//            tvAllDelete.setVisibility(View.GONE);
//        }
        showPrice(totalPrice);
        mCartDialogAdapter.notifyDataSetChanged();

    }

    /**
     * 更新购物车数据（请求更新购物车接口）
     *
     * @param product_id
     * @param spec_id
     * @param product_num
     */
    protected void updateCartData(String product_id, String spec_id, String product_num) {
        if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
            //获取购物车数据
            iCart.updaterCart(product_id, spec_id, product_num, BASECARTTAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }

    }

    /**
     * 显示价格
     *
     * @param totalPrice
     */
    protected void showPrice(double totalPrice) {
        double price = Double.parseDouble(String.format("%.2f", totalPrice));
        String newInfo = price + "元";
        tv_total_price.setText(newInfo);
    }


    /**
     * 购物车数量标示的显示隐藏控制
     *
     * @param isHide
     */
    protected void hideCartCount(boolean isHide) {
        int visiable = ctv_badge.getVisibility();
        if (isHide) {
            if (visiable == View.VISIBLE) {
                ctv_badge.setVisibility(View.INVISIBLE);
            }
        } else {
            if (visiable == View.INVISIBLE || visiable == View.GONE) {
                ctv_badge.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 改变购物车标示数量
     */
    protected void refreshCartCount() {
        List<DataRow> tempDrList = SqlUtil.getCartData();
        if (tempDrList != null && tempDrList.size() != 0) {
            ctv_badge.setVisibility(View.VISIBLE);
            int count = 0;
            for (DataRow row : tempDrList) {
                count = count + Integer.parseInt((String) row.get("product_num"));
            }
            ctv_badge.setText(String.valueOf(count));
        } else {
            ctv_badge.setText("0");
            ctv_badge.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    private PathMeasure mPathMeasure;


    /**
     * ★★★★★把商品添加到购物车的动画效果★★★★★
     *
     * @param iv
     */
    protected void addCart(ImageView iv) {
        int size = Util.dip2px(this, 22);
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(this);
        goods.setImageDrawable(iv.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        rl_anim.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        rl_anim.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        ctv_badge.getLocationInWindow(endLoc);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0];
        float startY = startLoc[1] - parentLocation[1];

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + ctv_badge.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(WValue.TIME_ADDCART);
        // 匀速线性插值器
//        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                rl_anim.removeView(goods);
                EventBus.getDefault().post(new RefreshCartEvent());
                updateBottomState();
                ToastUtil.shortToast(MasApplication.getInstance(), "添加购物车成功");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @OnClick({R.id.ll_all, R.id.ll_delete})
    void cartDialogClick(View v) {
        switch (v.getId()) {
            case R.id.ll_all:
                if (!isHasValidPro()) {
                    ToastUtil.shortToast(MasApplication.getInstance(), "无可选商品");
                    return;
                }
                if (isSelectAll) {
                    iv_all.setImageResource(R.mipmap.nochoice_default);
                } else {
                    iv_all.setImageResource(R.mipmap.choice_cart);
                }
                isSelectAll = !isSelectAll;
                refreshCheckBox(true);
                refresh();
                break;
            case R.id.ll_delete:
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
                    deleteCartData.clear();
                    for (DataRow row : mCartData) {
                        if ("1".equals(row.get("goods_show"))) {
                            if ("1".equals(row.get("isSelected"))) {
                                deleteCartData.add(row);
                            }
                        }
                    }
                    if (deleteCartData.size() != 0) {
                        //有选中的购物车条目，执行删除操作
                        StringBuilder builder = new StringBuilder();
                        for (DataRow row : deleteCartData) {
                            builder.append(row.get("cart_id")).append(",");
                        }
                        String deleteId = builder.deleteCharAt(builder.length() - 1).toString();
                        iCart.deleteCart(deleteId, BASECARTTAG);
                    } else {
                        ToastUtil.shortToast(MasApplication.getInstance(), "无选中项");
                    }
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
                }
                break;
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        if (result != null) {
            String action = result.getAction();
            if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
                List<CartBo> cart_list = ((CartListResult) result).getData();
                if (cart_list != null) {
                    //把购物车数据存储到数据库
                    SqlUtil.initCartData(cart_list);
                    MasApplication.getInstance().setIsInitCart(true);
                    List<DataRow> tempDrList = SqlUtil.getCartData();
                    mCartData.clear();
                    mCartData.addAll(tempDrList);
                    mCartDialogAdapter.notifyDataSetChanged();
                } else {
                    MasApplication.getInstance().setIsInitCart(false);
                }
            } else if (WAction.UPDATE_SHOPPING_CART.equals(action)) {
                int num = Integer.parseInt((String) mCartData.get(updaterPosition).get("product_num"));
                if (isAdd) {
                    num++;
                } else {
                    num--;
                }
                /**更新数据库*/
                builder.set(0, String.valueOf(num));
                builder.set(1, (String) mCartData.get(updaterPosition).get("cart_id"));
                builder.executeNoQuery();
                mCartData.get(updaterPosition).set("product_num", String.valueOf(num));
                refresh();
                refreshCartCount();
            } else if (WAction.DELETE_SHOPPING_CART.equals(action)) {
                //TODO 此时已经批量删除成功，需要更新数据库中的数据
                for (DataRow row : deleteCartData) {
                    builderDel.set(0, (String) row.get("cart_id"));
                    builderDel.executeNoQuery();
                    mCartData.remove(row);
                }
                refreshCartCount();
                if (mCartData.size() != 0) {
                    refresh();
                    int tempHeight = ViewUtil.measureHeight(ll_dialog);
                    ObjectAnimator.ofFloat(btn_cart, "translationY", -measuredHeight, -tempHeight).setDuration(50).start();
                    ObjectAnimator.ofFloat(ctv_badge, "translationY", -measuredHeight, -tempHeight).setDuration(50).start();
                    measuredHeight = tempHeight;
                } else {
                    ObjectAnimator.ofFloat(tv_total_price, "translationX", -measuredWidth, 0f).setDuration(animTime).start();
                    ObjectAnimator.ofFloat(ll_dialog, "translationY", 0f, measuredHeight).setDuration(animTime).start();
                    ObjectAnimator.ofFloat(btn_cart, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                    ObjectAnimator.ofFloat(ctv_badge, "translationY", -measuredHeight, 0f).setDuration(animTime).start();
                    ObjectAnimator.ofFloat(bg, "alpha", 1.0f, 0f).setDuration(animTime).start();
                    if (bg.getVisibility() == View.VISIBLE) bg.setVisibility(View.GONE);
                    isOpen = false;
                    refresh();
                    hideCartCount(true);
                    btn_cart.setOnClickListener(null);
                    btn_cart.setBackgroundResource(R.mipmap.classify_cart_nothing);
                    tv_total_price.setTextColor(getResources().getColor(R.color.cart_tv_gray02));
                    btn_balance.setBackgroundResource(R.color.cart_tv_gray02);
                }
            }
        } else {
            String action = result.getAction();
            if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
                MasApplication.getInstance().setIsInitCart(false);
            } else if (WAction.UPDATE_SHOPPING_CART.equals(action)) {
                if (isAdd) {
                    ToastUtil.shortToast(MasApplication.getInstance(), "删减失败");
                } else {
                    ToastUtil.shortToast(MasApplication.getInstance(), "添加失败");
                }
            } else if (WAction.DELETE_SHOPPING_CART.equals(action)) {
                ToastUtil.shortToast(MasApplication.getInstance(), "删除失败");
            }

        }
    }
}
