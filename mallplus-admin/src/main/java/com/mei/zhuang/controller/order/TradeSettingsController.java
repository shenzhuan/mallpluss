package com.mei.zhuang.controller.order;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mei.zhuang.annotation.FieldText;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.order.EsShopPayment;
import com.mei.zhuang.service.order.ShopPaymentService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.order.PaySettingParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 9:46
 * @Description:
 */
@Api(value = "交易设置管理", description = "", tags = {"交易设置管理"})
@Slf4j
@RestController
@RequestMapping("/api/trade")
public class TradeSettingsController {

    private static final String PRIVATE_KEY = "ABCDEFGHIJK";
    private static final String PUBLIC_KEY = "AAADEFGHIJK";

    @Resource
    private ShopPaymentService shopPaymentService;

    @SysLog(MODULE = "交易设置管理", REMARK = "查询支付设置列表")
    @ApiOperation("查询支付设置列表")
    @PostMapping("/list")
    public Object list(PaySettingParam paySettingParam) {
        try {
            //非空处理
            return new CommonResult().success(shopPaymentService.page(new Page<>(paySettingParam.getCurrent(),paySettingParam.getSize()),new QueryWrapper<EsShopPayment>()));
        } catch (Exception e) {
            log.error("查询支付设置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "交易设置管理", REMARK = "保存支付设置")
    @ApiOperation("保存支付设置")
    @PostMapping("/save")
    public Object savePaySettings(EsShopPayment entity) {
//MultipartFile certFile, MultipartFile certKeyFile

        try {
            //非空处理
            //文件处理
            //File certKeyFile

//            System.out.println("文件名称" + certFile.getName());
            //
            //非空处理
            entity.setShopId(1l);
            String columnS = "key,status,type,mch_id,appid,title,shop_id,keyCert,cert,payType";
            Object result = veryFieldEmpty(entity, columnS);
            if (result != null) {
                return result;
            }
            EsShopPayment temp = new EsShopPayment();
            temp.setShopId(entity.getShopId());
            temp.setType(entity.getType());
            temp.setStatus(entity.getStatus());
            temp.setIsDelete(0);
            if (this.shopPaymentService.count(new QueryWrapper<>(temp)) > 1) {
                return new CommonResult().success("一个店铺只能存在同一类型的一种支付方式");
            }

          /*  String certFileToStr = new String(certFile.getBytes());
            String encrptCertFileToStr = "";
            String encrptCertKeyFileToStr = "";

            entity.setCert(encrptCertFileToStr);//  设置证书文件字节
            entity.setKeyCert(encrptCertKeyFileToStr);//  设置证书文件密钥字节*/
            entity.setPrivateKey(PRIVATE_KEY);
            entity.setPublicKey(PUBLIC_KEY);
            entity.setCreateTime(new Date());
            entity.setCreateById(1l);//待完善
            entity.setUpdateTime(new Date());
            entity.setUpdateById(1l);//待完善
            entity.setIsDelete(0);
            entity.setRemainStaus(0);//余额支付状态
            if (shopPaymentService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存支付设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "交易设置管理", REMARK = "删除支付设置")
    @ApiOperation("删除支付设置")
    @PostMapping("/delete")
    public Object deletePayment(@ApiParam("支付设置id") @RequestParam Long id) {
        try {
            //非空处理
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("参数支付设置id不能为空");
            }
            if (shopPaymentService.deleteById(id)) {
                return new CommonResult().success("成功删除id为：" + id);
            }
        } catch (Exception e) {
            log.error("删除支付设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "交易设置管理", REMARK = "修改支付设置")
    @ApiOperation("修改支付设置")
    @PostMapping("/update")
    public Object updatePayment(EsShopPayment entity) {
        try {

            EsShopPayment temp = new EsShopPayment();
            temp.setShopId(entity.getShopId());
            temp.setType(entity.getType());
            temp.setStatus(entity.getStatus());
            temp.setIsDelete(0);
            if (this.shopPaymentService.count(new QueryWrapper<>(temp)) > 1) {
                return new CommonResult().success("一个店铺只能存在同一类型的一种支付方式");
            }
            entity.setUpdateById(1l);//待完善
            entity.setUpdateTime(new Date());
            if (shopPaymentService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("修改支付设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "交易设置管理", REMARK = "添加数据")
    @ApiOperation("添加数据")
    @PostMapping("/test/insert")
    public Object testSavePaySettings(@RequestBody EsShopPayment entity) {
        try {
            //非空处理
//            String
            entity.setKey(new Date() + "AGgedgfatw144325v");
            entity.setCert(new Date() + "dsgkjdshweitrhjk");
            entity.setMchId(String.valueOf(IdWorker.getId()));
            entity.setAppid(String.valueOf(IdWorker.getId()));
            entity.setPrivateKey(PRIVATE_KEY);
            entity.setPublicKey(PUBLIC_KEY);
            entity.setCreateTime(new Date());
            entity.setCreateById(1l);//待完善
            entity.setUpdateTime(new Date());
            entity.setUpdateById(1l);//待完善
            entity.setRemainStaus(0);//余额支付状态
            if (shopPaymentService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("添加数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "交易设置管理", REMARK = "查询支付设置")
    @ApiOperation("查询支付设置")
    @PostMapping("/selectById")
    public Object selectById(@ApiParam("支付设置id") @RequestParam Long id) {
        try {
            //非空处理
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("参数支付设置id不能为空");
            }
            return new CommonResult().success(shopPaymentService.getById(id));
        } catch (Exception e) {
            log.error("查询支付设置：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "交易设置管理", REMARK = "测试上传文件")
    @ApiOperation("测试上传文件")
    @PostMapping("/test/upload")
    public Object savePaySettings(MultipartFile certFile) {
        try {
//            certFile.transferTo(temp);
            //会在项目中生成一个新文件
            String fileText = new String(certFile.getBytes(), "UTF-8");
            System.out.println(fileText);
            return new CommonResult().success();
        } catch (Exception e) {
            log.info("上传失败");
            e.printStackTrace();
        }
        return new CommonResult().failed();
    }

    /**
     * 验证一些必须的字段
     *
     * @param entity  实体  --->  T
     * @param colmunS 已逗号(,)分隔开的字段
     * @return
     */
    private Object veryFieldEmpty(EsShopPayment entity, String colmunS) {
        try {
            String[] colmuns = colmunS.split(",");
            int len = colmuns.length;
            Class<?> clazz = entity.getClass();
            for (int i = 0; i < len; i++) {
                String fieldS = colmuns[i];
                if (fieldS != null) {
                    fieldS = fieldS.trim();
                    Field field = clazz.getDeclaredField(fieldS);
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (ValidatorUtils.empty(value) && !value.equals(0)) {
                        String annoValue = field.getDeclaredAnnotation(FieldText.class).value();
                        return new CommonResult().failed(annoValue == null ? fieldS : annoValue + " 不能为空");
                    }
                }

            }
        } catch (Exception e) {
            log.error("判断非空错误：%s", e.getMessage(), e);
        }

        return null;
    }


}
