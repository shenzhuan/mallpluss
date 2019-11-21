package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.order.EsAppletTemplates;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-06-16
 */
public interface EsAppletTemplatesMapper extends BaseMapper<EsAppletTemplates> {

    List<EsAppletTemplates> select(Pagination page, EsAppletTemplates entity);
    Integer count();
}
