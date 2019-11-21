package com.mei.zhuang.controller.marking;


import com.arvato.service.marking.api.service.EsShopFriendPaidService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.marking.EsShopFriendPaid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(value = "朋友代付管理", description = "", tags = {"朋友代付管理"})
@RestController
@RequestMapping("/api/paid")
public class EsShopFriendPaidController {

    @Resource
    private EsShopFriendPaidService esShopFriendPaidService;


    /**
     * 如果有id就修改，如果没有就新增
     *
     * @param entity
     * @return
     */
    @SysLog(MODULE = "朋友代付管理", REMARK = "保存朋友代付")
    @ApiOperation("保存朋友代付")
    @PostMapping("/save")
    public Object save(EsShopFriendPaid entity) {

        try {
            if (entity.getId() == null) {
                return new CommonResult().success("success", esShopFriendPaidService.insert(entity));
            } else {
                return new CommonResult().success("success", esShopFriendPaidService.updateById(entity));
            }

        } catch (Exception e) {
            log.error("朋友代付异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "朋友代付管理", REMARK = "朋友代付详情")
    @ApiOperation("朋友代付详情")
    @PostMapping("/detail")
    public Object detail(EsShopFriendPaid entity) {
        try {
            return new CommonResult().success("success", esShopFriendPaidService.selectOne(new QueryWrapper<>()));
        } catch (Exception e) {
            log.error("朋友代付异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }
}
