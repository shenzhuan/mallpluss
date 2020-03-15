package com.zscat.mallplus.notice;


import org.springframework.stereotype.Component;

@Component
public class AnotherComponent {

   // @ExceptionListener("user2") //注意注解位置与参数
    public void giveMeError() {
        throw new NullPointerException("又是一个com.zscat.mallplus.notice有故事的异常");
    }
}
