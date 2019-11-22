package com.mei.zhuang.controller.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.order.EsCoreLog;
import com.mei.zhuang.service.order.IShopLogService;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.order.EsCoreLogParam;
import com.mei.zhuang.vo.order.ExportParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Tiger
 * @Date: 2019-06-21 18:00
 * @Description:
 */
@Slf4j
@Api(value = "系统操作日志管理", description = "", tags = {"系统操作日志管理"})
@RestController
@RequestMapping("/api/log")
public class ShopLogController {

    @Resource
    private IShopLogService shopLogService;

    @SysLog(MODULE = "系统操作日志管理", REMARK = "根据条件查询所有操作列表")
    @ApiOperation("根据条件查询所有操作列表")
    @PostMapping(value = "/list")
    public Object getOrderByPage(EsCoreLogParam entity) {
        try {
            if (entity.getKeyword() != null) {
                entity.setKeyword(entity.getKeyword().trim());
            }
            Page<EsCoreLog> logList = shopLogService.selecPageList(entity);
            return new CommonResult().success(logList);
        } catch (Exception e) {
            log.error("根据条件查询所有订单列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "系统操作日志管理", REMARK = "根据条件导出所有操作日志列表")
    @ApiOperation("根据条件导出所有操作日志列表")
    @PostMapping(value = "/exportLogList")
    public Object exportLogList(EsCoreLogParam entity, ExportParam exportParam, HttpServletResponse response) {
        try {
            boolean exportFlag = shopLogService.exportLogList(entity, exportParam, response);
            if (exportFlag) {
                return new CommonResult().success("导出成功！");
            }
        } catch (Exception e) {
            log.error("根据条件导出所有操作日志列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("导出失败");

    }

    @SysLog(MODULE = "系统操作日志管理", REMARK = "根据条件查询所有日志操作类型列表")
    @ApiOperation("根据条件查询所有日志操作类型列表")
    @PostMapping(value = "/getLogTypeList")
    public Object getLogTypeList() {
        try {
            return new CommonResult().success(shopLogService.getLogTypeList());
        } catch (Exception e) {
            log.error("根据条件查询所有日志操作类型列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


}
