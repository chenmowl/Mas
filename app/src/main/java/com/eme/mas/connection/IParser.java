package com.eme.mas.connection;


import com.eme.mas.model.Result;

interface IParser {
    public Result parse(String response, Class<? extends Result> clazz);
}
