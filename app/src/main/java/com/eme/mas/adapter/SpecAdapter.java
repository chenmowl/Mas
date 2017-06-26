package com.eme.mas.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.data.sql.DataRow;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类页面的规格适配器（品牌、香型、产地、价格）
 * <p/>
 * Created by dijiaoliang on 16/7/21.
 */
public class SpecAdapter extends WBaseAdapter<DataRow> {


    private List<DataRow> specIdData;

    private int tv_color_normal;
    private int tv_color_select;

    public SpecAdapter(Context context, List<DataRow> list, int itemLayout) {
        super(context, list, itemLayout);
        tv_color_normal = context.getResources().getColor(R.color.text_color_bar);
        tv_color_select = context.getResources().getColor(R.color.main_color_red);
    }

    @Override
    public void getItemView(int position, View convertView) {
        DataRow spec = getList().get(position);
        String id_spec = (String) spec.get(0);
        TextView tv_spec_name = ViewHolder.get(convertView, R.id.tv_spec_name);
        tv_spec_name.setText(spec.getString(1));
        if (specIdData != null && specIdData.size() != 0) {
            String id;
            for (DataRow row : specIdData) {
                id = (String) row.get(0);
                if (id.equals(id_spec)) {
                    tv_spec_name.setSelected(true);
                    tv_spec_name.setTextColor(tv_color_select);
                    return;
                }
            }
        }
        tv_spec_name.setSelected(false);
        tv_spec_name.setTextColor(tv_color_normal);
    }

    /**
     * 设置临时规格存储容器
     *
     * @param specIds
     */
    public void setSpecRowData(List<DataRow> specIds) {
        if (specIdData == null) {
            specIdData = new ArrayList<>();
        } else {
            specIdData.clear();
        }
        this.specIdData.addAll(specIds);
    }

    /**
     * 获取所有规格
     *
     * @return
     */
    public List<DataRow> getSpecRowData() {
        return specIdData;
    }

    /**
     * 删除指定规格id
     *
     * @param row
     */
    public void deleteSpecRow(DataRow row) {
        if (specIdData != null) {
            for (DataRow r : specIdData) {
                if (r.get(0).equals(row.get(0))) {
                    specIdData.remove(r);
                    return;
                }
            }
//            specIdData.remove(row);
        }
    }

    /**
     * 删除全部规格id
     */
    public void deleteAllSpecRow() {
        if (specIdData != null) {
            specIdData.clear();
        }
    }

    /**
     * 添加指定规格id
     *
     * @param row
     */
    public void addSpecRow(DataRow row) {
        if (specIdData != null) {
            for (DataRow w : specIdData)
                if (w.get(0).equals(row.get(0))) {
                    return;
                }
        }
        specIdData.add(row);
    }
}
