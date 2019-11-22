package com.mei.zhuang.redis.constant;

/**
 * redis 工具常量
 *
 * @author mall
 * @date 2018/5/21 11:59
 */
public class RedisToolsConstant {
    /**
     * single Redis
     */
    public final static int SINGLE = 1;
    /**
     * Redis cluster
     */
    public final static int CLUSTER = 2;
    public static String GOODSDETAIL = "GOODSDETAIL:%s";

    private RedisToolsConstant() {
        throw new IllegalStateException("Utility class");
    }
}
