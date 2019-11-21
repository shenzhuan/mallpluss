package com.mei.zhuang.controller.sys;


import com.arvato.admin.service.ISysDbResourceService;
import com.arvato.common.msg.DictData;
import com.arvato.common.vo.returnformat.BaseResponse;
import com.arvato.utils.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = {"数据源管理CRUD接口"})
@RestController
@RequestMapping("/sysDbResources")
public class SysDbResourceController {

    @Autowired
    private ISysDbResourceService service;

    @SysLog(MODULE = "数据源管理CRUD接口", REMARK = "数据源字典列表")
    @ApiOperation("数据源字典列表")
    @GetMapping("/dict")
    public BaseResponse<List<DictData>> getDictList() {
        return BaseResponse.successResponnse(service.getDictList());
    }
}

