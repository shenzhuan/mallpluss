package com.zscat.mallplus.b2c;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.oms.entity.OmsPayments;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.sms.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsHomeAdvertiseService;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.sys.entity.SysSchool;
import com.zscat.mallplus.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.ums.entity.*;
import com.zscat.mallplus.ums.service.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private IUserBankcardsService bankcardsService;
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

    @SysLog(MODULE = "cms", REMARK = "判断是否签到")
    @ApiOperation(value = "判断是否签到")
    @PostMapping(value = "/user.issign")
    public Object issign(CmsSubject subject, BindingResult result) {
        CommonResult commonResult;
        UmsMember member = UserUtils.getCurrentMember();
        return new CommonResult().success();

    }

    @SysLog(MODULE = "cms", REMARK = "签到接口")
    @ApiOperation(value = "签到接口")
    @PostMapping(value = "/user.sign")
    public Object sign(CmsSubject subject, BindingResult result) {
        CommonResult commonResult;
        UmsMember member = UserUtils.getCurrentMember();

        return new CommonResult().success();
    }



    @ApiOperation("添加银行卡")
    @RequestMapping(value = "/user.addbankcard")
    @ResponseBody
    public Object saveusership(UserBankcards address) {
        boolean count = false;
        UmsMember umsMember =  UserUtils.getCurrentMember();
        if (address.getIsDefault()==1){
            UserBankcards query =new UserBankcards();
            query.setIsDefault(2);
            bankcardsService.update(query,new QueryWrapper<UserBankcards>().eq("user_id",umsMember.getId()));
        }
        address.setUserId(umsMember.getId());
        if (address != null && address.getId() != null) {
            count = bankcardsService.updateById(address);
        } else {
            count = bankcardsService.save(address);
        }
        if (count) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @IgnoreAuth
    @ApiOperation("获取我的银行卡列表")
    @RequestMapping(value = "/user.getbankcardlist", method = RequestMethod.POST)
    @ResponseBody
    public Object getbankcardlist() {
        UmsMember umsMember = UserUtils.getCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            List<UserBankcards> addressList = bankcardsService.list(new QueryWrapper<UserBankcards>().eq("user_id",umsMember.getId()));
            return new CommonResult().success(addressList);
        }
        return new ArrayList<UmsMemberReceiveAddress>();
    }



    @IgnoreAuth
    @ApiOperation("获取银行卡信息")
    @RequestMapping(value = "/user.getbankcardinfo", method = RequestMethod.POST)
    @ResponseBody
    public Object getbankcardinfo(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        return new CommonResult().success(bankcardsService.getById(id));
    }
    @IgnoreAuth
    @ApiOperation("获取默认的银行卡")
    @RequestMapping(value = "/user.getdefaultbankcard", method = RequestMethod.POST)
    @ResponseBody
    public Object getItemDefautl() {
        UserBankcards address = bankcardsService.getOne(new QueryWrapper<UserBankcards>().eq("user_id",UserUtils.getCurrentMember().getId()).eq("is_default",1));
        return new CommonResult().success(address);
    }


    /**
     * @param id
     * @return
     */
    @ApiOperation("设置默认银行卡")
    @RequestMapping(value = "/user.setdefaultbankcard")
    @ResponseBody
    public Object setDefault(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }



    @ApiOperation("用户推荐列表")
    @PostMapping(value = "/user.recommend")
    @ResponseBody
    public Object distrecommendList(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("邀请码")
    @PostMapping(value = "/user.sharecode")
    @ResponseBody
    public Object shareCode(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("获取分销商进度状态")
    @PostMapping(value = "/distribution_center-api-info")
    @ResponseBody
    public Object getDistributioninfo(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("申请分销商")
    @PostMapping(value = "/distribution_center-api-applydistribution")
    @ResponseBody
    public Object applyDistribution(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }
    @ApiOperation("店铺设置")
    @PostMapping(value = "/distribution_center-api-setstore")
    @ResponseBody
    public Object setStore(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("我的分销订单")
    @PostMapping(value = "/distribution_center-api-getstoreinfo")
    @ResponseBody
    public Object distribution_centerApiGetstoreinfo(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }
    @ApiOperation("我的分销订单")
    @PostMapping(value = "/distribution_center-api-myorder")
    @ResponseBody
    public Object getDistributionOrder(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        int count = bankcardsService.setDefault(id);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }
}
