package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.AdminDayStatics;
import com.zscat.mallplus.sys.mapper.AdminDayStaticsMapper;
import com.zscat.mallplus.sys.service.IAdminDayStaticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author mallplus
* @date 2020-04-10
*/
@Service
public class AdminDayStaticsServiceImpl extends ServiceImpl
<AdminDayStaticsMapper, AdminDayStatics> implements IAdminDayStaticsService {

@Resource
private  AdminDayStaticsMapper adminDayStaticsMapper;


}
