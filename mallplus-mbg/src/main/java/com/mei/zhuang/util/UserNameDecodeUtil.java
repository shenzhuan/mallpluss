package com.mei.zhuang.util;


import com.mei.zhuang.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Description: request header 用户名编码转换
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/24
 */
@Slf4j
public class UserNameDecodeUtil {

    public static  String getDecodeUserName(HttpServletRequest request){
        String userName = request.getHeader(CommonConstant.CONTEXT_KEY_USERNAME);
        if(StringUtils.isNotEmpty(userName)){
            try {
                userName = URLDecoder.decode(userName,"utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(),e);
            }
        }
        return userName;
    }

}
