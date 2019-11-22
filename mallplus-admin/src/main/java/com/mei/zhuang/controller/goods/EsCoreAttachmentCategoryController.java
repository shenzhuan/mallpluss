package com.mei.zhuang.controller.goods;

import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.EsCoreAttachmentCategory;
import com.mei.zhuang.service.goods.EsCoreAttachmentCategoryService;
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

@Slf4j
@Api(value = "上传：图片、文件分组管理", description = "", tags = {"上传：图片、文件分组管理"})
@RestController
@RequestMapping("/api/attachment")
public class EsCoreAttachmentCategoryController {

    @Resource
    private EsCoreAttachmentCategoryService coreAttachmentCategoryService;

    @SysLog(MODULE = "上传：图片、文件分组管理", REMARK = "查询分组列表")
    @ApiOperation("查询分组列表")
    @PostMapping("/selPageList")
    public Object selPageList(EsCoreAttachmentCategory entity) {
        try {

            return new CommonResult().success("success", coreAttachmentCategoryService.selectLists(entity));
        } catch (Exception e) {
            log.error("查询上传文件分组异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "上传：图片、文件分组管理", REMARK = "新增分组列表")
    @ApiOperation("新增分组列表")
    @PostMapping("/save")
    public Object save(EsCoreAttachmentCategory entity) {
        try {
            if (ValidatorUtils.empty(entity.getName())) {
                return new CommonResult().failed("分组名称不得为空");
            }
            return new CommonResult().success("success", coreAttachmentCategoryService.save(entity));
        } catch (Exception e) {
            log.error("新增分组列表异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "上传：图片、文件分组管理", REMARK = "删除分组")
    @ApiOperation("删除分组")
    @PostMapping("/delete")
    public Object delete(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定编号");
            }
            if (id != null && id == 1) {
                return new CommonResult().failed("默认分组不得删除");
            }
            return new CommonResult().success("success", coreAttachmentCategoryService.removeById(id));
        } catch (Exception e) {
            log.error("删除分组异常：", e);
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

}
