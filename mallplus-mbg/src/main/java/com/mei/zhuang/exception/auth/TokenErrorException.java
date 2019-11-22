package com.mei.zhuang.exception.auth;


import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.exception.BaseException;

/**
 * Created by john on 2017/9/8.
 */
public class TokenErrorException extends BaseException {
    public TokenErrorException(String message, int status) {
        super(message, CommonConstant.EX_TOKEN_ERROR_CODE);
    }
}
