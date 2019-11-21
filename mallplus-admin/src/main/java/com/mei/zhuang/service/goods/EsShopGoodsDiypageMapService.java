package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoodsDiyPageMap;
import io.swagger.annotations.ApiOperation;

public interface EsShopGoodsDiypageMapService extends IService<EsShopGoodsDiyPageMap> {

    @ApiOperation("查询商品详情自定义模版")
    EsShopGoodsDiyPageMap select(Long goodsId);
}
