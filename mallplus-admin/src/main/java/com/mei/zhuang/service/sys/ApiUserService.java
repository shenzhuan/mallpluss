package com.mei.zhuang.service.sys;

/**
 * @Description: 接口用户服务
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/3
 */
public interface ApiUserService {

    /**
     * 查询是否有接口权限
     * @param apiUserId
     * @param requestUri
     * @return
     */
    boolean hasApiAuth(String apiUserId, String requestUri);
}
