package com.eme.mas.demo_recycle;


import com.chad.library.adapter.base.BaseQuickSwipeAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eme.mas.R;

import java.util.List;
/**
 * 集成上下拉刷新，侧滑删除的Adapter Demo
 * auth:zulei
 * date:2016-9-5
 */
public class TestAdapter extends BaseQuickSwipeAdapter<String> {

    public TestAdapter(List<String> list) {
        super( R.layout.item_test, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        //给子控件添加点击事件
        helper.addOnClickListener(R.id.btn_iop_do);
        helper.addOnClickListener(R.id.delete);
        //子控件赋值
        helper.setText(R.id.btn_iop_do,"按钮测试"+item);

    }

    //绑定swipe
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.l_swipe;
    }
}
