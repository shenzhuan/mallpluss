package com.zscat.mallplus.ums.service.impl;



import com.zscat.mallplus.interceptor.SystemDefines;
import com.zscat.mallplus.task.QueuesTask;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.MyRedisService;
import com.zscat.mallplus.util.jwt.exception.JWTInvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cn.ipanel.authorization.demo.global.SystemDefines.DEVICE_PC;

@Service
public class ManagerServiceImpl implements ManagerService {
    private static Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    private ManagerRepository managerRepository;
    private MyRedisService myRedisService;
    private AsyncTask asyncTask;
    private QueuesTask queuesTask;
    private Map<String, Integer> devices;

    {
        devices = new HashMap<>(3);
        devices.put("pc", SystemDefines.DEVICE_PC);
        devices.put("stb", SystemDefines.DEVICE_STB);
        devices.put("app", SystemDefines.DEVICE_APP);
    }

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository, MyRedisService myRedisService, AsyncTask asyncTask, QueuesTask queuesTask) {
        this.managerRepository = managerRepository;
        this.myRedisService = myRedisService;
        this.asyncTask = asyncTask;
        this.queuesTask = queuesTask;
    }

    @Override
    public List<ManagerInfo> queryUserInfo(String userName){
        return managerRepository.findAllByUserName(userName);
    }

    @Override
    public ManagerVO pcLogin(ManagerLoginReq managerLoginReq) throws Exception {
        //基本登录,数据库信息验证
        ManagerInfo managerInfo =  managerRepository.findUserInfoByUserNameAndPassword(managerLoginReq.getUsername(), managerLoginReq.getPassword());
        logger.info("managerInfo : "+ managerInfo);
        if (managerInfo == null)
            throw  new RequestParamErrorException("用户名或密码错误");
        //更新登录时间
        managerInfo.setLoginTime(Instant.now().toEpochMilli());
        managerRepository.save(managerInfo);
        //获取token
        String token = getToken(managerInfo, DEVICE_PC,managerLoginReq.getIp());
        asyncTask.updateManagerPcActiveTime(managerInfo.getUserName());
        //创建管理员队列
        queuesTask.createQueue();
        return new ManagerVO(managerInfo, token);
    }

    @Override
    public UmsMemberVO getUmsMember(String token, String device) throws Exception {
        RawAccessJwtToken accessJwtToken = new RawAccessJwtToken(token, SystemDefines.JWT_SECRET);
        UmsMember loginInfo = myRedisService.getUmsMember(accessJwtToken.getTokenValue());
        if (null == loginInfo) {
            throw new JwtExpiredTokenException("token expired(not existing)");
        }
        logger.info("loginInfo =="+loginInfo);
        if (devices.containsKey(device) && loginInfo.getDeviceType().equals(devices.get(device))) {
            if (null == loginInfo.getStatus()) {
                return new UmsMemberVO(loginInfo);
            }
            if (loginInfo.getStatus() == 1) {
                throw new Exception(SystemDefines.LOGIN_KICK_OUT_MSG);
            }
            if (loginInfo.getStatus() == 2) {
                throw new Exception(SystemDefines.LOGIN_EXPIRED_MSG);
            }
            // 管理员pc使用token，记录时间。
            if (loginInfo.getDeviceType() == 1 && loginInfo.getUserType() == 1) {
                asyncTask.updateManagerPcActiveTime(loginInfo.getWord());
            }
            return new UmsMemberVO(loginInfo);
        } else {
            throw new JWTInvalidTokenException("invalid token(mismatching)");
        }
    }

    /**
     * 获取token
     * @param managerInfo 管理员信息
     * @param device 登陆设备
     * @param Ip Ip地址
     */
    private String getToken(ManagerInfo managerInfo, Integer device,String Ip) throws Exception {
        TokenAndUuidBean bean = createToken(device, SystemDefines.USER_TYPE_MANAGER, managerInfo.getUserName(), managerInfo.getLoginTime(), Ip);
       return bean.getToken();
    }
    /**
     *创建并缓存 token
     * @param device 登陆设备
     * @param userType 用户类型
     * @param word 用户名
     * @param loginTime 登录时间
     * @param ip ip 地址
     * @return
     * @throws Exception
     */
    private TokenAndUuidBean createToken(Integer device, Integer userType, String word, Long loginTime, String ip) throws Exception {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        UmsMember loginInfo = new UmsMember(device, userType, word, loginTime, ip, SystemDefines.LOGIN_STATUS_OK);
        if (myRedisService.saveUmsMember(uuid, loginInfo) && myRedisService.saveLoginWord(word, uuid)) {
            asyncTask.cleanUserLoginOnDevice(word, device, loginTime);
            return new TokenAndUuidBean(JwtTokenUtil.generateToken(uuid, SystemDefines.JWT_SECRET, SystemDefines.JWT_EXPIRATION), uuid);
        }
        throw new MyException("登录失败");
    }


}
