package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;

/**
 * Created by zulei on 16/8/16.
 */
public class DeleteDialog extends BaseDialog {

    //private Context mContext;

    /**
     * 构造函数
     */
    public DeleteDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_delete;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_dd_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
        view.findViewById(R.id.tv_dd_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.cancel();
            }
        });
        view.findViewById(R.id.tv_dd_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.ok();
            }
        });
    }

    @Override
    protected int getAnimStyle() {
        return R.style.AnimDialogAlphaEntry;
    }

    @Override
    protected int getWidth() {
        return Util.getScreenWidth();
    }

    @Override
    protected int getHeight() {
        return Util.getScreenHeight()-Util.getStatusBarHeight();
    }

    private OnDeleteListener mListener;

    public void setDeleteListener(OnDeleteListener listener){
        mListener=listener;
    }

    public interface OnDeleteListener{

        void cancel();

        void ok();

    }
}
