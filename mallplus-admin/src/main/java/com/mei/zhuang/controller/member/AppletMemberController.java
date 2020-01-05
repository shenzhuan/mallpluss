package com.mei.zhuang.controller.member;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.annotation.IgnoreAuth;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.dao.member.EsMemberAddressMapper;
import com.mei.zhuang.dao.order.EsDeliveryAddresserMapper;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsDeliveryAddresser;
import com.mei.zhuang.service.member.EsMemberService;
import com.mei.zhuang.service.member.impl.RedisUtil;
import com.mei.zhuang.utils.JsonUtils;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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



    @ApiOperation("注册")
    @PostMapping(value = "/user.reg")
    public Object register(@RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String confimpassword,
                           @RequestParam String authCode) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (password == null || "".equals(password)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (confimpassword == null || "".equals(confimpassword)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (authCode == null || "".equals(authCode)) {
            return new CommonResult().validateFailed("手机验证码为空");
        }

        return memberService.register(phone, password, confimpassword, authCode, null);
    }
    @ApiOperation("注册")
    @PostMapping(value = "/simpleReg")
    public Object simpleReg(@RequestParam String phone,
                            @RequestParam String password,
                            @RequestParam String confimpassword,
                            @RequestParam(required = false) String invitecode) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (password == null || "".equals(password)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (confimpassword == null || "".equals(confimpassword)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }


        return memberService.simpleReg(phone, password, confimpassword, invitecode);
    }



    @ApiOperation("修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(@RequestParam String telephone,
                                 @RequestParam String password,
                                 @RequestParam String authCode) {
        return memberService.updatePassword(telephone, password, authCode);
    }

    @ApiOperation(value = "appLogin登录")
    @PostMapping(value = "/appLogin")
    public Object appLogin(@RequestParam String openid,
                           @RequestParam Integer sex,
                           @RequestParam String headimgurl,
                           @RequestParam String unionid,
                           @RequestParam String nickname,
                           @RequestParam String city,
                           @RequestParam Integer source) {

        if (openid == null || "".equals(openid)) {
            return new CommonResult().validateFailed("openid为空");
        }
        try {
            return new CommonResult().success(memberService.appLogin(openid, sex, headimgurl, unionid, nickname, city, source));
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

    }

    @ApiOperation(value = "手机号 密码登录")
    @PostMapping(value = "/user.login")
    public Object login(@RequestParam String phone,
                        @RequestParam String password) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (password == null || "".equals(password)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        try {
            return new CommonResult().success(memberService.login(phone, password));
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

    }


    @ApiOperation(value = "手机号 密码登录")
    @PostMapping(value = "/user.logout")
    public Object logout() {
        try {

            return new CommonResult().success();
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

    }


    @ApiOperation(value = "手机和验证码登录")
    @PostMapping(value = "/user.smslogin")
    public Object loginByCode(@RequestParam String phone,
                              @RequestParam String authCode) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (authCode == null || "".equals(authCode)) {
            return new CommonResult().validateFailed("手机验证码为空");
        }
        try {
            return new CommonResult().success( memberService.loginByCode(phone, authCode));
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

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

    @Autowired
    private EsDeliveryAddresserMapper memberReceiveAddressService;

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/deleteAddress")
    @ResponseBody
    public Object delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = memberReceiveAddressService.deleteById(id);
        if (count>0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/saveAddress")
    @ResponseBody
    public Object update(EsDeliveryAddresser address) {
        int count = 0;

        if (address.getIsDefault() == 1) {
            memberReceiveAddressService.updateStatusByMember(address.getMemberId());
        }
        if (address != null && address.getId() != null) {
            count = memberReceiveAddressService.updateById(address);
        } else {
            count = memberReceiveAddressService.insert(address);
        }
        if (count>0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @IgnoreAuth
    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/listAddress", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "memberId", required = true)Long memberId) {

            List<EsDeliveryAddresser> addressList = memberReceiveAddressService.selectList(new QueryWrapper<EsDeliveryAddresser>().eq("member_id",memberId));
            return new CommonResult().success(addressList);

    }

    @IgnoreAuth
    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/detailAddress", method = RequestMethod.GET)
    @ResponseBody
    public Object getItem(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        EsDeliveryAddresser address = memberReceiveAddressService.selectById(id);
        return new CommonResult().success(address);
    }

    @IgnoreAuth
    @ApiOperation("显示默认收货地址")
    @RequestMapping(value = "/getItemDefautl", method = RequestMethod.GET)
    @ResponseBody
    public Object getItemDefautl(@RequestParam(value = "memberId", required = true)Long memberId) {
        EsDeliveryAddresser q = new EsDeliveryAddresser();
        q.setIsDefault(1);
        q.setMemberId(memberId);
        return new CommonResult().success(memberReceiveAddressService.selectOne(new QueryWrapper<EsDeliveryAddresser>(q)));
    }

    /**
     * @param id
     * @return
     */
    @ApiOperation("设为默认地址")
    @RequestMapping(value = "/address-set-default")
    @ResponseBody
    public Object setDefault(@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
                             @RequestParam(value = "memberId", required = true)Long memberId) {

        memberReceiveAddressService.updateStatusByMember(memberId);

        EsDeliveryAddresser def = new EsDeliveryAddresser();
        def.setId(id);
        def.setIsDefault(1);
        memberReceiveAddressService.updateById(def);

            return new CommonResult().success(1);

    }
}
