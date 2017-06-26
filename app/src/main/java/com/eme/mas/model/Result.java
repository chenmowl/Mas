package com.eme.mas.model;

/**
 * 接口返回结果
 */
public class Result {

    /**
     * 响应状态码
     */

    public String code;

    public String action;

    public String msg;

    public String success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", action='" + action + '\'' +
                ", msg='" + msg + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
