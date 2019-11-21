package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsShopBrand;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public interface EsShopBrandService extends IService<EsShopBrand> {
    @ApiOperation("保存品牌")
    @PostMapping(value = "/shop/brand/saves")
    public Object saveBrand(@RequestBody EsShopBrand entity);

    @ApiOperation("查询品牌明细")
    @PostMapping(value = "/shop/brand/detail")
    public Object detailBrand(Long brandid);

    @ApiOperation("更改品牌")
    @PostMapping(value = "/shop/brand/update")
    public Object updateBrand(EsShopBrand entity);

    @ApiOperation("删除品牌")
    @PostMapping(value = "/shop/brand/delete")
    public Object deleteBrand(Long brandid);
}
