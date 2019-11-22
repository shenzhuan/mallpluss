package com.mei.zhuang.service.goods.impl;

import com.mei.zhuang.dao.goods.EsShopGoodsImgGroupMapper;
import com.mei.zhuang.service.goods.EsShopGoodsImgGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsShopGoodsImgGroup;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Api(value = "商品图片分组管理", description = "", tags = {"商品图片分组管理"})
@Service
public class EsShopGoodsImgGroupServiceImpl extends ServiceImpl<EsShopGoodsImgGroupMapper, EsShopGoodsImgGroup> implements EsShopGoodsImgGroupService {

    @Resource
    private EsShopGoodsImgGroupMapper esShopGoodsImgGroupMapper;

    /**
     * 查询图片分组
     *
     * @return
     */
    @Override
    public Object selEsShopGoodsImgGroup() {
        return esShopGoodsImgGroupMapper.selEsShopGoodsImgGroupMapper();
    }

    /**
     * 删除图片分组
     *
     * @param id
     * @return
     */
    @Override
    public Integer delEsShopGoodsImgGroup(Long id) {
        return esShopGoodsImgGroupMapper.delEsShopGoodsImgGroup(id);
    }

    /**
     * 新增图片分组
     *
     * @param entity
     * @return
     */
    @Override
    public Integer insEsShopGoodsImgGroup(EsShopGoodsImgGroup entity) {
        return esShopGoodsImgGroupMapper.insEsShopGoodsImgGroup(entity.getGroupName());
    }

    @Override
    public Integer selCountGroup(Long groupId) {
        return esShopGoodsImgGroupMapper.selCountGroup(groupId);
    }

    @Override
    public Integer selCountGroupName(String name) {
        return esShopGoodsImgGroupMapper.selCountGroupName(name);
    }
}
