package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsDecorateTemplateMapper;
import com.mei.zhuang.dao.goods.EsDecorateTemplatePageMapper;
import com.mei.zhuang.entity.goods.EsDecorateTemplate;
import com.mei.zhuang.entity.goods.EsDecorateTemplatePage;
import com.mei.zhuang.service.goods.EsDecorateTemplateService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:14
 * @Description:
 */
@Slf4j
@Api(value = "商品模版管理", description = "", tags = {"商品模版管理"})
@Service
public class EsDecorateTemplateServiceImpl extends ServiceImpl<EsDecorateTemplateMapper, EsDecorateTemplate> implements EsDecorateTemplateService {
    @Resource
    private EsDecorateTemplateMapper decorateTemplateMapper;

    @Resource
    private EsDecorateTemplatePageMapper decorateTemplatePageMapper;

    @Override
    public boolean save(EsDecorateTemplate entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setType(1);
        decorateTemplateMapper.insert(entity);
        EsDecorateTemplatePage page = new EsDecorateTemplatePage();
        page.setCreateTime(new Date());
        page.setShopId(entity.getShopId());
        page.setTemplateId(entity.getId());

        return decorateTemplatePageMapper.insert(page)>0;
    }

    @Override
    public Object release(EsDecorateTemplate entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setReleaseTime(new Date());
        entity.setType(2);
        decorateTemplateMapper.insert(entity);
        return null;
    }
}
