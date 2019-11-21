package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.goods.EsStartAdvertising;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsStartAdvertisingMapper extends BaseMapper<EsStartAdvertising> {

   // List<EsStartAdvertising> select(Pagination page, EsStartAdvertising entity);

    Integer count(EsStartAdvertising entity);

    Integer countStatus(@Param("id") Long id);
}
