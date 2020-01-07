package com.zscat.mallplus.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.cms.entity.CmsZhaoPin;
import com.zscat.mallplus.cms.mapper.CmsZhaoPinMapper;
import com.zscat.mallplus.cms.service.ICmsZhaoPinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mallplus
 * @date 2020-01-06
 */
@Service
public class CmsZhaoPinServiceImpl extends ServiceImpl
        <CmsZhaoPinMapper, CmsZhaoPin> implements ICmsZhaoPinService {

    @Resource
    private CmsZhaoPinMapper cmsZhaoPinMapper;


}
