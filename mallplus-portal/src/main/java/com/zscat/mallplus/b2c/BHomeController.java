package com.zscat.mallplus.b2c;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.bill.entity.BillAftersales;
import com.zscat.mallplus.cms.entity.CmsTopic;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.sms.entity.SmsCoupon;
import com.zscat.mallplus.sms.entity.SmsCouponHistory;
import com.zscat.mallplus.sms.entity.SmsHomeAdvertise;
import com.zscat.mallplus.sms.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.sms.vo.HomeFlashPromotion;
import com.zscat.mallplus.ums.entity.SysNotice;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.ISysMessageService;
import com.zscat.mallplus.ums.service.ISysNoticeService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.OssAliyunUtil;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.PhoneUtil;
import com.zscat.mallplus.vo.SmsCode;
import com.zscat.mallplus.vo.UmsMemberInfoDetail;
import com.zscat.mallplus.vo.home.Configs;
import com.zscat.mallplus.vo.home.Pages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * 首页内容管理Controller
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
@Slf4j
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
public class BHomeController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RedisService redisService;
    @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Autowired
    private IOmsOrderService orderService;
    @Resource
    private ISmsCouponService couponService;

    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Resource
    private ISysNoticeService noticeService;
    @Resource
    private ISysMessageService messageService;


    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/pages.getpageconfig", method = RequestMethod.POST)
    public Object contentNew() {
        Pages contentResult = advertiseService.contentNew();
        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/api/common/jshopconf", method = RequestMethod.GET)
    public Object jshopconf() {

        return new Configs();
    }
    @IgnoreAuth
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

        return memberService.register(phone, password, confimpassword, authCode);
    }
    @IgnoreAuth
    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/userLogin")
    public Object login(UmsMember umsMember) {
        if (umsMember == null) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        try {
            Map<String, Object> token = memberService.login(umsMember.getUsername(), umsMember.getPassword());
            if (token.get("token") == null) {
                return new CommonResult().validateFailed("用户名或密码错误");
            }
            return new CommonResult().success(token);
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }

    }


    @ApiOperation("修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Object updatePassword(@RequestParam String telephone,
                                 @RequestParam String password,
                                 @RequestParam String authCode) {
        return memberService.updatePassword(telephone, password, authCode);
    }

    @IgnoreAuth
    @ApiOperation(value = "appLogin登录")
    @PostMapping(value = "/appLogin")
    public Object appLogin(@RequestParam String openid,
                           @RequestParam Integer sex,
                           @RequestParam String headimgurl,
                           @RequestParam String unionid,
                           @RequestParam boolean status,
                           @RequestParam String nickname,
                           @RequestParam String city,
                           @RequestParam Integer source) {

        if (openid == null || "".equals(openid)) {
            return new CommonResult().validateFailed("openid为空");
        }
        try {

            Map<String, Object> token = memberService.appLogin(openid, sex,headimgurl,unionid,nickname,city,source);
            if (token.get("token") == null) {
                return new CommonResult().validateFailed("用户名或密码错误");
            }
            return new CommonResult().success(token);
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

    }
    @IgnoreAuth
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

            Map<String, Object> token = memberService.login(phone, password);
            if (token.get("token") == null) {
                return new CommonResult().validateFailed("用户名或密码错误");
            }
            return new CommonResult().success(token);
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

    }
    @IgnoreAuth
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

    @IgnoreAuth
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

            Map<String, Object> token = memberService.loginByCode(phone, authCode);
            if (token.get("token") == null) {
                return new CommonResult().validateFailed("用户名或密码错误");
            }
            return new CommonResult().success(token);
        } catch (AuthenticationException e) {
            return new CommonResult().validateFailed("用户名或密码错误");
        } catch (Exception e) {
            return new CommonResult().validateFailed(e.getMessage());
        }

    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/user.info", method = RequestMethod.POST)
    public Object userInfo() {
        UmsMemberInfoDetail detail = new UmsMemberInfoDetail();
        UmsMember umsMember = UserUtils.getCurrentMember();
        if (umsMember!=null && umsMember.getId()!=null){
            umsMember = memberService.getById(umsMember.getId());
            List<SmsCouponHistory> histories = couponHistoryMapper.selectList(new QueryWrapper<SmsCouponHistory>().eq("member_id",umsMember.getId()));
            detail.setHistories(histories);
            detail.setMember(umsMember);
            return new CommonResult().success(detail);
        }
        return new CommonResult().failed();
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @IgnoreAuth
    @ApiOperation("获取验证码")
    @PostMapping(value = "/user.sms")
    public Object sendSmsCode(@RequestParam String phone) {
        try {
            if (!PhoneUtil.checkPhone(phone)) {
                throw new IllegalArgumentException("手机号格式不正确");
            }
            SmsCode smsCode = memberService.generateCode(phone);

            return new CommonResult().success(smsCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed(e.getMessage());
        }
    }


    /**
     * banner
     *
     * @return
     */
    @IgnoreAuth
    @SysLog(MODULE = "home", REMARK = "bannerList")
    @PostMapping("/advert.getAdvertList")
    public Object bannerList(@RequestParam(value = "type", required = false, defaultValue = "10") Integer type) {
        List<SmsHomeAdvertise> bannerList = advertiseService.getHomeAdvertiseList();
        return new CommonResult().success(bannerList);
    }

    /**
     * 获取广告
     *
     * @return
     */
    @IgnoreAuth
    @SysLog(MODULE = "home", REMARK = "获取广告")
    @PostMapping("/advert.getcarousellists")
    public Object advList(@RequestParam(value = "type", required = false, defaultValue = "10") Integer type) {
        List<SmsHomeAdvertise> bannerList = advertiseService.getHomeAdvertiseList();
        return new CommonResult().success(bannerList);
    }
    /**
     * 获取广告
     *
     * @return
     */
    @IgnoreAuth
    @SysLog(MODULE = "home", REMARK = "获取公告列表")
    @PostMapping("/notice.noticeList")
    public Object notice(SysNotice order,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(noticeService.page(new Page<SysNotice>(pageNum, pageSize), new QueryWrapper<>(order).orderByDesc("ctime")));
    }

    @SysLog(MODULE = "pms", REMARK = "查询公告详情信息")
    @IgnoreAuth
    @PostMapping(value = "/notice.noticeInfo")
    @ApiOperation(value = "查询公益详情信息")
    public Object noticeInfo(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SysNotice productResult = noticeService.getById(id);
        return new CommonResult().success(productResult);
    }

    @Autowired
    OssAliyunUtil aliyunOSSUtil;

    @IgnoreAuth
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        return new CommonResult().success(aliyunOSSUtil.upload(file));
    }

    @IgnoreAuth
    @PostMapping("/uploads")
    @ApiOperation("多文件上传文件")
    public Object uploads(@RequestPart("file") MultipartFile[] file) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        if (file != null && file.length > 0) {
            for (int i = 0; i < file.length; i++) {
                stringBuffer.append(aliyunOSSUtil.upload(file[i]) + ",");
            }
        }
        return new CommonResult().success(stringBuffer);
    }

}
