package com.mei.zhuang.controller.marking;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.ExcelExportUtil;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.service.marking.MemberCouponService;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户发放券
 */
@Slf4j
@Api(value = "用户发放券管理", description = "", tags = {"用户发放券管理"})
@RestController
@RequestMapping("/api/membercoupon")
public class EsMemberCouponController {

    @Resource
    private MemberCouponService memberCouponService;

    public static void main(String[] args) {
        EsMemberCouponController couponController = new EsMemberCouponController();
        EsMemberCoupon entity = new EsMemberCoupon();
        couponController.list(entity, 1, 10);

    }

    @SysLog(MODULE = "用户发放券管理", REMARK = "查询发放记录")
    @ApiOperation("查询发放记录")
    @PostMapping(value = "/lisetmembercoupon")
    public Object list(EsMemberCoupon entity, @RequestParam(value = "current", defaultValue = "1") Integer current,
                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            PageHelper.startPage(current, size);
            return new CommonResult().success(PageInfo.of(memberCouponService.selectMemberCoupon(entity)));
        } catch (Exception e) {
            log.error("查询发放记录：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "发放记录导出", REMARK = "发放记录导出")
    @ApiOperation("发放记录导出")
    @RequestMapping(value = "/excllist", method = RequestMethod.POST)
    public Object excllist(HttpServletRequest request, HttpServletResponse response, EsMemberCoupon entity) {
        try {

            List<Map<String, Object>> maps1 = memberCouponService.selectMemberCoupon(entity);
            String[] title = new String[]{"优惠券类型", "优惠券名称", "用户信息", "获得方式", "获得时间", "状态", "使用时间", "使用订单号"};        //设置表格表头字段
            String[] properties = {"type", "title", "nickname", "froms", "create_time", "status", "used_time", "order_id"};
            ExcelExportUtil excelExport2 = new ExcelExportUtil();
            excelExport2.setData(maps1);
            excelExport2.setHeardKey(properties);
            excelExport2.setFontSize(14);
            excelExport2.setHeardList(title);
            excelExport2.exportExport(request, response);
            return new CommonResult().success("导出成功");
        } catch (Exception e) {
            log.error("发放记录导出：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "用户发放券管理", REMARK = "添加用户发放记录")
    @ApiOperation("添加用户发放记录")
    @PostMapping(value = "/addsave")
    public Object addsave(@RequestParam Long memberId, @RequestParam Integer vouchers) {
        try {
            return new CommonResult().success(memberCouponService.saveadd(memberId, vouchers));
        } catch (Exception e) {
            log.error("添加用户发放记录：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "用户发放券管理", REMARK = "发放记录显示")
    @ApiOperation("发放记录显示")
    @PostMapping(value = "/record")
    public Object record() {
        try {
            return new CommonResult().success(memberCouponService.record());

        } catch (Exception e) {
            log.error("发放记录显示：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

}
