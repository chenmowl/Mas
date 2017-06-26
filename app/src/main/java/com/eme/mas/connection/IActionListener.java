package com.eme.mas.connection;


import com.eme.mas.model.Result;

/**
 * Created by luokaiwen on 15/5/7.
 * <p/>
 * UI界面请求监听
 */
public interface IActionListener {

    public void onActionSucc(Result result);

    public void onActionFail(Result result);
}
