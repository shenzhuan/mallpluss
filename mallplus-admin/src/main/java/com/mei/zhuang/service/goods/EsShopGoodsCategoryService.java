package com.mei.zhuang.service.goods;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoodsCategory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface EsShopGoodsCategoryService extends IService<EsShopGoodsCategory> {

    public Object getGoodsCategoryByPage(EsShopGoodsCategory entity);

    public Object selGoodsCategorySubClass(Long parentId);

    @Transactional(rollbackFor = Exception.class)
    public Object saveGoodsCategory(EsShopGoodsCategory entity);

    public Object updGoodsCategoryStatus(Long id, Integer status);

    public List<EsShopGoodsCategory> countSubClass(Long id);

    public Object getSonGoodsCategory(Integer parentId, Integer level);

    //删除分类
    public Integer delGoodsCategory(Long id);


    public Object updDisplayOrder(EsShopGoodsCategory entity);

    public Object updateGoodsCategory(EsShopGoodsCategory entity);


    public Object deleteGoodsCategory(Long id, @RequestParam("isDataDelete") Boolean isDataDelete);

    public Object getGoodsCategoryById(Long id);

    public Object selectAll();

    public void dataDelelteById(List<Long> ids);

    public void logicDeleteById(List<Long> ids);

    Map<String, Object> list(Integer current, Integer size);

    public EsShopGoodsCategory selDetail(Long id);

    public List<EsShopGoodsCategory> selEsShopGoodsCategory();

    public EsShopGoodsCategory selCategory(String name);
}
