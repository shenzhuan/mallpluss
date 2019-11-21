package com.mei.zhuang.service.goods;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsGroup;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */

public interface EsShopGoodsGroupService extends IService<EsShopGoodsGroup> {

    @ApiOperation("根据条件查询所有商品分组列表")
    public Object getGoodsGroupByPage(EsShopGoodsGroup entity);

    @ApiOperation("保存商品分组")
    public Object saveGoodsGroup(EsShopGoodsGroup entity);

    @ApiOperation("更新商品分组")
    public Object updateGoodsGroup(EsShopGoodsGroup entity);

    @ApiOperation("删除商品分组")
    public Object deleteGoodsGroup(Long id);

    @ApiOperation("批量删除商品分组")
    public Object deleteGoodsGroups(String ids);

    @ApiOperation("查询商品分组明细")
    public Object getGoodsGroupById(Long id);

    @ApiOperation("根据分组名称查询")
    public Map<String, Object> goodsgrouplist(EsShopGoodsGroup esShopGoodsGroup);

    @ApiOperation("批量修改启动状态")
    public Object updatestateid(String id, Integer status);

    @ApiOperation("选择商品分类查询")
    public List<Map<String, Object>> goodscatelist(EsShopGoods goods);

    @ApiOperation("判断分组是否存在")
    public Integer selCateCount(String cateName);



}


