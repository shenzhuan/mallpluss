package com.mei.zhuang.dao.goods;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopGoodsImgGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsShopGoodsImgGroupMapper extends BaseMapper<EsShopGoodsImgGroup> {

    /**
     * 查询所有商品图片分组
     *
     * @return
     */
    List<EsShopGoodsImgGroup> selEsShopGoodsImgGroupMapper();

    /**
     * 新增分组
     *
     * @param groupName
     * @return
     */
    Integer insEsShopGoodsImgGroup(@Param("greoupName") String groupName);

    /**
     * 删除分组
     *
     * @param id
     * @return
     */
    Integer delEsShopGoodsImgGroup(@Param("id") Long id);

    /**
     * 判断分组是否存在
     *
     * @param groupId
     * @return
     */
    Integer selCountGroup(@Param("id") Long groupId);

    /**
     * 判断分组名称是否存在
     *
     * @param name
     * @return
     */
    Integer selCountGroupName(@Param("name") String name);

}
