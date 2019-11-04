package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.vo.OrderStstic;
import com.zscat.mallplus.single.ApiBaseAction;
import com.zscat.mallplus.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.ums.entity.*;
import com.zscat.mallplus.ums.mapper.SysAppletSetMapper;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.ums.mapper.UmsMemberMemberTagRelationMapper;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.util.*;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.MatrixToImageWriter;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApiContext;
import com.zscat.mallplus.vo.AppletLoginParam;
import com.zscat.mallplus.vo.Rediskey;
import com.zscat.mallplus.vo.SmsCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements IUmsMemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);

    @Autowired
    OssAliyunUtil aliyunOSSUtil;

    @Resource
    private UmsMemberMapper memberMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisService redisService;
    /* @Resource
     private AuthenticationManager authenticationManager;*/
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private SmsService smsService;
    @Resource
    private SysAreaMapper areaMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${authCode.expire.seconds}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${aliyun.sms.expire-minute:1}")
    private Integer expireMinute;
    @Value("${aliyun.sms.day-count:30}")
    private Integer dayCount;
    @Resource
    private UmsMemberMemberTagRelationMapper umsMemberMemberTagRelationMapper;

    @Resource
    private SysAppletSetMapper appletSetMapper;

    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private OmsOrderMapper omsOrderMapper;
    @Resource
    private IUmsMemberBlanceLogService blanceLogService;
    @Resource
    private IUmsIntegrationChangeHistoryService umsIntegrationChangeHistoryService;

    @Autowired
    private ApiContext apiContext;

    Integer regJifen = 100;
    Integer logginJifen = 5;

    @Override
    public UmsMember getNewCurrentMember(){
      return (UmsMember) this.getCurrentMember();
    }
    @Override
    public  Object getCurrentMember() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String requestType = ((HttpServletRequest) request).getMethod();
            if ("OPTIONS".equals(requestType)) {
                return null;
            }
            String storeId = request.getParameter("storeid");
            if (ValidatorUtils.empty(storeId)){
                storeId = request.getHeader("storeid");
            }
            String tokenPre = "authorization"+storeId ;
            String authHeader = request.getParameter(tokenPre);
            if (ValidatorUtils.empty(authHeader)){
                authHeader = request.getHeader(tokenPre);
            }
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                String authToken = authHeader.substring("Bearer".length());
                String username = jwtTokenUtil.getUserNameFromToken(authToken);
                if (ValidatorUtils.notEmpty(username)){
                    UmsMember member = JsonUtils.jsonToPojo(redisService.get(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.MEMBER, username)),UmsMember.class);
                    if (member==null || member.getId()==null){
                        member=getByUsername(username);
                    }
                    return member;
                }
            }
            return new CommonResult().fail(100);
        } catch (Exception e) {
            e.printStackTrace();
            return new UmsMember();
        }
    }
    @Override
    public void updataMemberOrderInfo() {
        List<OrderStstic> orders = omsOrderMapper.listOrderGroupByMemberId();
        List<UmsMemberLevel> levelList = memberLevelService.list(new QueryWrapper<UmsMemberLevel>().orderByDesc("price"));
        for (OrderStstic o : orders) {
            UmsMember member = new UmsMember();
            member.setId(o.getMemberId());
            member.setBuyMoney(o.getTotalPayAmount());
            for (UmsMemberLevel level : levelList) {
                if (member.getBuyMoney() != null) {
                    if (member.getBuyMoney().compareTo(level.getPrice()) >= 0) {
                        member.setMemberLevelId(level.getId());
                        member.setMemberLevelName(level.getName());
                        break;
                    }
                }
            }
            member.setBuyCount(o.getTotalCount());
            memberMapper.updateById(member);
        }
    }

    /**
     * 添加余额记录 并更新用户余额
     *
     * @param id
     * @param integration
     */
    @Override
    public void addBlance(Long id, Integer integration, int type, String note) {

        UmsMemberBlanceLog blanceLog = new UmsMemberBlanceLog();
        blanceLog.setMemberId(id);
        blanceLog.setPrice(new BigDecimal(integration));
        blanceLog.setCreateTime(new Date());
        blanceLog.setType(type);
        blanceLog.setNote(note);
        blanceLogService.save(blanceLog);
        UmsMember member = memberMapper.selectById(id);
        member.setBlance(member.getBlance().add(blanceLog.getPrice()));
        memberMapper.updateById(member);

    }

    /**
     * 添加积分记录 并更新用户积分
     *
     * @param id
     * @param integration
     */
    @Override
    public void addIntegration(Long id, Integer integration, int changeType, String note, int sourceType, String operateMan) {
        UmsIntegrationChangeHistory history = new UmsIntegrationChangeHistory();
        history.setMemberId(id);
        history.setChangeCount(integration);
        history.setCreateTime(new Date());
        history.setChangeType(changeType);
        history.setOperateNote(note);
        history.setSourceType(sourceType);
        history.setOperateMan(operateMan);
        umsIntegrationChangeHistoryService.save(history);
        UmsMember member = memberMapper.selectById(id);
        member.setIntegration(member.getIntegration() + integration);
        memberMapper.updateById(member);
        redisService.set(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.MEMBER, member.getUsername()), JsonUtils.objectToJson(member));
    }

    @Override
    public UmsMember getByUsername(String username) {
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);

        return memberMapper.selectOne(new QueryWrapper<>(umsMember));
    }

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectById(id);
    }

    @Override
    public CommonResult register(String phone, String password, String confim, String authCode, String invitecode) {

        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(phone);
        umsMember.setPhone(phone);
        umsMember.setSourceType(3);
        umsMember.setPassword(password);
        umsMember.setConfimpassword(confim);
        umsMember.setPhonecode(authCode);
        umsMember.setInvitecode(invitecode);
        if (ValidatorUtils.notEmpty(umsMember.getPhonecode()) && !verifyAuthCode(umsMember.getPhonecode(), umsMember.getPhone())) {
            return new CommonResult().failed("验证码错误");
        }
        return this.register(umsMember);
    }

    @Override
    public SmsCode generateCode(String phone) {
        //生成流水号
        String uuid = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("code", sb.toString());
        map.put("phone", phone);

        //短信验证码缓存15分钟，
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + phone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + phone, expireMinute * 60);


        //存储sys_sms
        saveSmsAndSendCode(phone, sb.toString());
        SmsCode smsCode = new SmsCode();
        smsCode.setKey(uuid);
        return smsCode;
    }

    /**
     * 保存短信记录，并发送短信
     *
     * @param phone
     * @param code
     */
    private void saveSmsAndSendCode(String phone, String code) {
        checkTodaySendCount(phone);

        Sms sms = new Sms();
        sms.setPhone(phone);
        sms.setParams(code);
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        smsService.save(sms, params);

        //异步调用阿里短信接口发送短信
        CompletableFuture.runAsync(() -> {
            try {
                smsService.sendSmsMsg(sms);
            } catch (Exception e) {
                params.put("err", e.getMessage());
                smsService.save(sms, params);
                e.printStackTrace();
                LOGGER.error("发送短信失败：{}", e.getMessage());
            }

        });

        // 当天发送验证码次数+1
        String countKey = countKey(phone);
        redisService.increment(countKey, 1L);
        redisService.expire(countKey, 1 * 3600 * 24);
    }

    /**
     * 获取当天发送验证码次数
     * 限制号码当天次数
     *
     * @param phone
     * @return
     */
    private void checkTodaySendCount(String phone) {
        String value = redisService.get(countKey(phone));
        if (value != null) {
            Integer count = Integer.valueOf(value);
            if (count > dayCount) {
                throw new IllegalArgumentException("已超过当天最大次数");
            }
        }

    }

    private String countKey(String phone) {
        return "sms:count:" + LocalDate.now().toString() + ":" + phone;
    }

    @Override
    public CommonResult register(UmsMember user) {
        //验证验证码
        if (ValidatorUtils.notEmpty(user.getPhonecode()) && !verifyAuthCode(user.getPhonecode(), user.getPhone())) {
            return new CommonResult().failed("验证码错误");
        }
        if (!user.getPassword().equals(user.getConfimpassword())) {
            return new CommonResult().failed("密码不一致");
        }
        //查询是否已有该用户

        UmsMember queryM = new UmsMember();
        queryM.setUsername(user.getUsername());
        UmsMember umsMembers = memberMapper.selectOne(new QueryWrapper<>(queryM));
        if (umsMembers != null) {
            return new CommonResult().failed("该用户已经存在");
        }
        //没有该用户进行添加操作

        UmsMember umsMember = new UmsMember();
        umsMember.setMemberLevelId(4L);
        umsMember.setMemberLevelName("普通会员");
        umsMember.setUsername(user.getUsername());
        umsMember.setSourceType(user.getSourceType());
        umsMember.setPhone(user.getPhone());
        umsMember.setPassword(passwordEncoder.encode(user.getPassword()));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        umsMember.setBlance(new BigDecimal(10000));
        umsMember.setIntegration(10000);

        String defaultIcon = "http://yjlive160322.oss-cn-beijing.aliyuncs.com/mall/images/20190830/uniapp.jpeg";
        umsMember.setIcon(defaultIcon);
        //这是要生成二维码的url
        String url = "http://www.yjlive.cn:8082/?invitecode=" + user.getUsername();
        //要添加到二维码下面的文字
        String words = user.getUsername() + "的二维码";
        //调用刚才的工具类
        ByteArrayResource qrCode = MatrixToImageWriter.createQrCode(url, words);
        InputStream inputStream = new ByteArrayInputStream(qrCode.getByteArray());


        umsMember.setAvatar(aliyunOSSUtil.upload("png", inputStream));
        memberMapper.insert(umsMember);

        redisService.set(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.MEMBER, umsMember.getUsername()), JsonUtils.objectToJson(umsMember));

        addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername());
        umsMember.setPassword(null);
        return new CommonResult().success("注册成功", null);
    }

    @Override
    public Object simpleReg(String phone, String password, String confimpassword, String invitecode) {
        //没有该用户进行添加操作
        UmsMember user = new UmsMember();
        user.setUsername(phone);
        user.setPhone(phone);
        user.setPassword(password);
        user.setConfimpassword(confimpassword);
        user.setInvitecode(invitecode);
        return this.register(user);
    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return new CommonResult().success("获取验证码成功", sb.toString());
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMember example = new UmsMember();
        example.setPhone(telephone);
        UmsMember member = memberMapper.selectOne(new QueryWrapper<>(example));
        if (member == null) {
            return new CommonResult().failed("该账号不存在");
        }
        //验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            return new CommonResult().failed("验证码错误");
        }

        member.setPassword(passwordEncoder.encode(password));
        memberMapper.updateById(member);
        return new CommonResult().success("密码修改成功", null);
    }


    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember record = new UmsMember();
        record.setId(id);
        record.setIntegration(integration);
        memberMapper.updateById(record);
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return authCode.equals(realAuthCode);
    }

    @Override
    public UmsMember queryByOpenId(String openId) {
        UmsMember queryO = new UmsMember();
        queryO.setWeixinOpenid(openId);
        return memberMapper.selectOne(new QueryWrapper<>(queryO));
    }

    @Override
    public Map<String, Object> appLogin(String openid, Integer sex, String headimgurl, String unionid, String nickname, String city, Integer source) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UmsMember userVo = this.queryByOpenId(openid);
        String token = null;
        if (null == userVo) {
            UmsMember umsMember = new UmsMember();
            umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
            umsMember.setSourceType(source);
            umsMember.setPassword(passwordEncoder.encode("123456"));
            umsMember.setCreateTime(new Date());
            umsMember.setStatus(1);
            umsMember.setBlance(new BigDecimal(10000));
            umsMember.setIntegration(0);
            umsMember.setMemberLevelId(4L);
            umsMember.setCity(city);
            umsMember.setAvatar(headimgurl);
            umsMember.setGender(sex);
            umsMember.setHistoryIntegration(0);
            umsMember.setWeixinOpenid(openid);
            if (StringUtils.isEmpty(headimgurl)) {
                //会员头像(默认头像)
                umsMember.setIcon("/upload/img/avatar/01.jpg");
            } else {
                umsMember.setIcon(headimgurl);
            }

            umsMember.setNickname(nickname);
            umsMember.setPersonalizedSignature(unionid);
            memberMapper.insert(umsMember);
            token = jwtTokenUtil.generateToken(umsMember.getUsername());
            resultObj.put("userId", umsMember.getId());
            resultObj.put("userInfo", umsMember);
            addIntegration(umsMember.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), umsMember.getUsername());

        } else {
            addIntegration(userVo.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), userVo.getUsername());

            token = jwtTokenUtil.generateToken(userVo.getUsername());
            resultObj.put("userId", userVo.getId());
            resultObj.put("userInfo", userVo);
        }
        if (StringUtils.isEmpty(token)) {
            return ApiBaseAction.toResponsFail("登录失败");
        }
        resultObj.put("tokenHead", tokenHead);
        resultObj.put("token", token);
        return resultObj;
    }


    @Override
    public Object loginByWeixin(AppletLoginParam req) {
        try {
            SysAppletSet appletSet = appletSetMapper.selectOne(new QueryWrapper<>());
            if (null == appletSet) {
                return ApiBaseAction.toResponsFail("没有设置支付配置");
            }
            String codeH = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect";

            String code = req.getCode();
            if (StringUtils.isEmpty(code)) {
                log.error("code ie empty");
                return ApiBaseAction.toResponsFail("code ie empty");
            }
            String userInfos = req.getUserInfo();

            String signature = req.getSignature();
            System.out.println(com.alibaba.fastjson.JSONObject.toJSON(userInfos));
            Map<String, Object> me = JsonUtils.readJsonToMap(userInfos);
            if (null == me) {
                return ApiBaseAction.toResponsFail("登录失败 userInfos is null");
            }

            Map<String, Object> resultObj = new HashMap<String, Object>();

            String webAccessTokenhttps = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

            //获取openid
            String requestUrl = String.format(webAccessTokenhttps,
                    appletSet.getAppid(),
                    appletSet.getAppsecret(),
                    code);

            JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

            if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
                return ApiBaseAction.toResponsFail("登录失败openid is empty");
            }
            //验证用户信息完整性
            String sha1 = CommonUtil.getSha1(userInfos + sessionData.getString("session_key"));
            if (!signature.equals(sha1)) {
                return ApiBaseAction.toResponsFail("登录失败,验证用户信息完整性");
            }
            UmsMember userVo = this.queryByOpenId(sessionData.getString("openid"));
            String token = null;
            if (null == userVo) {
                UmsMember umsMember = new UmsMember();
                umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
                umsMember.setSourceType(2);
                umsMember.setPassword(passwordEncoder.encode("123456"));
                umsMember.setCreateTime(new Date());
                umsMember.setStatus(1);
                umsMember.setBlance(new BigDecimal(10000));
                umsMember.setIntegration(0);
                umsMember.setMemberLevelId(4L);
                umsMember.setAvatar(req.getCloudID());
                umsMember.setCity(me.get("country").toString() + "-" + me.get("province").toString() + "-" + me.get("city").toString());

                umsMember.setGender((Integer) me.get("gender"));
                umsMember.setHistoryIntegration(0);
                umsMember.setWeixinOpenid(sessionData.getString("openid"));
                if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                    //会员头像(默认头像)
                    umsMember.setIcon("/upload/img/avatar/01.jpg");
                } else {
                    umsMember.setIcon(me.get("avatarUrl").toString());
                }
                // umsMember.setGender(Integer.parseInt(me.get("gender")));
                umsMember.setNickname(me.get("nickName").toString());

                memberMapper.insert(umsMember);
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
                resultObj.put("userInfo", umsMember);
                addIntegration(umsMember.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), umsMember.getUsername());

            } else {
                addIntegration(userVo.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), userVo.getUsername());
                token = jwtTokenUtil.generateToken(userVo.getUsername());
                resultObj.put("userId", userVo.getId());
                resultObj.put("userInfo", userVo);
            }


            if (StringUtils.isEmpty(token)) {
                return ApiBaseAction.toResponsFail("登录失败");
            }
            resultObj.put("tokenHead", tokenHead);
            resultObj.put("token", token);


            return ApiBaseAction.toResponsSuccess(resultObj);
        } catch (ApiMallPlusException e) {
            e.printStackTrace();
            return ApiBaseAction.toResponsFail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiBaseAction.toResponsFail(e.getMessage());
        }

    }


    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }


    @Override
    public Map<String, Object> loginByCode(String phone, String authCode) {
        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(phone);

            UmsMember member = this.getByUsername(phone);
            //验证验证码
            if (!verifyAuthCode(authCode, member.getPhone())) {
                throw new ApiMallPlusException("验证码错误");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo", member);
            addIntegration(member.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), member.getUsername());
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());

        }
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return tokenMap;
    }

    @Override
    public Map<String, Object> login(String username, String password) {

        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UmsMember member = this.getByUsername(username);
            //验证验证码
           /* if (!verifyAuthCode(user.getCode(), member.getPhone())) {
                throw  new ApiMallPlusException("验证码错误");
            }*/

            //   Authentication authentication = authenticationManager.authenticate(authenticationToken);
            addIntegration(member.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), member.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo", member);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());

        }

        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return tokenMap;

    }

    @Override
    public Object initMemberRedis() {
        List<UmsMember> list = memberMapper.selectList(new QueryWrapper<>());
        for (UmsMember member : list) {
            redisService.set(apiContext.getCurrentProviderId()+":"+String.format(Rediskey.MEMBER, member.getUsername()), JsonUtils.objectToJson(member));
        }
        return 1;
    }

}

