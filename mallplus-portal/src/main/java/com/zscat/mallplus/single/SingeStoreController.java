package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.StatusEnum;
import com.zscat.mallplus.oms.vo.StoreContentResult;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IStoreService;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
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
    @Resource
    private RedisUtil redisUtil;

    @SysLog(MODULE = "sys", REMARK = "保存")
    @ApiOperation("保存")
    @PostMapping(value = "/applyStore")
    public Object applyStore(SysStore entity) {
        try {
            return storeService.applyStore(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed(e.getMessage());
        }
    }


    @IgnoreAuth
    @ApiOperation(value = "查询商铺列表")
    @GetMapping(value = "/store/list")
    @SysLog(MODULE = "ums", REMARK = "查询学校列表")
    public Object storeList(SysStore entity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        entity.setStatus(StatusEnum.AuditType.SUCESS.code());
        String orderColum = "create_time";
        if (ValidatorUtils.notEmpty(entity.getIsChecked())) {
            if (entity.getIsChecked() == 1) {
                orderColum = "hit";
            } else if (entity.getIsChecked() == 2) {
                orderColum = "collect";
            }
        }
        return new CommonResult().success(storeService.page(new Page<SysStore>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc(orderColum)));
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        if (ValidatorUtils.empty(id)) {
            UmsMember member = memberService.getNewCurrentMember();
            if (member == null) {
                return new CommonResult().fail(100);
            }
            id = memberService.getById(member.getId()).getStoreId();
        }
        SysStore store = storeService.getById(id);
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
        return new CommonResult().success(store);
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/detail1", method = RequestMethod.GET)
    @ResponseBody
    public Object detail1(@RequestParam(value = "id", required = false) Integer id) {
        Map map = new HashMap();
        if (ValidatorUtils.empty(id)) {
            UmsMember member = memberService.getNewCurrentMember();
            if (member == null) {
                return new CommonResult().fail(100);
            }
            UmsMember newMember = memberService.getById(member.getId());
            id = newMember.getStoreId();
            map.put("member", newMember);
        }
        SysStore store = storeService.getById(id);
        map.put("store", store);
        return new CommonResult().success(map);
    }

    @ApiOperation("获取店铺详情")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public Object home(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        StoreContentResult contentResult = null;
        String key = "";
        try {
            if (ValidatorUtils.empty(id)) {
                UmsMember member = memberService.getNewCurrentMember();
                if (member == null) {
                    return new CommonResult().fail(100);
                }
                UmsMember newMember = memberService.getById(member.getId());
                id = newMember.getStoreId();
            }
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("商户申请中");
            }
            key = String.format(Rediskey.STOREHOMEPAGEMOBILE, id);
            String json = redisService.get(key);
            if (ValidatorUtils.empty(json)) {
                contentResult = storeService.singeleContent(id);
                redisService.set(key, JsonUtils.objectToJson(contentResult));
                redisService.expire(key, 60);
            } else {
                contentResult = JsonUtils.jsonToPojo(redisService.get(key), StoreContentResult.class);
            }
        } catch (Exception e) {
            contentResult = storeService.singeleContent(id);
            redisService.set(key, JsonUtils.objectToJson(contentResult));
            redisService.expire(key, 60);
        }
        return new CommonResult().success(contentResult);
    }
}
