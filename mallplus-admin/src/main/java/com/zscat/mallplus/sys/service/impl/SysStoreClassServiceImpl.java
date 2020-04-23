package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysStoreClass;
import com.zscat.mallplus.sys.mapper.SysStoreClassMapper;
import com.zscat.mallplus.sys.service.ISysStoreClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
