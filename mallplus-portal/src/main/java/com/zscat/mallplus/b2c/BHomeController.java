package com.zscat.mallplus.b2c;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.service.IPmsSmallNaviconCategoryService;
import com.zscat.mallplus.sms.entity.SmsCouponHistory;
import com.zscat.mallplus.sms.entity.SmsHomeAdvertise;
import com.zscat.mallplus.sms.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.sms.vo.HomeFlashPromotion;
import com.zscat.mallplus.ums.entity.SysNotice;
import com.zscat.mallplus.ums.entity.TbUserFromId;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLocation;
import com.zscat.mallplus.ums.mapper.TbUserFromIdMapper;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.util.OssAliyunUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.PhoneUtil;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApiContext;
import com.zscat.mallplus.vo.Rediskey;
import com.zscat.mallplus.vo.SmsCode;
import com.zscat.mallplus.vo.UmsMemberInfoDetail;
import com.zscat.mallplus.vo.home.Configs;
import com.zscat.mallplus.vo.home.Pages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
public class BHomeController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

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

    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Resource
    private ISysNoticeService noticeService;
    @Resource
    private ISysMessageService messageService;
    @Autowired
    private ApiContext apiContext;

    @Resource
    private TbUserFromIdMapper fromIdMapper;
    @Resource
    private IPmsProductService pmsProductService;

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/pc.getpageconfig", method = RequestMethod.POST)
    public Object pc_home() {
        HomeContentResult contentResult = null;
        String key = Rediskey.HOMEPAGEPC + apiContext.getCurrentProviderId();
        try {
            String json = redisService.get(key);
            if (ValidatorUtils.empty(json)){
                contentResult = advertiseService.contentPc();
                redisService.set(key,JsonUtils.objectToJson(contentResult));
                redisService.expire(key,3600*5);
            }else{
                contentResult = JsonUtils.jsonToPojo(json, HomeContentResult.class);
            }
        }catch (Exception e){
            contentResult = advertiseService.contentNew1();
            redisService.set(key,JsonUtils.objectToJson(contentResult));
            redisService.expire(key,3600*5);
        }
        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/crmeb.getpageconfig", method = RequestMethod.POST)
    public Object contentNew1() {

        HomeContentResult contentResult = null;
        String key = Rediskey.HOMEPAGE2 + apiContext.getCurrentProviderId();
        try {
            String json = redisService.get(key);
            if (ValidatorUtils.empty(json)){
                contentResult = advertiseService.contentNew1();
                redisService.set(key,JsonUtils.objectToJson(contentResult));
                redisService.expire(key,3600*5);
            }else{
                contentResult = JsonUtils.jsonToPojo(json, HomeContentResult.class);
            }
        }catch (Exception e){
            contentResult = advertiseService.contentNew1();
            redisService.set(key,JsonUtils.objectToJson(contentResult));
            redisService.expire(key,3600*5);
        }
        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/pages.getpageconfig", method = RequestMethod.POST)
    public Object contentNew() {
        String key = Rediskey.HOMEPAGE + apiContext.getCurrentProviderId();
        String json = redisService.get(key);
        Pages contentResult = null;
        try {
            if (ValidatorUtils.empty(json)){
                contentResult = advertiseService.contentNew();
                redisService.set(key,JsonUtils.objectToJson(contentResult));
                redisService.expire(key,3600*5);
            }else{
                contentResult = JsonUtils.jsonToPojo(json, Pages.class);
            }
        }catch (Exception e){
            contentResult = advertiseService.contentNew();
            redisService.set(key,JsonUtils.objectToJson(contentResult));
            redisService.expire(key,3600*5);
        }

        return new CommonResult().success(contentResult);
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public Object share() {
        Map<String,String> data = new HashedMap();
        data.put("img","http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190807/QQ%E5%9B%BE%E7%89%8720190807191952.jpg");
        data.put("title","mallplus");
        data.put("synopsis","mallplus");
        return new CommonResult().success(data);
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
                           @RequestParam String authCode,
                           @RequestParam String invitecode) {
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
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember!=null && umsMember.getId()!=null){
            umsMember = memberService.getById(umsMember.getId());
            List<SmsCouponHistory> histories = couponHistoryMapper.selectList(new QueryWrapper<SmsCouponHistory>().eq("member_id",umsMember.getId()));
            detail.setHistories(histories);
            detail.setMember(umsMember);
            return new CommonResult().success(detail);
        }
        return new CommonResult().success();
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


    @IgnoreAuth
    @ApiOperation("首页秒杀活动")
    @SysLog(MODULE = "home", REMARK = "首页秒杀活动")
    @RequestMapping(value = "/seckill/index", method = RequestMethod.POST)
    public Object getHomeFlashPromotion() {
        HomeFlashPromotion contentResult = advertiseService.getHomeFlashPromotion();
        return new CommonResult().success(contentResult);

    }
    @Resource
    private IPmsSmallNaviconCategoryService IPmsSmallNaviconCategoryService;

    @SysLog(MODULE = "pms", REMARK = "查询pms_small_navicon_category表")
    @ApiOperation("查询pms_small_navicon_category表")
    @PostMapping(value = "/home.navlist")
    public Object navlist() {
        try {
            return new CommonResult().success(IPmsSmallNaviconCategoryService.list( new QueryWrapper<>()));
        } catch (Exception e) {
            log.error("分页获取pms_small_navicon_category列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @IgnoreAuth
    @ApiOperation("首页推荐专题")
    @SysLog(MODULE = "home", REMARK = "首页推荐专题")
    @RequestMapping(value = "/recommendsubjectList", method = RequestMethod.POST)
    public Object getSubjectList(
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = advertiseService.getRecommendSubjectList(pageSize, pageNum);
        return new CommonResult().success(subjectList);
    }
    /**
     * 提交小程序推送formid
     *
     * @param request
     * @param response
     * @param formId   小程序推送formId
     * @return
     */
    @RequestMapping(value = "submitFormId")
    @ApiOperation(value = "提交小程序推送formid")
    @ResponseBody
    public Object submitFormId(HttpServletRequest request, HttpServletResponse response, String formId) {

        TbUserFromId entity = new TbUserFromId();

        if (ValidatorUtils.empty(formId)) {
            return new CommonResult().validateFailed("前置参数错误，formId不能为空");
        }
        if ("the formId is a mock one".equals(formId)){
            return new CommonResult().success("添加成功");
        }
        entity.setFormId(formId);
        entity.setUserId(memberService.getNewCurrentMember().getId());
        //校验formId是否已经存在
        if(fromIdMapper.selectCount(new QueryWrapper<>(entity))>0) {
            return new CommonResult().validateFailed("前置参数错误，formId已经存在 formId：" + formId);
        }
        entity.setStatus(1);
        fromIdMapper.insert(entity);
        return new CommonResult().success("添加成功");
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页推荐品牌")
    @IgnoreAuth
    @ApiOperation(value = "查询首页推荐品牌")
    @PostMapping(value = "/recommendBrand/list")
    public Object getRecommendBrandList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        return new CommonResult().success(pmsProductService.getRecommendBrandList(1,1));
    }

    @SysLog(MODULE = "pms", REMARK = "查询首页新品精品、热门、首发列表")
    @IgnoreAuth
    @ApiOperation(value = "查询首页新品")
    @PostMapping(value = "/groom/list")
    public Object getNewProductList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "type", required = false, defaultValue = "1") Integer type) {
        List<SmsHomeAdvertise> banner = advertiseService.getHomeAdvertiseList(type);
        List<PmsProduct> list = new ArrayList<>();
        if (type==1){
            list = pmsProductService.getHotProductList(1,100);
        }else  if (type==2){
            list = advertiseService.getSaleProductList(1,100);
        }else  if (type==1){
            list = pmsProductService.getNewProductList(1,100);
        }
        Map<String,Object> map = new HashedMap();
        map.put("banner",banner);
        map.put("list",list);
        return new CommonResult().success(map);
    }

    @RequestMapping(value = "submitLocaltion")
    @ApiOperation(value = "记录位置信息")
    @ResponseBody
    public Object submitLocaltion(HttpServletRequest request, HttpServletResponse response, UmsMemberLocation location) {
        location.setCreateTime(new Date());
        memberLocationService.save(location);
        return new CommonResult().success("添加成功");
    }
}
