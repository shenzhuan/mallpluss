package com.zscat.mallplus.util;


import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.vo.MemberDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/5 19:48
 * @Description:
 */
public class UserUtils {


    public static UmsMember getCurrentMember() {
        try {
            SecurityContext ctx = SecurityContextHolder.getContext();
            Authentication auth = ctx.getAuthentication();
            if (auth==null || auth.getPrincipal()==null){
                return new UmsMember();
            }
            if ("anonymousUser".equals(auth.getPrincipal())){
                return new UmsMember();
            }
            System.out.println(auth.getPrincipal());
            MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
            System.out.println(memberDetails.toString());
            return memberDetails.getUmsMember();
        } catch (Exception e) {
            e.printStackTrace();
            return new UmsMember();
        }
    }
}
