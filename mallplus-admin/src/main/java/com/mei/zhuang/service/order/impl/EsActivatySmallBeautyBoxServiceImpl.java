package com.mei.zhuang.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.order.EsActivatySmallBeautyBoxGiftBoxMapper;
import com.mei.zhuang.dao.order.EsActivatySmallBeautyBoxGoodsMapper;
import com.mei.zhuang.dao.order.EsActivatySmallBeautyBoxMapper;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBox;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGiftBox;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGoods;
import com.mei.zhuang.service.order.EsActivatySmallBeautyBoxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小美盒定制活动列表
 */
@Service
public class EsActivatySmallBeautyBoxServiceImpl extends ServiceImpl<EsActivatySmallBeautyBoxMapper, EsActivatySmallBeautyBox> implements EsActivatySmallBeautyBoxService {

    @Resource
    private EsActivatySmallBeautyBoxMapper esActivatySmallBeautyBoxMapper;
    @Resource
    private EsActivatySmallBeautyBoxGoodsMapper esActivatySmallBeautyBoxGoodsMapper;
    @Resource
    private EsActivatySmallBeautyBoxGiftBoxMapper esActivatySmallBeautyBoxGiftBoxMapper;

    @Override
    public Map<String, Object> selPageList(EsActivatySmallBeautyBox entity) {
        Page<EsActivatySmallBeautyBox> page = new Page<EsActivatySmallBeautyBox>(entity.getCurrent(), entity.getSize());
        Map<String, Object> result = new HashMap<String, Object>();
        List<EsActivatySmallBeautyBox> selList = esActivatySmallBeautyBoxMapper.selList(entity);
        int count = esActivatySmallBeautyBoxMapper.count(entity);
        result.put("rows", selList);
        result.put("total", count);
        result.put("current", entity.getCurrent());
        result.put("size", entity.getSize());
        return result;
    }

    @Override
    public boolean deleteById(Long id) {
        Integer num = esActivatySmallBeautyBoxMapper.deleteById(id);
        //1.删除活动
        if (num > 0) {
            //2.删除产品
            EsActivatySmallBeautyBoxGoods box = new EsActivatySmallBeautyBoxGoods();
            box.setActivatyId(id);
            esActivatySmallBeautyBoxGoodsMapper.delete(new QueryWrapper<>(box));
            //3.删除礼盒
            EsActivatySmallBeautyBoxGiftBox giftBox = new EsActivatySmallBeautyBoxGiftBox();
            giftBox.setActivatyId(id);
            esActivatySmallBeautyBoxGiftBoxMapper.delete(new QueryWrapper<>(giftBox));
            return true;
        }

        return false;
    }

    @Override
    public List<EsActivatySmallBeautyBox> select(EsActivatySmallBeautyBox entity) {
        return esActivatySmallBeautyBoxMapper.select(entity);
    }
}
