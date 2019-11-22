package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopGoodsCateMapMapper;
import com.mei.zhuang.dao.goods.EsShopGoodsGroupMapMapper;
import com.mei.zhuang.dao.goods.EsShopGoodsGroupMapper;
import com.mei.zhuang.dao.goods.EsShopGoodsMapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsGroup;
import com.mei.zhuang.entity.goods.EsShopGoodsGroupMap;
import com.mei.zhuang.service.goods.EsShopGoodsGroupService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */

@Slf4j
@Api(value = "商品分组管理", description = "", tags = {"商品分组管理"})
@Service
public class EsShopGoodsGroupServiceImpl extends ServiceImpl<EsShopGoodsGroupMapper, EsShopGoodsGroup> implements EsShopGoodsGroupService {
    @Resource
    private EsShopGoodsCateMapMapper shopGoodsCateMapMapper;
    @Resource
    private EsShopGoodsGroupMapMapper shopGoodsGroupMapMapper;
    @Resource
    private EsShopGoodsMapper esShopGoodsMapper;
    @Resource
    private EsShopGoodsGroupService goodsGroupService;
    @Resource
    private EsShopGoodsGroupMapper goodsGroupMapper;


    @ApiOperation("选择商品分类查询")
    @Override
    public List<Map<String, Object>> goodscatelist(EsShopGoods goods) {
        return goodsGroupMapper.goodscatelist(goods);
    }

    @Override
    public Integer selCateCount(String cateName) {
        return goodsGroupMapper.selCateCount(cateName);
    }


    @ApiOperation("保存商品分组")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveGoodsGroup(EsShopGoodsGroup entity) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date da = new Date();
            String str = sdf.format(da);
            Date date = sdf.parse(str);
            entity.setCreateTime(date);
            // 1.添加分组信息
            goodsGroupService.save(entity);
            // 2.添加商品分组
            if (ValidatorUtils.notEmpty(entity.getGoodsIds())) {
                String[] goodsIds = entity.getGoodsIds().split(",");
                for (String gid : goodsIds) {
                    EsShopGoodsGroupMap group = new EsShopGoodsGroupMap();
                    group.setGroupId(entity.getId());
                    group.setGoodsId(Long.valueOf(gid));
                    group.setShopId(entity.getGoodsId());

                    shopGoodsGroupMapMapper.insert(group);
                }
            }

            return new CommonResult().success();

        } catch (Exception e) {
            log.error("保存商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation("更新商品分组")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object updateGoodsGroup(EsShopGoodsGroup entity) {
        try {
            // 1.更新分类信息
            this.baseMapper.updateById(entity);
            // 2.删除此分类下的商品
            EsShopGoodsGroupMap group = new EsShopGoodsGroupMap();
            group.setShopId(entity.getGoodsId());
            group.setGroupId(entity.getId());

            shopGoodsGroupMapMapper.delete(new QueryWrapper<>(group));

            // 3.重新插入此分组下的商品
            if (ValidatorUtils.notEmpty(entity.getGoodsIds())) {
                String[] goodsIds = entity.getGoodsIds().split(",");
                for (String gid : goodsIds) {
                    EsShopGoodsGroupMap groupmap = new EsShopGoodsGroupMap();
                    groupmap.setShopId(entity.getGoodsId());
                    groupmap.setGroupId(entity.getId());
                    groupmap.setGoodsId(Long.valueOf(gid));

                    shopGoodsGroupMapMapper.insert(groupmap);
                }
            }
            return new CommonResult().success();

        } catch (Exception e) {
            log.error("更新商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


    @ApiOperation("修改商品分组状态id")
    @Transactional(rollbackFor = Exception.class)

    public Object updatestateid(String id, Integer status) {
        try {
            goodsGroupMapper.updatestate(Long.valueOf(id), status);
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("修改商品分组状态：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


    @ApiOperation("删除商品分组")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object deleteGoodsGroup(Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品分组id");
            }
            // 1.更新分类信息
            this.baseMapper.deleteById(id);
            // 2.删除此分组下的商品
            EsShopGoodsGroupMap groupmap = new EsShopGoodsGroupMap();
            groupmap.setGroupId(id);
            shopGoodsGroupMapMapper.delete(new QueryWrapper<>(groupmap));
            return new CommonResult().success();

        } catch (Exception e) {
            log.error("删除商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("批量删除商品分组")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object deleteGoodsGroups(String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("商品Id");
            } else {
                String[] idw = ids.split(",");
                for (String vid : idw) {
                    this.baseMapper.deleteById(Long.valueOf(vid));
                    EsShopGoodsGroupMap groupmap = new EsShopGoodsGroupMap();
                    groupmap.setGroupId(Long.valueOf(vid));
                    shopGoodsGroupMapMapper.delete(new QueryWrapper<>(groupmap));
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("批量删除商品分组：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


    @ApiOperation("查询商品分组明细")
    @Override
    public Object getGoodsGroupById(Long id) {
        try {

            EsShopGoodsGroup coupon = goodsGroupMapper.selectById(id);
            if (coupon != null) {
                List<EsShopGoodsGroupMap> list = shopGoodsGroupMapMapper.selEsShopGoodsGroupMap(coupon.getId());
                List<EsShopGoods> lists = new ArrayList<EsShopGoods>();
                for (EsShopGoodsGroupMap lis : list) {
                    //根据商品ID查询商品名称、商品图片、商品虚拟库存
                    EsShopGoods es = esShopGoodsMapper.selEsShopGoods(lis.getGoodsId());
                    if (es != null) {
                        lists.add(es);
                    }
                }
                coupon.setListGoods(lists);
            }

            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商品分组明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


}
