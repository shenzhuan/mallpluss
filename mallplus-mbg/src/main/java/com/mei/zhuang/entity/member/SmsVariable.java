package com.mei.zhuang.entity.member;

import lombok.Data;

/**
 * @Auther: Tiger
 * @Date: 2019-06-06 11:35
 * @Description:该类是短信模板内容 变量
 */
@Data
public class SmsVariable {

    /**
     * 短信模板变量名称
     */
    private String variableName;

    /**
     * 短信类型功能id （取的是：ec_sms_function表中的id）
     */
    private Long smsTypeFunctionId;

    /**
     * 短信类型功能名称（取的是：ec_sms_function表中的名称）
     */
    private String name;


}
