package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsShopDiypageTemplateCategoryMapper;
import com.arvato.service.goods.api.service.EsShopDiypageTemplateCategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopDiypageTemplateCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class EsShopDiypageTemplateCategoryServiceImpl extends ServiceImpl<EsShopDiypageTemplateCategoryMapper, EsShopDiypageTemplateCategory> implements EsShopDiypageTemplateCategoryService {

    @Resource
    private EsShopDiypageTemplateCategoryMapper categoryMapper;

    @Override
    public List<EsShopDiypageTemplateCategory> selTemplateCategory() {
        return categoryMapper.selectList();
    }
}
