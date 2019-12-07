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
import com.zscat.mallplus.sms.mapper.SmsFlashPromotionSessionMapper;
import com.zscat.mallplus.sms.service.*;
import com.zscat.mallplus.sms.vo.SmsFlashPromotionSessionVo;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.util.DateUtil;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private ISmsCouponService couponService;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private IPmsFavoriteService favoriteService;
    @Resource
    private SmsFlashPromotionSessionMapper smsFlashPromotionSessionMapper;
    @Autowired
    private ISmsFlashPromotionProductRelationService smsFlashPromotionProductRelationService;
    @Resource
    private IPmsProductService pmsProductService;
    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private ISmsHomeAdvertiseService advertiseService;

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

    @ApiOperation("获取没有领取的优惠券列表")
    @RequestMapping(value = "/coupon.couponlist", method = RequestMethod.POST)
    public Object couponlist() {
        List<SmsCoupon> couponHistoryList = couponService.selectNotRecive();
        return new CommonResult().success(couponHistoryList);
    }


    @ApiOperation("获取单个商品得优惠详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        List<SmsBasicMarking> basicMarkingList = basicMarkingService.matchGoodsBasicMarking(id);
        List<SmsBasicGifts> basicGiftsList = basicGiftsService.matchGoodsBasicGifts(id);
        Map<String, Object> map = new HashMap<>();
        map.put("basicMarkingList", basicMarkingList);
        map.put("basicGiftsList", basicGiftsList);
        return new CommonResult().success(map);
    }


    @ApiOperation("秒杀活动时间段")
    @RequestMapping(value = "/seckillTime", method = RequestMethod.POST)
    public Object seckillTime() {
        SmsFlashPromotionSessionVo vo = new SmsFlashPromotionSessionVo();
        int count = 0;
        int count1 = 0;
        int count2 = 0;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        Date da = new Date();
        String format = sdf2.format(da);
        boolean falg = true;
        boolean falg1 = true;
        long nowT1 = DateUtil.strToDate(sdf1.format(da), "yyyy-MM-dd").getTime();
        long nowT = DateUtil.strToDate(format, "HH:mm:ss").getTime();
        List<SmsFlashPromotionSession> smsFlashSessionInfos = smsFlashPromotionSessionMapper.selectList(new QueryWrapper<SmsFlashPromotionSession>().eq("status", 1).orderByAsc("start_time"));
        for (SmsFlashPromotionSession session : smsFlashSessionInfos) {

            Date endtime = DateUtil.strToDate(session.getEndTime(), "HH:mm:ss");

            Date starttime = DateUtil.strToDate(session.getStartTime(), "HH:mm:ss");
            session.setStop(endtime.getTime() + nowT1 + 8 * 3600);

            if (nowT > endtime.getTime()) {
                session.setState("已结束");
                session.setStatus(0);
            }
            if (nowT < starttime.getTime()) {
                session.setState("即将开始");
                session.setStatus(2);
                if (falg1) {
                    falg1 = false;
                    count2 = count;

                }
            }
            if (nowT < endtime.getTime() && nowT > starttime.getTime()) {
                session.setState("抢购中");
                session.setStatus(1);
                if (falg) {
                    falg = false;
                    count1 = count;

                }
            }
            count++;
        }
        vo.setLovely("http://kaifa.crmeb.net/uploads/wechat/image/20190905/d07218c34eda83d9a19f2d30b86a7521.jpg");
        if (count1 == 0) {
            vo.setSeckillTimeIndex(count2);
        } else {
            vo.setSeckillTimeIndex(count1);
        }

        vo.setSeckillTime(smsFlashSessionInfos);
        return new CommonResult().success(vo);
    }


    @ApiOperation("秒杀活动时间段商品")
    @RequestMapping(value = "/seckillGoods", method = RequestMethod.POST)
    public Object seckillGoods(@RequestParam(value = "smsFlashSessionId", required = true) Long smsFlashSessionId,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {


        SmsFlashPromotionProductRelation querySMP = new SmsFlashPromotionProductRelation();

        querySMP.setFlashPromotionSessionId(smsFlashSessionId);
        IPage<SmsFlashPromotionProductRelation> page = smsFlashPromotionProductRelationService.page(new Page<SmsFlashPromotionProductRelation>(pageNum, pageSize), new QueryWrapper<>(querySMP));


        List<SmsFlashPromotionProductRelation> productAttrs = new ArrayList<>();
        for (SmsFlashPromotionProductRelation item : page.getRecords()) {
            PmsProduct tempproduct = pmsProductService.getById(item.getProductId());
            if (tempproduct != null) {
                SmsFlashPromotionProductRelation product = new SmsFlashPromotionProductRelation();
                BeanUtils.copyProperties(item, product);
                product.setProductId(tempproduct.getId());
                product.setProductImg(tempproduct.getPic());
                product.setProductName(tempproduct.getName());
                product.setProductPrice(tempproduct.getPrice() != null ? tempproduct.getPrice() : BigDecimal.ZERO);
                product.setFlashPromotionPrice(item.getFlashPromotionPrice());
                product.setFlashPromotionCount(item.getFlashPromotionCount());
                product.setPercent((double) (item.getFlashPromotionCount() * 100 / tempproduct.getStock()));
                if (item.getFlashPromotionLimit() < 1) {
                    product.setFlashPromotionLimit(1);
                } else {
                    product.setFlashPromotionLimit(item.getFlashPromotionLimit());
                }
                if (product.getProductPrice().compareTo(BigDecimal.ZERO) > 0 && item.getFlashPromotionCount() > 0) {
                    productAttrs.add(product);
                }
            } else {
                smsFlashPromotionProductRelationService.removeById(item.getId());
            }
        }
        return new CommonResult().success(productAttrs);
    }

    @SysLog(MODULE = "pms", REMARK = "查询商品详情信息")
    @IgnoreAuth
    @PostMapping(value = "/secskillDetail")
    @ApiOperation(value = "查询商品详情信息")
    public Object secskillDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        //记录浏览量到redis,然后定时更新到数据库

        SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(id);

        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, relation.getProductId() + "")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods)) {
                log.info("redis缓存失效：" + relation.getProductId());
                goods = pmsProductService.getGoodsRedisById(relation.getProductId());
            }
        } catch (Exception e) {
            log.info("redis缓存失效：" + relation.getProductId());
            goods = pmsProductService.getGoodsRedisById(relation.getProductId());
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();
        if (umsMember != null && umsMember.getId() != null) {
            PmsProduct p = goods.getGoods();
            p.setHit(recordGoodsFoot(id));
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
        map.put("skillDetail", relation);
        map.put("goods", goods);
        return new CommonResult().success(map);
    }


    private Integer recordGoodsFoot(Long id) {
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
            viewCount = 1;
            redisUtil.hPut(Rediskey.GOODS_VIEWCOUNT_KEY, key, 1 + "");
        }
        return viewCount;
    }
}
