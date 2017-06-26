package com.eme.mas.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.eme.mas.MainActivity;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.activity.OrderCommitActivity;
import com.eme.mas.adapter.CartAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.CartInterface;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.data.SqlUtil;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车模块
 * <p>
 * Created by dijiaoliang on 16/7/15.
 */
@WLayout(layoutId = R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements CartInterface {

    private final static String TAG = CartFragment.class.getSimpleName();

    @Bind(R.id.iv_select_all)
    ImageView ivSelectAll;
    @Bind(R.id.lv_cart)
    SwipeMenuListView lvCart;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;
    @Bind(R.id.rl_cart_add_loading)
    RelativeLayout rlCartAddLoading;
    @Bind(R.id.rl_cart_empty)
    RelativeLayout rlCartEmpty;


    private ICart iCart;

    private List<DataRow> cartData;
    private CartAdapter cartAdapter;

    private boolean isSelectAll;//是否全部选中
    private boolean isDeleteOne;//是否删除一个
    private List<DataRow> deleteCartData;//批量删除购物车容器
    private int deletePosition, updaterPosition;//删除和更新的位置
    private boolean isAdd;//添加和删除的标志


    private QueryBuilder builder = new QueryBuilder("update shopping_cart set product_num=? where cart_id=?", "", "");
    private QueryBuilder builderDel = new QueryBuilder("delete from shopping_cart where cart_id=?", "");

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
        ((BaseImpl)iCart).cancelRequestByTag(TAG);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iCart = mController.getIAccount(this);//初始化控制器
        if (cartData == null) {
            cartData = new ArrayList<>();
        }
        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(getActivity(), cartData, this);
        }

        if (deleteCartData == null) {
            deleteCartData = new ArrayList<>();
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Util.dip2px(getActivity(), 60));
                // set a icon
                //deleteItem.setIcon(R.mipmap.ic_delete);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(Util.dip2px(getActivity(), 5));
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        lvCart.setMenuCreator(creator);
        lvCart.setAdapter(cartAdapter);

        if (isLogin()) {
            //TODO 处理登陆逻辑
            if (MasApplication.getInstance().isInitCart()) {
                //TODO 获取数据库中的购物车数据
                List<DataRow> tempDrList = SqlUtil.getCartData();
                cartData.clear();
                cartData.addAll(tempDrList);
                cartAdapter.notifyDataSetChanged();
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, true);
                if (0 == tempDrList.size()) {
                    isHideLayer(rlCartEmpty, false);
                } else {
                    isHideLayer(rlCartEmpty, true);
                }
            } else {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    //获取购物车数据
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, false);
                    isHideLayer(rlNoNetwork, true);
                    iCart.getCartList(TAG);
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.net_error);
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, false);
                }
            }
        } else {
            //TODO 未登录展示空背景
            isHideLayer(rlCartEmpty, false);
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlNoNetwork, true);
        }

        lvCart.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
//                    DataRow cartItem = cartData.get(position);
//                    cartData.remove(cartItem);
//                    cartAdapter.notifyDataSetChanged();

                    deletePosition = position;
                    isDeleteOne = true;
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, false);
                    isHideLayer(rlNoNetwork, true);
                    iCart.deleteCart(cartData.get(position).getString("cart_id"), TAG);
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.net_error);
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, false);
                }
                return false;
            }
        });

        lvCart.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


        refresh();

    }

    @Override
    public void callBackCartView(LinearLayout llReduce, LinearLayout llAdd, LinearLayout llCheckbox, final ImageView iv_checkbox, final int position) {
        final DataRow row = cartData.get(position);
        llCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(cartData.get(position).getString(9))) {
                    cartData.get(position).set(9, "0");
                    iv_checkbox.setImageResource(R.mipmap.nochoice_default);
                } else {
                    cartData.get(position).set(9, "1");
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
                    updateCartData(row.getString("product_id"), row.getString("spec_id"), String.valueOf(size));
                } else if (size == 1) {
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, true);
                    ToastUtil.shortToast(getActivity(), "可左滑删除");
                } else {
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, true);
                    row.set("product_num", "1");
                }
            }
        });
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer size = Integer.parseInt((String) row.get("product_num"));
                if (size >= 1) {
                    size++;
                } else {
                    size = 1;
                }
                isAdd = true;
                updaterPosition = position;
                updateCartData(row.getString("product_id"), row.getString("spec_id"), String.valueOf(size));
            }
        });
    }


    /**
     * 更新购物车数据（请求更新购物车接口）
     *
     * @param product_id
     * @param spec_id
     * @param product_num
     */
    private void updateCartData(String product_id, String spec_id, String product_num) {
        if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
            //获取购物车数据
            isHideLayer(rlCartEmpty, true);
            isHideLayer(rlCartAddLoading, false);
            isHideLayer(rlNoNetwork, true);
            iCart.updaterCart(product_id, spec_id, product_num, TAG);
        } else {
            isHideLayer(rlCartEmpty, true);
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlNoNetwork, false);
            ToastUtil.shortToast(getActivity(), "当前网络不可用");
        }

    }


    /**
     * 更改是否选择全部的状态
     */
    public void changeTopCheckBoxStatus() {
        for (DataRow item : cartData) {
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
    private void refreshCheckBox(boolean isChangeBoxs) {
        if (isChangeBoxs) {
            if (isSelectAll) {
                //改变所有checkbox按钮状态
                for (DataRow item : cartData) {
                    if ("1".equals(item.getString(10))) {
                        if ("0".equals(item.getString(9))) {
                            item.set(9, "1");
                        }
                    }
                }
            } else {
                for (DataRow item : cartData) {
                    if ("1".equals(item.getString(10))) {
                        if ("1".equals(item.getString(9))) {
                            item.set(9, "0");
                        }
                    }
                }
            }
            cartAdapter.notifyDataSetChanged();
        } else {
            //改变顶部checkbox状态
            if (isSelectAll) {
                //全选
                ivSelectAll.setImageResource(R.mipmap.choice_cart);
            } else {
                //非全选
                ivSelectAll.setImageResource(R.mipmap.nochoice_default);
            }
        }
    }

    /**
     * 判断是否有可选商品
     *
     * @return
     */
    private boolean isHasValidPro() {
        for (DataRow item : cartData) {
            if ("1".equals(item.getString(10))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 刷新价格与积分。
     */
    private void refresh() {
        int count = 0;
        double totalPrice = 0.00;
        for (DataRow item : cartData) {
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
        cartAdapter.notifyDataSetChanged();

    }

    /**
     * 显示价格
     *
     * @param totalPrice
     */
    private void showPrice(double totalPrice) {
        double price = Double.parseDouble(String.format("%.2f", totalPrice));
        String newInfo = price + "元";
        tvTotalPrice.setText(newInfo);
//        ProductUtils.processProductPrice02(tv_total_price, newInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
        ivSelectAll.setImageResource(R.mipmap.nochoice_default);//改变全选的checkbox的状态
        if (isLogin()) {
            if (MasApplication.getInstance().isInitCart()) {
                //TODO 获取数据库中的购物车数据
                List<DataRow> tempDrList = SqlUtil.getCartData();
                cartData.clear();
                cartData.addAll(tempDrList);
                cartAdapter.notifyDataSetChanged();
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, true);
                if (0 == tempDrList.size()) {
                    isHideLayer(rlCartEmpty, false);
                } else {
                    isHideLayer(rlCartEmpty, true);
                }
            } else {
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    //获取购物车数据
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, false);
                    isHideLayer(rlNoNetwork, true);
                    iCart.getCartList(TAG);
                } else {
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, false);
                    ToastUtil.shortToast(getActivity(), R.string.net_error);
                }
            }
        } else {
            //TODO 未登录展示空背景
            isHideLayer(rlCartEmpty, false);
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlNoNetwork, true);
        }
    }

    /**
     * fragment 在隐藏显示切换调用的方法
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            ivSelectAll.setImageResource(R.mipmap.nochoice_default);//改变全选的checkbox的状态
            if (isLogin()) {
                if (MasApplication.getInstance().isInitCart()) {
                    //TODO 获取数据库中的购物车数据
                    List<DataRow> tempDrList = SqlUtil.getCartData();
                    cartData.clear();
                    cartData.addAll(tempDrList);
                    cartAdapter.notifyDataSetChanged();
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, true);
                    if (0 == tempDrList.size()) {
                        isHideLayer(rlCartEmpty, false);
                    } else {
                        isHideLayer(rlCartEmpty, true);
                    }
                } else {
                    /**判断网络状态*/
                    if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                        //获取购物车数据
                        isHideLayer(rlCartEmpty, true);
                        isHideLayer(rlCartAddLoading, false);
                        isHideLayer(rlNoNetwork, true);
                        iCart.getCartList(TAG);
                    } else {
                        isHideLayer(rlCartEmpty, true);
                        isHideLayer(rlCartAddLoading, true);
                        isHideLayer(rlNoNetwork, false);
                        ToastUtil.shortToast(getActivity(), R.string.net_error);
                    }
                }
            } else {
                //TODO 未登录展示空背景
                isHideLayer(rlCartEmpty, false);
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, true);
            }
        }
        super.onHiddenChanged(hidden);
    }

    @OnClick({R.id.ll_select_all, R.id.ll_delete, R.id.btn_stroll, R.id.btn_to_home, R.id.btn_check_out})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ll_select_all:
                if (!isHasValidPro()) {
                    ToastUtil.shortToast(getActivity(), "无可选商品");
                    return;
                }
                if (isSelectAll) {
                    ivSelectAll.setImageResource(R.mipmap.nochoice_default);
                } else {
                    ivSelectAll.setImageResource(R.mipmap.choice_cart);
                }
                isSelectAll = !isSelectAll;
                refreshCheckBox(true);
                refresh();
                break;
            case R.id.ll_delete:
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    deleteCartData.clear();
                    for (DataRow row : cartData) {
                        if ("1".equals(row.get("goods_show"))) {
                            if ("1".equals(row.get("isSelected"))) {
                                deleteCartData.add(row);
                            }
                        }
                    }
                    if (deleteCartData.size() != 0) {
                        //有选中的购物车条目，执行删除操作
                        isDeleteOne = false;
                        StringBuilder builder = new StringBuilder();
                        for (DataRow row : deleteCartData) {
                            builder.append(row.get("cart_id")).append(",");
                        }
                        String deleteId = builder.deleteCharAt(builder.length() - 1).toString();
                        isHideLayer(rlCartEmpty, true);
                        isHideLayer(rlCartAddLoading, false);
                        isHideLayer(rlNoNetwork, true);
                        iCart.deleteCart(deleteId, TAG);
                    } else {
                        isHideLayer(rlCartEmpty, true);
                        isHideLayer(rlCartAddLoading, true);
                        isHideLayer(rlNoNetwork, true);
                        ToastUtil.shortToast(getActivity(), "无选中项");
                    }

                } else {
                    isHideLayer(rlCartEmpty, true);
                    isHideLayer(rlCartAddLoading, true);
                    isHideLayer(rlNoNetwork, false);
                    ToastUtil.shortToast(getActivity(), R.string.net_error);
                }
                break;
            case R.id.btn_stroll:
                if (MasApplication.getInstance().isInitCart()) {
                    //TODO 获取数据库中的购物车数据
                    List<DataRow> tempDrList = SqlUtil.getCartData();
                    cartData.clear();
                    cartData.addAll(tempDrList);
                    cartAdapter.notifyDataSetChanged();
                } else {
                    /**判断网络状态*/
                    if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                        //获取购物车数据
                        isHideLayer(rlCartEmpty, true);
                        isHideLayer(rlCartAddLoading, false);
                        isHideLayer(rlNoNetwork, true);
                        iCart.getCartList(TAG);
                    } else {
                        isHideLayer(rlCartEmpty, true);
                        isHideLayer(rlCartAddLoading, true);
                        isHideLayer(rlNoNetwork, false);
                        ToastUtil.shortToast(getActivity(), R.string.net_error);
                    }
                }
                break;
            case R.id.btn_to_home:
                //跳转到首页
                ((MainActivity) getActivity()).switchFragment(WValue.MAIN_FRAGMENT_HOME);
                break;
            case R.id.btn_check_out:
                //跳转到提交订单页面
                /**判断网络状态*/
                if (NetworkStatusUtil.isNetworkConnected(getActivity())) {
                    if (isLogin()) {
                        StringBuilder builder = new StringBuilder();
                        for (DataRow row : cartData) {
                            if ("1".equals(row.get("goods_show"))) {
                                if ("1".equals(row.get("isSelected"))) {
                                    builder.append(row.get("cart_id")).append(",");
                                }
                            }
                        }
                        if (builder.length() != 0) {
                            String cartIds = builder.deleteCharAt(builder.length() - 1).toString();
                            Intent intent = new Intent(getActivity(), OrderCommitActivity.class);
                            intent.putExtra("cartIds", cartIds);
                            startActivity(intent);
                        } else {
                            ToastUtil.shortToast(getActivity(), "无选中购物车条目");
                        }
                    }
                } else {
                    ToastUtil.shortToast(getActivity(), R.string.net_error);
                }
                break;
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(rlCartEmpty, true);
        isHideLayer(rlCartAddLoading, true);
        String action = result.getAction();
        switch (action) {
            case WAction.GET_SHOPPING_CART_DATA:
                isHideLayer(rlNoNetwork, false);
                break;
            case WAction.UPDATE_SHOPPING_CART:
                isHideLayer(rlNoNetwork, true);
                if (isAdd) {
                    ToastUtil.shortToast(getActivity(), "删减失败");
                } else {
                    ToastUtil.shortToast(getActivity(), "添加失败");
                }
                break;
            case WAction.DELETE_SHOPPING_CART:
                ToastUtil.shortToast(getActivity(), "删除失败");
                isHideLayer(rlNoNetwork, true);
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
        super.onActionSucc(result);
        String action = result.getAction();
        if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
            List<CartBo> cart_list = ((CartListResult) result).getData();
            if (cart_list != null) {
                //把购物车数据存储到数据库
                SqlUtil.initCartData(cart_list);
                MasApplication.getInstance().setIsInitCart(true);
                List<DataRow> tempDrList = SqlUtil.getCartData();
                cartData.clear();
                cartData.addAll(tempDrList);
                cartAdapter.notifyDataSetChanged();
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, true);
                if (cartData.size() == 0) {
                    isHideLayer(rlCartEmpty, false);
                } else {
                    isHideLayer(rlCartEmpty, true);
                }
            } else {
                MasApplication.getInstance().setIsInitCart(false);
                isHideLayer(rlCartEmpty, true);
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, false);
            }
        } else if (WAction.UPDATE_SHOPPING_CART.equals(action)) {
            int num = Integer.parseInt((String) cartData.get(updaterPosition).get("product_num"));
            if (isAdd) {
                num++;
            } else {
                num--;
            }
            /**更新数据库*/
            builder.set(0, String.valueOf(num));
            builder.set(1, (String) cartData.get(updaterPosition).get("cart_id"));
            builder.executeNoQuery();
            cartData.get(updaterPosition).set("product_num", String.valueOf(num));
            refresh();
            isHideLayer(rlCartAddLoading, true);
            isHideLayer(rlNoNetwork, true);
            if (cartData.size() == 0) {
                isHideLayer(rlCartEmpty, false);
            } else {
                isHideLayer(rlCartEmpty, true);
            }
            EventBus.getDefault().post(new RefreshCartEvent());
        } else if (WAction.DELETE_SHOPPING_CART.equals(action)) {
            if (isDeleteOne) {
                String cart_id = (String) cartData.get(deletePosition).get("cart_id");
                builderDel.set(0, cart_id);
                builderDel.executeNoQuery();
                cartData.remove(deletePosition);
            } else {
                //TODO 此时已经批量删除成功，需要更新数据库中的数据
                for (DataRow row : deleteCartData) {
                    builderDel.set(0, (String) row.get("cart_id"));
                    builderDel.executeNoQuery();
                    cartData.remove(row);
                }
            }
            refresh();
            if (cartData.size() == 0) {
                isHideLayer(rlCartEmpty, false);
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, true);
            } else {
                isHideLayer(rlCartEmpty, true);
                isHideLayer(rlCartAddLoading, true);
                isHideLayer(rlNoNetwork, true);
            }
            EventBus.getDefault().post(new RefreshCartEvent());

            ivSelectAll.setImageResource(R.mipmap.nochoice_default);//改变全选的checkbox的状态
        }

    }
}

