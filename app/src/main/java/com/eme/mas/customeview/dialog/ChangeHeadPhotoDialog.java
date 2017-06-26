package com.eme.mas.customeview.dialog;

import android.view.View;

import com.eme.mas.R;
import com.eme.mas.utils.Util;


/**
 * Created by dijiaoliang on 16/5/26.
 */
public class ChangeHeadPhotoDialog extends BaseDialog {

    /**
     * 构造函数
     */
    public ChangeHeadPhotoDialog() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_change_headphoto;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_ch_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
        view.findViewById(R.id.dialog_ch_takepic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.takePic();
            }
        });
        view.findViewById(R.id.dialog_ch_aldum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.takeFromAlbum();
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

    private OnHeadChangeListener mListener;

    public void setOnHeadChangeListener(OnHeadChangeListener listener){
        mListener=listener;
    }

    public interface OnHeadChangeListener{

        void takePic();

        void takeFromAlbum();

    }
}
