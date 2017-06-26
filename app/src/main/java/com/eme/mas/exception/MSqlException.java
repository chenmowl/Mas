package com.eme.mas.exception;

/**
 * Created by luokaiwen on 15/5/26.
 * <p/>
 * 数据库异常
 */
public class MSqlException extends Exception {

    public MSqlException() {

    }

    public MSqlException(String message) {
        super(message);
    }
}
