package com.mei.zhuang.controller.goods;

import com.arvato.service.goods.api.service.*;
import com.arvato.utils.annotation.SysLog;
import com.mei.zhuang.entity.goods.EsShopCustomizedBasic;
import com.mei.zhuang.entity.goods.EsShopCustomizedPacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-07-05 15:45
 * @Description:
 */
@Slf4j
@Api(value = "定制服务管理", description = "", tags = {"定制服务管理"})
@RestController
@RequestMapping("/api/goods/custMarking")
public class OrderCustMakingController {


   /*
    @Resource
    private EsShopCustomizedLegendService esShopCustomizedLegendService;//定制服务样图service
    @Resource
    private EsShopCustomizedCardService esShopCustomizedCardService;//刻字服务：定制卡片service
    @Resource
    private EsShopCardMessageServer esShopCardMessageServer;//卡片寄语设置service

    */
    @Resource
    private EsShopCustomizedPacketServer esShopCustomizedPacketServer;//封套/包装列表service
    @Resource
    private EsShopCustomizedBasicService esShopCustomizedBasicService;//刻字服务:基本信息service


   /*
    @SysLog(MODULE = "定制服务管理", REMARK = "查询定制服务")
    @ApiOperation("查询定制服务")
    @PostMapping(value = "/selMakingDetail")
    public Map<String, OrderGoodsCustMakingInfo> getSkuById(@ApiParam @RequestParam("购物车id") List<Long> cartIds) {
        Map<String, OrderGoodsCustMakingInfo> data = new HashMap<>();
        //通过定制服务 业务类获得定制服务相关数据 构造所需数据。
        return data;
    }

    */

    @SysLog(MODULE = "定制服务管理", REMARK = "查询定制服务集合")
    @ApiOperation("获得封套/包装盒信息")
    @PostMapping(value = "/getPackInfo")
    public List<EsShopCustomizedPacket> getPackInfoList(@ApiParam @RequestParam("套装ids") List<Long> packIds) {
        return this.esShopCustomizedPacketServer.selectBatchIds(packIds);
    }

    @SysLog(MODULE = "定制服务管理", REMARK = "查询单个定制服务")
    @ApiOperation("获得封套/包装盒信息")
    @PostMapping(value = "/getPackInfoByOne")
    public EsShopCustomizedPacket getPackInfo(@ApiParam @RequestParam("套装id") Long packId) {
        return this.esShopCustomizedPacketServer.selectById(packId);
    }

    @SysLog(MODULE = "定制服务管理", REMARK = "查询单个定制服务")
    @ApiOperation("获得封套/包装盒信息")
    @PostMapping("/getCustBasicById")
    public EsShopCustomizedBasic getCustBasicById(@ApiParam @RequestParam("定字基础id") Long basic) {
        return this.esShopCustomizedBasicService.selectById(basic);
    }

    @SysLog(MODULE = "定制服务管理", REMARK = "查询单个定制服务")
    @ApiOperation("获得封套/包装盒信息")
    @PostMapping("/getCustBasicByIds")
    public List<EsShopCustomizedBasic> getCustBasicByIds(@ApiParam @RequestParam("定字基础ids") List<Long> basicIds) {
        return this.esShopCustomizedBasicService.selectBatchIds(basicIds);
    }

}
