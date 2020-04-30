package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.service.ISysAreaService;
import com.zscat.mallplus.cms.service.ISysSchoolService;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.fenxiao.entity.FenxiaoRecords;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoRecordsMapper;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.pms.entity.PmsFavorite;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.pms.mapper.PmsProductAttributeCategoryMapper;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.pms.service.IPmsFavoriteService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.sys.entity.SysSchool;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.ums.entity.UmsEmployInfo;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberLevel;
import com.zscat.mallplus.ums.entity.UmsMemberTag;
import com.zscat.mallplus.ums.mapper.UmsEmployInfoMapper;
import com.zscat.mallplus.ums.mapper.UmsRewardLogMapper;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@RestController
@Api(tags = "UmsController", description = "会员关系管理")
@RequestMapping("/api/single/user")
public class SingeUmsController extends ApiBaseAction {

    @Resource
    FenxiaoRecordsMapper fenxiaoRecordsMapper;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ISysSchoolService schoolService;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private ISysAreaService areaService;
    @Resource
    private IUmsMemberMemberTagRelationService memberTagService;
    @Resource
    private UmsRewardLogMapper rewardLogMapper;
    @Resource
    private UmsEmployInfoMapper employInfoMapper;
    @Resource
    private SysStoreMapper storeMapper;
    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private IPmsProductService pmsProductService;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private IPmsFavoriteService favoriteService;
    @Resource
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Resource
    private IOmsOrderService orderService;

    @Resource
    private IUmsMemberTagService IUmsMemberTagService;

    @ApiOperation("获取会员详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        UmsMember member = memberService.getById(id);
        return new CommonResult().success(member);
    }

    @ApiOperation("获取会员详情")
    @RequestMapping(value = "/currentMember", method = RequestMethod.GET)
    @ResponseBody
    public Object currentMember() {
        UmsMember member = memberService.getById(memberService.getNewCurrentMember().getId());
        return new CommonResult().success(member);
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = "/updateMember", method = RequestMethod.POST)
    public Object updatePassword(
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "icon", required = false) String icon,
            @RequestParam(value = "invitecode", required = false) String invitecode,
            @RequestParam(value = "gender", required = false) Integer gender
    ) {
        UmsMember m = new UmsMember();
        m.setId(memberService.getNewCurrentMember().getId());
        if (ValidatorUtils.notEmpty(nickname)) {
            m.setNickname(nickname);
        }
        if (ValidatorUtils.notEmpty(icon)) {
            m.setIcon(icon);
        }
        if (ValidatorUtils.notEmpty(gender)) {
            m.setGender(gender);
        }
        if (ValidatorUtils.notEmpty(invitecode)) {
            if (memberService.getById(invitecode) != null) {
                m.setInvitecode(invitecode);
            } else {
                return new CommonResult().failed();
            }
        }
        return memberService.updateById(m);
    }

    @IgnoreAuth
    @ApiOperation("重置密码")
    @PostMapping(value = "/resetPassword")
    public Object resetPassword(@RequestParam String phone,
                                @RequestParam String password,
                                @RequestParam String confimpassword,
                                @RequestParam String authCode) {
        if (phone == null || "".equals(phone)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (password == null || "".equals(password)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        if (confimpassword == null || "".equals(confimpassword)) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        return memberService.resetPassword(phone, password, confimpassword, authCode);
    }

    @IgnoreAuth
    @ApiOperation(value = "查询标签列表")
    @GetMapping(value = "/memberTag/list")
    @SysLog(MODULE = "ums", REMARK = "查询标签列表")
    public Object storeList(UmsMemberTag entity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(IUmsMemberTagService.page(new Page<UmsMemberTag>(pageNum, pageSize), new QueryWrapper<>(entity)));
    }

    @IgnoreAuth
    @ApiOperation(value = "查询商铺列表")
    @GetMapping(value = "/store/list")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object storeList(SysStore entity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(storeMapper.selectList(new QueryWrapper<SysStore>(entity)));
    }

    @IgnoreAuth
    @ApiOperation(value = "查询会员等级列表")
    @GetMapping(value = "/memberLevel/list")
    @SysLog(MODULE = "ums", REMARK = "查询会员等级列表")
    public Object memberLevelList(UmsMemberLevel entity,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(memberLevelService.list(new QueryWrapper<UmsMemberLevel>(entity)));
    }


    /**
     * 会员等级升级
     */
    @SysLog(MODULE = "pay", REMARK = "会员等级升级")
    @ApiOperation(value = "会员等级升级")
    @PostMapping("applyMember")
    public Object applyMember(@RequestParam(value = "memberLevelId", required = false, defaultValue = "0") Long memberLevelId) {
        try {
            return new CommonResult().success(orderService.applyMember(memberLevelId));
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }
    }

    @ApiOperation("获取商铺详情")
    @RequestMapping(value = "/storeDetail", method = RequestMethod.GET)
    @ResponseBody
    public Object storeDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SysStore store = storeMapper.selectById(id);
        List<PmsProductAttributeCategory> list = productAttributeCategoryMapper.selectList(new QueryWrapper<PmsProductAttributeCategory>().eq("store_id", id));
        for (PmsProductAttributeCategory gt : list) {
            PmsProduct productQueryParam = new PmsProduct();
            productQueryParam.setProductAttributeCategoryId(gt.getId());
            productQueryParam.setPublishStatus(1);
            productQueryParam.setDeleteStatus(1);
            productQueryParam.setVerifyStatus(1);
            IPage<PmsProduct> goodsList = pmsProductService.page(new Page<PmsProduct>(0, 8), new QueryWrapper<>(productQueryParam).select(ConstansValue.sampleGoodsList));
            if (goodsList != null && goodsList.getRecords() != null && goodsList.getRecords().size() > 0) {
                gt.setGoodsList(goodsList.getRecords());
            } else {
                gt.setGoodsList(new ArrayList<>());
            }
        }
        store.setList(list);
        store.setGoodsCount(pmsProductService.count(new QueryWrapper<PmsProduct>().eq("store_id", id)));
        //记录浏览量到redis,然后定时更新到数据库
        String key = Rediskey.STORE_VIEWCOUNT_CODE + id;
        //找到redis中该篇文章的点赞数，如果不存在则向redis中添加一条
        Map<Object, Object> viewCountItem = redisUtil.hGetAll(Rediskey.STORE_VIEWCOUNT_KEY);
        Integer viewCount = 0;
        if (!viewCountItem.isEmpty()) {
            if (viewCountItem.containsKey(key)) {
                viewCount = Integer.parseInt(viewCountItem.get(key).toString()) + 1;
                redisUtil.hPut(Rediskey.STORE_VIEWCOUNT_KEY, key, viewCount + "");
            } else {
                viewCount = 1;
                redisUtil.hPut(Rediskey.STORE_VIEWCOUNT_KEY, key, 1 + "");
            }
        } else {
            redisUtil.hPut(Rediskey.STORE_VIEWCOUNT_KEY, key, 1 + "");
        }
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {

            PmsFavorite query = new PmsFavorite();
            query.setObjId(id);
            query.setMemberId(umsMember.getId());
            query.setType(3);
            PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
            if (findCollection != null) {
                map.put("favorite", true);
            } else {
                map.put("favorite", false);
            }
        }
        store.setHit(viewCount);
        map.put("store", store);
        return new CommonResult().success(map);
    }

    @IgnoreAuth
    @ApiOperation(value = "查询学校列表")
    @GetMapping(value = "/school/list")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object schoolList(SysSchool entity,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(schoolService.page(new Page<SysSchool>(pageNum, pageSize), new QueryWrapper<>(entity)));
    }

    @ApiOperation("获取学校详情")
    @RequestMapping(value = "/schoolDetail", method = RequestMethod.GET)
    @ResponseBody
    public Object schoolDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SysSchool school = schoolService.getById(id);
        List<PmsProduct> list = productMapper.selectList(new QueryWrapper<PmsProduct>().eq("school_id", id).select(ConstansValue.sampleGoodsList));
        school.setGoodsList(list);
        school.setGoodsCount(list.size());
        return new CommonResult().success(school);
    }

    @IgnoreAuth
    @SysLog(MODULE = "ums", REMARK = "根据pid查询区域")
    @ApiOperation("根据pid查询区域")
    @RequestMapping(value = "/getAreaByPid", method = RequestMethod.GET)
    public Object getAreaByPid(@RequestParam(value = "pid", required = false, defaultValue = "0") Long pid) {
        SysArea queryPid = new SysArea();
        queryPid.setPid(pid);
        List<SysArea> list = areaService.list(new QueryWrapper<SysArea>(queryPid));
        return new CommonResult().success(list);
    }

    @IgnoreAuth
    @SysLog(MODULE = "ums", REMARK = "邀请好友数据")
    @ApiOperation("邀请好友数据")
    @RequestMapping(value = "/getInviteData", method = RequestMethod.GET)
    public Object getInviteData() {
        UmsMember member = memberService.getNewCurrentMember();
        UmsMember newMember = memberService.getById(member.getId());
        Integer count = memberService.count(new QueryWrapper<UmsMember>().eq("invitecode", member.getId()));
        newMember.setBuyCount(count);
        List<FenxiaoRecords> recordss = fenxiaoRecordsMapper.selectList(new QueryWrapper<FenxiaoRecords>().eq("member_id", member.getId()));
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (FenxiaoRecords fenxiaoRecords : recordss) {
            totalMoney = fenxiaoRecords.getMoney().add(totalMoney);
        }
        newMember.setBuyMoney(totalMoney);
        return new CommonResult().success(newMember);
    }

    @IgnoreAuth
    @ApiOperation(value = "邀请好友列表")
    @GetMapping(value = "/inviteUser")
    @SysLog(MODULE = "ums", REMARK = "邀请好友列表")
    public Object inviteUser(UmsMember entity,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(memberService.list(new QueryWrapper<UmsMember>().eq("invitecode", memberService.getNewCurrentMember().getId())));
    }

    @IgnoreAuth
    @ApiOperation(value = "邀请好友佣金列表")
    @GetMapping(value = "/inviteMoney")
    @SysLog(MODULE = "ums", REMARK = "邀请好友佣金列表")
    public Object inviteMoney(FenxiaoRecords entity,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        return new CommonResult().success(fenxiaoRecordsMapper.selectList(new QueryWrapper<FenxiaoRecords>().eq("member_id", memberService.getNewCurrentMember().getId())));
    }

    @ApiOperation("添加招聘")
    @SysLog(MODULE = "ums", REMARK = "添加招聘")
    @PostMapping(value = "/addJob")
    public Object addJob(UmsEmployInfo member) {
        return employInfoMapper.insert(member);
    }

    @ApiOperation(value = "会员绑定学校")
    @PostMapping(value = "/bindSchool")
    @SysLog(MODULE = "ums", REMARK = "会员绑定学校")
    public Object bindSchool(@RequestParam(value = "schoolId", required = true) Long schoolId) {
        try {
            UmsMember member = memberService.getNewCurrentMember();

            String countKey = "bindSchool:count:" + ":" + member.getId();
            String value = redisService.get(countKey);
            if (value != null) {
                Integer count = Integer.valueOf(value);
                if (count > 100) {
                    return new CommonResult().success("已超过当天最大次数");
                }
            }
            SysSchool area = schoolService.getById(schoolId);
            if (area == null) {
                return new CommonResult().failed("学校不存在");
            }
            member.setSchoolName(area.getName());
            member.setSchoolId(schoolId);
            memberService.updateById(member);
            // 当天发送验证码次数+1
            redisService.increment(countKey, 1L);
            redisService.expire(countKey, 1 * 3600 * 24 * 365);
            return new CommonResult().success("绑定学校成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("绑定学校失败");
        }
    }

    @ApiOperation(value = "会员绑定区域")
    @PostMapping(value = "/bindArea")
    @SysLog(MODULE = "ums", REMARK = "会员绑定区域")
    public Object bindArea(@RequestParam(value = "areaId", required = true) Long areaId) {
        try {
            UmsMember member = memberService.getNewCurrentMember();
            String countKey = "bindArea:count:" + ":" + member.getId();
            String value = redisService.get(countKey);
            if (value != null) {
                Integer count = Integer.valueOf(value);
                if (count > 100) {
                    return new CommonResult().success("已超过当天最大次数");
                }
            }

            SysArea area = areaService.getById(areaId);
            if (area == null) {
                return new CommonResult().failed("区域不存在");
            }
            member.setAreaId(areaId);
            member.setAreaName(area.getName());
            memberService.updateById(member);
            // 当天发送验证码次数+1
            redisService.increment(countKey, 1L);
            redisService.expire(countKey, 1 * 3600 * 24 * 365);
            return new CommonResult().success(area);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("绑定区域失败");
        }
    }



    /*@ApiOperation(value = "会员绑定区域")
    @PostMapping(value = "/bindArea")
    @SysLog(MODULE = "ums", REMARK = "会员绑定区域")
    public Object bindArea(@RequestParam(value = "areaIds", required = true) String areaIds) {
        try {
            if (ValidatorUtils.empty(areaIds)) {
                return new CommonResult().failed("请选择区域");
            }
            UmsMember member = memberService.getNewCurrentMember();
            String[] areIdList = areaIds.split(",");
            List<UmsMemberMemberTagRelation> list = new ArrayList<>();
            for (String id : areIdList) {
                UmsMemberMemberTagRelation tag = new UmsMemberMemberTagRelation();
                tag.setMemberId(member.getId());
                tag.setTagId(Long.valueOf(id));
                list.add(tag);
            }
            if (list != null && list.size() > 0) {
                memberTagService.saveBatch(list);
            }
            return new CommonResult().success("绑定区域成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed("绑定区域失败");
        }
    }*/
}
