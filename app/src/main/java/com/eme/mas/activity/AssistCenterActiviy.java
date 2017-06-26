package com.eme.mas.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 帮助中心
 * Created by zhaoxin on 16/7/27.
 */
@WLayout(layoutId = R.layout.activity_assist_center)
public class AssistCenterActiviy extends BaseActivity {

    @Bind(R.id.bar_title)
    TextView barTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText(R.string.mine_title_help);
    }
    @OnClick({R.id.back,R.id.ib_call})
    void click(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ib_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "10086-1"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }
}

