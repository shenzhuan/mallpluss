package com.zscat.mallplus.single;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.pms.entity.PmsFavorite;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsFavoriteService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.vo.GoodsDetailResult;
import com.zscat.mallplus.sms.entity.SmsBasicGifts;
import com.zscat.mallplus.sms.entity.SmsBasicMarking;
import com.zscat.mallplus.sms.entity.SmsCouponHistory;
import com.zscat.mallplus.sms.entity.SmsGroupActivity;
import com.zscat.mallplus.sms.service.ISmsBasicGiftsService;
import com.zscat.mallplus.sms.service.ISmsBasicMarkingService;
import com.zscat.mallplus.sms.service.ISmsCouponService;
import com.zscat.mallplus.sms.service.ISmsGroupActivityService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@RestController
@Api(tags = "SingeMarkingController", description = "营销管理")
@RequestMapping("/api/single/sms")
public class SingeMarkingController extends ApiBaseAction {

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
    @Autowired
    private IUmsMemberService memberService;


    @ApiOperation("领取指定优惠券")
    @PostMapping(value = "/add")
    public Object add(@RequestParam(value = "couponId", required = true) Long couponId) {
        return couponService.add(couponId);
    }

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/listMemberCoupon", method = RequestMethod.GET)
    public Object list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = couponService.listMemberCoupon(useStatus);
        return new CommonResult().success(couponHistoryList);
    }


    @ApiOperation("获取单个商品得优惠详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        List<SmsBasicMarking> basicMarkingList = basicMarkingService.matchGoodsBasicMarking(id);
        List<SmsBasicGifts> basicGiftsList = basicGiftsService.matchGoodsBasicGifts(id);
        Map<String,Object> map = new HashMap<>();
        map.put("basicMarkingList",basicMarkingList);
        map.put("basicGiftsList",basicGiftsList);
        return new CommonResult().success(map);
    }

    @IgnoreAuth
    @SysLog(MODULE = "oms", REMARK = "查询订单列表")
    @ApiOperation(value = "查询订单列表")
    @ResponseBody
    @RequestMapping(value = "/groupActivityList", method = RequestMethod.GET)
    public Object orderList(SmsGroupActivity groupActivity,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
                            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        IPage<SmsGroupActivity> page = null;
        groupActivity.setStatus(1);
        page = smsGroupActivityService.page(new Page<SmsGroupActivity>(pageNum, pageSize), new QueryWrapper<>(groupActivity).orderByDesc("create_time")) ;

        for (SmsGroupActivity smsGroupActivity : page.getRecords()) {
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) productService.list(new QueryWrapper<PmsProduct>().in("id",Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList())).select(ConstansValue.sampleGoodsList));
                if (productList != null && productList.size() > 0) {
                    smsGroupActivity.setProductList(productList);
                }
            }

        }
        return new CommonResult().success(page);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @ResponseBody
    @RequestMapping(value = "/group.activity.getdetial", method = RequestMethod.GET)
    @ApiOperation(value = "查询商品详情信息")
    public Object queryProductDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SmsGroupActivity groupActivity = smsGroupActivityService.getById(id);
        Map<String, Object> map = new HashMap<>();
        if (groupActivity != null) {
            if (ValidatorUtils.notEmpty(groupActivity.getGoodsIds())) {
                List<Long> goodIds = Arrays.asList(groupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                GoodsDetailResult goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, goodIds.get(0) + "")), GoodsDetailResult.class);
                if (goods == null || goods.getGoods() == null) {
                    goods = productService.getGoodsRedisById(goodIds.get(0));
                }
                    if (goods != null && goods.getGoods() != null) {
                        UmsMember umsMember = memberService.getNewCurrentMember();
                        if (umsMember != null && umsMember.getId() != null) {
                            isCollectGoods(map, goods, umsMember);
                        }
                        recordGoodsFoot(id);

                        List<Long> newGoodIds = goodIds.subList(1, goodIds.size());
                        if (newGoodIds != null && newGoodIds.size() > 0) {
                            List<PmsProduct> productList = (List<PmsProduct>) productService.list(new QueryWrapper<PmsProduct>().in("id",goodIds).select(ConstansValue.sampleGoodsList));
                            if (productList != null && productList.size() > 0) {
                                groupActivity.setProductList(productList);
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

    /**
     * 判断是否收藏商品
     * @param map
     * @param goods
     * @param umsMember
     */
    private void isCollectGoods(Map<String, Object> map, GoodsDetailResult goods, UmsMember umsMember) {
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

    /**
     * 记录商品浏览记录
     * @param id
     */
    private void recordGoodsFoot(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
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
    }

}
