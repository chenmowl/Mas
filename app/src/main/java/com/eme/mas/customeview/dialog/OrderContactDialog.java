package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;


/**
 * Created by zulei on 16/8/4.
 */
public class OrderContactDialog extends BaseDialog {

    private boolean haveBrother;

    /**
     * 构造函数
     */
    public OrderContactDialog(boolean haveBrother) {
        this.haveBrother = haveBrother;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_order_contact;
    }

    @Override
    protected void showView(View view) {
        if(!haveBrother){
            view.findViewById(R.id.dialog_oc_brother).setVisibility(View.GONE);
            view.findViewById(R.id.dialog_oc_line).setVisibility(View.GONE);
        }

        view.findViewById(R.id.dialog_oc_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
        view.findViewById(R.id.dialog_oc_brother).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.contactBrother();
            }
        });
        view.findViewById(R.id.dialog_oc_business).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.contactBusiness();
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

    private OnContactListener mListener;

    public void setOnContactListener(OnContactListener listener){
        mListener=listener;
    }

    public interface OnContactListener{

        void contactBrother();

        void contactBusiness();

    }
}
