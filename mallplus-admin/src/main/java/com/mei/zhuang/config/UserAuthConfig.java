package com.mei.zhuang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserAuthConfig {

    @Value("${auth.user.pub-key.path}")
    private String pubKeyPath;
    @Value("${auth.user.pri-key.path}")
    private String priKeyPath;
    @Value("${auth.user.expire}")
    private int expire;
    @Value("${auth.user.token-header}")
    private String tokenHeader;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getToken(HttpServletRequest request){
        return request.getHeader(this.getTokenHeader());
    }

}
