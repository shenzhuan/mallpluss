package com.mei.zhuang.controller.goods;

import com.arvato.service.goods.api.service.EsShopCustomizedPacketServer;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Api(value = "套装包装列表管理", description = "", tags = {"套装包装列表管理"})
@RestController
@RequestMapping("/shop/package")
public class EsShopCustomizedPacketController {

    @Resource
    private EsShopCustomizedPacketServer esShopCustomizedPacketServer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SysLog(MODULE = "套装包装列表管理", REMARK = "查询套装包装列表")
    @ApiOperation("查询套装包装列表")
    @PostMapping("/list")
    private Object selPageList(EsShopCustomizedPacket entity) {
        try {
            if (ValidatorUtils.empty(entity.getType())) {
                return new CommonResult().failed("请指定类型");
            }
            return new CommonResult().success("success", esShopCustomizedPacketServer.selPageList(entity));
        } catch (Exception e) {
            log.error("查询套装包装列表异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "套装包装列表管理", REMARK = "新增套装包装")
    @ApiOperation("新增套装包装")
    @PostMapping("/save")
    private Object save(EsShopCustomizedPacket entity) {
        try {
            if (ValidatorUtils.empty(entity.getType())) {
                return new CommonResult().failed("请指定类型");
            }
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("请指定名称");
            }
            String time = sdf.format(new Date());
            entity.setCreateTime(sdf.parse(time));
            return new CommonResult().success("success", esShopCustomizedPacketServer.insert(entity));
        } catch (Exception e) {
            log.error("查询新增套装包装异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "套装包装列表管理", REMARK = "查询套装包装详情")
    @ApiOperation("查询套装包装详情")
    @PostMapping("/detail")
    private Object detail(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedPacketServer.selectById(id));
        } catch (Exception e) {
            log.error("查询查询套装包装详情异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "套装包装列表管理", REMARK = "修改套装包装")
    @ApiOperation("修改套装包装")
    @PostMapping("/update")
    private Object update(EsShopCustomizedPacket entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedPacketServer.updateById(entity));
        } catch (Exception e) {
            log.error("修改套装包装异常：", e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "套装包装列表管理", REMARK = "删除套装包装")
    @ApiOperation("删除套装包装")
    @PostMapping("/delete")
    private Object delete(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success", esShopCustomizedPacketServer.deleteById(id));
        } catch (Exception e) {
            log.error("删除套装包装异常：", e);
            return new CommonResult().failed();
        }
    }
}
