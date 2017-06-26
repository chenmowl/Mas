package com.eme.mas.customeview.dialog;

import android.content.Context;
import android.view.View;

import com.eme.mas.R;
import com.eme.mas.customeview.datepicker.OnWheelScrollListener;
import com.eme.mas.customeview.datepicker.WheelView;
import com.eme.mas.customeview.datepicker.adapter.ArrayWheelAdapter;
import com.eme.mas.customeview.datepicker.adapter.NumericWheelAdapter;
import com.eme.mas.utils.ConstUtils;
import com.eme.mas.utils.TimeUtils;
import com.eme.mas.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 设置送达时间的dialog
 * Created by zulei on 16/8/4.
 */
public class ServedTimeDialog extends BaseDialog {

    private WheelView day;
    private WheelView hour;
    private WheelView minute;

    private ArrayWheelAdapter arrayWheelAdapter;
    private NumericWheelAdapter adapterHour,adapterMinute;

    /**
     * 构造函数
     */
    public ServedTimeDialog(Context context) {
        arrayWheelAdapter = new ArrayWheelAdapter(context,null);
        adapterHour=new NumericWheelAdapter(context, 0 , 0, "%02d");
        adapterMinute=new NumericWheelAdapter(context, 0 , 0, "%02d");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_set_servedtime;
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
        view.findViewById(R.id.btn_dialog_set_servedtime_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.rightNow();
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
        List list = get30date();
        //arrayWheelAdapter = new ArrayWheelAdapter(mContext,list.toArray());
        arrayWheelAdapter.updateAdapter(list.toArray());
        day = (WheelView) view.findViewById(R.id.dialog_sb_day);
        day.setViewAdapter(arrayWheelAdapter);
        day.setCyclic(false);//是否循环
        day.addScrollingListener(scrollListener);

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int m = c.get(Calendar.MINUTE) ;
        c.set(Calendar.MINUTE,m+20);
        int norHour = c.get(Calendar.HOUR_OF_DAY);
        int norMinute=c.get(Calendar.MINUTE);



        hour = (WheelView) view.findViewById(R.id.dialog_sb_hour);
        setNumericWheelAdapter(hour,adapterHour,norHour,24,"时");

        minute = (WheelView) view.findViewById(R.id.dialog_sb_minute);
        setNumericWheelAdapter(minute,adapterMinute,norMinute,60,"分");

        day.setVisibleItems(7);
        hour.setVisibleItems(7);
        minute.setVisibleItems(7);

        day.setCurrentItem(0);
        hour.setCurrentItem(0);
        minute.setCurrentItem(0);
    }



    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int postionDay = day.getCurrentItem();
            int postionHour = hour.getCurrentItem();
            int postionMinute = minute.getCurrentItem();
            if(wheel == day){
                if(postionDay==0){
                    Calendar c = Calendar.getInstance();
                    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    int m = c.get(Calendar.MINUTE) ;
                    c.set(Calendar.MINUTE,m+20);
                    int norHour = c.get(Calendar.HOUR_OF_DAY);
                    int norMinute=c.get(Calendar.MINUTE);
                    setNumericWheelAdapter(hour,adapterHour,norHour,24,"时");
                    setNumericWheelAdapter(minute,adapterMinute,norMinute,60,"分");
                    hour.setCurrentItem(0,true);
                    minute.setCurrentItem(0,true);
                }else{
                    CharSequence csPosHour = ((NumericWheelAdapter)hour.getViewAdapter()).getItemText(postionHour);
                    setNumericWheelAdapter(hour,adapterHour,0,24,"时");
                    hour.setCurrentItem(Integer.parseInt(csPosHour.toString()));

                    CharSequence csPosMinute = ((NumericWheelAdapter)minute.getViewAdapter()).getItemText(postionMinute);
                    setNumericWheelAdapter(minute,adapterMinute,0,60,"分");
                    minute.setCurrentItem(Integer.parseInt(csPosMinute.toString()));
                }
            }
            if(wheel == hour){
                if(postionDay == 0){
                    if(postionHour == 0){
                        Calendar c = Calendar.getInstance();
                        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                        int m = c.get(Calendar.MINUTE) ;
                        c.set(Calendar.MINUTE,m+20);
                        int norMinute=c.get(Calendar.MINUTE);
                        setNumericWheelAdapter(minute,adapterMinute,norMinute,60,"分");
                        minute.setCurrentItem(0,true);
                    }else{
                        CharSequence csPosMinute = ((NumericWheelAdapter)minute.getViewAdapter()).getItemText(postionMinute);
                        setNumericWheelAdapter(minute,adapterMinute,0,60,"分");
                        minute.setCurrentItem(Integer.parseInt(csPosMinute.toString()));
                    }
                }else{
                    CharSequence csPosMinute = ((NumericWheelAdapter)minute.getViewAdapter()).getItemText(postionMinute);
                    setNumericWheelAdapter(minute,adapterMinute,0,60,"分");
                    minute.setCurrentItem(Integer.parseInt(csPosMinute.toString()));
                }


            }
            if(wheel == minute){
                //ToastUtil.shortToast(mContext,"minute");
            }
        }
    };


    private void setNumericWheelAdapter(WheelView wheelView,NumericWheelAdapter adapter,int min,int max,String label){
        //NumericWheelAdapter adapter=new NumericWheelAdapter(mContext, min , max, "%02d");
        adapter.updateAdapter(min , max, "%02d");
        adapter.setLabel(label);
        wheelView.setViewAdapter(adapter);
        wheelView.setCyclic(false);
        wheelView.addScrollingListener(scrollListener);
    }

    /**
     * 获取今天往后3天的日期（年月日）
     */
    public static  List<String> get30date() {
        List<String > dates = new ArrayList<String>();

        for (int i = 0; i < 3; i++) {
            if(i==0){
                dates.add("今天");
            }else{
                Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                int mDay = c.get(Calendar.DAY_OF_MONTH) ;
                c.set(Calendar.DAY_OF_MONTH,mDay+i);
                int month = c.get(Calendar.MONTH)+1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                String wek = getWeekString(c.get(Calendar.DAY_OF_WEEK));
                dates.add(month+"月"+day+"日"+" "+wek);
            }
        }
        return dates;
    }

    public static String getWeekString(int dayOfWeek) {
        String mWay = String.valueOf(dayOfWeek);
        if ("1".equals(mWay)) {
            mWay = "周天";
        } else if ("2".equals(mWay)) {
            mWay = "周一";
        } else if ("3".equals(mWay)) {
            mWay = "周二";
        } else if ("4".equals(mWay)) {
            mWay = "周三";
        } else if ("5".equals(mWay)) {
            mWay = "周四";
        } else if ("6".equals(mWay)) {
            mWay = "周五";
        } else if ("7".equals(mWay)) {
            mWay = "周六";
        }
        return mWay;
    }


    private void showTime() {
        int dPosition = day.getCurrentItem();
        CharSequence h = ((NumericWheelAdapter)hour.getViewAdapter()).getItemText(hour.getCurrentItem());
        CharSequence m = ((NumericWheelAdapter)minute.getViewAdapter()).getItemText(minute.getCurrentItem());

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int mDay = c.get(Calendar.DAY_OF_MONTH) ;
        c.set(Calendar.DAY_OF_MONTH,mDay+dPosition);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);

        /**
         * 考虑到跨年的时间,这里要做一步判断,判断选择的时间与当前时间的毫秒值大小,如果选择时间小于当前时间则年份加一,否则不处理
         *
         * yyyy-MM-dd HH:mm:ss
         */
        String time=year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day)+" "+h+":"+m+":00";
        if(TimeUtils.getIntervalByNow(time, ConstUtils.TimeUnit.MSEC)<0){
            year++;
        }
        mListener.changeTime(month+"月"+day+"日"+h+"时"+m+"分",year+"-"+String.format("%02d", month)+
                "-"+String.format("%02d", day)+" "+h+":"+m+":00");
    }

    @Override
    protected int getAnimStyle() {
        return R.style.AnimDialogBottomEntry;
    }

    private OnTimeChangeListener mListener;

    public void setOnSexChangeListener(OnTimeChangeListener listener) {
        mListener = listener;
    }

    public interface OnTimeChangeListener {

        void changeTime(String time,String timeAll);
        void rightNow();
    }
}
