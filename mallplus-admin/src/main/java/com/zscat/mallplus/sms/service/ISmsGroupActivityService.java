package com.zscat.mallplus.sms.service;

import com.zscat.mallplus.sms.entity.SmsGroupActivity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zscat
 * @since 2019-10-12
 */
public interface ISmsGroupActivityService extends IService<SmsGroupActivity> {

    int updateShowStatus(Long ids, Integer status);
}
