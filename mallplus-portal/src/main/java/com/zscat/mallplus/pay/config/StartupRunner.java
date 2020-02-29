package com.zscat.mallplus.pay.config;

import com.zscat.mallplus.core.kit.HttpKit;
import com.zscat.mallplus.notice.NoticeComponents;
import com.zscat.mallplus.pay.custom.OkHttpKit;
import com.zscat.mallplus.unionpay.SDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

@Order(1)
public class StartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);


    @Autowired
    private NoticeComponents noticeComponents;

    @Override
    public void run(String... args) throws Exception {
        logger.info("startup runner");
        // 银联加载配置
        SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
        // 自定义 Http 客户端
        HttpKit.setDelegate(new OkHttpKit());

        //anotherComponent.giveMeError();
    }

}
