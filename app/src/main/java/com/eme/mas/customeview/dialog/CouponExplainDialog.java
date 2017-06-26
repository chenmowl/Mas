package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;

/**
 * 优惠券使用说明dialog
 * <p>
 * Created by dijiaoliang on 16/10/27.
 */

public class CouponExplainDialog extends BaseDialog {


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_coupon_explain;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_cs_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
