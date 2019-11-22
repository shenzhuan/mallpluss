package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopActivity;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-06-25
 */
public interface EsShopActivityMapper extends BaseMapper<EsShopActivity> {

    List<EsShopActivity> selPageList(EsShopActivity entity);

    Integer count(EsShopActivity entity);

}
