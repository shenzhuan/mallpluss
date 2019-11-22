package com.mei.zhuang.enums;

/**
 * ${DESCRIPTION}
 *
 * @author meizhuang team
 * @create 2017-06-14 8:36
 */
public enum TmallCodeEnum {

    //会员绑定状态查询

    //查询成功
    BIND_CODE_SUC("SUC"),
    // 会员不存在
    BIND_CODE_MEMBER_NOT_EXIST("E01"),
    //会员不存在可注册
    BIND_CODE_MEMBER_NOT_EXIST_REGIST("E04"),
    //会员已绑定
    BIND_CODE_MEMBER_BINDINGED("E02"),
    //系统繁忙或异常，可重试
    BIND_CODE_MEMBER_SYS_ERR("E05"),

    //会员绑定解绑处理

    //查询成功
    BIND_OPR_SUC("SUC"),
    //绑定失败，非品牌会 员，不可绑定
    BIND_OPR_MEMBER_NOT_EXIST("E02"),
    //会员已绑定
    BIND_OPR_MEMBER_BINDINGED("E04"),
    //会员被其他用户绑定
    BIND_OPR_MEMBER_OTHER_BINDINGED("E03"),
    //系统异常，可重试，
    BIND_OPR_SYS_ERR("E01"),


    //完成注册
    REGISTER_CODE_SUC("SUC"),
    //系统异常，可重试
    REGISTER_CODE_SYS_ERR("E01"),
    //注册失败，非品牌会员，不可注册
    REGISTER_CODE_NOT_SUPPORT("E02"),
    //注册失败，已被其他用户注册
    REGISTER_CODE_OTHER_MEMBER_EXISTS("E03"),
    //注册失败，该帐号已经注册
    REGISTER_CODE_REGISTED("E04"),

    //查询成功
    QUERY_CODE_SUC("SUC"),
    //不存在的会员
    QUERY_CODE_NOT_EXISTS("E01"),
    //会员未绑定
    QUERY_CODE_NOT_BINDINGED("E02"),
    //系统异常查询失败
    QUERY_CODE_SYS_ERR("E04");


    private String ret;

    private TmallCodeEnum(String ret) {
        this.ret = ret;
    }

    public String getValue() {
        return ret;
    }

}
