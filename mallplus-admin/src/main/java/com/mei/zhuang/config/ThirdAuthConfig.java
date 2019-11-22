package com.mei.zhuang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ThirdAuthConfig {

    @Value("${auth.third.pub-key.path:null}")
    private String pubPath;
    @Value("${auth.user.pri-key.path}")
    private String priPath;
    @Value("${auth.third.token-header:null}")
    private String tokenHeader;
    @Value("${auth.third.expire}")
    private int expire;

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public void setPubPath(String pubPath) {
        this.pubPath = pubPath;
    }

    public String getPubPath() {
        return pubPath;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getPriPath() {
        return priPath;
    }

    public void setPriPath(String priPath) {
        this.priPath = priPath;
    }
}
