package com.mei.zhuang.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mei.zhuang.annotation.FieldText;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.sys.EsShopNew;
import com.mei.zhuang.service.sys.IShopBrandService;
import com.mei.zhuang.service.sys.IShopService;
import com.mei.zhuang.utils.DateUtils;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.ShopParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * @Auther: Tiger
 * @Date: 2019-05-28 14:10
 * @Description:
 */
@Api(value = "店铺设置管理", description = "", tags = {"店铺设置管理"})
@Slf4j
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Resource
    private IShopService shopService;
    @Resource
    private IShopBrandService shopBrandService;
    @SysLog(MODULE = "店铺设置管理", REMARK = "查询店铺列表")
    @ApiOperation("查询店铺列表")
    @PostMapping("/list")
    public Object list(ShopParam shopOrderParam){
        try{
            Page<EsShopNew> page = shopService.selectPageExt(shopOrderParam);
            return new CommonResult().success(page);
        }catch(Exception e){
            log.error("查询店铺列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "店铺设置管理", REMARK = "保存店铺")
    @ApiOperation("保存店铺")
    @PostMapping("/save")
    public Object savePaySettings(EsShopNew entity){
        try{
            //非空处理
            String columnS = "name,brandId,typeId,appid,appSecret,originalId";
            Object result = veryFieldEmpty(entity, columnS);
            if(result != null){
                return result;
            }
           /* EsShopBrand shopBrand = shopBrandService.getById(entity.getBrandId());
            if(ValidatorUtils.empty(shopBrand)){
                return new CommonResult().success("系统数据错误！");
            }*/
            //验证店铺名称唯一性
            if(null != shopService.getOne(new QueryWrapper<>(new EsShopNew()).eq("name",entity.getName())) ){
                return new CommonResult().failed("已存在{"+ entity.getName() +"}店铺名称，请重新添加");
            }
            entity.setBrandName(shopBrandService.getById(entity.getBrandId()).getBrandName());

            //表的基础字段 与 admin 配置冲突
//            entity.setCreateTime(new Date());
            entity.setCreateById(1l);//待完善
//            entity.setUpdateTime(new Date());
            entity.setUpdateById(1l);//待完善
            entity.setIsDelete(0);
            entity.setCreateDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            entity.setUpdateDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));

            if(shopService.save(entity)){
                return new CommonResult().success("添加成功！");
            }
        }catch(Exception e){
            log.error("保存店铺：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "店铺设置管理", REMARK = "删除店铺")
    @ApiOperation("删除店铺")
    @PostMapping("/delete")
    public Object deletePayment(@ApiParam("店铺id") @RequestParam Long id){
        try{
            //非空处理
            if(ValidatorUtils.empty(id)){
                return new CommonResult().success("店铺id不能为空");
            }
            if(this.shopService.deleteById(id)){
                return new CommonResult().success("删除成功！");
            }
        }catch(Exception e){
            log.error("删除店铺：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "店铺设置管理", REMARK = "修改店铺设置")
    @ApiOperation("修改店铺设置")
    @PostMapping("/update")
    public Object updatePayment(EsShopNew entity){
        try{
            //判断
            if(this.shopService.updateById(entity)){
                return new CommonResult().success("更新成功！");
            }
        }catch(Exception e){
            e.printStackTrace();
            log.error("修改店铺设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "店铺设置管理", REMARK = "查询品牌列表")
    @ApiOperation("查询品牌列表")
    @PostMapping("/getBrandList")
    public Object getBrandLisdt(){
        try{
            return new CommonResult().success(this.shopService.selectBrandList());
        }catch(Exception e){
            log.error("修改店铺设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "店铺设置管理", REMARK = "添加数据")
    @ApiOperation("添加数据")
    @PostMapping("/test/insert")
    public Object testSavePaySettings(@RequestBody EsShopNew shopNew){
        try{
            return new CommonResult().success(this.shopService.save(shopNew));
        }catch(Exception e){
            log.error("添加数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "店铺设置管理", REMARK = "根据id查询某个店铺")
    @ApiOperation("根据id查询某个店铺")
    @PostMapping("/selectById")
    public Object selectById(@ApiParam("店铺id") @RequestParam Long id){
        try{
            EsShopNew shop = shopService.getById(id);
            if(shop == null){
                return new CommonResult().success("没有此id的数据");
            }
            return new CommonResult().success(shop);
        }catch(Exception e){
            log.error("查询店铺列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    /**
     * 验证一些必须的字段
     * @param entity 实体
     * @param colmunS  已逗号(,)分隔开的字段
     * @return
     */
    private Object veryFieldEmpty(EsShopNew entity, String colmunS){
        try{
            String[] colmuns = colmunS.split(",");
            int len = colmuns.length;
            Class<? extends EsShopNew> clazz = entity.getClass();
            for(int i = 0; i < len; i++){
                String fieldS = colmuns[i];
                if(fieldS != null){
                    fieldS = fieldS.trim();
                    Field field = clazz.getDeclaredField(fieldS);
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if(ValidatorUtils.empty(value) && !value.equals(0)){
                        String annoValue = field.getDeclaredAnnotation(FieldText.class).value();
                        return new CommonResult().failed(annoValue == null ? fieldS : annoValue + " 不能为空");
                    }
                }

            }
        }catch (Exception e){
            log.error("判断非空错误：%s", e.getMessage(), e);
        }
        return null;
    }

    @SysLog(MODULE = "店铺设置管理", REMARK = "查询当前登录店铺的详情")
    @ApiOperation("查询当前登录店铺的详情")
    @PostMapping("/selShopDetail")
    public Object selShopDetail(@RequestParam("shopId")Long shopId){
        try{

            if(ValidatorUtils.empty(shopId)){
                return new CommonResult().failed("商铺编号为空");
            }
            EsShopNew esShopNew = new EsShopNew();
            esShopNew.setId(shopId);
            return new CommonResult().success("success",shopService.getOne(new QueryWrapper<>(esShopNew)));
        }catch(Exception e){
            log.error("查询当前登录店铺的详情异常：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }




}

