package com.mei.zhuang.service.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.arvato.common.redis.template.RedisRepository;
import com.arvato.ec.common.constant.RedisConstant;
import com.arvato.ec.common.vo.EsMiniprogram;
import com.arvato.ec.common.vo.data.customer.CustGroupIndexParam;
import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.arvato.ec.common.vo.order.OrderStstic;
import com.arvato.service.member.api.config.SmsConfig;
import com.arvato.service.member.api.feigin.MarkingFegin;
import com.arvato.service.member.api.feigin.OrderFeigin;
import com.arvato.service.member.api.mq.Sender;
import com.arvato.service.member.api.orm.dao.EsCoreSmsMapper;
import com.arvato.service.member.api.orm.dao.EsMemberMapper;
import com.arvato.service.member.api.orm.dao.EsMiniprogramMapper;
import com.arvato.service.member.api.service.EsMemberService;
import com.arvato.service.member.api.util.CommonUtil;
import com.arvato.service.member.api.util.MiniAESUtil;
import com.arvato.service.member.api.util.SmsUtils;
import com.arvato.service.member.api.vo.LoginVo;
import com.arvato.utils.CommonResult;
import com.arvato.utils.json.JsonUtil;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.member.EsCoreSms;
import com.mei.zhuang.entity.member.EsMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */
@Slf4j
@Service
public class EsMemberServiceImpl extends ServiceImpl<EsMemberMapper, EsMember> implements EsMemberService {

    String webAccessTokenhttps = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    @Resource
    private MarkingFegin markingFegin;
    @Resource
    private EsMemberMapper memberMapper;
    @Resource
    private EsMiniprogramMapper miniprogramMapper;
    @Resource
    private RedisRepository redisRepository;
    @Resource
    private OrderFeigin orderFeigin;
    @Resource
    EsCoreSmsMapper smsMapper;
    @Resource
    private SmsConfig smsConfig;
    private String REDIS_KEY_PREFIX_AUTH_CODE = "bindPhone:authCode:";
    @Autowired
    private Sender sender;


    @Override
    public Object loginByWeixin(LoginVo entity) {
        try {

            String code = entity.getCode();
            if (StringUtils.isEmpty(code)) {
                return new CommonResult().failed("code is empty");
            }
            String userInfos = entity.getUserInfos();

            String signature = entity.getSignature();

            Map<String, Object> me = JsonUtil.readJsonToMap(userInfos);
            System.out.println(me+"登入数据");
            if (null == me) {
                return new CommonResult().failed("登录失败");
            }

            Map<String, Object> resultObj = new HashMap<String, Object>();
            EsMiniprogram miniprogram = this.getByShopId(entity.getShopId());

            //获取openid
            String requestUrl = String.format(webAccessTokenhttps,
                    miniprogram.getAppid(),
                    miniprogram.getAppSecret(),
                    code);//通过自定义工具类组合出小程序需要的登录凭证 code
            // this.getWebAccess(code);//通过自定义工具类组合出小程序需要的登录凭证 code
            JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

            if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
                return new CommonResult().failed(sessionData.getString("errmsg"));
            }
            //验证用户信息完整性
            String sha1 = CommonUtil.getSha1(userInfos + sessionData.getString("session_key"));
            if (!signature.equals(sha1)) {
                return new CommonResult().failed("验证登录失败");
            }
            EsMember queryOpenid = new EsMember();
            queryOpenid.setOpenid(sessionData.getString("openid"));
            EsMember userVo = memberMapper.selectOne(queryOpenid);
            EsMember umsMember = new EsMember();
            umsMember.setProvince(me.get("province").toString());
            umsMember.setCity(me.get("city").toString());
            umsMember.setArea(me.get("country").toString());

            umsMember.setSex(me.get("gender").toString());
            if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                //会员头像(默认头像)
                umsMember.setAvatar("/upload/img/avatar/01.jpg");
            } else {
                umsMember.setAvatar(me.get("avatarUrl").toString());
            }
            // umsMember.setGender(Integer.parseInt(me.get("gender")));
            umsMember.setNickname(me.get("nickName").toString());
            umsMember.setRealname(me.get("nickName").toString());
            //算法加密
            String wxDecrypt = MiniAESUtil.wxDecrypt(entity.getEncryptedData(),sessionData.getString("session_key"),entity.getIv());
            log.info(wxDecrypt);
            Map<String, Object> uInfo =  JsonUtil.readJsonToMap(wxDecrypt);
            log.info(uInfo.toString());
            if (ValidatorUtils.notEmpty(uInfo) && ValidatorUtils.notEmpty(uInfo.get("unionId"))){
                umsMember.setUnionid(uInfo.get("unionId").toString());
            }
            if (null == userVo) {
                umsMember.setBuyCount(0);
                umsMember.setBalance(BigDecimal.ZERO);
                umsMember.setBuyMoney(BigDecimal.ZERO);
                umsMember.setComeFrom(1);
                umsMember.setIsBlack(1);
                umsMember.setOpenid(sessionData.getString("openid"));
                umsMember.setCreateTime(new Date());
                umsMember.setShopId(entity.getShopId());
                umsMember.setBinding(1);
                umsMember.setMobileVerified(1);
                memberMapper.insert(umsMember);
                sender.appletRegisterMq(umsMember);
                resultObj.put("userInfo", umsMember);
                resultObj.put("userId", umsMember.getId());
                redisRepository.del(String.format(RedisConstant.MEMBER, umsMember.getId() + ""));
            } else {
                if (ValidatorUtils.notEmpty(uInfo) && ValidatorUtils.notEmpty(uInfo.get("unionId"))){
                    userVo.setUnionid(uInfo.get("unionId").toString());
                }
                userVo.setProvince(me.get("province").toString());
                userVo.setCity(me.get("city").toString());
                userVo.setArea(me.get("country").toString());

                userVo.setSex(me.get("gender").toString());
                if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                    //会员头像(默认头像)
                    userVo.setAvatar("/upload/img/avatar/01.jpg");
                } else {
                    userVo.setAvatar(me.get("avatarUrl").toString());
                }
                // umsMember.setGender(Integer.parseInt(me.get("gender")));
                userVo.setNickname(me.get("nickName").toString());
                userVo.setRealname(me.get("nickName").toString());
                memberMapper.updateById(userVo);
                resultObj.put("userInfo", userVo);
                resultObj.put("userId", userVo.getId());
                sender.appletLoginMq(umsMember.getNickname());
                redisRepository.del(String.format(RedisConstant.MEMBER, umsMember.getId() + ""));
            }

            CompletableFuture.runAsync(() -> {
                try {
                    markingFegin.sendNewCoupon(umsMember.getId(), 1);
                } catch (Exception e) {
                    log.error("發送新人券失败：{}", e.getMessage());
                }

            });

            return new CommonResult().success(resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed(e.getMessage());
        }
    }

    @Override
    public EsMiniprogram getByShopId(Long shopId) {
        EsMiniprogram query = new EsMiniprogram();
        query.setShopId(shopId);
        return miniprogramMapper.selectOne(query);
    }

    @Override
    public Object bindPhone(String phone, String code, Long memberId) {

        //验证验证码
        if (!verifyAuthCode(code, phone)) {
            return new CommonResult().failed("验证码错误");
        }
        EsMember member = new EsMember();
        member.setId(memberId);
        member.setBinding(2);
        member.setMobileVerified(2);
        member.setMobile(phone);
        memberMapper.updateById(member);
        EsMember newMember = memberMapper.selectById(memberId);
        redisRepository.set(String.format(RedisConstant.MEMBER, member.getId() + ""), newMember);
        sender.bindMobileMq(phone);
        sender.appletRegisterMq(newMember);
        return 1;
    }



    @Override
    public Object generateCode(String phone) {
        //   checkTodaySendCount(phone);

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //短信验证码缓存15分钟，
        redisRepository.set(REDIS_KEY_PREFIX_AUTH_CODE + phone, sb.toString());
        redisRepository.willExpire(REDIS_KEY_PREFIX_AUTH_CODE + phone, 60);
        EsCoreSms querySms = new EsCoreSms();
        querySms.setStatus(1);
        EsCoreSms coreSms  = smsMapper.selectOne(querySms);
        //异步调用阿里短信接口发送短信
        CompletableFuture.runAsync(() -> {
            try {
                SmsUtils.sendMsg(coreSms.getAccount(), coreSms.getPassword(),
                        phone, "验证码" + sb.toString() + ",您正在进行身份验证,打死都不要告诉别人哦");
            } catch (Exception e) {
                log.error("发送短信失败：{}", e.getMessage());
            }

        });

        // 当天发送验证码次数+1
       /* String countKey = countKey(phone);
        redisRepository.incr(countKey);
        redisRepository.willExpire(countKey, 1 * 3600 * 24);*/
        return 1;
    }

    @Override
    public EsMember selectmember(String mobile) {
        return memberMapper.selectmember(mobile);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMemberOrderInfo() {
        List<OrderStstic> orders = orderFeigin.listOrderGroupByMemberId();
        for (OrderStstic o : orders) {
            EsMember member = new EsMember();
            member.setId(o.getMemberId());
            member.setBuyMoney(o.getTotalPayAmount());
            member.setBuyCount(o.getTotalCount());
            memberMapper.updateById(member);
        }
        return true;
    }

    @Override
    public Integer selTotalMember(CustGroupIndexParam param) {
        return memberMapper.selTotalMember(param);
    }

    @Override
    public Integer membercount() {
        return memberMapper.membercount();
    }

    @Override
    public Integer memberNumber(TradeAnalyzeParam param) {
        return memberMapper.memberNumber(param);
    }

    @Override
    public List<EsMember> memberselect(Integer param1, Integer param2) {
        return memberMapper.memberselect(param1,param2);
    }

    /**
     * 获取当天发送验证码次数
     * 限制号码当天次数
     *
     * @param phone
     * @return
     */
    private void checkTodaySendCount(String phone) {
        Object value = redisRepository.get(countKey(phone));
        if (value != null) {
            Integer count = Integer.valueOf(value.toString());
            if (count > smsConfig.getDayCount()) {
                throw new IllegalArgumentException("已超过当天最大次数");
            }
        }

    }

    private String countKey(String phone) {
        return "sms:count:" + LocalDate.now().toString() + ":" + phone;
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisRepository.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone).toString();
        return authCode.equals(realAuthCode);
    }


}
