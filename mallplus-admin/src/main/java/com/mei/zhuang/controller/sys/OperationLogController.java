package com.mei.zhuang.controller.sys;

import com.arvato.admin.biz.OperationLogBiz;
import com.arvato.admin.vo.ExportParam;
import com.arvato.admin.vo.SysOpertionLogParam;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.date.DateUtils;
import com.baomidou.mybatisplus.plugins.Page;
import com.mei.zhuang.entity.sys.CrmOperationLog;
import groovy.util.logging.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2019/7/9.
 */
@Slf4j
@Api(value = "系统操作日志管理", description = "", tags = {"操作日志"})
@RestController
@RequestMapping("/api/sys/log")
public class OperationLogController {

    //    private static
    @Autowired
    private OperationLogBiz operationLogBiz;

    @SysLog(MODULE = "系统用户模块", REMARK = "根据条件查询所有操作日志列表")
    @ApiOperation("根据条件查询所有操作日志列表")
    @PostMapping(value = "/list")
    @ResponseBody
    public Object list(SysOpertionLogParam entity) {
        try {
            if (entity.getKeyword() != null) {
                entity.setKeyword(entity.getKeyword().trim());
            }
            Page<CrmOperationLog> logList = operationLogBiz.selecPageList(entity);
            return new CommonResult().success(logList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "系统操作日志管理", REMARK = "根据条件导出所有操作日志列表")
    @ApiOperation("根据条件导出所有操作日志列表")
    @PostMapping(value = "/exportLogList")
    public Object exportLogList(SysOpertionLogParam entity, ExportParam exportParam, HttpServletResponse response) {
        try {
              /*if (ValidatorUtils.empty(exportParam.getColumns())) {
                return new CommonResult().failed("需要导出的字段不能为空！");
            }*/
            exportParam.setFileName(DateUtils.format(new Date(),DateUtil.YYYY_MM_DD) + "操作日志导出列表");
            exportParam.setSheetName("系统操作日志导出列表");
            exportParam.setPath("D:/A");
            exportParam.setHeaders("编号,店员,操作类型,操作内容,操作IP,操作时间");
            exportParam.setColumns("id,userName,module,method,ip,addTime");

            boolean exportFlag = operationLogBiz.exportLogList(entity, exportParam, response);
            if (exportFlag) {
                return new CommonResult().success("导出成功！");
            }
        } catch (Exception e) {

        }
        return new CommonResult().failed("导出失败");

    }

    @SysLog(MODULE = "系统操作日志管理", REMARK = "根据条件查询所有日志操作类型列表")
    @ApiOperation("根据条件查询所有日志操作类型列表")
    @PostMapping(value = "/getLogTypeList")
    public Object getLogTypeList() {
        try {
            return new CommonResult().success(operationLogBiz.getLogTypeList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult().failed();
    }

    /**
     * 供开发人员添加模块名称
     * @param typeNames
     * @return
     */
    @SysLog(MODULE = "系统操作日志管理", REMARK = "插入日志模块类型名称")
    @ApiOperation("插入日志模块类型名称")
    @PostMapping(value = "/insModule")
    public Object insertModuleName(String typeNames, String belongModule) {
        try {
            String[] strs = typeNames.split(",");
            return new CommonResult().success(operationLogBiz.insertModuleName(strs,belongModule));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult().failed();
    }



}
