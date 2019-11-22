package com.mei.zhuang.service.sys;


import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.entity.sys.FrontUser;


public interface AuthService {

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    JSONObject login(String username, String password) throws Exception;

    /**
     * 刷新token
     * @param oldToken
     * @return
     * @throws Exception
     */
    String refresh(String oldToken) throws Exception;

    void validate(String token) throws Exception;

    /**
     * 获取平台用户信息
     * @param token
     * @return
     * @throws Exception
     */
    FrontUser getPlatformUserInfo(String token) throws Exception;


    /**
     * 获取具体租户的用户信息
     * @param token
     * @return
     * @throws Exception
     */
    FrontUser getUserInfo(String token, String tenantId) throws Exception;

    Boolean invalid(String token);
}
