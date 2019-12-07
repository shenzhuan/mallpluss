package com.zscat.mallplus.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsDiyPage;
import com.zscat.mallplus.sms.service.ISmsDiyPageService;
import com.zscat.mallplus.sms.service.ISmsDiypageTemplateCategoryService;
import com.zscat.mallplus.sys.service.impl.RedisUtil;
import com.zscat.mallplus.util.JsonUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 页面配置 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-10-17
 */
@Slf4j
@RestController
@RequestMapping("/sms/smsDiyPage")
public class SmsDiyPageController {
    @Resource
    private ISmsDiyPageService ISmsDiyPageService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private ISmsDiypageTemplateCategoryService categoryService;
    @Resource
    private RedisUtil redisRepository;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有页面配置列表")
    @ApiOperation("根据条件查询所有页面配置列表")
    @GetMapping(value = "/list")
    public Object getSmsDiyPageByPage(SmsDiyPage entity,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsDiyPageService.page(new Page<SmsDiyPage>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有页面配置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "添加自定义页面配置")
    @ApiOperation("添加自定义页面配置")
    @PostMapping(value = "/create")
    public Object saveBrand(@RequestBody SmsDiyPage entity) {
        try {

            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("请指定页面名称");
            }
            SmsDiyPage diypage = new SmsDiyPage();
            diypage.setName(entity.getName());
            SmsDiyPage es = ISmsDiyPageService.getOne(new QueryWrapper<>(diypage));
            if (es != null) {
                return new CommonResult().failed("名称重复");
            }
            String da = sdf.format(new Date());
            entity.setCreateTime(sdf.parse(da));
            ISmsDiyPageService.save(entity);
            if (entity.getType() == 2 && entity.getStatus() == 1) {
                redisRepository.set(String.format(Rediskey.EsShopDiypage, 12), JsonUtil.objectToJson(entity));
            }
            return new CommonResult().success("success");
        } catch (Exception e) {
            log.error("添加自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "删除自定义页面配置")
    @ApiOperation("删除自定义页面配置")
    @GetMapping(value = "/delete/{id}")
    public Object deleteSmsCoupon(@ApiParam("优惠卷表id") @PathVariable Long id) {
        try {
            boolean bool = false;
            //判断状态是否为禁用状态
            List<Long> idList = new ArrayList<>();
            SmsDiyPage div = ISmsDiyPageService.getById(id);
            if (div.getStatus() != 0) {
                return new CommonResult().failed("分类未禁用不得删除");
            }
            idList.add(id);
            bool = ISmsDiyPageService.removeByIds(idList);
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            log.error("删除自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "查询自定义页面配置明细")
    @ApiOperation("查询自定义页面配置明细")
    @GetMapping(value = "/detail")
    public Object detailBrand(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请选择对应页面配置项");
            }
            return new CommonResult().success("success", ISmsDiyPageService.getById(id));
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "sms", REMARK = "给优惠卷表分配优惠卷表")
    @ApiOperation("查询优惠卷表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:SmsCoupon:read')")
    public Object getSmsCouponById(@ApiParam("优惠卷表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请选择对应页面配置项");
            }
            return new CommonResult().success("success", ISmsDiyPageService.getById(id));
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "更改自定义页面配置信息")
    @ApiOperation("更改自定义页面配置信息")
    @PostMapping(value = "/update")
    public Object updateBrand(@RequestBody SmsDiyPage entity) {
        try {
            if (entity.getType() != null) {
                if (entity.getType() != 4) {
                    if (ValidatorUtils.empty(entity.getTitle())) {
                        return new CommonResult().failed("请指定页面标题");
                    }
                }
            }
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("请指定页面名称");
            }
            Integer es = ISmsDiyPageService.selectCounts(entity.getId(), entity.getName());
            if (es > 0) {
                return new CommonResult().failed("名称重复");
            }
            String time = sdf.format(new Date());
            entity.setUpdateTime(sdf.parse(time));
            ISmsDiyPageService.updateById(entity);
            if (entity.getType() == 2 && entity.getStatus() == 1) {
                redisRepository.set(String.format(Rediskey.EsShopDiypage, 12), JsonUtil.objectToJson(entity));
            }
            return new CommonResult().success("success", 1);
        } catch (Exception e) {
            log.error("更改自定义页面配置：%s", e.getMessage(), e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "查询页面")
    @ApiOperation("查询页面")
    @GetMapping("/selDiyPage")
    public Object selDiyPage(SmsDiyPage entity,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsDiyPageService.page(new Page<SmsDiyPage>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有页面配置列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "自定义页面查询商品详情页面", REMARK = "查询自定义模板详情页面")
    @ApiOperation("查询自定义模板详情页面")
    @GetMapping("/selDiyPageDetail")
    public Object selDiyPageDetail(SmsDiyPage entity,
                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            return new CommonResult().success(ISmsDiyPageService.page(new Page<SmsDiyPage>(pageNum, pageSize), new QueryWrapper<>(entity).like("name", entity.getName())));
        } catch (Exception e) {
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "查询类型")
    @ApiOperation("查询类型")
    @GetMapping("/selType")
    public Object selType(SmsDiyPage entity) {
        try {
            return new CommonResult().success("success", categoryService.selTemplateCategory());
        } catch (Exception e) {
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "修改状态")
    @ApiOperation("修改状态")
    @PostMapping("/updStatus")
    public Object updStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status, @RequestParam("typeId") Integer typeId) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("编号不得为空");
            }

            if (status == 1) {
                if (typeId == 4) {
                    Integer count = ISmsDiyPageService.selDiyPageTypeId(typeId, id);
                    if (count >= 10) {
                        return new CommonResult().failed("商品详情模版启用数量不得为10个");
                    }
                } else if (typeId == 3) {
                    Integer count = ISmsDiyPageService.selDiyPageTypeId(typeId, id);
                    if (count > 0) {
                        return new CommonResult().failed("会员模版启用数量只能为一个");
                    }
                } else if (typeId != 5) {
                    Integer count = ISmsDiyPageService.selDiyPageTypeId(typeId, id);
                    if (count > 0) {
                        return new CommonResult().failed(3, "类型模版存在已启用模版");
                    }
                }
            }


            List<SmsDiyPage> list = new ArrayList<SmsDiyPage>();
            SmsDiyPage page = new SmsDiyPage();
            page.setId(id);
            page.setStatus(status);
            page.setType(typeId);
            list.add(page);
            if (page.getType() == 2 && page.getStatus() == 1) {
                redisRepository.set(String.format(Rediskey.EsShopDiypage, 12), JsonUtil.objectToJson(ISmsDiyPageService.getById(id)));
            }
            return new CommonResult().success("success", ISmsDiyPageService.updateBatchById(list));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "根据类型查询所有页面")
    @ApiOperation("根据类型查询所有页面")
    @GetMapping("/selCustom")
    public Object selCustom(SmsDiyPage entity) {
        try {
            if (entity.getType() == 5) {
                entity.setStatus(1);
            }
            List<SmsDiyPage> list = ISmsDiyPageService.list(new QueryWrapper<>(entity));
            return new CommonResult().success("success", list);
        } catch (Exception e) {
            return new CommonResult().failed();
        }

    }

    @ApiOperation("修改展示状态")
    @RequestMapping(value = "/update/updateShowStatus")
    @ResponseBody
    @SysLog(MODULE = "cms", REMARK = "修改展示状态")
    public Object updateShowStatus(@RequestParam("ids") Long ids,
                                   @RequestParam("showStatus") Integer showStatus) {
        SmsDiyPage record = new SmsDiyPage();
        record.setStatus(showStatus);
        record.setId(ids);
        if (ISmsDiyPageService.updateById(record)) {
            return new CommonResult().success();
        } else {
            return new CommonResult().failed();
        }
    }
}
