package com.mei.zhuang.vo;

import lombok.Data;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/17 17:29
 * @Description:
 */
@Data
public class LoginVo {
    //临时登录凭证
    private String code;
    //用户非敏感信息缓存数据
    private String userInfos;
    //签名
    private String signature;
    private Long shopId=1l;
    //用户敏感信息
    private String encryptedData;
    //解密算法的向量
    private String iv;
}
