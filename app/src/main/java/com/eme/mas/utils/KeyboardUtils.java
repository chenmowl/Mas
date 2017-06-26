package com.eme.mas.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtils {
    public static void hide(Context c, View focusedView) {
        InputMethodManager inputMethodManager = (InputMethodManager) c
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                focusedView.getWindowToken(), 0);
    }

    public static boolean isShown(Context c) {
        return ((InputMethodManager) c
                .getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();
    }

    public static void show(Context c, View focusedView) {
        ((InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(focusedView, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 打开软键盘
     */
    public static void openInputMethod(EditText et){
        InputMethodManager inputManager =(InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    /**
     * 关闭软键盘
     * @param activity
     */
    public static void closeInputMethod(Activity activity){
        try {
            ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
