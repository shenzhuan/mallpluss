package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.arvato.admin.biz.ChannelBiz;
import com.mei.zhuang.controller.SysLog;
import com.arvato.utils.constant.CommonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Meng.Liu1@arvato.com
 * @Date: 2018/12/5 14:38
 * @Description: 渠道
 * @version: V1.0
 */
@Api(description = "CRUD-渠道",tags = {"系统渠道信息"})
@RestController
@RequestMapping("/channel")
@Slf4j
public class ChannelController {

    @Resource
    private ChannelBiz channelBiz;

    /**
     * 查询入会渠道
     *
     * @return JSONObject
     */
    @SysLog(MODULE = "CRUD-渠道", REMARK = "查询入会渠道")
    @ApiOperation("查询入会渠道")
    @RequestMapping(value = "/channelList", method = RequestMethod.GET)
    public JSONObject registerChannel() {
        JSONObject result = new JSONObject();
        try {
            result.put("code", CommonConstant.CODE_SUCCESS);
            result.put("data", channelBiz.getChannelList());
        } catch (Exception e) {
            log.error("registerChannel error:[{}]", e);
            result.put("code", CommonConstant.CODE_SYS_ERROR);
            result.put("msg", e);
        }
        return result;
    }
}
