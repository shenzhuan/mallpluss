package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import io.swagger.annotations.ApiOperation;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:14
 * @Description:
 */
public interface EsShopDiypageService extends IService<EsShopDiypage> {


    @ApiOperation("修改模版状态")
    Object updStatus(Long id, Integer status, Integer typeId);
    @ApiOperation("查询是否存在同类型")
    Integer selDiyPageTypeId(Integer typeId, Long id);
    @ApiOperation("查询模板详情")
    EsShopDiypage selDiyDetail(Integer id);

    Integer selectCounts(Long id, String name);
}
