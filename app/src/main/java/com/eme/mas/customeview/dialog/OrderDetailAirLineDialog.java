package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;

/**
 * 订单详情联系客服dialog
 * <p>
 * Created by dijiaoliang on 16/10/27.
 */

public class OrderDetailAirLineDialog extends BaseDialog {


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_order_detail_airline;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_cs_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        view.findViewById(R.id.btn_airline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAirLineListener != null) mOnAirLineListener.call();
                cancel();
            }
        });
    }

    @Override
    protected int getAnimStyle() {
        return R.style.AnimDialogBottomEntry;
    }

    @Override
    protected int getWidth() {
        return Util.getScreenWidth();
    }

    @Override
    protected int getHeight() {
        return Util.getScreenHeight() - Util.getStatusBarHeight();
    }

    private OnAirLineListener mOnAirLineListener;

    public void setmOnAirLineListener(OnAirLineListener mOnAirLineListener) {
        this.mOnAirLineListener = mOnAirLineListener;
    }

    public interface OnAirLineListener {
        void call();
    }
}
