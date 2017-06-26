package com.eme.mas.connection;


import com.alibaba.fastjson.JSON;
import com.eme.mas.model.Result;
import com.eme.mas.utils.LogUtil;

public class WParser implements IParser {
    private static WParser instance;

    private WParser() {

    }

    public static WParser getInstance() {
        if (instance == null) {
            instance = new WParser();
        }
        return instance;
    }

    @Override
    public Result parse(String raw, Class<? extends Result> clazz) {
        Result result = JSON.parseObject(raw, clazz);

        LogUtil.e("jason",result.action);
        return result;
    }
}
