package com.eme.mas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.controller.customeInterface.CartInterface;
import com.eme.mas.data.sql.DataRow;

import java.util.List;

/**
 * 购物车弹出框
 * <p/>
 * 购物车适配器
 * <p/>
 * Created by dijiaoliang on 16/7/21.
 */
public class CartDialogAdapter extends BaseAdapter {

    public static final int INVALID_PRO = 0;// 无效商品布局
    public static final int VALID_PRO = 1;// 有效商品布局

    private List<DataRow> mList;
    private LayoutInflater mInflate;

    private CartInterface mCartIn;

    public CartDialogAdapter(Context context, List<DataRow> list, CartInterface cartIn) {
        mInflate = LayoutInflater.from(context);
        mList = list;
        mCartIn = cartIn;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataRow cartBo = mList.get(position);
        int type = getItemViewType(position);
        ValidViewHolder validViewHolder = null;

        if (convertView == null) {
            switch (type) {
                case INVALID_PRO:
                    convertView = mInflate.inflate(R.layout.item_dialog_cart_invalid, null);
                    validViewHolder = new ValidViewHolder();
                    validViewHolder.tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
                    validViewHolder.tv_pro_price = (TextView) convertView.findViewById(R.id.tv_pro_price);
                    validViewHolder.tv_show_cart_item_number = (TextView) convertView.findViewById(R.id.tv_show_cart_item_number);
                    convertView.setTag(validViewHolder);
                    validViewHolder.tv_pro_name.setText(cartBo.getString("product_name"));
                    validViewHolder.tv_pro_price.setText(cartBo.getString("product_price"));
                    validViewHolder.tv_show_cart_item_number.setText(cartBo.getString("product_num"));
                    break;
                // 左边
                case VALID_PRO:
                    convertView = mInflate.inflate(R.layout.item_dialog_cart, null);
                    validViewHolder = new ValidViewHolder();
                    validViewHolder.iv_checkbox = (ImageView) convertView.findViewById(R.id.iv_checkbox);
                    validViewHolder.ll_checkbox = (LinearLayout) convertView.findViewById(R.id.ll_checkbox);
                    validViewHolder.ll_reduce = (LinearLayout) convertView.findViewById(R.id.ll_reduce);
                    validViewHolder.ll_add = (LinearLayout) convertView.findViewById(R.id.ll_add);
                    validViewHolder.tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
                    validViewHolder.tv_pro_price = (TextView) convertView.findViewById(R.id.tv_pro_price);
                    validViewHolder.tv_show_cart_item_number = (TextView) convertView.findViewById(R.id.tv_show_cart_item_number);
                    convertView.setTag(validViewHolder);
                    if ("1".equals(cartBo.getString("isSelected"))) {
                        validViewHolder.iv_checkbox.setImageResource(R.mipmap.choice_cart);
                    } else {
                        validViewHolder.iv_checkbox.setImageResource(R.mipmap.nochoice_default);
                    }
                    validViewHolder.tv_pro_name.setText(cartBo.getString("product_name"));
                    validViewHolder.tv_pro_price.setText(cartBo.getString("product_price"));
                    validViewHolder.tv_show_cart_item_number.setText(cartBo.getString("product_num"));
                    mCartIn.callBackCartView(validViewHolder.ll_reduce, validViewHolder.ll_add, validViewHolder.ll_checkbox, validViewHolder.iv_checkbox, position);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case INVALID_PRO:
                    validViewHolder = (ValidViewHolder) convertView.getTag();
                    validViewHolder.tv_pro_name.setText(cartBo.getString("product_name"));
                    validViewHolder.tv_pro_price.setText(cartBo.getString("product_price"));
                    validViewHolder.tv_show_cart_item_number.setText(cartBo.getString("product_num"));
                    break;
                case VALID_PRO:
                    validViewHolder = (ValidViewHolder) convertView.getTag();
                    if ("1".equals(cartBo.getString("isSelected"))) {
                        validViewHolder.iv_checkbox.setImageResource(R.mipmap.choice_cart);
                    } else {
                        validViewHolder.iv_checkbox.setImageResource(R.mipmap.nochoice_default);
                    }
                    validViewHolder.tv_pro_name.setText(cartBo.getString("product_name"));
                    validViewHolder.tv_pro_price.setText(cartBo.getString("product_price"));
                    validViewHolder.tv_show_cart_item_number.setText(cartBo.getString("product_num"));
                    mCartIn.callBackCartView(validViewHolder.ll_reduce, validViewHolder.ll_add, validViewHolder.ll_checkbox, validViewHolder.iv_checkbox, position);
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        DataRow row = mList.get(position);
        if (!"1".equals(row.getString(10))) {
            return INVALID_PRO;
        } else {
            return VALID_PRO;
        }
    }

    class ValidViewHolder {
        private LinearLayout ll_checkbox;//  左侧checkbox布局
        private LinearLayout ll_reduce;//  减少商品布局
        private LinearLayout ll_add;//  增加商品布局
        private ImageView iv_checkbox;// 左侧的checkbox
        private TextView tv_pro_name; //    商品名
        private TextView tv_pro_price; //   商品价格
        private TextView tv_show_cart_item_number; //  商品个数
    }
}
