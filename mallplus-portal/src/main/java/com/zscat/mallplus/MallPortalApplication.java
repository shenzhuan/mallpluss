package com.zscat.mallplus;

import com.zscat.mallplus.pay.config.StartupRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
@MapperScan({"com.zscat.mallplus.mapper", "com.zscat.mallplus.ums.mapper", "com.zscat.mallplus.sms.mapper", "com.zscat.mallplus.cms.mapper", "com.zscat.mallplus.sys.mapper", "com.zscat.mallplus.oms.mapper", "com.zscat.mallplus.pms.mapper"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class MallPortalApplication {


    public static void main(String[] args) {
        System.out.println("start-------------");
        SpringApplication.run(MallPortalApplication.class, args);
        System.out.println("end-------------");

    }

    @Bean
    public StartupRunner startupRunner() {
        return new StartupRunner();
    }
}
