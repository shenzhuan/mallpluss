package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopGoodsOptionMapper;
import com.mei.zhuang.dao.goods.EsShopGoodsSpecItemMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.goods.EsShopGoodsSpecItem;
import com.mei.zhuang.service.goods.EsShopGoodsOptionService;
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
            Page<EsShopGoodsOption> page = new Page<EsShopGoodsOption>(entity.getCurrent(), entity.getSize());
            IPage<EsShopGoodsOption> listOption = optionMapper.selectPage(page,new QueryWrapper<>(entity));
            //查询所有item;
            List<EsShopGoodsSpecItem> listSpecItem = esShopGoodsSpecItemMapper.selectSpecItemsGoodsId(entity.getGoodsId());
            if(listSpecItem != null){
                for (EsShopGoodsSpecItem specItem:listSpecItem) {
                    for (EsShopGoodsOption goodsOption:listOption.getRecords()) {
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
