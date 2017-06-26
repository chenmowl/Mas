package com.eme.mas.utils;

import android.text.TextUtils;

import com.eme.mas.environment.WValue;


/**
 * Created by dijiaoliang on 16/12/16.
 */
public class StringUtil {

    /**
     * string 判空
     * @param s
     * @return
     */
    public static boolean isEmpty(String s){

        if(!TextUtils.isEmpty(s) && !WValue.NULL_LOWER_CASE.equals(s) && !WValue.NULL_UPPER_CASE.equals(s)){
            return false;
        }else{
            return true;
        }
    }
}
