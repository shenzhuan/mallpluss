package com.zscat.mallplus.ums.service.impl;

import com.zscat.mallplus.sys.entity.SysStoreClass;
import com.zscat.mallplus.sys.mapper.SysStoreClassMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.ums.service.ISysStoreClassService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author mallplus
* @date 2020-04-22
*/
@Service
public class SysStoreClassServiceImpl extends ServiceImpl<SysStoreClassMapper, SysStoreClass> implements ISysStoreClassService {

@Resource
private  SysStoreClassMapper sysStoreClassMapper;


}
