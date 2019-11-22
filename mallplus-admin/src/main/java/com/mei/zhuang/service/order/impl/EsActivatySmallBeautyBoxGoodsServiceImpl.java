package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.order.EsActivatySmallBeautyBoxGiftBoxMapper;
import com.mei.zhuang.dao.order.EsActivatySmallBeautyBoxGoodsMapper;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGoods;
import com.mei.zhuang.service.order.EsActivatySmallBeautyBoxGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsActivatySmallBeautyBoxGoodsServiceImpl extends ServiceImpl<EsActivatySmallBeautyBoxGoodsMapper, EsActivatySmallBeautyBoxGoods> implements EsActivatySmallBeautyBoxGoodsService {

    @Resource
    private EsActivatySmallBeautyBoxGoodsMapper esActivatySmallBeautyBoxGoodsMapper;
    @Resource
    private EsActivatySmallBeautyBoxGiftBoxMapper esActivatySmallBeautyBoxGiftBoxMapper;

    @Override
    public Map<String, Object> selPageList(EsActivatySmallBeautyBoxGoods entity) {
        Page<EsActivatySmallBeautyBoxGoods> page = new Page<>(entity.getCurrent(), entity.getSize());
        Map<String, Object> result = new HashMap<String, Object>();
        List<EsActivatySmallBeautyBoxGoods> list = esActivatySmallBeautyBoxGoodsMapper.selectPage(page, new QueryWrapper<>(entity)).getRecords();
        int count = esActivatySmallBeautyBoxGoodsMapper.count(entity);
        result.put("rows", list);
        result.put("total", count);
        result.put("current", entity.getCurrent());
        result.put("size", entity.getSize());
        return result;
    }

    @Override
    public Boolean inserts(List<EsActivatySmallBeautyBoxGoods> entity) {
        //1.删除活动下的阶段中的产品
        for (EsActivatySmallBeautyBoxGoods goods : entity) {
            boolean bool = this.removeById(goods.getId());
            if (bool == true) {
                bool = this.save(goods);
                if (bool == true) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /*@Override
    public List<EsActivatySmallBeautyBoxGiftBox> selectSmall(EsActivatySmallBeautyBoxGoods entity) {
        EsActivatySmallBeautyBoxGiftBox boxGiftBox = new EsActivatySmallBeautyBoxGiftBox();
        boxGiftBox.setProductCode(entity.getProductCode());

        return esActivatySmallBeautyBoxGiftBoxMapper.selectGiftBox(boxGiftBox);
    }*/

}
