package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import org.apache.ibatis.annotations.Param;

public interface EsShopCustomizedBasicMapper extends BaseMapper<EsShopCustomizedBasic> {

    EsShopCustomizedBasic detail(@Param("id") Long id);
}
