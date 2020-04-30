package com.zscat.mallplus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "mallplus")
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
@Component
@Setter
@Getter
public class MallplusProperties {
    /**
     * 域名
     */
    private String domain;
    /**
     *默认图片
     */
    private String defaultIcon;
    /**
     *默认图片
     */
    private String defaultPassword;
    /**
     * 版本
     */
    private String  version ;
    /**
     * 项目名称
     */
    private String  name ;

    /**
     * 开发公司
     * mallplus科技
     */
    private String company;
    /**
     * mallplus.com
     */
    private String subdomain;
    /**
     * 备案号
     */
    private String code = "237524947";

}
