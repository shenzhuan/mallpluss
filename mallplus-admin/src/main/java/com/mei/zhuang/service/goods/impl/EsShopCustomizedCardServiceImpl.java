package com.mei.zhuang.service.goods.impl;

import com.mei.zhuang.dao.goods.EsShopCustomizedCardMapper;
import com.mei.zhuang.service.goods.EsShopCustomizedCardService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopCustomizedCard;
import com.mei.zhuang.entity.goods.EsShopGoods;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EsShopCustomizedCardServiceImpl extends ServiceImpl<EsShopCustomizedCardMapper, EsShopCustomizedCard> implements EsShopCustomizedCardService {

   @Resource
   private EsShopCustomizedCardMapper esShopCustomizedCardMapper;

}
