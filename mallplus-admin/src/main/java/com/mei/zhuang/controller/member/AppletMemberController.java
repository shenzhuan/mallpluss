package com.mei.zhuang.controller.member;


import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.service.member.EsMemberService;
import com.mei.zhuang.service.member.impl.RedisUtil;
import com.mei.zhuang.utils.JsonUtils;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 小程序会员管理
 */
@Slf4j
@Api(value = "小程序会员管理", description = "", tags = {"小程序会员管理"})
@RestController
@RequestMapping("/applet")
public class AppletMemberController {


    @Resource
    private RedisUtil redisRepository;
    @Resource
    private EsMemberService memberService;


    @SysLog(MODULE = "小程序会员管理", REMARK = "登入会员")
    @ApiOperation("登入会员")
    @PostMapping(value = "/loginByWeixin")
    public Object loginByWeixin(LoginVo entity) {
        return memberService.loginByWeixin(entity);
    }

    @SysLog(MODULE = "小程序会员管理", REMARK = "查询会员明细")
    @ApiOperation("查询会员明细")
    @PostMapping(value = "/getMemberById")
    public EsMember getMemberById(@RequestParam("id") Long id) {
        try {
            EsMember member =  JsonUtils.fromJson(redisRepository.get(String.format(RedisConstant.MEMBER, id + "")),EsMember.class);
            if (ValidatorUtils.empty(member) || ValidatorUtils.empty(member.getId())) {
                member = memberService.getById(id);
                redisRepository.set(String.format(RedisConstant.MEMBER, id + ""), member);
            }
            return member;
        } catch (Exception e) {
            log.error("查询会员明细：%s", e.getMessage(), e);
            return memberService.getById(id);
        }
    }

    @SysLog(MODULE = "小程序会员管理", REMARK = "查询会员明细")
    @ApiOperation("查询会员明细")
    @PostMapping(value = "/detail")
    public EsMember detail(@RequestParam("id") Long id) {
        try {
            EsMember  member = memberService.getById(id);
            return member;
        } catch (Exception e) {
            log.error("查询会员明细：%s", e.getMessage(), e);
            return memberService.getById(id);
        }
    }

    @SysLog(MODULE = "小程序会员管理", REMARK = "更新会员")
    @ApiOperation("更新会员")
    @PostMapping(value = "/updateMemberOrderById")
    public Object updateMemberOrderById(@RequestBody EsMember entity) {
        return memberService.updateById(entity);
    }

    @SysLog(MODULE = "小程序会员管理", REMARK = "绑定手机号")
    @ApiOperation(value = "绑定手机号")
    @PostMapping(value = "/bindPhone")
    public Object bindPhone(@RequestParam String phone,
                            @RequestParam String code,
                            @RequestParam Long memberId) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().paramFailed("用户名或密码错误");
        }
        if (code == null || "".equals(code)) {
            return new CommonResult().paramFailed("手机验证码为空");
        }
        try {
            return new CommonResult().success(memberService.bindPhone(phone, code, memberId));
        } catch (Exception e) {
            return new CommonResult().paramFailed(e.getMessage());
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @SysLog(MODULE = "小程序会员管理", REMARK = "获取验证码")

    @ApiOperation("获取验证码")
    @PostMapping(value = "/sms/codes")
    public Object sendSmsCode(@RequestParam String phone) {
        try {
            /*if (!PhoneUtil.checkPhone(phone)) {
                throw new IllegalArgumentException("手机号格式不正确");
            }*/
            return new CommonResult().success(memberService.generateCode(phone));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed(e.getMessage());
        }
    }

}
