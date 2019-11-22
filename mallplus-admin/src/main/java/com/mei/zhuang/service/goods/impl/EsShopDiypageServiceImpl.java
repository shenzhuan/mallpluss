package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopDiypageMapper;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import com.mei.zhuang.service.goods.EsShopDiypageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:14
 * @Description:
 */
@Slf4j
@Service
public class EsShopDiypageServiceImpl extends ServiceImpl<EsShopDiypageMapper, EsShopDiypage> implements EsShopDiypageService {

    @Resource
    private EsShopDiypageMapper esShopDiypageMapper;


    @Override
    public Object updStatus(Long id, Integer status, Integer typeId) {
        EsShopDiypage diypage = new EsShopDiypage();
        diypage.setId(id);
        diypage.setStatus(status);
        Object obj = esShopDiypageMapper.updateById(diypage);
        return obj;
    }

    @Override
    public Integer selDiyPageTypeId(Integer typeId, Long id) {
        return esShopDiypageMapper.selDiyPageTypeId(typeId, id);
    }

    @Override
    public EsShopDiypage selDiyDetail(Integer id) {
        return esShopDiypageMapper.selDiyDetail(id);
    }

    @Override
    public Integer selectCounts(Long id, String name) {
        return esShopDiypageMapper.selectCounts(id, name);
    }
}
