package com.mei.zhuang.controller.sys;


import com.arvato.admin.constant.SysTenantStatus;
import com.arvato.admin.dto.SysTenantPostData;
import com.arvato.admin.dto.SysTenantPutData;
import com.mei.zhuang.service.sys.ISysTenantService;
import com.arvato.common.dto.SysTenantPagingData;
import com.arvato.common.dto.SysTenantPagingParam;
import com.arvato.common.msg.DictData;
import com.arvato.common.orm.model.SysTenant;
import com.arvato.common.vo.returnformat.BaseResponse;
import com.arvato.common.vo.returnformat.TableData;
import com.mei.zhuang.controller.SysLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"租户管理CRUD接口"})
@RestController
@RequestMapping("/sysTenants")
public class SysTenantController {

    @Resource
    private ISysTenantService service;

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "租户列表")
    @ApiOperation("租户列表")
    @GetMapping
    public BaseResponse<TableData<SysTenantPagingData>> list(SysTenantPagingParam param) {
        return BaseResponse.successResponnse(
                new TableData<>()
                        .total(this.service.getPagingTotal(param))
                        .rows(this.service.getPagingList(param))
        );
    }

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "租户状态字典")
    @ApiOperation("租户状态字典")
    @GetMapping("/status/dict")
    public BaseResponse<List<DictData>> getStatusDictList() {
        return BaseResponse.successResponnse(
                SysTenantStatus.toDictList()
        );
    }

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "新增租户")
    @ApiOperation("新增租户")
    @PostMapping
    public BaseResponse add(SysTenantPostData tenantPostData) {
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPostData.getTenantName()),
                "请填写租户名"
        );
        Assert.isTrue(
                tenantPostData.getDbResourceId() != null,
                "请指定租户数据源"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPostData.getDbSchema()),
                "请填写租户schema"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPostData.getBrandName()),
                "请填写入驻品牌"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPostData.getBrandLogo()),
                "请上传品牌logo"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPostData.getCompanyName()),
                "请填写入驻公司"
        );

        Assert.isTrue(
                this.service.selectCount(new QueryWrapper<>(
                        new SysTenant().setTenantName(tenantPostData.getTenantName())
                )) == 0,
                "租户名称已存在"
        );

        Assert.isTrue(
                this.service.schemaNameCheck(tenantPostData.getDbSchema()),
                "租户schema只能包含数组，字母，下划线"
        );
        Assert.isTrue(
                this.service.selectCount(new QueryWrapper<>(
                        new SysTenant()
                                .setDbResourceId(tenantPostData.getDbResourceId())
                                .setDbSchema(tenantPostData.getDbSchema())
                )) == 0,
                "该数据源下已存在该schema"
        );

        Assert.isTrue(
                this.service.selectCount(new QueryWrapper<>(
                        new SysTenant().setBrandName(tenantPostData.getBrandName())
                )) == 0,
                "该品牌已经入驻"
        );

        SysTenant sysTenant = new SysTenant()
                .setTenantName(tenantPostData.getTenantName())
                .setDbResourceId(tenantPostData.getDbResourceId())
                .setDbSchema(tenantPostData.getDbSchema())
                .setBrandName(tenantPostData.getBrandName())
                .setBrandLogo(tenantPostData.getBrandLogo())
                .setCompanyName(tenantPostData.getCompanyName())
                .setCompanyTel(tenantPostData.getCompanyTel())
                .setCompanyEmail(tenantPostData.getCompanyEmail())
                .setCompanyAddress(tenantPostData.getCompanyAddress())
                .setStatus(SysTenantStatus.ENABLE.getStatus());
        this.service.insert(sysTenant);

        this.service.initTenant(sysTenant);

        return BaseResponse.successResponnse();
    }

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "更新租户")
    @ApiOperation("更新租户")
    @PutMapping
    public BaseResponse update(SysTenantPutData tenantPutData) {
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPutData.getTenantName()),
                "请填写租户名"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPutData.getBrandName()),
                "请填写入驻品牌"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPutData.getBrandLogo()),
                "请上传品牌logo"
        );
        Assert.isTrue(
                StringUtils.isNotBlank(tenantPutData.getCompanyName()),
                "请填写入驻公司"
        );

        Assert.isTrue(
                this.service.selectCount(
                        new QueryWrapper<>(
                                new SysTenant().setTenantName(tenantPutData.getTenantName())
                        ).notIn("id", tenantPutData.getId())
                ) == 0,
                "租户名称已存在"
        );
        Assert.isTrue(
                this.service.selectCount(
                        new QueryWrapper<>(
                                new SysTenant().setBrandName(tenantPutData.getBrandName())
                        ).notIn("id", tenantPutData.getId())
                ) == 0,
                "该品牌已经入驻"
        );

        this.service.updateById(
                new SysTenant()
                        .setId(tenantPutData.getId())
                        .setTenantName(tenantPutData.getTenantName())
                        .setBrandName(tenantPutData.getBrandName())
                        .setBrandLogo(tenantPutData.getBrandLogo())
                        .setCompanyName(tenantPutData.getCompanyName())
                        .setCompanyTel(tenantPutData.getCompanyTel())
                        .setCompanyEmail(tenantPutData.getCompanyEmail())
                        .setCompanyAddress(tenantPutData.getCompanyAddress())
        );


        return BaseResponse.successResponnse();
    }

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "租户状态设置")
    @ApiOperation("租户状态设置")
    @PutMapping("/{id}/status")
    public BaseResponse updateStatus(@PathVariable("id") int id, boolean enable) {

        this.service.updateById(
                new SysTenant()
                        .setId(id)
                        .setStatus(enable ? SysTenantStatus.ENABLE.getStatus() : SysTenantStatus.DISABLE.getStatus())
        );

        return BaseResponse.successResponnse();
    }

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "获取租户详情")
    @ApiOperation("获取租户详情")
    @GetMapping("/{id}")
    public BaseResponse<SysTenant> getById(@PathVariable("id") int id) {
        return BaseResponse.successResponnse(this.service.selectById(id));
    }

    @SysLog(MODULE = "租户管理CRUD接口", REMARK = "租户字典")
    @ApiOperation("租户字典")
    @GetMapping("/dict")
    public BaseResponse<List<DictData>> getById() {
        return BaseResponse.successResponnse(this.service.getDictList());
    }

}

