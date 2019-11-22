package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

public interface EsShopGoodsOptionService extends IService<EsShopGoodsOption> {

    @ApiOperation("查询单个商品的所有SKU")
    List<EsShopGoodsOption> listGoodsOptionDetail(Long goodsId);

    Map<String, Object> selPageList(EsShopGoodsOption entity);
}
