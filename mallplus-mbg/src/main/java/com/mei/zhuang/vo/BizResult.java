package com.mei.zhuang.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@ApiModel("接口返回对象")
public class BizResult {

    @ApiModelProperty("接口返回code")
    private int code;
    @ApiModelProperty("接口信息")
    private String msg;
    @ApiModelProperty("接口返回数据")
    private JSONObject data;

    public BizResult() {

    }

    public BizResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizResult(int code, String msg, JSONObject data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean ok() {
        return this.getCode() == 0;
    }

    public boolean error() {
        return this.getCode() != 0;
    }

    public BizResult okMsg(String msg) {
        if (this.ok()) {
            this.setMsg(msg);
        }
        return this;
    }
}
