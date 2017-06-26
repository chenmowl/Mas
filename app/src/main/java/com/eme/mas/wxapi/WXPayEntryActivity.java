package com.eme.mas.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.eme.mas.MainActivity;
import com.eme.mas.environment.WValue;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, "wxf2292dcd6f698454"); //APPID
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
//		Log.i(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode){
				case WValue.WXAPI_ERROR_CODE_SUCCESS:
					Intent intent=new Intent(this,MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(WValue.FLAG,WValue.MAIN_FRAGMENT_ORDER);
					intent.putExtra("dialog_share",WValue.ONE);
					startActivity(intent);
					break;
				case WValue.WXAPI_ERROR_CODE_MISTAKE://错误  -1
					break;
				case WValue.WXAPI_ERROR_CODE_CANCEL://错误  用户取消
					break;
			}
			Log.i(TAG, "onPayFinish, errCode = " + resp.errCode);
		}
	}
}