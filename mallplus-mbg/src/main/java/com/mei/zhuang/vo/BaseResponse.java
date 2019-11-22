package com.mei.zhuang.vo;

import lombok.Data;

@Data
public class BaseResponse<T> {


    protected static final Integer RESPONSE_SUCCESS_CODE = 0;
    protected static final String RESPONSE_SUCCESS_MSG = "请求成功";

    private Integer code;
    private String msg;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static BaseResponse successResponnse() {
        return new BaseResponse(RESPONSE_SUCCESS_CODE, RESPONSE_SUCCESS_MSG, new Object());
    }


    public static <T> BaseResponse successResponnse(T data) {
        return new BaseResponse(RESPONSE_SUCCESS_CODE, RESPONSE_SUCCESS_MSG, data);
    }

    public static <T> BaseResponse successResponnse(String msg, T data) {
        return new BaseResponse(RESPONSE_SUCCESS_CODE, msg, data);
    }


}
