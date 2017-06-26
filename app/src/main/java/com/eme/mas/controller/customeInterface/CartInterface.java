package com.eme.mas.controller.customeInterface;

import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zhangxiaoming on 16/6/23.
 */
public interface CartInterface {

    void callBackCartView(LinearLayout llReduce, LinearLayout llAdd, LinearLayout llCheckbox, ImageView iv_checkbox, int position);
}
