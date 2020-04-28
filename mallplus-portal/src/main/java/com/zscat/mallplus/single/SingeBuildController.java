package com.zscat.mallplus.single;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.build.BuildHomeResult;
import com.zscat.mallplus.build.entity.*;
import com.zscat.mallplus.build.service.IBuildRepairService;
import com.zscat.mallplus.build.service.IBuildingCommunityService;
import com.zscat.mallplus.build.service.IBuildingOwnerService;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@Slf4j
@RestController
@Api(tags = "SingeBuildController", description = "物业管理")
@RequestMapping("/api/single/build")
public class SingeBuildController extends ApiBaseAction {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private com.zscat.mallplus.build.service.IBuildGroupService IBuildGroupService;
    @Resource
    private com.zscat.mallplus.build.service.IBuildWuyeCompanyService IBuildWuyeCompanyService;
    @Resource
    private RedisService redisService;
    @Resource
    private IPmsProductService pmsProductService;
    @Resource
    private com.zscat.mallplus.build.service.IBuildNoticeService IBuildNoticeService;
    @Autowired
    private IUmsMemberService memberService;

    @Resource
    private IBuildingCommunityService communityService;

    @Resource
    private com.zscat.mallplus.build.service.IBuildingFloorService IBuildingFloorService;
    @Resource
    private IBuildingOwnerService ownerService;
    @Resource
    private IBuildRepairService repairService;


    @SysLog(MODULE = "pms", REMARK = "所有社区和房间")
    @IgnoreAuth
    @GetMapping(value = "/allCommunity")
    @ApiOperation(value = "所有社区和房间")
    public Object allCommunity() {
        return new CommonResult().success(communityService.allCommunity());
    }

    @SysLog(MODULE = "pms", REMARK = "绑定小区和房间")
    @IgnoreAuth
    @PostMapping(value = "/bindCommunity")
    @ApiOperation(value = "绑定小区和房间")
    public Object bindCommunity(@RequestParam(value = "selectedMore2", required = false, defaultValue = "0") String selectedMore2) {

        try {
            return communityService.bindCommunity(selectedMore2);
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }
    }

    @IgnoreAuth
    @ApiOperation("首页内容页信息展示")
    @SysLog(MODULE = "home", REMARK = "首页内容页信息展示")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public Object home_mobile(@RequestParam(value = "communityId", required = false) Long communityId) {
        String key = Rediskey.HOMEPAGEMOBILE;
        String json = redisService.get(key);
        BuildHomeResult contentResult = null;
        try {
            if (ValidatorUtils.empty(json)) {
                contentResult = communityService.singleContent(communityId);
                redisService.set(key, JsonUtils.objectToJson(contentResult));
                redisService.expire(key, 30);
            } else {
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), BuildHomeResult.class);
            }
        } catch (Exception e) {
            contentResult = communityService.singleContent(communityId);
            redisService.set(key, JsonUtils.objectToJson(contentResult));
            redisService.expire(key, 30);
        }
        return new CommonResult().success(contentResult);
    }

    @ApiOperation("根据条件查询所有楼房表列表")
    @GetMapping(value = "/communityList")
    public Object getBuildingCommunityByPage(BuildingCommunity entity,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return new CommonResult().success(communityService.page(new Page<BuildingCommunity>(pageNum, pageSize), new QueryWrapper<>(entity).select(ConstansValue.sampleCommunityList)));

    }

    @ApiOperation("根据条件查询所有楼房表列表")
    @GetMapping(value = "/floorList")
    public Object getBuildingFloorByPage(BuildingFloor entity,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            Long communityId = entity.getCommunityId();
            if (ValidatorUtils.notEmpty(communityId) && communityId > 0) {

            } else {
                UmsMember member = memberService.getNewCurrentMember();
                if (member == null) {
                    throw new ApiMallPlusException("100");
                }
                String[] split = member.getRoomNums().split(",");
                communityId = Long.valueOf(split[0]);
                entity.setCommunityId(communityId);
            }
            return new CommonResult().success(IBuildingFloorService.page(new Page<BuildingFloor>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有楼房表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    /**
     * @param entity
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getBuildNoticeByPage")
    public Object getBuildNoticeByPage(BuildNotice entity,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IBuildNoticeService.page(new Page<BuildNotice>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有公告表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    /**
     * @param entity
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getBuildWuyeCompanyByPage")
    public Object getBuildWuyeCompanyByPage(BuildWuyeCompany entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IBuildWuyeCompanyService.page(new Page<BuildWuyeCompany>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有物业公司表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    /**
     * @param entity
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getBuildingOwnerByPage")
    public Object getBuildWuyeCompanyByPage(BuildingOwner entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ownerService.page(new Page<BuildingOwner>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有物业公司表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "pms", REMARK = "查询团购商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询带团购商品列表")
    @GetMapping(value = "/groupList")
    public Object groupList(
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        List<BuildGroup> groupList = IBuildGroupService.list(new QueryWrapper<>());
        List<BuildGroup> result = new ArrayList<>();
        for (BuildGroup group : groupList) {

            Long nowT = System.currentTimeMillis();
            Date endTime = group.getEndTime();
            if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
                PmsProduct g = pmsProductService.getById(group.getGoodsId());
                if (g != null) {
                    group.setGoods(g);
                    result.add(group);
                }
            }
        }
        return new CommonResult().success(result);
    }

    /**
     * 根据经纬度获取商铺列表
     *
     * @param geoHash  40.0844020000,116.3483150000
     * @param distance
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/near/communityList", method = RequestMethod.GET)
    public Object restaurants(@RequestParam("geohash") String geoHash
            , @RequestParam(value = "distance", defaultValue = "10") Integer distance,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String[] geoHashArr = geoHash.split(",");
        String longitude = geoHashArr[1];
        String latitude = geoHashArr[0];
        List<BuildingCommunity> storeList = communityService.selectNearCommunity(distance, Double.valueOf(latitude), Double.valueOf(longitude), pageSize);
        return new CommonResult().success(storeList);
    }


    /**
     * @return
     */
    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public Object restaurants() {
        List<BuildWuyeCompany> companyList = IBuildWuyeCompanyService.list(new QueryWrapper<>());
        for (BuildWuyeCompany company : companyList) {
            BuildingCommunity community = new BuildingCommunity();
            community.setCreateTime(new Date());
            community.setCompanyId(company.getId());
            communityService.update(community, new QueryWrapper<BuildingCommunity>().eq("wuyecompany", company.getName()));
        }
        return new CommonResult().success();
    }

}
