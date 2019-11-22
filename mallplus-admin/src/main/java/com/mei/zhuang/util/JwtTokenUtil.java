package com.mei.zhuang.util;


import com.mei.zhuang.config.UserAuthConfig;
import com.mei.zhuang.jwt.IJWTInfo;
import com.mei.zhuang.jwt.JWTHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class JwtTokenUtil {

    @Resource
    private UserAuthConfig userAuthConfig;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return JWTHelper.generateToken(jwtInfo,userAuthConfig.getPriKeyPath(),userAuthConfig.getExpire());
    }

    public IJWTInfo  getInfoFromToken(String token) throws Exception {
        return JWTHelper.getInfoFromToken(token,userAuthConfig.getPubKeyPath());
    }


}
