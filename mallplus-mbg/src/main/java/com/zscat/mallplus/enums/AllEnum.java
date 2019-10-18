package com.zscat.mallplus.enums;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 交易API Constant
 *
 * @author dp
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AllEnum {



    /**
     * 改变类型：1->增加；2->减少
     *
     * @author dp
     */
    public enum ChangeType implements BaseEnum<Integer> {

        /**
         * 限价交易
         */
        Add(1, "add"),

        /**
         * 市价交易
         */
        Min(2, "min"),
        ;

        private int code;
        private String value;

        ChangeType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }
    /**
     * 积分来源
     *
     * @author dp
     */
    public enum ChangeSource implements BaseEnum<Integer> {

        /**
         * 下单
         */
        order(1, "order"),

        /**
         * 登录
         */
        login(2, "login"),
        /**
         * 注册
         */
        register(3, "register"),
        ;

        private int code;
        private String value;

        ChangeSource(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }

}
