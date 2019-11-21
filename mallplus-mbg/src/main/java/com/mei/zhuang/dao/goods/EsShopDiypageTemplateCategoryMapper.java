package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopDiypageTemplateCategory;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-06
 */
public interface EsShopDiypageTemplateCategoryMapper extends BaseMapper<EsShopDiypageTemplateCategory> {

    List<EsShopDiypageTemplateCategory> selectList();
}
