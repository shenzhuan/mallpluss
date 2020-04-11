package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.oms.service.IAdminDayStaticsService;
import com.zscat.mallplus.sys.entity.AdminDayStatics;
import com.zscat.mallplus.sys.mapper.AdminDayStaticsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mallplus
 * @date 2020-04-10
 */
@Service
public class AdminDayStaticsServiceImpl extends ServiceImpl<AdminDayStaticsMapper, AdminDayStatics> implements IAdminDayStaticsService {

    @Resource
    private AdminDayStaticsMapper adminDayStaticsMapper;


}
