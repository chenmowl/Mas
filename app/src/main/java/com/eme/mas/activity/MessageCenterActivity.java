package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息中心
 * <p>
 * Created by dijiaoliang on 16/8/9.
 */
@WLayout(layoutId = R.layout.activity_message_center)
public class MessageCenterActivity extends BaseActivity {

    @Bind(R.id.bar_title)
    TextView barTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText("消息中心");
//        your_btn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction()==MotionEvent.ACTION_DOWN) return true;
//                if(event.getAction()!=MotionEvent.ACTION_UP) return false;
//
//                doSomething();
//                you_btn.setPressed(true);
//                return true;
//            }
//        });
    }


    @OnClick({R.id.rl_order_notice, R.id.rl_pro_notice, R.id.rl_system_notice,R.id.back})
    void click(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_order_notice:
                startActivity(new Intent(this,MessageDetailActivity.class));
                ToastUtil.shortToast(this,"订单通知");
                break;
            case R.id.rl_pro_notice:
                ToastUtil.shortToast(this,"商品通知");
                break;
            case R.id.rl_system_notice:
                ToastUtil.shortToast(this,"系统消息");
                break;
        }
    }
}
