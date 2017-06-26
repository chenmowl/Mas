package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;


/**
 * Created by zulei on 16/8/4.
 */
public class ChangeSexDialog extends BaseDialog {

    /**
     * 构造函数
     */
    public ChangeSexDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_change_sex;
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
        view.findViewById(R.id.dialog_cs_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.changeMale();
            }
        });
        view.findViewById(R.id.dialog_cs_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.changeFemale();
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

    private OnSexChangeListener mListener;

    public void setOnSexChangeListener(OnSexChangeListener listener){
        mListener=listener;
    }

    public interface OnSexChangeListener{

        void changeMale();

        void changeFemale();

    }
}
