package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;


/**
 *
 * Created by zulei on 16/8/4.
 */
public class ProShareDialog extends BaseDialog {

    /**
     * 构造函数
     */
    public ProShareDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pro_share;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_cs_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        view.findViewById(R.id.ll_copy_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.shortToast(mContext, "ll_copy_link");
            }
        });
        view.findViewById(R.id.ll_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.shortToast(mContext, "ll_weixin");
            }
        });
        view.findViewById(R.id.ll_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.shortToast(mContext, "ll_qq");
            }
        });
        view.findViewById(R.id.ll_kongjian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.shortToast(mContext, "ll_kongjian");
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

    private OnShareListener mListener;

    public void setOnShareListener(OnShareListener listener) {
        mListener = listener;
    }

    public interface OnShareListener {

        void share();

    }
}
