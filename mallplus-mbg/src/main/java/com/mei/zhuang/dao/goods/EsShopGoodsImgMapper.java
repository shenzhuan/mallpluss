package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mei.zhuang.entity.goods.EsShopGoodsImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsShopGoodsImgMapper extends BaseMapper<EsShopGoodsImg> {

    /**
     * 根据分组查询图片
     *
     * @param entity
     * @return
     */
  //  List<EsShopGoodsImg> selImg(Pagination page, EsShopGoodsImg entity);



    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    Integer delImg(@Param("id") Long id);

    Integer selCountImg(EsShopGoodsImg entity);

    List<EsShopGoodsImg> selImgs(@Param("groupId") Long groupId);
}
