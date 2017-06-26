package com.eme.mas.connection;


import com.eme.mas.model.Result;

/**
 * Created by luokaiwen on 15/5/6.
 * <p/>
 * 定义Action接口
 */
public interface IAction {

    /**
     * @return Action according to provided index
     */
    String getAction(int index);

    /**
     * @return Method according to provided action
     */
    int getMethod(String action);

    /**
     * @return Method according to provided action
     */
    int getIndex(String action);

    /**
     * @return url according to provided action
     */
    String getUrl(String action);

    /**
     * @return ? extends Result according to provided action
     */
    Class<? extends Result> getResultType(String action);
}
