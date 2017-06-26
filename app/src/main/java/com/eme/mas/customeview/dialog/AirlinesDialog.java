package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;


/**
 * 客服的弹出框
 * <p/>
 * Created by zulei on 16/8/4.
 */
public class AirlinesDialog extends BaseDialog {

    /**
     * 构造函数
     */
    public AirlinesDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_airlines;
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
        view.findViewById(R.id.tv_airline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.toAirline();
            }
        });
        view.findViewById(R.id.tv_after_sale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.toAfterSale();
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


    //回调接口
    private OnCallListener mListener;

    /**
     * 联系客服的回调
     * @param listener
     */
    public void setOnCallListener(OnCallListener listener) {
        mListener = listener;
    }

    public interface OnCallListener {

        void toAirline();

        void toAfterSale();

    }
}
