package com.zscat.mallplus.enums;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 交易API Constant
 *
 * @author dp
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BargainEnum {


    /**
     * 改变类型：1->增加；2->减少
     *
     * @author dp
     */
    public enum Status implements BaseEnum<Integer> {

        /**
         * 砍价中
         */
        INIT(1, "init"),

        /**
         * 砍价成功
         */
        SUCESS(2, "sussce"),
        /**
         * 支付成功
         */
        PAY(3, "pay");

        private int code;
        private String value;

        Status(int code, String value) {
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
