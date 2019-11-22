package com.mei.zhuang.controller.goods;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.*;
import com.mei.zhuang.service.goods.*;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import com.mei.zhuang.vo.goods.EsShopGoodsParam;
import com.mei.zhuang.vo.goods.GoodsQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 商品管理
 */
@Slf4j
@Api(value = "商品管理", description = "", tags = {"商品管理"})
@RestController
@RequestMapping("/api/goods")
public class EsShopGoodsController {

    @Resource
    private EsShopGoodsService shopGoodsService;
    @Resource
    private EsCoreAddressService esCoreAddressService;
    @Resource
    private EsShopDiypageService esShopDiypageService;
    @Resource
    private EsShopGoodsOptionService optionService;
    @Resource
    private EsShopGoodsCategoryService esShopGoodsCategoryService;

    @Resource
    private EsShopGoodsSpecService esShopGoodsSpecService;
    @Resource
    private EsShopGoodsSpecItemService esShopGoodsSpecItemService;

    @SysLog(MODULE = "商品管理", REMARK = "根据条件查询所有商品列表")
    @ApiOperation("根据条件查询所有商品列表(模糊查询)")
    @PostMapping(value = "/list")
    public Object getGoodsByPages(GoodsQuery entity) {
        try {
            if (entity.getPutawayType() != null && entity.getPutawayType().equals("-1")) {
                entity.setPutawayType("");
            }
            return new CommonResult().success(shopGoodsService.selGoodsPageList(entity));
        } catch (Exception e) {
            log.error("根据条件查询所有商品列表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品管理", REMARK = "根据条件查询所有商品列表(模糊查询)")
    @ApiOperation("根据条件查询所有商品列表(模糊查询)")
    @PostMapping(value = "/lists")
    public Object lists(GoodsQuery entity) {
        try {
            return new CommonResult().success(shopGoodsService.lists(entity));
        } catch (Exception e) {
            log.error("选择商品弹出框使用：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


    @SysLog(MODULE = "商品管理", REMARK = "保存商品")
    @ApiOperation("保存商品")
    @PostMapping(value = "/save")
    public Object saveGoods(EsShopGoodsParam entity) {
        if (entity.getPutawayTime() != null && !entity.getPutawayTime().equals("")) {
            entity.setPutawayTime(entity.getPutawayTime().replace("\"", ""));
        }
        List<EsShopGoodsSpec> listSpec = new ArrayList<EsShopGoodsSpec>();
        try {
            if (entity.getTitle() == null || entity.getTitle().equals("")) {
                return new CommonResult().failed("请指定商品名称");
            }
            if (ValidatorUtils.empty(entity.getCargoTime())) {
                entity.setCargoTime("0");
            }
            if (entity.getStatus() == null) {
                entity.setStatus(0);
            }
            if (entity.getDisplayOrder() == null) {
                entity.setDisplayOrder(0);
            }
            if (entity.getDisplayOrder() < 0) {
                entity.setDisplayOrder(0);
            }
            if (entity.getEnableSpec() != null) {
                if (entity.getEnableSpec() != 1) {
                    if (entity.getVituralStock() == null) {
                        return new CommonResult().failed("请指定商品库存");
                    }
                }
            }
            if (entity.getDisplayOrder() >= 10000) {
                return new CommonResult().failed("排序不能超过9999");
            }
            if (entity.getPutawayTime() != null && !entity.getPutawayTime().equals("")) {
                boolean bool = StringUtils.isNumeric(entity.getPutawayTime());
                if (bool == false) {
                    return new CommonResult().failed("上架时间格式错误");
                }
            }
            //判断价格是否为负数
            if (entity.getPrice() != null) {
                if (entity.getPrice().signum() == -1) {
                    return new CommonResult().failed("价格不得为负数");
                }
            }
            if (entity.getVituralStock() != null) {
                if (entity.getVituralStock() < 0) {
                    return new CommonResult().failed("库存不得为负数");
                }
            }
            if (entity.getSalesCount() != null) {
                if (entity.getSalesCount() <= 0) {
                    return new CommonResult().failed("已出售数量不得为负数");
                }
            }

            if (entity.getDispatchPrice() != null) {
                if (entity.getDispatchPrice().signum() == -1) {
                    return new CommonResult().failed("运费模版价格不得为负数");
                }
            }
            if (entity.getEdmoney() != null) {
                if (entity.getEdmoney().signum() == -1) {
                    return new CommonResult().failed("单品满额包邮价格不得为负数");
                }
            }
            if (entity.getOriginalCost() != null) {
                if (entity.getOriginalCost().signum() == -1) {
                    return new CommonResult().failed("商品原价不得为负数");
                }
            }
            if (entity.getCostPrice() != null) {
                if (entity.getCostPrice().signum() == -1) {
                    return new CommonResult().failed("商品成本价不得为负数");
                }
            }
            if (entity.getPromotionMoney() != null) {
                if (entity.getPromotionMoney().signum() == -1) {
                    return new CommonResult().failed("会员促销价格不得为负数");
                }
            }
            if (entity.getUniformPostagePrice() != null) {
                if (entity.getUniformPostagePrice().signum() == -1) {
                    return new CommonResult().failed("统一邮费价格不得为负数");
                }
            }
            if (entity.getSpecList() != null && !entity.getSpecList().equals("")) {
                List<EsShopGoodsSpec> list = JSONObject.parseArray(entity.getSpecList(), EsShopGoodsSpec.class);
                if (list != null) {
                    entity.setGoodsSpecList(list);
                }

            }
            if (entity.getSpecItem() != null && !entity.getSpecItem().equals("")) {
                List<EsShopGoodsSpecItem> list = JSONObject.parseArray(entity.getSpecItem(), EsShopGoodsSpecItem.class);
                if (list != null) {
                    for (EsShopGoodsSpecItem item : list) {
                        if (item.getMoney().signum() == -1) {
                            return new CommonResult().failed("多规格价格不得为负数");
                        }
                    }
                    entity.setItemList(list);
                }
            }

            Object obj = shopGoodsService.saveGoods(entity);
            if (obj != null) {
                return new CommonResult().success("success", new Date());
            } else {
                return new CommonResult().success("fail", new Date());
            }
        } catch (Exception e) {
            log.error("保存商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品管理", REMARK = "更新商品")
    @ApiOperation("更新商品")
    @PostMapping(value = "/update")
    public Object updateGoods(EsShopGoodsParam entity) {
        Object obj = null;
        try {

            //判断价格是否为负数
            if (entity.getPrice() != null) {
                if (entity.getPrice().signum() == -1) {
                    return new CommonResult().failed("价格不得为负数");
                }
            }
            if (entity.getVituralStock() != null) {
                if (entity.getVituralStock() < 0) {
                    return new CommonResult().failed("库存不得为负数");
                }
            }
            if (entity.getSalesCount() != null) {
                if (entity.getSalesCount() <= 0) {
                    return new CommonResult().failed("已出售数量不得为负数");
                }
            }

            if (entity.getDispatchPrice() != null) {
                if (entity.getDispatchPrice().signum() == -1) {
                    return new CommonResult().failed("运费模版价格不得为负数");
                }
            }
            if (entity.getEdmoney() != null) {
                if (entity.getEdmoney().signum() == -1) {
                    return new CommonResult().failed("单品满额包邮价格不得为负数");
                }
            }
            if (entity.getOriginalCost() != null) {
                if (entity.getOriginalCost().signum() == -1) {
                    return new CommonResult().failed("商品原价不得为负数");
                }
            }
            if (entity.getCostPrice() != null) {
                if (entity.getCostPrice().signum() == -1) {
                    return new CommonResult().failed("商品成本价不得为负数");
                }
            }
            if (entity.getPromotionMoney() != null) {
                if (entity.getPromotionMoney().signum() == -1) {
                    return new CommonResult().failed("会员促销价格不得为负数");
                }
            }
            if (entity.getUniformPostagePrice() != null) {
                if (entity.getUniformPostagePrice().signum() == -1) {
                    return new CommonResult().failed("统一邮费价格不得为负数");
                }
            }

            if (entity.getSpecList() != null && !entity.getSpecList().equals("")) {
                List<EsShopGoodsSpec> list = JSONObject.parseArray(entity.getSpecList(), EsShopGoodsSpec.class);
                if (list != null) {
                    entity.setGoodsSpecList(list);
                }

            }


            if (entity.getSpecItem() != null && !entity.getSpecItem().equals("")) {
                List<EsShopGoodsSpecItem> list = JSONObject.parseArray(entity.getSpecItem(), EsShopGoodsSpecItem.class);
                if (list != null) {
                    for (EsShopGoodsSpecItem item : list) {
                        if (item.getMoney().signum() == -1) {
                            return new CommonResult().failed("多规格价格不得为负数");
                        }
                    }
                    entity.setItemList(list);
                }
            }


            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("编号不存在");
            }
            obj = shopGoodsService.updateGoods(entity);
            return new CommonResult().success("success", obj);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新商品：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }


    @SysLog(MODULE = "商品管理", REMARK = "查询赠品商品")
    @ApiOperation("查询赠品商品")
    @PostMapping(value = "/gifts")
    public Object selectgiftgoods(EsShopGoods entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageHelper.startPage(entity.getCurrent(), entity.getSize());

            return new CommonResult().success(PageInfo.of(shopGoodsService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            log.error("查询商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询商品明细")
    @ApiOperation("查询商品明细")
    @PostMapping(value = "/detail")
    public Object getGoodsById(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("商品id");
            }
            Object obj = shopGoodsService.getGoodsById(id);
            return new CommonResult().success("success", obj);
        } catch (Exception e) {
            log.error("查询商品明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品管理", REMARK = "批量修改商品状态（上下架）")
    @ApiOperation("商品批量上下架")
    @PostMapping(value = "/updStatus")
    public Object updEsShopGoodsStatus(@RequestParam("ids") String ids, @RequestParam("status") Integer status) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().paramFailed("商品id");
            } else {
                String[] id = ids.split(",");

                for (int i = 0; i < id.length; i++) {

                    Object obj = shopGoodsService.updEsShopGoodsStatus(id[i], status);
                }
            }
            return new CommonResult().success("success", new Date());

        } catch (Exception e) {
            log.error("修改商品上下架状态：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "商品管理", REMARK = "商品批量邏輯刪除（放到回收站）")
    @ApiOperation("商品批量逻辑刪除（放到回收站）")
    @PostMapping("/updShopDelID")
    public Object UpdShopDelID(@RequestParam("ids") String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().paramFailed("商品ID");
            } else {
                String[] id = ids.split(",");
                for (int i = 0; i < id.length; i++) {
                    shopGoodsService.UpdShopDelID(id[i]);
                }
            }
            return new CommonResult().success("success", new Date());
        } catch (Exception e) {
            log.error("删除商品（逻辑删除）异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "批量刪除回收站商品")
    @ApiOperation("批量刪除回收站商品")
    @PostMapping("/delRecycleShop")
    public Object delRecycleShop(@RequestParam("ids") String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("商品Id");
            }
            shopGoodsService.delRecycleShop(ids);
            return new CommonResult().success("success", new Date());
        } catch (Exception e) {
            log.error("批量刪除回收站商品異常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "批量移除回收站商品到仓库")
    @ApiOperation("批量移除回收站商品到仓库")
    @PostMapping("/updRecycleShopStatus")
    public Object updRecycleShopStatus(@RequestParam("ids") String ids, @RequestParam("status") Integer status) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("商品Id");
            } else {
                String[] id = ids.split(",");
                for (int i = 0; i < id.length; i++) {

                    shopGoodsService.updRecycleShopStatus(id[i], status);
                }
                return new CommonResult().success("success", new Date());
            }
        } catch (Exception e) {
            log.error("批量移除回收站商品到倉庫異常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询一级分类")
    @ApiOperation("查询一级分类")
    @PostMapping("/selCategory")
    public Object selCategory() {
        try {
            EsShopGoodsCategory category = new EsShopGoodsCategory();
            category.setStatus(1);
            category.setLevel(0);
            return esShopGoodsCategoryService.list(new QueryWrapper<>(category));
        } catch (Exception e) {
            log.error("查询一级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询二级分类")
    @ApiOperation("查询二级分类")
    @PostMapping("/selShopGoodsCategoryTwo")
    public Object selShopGoodsCategoryTwo(@RequestParam("parentId") Long parentId, @RequestParam("level") Integer level) {
        try {
            if (ValidatorUtils.empty(parentId)) {
                return new CommonResult().failed("父级ID");
            } else {
                return shopGoodsService.selShopGoodsCategoryTwo(parentId, level);
            }
        } catch (Exception e) {
            log.error("查询一级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询商品分組")
    @ApiOperation("查询商品分組")
    @PostMapping("/selShopGoodsGroup")
    public Object selShopGoodsGroup() {
        try {
            return shopGoodsService.selShopGoodsGroup();
        } catch (Exception e) {
            log.error("查询商品分組异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询省市名称")
    @ApiOperation("查询省市名称")
    @PostMapping("/selEsCoreAddress")
    public Object selEsCoreAddress(@RequestParam(value = "parentId", defaultValue = "0") Long parentId, @RequestParam("level") Integer level) {
        try {

            EsCoreAddress esCoreAddress = new EsCoreAddress();
            esCoreAddress.setParentId(parentId);
            if (level == null) {
                return new CommonResult().failed("请输入地址等级");
            }
            esCoreAddress.setLevel(level);
            Object obj = esCoreAddressService.selEsCoreAddress(esCoreAddress);
            return new CommonResult().success(obj);
        } catch (Exception e) {
            log.error("查询省市名称异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "修改排序")
    @ApiOperation("修改排序")
    @PostMapping("/updDisplayOrder")
    public Object updDisplayOrder(@RequestParam("id") Long id, @RequestParam("displayOrder") Integer displayOrder) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定商品ID");
            }
            if (ValidatorUtils.empty(displayOrder)) {
                return new CommonResult().failed("请指定排序值");
            }
            Integer num = shopGoodsService.updGoodsDisplayOrder(id, displayOrder);
            return new CommonResult().success("success", num);
        } catch (Exception e) {
            log.error("修改排序值异常", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询商品详情（前端使用）")
    @ApiOperation("查询多个商品详情")
    @PostMapping("/selGoodsIds")
    public Object selGoodsIds(@RequestParam("ids") String ids) {
        try {
            if (ValidatorUtils.empty(ids)) {
                return new CommonResult().failed("请指定商品ID");
            }
            List<EsShopGoods> list = new ArrayList<EsShopGoods>();
            String[] attr = ids.split(",");
            for (int i = 0; i < attr.length; i++) {
                EsShopGoods es = shopGoodsService.selShopGoodsDetail(Long.parseLong(attr[i]));
                if (es != null) {
                    list.add(es);
                }

            }
            return new CommonResult().success("success", list);
        } catch (Exception e) {
            log.error("查询查询商品详情（前端使用）异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询模板详情")
    @ApiOperation("查询模板详情")
    @PostMapping("/selDiyDetail")
    public Object selDiyDetail(@RequestParam("id") Integer id) {
        try {
            return new CommonResult().success("success", esShopDiypageService.selDiyDetail(id));
        } catch (Exception e) {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询单商品的所有sku")
    @ApiOperation("查询单商品的所有sku")
    @PostMapping("/selGoodsSKU")
    public Object selGoodsSKU(@RequestParam("goodsId") Long goodsId) {
        try {
            if (ValidatorUtils.empty(goodsId)) {
                return new CommonResult().failed("商品ID为空");
            }
            return new CommonResult().success("success", optionService.listGoodsOptionDetail(goodsId));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询商品详情自定义模版")
    @ApiOperation("查询商品详情自定义模版")
    @PostMapping("/selDiyPage")
    public Object selDiyPage() {
        try {
            return new CommonResult().success("success", shopGoodsService.selectAll());
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询上架和已售罄商品(公用模版)")
    @ApiOperation("查询上架和已售罄商品(公用模版)")
    @PostMapping("/selGoodsPutaway")
    public Object selGoodsPutaway(GoodsQuery entity) {
        try {
            if (entity.getCategoryId() != null && entity.getCategoryId().equals("1")) {
                entity.setCategoryId(null);
            }
            return new CommonResult().success("success", shopGoodsService.selGoodsPutaway(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询定制服务")
    @ApiOperation("查询定制服务")
    @PostMapping("/selCustService")
    public Object selCustService() {
        try {
            return new CommonResult().success("success", shopGoodsService.selCustService());
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "商品管理", REMARK = "删除规格")
    @ApiOperation("删除规格")
    @PostMapping("/delSpec")
    public Object delSpec(EsShopGoodsSpec spec) {
        try {
            if (ValidatorUtils.empty(spec.getGoodsId())) {
                return new CommonResult().failed("请指定商品id");
            }
            if (ValidatorUtils.empty(spec.getId())) {
                return new CommonResult().failed("请指定规格id");
            }
            return new CommonResult().success("success", shopGoodsService.delSpec(spec));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "删除规格值")
    @ApiOperation("删除规格值")
    @PostMapping("/delSpecItem")
    public Object delSpecItem(EsShopGoodsSpecItem item) {
        try {
            if (ValidatorUtils.empty(item.getGoodsId())) {
                return new CommonResult().failed("请指定商品id");
            }
            if (ValidatorUtils.empty(item.getSpecId())) {
                return new CommonResult().failed("请指定规格id");
            }
            if (ValidatorUtils.empty(item.getId())) {
                return new CommonResult().failed("请指定规格值id");
            }
            return new CommonResult().success("success", shopGoodsService.delSpecItem(item));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "根据商品查询sku列表")
    @ApiOperation("根据商品查询sku列表")
    @PostMapping("/selOptionList")
    public Object selOptionList(EsShopGoodsOption entity) {
        try {
            if (ValidatorUtils.empty(entity.getGoodsId())) {
                return new CommonResult().failed("请指定商品编号");
            }
            return new CommonResult().success("success", optionService.selPageList(entity));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "批量修改sku")
    @ApiOperation("批量修改sku")
    @PostMapping("/updOptionList")
    public Object updOptionList(EsShopGoodsOption entity) {
        try {
            if (ValidatorUtils.empty(entity.getGoodsId())) {
                return new CommonResult().failed("请指定商品编号");
            }
            Integer virtualStock = 0;
            Long goodsId = null;
            List<EsShopGoodsOption> listOption = JSONObject.parseArray(entity.getOptionList(), EsShopGoodsOption.class);
            boolean bool = false;
            for (EsShopGoodsOption option : listOption) {
                if (ValidatorUtils.empty(option.getId())) {
                    return new CommonResult().failed("请指定列表编号");
                }
                if (option.getVirtualStock() == null || option.getVirtualStock().equals("")) {
                    option.setVirtualStock(0);
                }
                bool = optionService.updateById(option);
            }
            EsShopGoodsOption option = new EsShopGoodsOption();
            option.setGoodsId(entity.getGoodsId());
            List<EsShopGoodsOption> list = optionService.list(new QueryWrapper<>(option));
            for (EsShopGoodsOption options : list) {
                if (options.getVirtualStock() == null || options.getVirtualStock().equals("")) {
                    virtualStock = 0;
                } else {
                    virtualStock += options.getVirtualStock();
                }
            }
            EsShopGoods esShopGoods = shopGoodsService.getById(entity.getGoodsId());
            if (esShopGoods.getStatus() == 3) {
                if (virtualStock > 0) {
                    esShopGoods.setStatus(1);
                }
            }
            esShopGoods.setId(entity.getGoodsId());
            esShopGoods.setVituralStock(virtualStock);
            shopGoodsService.updateById(esShopGoods);
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "商品管理", REMARK = "新增小美盒中的定制礼盒")
    @ApiOperation("新增小美盒中的定制礼盒")
    @PostMapping("/insSmallBeautyBoxCust")
    public boolean insSmallBeautyBoxCust(@RequestParam("boxName") String boxName,
                                         @RequestParam("boxCode") String boxCode,
                                         @RequestParam("boxImg") String boxImg,
                                         @RequestParam("vituralStock") Integer vituralStock,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("id") Long id,
                                         @RequestParam("productSn") String productSn) {
        boolean bool = false;
        try {
            //1.添加商品
            EsShopGoods goods = new EsShopGoods();
            goods.setTitle(boxName);
            goods.setGoodsCode(boxCode);
            goods.setThumb(boxImg);
            goods.setVituralStock(vituralStock);
            goods.setPrice(price);
            goods.setType(6);
            goods.setStatus(1);
            goods.setSmallBeautyBoxId(id);//定制礼盒id
            goods.setEnableSpec(1);
            goods.setPutawayTime(String.valueOf(System.currentTimeMillis()));
            goods.setPutawayType("微信小程序");
            goods.setDisplayOrder(0);
            goods.setDefaultSpec("规格:规格值");
            goods.setCreateTime(new Date());
            bool = shopGoodsService.save(goods);
            //1添加规格规格值
            //2.添加规格
            EsShopGoodsSpec spec = new EsShopGoodsSpec();
            spec.setGoodsId(goods.getId());
            spec.setIsMainSpec(1);
            spec.setTitle("规格");
            spec.setDisplayOrder(0);
            bool = esShopGoodsSpecService.save(spec);
            //3.添加规格值
            EsShopGoodsSpecItem specItem = new EsShopGoodsSpecItem();
            specItem.setGoodsId(goods.getId());
            specItem.setSpecId(spec.getId());
            specItem.setTitle("规格值");
            specItem.setDisplayOrder(0);
            specItem.setShow("1");
            specItem.setStatus(1);
            specItem.setType(1);
            specItem.setTypeword("1");
            specItem.setMoney(goods.getPrice());
            specItem.setTitleItem(spec.getTitle());
            bool = esShopGoodsSpecItemService.save(specItem);
            //3.option
            EsShopGoodsOption option = new EsShopGoodsOption();
            option.setGoodsName(goods.getTitle());
            option.setSpecIds(specItem.getId().toString());
            option.setTitle(specItem.getTitle());
            option.setDisplayOrder(0);
            option.setPrice(goods.getPrice());
            option.setSpecs("规格:规格值");
            option.setStock(goods.getVituralStock());
            option.setStockWarning(0);
            option.setBanner("[\"\"]");
            option.setVirtualStock(goods.getVituralStock());
            option.setMarketprice(goods.getPrice());
            option.setGoodsId(goods.getId());
            option.setThumb(boxImg);
            option.setGoodsCode(boxCode);
            option.setProductsn(productSn);//条码
            bool = optionService.save(option);
        } catch (Exception e) {
            e.printStackTrace();
            bool = false;
        }
        return bool;
    }

    @SysLog(MODULE = "商品管理", REMARK = "新增小美盒中的定制礼盒")
    @ApiOperation("修改小美盒中的定制礼盒")
    @PostMapping("/updSmallBeautyBoxCust")
    public boolean updSmallBeautyBoxCust(@RequestParam("boxName") String boxName,
                                         @RequestParam("boxCode") String boxCode,
                                         @RequestParam("boxImg") String boxImg,
                                         @RequestParam("vituralStock") Integer vituralStock,
                                         @RequestParam("price") BigDecimal price,
                                         @RequestParam("id") Long id,
                                         @RequestParam("productSn") String productSn) {
        boolean bool = false;
        try {
            //查询商品id
            EsShopGoods goods = new EsShopGoods();
            goods.setSmallBeautyBoxId(id);
            EsShopGoods esShopGoods = shopGoodsService.getOne(new QueryWrapper<>(goods));
            //1.添加商品
            goods.setTitle(boxName);
            goods.setGoodsCode(boxCode);
            goods.setThumb(boxImg);
            goods.setVituralStock(vituralStock);
            goods.setPrice(price);
            goods.setType(6);
            goods.setSmallBeautyBoxId(id);//定制礼盒id
            goods.setId(esShopGoods.getId());
            goods.setEnableSpec(1);
            bool = shopGoodsService.updateById(goods);
            //2.添加规格
            EsShopGoodsSpec spec = new EsShopGoodsSpec();
            spec.setGoodsId(goods.getId());
            EsShopGoodsSpec specs = esShopGoodsSpecService.getOne(new QueryWrapper<>(spec));
            spec.setIsMainSpec(1);
            spec.setDisplayOrder(0);
            spec.setId(specs.getId());
            bool = esShopGoodsSpecService.updateById(spec);
            //3.添加规格值
            EsShopGoodsSpecItem specItem = new EsShopGoodsSpecItem();
            specItem.setGoodsId(goods.getId());
            EsShopGoodsSpecItem specItem1 = esShopGoodsSpecItemService.getOne(new QueryWrapper<>(specItem));
            specItem.setSpecId(spec.getId());
            specItem.setDisplayOrder(0);
            specItem.setShow("1");
            specItem.setStatus(1);
            specItem.setType(1);
            specItem.setMoney(goods.getPrice());
            specItem.setTitleItem(spec.getTitle());
            specItem.setId(specItem1.getId());
            bool = esShopGoodsSpecItemService.updateById(specItem);
            //3.option
            EsShopGoodsOption option = new EsShopGoodsOption();
            option.setGoodsId(goods.getId());
            EsShopGoodsOption opt = optionService.getOne(new QueryWrapper<>(option));
            option.setGoodsName(goods.getTitle());
            option.setSpecIds(specItem.getId().toString());
            option.setTitle(specItem.getTitle());
            option.setDisplayOrder(0);
            option.setPrice(goods.getPrice());
            option.setSpecs(spec.getTitle() + ":" + specItem.getTitle());
            option.setStock(goods.getVituralStock());
            option.setStockWarning(0);
            option.setVirtualStock(goods.getVituralStock());
            option.setMarketprice(goods.getPrice());
            option.setId(opt.getId());
            option.setThumb(boxImg);
            option.setGoodsCode(boxCode);
            option.setProductsn(productSn);//条码
            bool = optionService.updateById(option);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return bool;

    }

    @SysLog(MODULE = "商品管理", REMARK = "删除小美盒中的定制礼盒")
    @ApiOperation("删除小美盒中的定制礼盒")
    @PostMapping("/delSmallBeautyBoxCust")
    public boolean delSmallBeautyBoxCust(@RequestParam("id") Long id) {
        EsShopGoods goods = new EsShopGoods();
        goods.setSmallBeautyBoxId(id);//定制礼盒id
        return shopGoodsService.remove(new QueryWrapper<>(goods));
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询小美盒中的定制礼盒")
    @ApiOperation("查询小美盒中的定制礼盒")
    @PostMapping("/goodsDetail")
    public EsShopGoods goodsDetail(@RequestParam("id") Long id) {
        EsShopGoods goods = new EsShopGoods();
        goods.setSmallBeautyBoxId(id);//定制礼盒id
        return shopGoodsService.getOne(new QueryWrapper<>(goods));
    }

    @SysLog(MODULE = "商品管理", REMARK = "查询sku")
    @ApiOperation("查询sku")
    @PostMapping("/goodsOption")
    public EsShopGoodsOption goodsOption(@RequestParam("goodsId") Long goodsId) {
        EsShopGoodsOption option = new EsShopGoodsOption();
        option.setGoodsId(goodsId);
        return optionService.getOne(new QueryWrapper<>(option));
    }

    //商品状态详情
    @PostMapping("/selShopGoodsList")
    public List<EsShopGoods> selShopGoodsList(@RequestBody TradeAnalyzeParam param) {
        //  param.setEndTime(param.getEndTime() + " 23:59:59.999");
        //status商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
        //条件
        QueryWrapper<EsShopGoods> condition = new QueryWrapper();
        if (ValidatorUtils.notEmpty(param.getStartTime()))
            condition.ge("create_time", Timestamp.valueOf(DateUtil.format(param.getStartTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        if (ValidatorUtils.notEmpty(param.getEndTime()))
            condition.le("create_time", Timestamp.valueOf(param.getEndTime()));
        if (ValidatorUtils.notEmpty(param.getShopId())) {
            condition.eq("shop_id", param.getShopId());
        }

        return shopGoodsService.list(condition);

    }

}
