package com.zscat.mallplus.notice;

import com.kuding.anno.ExceptionListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@ExceptionListener //异常通知的监控来自这个注解
@Async
public class NoticeComponents {

    public void createOrderSucess(String name) {
        System.out.println("这是一个参数：" + name);
        throw new NullPointerException("这是一个参数：" + name);
    }

    public void createOrderFail(String name) {
        System.out.println("这是一个参数：" + name);
        throw new NullPointerException("这是一个参数：" + name);
    }

    public void someMethod(String name) {
        System.out.println("这是一个参数：" + name);
        throw new NullPointerException("第一个异常");
    }

    public void anotherMethod(String name, int age) {
        System.out.println("这又是一个参数" + age);
        throw new IllegalArgumentException(name + ":" + age);
    }
}
