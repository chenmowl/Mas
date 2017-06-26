package com.eme.mas.customeview.dialog;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.environment.KConfig;
import com.eme.mas.utils.Util;


/**
 *  设置主地址的dialog
 *
 * Created by dijiaoliang on 16/8/4.
 */
public class ChangeImageHostDialog extends BaseDialog {

    /**
     * 构造函数
     */
    public ChangeImageHostDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_change_host;
    }

    @Override
    protected void showView(View view) {

        TextView tv= (TextView) view.findViewById(R.id.tv_info);
        tv.setText(R.string.title_image_host_change);

        final EditText editText= (EditText) view.findViewById(R.id.et_host);
        editText.setText(KConfig.getHostUrl());

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
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host=editText.getText().toString().trim();
                KConfig.setHostUrl(host);
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
        return Util.getScreenHeight()-Util.getStatusBarHeight();
    }

}
