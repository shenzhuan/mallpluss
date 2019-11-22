package com.mei.zhuang.exception.auth;


import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.exception.BaseException;

/**
 * Created by john on 2017/9/10.
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstant.EX_USER_INVALID_CODE);
    }
}
