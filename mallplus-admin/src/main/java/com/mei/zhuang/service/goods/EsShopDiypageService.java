package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:14
 * @Description:
 */
public interface EsShopDiypageService extends IService<EsShopDiypage> {

    @ApiOperation("条件查询自定义模版")
    Map<String, Object> selDiyPage(EsShopDiypage entity);

    @ApiOperation("条件查询自定义模版详情页")
    Map<String, Object> selDiyPageDetail(EsShopDiypage entity);
    @ApiOperation("修改模版状态")
    Object updStatus(Long id, Integer status, Integer typeId);
    @ApiOperation("查询是否存在同类型")
    Integer selDiyPageTypeId(Integer typeId, Long id);
    @ApiOperation("查询模板详情")
    EsShopDiypage selDiyDetail(Integer id);

    Integer selectCounts(Long id, String name);
}
