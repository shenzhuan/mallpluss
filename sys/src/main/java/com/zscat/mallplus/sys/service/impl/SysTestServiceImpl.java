package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysTest;
import com.zscat.mallplus.sys.mapper.SysTestMapper;
import com.zscat.mallplus.sys.service.ISysTestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author mallplus
* @date 2019-12-02
*/
@Service
public class SysTestServiceImpl extends ServiceImpl<SysTestMapper, SysTest> implements ISysTestService {


    private final SysTestMapper sysTestMapper;


}
