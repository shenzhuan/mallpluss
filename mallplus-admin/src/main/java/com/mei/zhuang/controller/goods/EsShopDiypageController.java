package com.mei.zhuang.controller.goods;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.constant.RedisConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import com.mei.zhuang.service.goods.EsShopDiypageService;
import com.mei.zhuang.service.goods.EsShopDiypageTemplateCategoryService;
import com.mei.zhuang.service.member.impl.RedisUtil;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 10:54
 * @Description:
 */
@Slf4j
@Api(value = "自定义页面配置", description = "", tags = {"自定义页面配置"})
@RestController
@RequestMapping("/shop/diypage")
public class EsShopDiypageController {
    @Resource
    private EsShopDiypageService diypageService;
    @Resource
    private EsShopDiypageTemplateCategoryService categoryService;
    @Resource
    private RedisUtil redisRepository;
   // @Resource
  //  private RedisUtil redisRepository;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SysLog(MODULE = "自定义页面配置", REMARK = "添加自定义页面配置")
    @ApiOperation("添加自定义页面配置")
    @PostMapping(value = "/saves")
    public Object saveBrand(EsShopDiypage entity) {
        try {
            if (entity.getType() != 4) {
                if (ValidatorUtils.empty(entity.getTitle())) {
                    return new CommonResult().failed("请指定页面标题");
                }
            }

            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("请指定页面名称");
            }
            EsShopDiypage diypage = new EsShopDiypage();
            diypage.setName(entity.getName());
            EsShopDiypage es = diypageService.getOne(new QueryWrapper<>(diypage));
            if (es != null) {
                return new CommonResult().failed("名称重复");
            }
            String da = sdf.format(new Date());
            entity.setCreateTime(sdf.parse(da));
            diypageService.save(entity);
            if (entity.getType() == 2 && entity.getStatus() == 1) {
                redisRepository.set(String.format(RedisConstant.EsShopDiypage, 12), entity);
            }
            return new CommonResult().success("success");
        } catch (Exception e) {
            log.error("添加自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "删除自定义页面配置")
    @ApiOperation("删除自定义页面配置")
    @PostMapping(value = "/delete")
    public Object deleteBrand(@RequestParam("id") Long id) {
        try {
            boolean bool = false;
            //判断状态是否为禁用状态
            List<Long> idList = new ArrayList<>();
            EsShopDiypage div = diypageService.getById(id);
            if (div.getStatus() != 0) {
                return new CommonResult().failed("分类未禁用不得删除");
            }
            idList.add(id);
            bool = diypageService.removeByIds(idList);
            return new CommonResult().success("success", bool);
        } catch (Exception e) {
            log.error("删除自定义页面配置：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "查询自定义页面配置明细")
    @ApiOperation("查询自定义页面配置明细")
    @PostMapping(value = "/detail")
    public Object detailBrand(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请选择对应页面配置项");
            }
            return new CommonResult().success("success", diypageService.getById(id));
        } catch (Exception e) {
            log.error("查询明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "更改自定义页面配置信息")
    @ApiOperation("更改自定义页面配置信息")
    @PostMapping(value = "/update")
    public Object updateBrand(EsShopDiypage entity) {
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
            Integer es = diypageService.selectCounts(entity.getId(), entity.getName());
            if (es > 0) {
                return new CommonResult().failed("名称重复");
            }
            String time = sdf.format(new Date());
            entity.setLasteditTime(sdf.parse(time));
            diypageService.updateById(entity);
            if (entity.getType() == 2 && entity.getStatus() == 1) {
                redisRepository.set(String.format(RedisConstant.EsShopDiypage, 12), entity);
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
    @PostMapping("/selDiyPage")
    public Object selDiyPage(EsShopDiypage entity) {
        try {
            if (ValidatorUtils.empty(entity.getType())) {
                return new CommonResult().failed("请指定页面类型");
            }
            PageHelper.startPage(entity.getCurrent(), entity.getSize());

            return new CommonResult().success(PageInfo.of(diypageService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面查询商品详情页面", REMARK = "查询自定义模板详情页面")
    @ApiOperation("查询自定义模板详情页面")
    @PostMapping("/selDiyPageDetail")
    public Object selDiyPageDetail(EsShopDiypage entity) {
        try {
            entity.setType(4);
            PageHelper.startPage(entity.getCurrent(), entity.getSize());

            return new CommonResult().success(PageInfo.of(diypageService.list(new QueryWrapper<>(entity))));
        } catch (Exception e) {
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "查询类型")
    @ApiOperation("查询类型")
    @PostMapping("/selType")
    public Object selType(EsShopDiypage entity) {
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
                    Integer count = diypageService.selDiyPageTypeId(typeId, id);
                    if (count >= 10) {
                        return new CommonResult().failed("商品详情模版启用数量不得为10个");
                    }
                } else if (typeId == 3) {
                    Integer count = diypageService.selDiyPageTypeId(typeId, id);
                    if (count > 0) {
                        return new CommonResult().failed("会员模版启用数量只能为一个");
                    }
                } else if (typeId != 5) {
                    Integer count = diypageService.selDiyPageTypeId(typeId, id);
                    if (count > 0) {
                        return new CommonResult().failed(3, "类型模版存在已启用模版");
                    }
                }
            }


            List<EsShopDiypage> list = new ArrayList<EsShopDiypage>();
            EsShopDiypage page = new EsShopDiypage();
            page.setId(id);
            page.setStatus(status);
            page.setType(typeId);
            list.add(page);
            if (page.getType() == 2 && page.getStatus() == 1) {
                redisRepository.set(String.format(RedisConstant.EsShopDiypage, 12), diypageService.getById(id));
            }
            return new CommonResult().success("success", diypageService.updateBatchById(list));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }

    @SysLog(MODULE = "自定义页面配置", REMARK = "根据类型查询所有页面")
    @ApiOperation("根据类型查询所有页面")
    @PostMapping("/selCustom")
    public Object selCustom(EsShopDiypage entity) {
        try {
            if (entity.getType() == 5) {
                entity.setStatus(1);
            }
            List<EsShopDiypage> list = diypageService.list(new QueryWrapper<>(entity));
            return new CommonResult().success("success", list);
        } catch (Exception e) {
            return new CommonResult().failed();
        }

    }

}
