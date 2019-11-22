package com.mei.zhuang.controller.goods;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.*;
import com.mei.zhuang.service.member.impl.RedisUtil;
import com.mei.zhuang.service.goods.*;
import com.mei.zhuang.utils.JsonUtils;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.goods.GoodsQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/13 14:36
 * @Description:
 */
@Slf4j
@Api(value = "小程序商品管理", description = "", tags = {"小程序商品管理"})
@RestController
@RequestMapping("/applet")
public class AppletGoodsController {

    @Resource
    private EsShopGoodsService goodsService;
    @Resource
    private EsShopDiypageService esShopDiypageService;
    @Resource
    private EsShopGoodsDiypageMapService esShopGoodsDiypageMapService;
    @Resource
    private EsStartAdvertisingService esStartAdvertisingService;
    @Resource
    private EsStartAdvertisingImgService esStartAdvertisingImgService;
    @Resource
    private RedisUtil redisRepository;
    @Resource
    private EsShopCustomizedLegendService esShopCustomizedLegendService;
    @Resource
    private EsShopCustomizedPacketServer esShopCustomizedPacketServer;
    @Resource
    private EsShopCustomizedCardService esShopCustomizedCardService;
    @Resource
    private EsShopCustomizedBasicService esShopCustomizedBasicService;

    @ApiOperation("小程序首页")
    @PostMapping(value = "/home")
    public Object home() {

        return null;
    }

    @SysLog(MODULE = "小程序商品管理", REMARK = "商品详情")
    @ApiOperation("商品详情")
    @PostMapping(value = "/goodsDetail")
    public Object goodsDetail(@ApiParam("商品id") @RequestParam("id") Long id) {
        if (ValidatorUtils.empty(id)) {
            return new CommonResult().paramFailed();
        }
        return new CommonResult().success(goodsService.goodsDetail(id));
    }

    @SysLog(MODULE = "小程序商品管理", REMARK = "根据条件查询所有商品列表(模糊查询)")
    @ApiOperation("根据条件查询所有商品列表(模糊查询)")
    @PostMapping(value = "/goodsListByCate")
    public Object goodsListByCate(GoodsQuery entity) {
        try {
            if (entity != null) {
                if (ValidatorUtils.notEmpty(entity.getCategoryId()) && entity.getCategoryId().equals("1")) {
                    entity.setCategoryId(null);
                }
            }
            entity.setNeedPutAway(1);
            entity.setAppletstatus(1);
            entity.setType("1");
            return new CommonResult().success(goodsService.goodsListByCatePageList(entity));
        } catch (Exception e) {
            log.error("根据条件查询所有商品列表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "小程序商品管理", REMARK = "首页搜索")
    @ApiOperation("首页搜索")
    @PostMapping(value = "/searchGoods")
    public Object searchGoods(@RequestParam("keywords") String keywords,
                              @RequestParam(value = "current", defaultValue = "1") Integer current,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            if (ValidatorUtils.empty(keywords)) {
                return new CommonResult().failed("清输入查询条件");
            }
            PageHelper.startPage(current, size);
            Map<String, Object> map = goodsService.searchGoods(keywords);
            map.put("current", current);
            map.put("size", size);
            //   map.put("total", (int) PageHelper.);
            return new CommonResult().success(map);

        } catch (Exception e) {
            log.error("首页搜索：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @Deprecated
    @ApiOperation("商品详情商品同组的的商品")
    @PostMapping(value = "/goodsListByGoodsId")
    public Object goodsListByGoodsId(@ApiParam("商品id") @RequestParam("id") Long id) {
        if (ValidatorUtils.empty(id)) {
            return new CommonResult().paramFailed();
        }
        EsShopGoods goods = goodsService.getById(id);
        if (goods != null && ValidatorUtils.empty(goods.getCategoryId())) {
            return new CommonResult().success(new ArrayList<EsShopGoods>());
        }
        List<String> ids = Arrays.asList(goods.getCategoryId().split(","));
        List<EsShopGoods> goodsList = goodsService.selectgoodsListByCateIds(ids);
        return new CommonResult().success(goodsList);
    }

    @ApiOperation("通过规格获取sku")
    @PostMapping(value = "/getGoodsSku")
    public Object getGoodsSku(@ApiParam("商品id") @RequestParam("id") Long id,
                              @ApiParam("商品规格") @RequestParam("specs") String specs) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed();
            }
            if (ValidatorUtils.empty(specs)) {
                return new CommonResult().paramFailed();
            }
            // List<EsShopGoodsOption> skuList =
            return goodsService.getGoodsSku(id, specs);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().paramFailed(e.getMessage());
        }
    }

    @ApiOperation("查询一级分类")
    @PostMapping("/selShopGoodsCategoryOne")
    public Object selShopGoodsCategoryOne() {
        try {
            return goodsService.selDisplayShopGoodsCategoryOne();
        } catch (Exception e) {
            log.error("查询一级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询二级分类")
    @PostMapping("/selShopGoodsCategoryTwo")
    public Object selShopGoodsCategoryTwo(@RequestParam("parentId") Long parentId, @RequestParam("level") Integer level) {
        try {
            if (ValidatorUtils.empty(parentId)) {
                return new CommonResult().failed("父级ID");
            } else {
                return goodsService.selShopGoodsCategoryTwo(parentId, level);
            }
        } catch (Exception e) {
            log.error("查询一级分类异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询自定义页面首页")
    @PostMapping("/selDiyPageHome")
    public Object selDiyPageHome() {
        try {
            EsShopDiypage diypage = new EsShopDiypage();
            diypage.setStatus(1);
            diypage.setType(2);
            // EsShopDiypage newDiy = esShopDiypageService.getOne(new QueryWrapper<>(diypage));
            EsShopDiypage newDiy = (EsShopDiypage) JsonUtils.fromJson(redisRepository.get(String.format(RedisConstant.EsShopDiypage, 12)),EsShopDiypage.class);
            if (ValidatorUtils.empty(newDiy)) {
                newDiy = esShopDiypageService.getOne(new QueryWrapper<>(diypage));
                redisRepository.set(String.format(RedisConstant.EsShopDiypage, 12), newDiy);
            }
            return new CommonResult().success("success", newDiy);
        } catch (Exception e) {
            log.error("查询自定义页面首页异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询自定义页面商品详情页")
    @PostMapping("/selDiyPageGoodsDetail")
    public Object selDiyPageGoodsDetail(@RequestParam("goodsId") Long goodsId) {
        try {
            if (ValidatorUtils.empty(goodsId)) {
                return new CommonResult().failed("商品ID为空");
            }
            return new CommonResult().success("success", esShopGoodsDiypageMapService.select(goodsId));
        } catch (Exception e) {
            log.error("查询自定义页面首页异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询自定义页面商品详情页")
    @PostMapping("/selUser")
    public Object selUser(EsShopDiypage entity) {
        try {
            entity.setType(3);
            entity.setStatus(1);
            return new CommonResult().success("success", esShopDiypageService.getOne(new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("查询自定义页面首页异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("根据ID查询自定义页面详情")
    @PostMapping("/selCustom")
    public Object selCustom(EsShopDiypage entity) {
        try {
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定编号");
            }
            entity.setType(5);
            entity.setStatus(1);
            return new CommonResult().success("success", esShopDiypageService.getOne(new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("查询自定义页面首页异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询启用广告")
    @PostMapping("/selStartAdvert")
    public Object selStartAdvert() {
        try {
            //查询立即启用广告和定时启用广告
            EsStartAdvertising esStartAdvertising = new EsStartAdvertising();
            esStartAdvertising.setIsStart(1);
            EsStartAdvertising list = esStartAdvertisingService.getOne(new QueryWrapper<>(esStartAdvertising));
            if (list != null) {
                EsStartAdvertisingImg img = new EsStartAdvertisingImg();
                img.setAdvertisingId(list.getId());
                list.setListAdvertImg(esStartAdvertisingImgService.list(new QueryWrapper<>(img)));
                return new CommonResult().success("success", list);
            }
            esStartAdvertising.setIsStart(2);
            list = esStartAdvertisingService.getOne(new QueryWrapper<>(esStartAdvertising));
            if (list != null) {
                //判断启用时间是否超过当前时间
                if (Long.parseLong(list.getStartTime()) <= System.currentTimeMillis()) {
                    EsStartAdvertisingImg img = new EsStartAdvertisingImg();
                    img.setAdvertisingId(list.getId());
                    list.setListAdvertImg(esStartAdvertisingImgService.list(new QueryWrapper<>(img)));
                    return new CommonResult().success("success", list);
                }

            }
            return new CommonResult().success("success", "");
        } catch (Exception e) {
            log.error("查询启用广告异常：", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询商品定制服务")
    @PostMapping("/selectCust")
    public Object selectCust(@RequestParam("id") Long id) {
        if (ValidatorUtils.empty(id)) {
            return new CommonResult().failed("请选择商品");
        }
        List<EsShopCustomizedLegend> listLegend = new ArrayList<EsShopCustomizedLegend>();
        List<EsShopCustomizedPacket> listPacket = new ArrayList<EsShopCustomizedPacket>();
        List<EsShopCustomizedCard> listCard = new ArrayList<EsShopCustomizedCard>();
        List<EsShopCustomizedPacket> listPackBox = new ArrayList<EsShopCustomizedPacket>();
        Map<String, Object> map = new HashMap<String, Object>();
        EsShopGoods goods = goodsService.getById(id);
        if (goods.getLetterStatus() == 1) {
            if (goods.getLetterIds() != null && !goods.getLetterIds().equals("")) {
                String[] attr = goods.getLetterIds().split(",");
                for (int i = 0; i < attr.length; i++) {
                    if (attr[i] != null && !attr[i].equals("")) {
                        EsShopCustomizedLegend legend = esShopCustomizedLegendService.getById(Long.parseLong(attr[i]));
                        if (legend != null) {
                            listLegend.add(legend);
                        }

                    }

                }
            }
        }

        if (goods.getPacketStatus() == 1) {
            if (goods.getPacketContent() != null && !goods.getPacketContent().equals("")) {
                String[] attr = goods.getPacketContent().split(",");
                for (int i = 0; i < attr.length; i++) {
                    EsShopCustomizedPacket packet = esShopCustomizedPacketServer.getById(Long.parseLong(attr[i]));
                    if (packet != null) {
                        if (packet.getType() == 1) {
                            listPacket.add(packet);
                        } else {
                            listPackBox.add(packet);
                        }


                    }

                }
            }
        }
        if (goods.getCardStatus() == 1) {
            if (goods.getCardContent() != null && !goods.getCardContent().equals("")) {
                String[] attr = goods.getCardContent().split(",");
                for (int i = 0; i < attr.length; i++) {
                    if (attr[i] != null && !attr[i].equals("") && !attr[i].equals("1")) {
                        EsShopCustomizedCard card = esShopCustomizedCardService.getById(Long.parseLong(attr[i]));
                        if (card != null) {
                            listCard.add(card);
                        }

                    }

                }
            }
        }
        EsShopCustomizedBasic basic = esShopCustomizedBasicService.getOne(new QueryWrapper<>());
        map.put("card", listCard);
        map.put("pagket", listPacket);
        map.put("pagkBox", listPackBox);
        map.put("legend", listLegend);
        map.put("basic", basic);
        return new CommonResult().success("success", map);

    }

}
