package com.mei.zhuang.dao.goods;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsCategory;
import com.mei.zhuang.vo.data.goods.GoodsRankTopParam;
import com.mei.zhuang.vo.goods.GoodsQuery;
import io.swagger.annotations.ApiOperation;
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
public interface EsShopGoodsMapper extends BaseMapper<EsShopGoods> {

    int decrGoodsStock(@Param("id") long id, @Param("count") int count);

    int addGoodsStock(@Param("id") long id, @Param("count") int count);

    /**
     * 修改商品状态
     *
     * @param id
     * @param status
     * @return
     */
    Integer updEsShopGoodsStatus(@Param("id") Long id, @Param("status") Integer status, @Param("date") Date date);

    /**
     * 修改商品状态
     *
     * @param id
     * @param status
     * @return
     */
    Integer updEsShopGoodsStatusTime(@Param("id") Long id, @Param("status") Integer status, @Param("time") Date time);

    /**
     * 删除商品（逻辑删除）：将商品放到回收站
     *
     * @param id
     * @return
     */
    Integer updEsShopGoodsIsDel(@Param("id") Long id, @Param("date") Date date);

    /**
     * 刪除回收站垃圾
     *
     * @param id
     * @return
     */
    Integer delRecycleShop(@Param("id") Long id);

    Integer delGoodsCateMap(@Param("goodsId") Long id);

    /**
     * 查询一级分类
     *
     * @return
     */
    List<EsShopGoodsCategory> selShopGoodsCategoryOne();

    /**
     * 查询一级分类 没有禁用的
     *
     * @return
     */
    List<EsShopGoodsCategory> selDisplayShopGoodsCategoryOne();

    /**
     * 查询二级分类
     *
     * @param parentId
     * @return
     */
    List<EsShopGoodsCategory> selShopGoodsCategoryTwo(@Param("parentId") Long parentId, @Param("level") Integer level);

    /**
     * 查詢分類名稱
     *
     * @param categoryId
     * @return
     */
    String selCategoryName(@Param("分类ID") Long categoryId);

    /**
     * 查詢商品分組
     */
    List<EsShopGoodsCategory> selShopGoodsGroup();

    /**
     * 查询根据名称赠品商品
     */
    List<EsShopGoods> selectgiftsgoods(@Param("title") String title);

    List<EsShopGoods> selGoodsPageList(GoodsQuery esShopGoods);

    List<EsShopGoods> selGoodsPageLists(GoodsQuery esShopGoods);

    int count(GoodsQuery esShopGoods);

    EsShopGoods selEsShopGoods(@Param("id") Long id);

    EsShopGoods selEsShopGoodsTitle(@Param("title") String title);

    EsShopGoods selShopGoodsDetail(@Param("id") Long id);

    Integer selCountShopTitle(@Param("title") String title);

    Integer updGoodsDisplayOrder(@Param("id") Long id, @Param("num") Integer num);

    List<EsShopGoods> selectgoodsListByCateIds(@Param("ids") List<String> ids);

    List<EsShopGoods> selGoodsPutAwayTime();

    @ApiOperation("修改商品上架时间")
    Integer updPutawayTime(@Param("id") Long id, @Param("putawayTime") Long putawayTime);

    List<EsShopGoods> selGoodsPutaway(GoodsQuery entity);

    Integer selGoodsPutawayCount(GoodsQuery entity);

    List<EsShopGoods> selectSaleByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<EsShopGoods> lists(GoodsQuery esShopGoods);

    int counts(GoodsQuery esShopGoods);

    List<EsShopGoods> selRankTopList(GoodsRankTopParam param);


    List<Map<String, Object>> searchGoods(String keywords);
}
