package com.eme.mas.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.customeview.CenterImageSpan;


/**
 * Created by dijiaoliang on 16/4/20.
 */
public class ProductUtils {

//    private SpannableString msp;

    /**
     * 处理商品价格
     *
     * @param context
     * @param tv
     * @param price
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void processProductPrice(Context context, TextView tv, String price) {
        int position = price.indexOf(".");
        int position1 = price.indexOf("@");
        SpannableString msp = new SpannableString(price);
//        BitmapFactory.Options op = new BitmapFactory.Options();
//        op.inJustDecodeBounds = true;
//        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
//        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, op);
//        op.inSampleSize = 2;
//        op.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        mBitmap.setHeight(Util.dip2px(context,18));
        mBitmap.setWidth(Util.dip2px(context,18));
        ImageSpan imageSpan = new ImageSpan(context, mBitmap);
        //字体变大
//        msp.setSpan(new AbsoluteSizeSpan(20, true), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new AbsoluteSizeSpan(20, true), position1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体变小
        msp.setSpan(new AbsoluteSizeSpan(12, true), position, position1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(imageSpan, position1, position1 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
    }

    /**
     * 处理商品价格
     *
     * @param tv
     * @param price
     */
    public static void processProductPrice02(TextView tv, String price) {
        int position = price.indexOf(".");
        int position1 = price.indexOf("+");
        int position2 = price.indexOf("积");
        int position3 = price.length();
        SpannableString msp = new SpannableString(price);
//        BitmapFactory.Options op = new BitmapFactory.Options();
//        op.inJustDecodeBounds = true;
//        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
//        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, op);
//        op.inSampleSize = 2;
//        op.inJustDecodeBounds = false;
//        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, op);
//        ImageSpan imageSpan = new ImageSpan(context, mBitmap);
        //字体变大
//        msp.setSpan(new AbsoluteSizeSpan(20, true), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new AbsoluteSizeSpan(20, true), position1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体变小
        if(position!=-1&&position1!=-1){
            msp.setSpan(new AbsoluteSizeSpan(13, true), position, position1+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(position2!=-1&&position3!=-1){
            msp.setSpan(new AbsoluteSizeSpan(13, true), position2, position3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
//        msp.setSpan(imageSpan, position1, position1 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
    }


    /**
     * 处理商品优惠券icon
     *
     * @param context
     * @param tv
     */
    public static void processProductTitle(Context context,TextView tv) {
//        int position = price.indexOf(".");
//        int position1 = price.indexOf("＋");
//        int position2 = price.indexOf("积");
//        int position3 = price.length();
        String info="1"+tv.getText().toString();
        SpannableString msp = new SpannableString(info);
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, op);
        op.inSampleSize = 3;
        op.inJustDecodeBounds = false;
//        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.youhuiquan, op);
        ImageSpan imageSpan = new ImageSpan(context, mBitmap);
        //字体变大
//        msp.setSpan(new AbsoluteSizeSpan(20, true), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new AbsoluteSizeSpan(20, true), position1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体变小
//        if(position!=-1&&position1!=-1){
//            msp.setSpan(new AbsoluteSizeSpan(11, true), position, position1+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        if(position!=-1&&position1!=-1){
//            msp.setSpan(new AbsoluteSizeSpan(11, true), position2, position3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        msp.setSpan(imageSpan,0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
    }

    /**
     * 处理商品优惠券icon
     *
     * @param context
     * @param tv
     */
    public static void processProductTitle02(Context context,TextView tv) {
//        int position = price.indexOf(".");
//        int position1 = price.indexOf("＋");
//        int position2 = price.indexOf("积");
//        int position3 = price.length();
        String info=tv.getText().toString();
        SpannableString msp = new SpannableString(info);
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, op);
        op.inSampleSize = 3;
        op.inJustDecodeBounds = false;
//        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.youhuiquan, op);
        ImageSpan imageSpan = new ImageSpan(context, mBitmap);
        //字体变大
//        msp.setSpan(new AbsoluteSizeSpan(20, true), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new AbsoluteSizeSpan(20, true), position1, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体变小
//        if(position!=-1&&position1!=-1){
//            msp.setSpan(new AbsoluteSizeSpan(11, true), position, position1+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        if(position!=-1&&position1!=-1){
//            msp.setSpan(new AbsoluteSizeSpan(11, true), position2, position3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        msp.setSpan(imageSpan,0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
    }

    /**
     * 给指定textview控件添加手机专享的icon
     * @param context
     * @param tv
     * @param info
     *
     *      //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE、Spanned.SPAN_INCLUSIVE_EXCLUSIVE、Spanned.SPAN_EXCLUSIVE_INCLUSIVE、Spanned.SPAN_INCLUSIVE_INCLUSIVE 几种模式
     *
     */
    public static void addPhoneVipIcon(Context context,TextView tv,String info){
        SpannableStringBuilder span = new SpannableStringBuilder("*" + info);
        CenterImageSpan imageSpan = new CenterImageSpan(context, R.mipmap.cart_iv_phone);
        span.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(span);
    }
}
