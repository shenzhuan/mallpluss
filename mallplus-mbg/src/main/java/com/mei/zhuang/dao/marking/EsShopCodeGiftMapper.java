package com.mei.zhuang.dao.marking;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.marking.EsShopCodeGift;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
public interface EsShopCodeGiftMapper extends BaseMapper<EsShopCodeGift> {

 //   List<EsShopCodeGift> selPageList(Pagination page, EsShopCodeGift entity);

    Integer count(EsShopCodeGift entity);

}
