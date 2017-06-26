package com.eme.mas.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.MessageAdapter;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.customeInterface.HomeAddCartInterface;
import com.eme.mas.model.entity.MessageBo;
import com.eme.mas.model.entity.home.HomeMobileVipBo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息详情页面
 * <p/>
 * Created by dijiaoliang on 16/8/9.
 */
@WLayout(layoutId = R.layout.activity_message_detail)
public class MessageDetailActivity extends BaseActivity implements HomeAddCartInterface {

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.rv_message)
    RecyclerView rvMessage;

    private List<MessageBo> messageData;//
    private MessageAdapter vipAdapter;//手机专享适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText("商品提醒");
        barRightTitle.setTextColor(getResources().getColor(R.color.hint_text_gray));
        barRightTitle.setText("清空纪录");

        if (messageData == null) messageData = new ArrayList<>();
        messageData.add(new MessageBo());
        messageData.add(new MessageBo());
        messageData.add(new MessageBo());
        messageData.add(new MessageBo());
        messageData.add(new MessageBo());
        if (vipAdapter == null)
            vipAdapter = new MessageAdapter(MessageDetailActivity.this, messageData, this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MessageDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置Item增加、移除动画
        rvMessage.setItemAnimator(new DefaultItemAnimator());
        rvMessage.setLayoutManager(mLayoutManager);
        rvMessage.setAdapter(vipAdapter);
    }

    @Override
    public void addCart(ImageView iv_checkbox, HomeMobileVipBo productBo) {

    }
}
