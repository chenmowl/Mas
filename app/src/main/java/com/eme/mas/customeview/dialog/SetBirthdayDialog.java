package com.eme.mas.customeview.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.eme.mas.R;
import com.eme.mas.customeview.datepicker.OnWheelScrollListener;
import com.eme.mas.customeview.datepicker.WheelView;
import com.eme.mas.customeview.datepicker.adapter.NumericWheelAdapter;
import com.eme.mas.utils.Util;

import java.util.Calendar;

/**
 * 设置生日的dialog
 * <p>
 * Created by dijiaoliang on 16/5/26.
 */
public class SetBirthdayDialog extends BaseDialog {

    private WheelView year;
    private WheelView month;
    private WheelView day;

    private String nian,yue,ri;
    private NumericWheelAdapter numericWheelAdapter,numericWheelAdapter1,numericWheelAdapter2,
            numericWheelAdapter3;
    /**
     * 构造函数
     *
     * @param context
     */
    public SetBirthdayDialog(Context context) {
        numericWheelAdapter1=new NumericWheelAdapter(context,0, 0);
        numericWheelAdapter2=new NumericWheelAdapter(context,0, 0, "%02d");
        numericWheelAdapter3=new NumericWheelAdapter(context,0, 0, "%02d");
        numericWheelAdapter=new NumericWheelAdapter(context,0,0, "%02d");
    }

    public SetBirthdayDialog(Context context,String nian,String yue,String ri) {
        this(context);
        this.nian = nian;
        this.yue = yue;
        this.ri = ri;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_set_birth;
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
        view.findViewById(R.id.dialog_sb_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        view.findViewById(R.id.dialog_sb_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        view.findViewById(R.id.dialog_sb_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
                cancel();
            }
        });

        initDate(view);
    }

    /**
     * 初始化时间
     */
    private void initDate(View view){
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
        int norMonth=c.get(Calendar.MONTH);
        int norDay =c.get(Calendar.DAY_OF_MONTH);
        int curYear = norYear;
        int curMonth =norMonth+1;
        int curDate =norDay ;//mDay
        year = (WheelView) view.findViewById(R.id.dialog_sb_year);
//        NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(this,1950, norYear);
        //NumericWheelAdapter numericWheelAdapter1=new NumericWheelAdapter(mContext,1900, norYear);
        numericWheelAdapter1.updateAdapter(1900, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(false);//是否循环
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.dialog_sb_month);
        //NumericWheelAdapter numericWheelAdapter2=new NumericWheelAdapter(mContext,1, 12, "%02d");
        numericWheelAdapter2.updateAdapter(1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(false);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.dialog_sb_day);
        if(!TextUtils.isEmpty(nian) && !TextUtils.isEmpty(yue)){
            curYear = Integer.parseInt(nian);
            curMonth = Integer.parseInt(yue);
        }
        int realdays = getDay(curYear, curMonth);
        //NumericWheelAdapter numericWheelAdapter3=new NumericWheelAdapter(mContext,1,realdays , "%02d");
        numericWheelAdapter3.updateAdapter(1,realdays , "%02d");
        numericWheelAdapter3.setLabel("日");
        day.setViewAdapter(numericWheelAdapter3);
        day.setCyclic(false);
        day.addScrollingListener(scrollListener);
        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        if(TextUtils.isEmpty(nian)){
            year.setCurrentItem(curYear-1900);
        }else{
            year.setCurrentItem(Integer.parseInt(nian)-1900);
        }

        if(TextUtils.isEmpty(nian)){
            month.setCurrentItem(curMonth-1);
        }else{
            month.setCurrentItem(Integer.parseInt(yue)-1);
        }

        if(TextUtils.isEmpty(nian)){
            day.setCurrentItem(curDate-1);
        }else{
            day.setCurrentItem(Integer.parseInt(ri)-1);
        }

    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int postionYear = year.getCurrentItem();
            int postionMonth = month.getCurrentItem()+1;
            int postionDay = day.getCurrentItem()+1;
            if(wheel == year || wheel == month){
                CharSequence y = ((NumericWheelAdapter)year.getViewAdapter()).getItemText(postionYear);
                CharSequence m = ((NumericWheelAdapter)month.getViewAdapter()).getItemText(postionMonth-1);
                int d = getDay(Integer.parseInt(y.toString()),Integer.parseInt(m.toString()));
                //NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(mContext,1, d, "%02d");
                numericWheelAdapter.updateAdapter(1, d, "%02d");
                numericWheelAdapter.setLabel("日");
                day.setViewAdapter(numericWheelAdapter);
                day.setCyclic(false);
                day.addScrollingListener(scrollListener);
                if(d<postionDay){
                    day.setCurrentItem(d-1,true);
                }

            }
        }
    };

    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    private void showTime() {
        CharSequence y = ((NumericWheelAdapter)year.getViewAdapter()).getItemText(year.getCurrentItem());
        CharSequence m = ((NumericWheelAdapter)month.getViewAdapter()).getItemText(month.getCurrentItem());
        CharSequence d = ((NumericWheelAdapter)day.getViewAdapter()).getItemText(day.getCurrentItem());
        mListener.changeBirthday((String) TextUtils.concat(y,"-",m,"-",d));
    }

    @Override
    protected int getAnimStyle() {
        return R.style.AnimDialogBottomEntry;
    }

    private OnBirthChangeListener mListener;

    public void setOnBirthChangeListener(OnBirthChangeListener listener) {
        mListener = listener;
    }

    public interface OnBirthChangeListener {

        void changeBirthday(String birthday);

    }
}
