package com.zscat.mallplus.single;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.sms.entity.SmsCoupon;
import com.zscat.mallplus.sms.entity.SmsCouponHistory;
import com.zscat.mallplus.sms.entity.SmsHomeAdvertise;
import com.zscat.mallplus.sms.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.sms.vo.HomeFlashPromotion;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLocation;
import com.zscat.mallplus.ums.service.IUmsMemberLocationService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.util.OssAliyunUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.PhoneUtil;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApiContext;
import com.zscat.mallplus.vo.Rediskey;
import com.zscat.mallplus.vo.SmsCode;
import com.zscat.mallplus.vo.UmsMemberInfoDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 首页内容管理Controller
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
@Slf4j
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
@RequestMapping("/api/single/home")
public class SingelHomeController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private IPmsProductService pmsProductService;
    @Autowired
    private IUmsMemberLocationService memberLocationService;
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
    @Autowired
    private ApiContext apiContext;
    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public Object userInfo() {
        UmsMemberInfoDetail detail = new UmsMemberInfoDetail();
       UmsMember umsMember = memberService.getNewCurrentMember();
       if (umsMember!=null && umsMember.getId()!=null){
           umsMember = memberService.getById(umsMember.getId());
           List<SmsCouponHistory> histories = couponHistoryMapper.selectList(new QueryWrapper<SmsCouponHistory>().eq("member_id",umsMember.getId()));
           detail.setHistories(histories);
           detail.setMember(umsMember);
           return new CommonResult().success(detail);
       }
        return new CommonResult().failed();
    }
    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public Object content() {
        String key = Rediskey.HOMEPAGEmallplus1 + apiContext.getCurrentProviderId();
        String json = redisService.get(key);
        HomeContentResult contentResult = null;
        try {
            if (ValidatorUtils.empty(json)){
                contentResult = advertiseService.singelContent();
                redisService.set(key,JsonUtils.objectToJson(contentResult));
                redisService.expire(key,3600*5);
            }else{
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), HomeContentResult.class);
            }
        }catch (Exception e){
            contentResult = advertiseService.singelContent();
            redisService.set(key,JsonUtils.objectToJson(contentResult));
            redisService.expire(key,3600*5);
        }
        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/content1", method = RequestMethod.GET)
    public Object content1() {
        String key = Rediskey.HOMEPAGEmallplus2 + apiContext.getCurrentProviderId();
        String json = redisService.get(key);
        HomeContentResult contentResult = null;
        try {
            if (ValidatorUtils.empty(json)){
                contentResult = advertiseService.singelContent1();
                redisService.set(key,JsonUtils.objectToJson(contentResult));
                redisService.expire(key,3600*5);
            }else{
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), HomeContentResult.class);
            }
        }catch (Exception e){
            contentResult = advertiseService.singelContent1();
            redisService.set(key,JsonUtils.objectToJson(contentResult));
            redisService.expire(key,3600*5);
        }
        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("首页秒杀活动")
    @SysLog(MODULE = "home", REMARK = "首页秒杀活动")
    @RequestMapping(value = "/homeFlashPromotionList", method = RequestMethod.GET)
    public Object homeFlashPromotionList() {
        List<HomeFlashPromotion> contentResult = advertiseService.homeFlashPromotionList();
        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("优惠券")
    @SysLog(MODULE = "home", REMARK = "首页秒杀活动")
    @RequestMapping(value = "/couponList", method = RequestMethod.GET)
    public Object couponList() {
        List<SmsCoupon> contentResult = couponService.selectNotRecive();
        return new CommonResult().success(contentResult);
    }

    /**
     * banner
     *
     * @return
     */
    @IgnoreAuth
    @SysLog(MODULE = "home", REMARK = "bannerList")
    @GetMapping("/bannerList")
    public Object bannerList(@RequestParam(value = "type", required = false, defaultValue = "10") Integer type) {
        List<SmsHomeAdvertise> bannerList = advertiseService.getHomeAdvertiseList();
        return new CommonResult().success(bannerList);
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页推荐品牌")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐品牌")
    @GetMapping(value = "/recommendBrand/list")
    public Object getRecommendBrandList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(advertiseService.getRecommendBrandList(1, 1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页新品")
    @IgnoreAuth
    @ApiOperation(value = "查询首页新品")
    @GetMapping(value = "/newProductList/list")
    public Object getNewProductList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(advertiseService.getRecommendBrandList(1, 1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页推荐商品")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐商品")
    @GetMapping(value = "/hotProductList/list")
    public Object getHotProductList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(advertiseService.getHotProductList(1, 1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页推荐文章")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐文章")
    @GetMapping(value = "/recommendSubjectList/list")
    public Object getRecommendSubjectList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(advertiseService.getRecommendSubjectList(1, 1));
    }

    @IgnoreAuth
    @ApiOperation(value = "登录以后返回token")
    @GetMapping(value = "/login")
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

    /* @IgnoreAuth
     @ApiOperation("获取验证码")
     @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
     @ResponseBody
     public Object getAuthCode(@RequestParam String telephone) {
         return memberService.generateAuthCode(telephone);
     }
 */
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
    @PostMapping(value = "/login")
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
    @ApiOperation(value = "手机和验证码登录")
    @PostMapping(value = "/loginByCode")
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
    @ApiOperation("注册")
    @PostMapping(value = "/reg")
    public Object register(@RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String confimpassword,
                           @RequestParam String authCode,
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
        if (authCode == null || "".equals(authCode)) {
            return new CommonResult().validateFailed("手机验证码为空");
        }

        return memberService.register(phone, password, confimpassword, authCode,invitecode);
    }

    @IgnoreAuth
    @ApiOperation("注册")
    @PostMapping(value = "/simpleReg")
    public Object simpleReg(@RequestParam String phone,
                            @RequestParam String password,
                            @RequestParam String confimpassword,@RequestParam(required = false)  String invitecode) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (password == null || "".equals(password)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (confimpassword == null || "".equals(confimpassword)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }


        return memberService.simpleReg(phone, password, confimpassword,invitecode);
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @IgnoreAuth
    @ApiOperation("获取验证码")
    @PostMapping(value = "/sms/codes")
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

    @IgnoreAuth
    @PostMapping("/test")
    @ApiOperation("多文件上传文件")
    public Object test() throws Exception {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

                /* 读入TXT文件 */
            String pathname = "E:\\test\\test.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine(); // 一次读入一行数据
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @IgnoreAuth
    @ApiOperation(value = "登录以后返回token")
    @GetMapping(value = "/logs")
    public Object log(@RequestParam("logs") String logs) {
        System.out.println(logs);
        log.error(logs);
        return new CommonResult().success();

    }

    @RequestMapping(value = "submitLocaltion")
    @ApiOperation(value = "记录位置信息")
    @ResponseBody
    public Object submitLocaltion(HttpServletRequest request, HttpServletResponse response, UmsMemberLocation location) {
        location.setCreateTime(new Date());
        memberLocationService.save(location);
        return new CommonResult().success("添加成功");
    }



    @SysLog(MODULE = "pms", REMARK = "查询商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐商品")
    @GetMapping(value = "/initGoodsRedis")
    public Object initGoodsRedis() {

        return pmsProductService.initGoodsRedis();

    }
    @SysLog(MODULE = "pms", REMARK = "查询商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐商品")
    @GetMapping(value = "/initMemberRedis")
    public Object initMemberRedis() {

        return new CommonResult().success(memberService.initMemberRedis());

    }
}
