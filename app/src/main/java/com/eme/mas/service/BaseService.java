package com.eme.mas.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.eme.mas.connection.ActionListener;
import com.eme.mas.connection.IActionListener;
import com.eme.mas.controller.Controller;
import com.eme.mas.model.Result;

/**
 * Created by zulei on 16/8/25.
 */
public class BaseService extends Service implements ActionListener<Result>, IActionListener {

    public Controller mController;

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Result response) {

    }

    @Override
    public void onActionSucc(Result result) {

    }

    @Override
    public void onActionFail(Result result) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mController = new Controller();
    }
}
