package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.mei.zhuang.dao.goods.*;
import com.mei.zhuang.entity.goods.*;
import com.mei.zhuang.service.goods.EsShopGoodsCategoryService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:50
 * @Description:
 */

@Slf4j
@Api(value = "商品分类管理", description = "", tags = {"商品分类管理"})
@Service
public class EsShopGoodsCategoryServiceImpl extends ServiceImpl<EsShopGoodsCategoryMapper, EsShopGoodsCategory> implements EsShopGoodsCategoryService {
    @Resource
    private EsShopGoodsCateMapMapper shopGoodsCateMapMapper;//商品分类关联表
    @Resource
    private EsShopGoodsCategoryMapper shopGoodsCategoryMapper;//商品分类
    @Resource
    private EsShopGoodsCategoryAdvertimgMapper esShopGoodsCategoryAdvertimgMapper;//商品分类广告图
    @Resource
    private EsShopGoodsMapper esShopGoodsMapper;
    @Resource
    private EsShopGoodsCategoryRecomMapper esShopGoodsCategoryRecomMapper;

    @Resource
    private EsShopGoodsCategoryService goodsCategoryService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据父id查询子分类
     * parentId： 父Id level：分类级别
     */
    @ApiOperation("根据条件查询所有商品分类列表")
    @Override
    public Object getGoodsCategoryByPage(EsShopGoodsCategory entity) {
        try {
            // page = orderService.page(new Page<OmsOrder>(pageNum, pageSize), new QueryWrapper<OmsOrder>()
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<EsShopGoodsCategory> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(entity.getCurrent(),entity.getSize());
            return new CommonResult().success(shopGoodsCategoryMapper.selectPage(page, new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有商品分类列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    /**
     * 根据父id查询子分类
     * parentId： 父Id level：分类级别
     */
    @ApiOperation("根据父id查询子分类")
    @Override
    public Object getSonGoodsCategory(Integer parentId, Integer level) {
        try {
            return new CommonResult().success(this.getSonGoodsCategory(parentId, level));
        } catch (Exception e) {
            log.error("根据条件查询所有商品分类列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @Override
    public Integer delGoodsCategory(Long id) {
        //判断分类下是否存在商品
        List<EsShopGoodsCateMap> list=shopGoodsCateMapMapper.selEsShopGoodsCateMap(id);
        if(list!=null && list.size()>0){
            //删除分类下的商品
            Integer num=null;
            for (EsShopGoodsCateMap li:list) {
                num= shopGoodsCateMapMapper.delete(new QueryWrapper<>(li));

                EsShopGoods esShopGoods=esShopGoodsMapper.selectById(li.getGoodsId());
                if(esShopGoods !=null){
                    String[] attrId = esShopGoods.getCategoryId().split(",");
                    for(int i=0; i<attrId.length;i++){
                        esShopGoods.setCategoryId(attrId[i]);
                    }
                    String[] attrName =  esShopGoods.getCategoryName().split(",");
                }
            }
            shopGoodsCategoryMapper.delGoodsCategory(id);
            return num;
        }else{
            return shopGoodsCategoryMapper.delGoodsCategory(id);
        }

    }

    @ApiOperation("保存商品分类")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveGoodsCategory(EsShopGoodsCategory entity) {
        try {

            // 1.添加分类信息
            Date ds = new Date();
            String str=sdf.format(ds);
            Date date=sdf.parse(str);
            entity.setCreateTime(date);
            entity.setLastTime(date);
            if(entity.getThumb()!=null){
                entity.setThumb(entity.getThumb().replace("\"", ""));
            }

            goodsCategoryService.save(entity);
            // 2.添加商品分类
            if (ValidatorUtils.notEmpty(entity.getGoodsIds())) {
                //添加推荐商品
                EsShopGoodsCategoryRecom recom = new EsShopGoodsCategoryRecom();
                String[] attr = entity.getGoodsIds().split(",");
                //根据名称查询分类ID

                for (int i = 0; i < attr.length; i++) {
                    recom.setGoodsId(Long.parseLong(attr[i]));
                    recom.setCategoryId(entity.getId());
                    esShopGoodsCategoryRecomMapper.insert(recom);
                }

            }
            //添加广告图
            if (entity != null) {
                EsShopGoodsCategoryAdvertimg advertimg = new EsShopGoodsCategoryAdvertimg();
                if(entity.getAdvertImg()!=null){
                    String[] img = entity.getAdvertImg().split(",");
                        for (int i = 0; i < img.length; i++) {
                            advertimg.setCategoruAdimg(img[i].replace("\"", ""));
                            String[] address = entity.getAdvertAddress().split(",");
                            for (int j = 0; j < address.length; j++) {
                                if (i == j) {
                                    advertimg.setCategoruAdaddress(address[j].replace("\"", ""));
                                    continue;
                                }

                            }
                            advertimg.setCategoryId(entity.getId());
                            if(advertimg.getCategoruAdimg()!=null && !advertimg.getCategoruAdimg().equals("")){
                                esShopGoodsCategoryAdvertimgMapper.insert(advertimg);
                            }

                        }
                }


            }

            return new CommonResult().success();

        } catch (Exception e) {
            log.error("保存商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @Override
    public Object updGoodsCategoryStatus(Long id, Integer status) {
        return shopGoodsCategoryMapper.updGoodsCategoryStatus(id, status);
    }

    @Override
    @ApiOperation("统计父级分类下的所有子分类")
    public List<EsShopGoodsCategory> countSubClass(Long id) {
        return shopGoodsCategoryMapper.selGoodsCategorySubClass(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @ApiOperation("更新商品分类排序")
    public Object updDisplayOrder(EsShopGoodsCategory entity) {
        try {

            shopGoodsCategoryMapper.updDisplayOrder(entity.getId(), entity.getDisplayOrder());
            return new CommonResult().success();

        } catch (Exception e) {
            log.error("更新商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @Override
    public Object updateGoodsCategory(EsShopGoodsCategory entity) {
        try {
            // 1.更新分类信息
            // this.baseMapper.updateById(entity);
            entity.setId(entity.getId());
            entity.setStatus(entity.getStatus());
            // 1.添加分类信息
            Date ds = new Date();
            String str=sdf.format(ds);
            Date date=sdf.parse(str);
            entity.setLastTime(date);
            entity.setThumb(entity.getThumb().replace("\"", ""));
            this.baseMapper.updateById(entity);
            // 2.添加推荐商品
            if (ValidatorUtils.notEmpty(entity.getId())) {
                //删除推荐商品重新插入
                List<EsShopGoodsCategoryRecom> recoms = esShopGoodsCategoryRecomMapper.selCategoryRecom(entity.getId());
                if (recoms.size() > 0) {
                    for (EsShopGoodsCategoryRecom re : recoms) {
                        esShopGoodsCategoryRecomMapper.delete(entity.getId());
                    }
                }
                //添加推荐商品
                EsShopGoodsCategoryRecom recom = new EsShopGoodsCategoryRecom();
                if(entity.getGoodsIds()!=null&&!entity.getGoodsIds().equals("")){
                    String[] attr = entity.getGoodsIds().split(",");
                    //根据名称查询分类ID
                    EsShopGoodsCategory category = shopGoodsCategoryMapper.selCategory(entity.getName());
                    for (int i = 0; i < attr.length; i++) {
                        recom.setGoodsId(Long.parseLong(attr[i]));
                        recom.setCategoryId(category.getId());
                        esShopGoodsCategoryRecomMapper.insert(recom);
                    }
                }


            }
            //添加广告图
            if (entity != null) {
                //删除广告图片
                List<EsShopGoodsCategoryAdvertimg> advertimgs = esShopGoodsCategoryAdvertimgMapper.selEsShopGoodsCategoryAdvertimg(entity.getId());
                if (advertimgs.size() > 0) {
                    esShopGoodsCategoryAdvertimgMapper.delete(entity.getId());
                }

                EsShopGoodsCategoryAdvertimg advertimg = new EsShopGoodsCategoryAdvertimg();
                String[] img = entity.getAdvertImg().split(",");
                for (int i = 0; i < img.length; i++) {
                    advertimg.setCategoruAdimg(img[i].replace("\"", ""));
                    String[] address = entity.getAdvertAddress().split(",");
                    for (int j = 0; j < address.length; j++) {
                        if (i == j) {
                            advertimg.setCategoruAdaddress(address[j].replace("\"", ""));
                            continue;
                        }

                    }
                    advertimg.setCategoryId(entity.getId());
                    esShopGoodsCategoryAdvertimgMapper.insert(advertimg);
                }
            }

            return new CommonResult().success();

        } catch (Exception e) {
            log.error("更新商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    /**
     * isDataDelete ： false ：逻辑删除  true：数据删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @ApiOperation("删除商品分类")
    public Object deleteGoodsCategory(Long id, Boolean isDataDelete) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品分类id");
            }
            if (isDataDelete) {
                // 1.删除分类信息
                this.baseMapper.deleteById(id);
                // 2.删除此分类下的商品
                EsShopGoodsCateMap querycate = new EsShopGoodsCateMap();
                querycate.setCategoryId(id);
                shopGoodsCateMapMapper.delete(new QueryWrapper<>(querycate));
                log.info("已从删除商品分类成功！");
            } else {
                EsShopGoodsCategory esShopGoodsCategory = shopGoodsCategoryMapper.selectById(id);
                esShopGoodsCategory.setStatus(1);  //1:代表逻辑删除
                this.updateById(esShopGoodsCategory);
                log.info("将商品分类移动到回收站！");
            }
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("删除商品分类：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @Override
    @ApiOperation("查询商品分类明细")
    public Object getGoodsCategoryById(Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品分类id");
            }
            EsShopGoodsCategory coupon = shopGoodsCategoryMapper.selectById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询商品分类明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @Override
    public Object selectAll() {
        List<EsShopGoodsCategory> list = shopGoodsCategoryMapper.selAll();
        for (EsShopGoodsCategory cate:list) {
            if(cate.getLevel()==0){
                //查询二级分类
                List<EsShopGoodsCategory> listTwo=shopGoodsCategoryMapper.selCategoryByLevel(1,cate.getId());
                if(listTwo!=null&&listTwo.size()>0){
                    cate.setListTwo(listTwo);
                    //查询三级分类
                    for (EsShopGoodsCategory listThree:listTwo) {
                        listThree.setListThree(shopGoodsCategoryMapper.selCategoryByLevel(2,listThree.getId()));
                    }
                }
            }
        }
        return list;
    }

    @Override
    @ApiOperation("查询商品子分类")
    public Object selGoodsCategorySubClass(Long parentId) {
        return shopGoodsCategoryMapper.selGoodsCategorySubClass(parentId);
    }

    @Override
    public void dataDelelteById(List<Long> ids) {

    }

    @Override
    public void logicDeleteById(List<Long> ids) {

    }

    @Override
    public Map<String, Object> list(Integer current, Integer size) {
        Map<String, Object> result = new HashMap<>();
        Page<EsShopGoods> page = new Page<EsShopGoods>(current, size);
        //查询一级分类，分页查询
        List<EsShopGoodsCategory> list = shopGoodsCategoryMapper.selectList(new QueryWrapper<>());
        for (EsShopGoodsCategory cate:list) {
            if(cate.getLevel()==0){
                //查询二级分类
                List<EsShopGoodsCategory> listTwo=shopGoodsCategoryMapper.selCategoryByLevel(1,cate.getId());
                if(listTwo!=null&&listTwo.size()>0){
                    cate.setListTwo(listTwo);
                    //查询三级分类
                    for (EsShopGoodsCategory listThree:listTwo) {
                        listThree.setListThree(shopGoodsCategoryMapper.selCategoryByLevel(2,listThree.getId()));
                    }
                }
            }
        }

        int count = shopGoodsCategoryMapper.count();
        result.put("rows", list);
        result.put("total", count);
        result.put("current", current);
        result.put("size", 5);
        return result;
    }

    @Override
    public EsShopGoodsCategory selDetail(Long id) {
        List<EsShopGoods> goodsList = new ArrayList<EsShopGoods>();
        EsShopGoodsCategory category = shopGoodsCategoryMapper.selDetail(id);
        //查询推荐分类
        List<EsShopGoodsCategoryAdvertimg> list = esShopGoodsCategoryAdvertimgMapper.selEsShopGoodsCategoryAdvertimg(id);
        if (list.size() > 0) {
            category.setCategoryAdvert(list);
        }
        //查询推荐商品
        //List<EsShopGoodsCateMap> listEsShopGoodsCateMap=shopGoodsCateMapMapper.selEsShopGoodsCateMap(id);
        List<EsShopGoodsCategoryRecom> listRecom = esShopGoodsCategoryRecomMapper.selCategoryRecom(id);

        for (EsShopGoodsCategoryRecom es : listRecom) {
            EsShopGoods esShopGoods = esShopGoodsMapper.selEsShopGoods(es.getGoodsId());
            goodsList.add(esShopGoods);
        }
        if (goodsList.size() > 0) {
            category.setGoodsList(goodsList);
        }

        return category;
    }

    @Override
    public List<EsShopGoodsCategory> selEsShopGoodsCategory() {
        return shopGoodsCategoryMapper.selEsShopGoodsCategory();
    }

    @Override
    public EsShopGoodsCategory selCategory(String name) {
        return shopGoodsCategoryMapper.selCategory(name);
    }


}
