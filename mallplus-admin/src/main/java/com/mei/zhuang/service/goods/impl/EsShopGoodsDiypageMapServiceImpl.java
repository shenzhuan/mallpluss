package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopGoodsDiypageMapMapper;
import com.arvato.service.goods.api.service.EsShopGoodsDiypageMapService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoodsDiyPageMap;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Api(value = "自定义模版商品详情管理", description = "", tags = {"自定义模版商品详情管理"})
@Service
public class EsShopGoodsDiypageMapServiceImpl extends ServiceImpl<EsShopGoodsDiypageMapMapper, EsShopGoodsDiyPageMap> implements EsShopGoodsDiypageMapService {

    @Resource
    private EsShopGoodsDiypageMapMapper esShopGoodsDiypageMapMapper;


    @Override
    public EsShopGoodsDiyPageMap select(Long goodsId) {
        EsShopGoodsDiyPageMap pageMap = new EsShopGoodsDiyPageMap();
        pageMap.setGoodsId(goodsId);
        return esShopGoodsDiypageMapMapper.selectOne(pageMap);
    }
}
