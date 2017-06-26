package com.eme.mas.customeview.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.FootMarkAdapter;
import com.eme.mas.customeview.coverflow.TwoWayAdapterView;
import com.eme.mas.customeview.coverflow.TwoWayGallery;
import com.eme.mas.data.sql.DataRow;
import com.eme.mas.utils.Util;

import java.util.List;


/**
 * 商品详情足迹弹出框
 * <p/>
 * Created by zulei on 16/8/4.
 */
public class ProFootMarkDialog extends BaseDialog implements TwoWayAdapterView.OnItemClickListener, TwoWayAdapterView.OnItemSelectedListener {

    private FootMarkAdapter mFootMakrAdapter;
    private List<DataRow> mFootData;

    private TextView tvPageNum;
    private int pageSize;

    /**
     * 构造函数
     */
    public ProFootMarkDialog(Context context,List<DataRow> data) {
        mFootData = data;
        mFootMakrAdapter = new FootMarkAdapter(context, mFootData, R.layout.page_foot_mark);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_foot_mark;
    }

    @Override
    protected void showView(View view) {
        view.findViewById(R.id.dialog_cs_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        //TODO 这里对控件事件进行控制
//        ViewPager vpFootMark= (ViewPager) view.findViewById(R.id.vp_foot_mark);
//        vpFootMark.setPageMargin(Util.dipToPixels(mContext, 20));
//        vpFootMark.setOffscreenPageLimit(3);
        TwoWayGallery gallery = (TwoWayGallery) view.findViewById(R.id.gallery_horizontal);
        gallery.setAdapter(mFootMakrAdapter);
        gallery.setOnItemClickListener(this);
        gallery.setOnItemSelectedListener(this);
        pageSize = mFootData.size();
        tvPageNum = (TextView) view.findViewById(R.id.tv_page_num);
    }

    @Override
    protected int getAnimStyle() {
        return R.style.AnimDialogUpEntry;
    }

    @Override
    protected int getWidth() {
        return Util.getScreenWidth();
    }

    @Override
    protected int getHeight() {
        return Util.getScreenHeight() - Util.getStatusBarHeight();
    }

    @Override
    public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
        mListener.skip(mFootData.get(position).getString("product_id"));
    }

    @Override
    public void onItemSelected(TwoWayAdapterView<?> parent, View view, int position, long id) {
        position++;
        tvPageNum.setText(TextUtils.concat("(" + position + "/" + pageSize + ")"));
    }

    @Override
    public void onNothingSelected(TwoWayAdapterView<?> parent) {

    }

    private ProFootMarkDialog.OnSkipListener mListener;

    public void setOnSkipListener(ProFootMarkDialog.OnSkipListener listener) {
        mListener = listener;
    }

    public interface OnSkipListener {

        void skip(String product_id);

    }

}
