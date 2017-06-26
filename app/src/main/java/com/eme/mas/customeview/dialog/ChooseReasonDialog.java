package com.eme.mas.customeview.dialog;

import android.content.Context;
import android.view.View;

import com.eme.mas.R;
import com.eme.mas.customeview.datepicker.OnWheelScrollListener;
import com.eme.mas.customeview.datepicker.WheelView;
import com.eme.mas.customeview.datepicker.adapter.ArrayWheelAdapter;
import com.eme.mas.utils.Util;

/**
 * 选择退单原因
 * Created by zulei on 16/8/9.
 */
public class ChooseReasonDialog extends BaseDialog {

    private WheelView reason;
    private ArrayWheelAdapter adapter;
    String[] reasons = {"我不想买了","信息填写错误，重新拍","卖家缺货","同城见面交易","其他原因"};

    /**
     * 构造函数
     */
    public ChooseReasonDialog(Context context) {

        adapter = new ArrayWheelAdapter(context,reasons);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_choose_reason;
    }

    @Override
    protected int getWidth() {
        return Util.getScreenWidth();
    }

    @Override
    protected int getHeight() {
        return Util.getScreenHeight() - Util.getStatusBarHeight();
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_cr_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
        view.findViewById(R.id.dialog_cr_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        view.findViewById(R.id.dialog_cr_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReason();
                cancel();
            }
        });

        initDate(view);

    }




    /**
     * 初始化
     */
    private void initDate(View view){
        reason = (WheelView) view.findViewById(R.id.dialog_cr_reason);

        reason.setViewAdapter(adapter);
        reason.setCyclic(false);//是否循环
        reason.addScrollingListener(scrollListener);
        reason.setVisibleItems(7);
        reason.setCurrentItem(2);
    }



    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
        }
    };


    private void showReason() {
        //CharSequence y = ((NumericWheelAdapter)year.getViewAdapter()).getItemText(year.getCurrentItem());
        //CharSequence m = ((NumericWheelAdapter)month.getViewAdapter()).getItemText(month.getCurrentItem());
        //CharSequence d = ((NumericWheelAdapter)day.getViewAdapter()).getItemText(day.getCurrentItem());
        //mListener.changeBirthday(y+"-"+m+"-"+d);

        int p = reason.getCurrentItem();
        mListener.showReason(p,reasons[p]);
    }

    @Override
    protected int getAnimStyle() {
        return R.style.AnimDialogBottomEntry;
    }

    private OnChooseReasonListener mListener;

    public void setOnChooseReasonListener(OnChooseReasonListener listener) {
        mListener = listener;
    }

    public interface OnChooseReasonListener {

        void showReason(int position,String reason);

    }
}
