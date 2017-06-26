package com.eme.mas.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.eme.mas.connection.WAction;
import com.eme.mas.controller.customeInterface.ILocation;
import com.eme.mas.model.BrotherLocationResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.BrotherLocationBo;

/**
 * Created by zulei on 16/8/25.
 */
public class BrotherLocationService extends BaseService {
    private OnLocationListener onLocationListener;
    private ILocation iLocation;
    private BrotherLocationBo brotherLocationBo;
    private String oid;
    private boolean run;

    /**
     * 注册回调接口的方法，供外部调用
     * @param onLocationListener
     */
    public void setOnLocationListener(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iLocation = mController.getLocation(this);
        startLocation();
    }


    /**
     * 增加get()方法，供Activity调用
     * @return 下载进度
     */
//    public BrotherLocationBo getLocation() {
//        return brotherLocationBo;
//    }

    public void startLocation(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(run){
                    iLocation.getBrotherLocation(oid,"SSS");
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }


    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        run = true;
        oid = intent.getStringExtra("oid");
        return new MsgBinder();
    }

    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         * @return
         */
        public BrotherLocationService getService(){
            return BrotherLocationService.this;
        }
    }

    public interface OnLocationListener {
        void onLocation(BrotherLocationBo brotherLocationBo);
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        if (null != result) {
            String action = result.getAction();
            if (WAction.GET_BROTHER_LOCATION.equals(action)) {
                BrotherLocationResult brotherLocationResult = (BrotherLocationResult) result;
                brotherLocationBo = brotherLocationResult.getData();
            }
            if(onLocationListener != null){
                onLocationListener.onLocation(brotherLocationBo);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        run = false;
    }
}
