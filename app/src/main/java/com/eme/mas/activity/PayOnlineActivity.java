package com.eme.mas.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eme.mas.R;
import com.eme.mas.ali.PayOrderInfoUtil2_0;
import com.eme.mas.ali.PayResult;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.model.Result;
import com.eme.mas.model.WXPrepareInfoResult;
import com.eme.mas.model.entity.WXPrepareBo;
import com.eme.mas.utils.ToastUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 在线支付
 * Created by zulei on 16/8/13.
 */
@WLayout(layoutId = R.layout.activity_pay_online)
public class PayOnlineActivity extends BaseActivity {

    private final static String TAG = PayOnlineActivity.class.getSimpleName();

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;
    @Bind(R.id.btn_stroll)
    Button btnStroll;


    //ID
    public static final String APPID = "2088111194656671";
    // 商户收款账号
    public static final String SELLER = "ipmphexam@163.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKPx4MPX816nc77Fep8ZZ5zx7sWYCBY4f29cgauKLYeVzT+H+o1Zm7R62J8jjmDJIclptbFlNELAZRgu/rNnPX7u36w5Katb0oj4mohb2fzOlnxJAqVAZSzZoKf8lvZZkFMqEf8iJvdALV8axQ9eShlbiUtFsPsv+D8rWnKb///lAgMBAAECgYBpa7nadVy7evYEsDu/XrivxSqCyNY6Y64eT9/aDIpmaz+GIMxOZFapYW02N8jwF6WwPFdncOqAGfNn76A8gKCbnTrFjMmfo18cGBAaguFioNxuq/NaoCEVCszVIzmXOoXd1QQjmqmdtAI+Oic1GkekAD1KA+5v+sTd2fUGCW9b6QJBANFQd+bODAK5TnPBSZ2ZUpuXYvh6C/pf5Tn94gzZPAJHBzuLaPj4dCGj8NryMn7kQnZwcQlsQ2HqTlgQTAhxs5sCQQDIguFR1yrH2RIjrTAY4Ccqm2EjttwyD5mC4QPTPxFRXOdRNA1LAqaPtXVqQLjgiQvzurFuKcujNeV17ahH1RJ/AkEArGwe+Q/OsQe46n3UiHBerjDcpFbVOyB3kXfkGDVyy2V7Pb+vbzgrYfLMW2+bzQ92+F3Tn7GBKE18z+pM17/h9QJAV/xGjzkRxrxUH1+h2WiTBzlDeP6GxVjfDfVbTllAaimQP6vNoXq6SBliOovIFI9OtVbZAPRKLYY6+HKfA1ZWVQJBAJseqmFDl5/6y8L/Rq9aVoTr+aO3sZSCRrroWMUcCJfBIqsHn05LC9Ey4XoTh5W6knUzREiI6hiFvHRu7I3DZdQ=";

    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private IOrder iOrder;
    private IWXAPI wxApi;
    private static final int SDK_PAY_FLAG = 1;

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) iOrder).cancelRequestByTag(TAG);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        iOrder = mController.getOrder(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText(R.string.pay_online);
        orderId=getIntent().getStringExtra("order_id");
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick({R.id.rl_pay_online_wx, R.id.rl_pay_online_ali})
    public void payWay(View view) {
        switch (view.getId()) {
            case R.id.rl_pay_online_wx:
                if(!TextUtils.isEmpty(orderId)){
                    ToastUtil.shortToast(this,R.string.toast_tip_pay);
                    isHideLayer(llAvLoadingTransparent44, false);
                    iOrder.getPrePayInfo(orderId,"EME-01",1,TAG);
                }else{
                    ToastUtil.shortToast(this,R.string.toast_tip_order_miss);
                }
                break;
            case R.id.rl_pay_online_ali:
                payALi();
                break;
        }
    }

    private void payWX(WXPrepareBo wxPrepareBo) {
        try {
            wxApi = WXAPIFactory.createWXAPI(this, null);
            wxApi.registerApp(wxPrepareBo.getAppId());

            PayReq req = new PayReq();
            req.appId = wxPrepareBo.getAppId();
            req.partnerId = wxPrepareBo.getPartnerId();
            req.prepayId = wxPrepareBo.getPrepayId();
            req.nonceStr = wxPrepareBo.getNonceStr();
            req.timeStamp = wxPrepareBo.getTimeStamp();
            req.packageValue = wxPrepareBo.getPackageValue();
            req.sign = wxPrepareBo.getSign();

//测试支付失败的情况
//            req.nonceStr = wxPrepareBo.getNonceStr()+"1";
//            req.timeStamp = wxPrepareBo.getTimeStamp()+"1";
//            req.packageValue = wxPrepareBo.getPackageValue()+"1";
//            req.sign = wxPrepareBo.getSign()+"1";
            //Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();
            wxApi.sendReq(req);

        } catch (Exception e) {
            Log.e("PAY_GET", "异常：" + e.getMessage());
            Toast.makeText(this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void payALi() {
        if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        // 请配置好如下3个参数
        String seller_id = SELLER;
        String app_id = APPID;
        String rsaKey = RSA_PRIVATE;

        Map<String, String> params = PayOrderInfoUtil2_0.buildOrderParamMap(seller_id, app_id);
        String orderParam = PayOrderInfoUtil2_0.buildOrderParam(params);
        String sign = PayOrderInfoUtil2_0.getSign(params, rsaKey);

        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayOnlineActivity.this);
                String result = alipay.pay(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayOnlineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayOnlineActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayOnlineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private void createPrepareId() {
        new Thread() {
            @Override
            public void run() {
                StringBuilder xml = new StringBuilder();
                //xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                xml.append("<xml>");
                xml.append("<appid>wxf2292dcd6f698454</appid>");
                xml.append("<mch_id>1387998602</mch_id>");
                xml.append("<nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>");
                xml.append("<body>赵鑫的头发</body>");
                xml.append("<out_trade_no>20160806125346</out_trade_no>");
                xml.append("<total_fee>1</total_fee>");
                xml.append("<spbill_create_ip>123.12.12.123</spbill_create_ip>");
                xml.append("<notify_url>http://www.weixin.qq.com/wxpay/pay.php</notify_url>");
                xml.append("<trade_type>APP</trade_type>");
                xml.append("<sign>44ED0DED3807A52C598A3E15522E68E6</sign>");
                xml.append("</xml>");

                try {

                    byte[] xmlbyte = xml.toString().getBytes("UTF-8");

                    System.out.println(xml);

                    URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setDoOutput(true);// 允许输出
                    conn.setDoInput(true);
                    conn.setUseCaches(false);// 不使用缓存
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("Content-Length", String.valueOf(xmlbyte.length));
                    conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");

                    conn.getOutputStream().write(xmlbyte);
                    conn.getOutputStream().flush();
                    conn.getOutputStream().close();


                    if (conn.getResponseCode() != 200)
                        throw new RuntimeException("请求url失败");

                    InputStream is = conn.getInputStream();// 获取返回数据

                    // 使用输出流来输出字符(可选)
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    String string = out.toString("UTF-8");
                    Log.i("info", string);
                    out.close();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44, true);
        String msg=result.getMsg();
        switch (result.getAction()){
            case WAction.WX_PREPAY:
                if(TextUtils.isEmpty(msg)){
                    msg=getText(R.string.toast_prepay_failure).toString().trim();
                }
                break;
        }
        ToastUtil.shortToast(this,msg);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44, true);
        switch (result.getAction()){
            case WAction.WX_PREPAY:
                WXPrepareInfoResult wxPrepareInfoResult = (WXPrepareInfoResult) result;
                WXPrepareBo wxPrepareBo = wxPrepareInfoResult.getData();
                payWX(wxPrepareBo);
                break;
        }
    }
}
