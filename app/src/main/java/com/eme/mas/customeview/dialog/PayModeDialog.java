package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;


/**
 * Created by zulei on 16/8/4.
 */
public class PayModeDialog extends BaseDialog {

    /**
     * 构造函数
     */
    public PayModeDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pay_mode;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_pm_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
        view.findViewById(R.id.dialog_pm_pay_online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.online();
            }
        });
        view.findViewById(R.id.dialog_pm_pay_offline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.offline();
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
        return Util.getScreenHeight()-Util.getStatusBarHeight();
    }

    private OnPayModeChangeListener mListener;

    public void setOnPayModeChangeListener(OnPayModeChangeListener listener){
        mListener=listener;
    }

    public interface OnPayModeChangeListener{

        void online();

        void offline();

    }
}
