package com.mei.zhuang.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.constant.CommonConstant;


/**
 * Created by Martin on 2017/11/17.
 */
public class Result extends BizResult {

    public static Result success() {
        Result result = new Result();
        result.setCode(CommonConstant.CODE_SUCCESS);
        result.setMsg("操作成功");
        return result;
    }

    public static Result toResult(int code, String msg, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData((JSONObject) JSON.toJSON(object));
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(CommonConstant.CODE_SUCCESS);
        result.setMsg("操作成功");
        result.setData((JSONObject) JSON.toJSON(object));
        return result;
    }

    public static Result success(String msg) {
        Result result = new Result();
        result.setCode(CommonConstant.CODE_SUCCESS);
        result.setMsg(msg);
        return result;
    }

    public static Result failed() {
        Result result = new Result();
        result.setCode(CommonConstant.CODE_BIZ_ERROR);
        result.setMsg("操作失败");
        return result;
    }

    public static Result failed(String msg) {
        Result result = new Result();
        result.setCode(CommonConstant.CODE_BIZ_ERROR);
        result.setMsg(msg);
        return result;
    }

    public static Result failed(Object object) {
        Result result = new Result();
        result.setCode(CommonConstant.CODE_BIZ_ERROR);
        result.setMsg("操作失败");
        result.setData((JSONObject) JSON.toJSON(object));
        return result;
    }

    public static Result databaseUpdate(Integer updateResult) {
        if (updateResult != null && updateResult > 0) return Result.success();
        throw new RuntimeException("databaseUpdate failed");
    }

    public static Result databaseUpdate(Integer updateResult, Object successData) {
        if (updateResult != null && updateResult > 0) return Result.success(successData);
        throw new RuntimeException("databaseUpdate failed");
    }

    public static boolean isSuccess(BizResult bizResult) {
        return bizResult != null && bizResult.getCode() == 0;
    }

    public static boolean isFailed(BizResult bizResult) {
        return bizResult == null || bizResult.getCode() != 0;
    }

    public static void failedThrow(BizResult bizResult) throws Exception {
        if (isFailed(bizResult)) throw new Exception(bizResult.getMsg() == null ? "操作失败" : bizResult.getMsg());
    }

    public static void failedThrow(boolean result) throws Exception {
        if (!result) throw new Exception("操作失败");
    }

    public static void failedThrowRuntime(BizResult bizResult) {
        if (isFailed(bizResult)) throw new RuntimeException(bizResult.getMsg() == null ? "操作失败" : bizResult.getMsg());
    }

    public static <T> T nullFailedThrow(T object, String failedMsg) {
        if (object == null) throw new RuntimeException(failedMsg);
        return object;
    }

    public static BizResult toResult(boolean ok) {
        return ok ? success() : failed();
    }

    public static BizResult toResult(boolean ok, String faildMsg) {
        return ok ? success() : failed(faildMsg);
    }

    public Result msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public Result okMsg(String msg) {
        if (Result.isSuccess(this)) {
            this.setMsg(msg);
        }
        return this;
    }

}
