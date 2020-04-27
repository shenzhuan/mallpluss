package com.zscat.mallplus.enums;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 交易API Constant
 *
 * @author dp
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UmsStatusEnum {


    /**
     * 标签类型：
     *
     * @author mallplus
     */
    public enum TagType implements BaseEnum<Integer> {

        /**
         * 会员标签
         */
        INIT(1, "会员标签"),
        /**
         * 审核成功
         */
        FAIL(2, "商品标签"),
        /**
         * 审核失败
         */
        SUCESS(3, "文章标签"),
        ;

        private int code;
        private String value;

        TagType(int code, String value) {
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
     * 标签方式类型：
     *
     * @author mallplus
     */
    public enum YesNoType implements BaseEnum<Integer> {

        /**
         * 自动标签
         */
        YES(1, "自动标签"),

        /**
         * 手动标签
         */
        NO(2, "手动标签"),
        ;

        private int code;
        private String value;

        YesNoType(int code, String value) {
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
