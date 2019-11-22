package com.mei.zhuang.controller.order;

import com.alibaba.fastjson.JSONObject;

import com.mei.zhuang.service.order.EsActivatySmallBeautyBoxGiftBoxService;
import com.mei.zhuang.service.order.EsActivatySmallBeautyBoxGoodsService;
import com.mei.zhuang.service.order.EsActivatySmallBeautyBoxService;
import com.mei.zhuang.service.order.GoodsFegin;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.utils.ValidatorUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBox;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGiftBox;
import com.mei.zhuang.entity.order.EsActivatySmallBeautyBoxGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "小美盒定制管理", description = "", tags = {"小美盒定制管理"})
@RestController
@RequestMapping("/api/smallBeautyBox")
public class EsActivatySmallBeautyBoxController {

    @Resource
    private EsActivatySmallBeautyBoxService esActivatySmallBeautyBoxService;
    @Resource
    private EsActivatySmallBeautyBoxGoodsService esActivatySmallBeautyBoxGoodsService;
    @Resource
    private EsActivatySmallBeautyBoxGiftBoxService esActivatySmallBeautyBoxGiftBoxService;
    @Resource
    private GoodsFegin goodsFegin;


    @SysLog(MODULE = "小美盒定制管理", REMARK = "查询小美盒定制活动列表")
    @ApiOperation("查询小美盒定制活动列表")
    @PostMapping("/list")
    public Object selPagelist(EsActivatySmallBeautyBox entity){
        try{
            return new CommonResult().success("success",esActivatySmallBeautyBoxService.selPageList(entity));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("列表加载失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "删除小美盒定制活动")
    @ApiOperation("删除小美盒定制活动")
    @PostMapping("/delete")
    public Object deleteById(@RequestParam("id") Long id){
        try{
            return new CommonResult().success("success",esActivatySmallBeautyBoxService.deleteById(id));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "新增小美盒定制活动")
    @ApiOperation("新增小美盒定制活动")
    @PostMapping("/insert")
    public Object insert(EsActivatySmallBeautyBox entity){
        try{
            if(ValidatorUtils.empty(entity.getActivatyName())){
                return new CommonResult().failed("请指定活动名称");
            }
            long begin = Long.parseLong(entity.getStartTime());
            long end = Long.parseLong(entity.getEndTime());
            //判断时间段内是否有进行中的活动
            List<EsActivatySmallBeautyBox> list =esActivatySmallBeautyBoxService.list(new QueryWrapper<>());
            if(list != null && list.size() !=0) {
                for (EsActivatySmallBeautyBox box : list) {
                    long beginTime = Long.parseLong(box.getStartTime());//开始时间
                    long endTime = Long.parseLong(box.getEndTime());//结束时间
                    if (beginTime >= begin) {
                        if (endTime >= end) {
                            return new CommonResult().failed("该时间段内已存在活动");
                        } else {
                            return new CommonResult().failed("该时间段内已存在活动");
                        }
                    }
                }
            }
            entity.setCreateTime(new Date());
            return new CommonResult().success("success",esActivatySmallBeautyBoxService.save(entity));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "小美盒定制活动详情")
    @ApiOperation("小美盒定制活动详情")
    @PostMapping("/detail")
    public Object detail(@RequestParam("id") Long id){
        try{
            if(ValidatorUtils.empty(id)){
                return new CommonResult().failed("请指定活动编号");
            }
            return new CommonResult().success("success",esActivatySmallBeautyBoxService.getById(id));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "修改小美盒定制活动")
    @ApiOperation("修改小美盒定制活动")
    @PostMapping("/update")
    public Object update(EsActivatySmallBeautyBox entity){
        try{
            if(ValidatorUtils.empty(entity.getId())){
                return new CommonResult().failed("请指定活动编号");
            }
            if(ValidatorUtils.empty(entity.getActivatyName())){
                return new CommonResult().failed("请指定活动名称");
            }
            long begin = Long.parseLong(entity.getStartTime());
            long end = Long.parseLong(entity.getEndTime());
            //判断时间段内是否有进行中的活动
            List<EsActivatySmallBeautyBox> list =esActivatySmallBeautyBoxService.select(entity);
            if(list != null && list.size() !=0) {
                for (EsActivatySmallBeautyBox box : list) {
                    long beginTime = Long.parseLong(box.getStartTime());//开始时间
                    long endTime = Long.parseLong(box.getEndTime());//结束时间
                    if (beginTime >= begin) {
                        if (endTime >= end) {
                            return new CommonResult().failed("该时间段内已存在活动");
                        } else {
                            return new CommonResult().failed("该时间段内已存在活动");
                        }
                    }
                }
            }
            return new CommonResult().success("success",esActivatySmallBeautyBoxService.updateById(entity));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "根据活动编号和阶段编号查询产品")
    @ApiOperation("根据活动编号和阶段编号查询产品")
    @PostMapping("/selProductList")
    public Object selProductList(EsActivatySmallBeautyBoxGoods entity){
        try{
            if(ValidatorUtils.empty(entity.getActivatyId())){
                return new CommonResult().failed("请指定活动编号");
            }
            if(ValidatorUtils.empty(entity.getProduct())){
                return new CommonResult().failed("请指定阶段编号");
            }
            return new CommonResult().success("success",esActivatySmallBeautyBoxGoodsService.selPageList(entity));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "添加产品")
    @ApiOperation("添加产品")
    @PostMapping("/insActivaty")
    public Object insActivaty(EsActivatySmallBeautyBoxGoods entity){
        try{
            boolean bool =false;
            List<EsActivatySmallBeautyBoxGoods> listSmallBeautyBoxGoods = JSONObject.parseArray(entity.getDate(), EsActivatySmallBeautyBoxGoods.class);
            Integer num =1;
            for (EsActivatySmallBeautyBoxGoods goods:listSmallBeautyBoxGoods ) {
                if(goods.getProduct() == null){
                    return new CommonResult().failed("请指定阶段编号");
                }

                if(goods.getId() == null ){
                    //新增
                    bool = esActivatySmallBeautyBoxGoodsService.save(goods);
                }else{
                    //修改
                    bool = esActivatySmallBeautyBoxGoodsService.updateById(goods);
                }
            }
            return new CommonResult().success("success",bool);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "修改产品")
    @ApiOperation("修改产品")
    @PostMapping("/updActivaty")
    public Object updActivaty(EsActivatySmallBeautyBoxGoods entity){
        try{
            boolean bool =false;
            List<EsActivatySmallBeautyBoxGoods> listSmallBeautyBoxGoods = JSONObject.parseArray(entity.getDate(), EsActivatySmallBeautyBoxGoods.class);
            Integer num =1;
            for (EsActivatySmallBeautyBoxGoods goods:listSmallBeautyBoxGoods ) {
                if(goods.getProduct() == null){
                    return new CommonResult().failed("请指定阶段编号");
                }

                if(goods.getId() == null ){
                    //新增
                    bool = esActivatySmallBeautyBoxGoodsService.save(goods);
                }else{
                    //修改
                    bool = esActivatySmallBeautyBoxGoodsService.updateById(goods);
                }
            }
            return new CommonResult().success("success",bool);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "删除产品")
    @ApiOperation("删除产品")
    @PostMapping("/delActivaty")
    public Object delActivaty(@RequestParam("id") Long id){
        try{
            if(ValidatorUtils.empty(id)){
                return new CommonResult().failed("请指定产品编号");
            }
            return new CommonResult().success("success",esActivatySmallBeautyBoxGoodsService.removeById(id));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "查询礼盒")
    @ApiOperation("查询礼盒")
    @PostMapping("/selGiftBox")
    public Object selGiftBox(EsActivatySmallBeautyBoxGiftBox entity){
        try{
            if(ValidatorUtils.empty(entity.getActivatyId())){
                return new CommonResult().failed("请指定活动编号");
            }
            return new CommonResult().success("success",esActivatySmallBeautyBoxGiftBoxService.select(entity));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "新增礼盒")
    @ApiOperation("新增礼盒")
    @PostMapping("/insGiftBox")
    public Object insGiftBox(EsActivatySmallBeautyBoxGiftBox entity){
        try{
            if(ValidatorUtils.empty(entity.getBoxName())){
                return new CommonResult().failed("请指定礼盒名称");
            }
            if(ValidatorUtils.empty(entity.getBoxCode())){
                return new CommonResult().failed("请指定礼盒编码");
            }
            if(ValidatorUtils.empty(entity.getBoxImg())){
                return new CommonResult().failed("请指定礼盒图片");
            }
            if(ValidatorUtils.empty(entity.getVituralStock())){
                return new CommonResult().failed("请指定虚拟库存");
            }
            if(ValidatorUtils.empty(entity.getBoxMoney())){
                return new CommonResult().failed("请指定礼盒价格");
            }
            if(ValidatorUtils.empty(entity.getActivatyId())){
                return new CommonResult().failed("请指定所属活动");
            }
            entity.setCreateTime(new Date());
            esActivatySmallBeautyBoxGiftBoxService.save(entity);
            String[] attr =entity.getProductCode().split(",");

            return new CommonResult().success("success",goodsFegin.insSmallBeautyBoxCust(entity.getBoxName(),entity.getBoxCode(),entity.getBoxImg(),entity.getVituralStock(),entity.getBoxMoney(),entity.getId(),attr[0]));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "礼盒详情")
    @ApiOperation("礼盒详情")
    @PostMapping("/detailGiftBox")
    public Object detailGiftBox(@RequestParam("id") Long id){
        try{
            if(ValidatorUtils.empty(id)){
                return new CommonResult().failed("请指定编号");
            }
            return new CommonResult().success("success",esActivatySmallBeautyBoxGiftBoxService.getById(id));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "修改礼盒")
    @ApiOperation("修改礼盒")
    @PostMapping("/updGiftBox")
    public Object updGiftBox(EsActivatySmallBeautyBoxGiftBox entity){
        try{
            if(ValidatorUtils.empty(entity.getId())){
                return new CommonResult().failed("请指定编号");
            }
            String[] attr= entity.getProductCode().split(",");
            goodsFegin.updSmallBeautyBoxCust(entity.getBoxName(),entity.getBoxCode(),entity.getBoxImg(),entity.getVituralStock(),entity.getBoxMoney(),entity.getId(),attr[0]);
            return new CommonResult().success("success",esActivatySmallBeautyBoxGiftBoxService.updateById(entity));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "删除礼盒")
    @ApiOperation("删除礼盒")
    @PostMapping("/delGiftBox")
    public Object delGiftBox(@RequestParam("id") Long id){
        try{
            if(ValidatorUtils.empty(id)){
                return new CommonResult().failed("请指定编号");
            }
            goodsFegin.delSmallBeautyBoxCust(id);
            return new CommonResult().success("success",esActivatySmallBeautyBoxGiftBoxService.removeById(id));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "复制新增礼盒")
    @ApiOperation("复制新增礼盒")
    @PostMapping("/copyGiftBox")
    public Object copyGiftBox(@RequestParam("id") Long id){
        try{
            if(ValidatorUtils.empty(id)){
                return new CommonResult().failed("请指定编号");
            }
            boolean bool=false;
            EsActivatySmallBeautyBoxGiftBox boxGiftBox = esActivatySmallBeautyBoxGiftBoxService.getById(id);
            boxGiftBox.setId(null);
            boxGiftBox.setCreateTime(new Date());
            bool = esActivatySmallBeautyBoxGiftBoxService.save(boxGiftBox);
            EsActivatySmallBeautyBoxGiftBox box = new EsActivatySmallBeautyBoxGiftBox();
            box.setBoxCode(boxGiftBox.getBoxCode());
            box.setActivatyId(boxGiftBox.getActivatyId());
            box.setBoxImg(boxGiftBox.getBoxImg());
            box.setProductCode(boxGiftBox.getProductCode());
            Long ids = null;
            if(bool ==true){
                List<EsActivatySmallBeautyBoxGiftBox> list =esActivatySmallBeautyBoxGiftBoxService.list(new QueryWrapper<>(box));
                for (EsActivatySmallBeautyBoxGiftBox goxs:list) {
                    if(ids == null){
                        ids=goxs.getId();
                    }else{
                        if(goxs.getId() >ids){
                            ids=goxs.getId();
                        }
                    }
                }
            }
            String[] attr=boxGiftBox.getProductCode().split(",");
            return new CommonResult().success("success",goodsFegin.insSmallBeautyBoxCust(boxGiftBox.getBoxName(),boxGiftBox.getBoxCode(),boxGiftBox.getBoxImg(),boxGiftBox.getVituralStock(),boxGiftBox.getBoxMoney(),ids,attr[0]));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @SysLog(MODULE = "小美盒定制管理", REMARK = "查询所有活动")
    @ApiOperation("查询所有活动")
    @PostMapping("/selectActivaty")
    public Object selectActivaty(){
        try{
            return new CommonResult().success("success",esActivatySmallBeautyBoxService.list(new QueryWrapper<>()));
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult().failed("操作失败");
        }
    }

    @ApiOperation("修改礼盒库存")
    @PostMapping("/updvituralStock")
    public boolean updvituralStock(@RequestParam("id")Long id,@RequestParam("vituralStock")Integer vituralStock){
        try{
            EsActivatySmallBeautyBoxGiftBox giftBox = esActivatySmallBeautyBoxGiftBoxService.getById(id);
            giftBox.setVituralStock(giftBox.getVituralStock()-vituralStock);
            return esActivatySmallBeautyBoxGiftBoxService.updateById(giftBox);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
