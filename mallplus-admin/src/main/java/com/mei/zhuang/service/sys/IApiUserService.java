package com.mei.zhuang.service.sys;

import com.mei.zhuang.entity.sys.CrmApiUser;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/3
 */

public interface IApiUserService {

    /**
     * 根据用户名获取api用户信息
     * @param name
     * @return
     */

    CrmApiUser getApiUserByName(@RequestParam("name") String name);

    /**
     * 根据用户id,判断接口是否有权限
     * @param apiUserId
     * @param requestUri
     * @return
     */

    boolean hasApiAuth(@RequestParam("apiUserId") String apiUserId, @RequestParam("requestUri") String requestUri);




}
