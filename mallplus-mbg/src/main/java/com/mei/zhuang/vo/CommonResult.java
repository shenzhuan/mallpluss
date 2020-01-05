package com.mei.zhuang.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回对象
 * Created by macro on 2018/4/26.
 */
@Slf4j
public class CommonResult {
    //操作成功
    public static final int SUCCESS = 1;
    //操作失败
    public static final int FAILED = 2;
    private int code;
    private String message;
    private Object data;
    //参数校验失败
    public static final int VALIDATE_FAILED = 102;

    /**
     * 普通成功返回
     *
     * @param data 获取的数据
     */
    public CommonResult success(Object data) {

        this.data = data;
        this.code = SUCCESS;
        this.message = "操作成功";

        return this;
    }
    /**
     * 参数验证失败使用
     *
     * @param msg 错误信息
     */
    public CommonResult validateFailed(String msg) {
        this.code = VALIDATE_FAILED;
        this.message = msg;
        return this;
    }

    /**
     * 普通成功返回
     */
    public CommonResult success() {
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = "操作成功";
        return this;
    }

    /**
     * 普通成功返回
     */
    public CommonResult success(String message, Object data) {
        this.code = SUCCESS;
        this.message = message;
        this.data = data;
        return this;
    }

    /**
     * 返回分页成功数据
     */
    public CommonResult pageSuccess(Page pageInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("pageSize", pageInfo.getSize());
        result.put("totalPage", pageInfo.getTotal());
        result.put("total", pageInfo.getSize());
        result.put("pageNum", pageInfo.getCurrent());
        result.put("list", pageInfo.getRecords());
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = result;
        return this;
    }

    /**
     * 普通失败提示信息
     */
    public CommonResult failed() {
        this.code = FAILED;
        this.message = "操作失败";
        return this;
    }

    public CommonResult failed(Integer code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public CommonResult failed(Integer code, Object data) {
        this.code = code;
        this.data = data;
        return this;
    }


    public CommonResult failed(String message) {
        this.code = FAILED;
        this.message = message;
        return this;
    }

    /**
     * 普通失败提示信息
     */
    public CommonResult paramFailed() {
        this.code = FAILED;
        this.message = "参数失败";
        return this;
    }

    public CommonResult paramFailed(String message) {
        this.code = FAILED;
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
