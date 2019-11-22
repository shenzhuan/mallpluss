package com.mei.zhuang.vo.sys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口返回数据类型，返回的数据类型为 T
 *
 * @param <T>
 */
@Getter
@Setter
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 42L;

    public static final int SUCCESS_CODE = 0;
    public static final int EXCEPTION_PARAM_CODE = 1;
    public static final int EXCEPTION_BIZ_CODE = 2;
    public static final int EXCEPTION_SYSTEM_CODE = 3;
    public static final String SUCCESS_MSG = "操作成功";
    public static final String EXCEPTION_PARAM_MSG = "请求参数异常";
    public static final String EXCEPTION_BIZ_MSG = "请求业务异常";
    public static final String EXCEPTION_SYSTEM_MSG = "系统程序异常";
    public static final ReturnT<String> SUCCESS = new ReturnT<>(SUCCESS_CODE, SUCCESS_MSG);
    public static final ReturnT<String> EXCEPTION_PARAM_FAIL = new ReturnT<>(EXCEPTION_PARAM_CODE, EXCEPTION_PARAM_MSG);
    public static final ReturnT<String> EXCEPTION_BIZ_FAIL = new ReturnT<>(EXCEPTION_BIZ_CODE, EXCEPTION_BIZ_MSG);
    public static final ReturnT<String> EXCEPTION_SYSTEM_FAIL = new ReturnT<>(EXCEPTION_SYSTEM_CODE, EXCEPTION_SYSTEM_MSG);

    private int code;
    private String msg;
    private T data;

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnT(T data) {
        this.code = SUCCESS_CODE;
        this.msg = SUCCESS_MSG;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + data + "]";
    }
}
