package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsShopBrandMapper;
import com.mei.zhuang.entity.goods.EsShopBrand;
import com.mei.zhuang.service.goods.EsShopBrandService;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Api(value = "品牌", description = "", tags = {"品牌"})
@Service
public class EsShopBrandServiceImpl extends ServiceImpl<EsShopBrandMapper, EsShopBrand> implements EsShopBrandService {

    @Resource
    private EsShopBrandMapper brandMapper;


    @ApiOperation("添加品牌")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveBrand(EsShopBrand entity) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            entity.setCreateTime(new Date());
            brandMapper.addbrand(entity);
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("添加品牌：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询品牌明细")
    @Override
    public Object detailBrand(Long brandid) {
        try {
            return new CommonResult().success(brandMapper.selectbrandid(brandid));
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("更改品牌信息")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object updateBrand(EsShopBrand entity) {
        try {
            brandMapper.updatebrand(entity);
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("更改品牌：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("删除品牌")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object deleteBrand(Long brandid) {
        try {
            brandMapper.deletebrand(brandid);
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("删除品牌：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }
}
