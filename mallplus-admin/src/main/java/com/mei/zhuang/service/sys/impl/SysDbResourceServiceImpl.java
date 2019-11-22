package com.mei.zhuang.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.sys.SysDbResourceMapper;
import com.mei.zhuang.entity.sys.SysDbResource;
import com.mei.zhuang.service.sys.ISysDbResourceService;
import com.mei.zhuang.vo.DictData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
@Service
public class SysDbResourceServiceImpl extends ServiceImpl<SysDbResourceMapper, SysDbResource> implements ISysDbResourceService {

    @Override
    public List<DictData> getDictList() {
        return this.baseMapper.getDictList();
    }
}
