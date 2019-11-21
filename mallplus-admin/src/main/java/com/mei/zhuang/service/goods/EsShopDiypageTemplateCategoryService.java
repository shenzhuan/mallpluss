package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopDiypageTemplateCategory;

import java.util.List;

public interface EsShopDiypageTemplateCategoryService extends IService<EsShopDiypageTemplateCategory> {

    List<EsShopDiypageTemplateCategory> selTemplateCategory();

}
