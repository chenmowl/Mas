package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.environment.WValue;
import com.eme.mas.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 发票信息
 * Created by zulei on 16/8/3.
 */
@WLayout(layoutId = R.layout.activity_invoice_info)
public class InvoiceInfoActivity extends BaseActivity {

    private final static String TAG = InvoiceInfoActivity.class.getSimpleName();

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.rg_invoice_info_title)
    RadioGroup rgInvoiceInfoTitle;
    @Bind(R.id.rg_invoice_info_content)
    RadioGroup rgInvoiceInfoContent;
    @Bind(R.id.ll_invoice_info_company_name)
    LinearLayout llInvoiceInfoCompanyName;
    @Bind(R.id.rg_invoice_info_type)
    RadioGroup rgInvoiceInfoType;
    @Bind(R.id.et_invoice_info_company_name)
    EditText etInvoiceInfoCompanyName;

    private String type, title, content;

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
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        barTitle.setText(R.string.order_edit_title_invoice_info);
    }

    private void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra(WValue.INVOICE_TYPE);
        title = intent.getStringExtra(WValue.INVOICE_HEAD);
        content = intent.getStringExtra(WValue.INVOICE_CONTENT);
        switch (type) {
            case WValue.INVOICE_TYPE_ORDINARY:
                rgInvoiceInfoType.check(R.id.rb_invoice_info_ordinary);
                break;
            case WValue.INVOICE_TYPE_VAT:
                rgInvoiceInfoType.check(R.id.rb_invoice_info_vat);
                break;
            default:
                rgInvoiceInfoType.check(R.id.rb_invoice_info_ordinary);
                type=WValue.INVOICE_TYPE_ORDINARY;
                break;
        }
        switch (title) {
            case WValue.STRING_EMPTY:
                rgInvoiceInfoTitle.check(R.id.rb_invoice_info_personal);
                etInvoiceInfoCompanyName.setText(WValue.STRING_EMPTY);
                llInvoiceInfoCompanyName.setVisibility(View.GONE);
                title=WValue.INVOICE_HEAD_PERSON;
                break;
            case WValue.INVOICE_HEAD_PERSON:
                rgInvoiceInfoTitle.check(R.id.rb_invoice_info_personal);
                etInvoiceInfoCompanyName.setText(WValue.STRING_EMPTY);
                llInvoiceInfoCompanyName.setVisibility(View.GONE);
                break;
            default://公司项
                rgInvoiceInfoTitle.check(R.id.rb_invoice_info_company);
                llInvoiceInfoCompanyName.setVisibility(View.VISIBLE);
                etInvoiceInfoCompanyName.setText(title);
                break;
        }
        switch (content) {
            case WValue.INVOICE_CONTENT_WINE:
                rgInvoiceInfoContent.check(R.id.rb_invoice_info_wine);
                break;
            case WValue.INVOICE_CONTENT_DETAIL:
                rgInvoiceInfoContent.check(R.id.rb_invoice_info_detail);
                break;
            default:
                rgInvoiceInfoContent.check(R.id.rb_invoice_info_wine);
                content = WValue.INVOICE_CONTENT_WINE;
                break;
        }
    }

    private void initListener() {
        rgInvoiceInfoTitle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_invoice_info_personal:
                        etInvoiceInfoCompanyName.setText(WValue.STRING_EMPTY);
                        llInvoiceInfoCompanyName.setVisibility(View.GONE);
                        title = WValue.INVOICE_HEAD_PERSON;
                        break;
                    case R.id.rb_invoice_info_company:
                        llInvoiceInfoCompanyName.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        rgInvoiceInfoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_invoice_info_ordinary) {
                    type=WValue.INVOICE_TYPE_ORDINARY;
                } else if (checkedId == R.id.rb_invoice_info_vat) {
                    type=WValue.INVOICE_TYPE_VAT;
                }
            }
        });

        rgInvoiceInfoContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_invoice_info_wine) {
                    content = WValue.INVOICE_CONTENT_WINE;
                } else if (checkedId == R.id.rb_invoice_info_detail) {
                    content = WValue.INVOICE_CONTENT_DETAIL;
                }
            }
        });

    }


    @OnClick(R.id.back)
    public void back() {
        finish();
    }


    @OnClick({R.id.btn_invoice_info_done, R.id.btn_without_invoice})
    public void done(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_invoice_info_done:
                if (!TextUtils.isEmpty(title) && !WValue.INVOICE_HEAD_PERSON.equals(title)) {
                    if(TextUtils.isEmpty(etInvoiceInfoCompanyName.getText().toString().trim())){
                        ToastUtil.shortToast(getApplicationContext(),R.string.toast_edit_company);
                        return;
                    }
                }
                intent.putExtra(WValue.INVOICE_TYPE, type);
                intent.putExtra(WValue.INVOICE_HEAD, title);
                intent.putExtra(WValue.INVOICE_CONTENT, content);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_without_invoice:
                //TODO 这里是不添加发票的点击事件
                intent.putExtra(WValue.INVOICE_TYPE, WValue.STRING_EMPTY);
                intent.putExtra(WValue.INVOICE_HEAD, WValue.STRING_EMPTY);
                intent.putExtra(WValue.INVOICE_CONTENT, WValue.STRING_EMPTY);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @OnTextChanged(R.id.et_invoice_info_company_name)
    public void etChanged(CharSequence text) {
        if(!TextUtils.isEmpty(text.toString()))
        title = text.toString();
    }
}
