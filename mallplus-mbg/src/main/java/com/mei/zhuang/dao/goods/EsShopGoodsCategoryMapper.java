package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-16
 */
public interface EsShopGoodsCategoryMapper extends BaseMapper<EsShopGoodsCategory> {

    List<EsShopGoodsCategory> selectAll();

    /**
     * 根据父id,分类等级查询子分类
     * parentId： 父Id level：分类级别
     */
    List<EsShopGoodsCategory> getSonGoodsCategory(@Param("parentId") Long parentId, @Param("level") Integer level, @Param("status") Integer status);


    /**
     * 根据id批量修改状态
     *
     * @param map ids  status
     */
    Boolean updateStatusBatchById(Map<String, Object> map);


    /**
     * 批量物理删除
     *
     * @param ids
     */
    void dataDelelteById(List<Long> ids);

    List<EsShopGoodsCategory> selGoodsCategorySubClass(@Param("parentId") Long parentId);

    Integer updGoodsCategoryStatus(@Param("id") Long id, @Param("status") Integer status);

    Integer delGoodsCategory(@Param("id") Long id);

    Integer updDisplayOrder(@Param("id") Long id, @Param("displayOrder") Integer displayOrder);

    //  List<EsShopGoodsCategory> list(Pagination page, @Param("current") Integer current, @Param("size") Integer size);

    int count();

    public EsShopGoodsCategory selDetail(@Param("id") Long id);

    List<EsShopGoodsCategory> selEsShopGoodsCategory();

    EsShopGoodsCategory selCategory(@Param("name") String name);

    List<EsShopGoodsCategory> selCategoryByLevel(@Param("level") Integer lelve, @Param("parentId") Long parentId);

    List<EsShopGoodsCategory> selAll();
}

