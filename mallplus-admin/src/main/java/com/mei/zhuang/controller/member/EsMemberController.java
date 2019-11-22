package com.mei.zhuang.controller.member;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.service.member.EsMemberService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.query.QueryMemberParma;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 会员管理
 */
@Slf4j
@Api(value = "会员管理", description = "", tags = {"会员管理"})
@RestController
@RequestMapping("/api/member")
public class EsMemberController {



    @Resource
    private EsMemberService memberService;
    private String MbluatURl="https://mbluat.acxiom.com.cn/mbl/member/getCustomer";
    private String InsertURL="https://mbluat.acxiom.com.cn/mbl/cce";

    @SysLog(MODULE = "会员管理", REMARK = "根据条件查询所有会员列表")
    @ApiOperation("根据条件查询所有会员列表")
    @PostMapping(value = "/list")
    public Object getCouponByPage(QueryMemberParma entity,
                                  @RequestParam(value = "current", defaultValue = "1") Integer current,
                                  @RequestParam(value = "size", defaultValue = "5") Integer size
    ) {
        try {
            QueryWrapper<EsMember> entityWrapper = new QueryWrapper<>();
            if (entity.getBuyCount() == 0) {
                entityWrapper.eq("buy_count", 0);
            }
            if (entity.getBuyCount() == 1) {
                entityWrapper.ge("buy_count", 1);
            }
            //成交次数2+
            if (entity.getBuyCount() == 2) {
                entityWrapper.ge("buy_count", 2);
            }
            //成交次数3+
            if (entity.getBuyCount() == 3) {
                entityWrapper.ge("buy_count", 3);
            }
            //查询所有
            if (entity.getBuyCount() == 4) {
                entityWrapper.ge("buy_count", 0);
                entityWrapper.orderByDesc("create_time");
            }
            //成交次数5+
            if (entity.getBuyCount() == 5) {
                entityWrapper.ge("buy_count", 5);
            }
            //成交次数10+
            if (entity.getBuyCount() == 10) {
                entityWrapper.ge("buy_count", 10);
            }
            if (entity.getMobile() != null && !entity.getMobile().equals("")) {
                if (entity.getMobile().length() != 11) {
                    return new CommonResult().failed("手机号码应为11位");
                } else {
                    String regex = "^((13[0-9])|(14[0-9])|(15([0-3]|[5-9]))|(16[0-9])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[0-9]))\\d{8}$";
                    Pattern pattern = Pattern.compile(regex);
                    Boolean matcher = pattern.matcher(entity.getMobile()).matches();
                    if (!matcher) {
                        return new CommonResult().failed("请输入正确的手机号");
                    }
                    entityWrapper.like(true, "mobile", entity.getMobile());
                }
            }
            if (entity.getSex() != null) {
                entityWrapper.like("sex", entity.getSex());
            }
            if (entity.getComeFrom() != null) {
                entityWrapper.like(true, "come_from", entity.getComeFrom());
            }


            return new CommonResult().success(memberService.page(new Page<EsMember>(current, size), entityWrapper));
        } catch (Exception e) {
            log.error("根据条件查询所有会员列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "会员管理", REMARK = "更新会员的订单信息")
    @ApiOperation("更新会员的订单信息")
    @PostMapping(value = "/updateMemberOrderInfo")
    public Object updateMemberOrderInfo() {
        try {
            return new CommonResult().success(memberService.updateMemberOrderInfo());
        } catch (Exception e) {
            log.error("更新会员的订单信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "会员管理", REMARK = "更新会员")
    @ApiOperation("更新会员")
    @PostMapping(value = "/update")
    public Object updateCoupon( EsMember entity) {
        try {
            if (memberService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "会员管理", REMARK = "删除会员")
    @ApiOperation("删除会员")
    @PostMapping(value = "/delete/{id}")
    public Object deleteCoupon(@ApiParam("会员id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员id");
            }
            if (memberService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除会员：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "会员管理", REMARK = "查询会员明细")
    @ApiOperation("查询会员明细")
    @PostMapping(value = "/detail")
    public Object getGoodsCategoryById(@RequestParam("id") Long id) {
        try {
            EsMember newMember = memberService.getById(id);
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员id");
            }

            return new CommonResult().success(newMember);
        } catch (Exception e) {
            log.error("查询会员明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation("查询用户数量")
    @PostMapping(value = "/membercount")
    public Integer membercount() {
       return memberService.membercount();

    }
    @ApiOperation("查询规定的用户数量")
    @PostMapping(value = "/memberselect")
    public List<EsMember> memberselect(@RequestParam("size")Integer size,@RequestParam("current")Integer current) {
        return memberService.memberselect(size,current);

    }

    @ApiOperation("积累用户数量")
    @PostMapping(value = "/memberNumber")
    public Integer memberNumber(@RequestBody TradeAnalyzeParam param) {
        return memberService.memberNumber(param);

    }

    @ApiOperation("手机号查询")
    @PostMapping(value = "/mobileselect")
    public EsMember mobileselect(@RequestParam long memberId) {
        return memberService.getById(memberId);
    }
    @ApiOperation("修改")
    @PostMapping(value = "/updatemeber")
    public boolean updatemeber(@RequestBody EsMember entity) {
        return memberService.updateById(entity);
    }

    @SysLog(MODULE = "小程序会员管理", REMARK = "查询会员明细")
    @ApiOperation("查询会员明细")
    @PostMapping(value = "/miniprogram")
    public EsMiniprogram getByShopId(@RequestParam("shopId") Long shopId) {
        return memberService.getByShopId(shopId);
    }

    @ApiOperation("mq手机")
    @PostMapping(value = "/mqphone")
    public String mqphone() {

        return "success";
    }
}
