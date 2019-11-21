package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoodsImgGroup;

public interface EsShopGoodsImgGroupService extends IService<EsShopGoodsImgGroup> {

    /**
     * 查询所有商品图片分组
     *
     * @return
     */
    Object selEsShopGoodsImgGroup();

    /**
     * 删除分组
     *
     * @param id
     * @return
     */
    Integer delEsShopGoodsImgGroup(Long id);

    /**
     * 新增分组
     *
     * @param esShopGoodsImgGroup
     * @return
     */
    Integer insEsShopGoodsImgGroup(EsShopGoodsImgGroup esShopGoodsImgGroup);

    /**
     * 判断分组是否存在
     *
     * @param groupId
     * @return
     */
    Integer selCountGroup(Long groupId);

    /**
     * 判断分组名称是否存在
     *
     * @param name
     * @return
     */
    Integer selCountGroupName(String name);
}
