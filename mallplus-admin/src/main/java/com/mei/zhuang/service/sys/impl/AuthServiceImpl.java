package com.mei.zhuang.service.sys.impl;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.config.UserAuthConfig;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.constant.UserConsts;
import com.mei.zhuang.entity.sys.FrontUser;
import com.mei.zhuang.entity.sys.SysPlatformUser;
import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.jwt.IJWTInfo;
import com.mei.zhuang.jwt.JWTInfo;
import com.mei.zhuang.service.sys.*;
import com.mei.zhuang.util.JwtTokenUtil;
import com.mei.zhuang.util.LoginResultEnum;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.vo.sys.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private IUserService userService;
    @Resource
    private SysLoginLogService sysLoginLogService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private TenantInfoService tenantInfoService;
    @Resource
    private IApiUserService apiUserService;
    @Resource
    private UserAuthConfig userAuthConfig;



    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // 最多允许输入错误次数
    @Value("${login.errorCountLimit}")
    private int errorCountLimit;

    @Override
    public JSONObject login(String username, String password) throws Exception {
        JSONObject jsonObject = new JSONObject();
        SysPlatformUser info = sysUserService.getPlatUserInfoByUserName(username);
        String schema;
        String token;
        if (info == null) {
            jsonObject.put("code", CommonConstant.CODE_BIZ_ERROR);
            jsonObject.put("msg", "该用户不存在!");
        } else {
            if (StringUtils.equals(UserConsts.LOCK_STATUS, info.getStatus())) {
                userService.updatestatus("1",info.getUsername());
                jsonObject.put("code", CommonConstant.CODE_BIZ_ERROR);
                jsonObject.put("msg", "该用户已被锁定，请联系管理员!");
                LoginResultEnum.FAIL_ACCOUNT_LOCKED.recordLoginLog(username, sysLoginLogService);
                return jsonObject;
            }
            Integer userId = Integer.valueOf(info.getId());
            boolean bo = encoder.matches(password, info.getPassword());
            if (bo) {
                //TODO 租户
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(info.getManageTenantIds())) {
               // if (info.getManageTenantIds() != null && info.getManageTenantIds().length > 0) {
                    token = jwtTokenUtil.generateToken(new JWTInfo(info.getUsername(), info.getId().toString(), info.getUsername()));
                    log.info(token+"登入token"+userAuthConfig.getExpire()+userAuthConfig.getPriKeyPath());
                    jsonObject.put("code", CommonConstant.CODE_SUCCESS);
                    jsonObject.put("msg", "用户权限校验成功!");
                    jsonObject.put("data", token);
                    sysUserService.recordLastLoginTimeAndResetLoginErrorCount(userId);
                    LoginResultEnum.SUCCESS.recordLoginLog(username, sysLoginLogService);
                    sysLoginLogService.updateUnlockFlagOfSpecifyMinutes(username, DateUtil.getDateOfSpecifyMinuteBeFore(UserConsts.SPECIFY_MINUTES));
                } else {
                    jsonObject.put("code", CommonConstant.CODE_BIZ_ERROR);
                    jsonObject.put("msg", "当前登录账号没有分配任何租户，请联系管理员!");
                    LoginResultEnum.FAIL_NO_AUTH.recordLoginLog(username, sysLoginLogService);
                }
            } else {
                int passwordErrorCount =0;
                //获取10分钟之前的时间
                String specifyMinutesDate = DateUtil.getDateOfSpecifyMinuteBeFore(UserConsts.SPECIFY_MINUTES);
                log.info("密码错误！！！");
                LoginResultEnum.FAIL_PASSWORD_ERROR.recordLoginLog(username, sysLoginLogService);
                jsonObject.put("code", CommonConstant.CODE_BIZ_ERROR);
                passwordErrorCount= sysLoginLogService.getSpecifyMinutesPasswordErrorCount(username, specifyMinutesDate);
                if(passwordErrorCount>3) {
                    sysLoginLogService.updateUnlockFlagOfSpecifyMinutes(username,specifyMinutesDate); log.info("密码错误！！！");
                    LoginResultEnum.FAIL_PASSWORD_ERROR.recordLoginLog(username, sysLoginLogService);
                    jsonObject.put("code", CommonConstant.CODE_BIZ_ERROR);
                    passwordErrorCount=sysLoginLogService.getSpecifyMinutesPasswordErrorCount(username, specifyMinutesDate);
                }
                log.info("10分钟内密码已经错误次数：{}",passwordErrorCount);

                if ((passwordErrorCount) == errorCountLimit) {
                    sysUserService.lockAccount(userId);
                    StringBuffer msg = new StringBuffer();
                    msg.append("由于");
                    msg.append(UserConsts.SPECIFY_MINUTES);
                    msg.append("分钟内连续输入密码错误");
                    msg.append(errorCountLimit);
                    msg.append("次，账号已被锁定，请联系管理员!");
                    jsonObject.put("msg", msg);
                    System.out.println(passwordErrorCount+"错误");
                } else {
                    StringBuffer msg = new StringBuffer();
                    msg.append(UserConsts.SPECIFY_MINUTES);
                    msg.append("分钟内连续输入密码错误");
                    msg.append(passwordErrorCount);
                    msg.append("次，还剩");
                    msg.append(errorCountLimit - passwordErrorCount);
                    msg.append("次机会，之后账号将会被锁定");
                    jsonObject.put("msg", msg);
                }
            }
        }
        return jsonObject;
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public FrontUser getPlatformUserInfo(String token) throws Exception {
        String username = jwtTokenUtil.getInfoFromToken(token).getUniqueName();
        if (username == null)
            return null;
        FrontUser frontUser = new FrontUser();
        SysPlatformUser platformUser = sysUserService.getPlatUserInfoByUserName(username);
        frontUser.setId(platformUser.getId().toString());
        frontUser.setUsername(username);
        List<SysTenant> tenants = tenantInfoService.getValidTenantByIds(platformUser.getManageTenantIds().split(","));
        frontUser.setTenants(tenants);
        return frontUser;
    }

    @Override
    public FrontUser getUserInfo(String token, String tenantId) throws Exception {
        String username = jwtTokenUtil.getInfoFromToken(token).getUniqueName();
        if (username == null) {
            return null;
        }
        log.info("获取用户：{} 所在租户id ：{} 的信息",username,tenantId);
        UserInfo user = userService.getUserByUsername(username);
        FrontUser frontUser = new FrontUser();
        BeanUtils.copyProperties(user, frontUser);

        return frontUser;
    }

    @Override
    public Boolean invalid(String token) {
        // TODO: 2017/9/11 注销token
        return null;
    }

    @Override
    public String refresh(String oldToken) throws Exception {
        IJWTInfo ijwtInfo = jwtTokenUtil.getInfoFromToken(oldToken);
        return jwtTokenUtil.generateToken(ijwtInfo);
    }
}
