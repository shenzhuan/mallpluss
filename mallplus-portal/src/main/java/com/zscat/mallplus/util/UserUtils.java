package com.zscat.mallplus.util;


import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.MemberDetails;
import com.zscat.mallplus.vo.Rediskey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/5 19:48
 * @Description:
 */
@Service
public class UserUtils {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;

    public  UmsMember getCurrentMember() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String storeId = request.getParameter("storeid");
            String tokenPre = "authorization"+storeId ;
            String authHeader = request.getParameter(tokenPre);

            if (authHeader != null && authHeader.startsWith(tokenPre)) {
                String authToken = authHeader.substring(tokenPre.length());
                String username = jwtTokenUtil.getUserNameFromToken(authToken);
                if (ValidatorUtils.notEmpty(username)){
                    UmsMember member = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.MEMBER, username)),UmsMember.class);
                    return member;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new UmsMember();
        }
    }
}
