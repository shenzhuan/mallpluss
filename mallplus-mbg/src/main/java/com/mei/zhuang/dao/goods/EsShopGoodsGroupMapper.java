package com.mei.zhuang.dao.goods;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-04-16
 */
public interface EsShopGoodsGroupMapper extends BaseMapper<EsShopGoodsGroup> {

    Integer add(EsShopGoodsGroup esShopGoodsGroup);

    //分组连表查询
    // List<Map<String, Object>> goodsgrouplist(Page<EsShopGoodsGroup> esShopGoodsGroupPage, EsShopGoodsGroup esShopGoodsGroup);

    //修改状态
    Integer updatestate(@Param("id") Long id, @Param("status") Integer status);

    //选择商品分类查询
    List<Map<String, Object>> goodscatelist(EsShopGoods goods);

    //  List<EsShopGoodsGroup> list(Pagination page, @Param("current") Integer current, @Param("size") Integer size, @Param("groupName") String name);

    int count();

    EsShopGoodsGroup selEsShopGoodsGroup(@Param("name") String name, @Param("createTime") Date createTime);

    Integer selCateCount(@Param("cateName") String cateName);

//    List<EsShopOrder> selOrderListByGoodsParam(GoodsAnalyzeParam param);

}
