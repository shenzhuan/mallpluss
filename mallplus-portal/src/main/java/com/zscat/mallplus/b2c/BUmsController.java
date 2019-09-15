package com.zscat.mallplus.b2c;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.sms.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.sys.entity.SysSchool;
import com.zscat.mallplus.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberBlanceLog;
import com.zscat.mallplus.ums.service.IUmsIntegrationChangeHistoryService;
import com.zscat.mallplus.ums.service.IUmsMemberBlanceLogService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.home.Pages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 首页内容管理Controller
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
@Slf4j
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
public class BUmsController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Resource
    private RedisService redisService;
    @Autowired
    private IUmsMemberService memberService;
    @Autowired
    private ISmsHomeAdvertiseService advertiseService;
    @Autowired
    private IOmsOrderService orderService;
    @Resource
    private ISmsCouponService couponService;

    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Resource
    private IUmsMemberBlanceLogService blanceLogService;
    @Resource
    private IUmsIntegrationChangeHistoryService integrationChangeHistoryService;
    @Resource
    private SysAreaMapper areaMapper;

    @ApiOperation("更新会员信息")
    @SysLog(MODULE = "ums", REMARK = "更新会员信息")
    @PostMapping(value = "/user.editinfo")
    public Object updateMember(UmsMember member) {
        if (member==null){
            return new CommonResult().paramFailed();
        }
        UmsMember member1 = UserUtils.getCurrentMember();
        if(member1!=null&& member1.getId()!=null){
            member.setId(member1.getId());
            return new CommonResult().success(memberService.updateById(member));
        }
        return new CommonResult().failed();
    }

    @IgnoreAuth
    @ApiOperation(value = "查询学校列表")
    @PostMapping(value = "/user.balancelist")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object schoolList(UmsMemberBlanceLog entity,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        entity.setMemberId(UserUtils.getCurrentMember().getId());
        return new CommonResult().success(blanceLogService.page(new Page<UmsMemberBlanceLog>(pageNum, pageSize), new QueryWrapper<>(entity)));
    }

    @IgnoreAuth
    @ApiOperation(value = "查询学校列表")
    @PostMapping(value = "/user.userpointlog")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object userpointlog(UmsIntegrationChangeHistory entity,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        Map<String,Object> map = new HashedMap();
        map.put("member",memberService.getById(UserUtils.getCurrentMember().getId()));
        entity.setMemberId(UserUtils.getCurrentMember().getId());
        map.put("intList",integrationChangeHistoryService.page(new Page<UmsIntegrationChangeHistory>(pageNum, pageSize), new QueryWrapper<>(entity)));
        return new CommonResult().success(map);
    }

    @IgnoreAuth
    @ApiOperation(value = "查询学校列表")
    @PostMapping(value = "/user.getareaid")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object getareaid(SysArea entity) {
        return new CommonResult().success(areaMapper.selectOne(new QueryWrapper<>(entity)));
    }
    @IgnoreAuth
    @ApiOperation(value = "查询学校列表")
    @PostMapping(value = "/user.getarealist")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object getarealist() throws Exception {
        String json = redisService.get("areaList");
        if (ValidatorUtils.notEmpty(json)){
            log.info("redis----areaList");
            return   new CommonResult().success(JsonUtils.json2list(json,SysArea.class));
        }
        List<SysArea>  list =  areaMapper.selectList(new QueryWrapper<SysArea>().eq("deep",0));
        List<SysArea>  onelist =  areaMapper.selectList(new QueryWrapper<SysArea>().eq("deep",1));
        List<SysArea>  twolist =  areaMapper.selectList(new QueryWrapper<SysArea>().eq("deep",2));
        List<SysArea>  threelist =  areaMapper.selectList(new QueryWrapper<SysArea>().eq("deep",3));
        for (SysArea area: list){
            for (SysArea one : onelist){
                if (area.getId().equals(one.getPid())){
                    area.getChildren().add(one);
                }
                for (SysArea two : twolist){
                    if (one.getId().equals(two.getPid())){
                        one.getChildren().add(one);
                    }
                    for (SysArea three : threelist){
                        if (two.getId().equals(three.getPid())){
                            two.getChildren().add(one);
                        }
                    }
                }
            }
        }
        redisService.set("areaList",JsonUtils.objectToJson(list));
        return new CommonResult().success(list);
    }
}
