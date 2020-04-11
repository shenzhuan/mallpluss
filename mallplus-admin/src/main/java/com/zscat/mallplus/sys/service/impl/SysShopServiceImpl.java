package com.zscat.mallplus.sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.sys.entity.SysShop;
import com.zscat.mallplus.sys.mapper.SysShopMapper;
import com.zscat.mallplus.sys.service.ISysShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mallplus
 * @date 2020-03-29
 */
@Service
public class SysShopServiceImpl extends ServiceImpl<SysShopMapper, SysShop> implements ISysShopService {

    @Resource
    private SysShopMapper sysShopMapper;


}
