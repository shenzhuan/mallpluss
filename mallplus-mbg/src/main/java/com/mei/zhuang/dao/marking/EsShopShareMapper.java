package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopShare;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-08-20
 */
public interface EsShopShareMapper extends BaseMapper<EsShopShare> {

    List<EsShopShare> ShareList();

    Integer updateStatus(Long id, Integer status);

    Integer status(Integer status);

}
