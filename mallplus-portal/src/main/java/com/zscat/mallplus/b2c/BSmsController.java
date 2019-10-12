package com.zscat.mallplus.b2c;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.pms.entity.PmsFavorite;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsFavoriteService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.vo.GoodsDetailResult;
import com.zscat.mallplus.single.ApiBaseAction;
import com.zscat.mallplus.sms.entity.*;
import com.zscat.mallplus.sms.service.ISmsBasicGiftsService;
import com.zscat.mallplus.sms.service.ISmsBasicMarkingService;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsGroupActivityService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.GoodsUtils;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/2 15:02
 * @Description:
 */
@Slf4j
@RestController
@Api(tags = "SingeMarkingController", description = "营销管理")
public class BSmsController extends ApiBaseAction {

    @Resource
    private ISmsBasicGiftsService basicGiftsService;
    @Resource
    private ISmsBasicMarkingService basicMarkingService;
    @Resource
    private RedisService redisService;
    @Resource
    private ISmsGroupActivityService smsGroupActivityService;
    @Resource
    private IPmsProductService productService;
    @Autowired
    ISmsCouponService couponService;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private IPmsFavoriteService favoriteService;

    @ApiOperation("领取指定优惠券")
    @PostMapping(value = "/coupon.getcoupon")
    public Object add(@RequestParam(value = "couponId", required = true) Long couponId) {
        return couponService.add(couponId);
    }
    @ApiOperation("批量领取指定优惠券")
    @PostMapping(value = "/batch.getcoupon")
    public Object addbatch(@RequestParam(value = "couponIds", required = true) String couponIds) {
        return couponService.addbatch(couponIds);
    }
    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/coupon.usercoupon", method = RequestMethod.POST)
    public Object list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = couponService.listMemberCoupon(useStatus);
        return new CommonResult().success(couponHistoryList);
    }

    @RequestMapping(value = "/coupon.couponlist", method = RequestMethod.POST)
    public Object couponlist() {
        List<SmsCoupon> couponHistoryList = couponService.selectNotRecive();
        return new CommonResult().success(couponHistoryList);
    }

    @ApiOperation("获取单个商品得优惠详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        List<SmsBasicMarking> list1 = basicMarkingService.matchGoodsMk(id);
        List<SmsBasicGifts> listg = basicGiftsService.matchGoodsMk(id);
        return new CommonResult().success(1);
    }

    @IgnoreAuth
    @SysLog(MODULE = "oms", REMARK = "查询订单列表")
    @ApiOperation(value = "查询订单列表")
    @PostMapping(value = "/groupActivityList")
    public Object orderList(SmsGroupActivity groupActivity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        IPage<SmsGroupActivity> page = null;
        groupActivity.setStatus(1);
            page = smsGroupActivityService.page(new Page<SmsGroupActivity>(pageNum, pageSize), new QueryWrapper<>(groupActivity).orderByDesc("create_time")) ;

        for (SmsGroupActivity smsGroupActivity : page.getRecords()) {
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                        Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                if (productList != null && productList.size() > 0) {
                    smsGroupActivity.setProductList(GoodsUtils.sampleGoodsList(productList));
                }
            }

        }
        return new CommonResult().success(page);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @PostMapping(value = "/group.activity.getdetial")
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SmsGroupActivity groupActivity = smsGroupActivityService.getById(id);
        Map<String, Object> map = new HashMap<>();
        if (groupActivity != null) {
            if (ValidatorUtils.notEmpty(groupActivity.getGoodsIds())) {
                List<Long> goodIds = Arrays.asList(groupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                GoodsDetailResult goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, goodIds.get(0) + "")), GoodsDetailResult.class);
                if (goods != null && goods.getGoods() != null) {
                    UmsMember umsMember = UserUtils.getCurrentMember();
                    if (umsMember != null && umsMember.getId() != null) {
                        PmsProduct p = goods.getGoods();
                        PmsFavorite query = new PmsFavorite();
                        query.setObjId(p.getId());
                        query.setMemberId(umsMember.getId());
                        query.setType(1);
                        PmsFavorite findCollection = favoriteService.getOne(new QueryWrapper<>(query));
                        if (findCollection != null) {
                            map.put("favorite", true);
                        } else {
                            map.put("favorite", false);
                        }
                    }
                    //记录浏览量到redis,然后定时更新到数据库
                    String key = Rediskey.GOODS_VIEWCOUNT_CODE + id;
                    //找到redis中该篇文章的点赞数，如果不存在则向redis中添加一条
                    Map<Object, Object> viewCountItem = redisUtil.hGetAll(Rediskey.GOODS_VIEWCOUNT_KEY);
                    Integer viewCount = 0;
                    if (!viewCountItem.isEmpty()) {
                        if (viewCountItem.containsKey(key)) {
                            viewCount = Integer.parseInt(viewCountItem.get(key).toString()) + 1;
                            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, viewCount + "");
                        } else {
                            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, 1 + "");
                        }
                    } else {
                        redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, 1 + "");
                    }

                    List<Long> newGoodIds = goodIds.subList(1, goodIds.size() - 1);
                    if (newGoodIds != null && newGoodIds.size() > 0) {
                        List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(newGoodIds);
                        if (productList != null && productList.size() > 0) {
                            groupActivity.setProductList(GoodsUtils.sampleGoodsList(productList));
                        }
                    }
                    map.put("groupActivity", groupActivity);
                    map.put("goods", goods);
                    return new CommonResult().success(map);
                }
            }
        }
        return new CommonResult().failed();
    }
}
