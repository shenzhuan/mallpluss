package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoodsImg;

import java.util.List;
import java.util.Map;

public interface EsShopGoodsImgService extends IService<EsShopGoodsImg> {

    /**
     * 根据分组查询图片
     *
     * @param entity
     * @return
     */
    Map<String, Object> selImg(EsShopGoodsImg entity);

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    Integer delImg(Long id);

    /**
     * 新增图片
     *
     * @return
     */
    Integer insImg(Long groupId, String img, String filename);

    List<EsShopGoodsImg> selImgs(EsShopGoodsImg entity);



}
