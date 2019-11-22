package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopCustomizedCard;

import java.util.List;

public interface EsShopCustomizedCardMapper extends BaseMapper<EsShopCustomizedCard> {

    //  List<EsShopCustomizedCard> selCardPage(Pagination page, EsShopCustomizedCard entity);

    Integer count();

    List<EsShopCustomizedCard> list();
}
