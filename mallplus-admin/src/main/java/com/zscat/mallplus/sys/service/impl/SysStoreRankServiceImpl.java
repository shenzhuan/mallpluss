package com.zscat.mallplus.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.sys.entity.SysStoreRank;
import com.zscat.mallplus.sys.mapper.SysStoreRankMapper;
import com.zscat.mallplus.sys.service.ISysStoreRankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mallplus
 * @date 2020-04-11
 */
@Service
public class SysStoreRankServiceImpl extends ServiceImpl<SysStoreRankMapper, SysStoreRank> implements ISysStoreRankService {

    @Resource
    private SysStoreRankMapper sysStoreRankMapper;


}
