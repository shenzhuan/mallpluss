package com.mei.zhuang.service.sys;


import com.mei.zhuang.vo.sys.UserInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



public interface IUserService {


  UserInfo getUserByUsername(@PathVariable("username") String username);


  Integer getCurrentname(@RequestParam("userName") String userName);


  Integer updatestatus(@RequestParam("status") String status, @RequestParam("username") String username);

  /**
   * 根据用户id,判断接口是否有权限
   * @param userId
   * @param requestUri
   * @return
   */

  boolean hasApiAuth(@RequestParam("userId") Integer userId, @RequestParam("requestUri") String requestUri);

}
