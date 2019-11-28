package com.zscat.mallplus.build.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.build.entity.BuildingFloor;
import com.zscat.mallplus.build.entity.BuildingRoom;
import com.zscat.mallplus.build.entity.BuildingUnit;
import com.zscat.mallplus.build.service.IBuildingFloorService;
import com.zscat.mallplus.build.service.IBuildingUnitService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 小区 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
@Slf4j
@RestController
@RequestMapping("/building/room")
public class BuildingRoomController {

    @Resource
    private com.zscat.mallplus.build.service.IBuildingRoomService IBuildingRoomService;

    @Resource
    private IBuildingUnitService unitService;
    @Resource
    private IBuildingFloorService floorService;
    @SysLog(MODULE = "cms", REMARK = "根据条件查询所有房间表列表")
    @ApiOperation("根据条件查询所有房间表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('building:room:read')")
    public Object getBuildingRoomByPage(BuildingRoom entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IBuildingRoomService.page(new Page<BuildingRoom>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有房间表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "保存房间表")
    @ApiOperation("保存房间表")
    @PostMapping(value = "/create")

    public Object saveBuildingRoom(@RequestBody BuildingRoom entity) {
        try {
            if (ValidatorUtils.empty(entity.getUnitId())){
                return new CommonResult().failed("请选择单元");
            }
            entity.setCreateTime(new Date());
            BuildingUnit unit = unitService.getById(entity.getUnitId());
            BuildingFloor floor = floorService.getById(unit.getFloorId());
            entity.setFloorId(floor.getId());entity.setCommunityId(floor.getCommunityId());
            if (IBuildingRoomService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存房间表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "更新房间表")
    @ApiOperation("更新房间表")
    @PostMapping(value = "/update/{id}")

    public Object updateBuildingRoom(@RequestBody BuildingRoom entity) {
        try {
            if (IBuildingRoomService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新房间表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "删除房间表")
    @ApiOperation("删除房间表")
    @GetMapping(value = "/delete/{id}")

    public Object deleteBuildingRoom(@ApiParam("房间表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("房间表id");
            }
            if (IBuildingRoomService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除房间表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "cms", REMARK = "给房间表分配房间表")
    @ApiOperation("查询房间表明细")
    @GetMapping(value = "/{id}")

    public Object getBuildingRoomById(@ApiParam("房间表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("房间表id");
            }
            BuildingRoom coupon = IBuildingRoomService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询房间表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除房间表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除房间表")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IBuildingRoomService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}



