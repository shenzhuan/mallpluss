package com.mei.zhuang.controller.order;

import com.arvato.service.order.api.feigin.GoodsFegin;
import com.arvato.service.order.api.service.EsActivatySmallBeautyBoxGiftBoxService;
import com.arvato.service.order.api.service.EsActivatySmallBeautyBoxGoodsService;
import com.arvato.service.order.api.service.EsActivatySmallBeautyBoxService;
import com.arvato.service.order.api.service.ShopOrderService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.goods.EsShopGoods;
import com.mei.zhuang.entity.goods.EsShopGoodsOption;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBox;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGiftBox;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGoods;
import com.mei.zhuang.entity.order.EsShopCart;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/7/31 16:17
 * @Version 1.0
 **/
@Slf4j
@Api(value = "小美盒管理", description = "", tags = {"小程序小美盒管理"})
@RestController
@RequestMapping("/applet/small")
public class AppletSmallBeautyBoxController {

    @Resource
    private EsActivatySmallBeautyBoxService esActivatySmallBeautyBoxService;
    @Resource
    private EsActivatySmallBeautyBoxGiftBoxService esActivatySmallBeautyBoxGiftBoxService;
    @Resource
    private EsActivatySmallBeautyBoxGoodsService esActivatySmallBeautyBoxGoodsService;
    @Resource
    private ShopOrderService shopOrderService;
    @Resource
    private GoodsFegin goodsFegin;

    @SysLog(MODULE = "小美盒管理", REMARK = "查询正在进行的小美盒活动")
    @ApiOperation("查询正在进行的小美盒活动")
    @PostMapping(value = "/selSmallBeautyBox")
    public Object selSmallBeautyBox(EsActivatySmallBeautyBox entity) {
        try {
            if(ValidatorUtils.empty(entity.getMemberId())){
                return new CommonResult().failed("请指定用户编号");
            }
            //查询正在进行的活动
            Map<String,Object> map = new HashMap<String,Object>();
            long beginTime = System.currentTimeMillis();
            ////您有未结算的礼盒
            //判断购物车是否有未结算的定制礼盒
            EsMember member = new EsMember();
            member.setId(entity.getMemberId());
            Long countNum = shopOrderService.selectExist(member);
            if(countNum>0){
                EsActivatySmallBeautyBoxGiftBox giftBox = esActivatySmallBeautyBoxGiftBoxService.selectById(countNum);
                List<EsActivatySmallBeautyBox> list = esActivatySmallBeautyBoxService.selectList(new QueryWrapper<>());
                if(list != null && list.size() > 0){
                    for (EsActivatySmallBeautyBox box : list) {
                        if (Long.parseLong(box.getStartTime()) <= beginTime) {
                            if (Long.parseLong(box.getEndTime()) >= beginTime) {
                                //查询当前时间点正在进行的活动的小美盒
                                EsActivatySmallBeautyBoxGoods goods = new EsActivatySmallBeautyBoxGoods();
                                goods.setActivatyId(box.getId());
                                goods.setProduct(1);
                                List<EsActivatySmallBeautyBoxGoods> listGoods = esActivatySmallBeautyBoxGoodsService.selectList(new QueryWrapper<>(goods));
                                map.put("activaty",box);
                                map.put("goods",listGoods);
                                map.put("giftBox",giftBox);
                                return new CommonResult().success("success", map);
                            }
                        }
                    }
                }
                return new CommonResult().success("success",null);
            }

            List<EsActivatySmallBeautyBox> list = esActivatySmallBeautyBoxService.selectList(new QueryWrapper<>());
            if(list != null && list.size() > 0){
                for (EsActivatySmallBeautyBox box : list) {
                    if (Long.parseLong(box.getStartTime()) <= beginTime) {
                        if (Long.parseLong(box.getEndTime()) >= beginTime) {
                            //查询当前时间点正在进行的活动的小美盒
                            EsActivatySmallBeautyBoxGoods goods = new EsActivatySmallBeautyBoxGoods();
                            goods.setActivatyId(box.getId());
                            goods.setProduct(1);
                            List<EsActivatySmallBeautyBoxGoods> listGoods = esActivatySmallBeautyBoxGoodsService.selectList(new QueryWrapper<>(goods));
                            map.put("activaty",box);
                            map.put("goods",listGoods);
                            return new CommonResult().success("success", map);
                        }
                    }
                }
            }
            return new CommonResult().failed("该时间段内暂无活动");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "小美盒管理", REMARK = "根据步数查询对应的code")
    @ApiOperation("根据步数查询对应的code")
    @PostMapping(value = "/selSmallGoods")
    public Object selSmallGoods(EsActivatySmallBeautyBoxGoods entity){
        try{
            if(ValidatorUtils.empty(entity.getActivatyId())){
                return new CommonResult().failed("请指定活动编号");
            }
            if(ValidatorUtils.empty(entity.getSerialNember())){
                return new CommonResult().failed("请指定步数");
            }
            //此编码指当前步数选择的编码加前几步的编码
            if(ValidatorUtils.empty(entity.getProductCode())){
                return new CommonResult().failed("请指定商品编码");
            }
            List<EsActivatySmallBeautyBoxGoods> list = new ArrayList<EsActivatySmallBeautyBoxGoods>();

            String[] attr = entity.getProductCode().split(",");
            List<String> listString=Arrays.asList(attr);
            List<String> listStr = new ArrayList<String>();
            List<EsActivatySmallBeautyBoxGiftBox> listCode = new ArrayList<EsActivatySmallBeautyBoxGiftBox>();
            List<EsActivatySmallBeautyBoxGoods> lists = new ArrayList<EsActivatySmallBeautyBoxGoods>();

            //1、查询所有相匹配的定制礼盒
            List<EsActivatySmallBeautyBoxGiftBox> listGoods =  esActivatySmallBeautyBoxGiftBoxService.selectSmall(listString,entity.getActivatyId());
            List<String> attrGift= new ArrayList<String>();
            if(entity.getSerialNember() ==5){
                //直接返回一个礼盒
                for (EsActivatySmallBeautyBoxGiftBox gift:listGoods) {
                    attrGift = Arrays.asList(gift.getProductCode().split(","));
                }
                List<String> attrCode = Arrays.asList(entity.getProductCode().split(","));
                attrCode =getDiffrent(attrGift,attrCode);
                List<EsActivatySmallBeautyBoxGoods> lis = new ArrayList<EsActivatySmallBeautyBoxGoods>();
                for (String gb:attrCode) {
                    EsActivatySmallBeautyBoxGoods goods = new EsActivatySmallBeautyBoxGoods();
                    goods.setActivatyId(entity.getActivatyId());
                    goods.setProductCode(gb);
                    lis.add(esActivatySmallBeautyBoxGoodsService.selectOne(new QueryWrapper<>(goods)));
                }
                return new CommonResult().success("success",lis);
            }else{
                for (EsActivatySmallBeautyBoxGiftBox gift:listGoods) {
                    String[] attrPC = gift.getProductCode().split(",");
                    for (int i=0;i<attrPC.length;i++){
                        for (String str:listString) {
                            if(str.equals(attrPC[i])){
                                listCode.add(gift);
                            }
                        }
                    }
                }
                //2、查询当前活动步数的产品
                EsActivatySmallBeautyBoxGoods boxGoods = new EsActivatySmallBeautyBoxGoods();
                boxGoods.setActivatyId(entity.getActivatyId());
                boxGoods.setProduct(entity.getSerialNember());
                List<EsActivatySmallBeautyBoxGoods> goodsList = esActivatySmallBeautyBoxGoodsService.selectList(new QueryWrapper<>(boxGoods));
                for (EsActivatySmallBeautyBoxGoods goods :goodsList) {//礼品
                    for (EsActivatySmallBeautyBoxGiftBox code:listCode) {//礼盒
                        String[] attrCode=code.getProductCode().split(",");
                        for (int i=0;i<attrCode.length;i++){
                            if(attrCode[i].equals(goods.getProductCode())){
                                lists.add(goods);
                            }
                        }
                    }
                }
                HashSet h = new HashSet(lists);
                lists.clear();
                lists.addAll(h);
                return new CommonResult().success("success",lists);

            }

        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作异常");
        }
}
    private static List<String> getDiffrent(List<String> list1, List<String> list2) {
        long st = System.nanoTime();
        List<String> diff = new ArrayList<String>();
        for(String str:list1)
        {
            if(!list2.contains(str))
            {
                diff.add(str);
            }
        }
        System.out.println("total times "+(System.nanoTime()-st));
        return diff;
    }




    @SysLog(MODULE = "小美盒管理", REMARK = "根据code查询对应礼盒")
    @ApiOperation("根据code查询对应礼盒")
    @PostMapping("/selGiftBox")
    public Object selGiftBox(EsActivatySmallBeautyBoxGiftBox entity){
        try{
            if(ValidatorUtils.empty(entity.getProductCode())){
                return new CommonResult().failed("产品编码为空");
            }
            if(ValidatorUtils.empty(entity.getActivatyId())){
                return new CommonResult().failed("请指定活动编号");
            }
            String[] attr = entity.getProductCode().split(",");
            List<String> list =Arrays.asList(attr);
            List<EsActivatySmallBeautyBoxGiftBox> lists = esActivatySmallBeautyBoxGiftBoxService.selectSmall(list,entity.getActivatyId());
            for (EsActivatySmallBeautyBoxGiftBox box:lists) {
                return new CommonResult().success("success",box);
            }
            return new CommonResult().failed("暂无");
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

   @SysLog(MODULE = "小美盒管理", REMARK = "添加小美盒到购物车")
    @ApiOperation("添加小美盒到购物车")
    @PostMapping("/insSmallBeautyCart")
    public Object insSmallBeautyCart(EsActivatySmallBeautyBoxGiftBox entity){
        try{
            if(ValidatorUtils.empty(entity.getId())){
                return new CommonResult().failed("请指定礼盒编号");
            }
            if(ValidatorUtils.empty(entity.getMemberId())){
                return new CommonResult().failed("请指定用户编号");
            }
            EsShopGoods goods = goodsFegin.goodsDetail(entity.getId());
            if(goods != null){
                EsShopGoodsOption goodsOption = goodsFegin.goodsOption(goods.getId());
                EsShopCart cart = new EsShopCart();
                cart.setMemberId(entity.getMemberId());
                cart.setCreateTime(new Date());
                cart.setIsLoseEfficacy(0);
                cart.setTotal(1);
                cart.setActivatyType(1);//小美盒
                cart.setPrice(goods.getPrice());
                cart.setGoodsName(goods.getTitle());
                cart.setPic(goodsOption.getThumb());
                cart.setShopId(Long.parseLong("0"));
                cart.setGoodsId(goods.getId());
                cart.setCreateTime(new Date());
                cart.setOptionName(goodsOption.getTitle());
                cart.setOptionId(goodsOption.getId());
                return new CommonResult().success("success",shopOrderService.inserts(cart));
            }
            return new CommonResult().success("success",null);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒管理", REMARK = "(查询商品编号)")
    @ApiOperation("送礼(查询商品编号)")
    @PostMapping("/selGoodsId")
    public Object selGoodsId(@RequestParam("id") Long id){
        try{

            if(ValidatorUtils.empty(id)){
                return new CommonResult().failed("请指定礼盒编号");
            }
            EsShopGoods goods = goodsFegin.goodsDetail(id);
            if(goods != null){
                return new CommonResult().success("success",goods.getId());
            }
            return new CommonResult().success("success",null);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }



}
