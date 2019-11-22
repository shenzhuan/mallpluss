package com.mei.zhuang.service.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.dao.member.EsCoreSmsMapper;
import com.mei.zhuang.dao.member.EsMemberMapper;
import com.mei.zhuang.dao.order.EsMiniprogramMapper;
import com.mei.zhuang.entity.member.EsCoreSms;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.redis.template.RedisRepository;
import com.mei.zhuang.service.member.EsMemberService;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.util.JsonUtil;
import com.mei.zhuang.util.MiniAESUtil;
import com.mei.zhuang.utils.CommonUtil;
import com.mei.zhuang.utils.SmsUtils;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.LoginVo;
import com.mei.zhuang.vo.data.customer.CustGroupIndexParam;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.order.OrderStstic;
import lombok.extern.slf4j.Slf4j;
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
    EsCoreSmsMapper smsMapper;
    @Resource
    private EsMemberMapper memberMapper;
    @Resource
    private EsMiniprogramMapper miniprogramMapper;
    @Resource
    private RedisRepository redisRepository;
    @Resource
    private ShopOrderService orderService;
    private String REDIS_KEY_PREFIX_AUTH_CODE = "bindPhone:authCode:";


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
            System.out.println(me + "登入数据");
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
            EsMember userVo = memberMapper.selectOne(new QueryWrapper<>(queryOpenid));
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
            String wxDecrypt = MiniAESUtil.wxDecrypt(entity.getEncryptedData(), sessionData.getString("session_key"), entity.getIv());
            log.info(wxDecrypt);
            Map<String, Object> uInfo = JsonUtil.readJsonToMap(wxDecrypt);
            log.info(uInfo.toString());
            if (ValidatorUtils.notEmpty(uInfo) && ValidatorUtils.notEmpty(uInfo.get("unionId"))) {
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

                resultObj.put("userInfo", umsMember);
                resultObj.put("userId", umsMember.getId());
                redisRepository.del(String.format(RedisConstant.MEMBER, umsMember.getId() + ""));
            } else {
                if (ValidatorUtils.notEmpty(uInfo) && ValidatorUtils.notEmpty(uInfo.get("unionId"))) {
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

                redisRepository.del(String.format(RedisConstant.MEMBER, umsMember.getId() + ""));
            }

            CompletableFuture.runAsync(() -> {
                try {
                    // markingFegin.sendNewCoupon(umsMember.getId(), 1);
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
        return miniprogramMapper.selectOne(new QueryWrapper<>(query));
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
        EsCoreSms coreSms = smsMapper.selectOne(new QueryWrapper<>(querySms));
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
        List<OrderStstic> orders = orderService.listOrderGroupByMemberId();
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
        return memberMapper.memberselect(param1, param2);
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
