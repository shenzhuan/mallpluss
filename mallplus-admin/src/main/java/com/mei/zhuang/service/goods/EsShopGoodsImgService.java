package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoodsImg;

import java.util.List;

public interface EsShopGoodsImgService extends IService<EsShopGoodsImg> {


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
