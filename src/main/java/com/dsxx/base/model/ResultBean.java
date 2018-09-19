package com.dsxx.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 接口返回数据模型
 *
 * @author slm
 * @date 2017/9/18
 */
@ApiModel("ResultBean")
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = -6248298306422072592L;
    /**
     * 表示接口调用成功
     */
    public static final int SUCCESS = 1;
    /**
     * 表示接口调用失败
     */
    public static final int FAIL = -1;
    /**
     * 表示没有权限调用该接口
     */
    public static final int NO_PERMISSION = -2;
    /**
     * 表示未登录或者登录过期
     */
    public static final int NO_LOGIN = -3;
    /**
     * 含义:表示token错误导致解析失败<br>
     */
    public static final int TOKEN_ERROR = -4;

    public static final String NO_LOGIN_MSG = "未登录";
    public static final String NO_PERMISSION_MSG = "没有权限";
    public static final String SUCC_MSG = "成功";
    public static final String FAIL_MSG = "失败";
    @ApiModelProperty(value = "信息")
    private String msg = SUCC_MSG;
    @ApiModelProperty(value = "状态码 1:成功, -1:失败, -2:没有权限, -3:未登录或者登录过期, -4:token错误")
    private int result = SUCCESS;
    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的数据")
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    /**
     * 包装异常信息
     *
     * @param e
     */
    public ResultBean(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.result = FAIL;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "ResultBean{" +
                "msg='" + msg + '\'' +
                ", result=" + result +
                ", data=" + data +
                '}';
    }

    /**
     * 返回成功
     * 建议直接用构造方法
     * @param t
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> ResultBean success(T t){
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setMsg(ResultBean.SUCC_MSG);
        resultBean.setResult(ResultBean.SUCCESS);
        resultBean.setData(t);
        return resultBean;
    }

    /**
     * 返回成功
     * @return
     */
    public static ResultBean success(){
        ResultBean resultBean = new ResultBean<>();
        resultBean.setMsg(ResultBean.SUCC_MSG);
        resultBean.setResult(ResultBean.SUCCESS);
        return resultBean;
    }

    /**
     * 返回失败
     * 失败建议直接抛出异常
     * @param detail
     * @return
     */
    public static ResultBean error(String detail){
        ResultBean<String> resultBean = new ResultBean<>();
        resultBean.setMsg(ResultBean.FAIL_MSG);
        resultBean.setResult(ResultBean.FAIL);
        resultBean.setData(detail);
        return resultBean;
    }
}
