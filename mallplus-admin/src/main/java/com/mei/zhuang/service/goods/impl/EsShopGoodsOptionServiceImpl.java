package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopGoodsOptionMapper;
import com.arvato.service.goods.api.orm.dao.EsShopGoodsSpecItemMapper;
import com.arvato.service.goods.api.service.EsShopGoodsOptionService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.goods.EsShopGoodsSpecItem;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Api(value = "商品SKU管理", description = "", tags = {"商品SKU管理"})
@Service
public class EsShopGoodsOptionServiceImpl  extends ServiceImpl<EsShopGoodsOptionMapper, EsShopGoodsOption> implements EsShopGoodsOptionService {

    @Resource
    private EsShopGoodsOptionMapper optionMapper;
    @Resource
    private EsShopGoodsSpecItemMapper esShopGoodsSpecItemMapper;
    @Override
    public List<EsShopGoodsOption> listGoodsOptionDetail(Long goodsId) {
        EsShopGoodsOption option = new EsShopGoodsOption();
        option.setGoodsId(goodsId);
        return optionMapper.selectList(new QueryWrapper<>(option));
    }

    @Override
    public Map<String, Object> selPageList(EsShopGoodsOption entity) {
        Map<String,Object> result = new HashMap<String,Object>();
        List<EsShopGoodsOption> list = new CopyOnWriteArrayList<>();
        EsShopGoodsOption option = new EsShopGoodsOption();
        option.setGoodsId(entity.getGoodsId());
        try{
            Page<EsShopGoods> page = new Page<EsShopGoods>(entity.getCurrent(), entity.getSize());
            List<EsShopGoodsOption> listOption = optionMapper.selPageList(page,entity);
            //查询所有item;
            List<EsShopGoodsSpecItem> listSpecItem = esShopGoodsSpecItemMapper.selectSpecItemsGoodsId(entity.getGoodsId());
            if(listSpecItem != null){
                for (EsShopGoodsSpecItem specItem:listSpecItem) {
                    for (EsShopGoodsOption goodsOption:listOption) {
                        String[] attr =goodsOption.getSpecIds().split(",");
                        for (int i=0 ; i<attr.length; i++){
                            if(Long.parseLong(attr[i]) == specItem.getId()){
                                list.add(goodsOption);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        LinkedHashSet<EsShopGoodsOption> set = new LinkedHashSet<EsShopGoodsOption>(list.size());
        List<EsShopGoodsOption> newList = new ArrayList<EsShopGoodsOption>();
        set.addAll(list);
        newList.addAll(set);
        result.put("rows",newList);
        result.put("total", optionMapper.selectCount(new QueryWrapper<>(option)));
        result.put("current", entity.getCurrent());
        result.put("size", entity.getSize());
        return result;
    }
}
