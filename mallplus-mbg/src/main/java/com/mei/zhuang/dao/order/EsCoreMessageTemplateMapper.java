package com.mei.zhuang.dao.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.order.EsCoreMessageTemplate;

import java.util.List;


public interface EsCoreMessageTemplateMapper extends BaseMapper<EsCoreMessageTemplate> {

   // List<EsCoreMessageTemplate> select(Pagination page, EsCoreMessageTemplate entity);

    Integer count(EsCoreMessageTemplate entity);
}
