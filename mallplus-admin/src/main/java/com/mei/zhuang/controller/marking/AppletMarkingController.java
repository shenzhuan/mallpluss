package com.mei.zhuang.controller.marking;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.marking.EsShopActivityPrizeMapper;
import com.mei.zhuang.dao.marking.EsShopCouponMapper;
import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.entity.order.EsShopCart;
import com.mei.zhuang.entity.order.EsShopOrderGoods;
import com.mei.zhuang.service.marking.*;
import com.mei.zhuang.service.order.MembersFegin;
import com.mei.zhuang.service.order.ShopOrderService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import com.mei.zhuang.vo.marking.AllMemberCoupon;
import com.mei.zhuang.vo.marking.CodeResult;
import com.mei.zhuang.vo.marking.MjDcVo;
import com.mei.zhuang.vo.order.CartMarkingVo;
import com.mei.zhuang.vo.order.CouponFilterParam;
import com.mei.zhuang.vo.order.OrderStstic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Auther: shenzhuan
 * @Date: 2019/5/26 00:37
 * @Description:
 */
@Slf4j
@RestController
@Api(value = "小程序营销管理", description = "", tags = {"小程序营销管理"})
public class AppletMarkingController {
    @Resource
    private ShopOrderService orderFegin;
    @Resource
    private MemberCouponService memberCouponService;
    @Resource
    private ManJianService manJianService;
    @Resource
    private DiscountService discountService;
    @Resource
    private FirstPurchaseService firstPurchaseService;
    @Resource
    private FullGiftService fullGiftService;
    @Resource
    private SingleGiftService singleGiftService;
    @Resource
    private RulesSpecService rulesService;
    @Resource
    private CodeGiftService codeGiftService;
    @Resource
    private EsShopFriendGiftService giftService;
    @Resource
    private EsShopActivityService esShopActivityService;
    @Resource
    private EsShopActivityPrizeMapper esShopActivityPrizeMapper;
    @Resource
    private EsMemberActivatyRecordService esMemberActivatyRecordService;
    @Resource
    private MembersFegin membersFegin;
    @Resource
    private EsShopShareService esShopShareService;
    @Resource
    private EsShopCouponMapper esShopCouponMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");

    public static int drawGift(List<EsShopActivityPrize> giftList) {

        if (null != giftList && giftList.size() > 0) {
            List<Double> orgProbList = new ArrayList<Double>(giftList.size());
            for (EsShopActivityPrize prize : giftList) {
                //按顺序将概率添加到集合中

                orgProbList.add(Double.valueOf(prize.getWinning()));
            }

            return draw(orgProbList);

        }
        return -1;
    }

    public static int draw(List<Double> giftProbList) {
        if (giftProbList == null || giftProbList.isEmpty()) {
            return -1;
        }
        int size = giftProbList.size();
        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : giftProbList) {
            sumRate += rate;
        }
        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        /*遍历奖品概率的集合，计算每一个奖品的中间区间*/
        for (double rate : giftProbList) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }
        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);
        return sortOrignalRates.indexOf(nextDouble);
    }

    public static Date addDays(Date s, int n) {

        try {
            SimpleDateFormat FORMATER_DATE_YMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cd = Calendar.getInstance();
            cd.setTime(s);
            cd.add(5, n);
            return FORMATER_DATE_YMD.parse(FORMATER_DATE_YMD.format(cd.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @ApiOperation("根据条件查询所有验证码赠礼列表")
    @PostMapping(value = "/preOrderMarking")
    public Object preOrderMarking() {
        try {
        } catch (Exception e) {
            log.error("根据条件查询所有验证码赠礼列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("根据用户id获得该用户的所有优惠券信息")
    @PostMapping(value = "/applet/selectListByMemberId", params = "memberId")
    public List<EsMemberCoupon> selectListByMemberId(@RequestParam("memberId") Long memberId) {
        return memberCouponService.list(new QueryWrapper<EsMemberCoupon>().eq("member_id", memberId));
    }

    @ApiOperation("获得用户不同状态的所有优惠券")
    @PostMapping(value = "/applet/selectUserCoupon")
    public AllMemberCoupon selectUserMemberCouponList(@RequestParam("memberId") Long memberId) throws ParseException {
        return memberCouponService.selectUserMemberCouponList(memberId);
    }

    @ApiOperation("根据不同条件（Vo）获得可用的优惠券")
    @PostMapping(value = "/applet/selectUserMemberCoupon")
    public List<EsMemberCoupon> selectUserMemberCoupon(@RequestBody CartMarkingVo vo) throws ParseException {
        return memberCouponService.selectUserMemberCoupon(vo);
    }

    @ApiOperation("判断该优惠券是否可用")
    @PostMapping(value = "/applet/isCouponUsable")
    public boolean isCouponUsable(@RequestParam("couponUser") EsMemberCoupon couponUser, @RequestParam("condition") CouponFilterParam condition) {
        return memberCouponService.isCouponUsable(couponUser, condition);
    }

    @ApiOperation("查询赠品券得赠品")
    @PostMapping(value = "/applet/selectSendCouponGift")
    public List<EsShopCouponGoodsMap> selectSendCouponGift(@RequestParam("couponId") Long couponId) {
        return memberCouponService.selectSendCouponGift(couponId);
    }

    @ApiOperation("除去所有优惠条件获得最终支付金额")
    @PostMapping(value = "/applet/selectById")
    public BigDecimal selectById(@RequestBody CartMarkingVo vo) {
        return memberCouponService.selectUserCouponById(vo);
    }

    @ApiOperation("更新用户优惠券")
    @PostMapping(value = "/applet/updateMemberCoupon")
    public void updateMemberCoupon(@RequestParam("couponId") Long couponId, @RequestParam("orderId") long orderId, @RequestParam("orderNo") String orderNo, @RequestParam("status") Integer status) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EsMemberCoupon coupon = new EsMemberCoupon();
        if (status == 0) {
            coupon.setStatus(0);
            coupon.setOrderNo(orderNo);
            coupon.setOrderId(orderId);
            coupon.setId(couponId);
            coupon.setUsedTime(sdf.format(new Date()));
            coupon.setUesdDate(sdf.format(new Date()));
        } else {
            coupon.setStatus(1);
            coupon.setId(couponId);
            coupon.setUsedTime(null);
            coupon.setUesdDate(null);
        }

        memberCouponService.updateById(coupon);
    }

    @ApiOperation("将优惠金额平摊到每个订单商品中")
    @PostMapping(value = "/applet/shareCouponDiscount")
    public void shareCouponDiscount(@RequestParam("memberCoupon") EsMemberCoupon memberCoupon, @RequestParam("orderItemList") List<EsShopOrderGoods> orderItemList) {
        memberCouponService.shareCouponDiscount(memberCoupon, orderItemList);
    }

    @ApiOperation("释放优惠券")
    @PostMapping(value = "/applet/releaseCoupon", params = "id")
    public void releaseCoupon(@RequestParam("id") Long id) {
        memberCouponService.releaseCoupon(id);
    }

    @ApiOperation("发送新人券 推券节点1,进店2下单,3支付")
    @PostMapping(value = "/applet/sendNewCoupon")
    public Object sendNewCoupon(@RequestParam("memberId") Long memberId, @RequestParam("type") int type) {

        return new CommonResult().success(memberCouponService.saveadd(memberId, type));
    }

    @ApiOperation("满额发券 订单完成1, 订单支付2")
    @PostMapping(value = "/applet/sendFillFillCoupon")
    public void sendFillFillCoupon(@RequestBody CartMarkingVo vo) {
        memberCouponService.sendFillFillCoupon(vo);
    }

    @ApiOperation("购物发券 订单完成1, 订单支付2")
    @PostMapping(value = "/applet/sendShopCoupon")
    public void sendShopCoupon(@RequestBody CartMarkingVo vo) {
        memberCouponService.sendShopCoupon(vo);
    }

    @ApiOperation("手工发券 订单完成1, 订单支付2")
    @PostMapping(value = "/applet/sendManualCoupon")
    public void sendManualCoupon(@RequestBody CartMarkingVo vo) throws ParseException {
        memberCouponService.sendManualCoupon(vo);
    }

    @ApiOperation("验证码兑换")
    @PostMapping(value = "/applet/getCodeGoods")
    public CodeResult getCodeGoods(@RequestBody CartMarkingVo vo) throws Exception {
        return codeGiftService.getCodeGoods(vo);
    }

    @ApiOperation("更新验证码状态")
    @PostMapping(value = "/applet/updateCodeStatus")
    public void updateCodeStatus(@RequestParam("code") String code, @RequestParam("status") Integer status) {
        codeGiftService.updateCodeStatus(code, status);
    }

    @ApiOperation("根据购物车信息获得满减规则")
    @PostMapping(value = "/applet/matchManjian")
    public MjDcVo matchManjian(@RequestBody List<EsShopCart> cartList) throws Exception {
        return manJianService.matchManjian(cartList);
    }

    @ApiOperation("限时折扣")
    @PostMapping(value = "/applet/matchDiscount")
    public MjDcVo matchDiscount(@RequestBody List<EsShopCart> cartList) throws ParseException {
        return discountService.matchDiscount(cartList);
    }

    @ApiOperation("首赠礼")
    @PostMapping(value = "/applet/matchFirstPurchase")
    public List<EsShopFirstPurchaseRule> matchFirstPurchase(@RequestBody CartMarkingVo vo) throws Exception {
        return firstPurchaseService.matchFirstPurchase(vo);
    }

    @ApiOperation("满赠礼")
    @PostMapping(value = "/applet/matchFullGift")
    public List<EsShopFullGift> matchFullGift(@RequestBody List<EsShopCart> cartList) throws Exception {
        return fullGiftService.matchFullGift(cartList);
    }

    @ApiOperation("选赠礼")
    @PostMapping(value = "/applet/ChooseFullGift")
    public List<EsShopFullGift> ChooseFullGift(@RequestBody List<EsShopCart> cartList) throws Exception {
        return fullGiftService.ChooseFullGift(cartList);
    }

    @ApiOperation("满赠礼不符合也显示")
    @PostMapping(value = "/applet/matchFullGift2")
    public List<EsShopFullGift> matchFullGift2() {
        return fullGiftService.selectrule();
    }

    @ApiOperation("规则商品换购")
    @PostMapping(value = "/applet/matchGoodsRules")
    public EsShopGoodsRules matchGoodsRules(@RequestBody List<EsShopCart> cartList) throws Exception {
        return rulesService.matchGoodsRules(cartList);
    }

    @ApiOperation("单品礼赠")
    @PostMapping(value = "/applet/matchSingleGift")
    public List<EsShopSingleGift> matchSingleGift(@RequestBody List<EsShopCart> cartList) throws Exception {
        return singleGiftService.matchSingleGift(cartList);
    }

    @ApiOperation("选中的单品礼赠")
    @PostMapping(value = "/applet/isSingleGiftUseAble")
    public List<EsShopSingleGiftGoodsMap> isSingleGiftUseAble(@RequestBody CartMarkingVo vo) {
        return singleGiftService.isSingleGiftUseAble(vo);
    }

    @ApiOperation("选中的满减是否可以用")
    @PostMapping(value = "/applet/isManJianUseAble")
    public EsShopManjianRule isManJianUseAble(@RequestBody CartMarkingVo vo) {
        return manJianService.isManJianUseAble(vo);
    }

    @ApiOperation("选中的折扣是否可以用")
    @PostMapping(value = "/applet/isDiscountRuleUseAble")
    public EsShopDiscountRule isDiscountRuleUseAble(@RequestBody CartMarkingVo vo) {
        return discountService.isDiscountRuleUseAble(vo);
    }

    @ApiOperation("选中的首赠礼是否可以用")
    @PostMapping(value = "/applet/isFirstPurchaseUseAble")
    public List<EsShopFirstPurchaseRule> isFirstPurchaseUseAble(@RequestBody CartMarkingVo vo) {
        return firstPurchaseService.isFirstPurchaseUseAble(vo);
    }

    @ApiOperation("选中的满赠礼是否可以用")
    @PostMapping(value = "/applet/isFullGiftGoodsUseAble")
    public List<EsShopFullGiftGoodsMap> isFullGiftGoodsUseAble(@RequestBody CartMarkingVo vo) {
        return fullGiftService.isFullGiftGoodsUseAble(vo);
    }

    @ApiOperation("选中的选赠礼是否可以用")
    @PostMapping(value = "/applet/isChooseGiftGoodsUseAble")
    public List<EsShopFullGiftGoodsMap> isChooseGiftGoodsUseAble(@RequestBody CartMarkingVo vo) {
        return fullGiftService.isChooseGiftGoodsUseAble(vo);
    }

    @ApiOperation("更新折扣的订单信息")
    @PostMapping(value = "/updateDiscountOrderInfo")
    public Object updateDiscountOrderInfo() {
        try {
            List<OrderStstic> orders = orderFegin.listOrderGroupByDiscountId();
            for (OrderStstic o : orders) {
                EsShopDiscount discount = new EsShopDiscount();
                discount.setId(o.getObjId());
                discount.setPayOrderCount(o.getTotalCount());
                discount.setPayAmount(o.getTotalPayAmount());
                discount.setAttendUserCount(o.getMemberCount());
                discount.setUnitPrice(o.getTotalPayAmount().divide(new BigDecimal(o.getTotalCount())));
                discountService.updateById(discount);
            }
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("更新折扣的订单信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("更新满减的订单信息")
    @PostMapping(value = "/updateManJianOrderInfo")
    public Object updateManJianOrderInfo() {
        try {
            List<OrderStstic> orders = orderFegin.listOrderGroupByManJianId();
            for (OrderStstic o : orders) {
                EsShopManjian manjian = new EsShopManjian();
                manjian.setId(o.getObjId());
                manjian.setPayOrderCount(o.getTotalCount());
                manjian.setPayAmount(o.getTotalPayAmount());
                manjian.setAttendUserCount(o.getMemberCount());
                manjian.setUnitPrice(o.getTotalPayAmount().divide(new BigDecimal(o.getTotalCount())));
                manJianService.updateById(manjian);
            }
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("更新满减的订单信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询正在进行的抽奖有礼活动")
    @PostMapping(value = "/selActivaty")
    public Object selActivaty(@RequestParam("id") Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().failed("请指定活动编号")
                        ;
            }
            long time = System.currentTimeMillis();//当前时间时间戳
            EsShopActivity esShopActivity = new EsShopActivity();
            esShopActivity.setId(id);
            EsShopActivity actavaty = esShopActivityService.getOne(new QueryWrapper<>(esShopActivity));
            if (actavaty != null) {
                if (actavaty.getActivityStartTime() <= time) {
                    if (actavaty.getActivityEndTime() >= time) {
                        //查询奖品
                        EsShopActivityPrize prize = new EsShopActivityPrize();
                        prize.setActivatyId(actavaty.getId());
                        actavaty.setList(esShopActivityPrizeMapper.selectList(new QueryWrapper<>(prize)));
                        return new CommonResult().success("success", actavaty);
                    }
                }
            }

            return new CommonResult().failed(3, "暂无进行中的活动");
        } catch (Exception e) {
            log.error("查询正在进行的礼活动异常：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("抽奖")
    @PostMapping(value = "/selActivatyApplet")
    public Object selActivatyApplet(EsShopActivity entity) {
        try {
            if (ValidatorUtils.empty(entity.getMemberId())) {
                return new CommonResult().failed("请指定用户编号");
            }
            if (ValidatorUtils.empty(entity.getId())) {
                return new CommonResult().failed("请指定活动编号");
            }
            EsMemberActivatyRecord record = new EsMemberActivatyRecord();
            EsShopActivity activity = esShopActivityService.getById(entity.getId());
            if (activity != null) {
                EsShopActivityPrize prize = new EsShopActivityPrize();
                prize.setActivatyId(entity.getId());
                List<EsShopActivityPrize> list = esShopActivityPrizeMapper.selectList(new QueryWrapper<>(prize));
                EsMember esMember = membersFegin.getMemberById(entity.getMemberId());
                //添加未中奖率
                prize.setWinning(activity.getWinRate());
                prize.setId(activity.getId());
                list.add(prize);
                //随机执行
                for (int i = 0; i < 1; i++) {
                    int index = drawGift(list);
                    if (index >= 0) {
                        prize = new EsShopActivityPrize();
                        prize = list.get(index);
                    }
                }
                Integer iswin = 0;
                if (prize.getId().equals(activity.getId())) {//未中奖id和中奖id用的一个id
                    iswin = 0;
                } else {
                    iswin = 1;
                }
                //插入一条中奖或未中奖记录
                EsMemberActivatyRecord record1 = new EsMemberActivatyRecord();
                record1.setCreateTime(new Date());
                record1.setActivatyId(entity.getId());
                record1.setMemberId(entity.getMemberId());
                record1.setIsWin(iswin);
                record1.setPrizeId(prize.getId());
                if (esMember != null) {
                    record1.setNickName(esMember.getNickname());
                    record1.setOpenId(esMember.getOpenid());
                    if (iswin == 1) {
                        EsShopActivityPrize esShopActivityPrize = esShopActivityPrizeMapper.selectById(prize.getId());
                        EsShopCoupon coupon = esShopCouponMapper.selectById(Long.parseLong(esShopActivityPrize.getGoodsContent()));
                        record1.setPrizeLevel(esShopActivityPrize.getName());
                        record1.setPrizeName(coupon.getCouponsName());
                    }
                    record1.setActivatyName(activity.getName());
                } else {
                    return new CommonResult().failed("用户不存在");
                }
                //参与总数
                //中奖次数
                //每人每天最多参与次数

                //1.查询用户参与总次数
                record = new EsMemberActivatyRecord();
                record.setMemberId(entity.getMemberId());
                record.setActivatyId(entity.getId());
                Integer count = esMemberActivatyRecordService.count(new QueryWrapper<>(record));
                if (count != null) {
                    count = activity.getParticipantNum().compareTo(count);
                    if (count == -1 || count == 0) {
                        return new CommonResult().failed("参与次数已满");
                    }
                }

                //2.中奖次数
                record = new EsMemberActivatyRecord();
                record.setMemberId(entity.getMemberId());
                record.setActivatyId(entity.getId());
                record.setIsWin(1);
                Integer recordCount = esMemberActivatyRecordService.count(new QueryWrapper<>(record));
                if (recordCount != null) {
                    recordCount = activity.getWinNum().compareTo(recordCount);
                    if (recordCount == -1 || recordCount == 0) {
                        return new CommonResult().success("success", selIsWinn(entity.getId(), record1));//iswin//中奖次数已满
                    }
                }

                //3.单人每天最多参与次数
                String time = sdfTime.format(new Date());
                String beginTimeS = time + " 00:00:00";//开始时间
                String endTimeS = time + " 23:59:59";//结束时间
                Date beginTimeD = sdf.parse(beginTimeS);
                Date endTimeD = sdf.parse(endTimeS);
                record = new EsMemberActivatyRecord();
                record.setMemberId(entity.getMemberId());
                record.setActivatyId(entity.getId());
                List<EsMemberActivatyRecord> esMemberActivatyRecordList = esMemberActivatyRecordService.list(new QueryWrapper<>(record));
                //次数统计
                Integer number = 0;
                if (esMemberActivatyRecordList != null) {
                    for (EsMemberActivatyRecord recordList : esMemberActivatyRecordList) {
                        int num = recordList.getCreateTime().compareTo(beginTimeD);
                        if (num == 1) {
                            num = recordList.getCreateTime().compareTo(endTimeD);
                            if (num == -1) {
                                number += 1;
                            }
                        }
                    }
                    if (number >= activity.getEveryoneParticipantNum()) {
                        return new CommonResult().failed("当天次数已满");
                    }
                }
                if (esMember != null) {
                    record1.setNickName(esMember.getNickname());
                    record1.setOpenId(esMember.getOpenid());
                    if (iswin == 1) {
                        EsShopActivityPrize esShopActivityPrize = esShopActivityPrizeMapper.selectById(prize.getId());
                        EsShopCoupon coupon = esShopCouponMapper.selectById(Long.parseLong(esShopActivityPrize.getGoodsContent()));
                        record1.setPrizeLevel(esShopActivityPrize.getName());
                        record1.setPrizeName(coupon.getCouponsName());
                    }
                    record1.setActivatyName(activity.getName());

                    Map<String, Object> map = new HashMap<String, Object>();
                    if (iswin == 1) {
                        //给中奖用户发卷
                        //查询优惠卷
                        EsMemberCoupon memberCoupon = new EsMemberCoupon();
                        if (prize.getType() == 2) {
                            EsShopCoupon coupon = esShopCouponMapper.selectById(Long.parseLong(prize.getGoodsContent()));
                            if (coupon.getStock() > 0) {
                                //1.查询单日最低发放数量
                                //已发放奖品数量
                                EsMemberCoupon esMemberCoupon = new EsMemberCoupon();
                                esMemberCoupon.setMemberId(entity.getMemberId());
                                esMemberCoupon.setCouponId(coupon.getId());
                                EsShopActivityPrize activityPrize = esShopActivityPrizeMapper.selectById(prize.getId());
                                if (activityPrize.getUpperLimit() != null && activityPrize.getUpperLimit() > 0) {
                                    Integer num = memberCouponService.count(new QueryWrapper<>(esMemberCoupon));
                                    if (num >= activityPrize.getUpperLimit()) {
                                        //单人中奖个数上限
                                        return new CommonResult().success("success", selIsWinn(entity.getId(), record1));
                                    }
                                }
                                //2.单日最多发放件数
                                if (activityPrize.getMost() != null && activityPrize.getMost() > 0) {
                                    List<EsMemberCoupon> countNum = memberCouponService.selectCountMax(esMemberCoupon);
                                    if (countNum != null && countNum.size() > 0) {
                                        if (activityPrize.getMost() <= countNum.size()) {
                                            //2.单日最多发放件数
                                            System.out.println("满单日最多发放件数");
                                            return new CommonResult().success("success", selIsWinn(entity.getId(), record1));
                                        }
                                    }
                                }

                                memberCoupon.setFroms(6);
                                memberCoupon.setCreateTime(sdf.format(new Date()));
                                memberCoupon.setCouponId(coupon.getId());
                                memberCoupon.setTitle(coupon.getCouponsName());
                                memberCoupon.setStartTime(coupon.getExpiryBeginTime());
                                memberCoupon.setEndTime(coupon.getExpiryEndTime());
                                //有效期 1固定，2当前，3次日
                                Date createTime = new Date();
                                if (coupon.getAmount() == 2) {
                                    memberCoupon.setStartTime(createTime);
                                    memberCoupon.setEndTime(addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
                                }
                                if (coupon.getAmount() == 3) {
                                    memberCoupon.setStartTime(addDays(createTime, 1));
                                    memberCoupon.setEndTime(addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
                                }
                                //用户id
                                memberCoupon.setMemberId(entity.getMemberId());
                                memberCoupon.setConditions(1);
                                //金额或折扣
                                memberCoupon.setAmount(coupon.getEnough());
                                memberCoupon.setShopId((long) 1);
                                memberCoupon.setType(coupon.getType());
                                //有效期状态
                                memberCoupon.setEffective(coupon.getStatus());
                                //券说明
                                memberCoupon.setDescription("抽奖有礼中奖优惠卷：" + coupon.getId() + "，推送节点");
                                memberCoupon.setStatus(1);
                                memberCouponService.save(memberCoupon);
                                EsShopCoupon esShopCoupon = new EsShopCoupon();
                                esShopCoupon.setId(coupon.getId());
                                esShopCoupon.setStock(coupon.getStock() - 1);
                                esShopCouponMapper.updateById(esShopCoupon);
                                map.put("isWinn", 1);//中奖
                                map.put("Winn", prize);
                                esMemberActivatyRecordService.save(record1);
                                //发送模版消息
                              /* String[] attr = activity.getPrizeNotice().split(",");
                               esMember.setFormid(entity.getFormId());
                               esMember.setIds(Long.parseLong(attr[0]));
                               System.out.println("数据打印："+esMember);
                               orderFegin.sendTemplate(esMember.getOpenid(),entity.getFormId(),esMember.getShopId(),attr[0],esMember.getNickname(),esMember.getId());*/
                            } else {
                                System.out.println("库存不足");
                                return new CommonResult().success("success", selIsWinn(entity.getId(), record1));//iswin
                            }
                        }
                        return new CommonResult().success("success", map);
                    } else {
                        //返回未中奖信息
                        return new CommonResult().success("success", selIsWinn(entity.getId(), record1));//iswin
                    }
                } else {
                    return new CommonResult().failed("用户不存在");
                }

            } else {
                return new CommonResult().failed("活动已结束");
            }


        } catch (Exception e) {
            log.error("查询正在进行的抽奖有礼活动异常：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }

    @ApiOperation("查询用户抽奖记录")
    @PostMapping(value = "/selMemberPrize")
    public Object selActivatyApplet(EsMemberActivatyRecord entity) {
        if (ValidatorUtils.empty(entity.getMemberId())) {
            return new CommonResult().failed("用户编号为空");
        }
        if (ValidatorUtils.empty(entity.getActivatyId())) {
            return new CommonResult().failed("活动编号为空");
        }
        return new CommonResult().success("success", esMemberActivatyRecordService.list(new QueryWrapper<>(entity)));
    }

    @ApiOperation("好友赠礼查询")
    @PostMapping("/friendGiftlist")
    public EsShopFriendGift list() {
        try {
            EsShopFriendGift friend = giftService.friend();
            if (friend != null) {
                return friend;
            }
        } catch (Exception e) {
            log.error("好友赠礼查询失败：", e);
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation("送礼卡图")
    @PostMapping(value = "/GiftCard")
    public EsShopFriendGiftCard GiftCard(@RequestParam long id) {
        return giftService.giftcard(id);
    }

    @ApiOperation("查询分享助力活动")
    @PostMapping(value = "/selShareActivaty")
    public Object selShareActivaty(@RequestParam("id") Long id) {
        if (ValidatorUtils.empty(id)) {
            return new CommonResult().failed("请指定活动编号");
        }
        EsShopShare esShopShare = new EsShopShare();
        esShopShare.setId(id);
        EsShopShare share = esShopShareService.getOne(new QueryWrapper<>(esShopShare));
        if (share != null) {
            if (share.getStatus() == 1) {
                int num1 = share.getActivitystartTime().compareTo(new Date());
                int num2 = share.getActivityendTime().compareTo(new Date());
                System.out.println(num1 + " " + num2);
                if (num1 <= 0) {
                    if (num2 > 0) {
                        return new CommonResult().success("success", share);
                    }
                }
            }
        }
        return new CommonResult().failed(3, "活动未开始");
    }

    @ApiOperation("发送中奖模版")
    @PostMapping(value = "/sendTemplate")
    public Object sendTemplate(@RequestParam("memberId") Long memberId, @RequestParam("activatyId") Long activatyId, @RequestParam("formId") String formId) {
        //发送模版消息
        try {
            EsMember esMember = membersFegin.getMemberById(memberId);
            EsShopActivity activity = esShopActivityService.getById(activatyId);
            String[] attr = activity.getPrizeNotice().split(",");
            esMember.setIds(Long.parseLong(attr[0]));
            orderFegin.sendTemplate(esMember.getOpenid(), formId, esMember.getShopId(), attr[0], esMember.getNickname(), esMember.getId());
            return new CommonResult().success("success", "");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }

    }

    public Map<String, Object> selIsWinn(Long id, EsMemberActivatyRecord record1) {
        Map<String, Object> map = new HashMap<String, Object>();
        EsShopActivityPrize activityPrize = new EsShopActivityPrize();
        activityPrize.setActivatyId(id);
        List<EsShopActivityPrize> listPrize = esShopActivityPrizeMapper.selectList(new QueryWrapper<>(activityPrize));
        List<Integer> integers = new ArrayList<Integer>();
        for (EsShopActivityPrize pr : listPrize) {
            integers.add(pr.getLocation());
        }
        map.put("isWinn", 0);//未中奖
        map.put("NoWinn", esShopActivityService.getById(id));
        map.put("Winn", integers);
        record1.setIsWin(0);
        record1.setPrizeLevel(null);
        record1.setPrizeName(null);
        esMemberActivatyRecordService.save(record1);
        return map;
    }


}
