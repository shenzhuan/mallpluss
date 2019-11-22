package com.mei.zhuang.annotation;

import com.arvato.common.dto.FeignTenantInfoDto;
import com.arvato.common.interceptor.CommonServiceFeignInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Description: 允许多租户feign请求
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignTenantInfoDto.class,CommonServiceFeignInterceptor.class})
@Documented
@Inherited
public @interface EnableFeignTenant{
}

