package com.eme.mas.data.sp;

import android.text.TextUtils;

import com.eme.mas.MasApplication;
import com.eme.mas.data.sql.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 保存历史搜索纪录
 * <p/>
 * Created by dijiaoliang on 16/8/24.
 */
public class SpSearch extends SPBase {

    private static final String regularExpression = "\\.\\|//*//+";
//    private static final String regularExpression = "\\.\\|";
//    private static final String regularExpression = "\\.";

    /**
     * 保存搜索纪录
     *
     * @param info
     */
    public static void saveSearchRecord(String info) {
        String catchInfo = SPBase.getContent(MasApplication.getInstance(), SpConstant.FILE_SEARCH, SpConstant.KEY_SEARCH_RECORD);
        if (catchInfo == null || "".equals(catchInfo)) {
            SPBase.setContent(MasApplication.getInstance(), SpConstant.FILE_SEARCH, SpConstant.KEY_SEARCH_RECORD, info);
        } else {
            String[] arr = StringUtil.splitEx(catchInfo, regularExpression);
            List<String> list = new ArrayList<>(Arrays.asList(arr));
            if (list.contains(info)) {
                list.remove(info);
            }
            list.add(0, info);
            if (list.size() > 10) {
                list.remove(list.size() - 1);
            }
            SPBase.setContent(MasApplication.getInstance(), SpConstant.FILE_SEARCH, SpConstant.KEY_SEARCH_RECORD, TextUtils.join(regularExpression, list.toArray()));
        }
    }

    /**
     * 获取搜索纪录
     *
     * @return
     */
    public static List<String> getSearchRecord() {
        String catchInfo = SPBase.getContent(MasApplication.getInstance(), SpConstant.FILE_SEARCH, SpConstant.KEY_SEARCH_RECORD);
        if (catchInfo == null || "".equals(catchInfo)) {
            return null;
        } else {
            return Arrays.asList(StringUtil.splitEx(catchInfo, regularExpression));
        }
    }

    /**
     * 删除搜索纪录
     */
    public static void deleteSearchRecord() {
        SPBase.setContent(MasApplication.getInstance(), SpConstant.FILE_SEARCH, SpConstant.KEY_SEARCH_RECORD, "");
    }
}
