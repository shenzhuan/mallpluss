package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.cms.service.ISysAreaService;
import com.zscat.mallplus.cms.service.ISysSchoolService;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.oms.vo.HomeContentResult;
import com.zscat.mallplus.oms.vo.StoreContentResult;
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
import com.zscat.mallplus.ums.mapper.UmsEmployInfoMapper;
import com.zscat.mallplus.ums.mapper.UmsRewardLogMapper;
import com.zscat.mallplus.ums.service.IStoreService;
import com.zscat.mallplus.ums.service.IUmsMemberMemberTagRelationService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "SingeStoreController", description = "会员关系管理")
@RequestMapping("/api/single/store")
public class SingeStoreController extends ApiBaseAction {
    @Resource
    IUmsMemberService memberService;
    @Resource
    private IStoreService storeService;
    @Autowired
    private RedisService redisService;

    @SysLog(MODULE = "sys", REMARK = "保存")
    @ApiOperation("保存")
    @PostMapping(value = "/applyStore")
    public Object applyStore(SysStore entity) {
        try {
            storeService.applyStore(entity);
        } catch (Exception e) {
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed("保存失败");
    }


    @IgnoreAuth
    @ApiOperation(value = "查询商铺列表")
    @GetMapping(value = "/store/list")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object storeList(SysStore entity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        entity.setStatus(3);
        String orderColum="create_time";
        if (ValidatorUtils.notEmpty(entity.getIsChecked())){
            if (entity.getIsChecked()==1){
                orderColum="hit";
            }else  if (entity.getIsChecked()==2){
                orderColum="collect";
            }
        }
        return new CommonResult().success(storeService.page(new Page<SysStore>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc(orderColum)));
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        if (ValidatorUtils.empty(id)){
            UmsMember member = memberService.getNewCurrentMember();
            if (member==null){
                return new CommonResult().fail(100);
            }
            id=member.getStoreId();
        }
        SysStore store = storeService.getById(id);
        return new CommonResult().success(store);
    }
    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public Object home(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        StoreContentResult contentResult = null;
        String key ="";
        try {
            if (ValidatorUtils.empty(id)){
                UmsMember member = memberService.getNewCurrentMember();
                if (member==null){
                    return new CommonResult().fail(100);
                }
                id=member.getStoreId();
            }
            if (ValidatorUtils.empty(id)){
                return new CommonResult().failed("weikaitou");
            }
            key = String.format(Rediskey.STOREHOMEPAGEMOBILE, id);
            String json = redisService.get(key);
            if (ValidatorUtils.empty(json)) {
                contentResult = storeService.singeleContent(id);
                redisService.set(key, JsonUtils.objectToJson(contentResult));
                redisService.expire(key, 360);
            } else {
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), StoreContentResult.class);
            }
        } catch (Exception e) {
            contentResult = storeService.singeleContent(id);
            redisService.set(key, JsonUtils.objectToJson(contentResult));
            redisService.expire(key, 360);
        }
        return new CommonResult().success(contentResult);
    }
}
