package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eme.mas.R;
import com.eme.mas.adapter.MyAddressAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.ILocation;
import com.eme.mas.customeview.dialog.DeleteDialog;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.MyAddressResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.MyAddressBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

/**
 * 我的地址
 * Created by zulei on 16/7/28.
 */
@WLayout(layoutId = R.layout.activity_my_address)
public class MyAddressActivity extends BaseActivity {

    private final static String TAG = MyAddressActivity.class.getSimpleName();

    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoading;
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private ILocation iLocation;
    private MyAddressAdapter myAddressadapter;
    private List<MyAddressBo> myAddressBoList;
    private int deleteFlag;
    private View viewNoData,viewNetErr;

    private String enterFlag;

    private final static int ADDRESS_ADD = 3001;
    private final static int ADDRESS_EDIT = 3002;
    private final static int ADDRESS_DELETE = 3003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataOnline();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) iLocation).cancelRequestByTag(TAG);
    }

    private void initView() {

        //获取网络数据
        iLocation = mController.getLocation(this);
        String flag=getIntent().getStringExtra(WValue.FLAG);
        if(!TextUtils.isEmpty(flag) && WValue.ORDER_COMMIT.equals(flag)){
            enterFlag=WValue.ORDER_COMMIT;
        }else{
            enterFlag=WValue.STRING_EMPTY;
        }
        barTitle.setText(R.string.my_address_title);
        barRightTitle.setText(R.string.my_address_title_add);

        //设置recycleview显示模式
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置recycleview添加删除动画 引用jp.wasabeef:recyclerview-animators:2.2.3
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

        //初始化空数据页面
        initEmptyView();
        //初始化网络异常页面
        iniNetErrView();

        deleteFlag = -1;
        //初始化Adapter
        myAddressBoList = new ArrayList<>();
        myAddressadapter = new MyAddressAdapter(myAddressBoList);
        //设置item加载动画显示模式，true只显示1次，false每次都显示
        myAddressadapter.isFirstOnly(true);
        //设置item加载动画样式
        myAddressadapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置空页面，显示时无需主动调用，无数据时自动加载
        myAddressadapter.setNoDataView(viewNoData);
        //设置网络异常页面，显示时需手动调用showNetErrView
        myAddressadapter.setNetErrView(viewNetErr);
        //设置Adapter
        recyclerView.setAdapter(myAddressadapter);

    }

    private void initEmptyView(){
        viewNoData = getLayoutInflater().inflate(R.layout.layout_no_data_full, (ViewGroup) recyclerView.getParent(), false);
        viewNoData.findViewById(R.id.rl_no_data).setVisibility(View.VISIBLE);
        viewNoData.findViewById(R.id.btn_stroll_nodata).setVisibility(View.GONE);
        ((TextView)viewNoData.findViewById(R.id.tv_no_data_hint)).setText(R.string.my_address_hint_no_data);
        ((ImageView)viewNoData.findViewById(R.id.iv_no_data_pic)).setImageResource(R.mipmap.address_wudizhi);
    }

    private void iniNetErrView(){
        viewNetErr = getLayoutInflater().inflate(R.layout.no_network_loading_full, (ViewGroup) recyclerView.getParent(), false);
        viewNetErr.findViewById(R.id.rl_no_network).setVisibility(View.VISIBLE);
        Button btnScroll = (Button)viewNetErr.findViewById(R.id.btn_stroll);
        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataOnline();
            }
        });
    }

    private void initListener() {
        //设置recycleview点击事件
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            //item点击事件
            @Override
            public void SimpleOnItemClick(RecyclerView.Adapter adapter, View view, int position) {
                switch (enterFlag){
                    case WValue.ORDER_COMMIT:
                        MyAddressBo myAddressBo=myAddressBoList.get(position);
                        Intent intent=new Intent();
                        intent.putExtra(WValue.MYADDRESSBO,myAddressBo);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
            //item中子控件点击事件
            @Override
            public void onItemChildClick(final RecyclerView.Adapter adapter, View parent, View view, final int position) {
                super.onItemChildClick(adapter,parent, view, position);
                final BaseQuickAdapter ad = (BaseQuickAdapter) adapter;
                final MyAddressBo myAddressBo = (MyAddressBo) ad.getItem(position);
                switch (view.getId()) {
                    case R.id.ll_item_my_address_edit:
                        toAddOrUpdateAddressActivity(myAddressBo);
                        break;
                    case R.id.ll_item_my_address_delete:
                        openDeleteDialog(myAddressBo,position);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getDataOnline() {
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoading,false);
            iLocation.getMyAddressList("", TAG);
        } else {
            ToastUtil.shortToast(this, R.string.net_error);
        }
    }

    private void toAddOrUpdateAddressActivity(MyAddressBo myAddressBo){
        Intent intent = new Intent(MyAddressActivity.this, AddOrUpdateAddressActivity.class);
        intent.putExtra(WValue.FLAG, ADDRESS_EDIT);
        intent.putExtra("model_address", myAddressBo);
        startActivityForResult(intent, ADDRESS_EDIT);
    }

    private void openDeleteDialog(MyAddressBo myAddressBo,int position){
        final DeleteDialog dDialog = new DeleteDialog();
        final String addressID = myAddressBo.getAddress_id();
        deleteFlag = position;
        dDialog.setDeleteListener(new DeleteDialog.OnDeleteListener() {
            @Override
            public void cancel() {
                dDialog.cancel();
            }

            @Override
            public void ok() {
                deleteMyAddress(addressID);
                dDialog.cancel();
            }
        });
        dDialog.showDialog(MyAddressActivity.this, null);
    }

    private void deleteMyAddress(String addressID){
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoading,false);
            iLocation.deleteMyAddress(addressID, TAG);
        } else {
            ToastUtil.shortToast(this, R.string.net_error);
        }
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.bar_right_title)
    public void addNewAddress() {
        Intent intent = new Intent(this, AddOrUpdateAddressActivity.class);
        intent.putExtra(WValue.FLAG, ADDRESS_ADD);
        startActivityForResult(intent, ADDRESS_ADD);
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoading,true);
        String action = result.getAction();
        if (WAction.DELETE_ADDRESS.equals(action)) {
            deleteFlag = -1;
            ToastUtil.shortToast(this, "地址删除失败，请稍后重试");
        }
        if (WAction.GET_ADDRESS.equals(action)) {
            //显示网络异常页面
            myAddressadapter.showNetErrView();
        }
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch (result.getAction()){
           case WAction.GET_ADDRESS:
               MyAddressResult myAddressResult = (MyAddressResult) result;
               List<MyAddressBo> bos = myAddressResult.getData();
               if(bos!=null&&bos.size()!=0){
                   myAddressBoList.addAll(bos);
                   myAddressadapter.getData().clear();
                   myAddressadapter.addData(bos);
               }else{
                   myAddressadapter.addData(bos);
               }
               break;
           case WAction.DELETE_ADDRESS:
//               myAddressBoList.remove(deleteFlag);
               myAddressadapter.remove(deleteFlag);
               ToastUtil.shortToast(this, R.string.toast_delete_address_ok);
               break;
        }
        isHideLayer(llAvLoading,true);
    }

}