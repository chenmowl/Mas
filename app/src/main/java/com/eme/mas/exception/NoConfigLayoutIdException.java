package com.eme.mas.exception;

/**
 * Created by luokaiwen on 15/5/11.
 * <p/>
 * 没有配置LayoutId异常
 */
public class NoConfigLayoutIdException extends WException {

    public NoConfigLayoutIdException() {

        super("请配置LayoutId" +
                "");
    }
}
