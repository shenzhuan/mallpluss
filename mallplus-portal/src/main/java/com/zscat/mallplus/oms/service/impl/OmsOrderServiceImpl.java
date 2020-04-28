package com.zscat.mallplus.oms.service.impl;

import cn.hutool.http.HttpUtil;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zscat.mallplus.config.ExpressProperties;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.OrderStatus;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.fenxiao.entity.FenxiaoConfig;
import com.zscat.mallplus.fenxiao.entity.FenxiaoRecords;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoConfigMapper;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoRecordsMapper;
import com.zscat.mallplus.oms.entity.*;
import com.zscat.mallplus.oms.mapper.OmsCartItemMapper;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.mapper.OmsOrderReturnApplyMapper;
import com.zscat.mallplus.oms.mapper.OmsOrderSettingMapper;
import com.zscat.mallplus.oms.service.IOmsCartItemService;
import com.zscat.mallplus.oms.service.IOmsOrderItemService;
import com.zscat.mallplus.oms.service.IOmsOrderOperateHistoryService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.*;
import com.zscat.mallplus.pay.controller.alipay.AliPayController;
import com.zscat.mallplus.pay.controller.wxpay.WxPayController;
import com.zscat.mallplus.pms.entity.PmsGifts;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductConsult;
import com.zscat.mallplus.pms.entity.PmsSkuStock;
import com.zscat.mallplus.pms.mapper.PmsSkuStockMapper;
import com.zscat.mallplus.pms.service.IPmsGiftsService;
import com.zscat.mallplus.pms.service.IPmsProductConsultService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.vo.ProductConsultParam;
import com.zscat.mallplus.sms.entity.*;
import com.zscat.mallplus.sms.mapper.SmsGroupMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupRecordMapper;
import com.zscat.mallplus.sms.service.*;
import com.zscat.mallplus.sms.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.ums.entity.*;
import com.zscat.mallplus.ums.mapper.SysAppletSetMapper;
import com.zscat.mallplus.ums.mapper.UmsIntegrationChangeHistoryMapper;
import com.zscat.mallplus.ums.mapper.UmsIntegrationConsumeSettingMapper;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.JsonUtils;
import com.zscat.mallplus.util.applet.TemplateData;
import com.zscat.mallplus.util.applet.WX_TemplateMsgUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.ApplyRefundVo;
import com.zscat.mallplus.vo.CartParam;
import com.zscat.mallplus.vo.Rediskey;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
@Slf4j
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements IOmsOrderService {

    @Resource
    FenxiaoConfigMapper fenxiaoConfigMapper;
    @Resource
    FenxiaoRecordsMapper fenxiaoRecordsMapper;
    @Resource
    private ISmsGroupActivityService smsGroupActivityService;
    @Resource
    private RedisService redisService;
    @Value("${redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;
    @Resource
    private IPmsProductService productService;
    @Resource
    private IUmsMemberReceiveAddressService addressService;
    @Resource
    private IUmsMemberBlanceLogService memberBlanceLogService;
    @Resource
    private WechatApiService wechatApiService;
    @Resource
    private ISmsGroupService groupService;
    @Resource
    private ISmsGroupMemberService groupMemberService;
    @Resource
    private IOmsCartItemService cartItemService;
    @Resource
    private ISmsCouponService couponService;
    @Resource
    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
    @Autowired
    private ISmsFlashPromotionProductRelationService smsFlashPromotionProductRelationService;
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private ISmsCouponHistoryService couponHistoryService;
    @Resource
    private IOmsOrderService orderService;
    @Resource
    private SmsGroupRecordMapper groupRecordMapper;
    @Autowired
    private IPmsProductConsultService pmsProductConsultService;
    @Resource
    private IOmsOrderItemService orderItemService;
    @Resource
    private OmsOrderMapper orderMapper;
    @Resource
    private SmsGroupMemberMapper groupMemberMapper;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private OmsOrderSettingMapper orderSettingMapper;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private OmsCartItemMapper cartItemMapper;
    @Resource
    private SmsGroupMapper groupMapper;
    @Resource
    private IPmsGiftsService giftsService;
    @Resource
    private SysAppletSetMapper appletSetMapper;
    @Resource
    private UmsIntegrationChangeHistoryMapper integrationChangeHistoryMapper;
    @Autowired
    private ISmsBasicGiftsService basicGiftsService;
    @Autowired
    private ISmsBasicMarkingService basicMarkingService;
    @Autowired
    private IOmsOrderOperateHistoryService orderOperateHistoryService;
    @Autowired
    private IPmsGiftsService pmsGiftsService;
    @Resource
    private OmsOrderReturnApplyMapper orderReturnApplyMapper;

    //请求url
    private String ReqURL = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

    private ExpressProperties properties;

    public ExpressProperties getProperties() {
        return properties;
    }

    public void setProperties(ExpressProperties properties) {
        this.properties = properties;
    }
    @Override
    public int payOrder(TbThanks tbThanks) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        tbThanks.setTime(time);
        tbThanks.setDate(new Date());

        //设置订单为已付款
        OmsOrder tbOrder = orderMapper.selectById(tbThanks.getOrderId());
        if (tbOrder == null) {
            throw new ApiMallPlusException("订单不存在");
        }
        tbOrder.setStatus(OrderStatus.TO_DELIVER.getValue());
        tbOrder.setPayType(tbThanks.getPayType());
        tbOrder.setPaymentTime(new Date());
        tbOrder.setModifyTime(new Date());
        if (orderMapper.updateById(tbOrder) != 1) {
            throw new ApiMallPlusException("更新订单失败");
        }
        //恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderItem queryO = new OmsOrderItem();
        queryO.setOrderId(tbThanks.getOrderId());
        List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));

        int count = orderMapper.updateSkuStock(list);
        //发送通知确认邮件
        String tokenName = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString();

        // emailUtil.sendEmailDealThank(EMAIL_SENDER,"【mallcloud商城】支付待审核处理",tokenName,token,tbThanks);
        return count;
    }

    @Override
    public Object preGroupActivityOrder(OrderParam orderParam) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        SmsGroupActivity smsGroupActivity = smsGroupActivityService.getById(orderParam.getGroupId());
        if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
            List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                    Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            if (productList != null && productList.size() > 0) {

                ConfirmOrderResult result = new ConfirmOrderResult();
                // 邮费
                BigDecimal transFee = BigDecimal.ZERO;
                //获取购物车信息
                List<OmsCartItem> list = new ArrayList<>();
                for (PmsProduct product : productList) {
                    if (product == null && product.getId() == null) {
                        return new CommonResult().failed("库存不够");
                    }
                    if (product != null && product.getStock() < 1) {
                        return new CommonResult().failed("库存不够");
                    }
                    OmsCartItem omsCartItem = new OmsCartItem();
                    omsCartItem.setProductId(product.getId());
                    omsCartItem.setPrice(product.getPrice());
                    omsCartItem.setChecked(1);
                    omsCartItem.setCreateDate(new Date());
                    omsCartItem.setProductBrand(product.getBrandName());
                    omsCartItem.setProductCategoryId(product.getProductCategoryId());
                    omsCartItem.setProductName(product.getName());
                    omsCartItem.setProductSn(product.getProductSn());
                    omsCartItem.setQuantity(1);
                    omsCartItem.setProductPic(product.getPic());
                    list.add(omsCartItem);
                }
                result.setCartPromotionItemList(list);
                //获取用户收货地址列表
                UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
                if (currentMember == null) {
                    return new CommonResult().fail(100);
                }
                queryU.setMemberId(currentMember.getId());
                List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
                result.setMemberReceiveAddressList(memberReceiveAddressList);
                UmsMemberReceiveAddress address = addressService.getDefaultItem();
                if (address==null && memberReceiveAddressList!=null && memberReceiveAddressList.size()>0){
                    address=memberReceiveAddressList.get(0);
                }
                //获取用户可用优惠券列表
                List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(list, 1);
                result.setCouponHistoryDetailList(couponHistoryDetailList);
                //获取用户积分
                result.setMemberIntegration(currentMember.getIntegration());
                result.setBlance(currentMember.getBlance());

                //获取积分使用规则
                UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
                result.setIntegrationConsumeSetting(integrationConsumeSetting);
                //计算总金额、活动优惠、应付金额
                if (list != null && list.size() > 0) {
                    ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(list, smsGroupActivity.getTransfee(), smsGroupActivity.getPrice());
                    result.setCalcAmount(calcAmount);
                    result.setAddress(address);
                    smsGroupActivity.setProductList(null);
                    result.setGroupActivity(smsGroupActivity);
                    return new CommonResult().success(result);
                }
                return null;
            }
        }
        return null;
    }

    private List<OmsCartItem> goodsToCartList(List<PmsProduct> productList) {
        List<OmsCartItem> cartItems = new ArrayList<>();
        for (PmsProduct product : productList) {
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setProductId(product.getId());
            omsCartItem.setPrice(product.getPrice());
            omsCartItem.setChecked(1);
            omsCartItem.setCreateDate(new Date());
            omsCartItem.setProductBrand(product.getBrandName());
            omsCartItem.setProductCategoryId(product.getProductCategoryId());
            omsCartItem.setProductName(product.getName());
            omsCartItem.setProductSn(product.getProductSn());
            omsCartItem.setQuantity(1);
            omsCartItem.setProductPic(product.getPic());
            cartItems.add(omsCartItem);
        }
        return cartItems;
    }

    /**
     * @return
     */
    @Override
    public Object couponHistoryDetailList(OrderParam orderParam) {

        String type = orderParam.getType();
        UmsMember currentMember = memberService.getNewCurrentMember();
        List<OmsCartItem> list = new ArrayList<>();
        if ("3".equals(type)) { // 1 商品详情 2 勾选购物车 3全部购物车的商品
            list = cartItemService.list(currentMember.getId(), null);
        } else if ("1".equals(type)) {
            OmsCartItem omsCartItem = getOmsCartItemByGoodsDetail(orderParam, currentMember);
            if (omsCartItem == null) {
                return null;
            }
            list.add(omsCartItem);
        } else if ("2".equals(type)) {
            String cart_id_list1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cart_id_list1)) {
                throw new ApiMallPlusException("参数为空");
            }
            String[] ids1 = cart_id_list1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            list = cartItemService.list(currentMember.getId(), resultList);
        } else if ("6".equals(type)) { // 秒杀
            SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
            PmsProduct product = productService.getById(relation.getProductId());
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setQuantity(orderParam.getTotal());
            if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                throw new ApiMallPlusException("超过秒杀个数！");
            }
            omsCartItem.setPrice(relation.getFlashPromotionPrice());
            omsCartItem.setProductBrand(product.getBrandId() + "");
            omsCartItem.setProductCategoryId(product.getProductCategoryId());
            omsCartItem.setProductName(product.getName());
            omsCartItem.setProductPic(product.getPic());
            omsCartItem.setProductId(product.getId());
            omsCartItem.setChecked(1);
            omsCartItem.setProductSn(product.getProductSn());

            list.add(omsCartItem);
        }

        //获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(list, 1);
        return couponHistoryDetailList;
    }

    /**
     * @return
     */
    @Override
    public Object submitPreview(OrderParam orderParam) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        String type = orderParam.getType();
        StopWatch stopWatch = new StopWatch("下单orderType=" + orderParam.getOrderType());
        stopWatch.start("1. 获取购物车商品");

        List<OmsCartItem> list = new ArrayList<>();
        if ("3".equals(type)) { // 1 商品详情 2 勾选购物车 3全部购物车的商品
            list = cartItemService.list(currentMember.getId(), null);
        } else if ("1".equals(type)) {
            OmsCartItem omsCartItem = getOmsCartItemByGoodsDetail(orderParam, currentMember);
            if (omsCartItem == null) {
                return null;
            }
            list.add(omsCartItem);
        } else if ("2".equals(type)) {
            String cart_id_list1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cart_id_list1)) {
                throw new ApiMallPlusException("参数为空");
            }
            String[] ids1 = cart_id_list1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            list = cartItemService.list(currentMember.getId(), resultList);
        } else if ("6".equals(type)) { // 秒杀
            SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
            PmsProduct product = productService.getById(relation.getProductId());
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setQuantity(orderParam.getTotal());
            if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                throw new ApiMallPlusException("超过秒杀个数！");
            }
            omsCartItem.setPrice(relation.getFlashPromotionPrice());
            omsCartItem.setProductBrand(product.getBrandId() + "");
            omsCartItem.setProductCategoryId(product.getProductCategoryId());
            omsCartItem.setProductName(product.getName());
            omsCartItem.setProductPic(product.getPic());
            omsCartItem.setProductId(product.getId());
            omsCartItem.setChecked(1);
            omsCartItem.setProductSn(product.getProductSn());

            list.add(omsCartItem);
        }
        if (list == null && list.size() < 1) {
            throw new ApiMallPlusException("订单已提交");
        }
        List<OmsCartItem> newCartList = new ArrayList<>();
        // 取商品大的运费
        BigDecimal transFee = BigDecimal.ZERO;
        for (OmsCartItem cart : list) {
            PmsProduct goods = productService.getById(cart.getProductId());
            if (goods != null && goods.getStock() > 0 && goods.getStock() >= cart.getQuantity()) {
                if (goods.getTransfee().compareTo(transFee) > 0) {
                    transFee = goods.getTransfee();
                }
            }
            newCartList.add(cart);
        }
        stopWatch.stop();
        stopWatch.start("赠品营销计算");
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        CartMarkingVo vo = new CartMarkingVo();
        vo.setCartList(newCartList);
        //是否首单
        int firstOrder = orderMapper.selectCount(new QueryWrapper<OmsOrder>().eq("member_id", currentMember.getId()));
        vo.setType(1);
        if (firstOrder > 0) {
            vo.setType(2);
        }
       /* List<SmsBasicGifts> basicGiftsList = basicGiftsService.matchOrderBasicGifts(vo);
        log.info(com.alibaba.fastjson.JSONObject.toJSONString(basicGiftsList));
        result.setBasicGiftsList(basicGiftsList);*/
        stopWatch.stop();
        stopWatch.start("其他数据计算");
        result.setCartPromotionItemList(newCartList);
        //获取用户收货地址列表
        UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
        queryU.setMemberId(currentMember.getId());
        List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        UmsMemberReceiveAddress address = addressService.getDefaultItem();
        if (address==null && memberReceiveAddressList!=null && memberReceiveAddressList.size()>0){
            address=memberReceiveAddressList.get(0);
        }
        //获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(newCartList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        //获取用户积分

        result.setBlance(currentMember.getBlance());
        result.setOffline(2);
        //获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectOne(new QueryWrapper<>());

        if (integrationConsumeSetting != null && currentMember.getIntegration() > 0) {
            result.setMemberIntegration(currentMember.getIntegration() * integrationConsumeSetting.getMaxPercentPerOrder() / 100);
            result.setIntegrationAmount(BigDecimal.valueOf((currentMember.getIntegration() * integrationConsumeSetting.getMaxPercentPerOrder() / 100 / integrationConsumeSetting.getDeductionPerAmount())));
        }

        //计算总金额、活动优惠、应付金额
        if (list != null && list.size() > 0) {
            ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(newCartList, transFee, BigDecimal.ZERO);
            result.setCalcAmount(calcAmount);
            result.setAddress(address);
            return result;
        }
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return null;

    }

    /**
     * @return
     */
    @Override
    public Object submitStorePreview(OrderParam orderParam) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        ConfirmListOrderResult confirmListOrderResult = new ConfirmListOrderResult();
        List<ConfirmOrderResult> confirmOrderResultList = new ArrayList<>();
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }

        List<OmsCartItem> list = new ArrayList<>();

        String cart_id_list1 = orderParam.getCartIds();
        if (org.apache.commons.lang.StringUtils.isBlank(cart_id_list1)) {
            throw new ApiMallPlusException("参数为空");
        }
        String[] ids1 = cart_id_list1.split(",");
        List<Long> resultList = new ArrayList<>(ids1.length);
        for (String s : ids1) {
            resultList.add(Long.valueOf(s));
        }
        list = cartItemService.list(currentMember.getId(), resultList);

        if (list == null && list.size() < 1) {
            throw new ApiMallPlusException("订单已提交");
        }
        BigDecimal totalPayAmount = BigDecimal.ZERO;
        Map<Integer, List<OmsCartItem>> map = list.stream().collect(Collectors.groupingBy(OmsCartItem::getStoreId));

        for (Map.Entry<Integer, List<OmsCartItem>> entry : map.entrySet()) {
            List<OmsCartItem> subList = entry.getValue();
            List<OmsCartItem> newCartList = new ArrayList<>();
            // 取商品大的运费
            BigDecimal transFee = BigDecimal.ZERO;
            ConfirmOrderResult result = new ConfirmOrderResult();
            for (OmsCartItem cart : subList) {
                PmsProduct goods = productService.getById(cart.getProductId());
                if (goods != null && goods.getStock() > 0 && goods.getStock() >= cart.getQuantity()) {
                    if (goods.getTransfee().compareTo(transFee) > 0) {
                        transFee = goods.getTransfee();
                    }
                }
                newCartList.add(cart);
                result.setStoreName(cart.getStoreName());
            }


            //获取购物车信息
            CartMarkingVo vo = new CartMarkingVo();
            vo.setCartList(newCartList);
            //是否首单
            int firstOrder = orderMapper.selectCount(new QueryWrapper<OmsOrder>().eq("member_id", currentMember.getId()));
            vo.setType(1);
            if (firstOrder > 0) {
                vo.setType(2);
            }
            List<SmsBasicGifts> basicGiftsList = basicGiftsService.matchOrderBasicGifts(vo);
            result.setBasicGiftsList(basicGiftsList);
            result.setCartPromotionItemList(newCartList);
            if (map.size() > 1) {
                result.setOffline(1);
            } else {
                result.setOffline(2);
            }
            //获取用户可用优惠券列表
            List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(newCartList, 1);
            result.setCouponHistoryDetailList(couponHistoryDetailList);

            //计算总金额、活动优惠、应付金额
            if (newCartList != null && newCartList.size() > 0) {
                ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(newCartList, transFee, BigDecimal.ZERO);
                result.setCalcAmount(calcAmount);
                confirmOrderResultList.add(result);
                totalPayAmount = totalPayAmount.add(result.getCalcAmount().getPayAmount());
            }

        }
        //   }
        confirmListOrderResult.setConfirmOrderResultList(confirmOrderResultList);
        //获取用户收货地址列表
        UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
        queryU.setMemberId(currentMember.getId());
        List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
        confirmListOrderResult.setMemberReceiveAddressList(memberReceiveAddressList);
        UmsMemberReceiveAddress address = addressService.getDefaultItem();
        if (address==null && memberReceiveAddressList!=null && memberReceiveAddressList.size()>0){
            address=memberReceiveAddressList.get(0);
        }
        confirmListOrderResult.setAddress(address);
        confirmListOrderResult.setBlance(currentMember.getBlance());
        confirmListOrderResult.setTotalPayAmount(totalPayAmount);
        return confirmListOrderResult;

    }

    @Override
    public CommonResult generateOrder(OrderParam orderParam) {

        String type = orderParam.getType();
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        OmsOrder order = new OmsOrder();
        // 1. 获取购物车商品
        List<OmsCartItem> cartPromotionItemList = new ArrayList<>();
        StopWatch stopWatch = new StopWatch("下单orderType=" + orderParam.getOrderType());
        stopWatch.start("1. 获取购物车商品");
        if (orderParam.getOrderType() != 7 && ValidatorUtils.empty(orderParam.getAddressId())) {
            return new CommonResult().failed("address is null");
        }
        //团购订单
        SmsGroupActivity smsGroupActivity = null;
        if (orderParam.getOrderType() == 3) {
            smsGroupActivity = smsGroupActivityService.getById(orderParam.getGroupActivityId());
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                        Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                if (productList != null && productList.size() > 0) {
                    order.setFreightAmount(smsGroupActivity.getTransfee());
                    //获取购物车信息
                    cartPromotionItemList = goodsToCartList(productList);
                }
            }
        } else {
            if ("3".equals(type)) { // 1 商品详情 2 勾选购物车 3全部购物车的商品
                cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
            }
            if ("1".equals(type)) {
                if (ValidatorUtils.empty(orderParam.getTotal())) {
                    orderParam.setTotal(1);
                }
                OmsCartItem omsCartItem = getOmsCartItemByGoodsDetail(orderParam, currentMember);
                List<OmsCartItem> list = new ArrayList<>();
                if (omsCartItem != null) {
                    list.add(omsCartItem);
                } else {
                    throw new ApiMallPlusException("订单已提交");
                }
                if (!CollectionUtils.isEmpty(list)) {
                    cartPromotionItemList = cartItemService.calcCartPromotion(list);
                }
            } else if ("2".equals(type)) {
                String cart_id_list1 = orderParam.getCartIds();
                String[] ids1 = cart_id_list1.split(",");
                List<Long> resultList = new ArrayList<>(ids1.length);
                for (String s : ids1) {
                    resultList.add(Long.valueOf(s));
                }
                cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), resultList);
            } else if ("6".equals(type)) { // 秒杀
                SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
                PmsProduct product = productService.getById(relation.getProductId());
                OmsCartItem omsCartItem = new OmsCartItem();
                if (ValidatorUtils.empty(orderParam.getTotal())) {
                    orderParam.setTotal(1);
                }
                omsCartItem.setQuantity(orderParam.getTotal());
                if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                    throw new ApiMallPlusException("超过秒杀个数！");
                }
                omsCartItem.setPrice(relation.getFlashPromotionPrice());
                omsCartItem.setProductBrand(product.getBrandId() + "");
                omsCartItem.setProductCategoryId(product.getProductCategoryId());
                omsCartItem.setProductName(product.getName());
                omsCartItem.setProductPic(product.getPic());
                omsCartItem.setProductId(product.getId());
                omsCartItem.setChecked(1);
                omsCartItem.setProductSn(product.getProductSn());

                cartPromotionItemList.add(omsCartItem);
            }
        }
        stopWatch.stop();
        if (cartPromotionItemList == null || cartPromotionItemList.size() < 1) {
            return new CommonResult().failed("没有下单的商品");
        }
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        //获取购物车及优惠信息
        String name = "";
        BigDecimal transFee = BigDecimal.ZERO;
        List<OmsCartItem> newCartItemList = new ArrayList<>();
        Integer isFirst = 1;
        stopWatch.start("2. 校验商品库存，舍弃商品不存或没有库存 计算运费");
        BigDecimal totalAmount = BigDecimal.ZERO;

        BigDecimal vipPrice = BigDecimal.ZERO; // 会员折扣优惠 total
        BigDecimal singVipPrice = BigDecimal.ZERO; // 会员折扣优惠
        int memberRate = 10;
        UmsMemberLevel memberLevel = memberLevelService.getById(currentMember.getMemberLevelId());
        if (memberLevel != null && memberLevel.getPriviledgeMemberPrice() > 0) {
            memberRate = memberLevel.getPriviledgeMemberPrice();
        }
        // 2. 校验商品库存，舍弃商品不存或没有库存 计算运费
        for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
            boolean flag = false;
            PmsProduct goods = productService.getById(cartPromotionItem.getProductId());
            if (!ValidatorUtils.empty(cartPromotionItem.getProductSkuId()) && cartPromotionItem.getProductSkuId() > 0) {
                if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() > cartPromotionItem.getQuantity()) {
                    PmsSkuStock skuStock = skuStockMapper.selectById(cartPromotionItem.getProductSkuId());
                    if (skuStock.getStock() > 0 && skuStock.getStock() >= cartPromotionItem.getQuantity()) {
                        totalAmount = totalAmount.add(skuStock.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
                        flag = true;
                        if (goods.getIsVip() != null && goods.getIsVip() == 1) {
                            singVipPrice = skuStock.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10))).setScale(2);
                            vipPrice = vipPrice.add(skuStock.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10)))).setScale(2);
                        }
                    }
                }
            } else {
                if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() >= cartPromotionItem.getQuantity()) {
                    totalAmount = totalAmount.add(goods.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
                    flag = true;
                    if (goods.getIsVip() != null && goods.getIsVip() == 1) {
                        singVipPrice = goods.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10))).setScale(2);
                        vipPrice = vipPrice.add(goods.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10)))).setScale(2);
                    }
                }
            }
            if (flag) {
                if (goods.getTransfee().compareTo(transFee) > 0) {
                    transFee = goods.getTransfee();
                }
                //生成下单商品信息
                OmsOrderItem orderItem = createOrderItem(cartPromotionItem);
                orderItem.setStatus(OrderStatus.TO_DELIVER.getValue());
                orderItem.setType(AllEnum.OrderItemType.GOODS.code());
                orderItem.setPromotionAmount(singVipPrice);
                orderItem.setRealAmount(goods.getPrice());
                orderItem.setIsFenxiao(goods.getIsFenxiao());
                orderItemList.add(orderItem);
                if (isFirst == 1) {
                    name = cartPromotionItem.getProductName();
                    order.setGoodsId(cartPromotionItem.getProductId());
                    order.setGoodsName(cartPromotionItem.getProductName());
                    order.setStoreId(goods.getStoreId());
                    order.setStoreName(goods.getStoreName());
                }
                isFirst++;
                newCartItemList.add(cartPromotionItem);
            }
        }
        if (newCartItemList == null || newCartItemList.size() < 1) {
            return new CommonResult().failed("没有下单的商品");
        }

        //3.计算优惠券
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        SmsCoupon coupon = null;
        if (orderParam.getCouponId() != null) {
            //   couponHistory = couponHistoryService.getById(orderParam.getMemberCouponId());
            coupon = couponService.getById(orderParam.getCouponId());
        }

        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额

        createOrderObj(order, orderParam, currentMember, orderItemList, orderParam.getAddressId());
        if (smsGroupActivity != null) {
            order.setTotalAmount(smsGroupActivity.getPrice());
        }
        if (orderParam.getOrderType() != 3) {
            order.setFreightAmount(transFee);
        }
        if (orderParam.getCouponId() == null || orderParam.getCouponId() == 0) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(coupon.getAmount());
        }
        //获取积分使用规则
       /* UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectOne(new QueryWrapper<>());

        if (integrationConsumeSetting!=null && currentMember.getIntegration()>0){
            order.setUseIntegration(currentMember.getIntegration()*integrationConsumeSetting.getMaxPercentPerOrder()/100);
            order.setIntegrationAmount(BigDecimal.valueOf((currentMember.getIntegration()*integrationConsumeSetting.getMaxPercentPerOrder()/100/integrationConsumeSetting.getDeductionPerAmount())));
        }*/
        CartMarkingVo vo = new CartMarkingVo();
        vo.setCartList(newCartItemList);
        SmsBasicMarking basicMarking = basicMarkingService.matchOrderBasicMarking(vo);
        log.info("basicMarking=" + com.alibaba.fastjson.JSONObject.toJSONString(basicMarking));
        if (basicMarking != null) {
            order.setPromotionAmount(basicMarking.getMinAmount());
        }
        order.setVipAmount(vipPrice);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(calcPayAmount(order));
        if (order.getPayAmount().compareTo(BigDecimal.ZERO) <= 0) {
            order.setPayAmount(new BigDecimal("0.01"));
        }
        stopWatch.stop();

        stopWatch.start("3.计算赠品营销");
        if (ValidatorUtils.notEmpty(orderParam.getBasicGiftsVar())) {
            insertGiftOrderItem(orderParam, orderItemList);
        }

        stopWatch.stop();
        stopWatch.start("4.计算优惠券 插入订单");
        // TODO: 2018/9/3 bill_*,delivery_*
        //插入order表和order_item表
        orderService.save(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
            if (ValidatorUtils.notEmpty(orderParam.getSkillId())) {
                orderItem.setGiftIntegration(orderParam.getSkillId().intValue());
            }
        }
        orderItemService.saveBatch(orderItemList);

        //如使用优惠券更新优惠券使用状态
        if (orderParam.getCouponId() != null && orderParam.getCouponId() > 0) {
            couponHistory.setId(orderParam.getMemberCouponId());
            couponHistory.setUseStatus(1);
            couponHistory.setUseTime(new Date());
            couponHistory.setOrderId(order.getId());
            couponHistory.setOrderSn(order.getOrderSn());
            couponHistoryService.updateById(couponHistory);
        }
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(order.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(-1);
        history.setOrderStatus(OrderStatus.INIT.getValue());
        history.setNote("创建订单");
        orderOperateHistoryService.save(history);



        stopWatch.stop();
        //如使用积分需要扣除积分
        if (order.getUseIntegration() != null) {
            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - order.getUseIntegration());
        }
        stopWatch.start("5.锁库存 删除购物车");
        lockStockByOrder(orderItemList, type);
        //删除购物车中的下单商品
        if (!"1".equals(orderParam.getType())){
            deleteCartItemList(cartPromotionItemList, currentMember);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        stopWatch.stop();
        if (ValidatorUtils.empty(orderParam.getFormId())) {
            stopWatch.start("5.小程序推送消息");
            push(currentMember, order, orderParam.getPage(), orderParam.getFormId(), name);
            stopWatch.stop();
        }
        log.info(stopWatch.prettyPrint());
        return new CommonResult().success("下单成功", result);
    }

    private void insertGiftOrderItem(OrderParam orderParam, List<OmsOrderItem> orderItemList) {
        String[] basicGiftsList = orderParam.getBasicGiftsVar().split("@");
        if (basicGiftsList != null && basicGiftsList.length > 0) {
            for (String basicGifts : basicGiftsList) {
                if (ValidatorUtils.notEmpty(basicGifts)) {
                    String[] beanKv = basicGifts.split(":");
                    if (beanKv != null && beanKv.length > 1) {
                        String[] ids = beanKv[1].split(",");
                        if (ids != null && ids.length > 0) {
                            for (String id : ids) {
                                PmsGifts pmsGifts = pmsGiftsService.getById(id);
                                if (pmsGifts != null) {
                                    OmsOrderItem orderItem = new OmsOrderItem();
                                    orderItem.setOrderSn(beanKv[0]);
                                    orderItem.setProductId(pmsGifts.getId());
                                    orderItem.setProductName(pmsGifts.getTitle());
                                    orderItem.setProductPic(pmsGifts.getIcon());
                                    orderItem.setProductPrice(pmsGifts.getPrice());
                                    orderItem.setProductQuantity(1);
                                    orderItem.setType(AllEnum.OrderItemType.GIFT.code());

                                    orderItemList.add(orderItem);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Object quitGroup(Long id) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        groupMemberMapper.deleteById(id);

        return new CommonResult().success("下单成功");
    }

    // 分拥计算
    @Override
    public void recordFenxiaoMoney(List<OmsOrderItem> list, UmsMember currentMember) {
        // 佣金计算
        for (OmsOrderItem cartPromotionItem : list) {
            if (ValidatorUtils.notEmpty(cartPromotionItem.getIsFenxiao()) && cartPromotionItem.getIsFenxiao() == 1) {
                FenxiaoConfig fenxiaoConfig = fenxiaoConfigMapper.selectById(cartPromotionItem.getStoreId());
                // 商品分佣
                if (fenxiaoConfig != null && fenxiaoConfig.getStatus() == 1 && ValidatorUtils.notEmpty(cartPromotionItem.getInviteMemberId())
                        && cartPromotionItem.getInviteMemberId() > 0 && fenxiaoConfig.getOnePercent() > 0) {
                    //一级 分销
                    FenxiaoRecords records1 = new FenxiaoRecords();
                    records1.setCreateTime(new Date());
                    records1.setStatus("1");
                    records1.setGoodsId(cartPromotionItem.getProductId());
                    if (ValidatorUtils.empty(cartPromotionItem.getStoreId())) {
                        throw new ApiMallPlusException("商品编号不存在");
                    }
                    records1.setStoreId(cartPromotionItem.getStoreId());
                    records1.setLevel("1");
                    records1.setType(fenxiaoConfig.getType());
                    records1.setMemberId(cartPromotionItem.getInviteMemberId());
                    records1.setInviteId(currentMember.getId());
                    records1.setOrderId(cartPromotionItem.getOrderId());
                    records1.setMoney(cartPromotionItem.getProductPrice().multiply(new BigDecimal(cartPromotionItem.getProductQuantity()).multiply(new BigDecimal(fenxiaoConfig.getOnePercent()))).divide(BigDecimal.valueOf(100)));
                    fenxiaoRecordsMapper.insert(records1);
                }
                if (fenxiaoConfig != null && fenxiaoConfig.getStatus() == 1 && ValidatorUtils.notEmpty(currentMember.getInvitecode()) && fenxiaoConfig.getOnePercent() > 0) {
                    //  UmsMember member = memberService.getById(currentMember.getInvitecode());
                    //一级 分销
                    FenxiaoRecords records1 = new FenxiaoRecords();
                    records1.setCreateTime(new Date());
                    records1.setStatus("1");
                    records1.setGoodsId(cartPromotionItem.getProductId());
                    records1.setStoreId(cartPromotionItem.getStoreId());
                    records1.setLevel("1");
                    records1.setType(fenxiaoConfig.getType());
                    records1.setMemberId(Long.valueOf(currentMember.getInvitecode()));
                    records1.setInviteId(currentMember.getId());
                    records1.setOrderId(cartPromotionItem.getOrderId());
                    records1.setMoney(cartPromotionItem.getProductPrice().multiply(new BigDecimal(cartPromotionItem.getProductQuantity()).multiply(new BigDecimal(fenxiaoConfig.getOnePercent()))).divide(BigDecimal.valueOf(100)));
                    fenxiaoRecordsMapper.insert(records1);
                    //二级 分销
                    UmsMember member = memberService.getById(currentMember.getInvitecode());
                    if (member != null && ValidatorUtils.notEmpty(member.getInvitecode()) && fenxiaoConfig.getTwoPercent() > 0) {
                        FenxiaoRecords records = new FenxiaoRecords();
                        records.setCreateTime(new Date());
                        records.setStatus("1");
                        records.setGoodsId(cartPromotionItem.getProductId());
                        records.setStoreId(cartPromotionItem.getStoreId());
                        records.setLevel("2");
                        records.setType(fenxiaoConfig.getType());
                        records.setMemberId(Long.valueOf(member.getInvitecode()));
                        records.setInviteId(member.getId());
                        records.setOrderId(cartPromotionItem.getOrderId());
                        records.setMoney(cartPromotionItem.getProductPrice().multiply(new BigDecimal(cartPromotionItem.getProductQuantity()).multiply(new BigDecimal(fenxiaoConfig.getTwoPercent()))).divide(BigDecimal.valueOf(100)));
                        fenxiaoRecordsMapper.insert(records);
                    }
                }
            }

        }
    }
    @Override
    @Transactional(readOnly = true)
    @Cacheable("transitSteps")
    public List<Map<String, String>> getTransitSteps(String deliveryCorpCode, String trackingNo) {
     //   Setting setting = SystemUtils.getSetting();
        String kuaidi100Customer = "setting.getKuaidi100Customer()";
        String kuaidi100Key = "setting.getKuaidi100Key()";
        if (org.apache.commons.lang.StringUtils.isEmpty(kuaidi100Customer) || org.apache.commons.lang.StringUtils.isEmpty(kuaidi100Key) || org.apache.commons.lang.StringUtils.isEmpty(deliveryCorpCode) || org.apache.commons.lang.StringUtils.isEmpty(trackingNo)) {
            return Collections.emptyList();
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("com", deliveryCorpCode);
        paramMap.put("num", trackingNo);
        String param = JsonUtils.objectToJson(paramMap);
        String sign = DigestUtils.md5Hex(param + kuaidi100Key + kuaidi100Customer).toUpperCase();

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("customer", kuaidi100Customer);
        parameterMap.put("sign", sign);
        parameterMap.put("param", param);
        String content = HttpUtil.post("http://poll.kuaidi100.com/poll/query.do", parameterMap);
        Map<String, Object> data = JsonUtils.readJsonToMap(content);

        if (!org.apache.commons.lang.StringUtils.equals(String.valueOf(data.get("message")), "ok")) {
            return Collections.emptyList();
        }
        return (List<Map<String, String>>) data.get("data");
    }

    @Resource
    private WxPayController wxPayController;
    @Resource
    private AliPayController aliPayController;

    @Transactional
    @Override
    public void refund(OmsOrderReturnApply orderReturnApply) {
        if (orderReturnApply.getType()==1 ||orderReturnApply.getType()==3){
            OmsOrder order = orderMapper.selectById(orderReturnApply.getOrderId());
            if (order!=null ) {
                if (order.getPayType().equals(AllEnum.OrderPayType.weixinAppletPay.code())) {
                    wxPayController.refund(null, order.getOrderSn());
                } else if (order.getPayType().equals(AllEnum.OrderPayType.alipay.code())) {
                    AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                    model.setOutTradeNo(order.getOrderSn());
                    model.setTradeNo(order.getOrderSn());
                    model.setRefundAmount(orderReturnApply.getReturnAmount().toPlainString());
                    model.setRefundReason(orderReturnApply.getReason());
                    aliPayController.tradeRefund(model);
                } else if (order.getPayType().equals(AllEnum.OrderPayType.balancePay.code())) {
                    UmsMember userDO =memberService.getById(order.getMemberId());
                    userDO.setBlance(userDO.getBlance().add(orderReturnApply.getReturnAmount()));
                    memberService.updateById(userDO);
                }
                order.setStatus(OrderStatus.REFUND.getValue());
                order.setModifyTime(new Date());
                orderMapper.updateById(order);
                if (order.getOrderType().equals(AllEnum.OrderType.PIN_GROUP.code())){
                    SmsGroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<SmsGroupMember>().eq("order_id",order.getId()));
                    groupMember.setStatus(1);
                    groupMemberMapper.updateById(groupMember);
                    SmsGroupRecord record = groupRecordMapper.selectById(groupMember.getGroupRecordId());
                    record.setStatus("1");
                    groupRecordMapper.updateById(record);
                }


            }
        }

    }

    @Override
    public ExpressInfo getExpressInfo(String orderCode, String shipperCode, String logisticCode) {
        try {
            String result = getOrderTracesByJson(orderCode,shipperCode, logisticCode);
            ObjectMapper objMap = new ObjectMapper();
            ExpressInfo ei = objMap.readValue(result, ExpressInfo.class);
            ei.setShipperName(getVendorName(shipperCode));
            return ei;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    private String getOrderTracesByJson(String OrderCode,String ShipperCode, String LogisticCode) throws Exception {
        if (!properties.isEnable()) {
            return null;
        }
        String requestData = "{'OrderCode':'"+OrderCode+"','ShipperCode':'" + ShipperCode + "','LogisticCode':'" + LogisticCode + "'}";
        Map<String, Object> params = new HashMap<>();
        params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getAppId());
        params.put("RequestType", "1002");
        String dataSign = encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = HttpUtil.post(ReqURL, params);
        //根据公司业务处理返回的信息......
        return result;
    }
    /**
     * Sign签名生成
     *
     * @param content  内容
     * @param keyValue Appkey
     * @param charset  编码方式
     * @return DataSign签名
     */
    private String encrypt(String content, String keyValue, String charset) {
        if (keyValue != null) {
            content = content + keyValue;
        }
        byte[] src;
        try {
            src = MD5(content, charset).getBytes(charset);
            return Base64Utils.encodeToString(src);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
    /**
     * MD5加密
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws Exception
     */
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }
    /**
     * 获取物流供应商名
     *
     * @param vendorCode
     * @return
     */
    public String getVendorName(String vendorCode) {
        for (Map<String, String> item : properties.getVendors()) {
            if (item.get("code").equals(vendorCode))
                return item.get("name");
        }
        return null;
    }
    @Override
    public CommonResult acceptGroup(OrderParam orderParam) {

        OmsOrder order = new OmsOrder();
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        List<OmsCartItem> list = new ArrayList<>();
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        OmsCartItem cartItem = new OmsCartItem();
        PmsProduct pmsProduct = productService.getById(orderParam.getGoodsId());
        createCartObj(orderParam, list, cartItem, pmsProduct);
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        //获取购物车及优惠信息
        String name = "";
        for (OmsCartItem cartPromotionItem : list) {
            PmsProduct goods = productService.getById(cartPromotionItem.getProductId());
            if (!ValidatorUtils.empty(cartPromotionItem.getProductSkuId()) && cartPromotionItem.getProductSkuId() > 0) {
                checkGoods(goods, false, cartPromotionItem.getQuantity());
                PmsSkuStock skuStock = skuStockMapper.selectById(cartPromotionItem.getProductSkuId());
                checkSkuGoods(skuStock, cartPromotionItem.getQuantity());
            } else {
                checkGoods(goods, true, cartPromotionItem.getQuantity());
            }
            //生成下单商品信息
            OmsOrderItem orderItem = createOrderItem(cartPromotionItem);
            orderItem.setRealAmount(goods.getPrice());
            orderItem.setType(AllEnum.OrderItemType.GOODS.code());
            orderItem.setStatus(OrderStatus.TO_DELIVER.getValue());
            orderItemList.add(orderItem);
        }
        //进行库存锁定
        lockStock(list);
        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额

        createOrderObj(order, orderParam, currentMember, orderItemList, orderParam.getAddressId());
        order.setMemberId(currentMember.getId());
        SmsGroup group = groupMapper.getByGoodsId(orderParam.getGoodsId());
        Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()), "yyyy-MM-dd HH:mm:ss");

        // TODO: 2018/9/3 bill_*,delivery_*
        //插入order表和order_item表
        orderService.save(order);
        Long nowT = System.currentTimeMillis();
        if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
            SmsGroupMember groupMember = new SmsGroupMember();
            SmsGroupRecord groupRecord = new SmsGroupRecord();
            if (orderParam.getGroupType() == 1) { // 1 发起拼团 2 参与拼团
                groupRecord.setCreateTime(new Date());
                groupRecord.setGroupId(group.getId());
                groupRecord.setStatus("1");
                groupRecord.setHandleStatus(1);
                groupRecordMapper.insert(groupRecord);

                groupMember.setGoodsId(orderParam.getGoodsId());
                groupMember.setName(currentMember.getNickname());
                groupMember.setMemberId(currentMember.getId());
                groupMember.setPic(currentMember.getIcon());
                groupMember.setStatus(1);
                groupMember.setOrderId(order.getId());
                groupMember.setCreateTime(new Date());
                groupMember.setGroupRecordId(groupRecord.getId());
                groupMember.setGroupId(group.getId());
                groupMemberMapper.insert(groupMember);
            } else {
                List<SmsGroupMember> list1 = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_record_id", orderParam.getMgId()));
                if (list1 != null && list1.size() > group.getMaxPeople()) {
                    return new CommonResult().failed("此拼团已达最大人数");
                }
                for (SmsGroupMember smsGroupMember : list1) {
                    if (smsGroupMember.getMemberId().equals(currentMember.getId())) {
                        return new CommonResult().failed("你已经参加过此团");
                    }
                }
                groupMember.setGoodsId(orderParam.getGoodsId());
                groupMember.setName(currentMember.getNickname());
                groupMember.setMemberId(currentMember.getId());
                groupMember.setPic(currentMember.getIcon());
                groupMember.setStatus(1);
                groupMember.setOrderId(order.getId());
                groupMember.setCreateTime(new Date());
                groupMember.setGroupRecordId(orderParam.getMgId());
                groupMember.setGroupId(group.getId());
                groupMemberMapper.insert(groupMember);
            }
            //3.计算优惠券
            SmsCoupon coupon = null;
            if (orderParam.getCouponId() != null) {
                //   couponHistory = couponHistoryService.getById(orderParam.getMemberCouponId());
                coupon = couponService.getById(orderParam.getCouponId());
            }

            if (orderParam.getCouponId() == null || orderParam.getCouponId() == 0) {
                order.setCouponAmount(new BigDecimal(0));
            } else {
                order.setCouponId(orderParam.getCouponId());
                order.setCouponAmount(coupon.getAmount());
            }
            order.setPayAmount(group.getGroupPrice().add(pmsProduct.getTransfee()).subtract(order.getCouponAmount()));
            order.setGroupId(groupMember.getId());
            orderMapper.updateById(order);
        } else {
            return new CommonResult().failed("活动已经结束");
        }
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }

        orderItemService.saveBatch(orderItemList);
        //如使用优惠券更新优惠券使用状态
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        //如使用积分需要扣除积分
        if (orderParam.getUseIntegration() != null) {
            order.setUseIntegration(orderParam.getUseIntegration());
            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);

        String platform = orderParam.getPlatform();
        if ("1".equals(platform)) {
            push(currentMember, order, orderParam.getPage(), orderParam.getFormId(), name);
        }
        return new CommonResult().success("下单成功", result);
    }

    private void createCartObj(OrderParam orderParam, List<OmsCartItem> list, OmsCartItem cartItem, PmsProduct pmsProduct) {
        cartItem.setIsFenxiao(pmsProduct.getIsFenxiao());
        if (ValidatorUtils.notEmpty(orderParam.getSkuId()) && orderParam.getSkuId()>0) {
            PmsSkuStock pmsSkuStock = skuStockMapper.selectById(orderParam.getSkuId());
            checkGoods(pmsProduct, false, 1);
            checkSkuGoods(pmsSkuStock, 1);
            cartItem.setProductId(pmsSkuStock.getProductId());
            cartItem.setMemberId(memberService.getNewCurrentMember().getId());
            cartItem.setProductSkuId(pmsSkuStock.getId());
            cartItem.setChecked(1);
            if (orderParam.getOrderType().equals(AllEnum.OrderType.PIN_GROUP.code())) {
                if (ValidatorUtils.notEmpty(orderParam.getGroupId())) {
                    SmsGroup group = groupMapper.selectById(orderParam.getGroupId());
                    if (group != null) {
                        cartItem.setPrice(group.getGroupPrice());
                    } else {
                        throw new ApiMallPlusException("拼团不存在");
                    }
                } else {
                    throw new ApiMallPlusException("参数错误="+orderParam.getGroupId());
                }
            } else {
                cartItem.setPrice(pmsProduct.getPrice());
            }
            cartItem.setProductSkuCode(pmsSkuStock.getSkuCode());
            cartItem.setQuantity(orderParam.getTotal());
            cartItem.setProductAttr(pmsSkuStock.getMeno());
            cartItem.setProductPic(pmsSkuStock.getPic());
            cartItem.setSp1(pmsSkuStock.getSp1());
            cartItem.setSp2(pmsSkuStock.getSp2());
            cartItem.setSp3(pmsSkuStock.getSp3());
            cartItem.setProductName(pmsSkuStock.getProductName());
            cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
            cartItem.setProductBrand(pmsProduct.getBrandName());
            cartItem.setCreateDate(new Date());

        } else {
            checkGoods(pmsProduct, true, orderParam.getTotal());
            cartItem.setProductId(orderParam.getGoodsId());
            cartItem.setMemberId(memberService.getNewCurrentMember().getId());
            cartItem.setChecked(1);
            cartItem.setPrice(pmsProduct.getPrice());
            if (orderParam.getOrderType().equals(AllEnum.OrderType.PIN_GROUP.code())) {
                if (ValidatorUtils.notEmpty(orderParam.getGroupId())) {
                    SmsGroup group = groupMapper.selectById(orderParam.getGroupId());
                    if (group != null) {
                        cartItem.setPrice(group.getGroupPrice());
                    } else {
                        throw new ApiMallPlusException("拼团不存在");
                    }
                } else {
                    throw new ApiMallPlusException("参数错误="+orderParam.getGroupId());
                }
            } else {
                cartItem.setPrice(pmsProduct.getPrice());
            }
            cartItem.setProductName(pmsProduct.getName());
            cartItem.setQuantity(orderParam.getTotal());
            cartItem.setProductPic(pmsProduct.getPic());
            cartItem.setCreateDate(new Date());
            cartItem.setMemberId(memberService.getNewCurrentMember().getId());
            cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
            cartItem.setProductBrand(pmsProduct.getBrandName());

        }
        if (ValidatorUtils.empty(cartItem.getProductPic())){
            cartItem.setProductPic(pmsProduct.getPic());
        }
        list.add(cartItem);
    }

    private OmsOrderItem createOrderItem(OmsCartItem cartPromotionItem) {
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setProductAttr(cartPromotionItem.getProductAttr());
        orderItem.setProductId(cartPromotionItem.getProductId());
        orderItem.setProductName(cartPromotionItem.getProductName());
        orderItem.setProductPic(cartPromotionItem.getProductPic());
        orderItem.setProductAttr(cartPromotionItem.getProductAttr());
        orderItem.setProductBrand(cartPromotionItem.getProductBrand());
        orderItem.setProductSn(cartPromotionItem.getProductSn());
        orderItem.setProductPrice(cartPromotionItem.getPrice());
        orderItem.setProductQuantity(cartPromotionItem.getQuantity());
        orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
        orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
        orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
        orderItem.setStoreName(cartPromotionItem.getStoreName());
        orderItem.setStoreId(cartPromotionItem.getStoreId());
        orderItem.setStatus(OrderStatus.TO_DELIVER.getValue());
        orderItem.setIsFenxiao(cartPromotionItem.getIsFenxiao());

           /* orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());*/
        return orderItem;
    }

    private OmsOrder createOrderObj(OmsOrder order, OrderParam orderParam, UmsMember currentMember, List<OmsOrderItem> orderItemList, Long addressId) {
        order.setIsComment(1);
        order.setTaxType(1);
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));

        if (ValidatorUtils.notEmpty(orderParam.getGroupId())) {
            order.setGroupId(orderParam.getGroupId());
        }
        if (orderParam.getUseIntegration() == null) {
            order.setIntegration(0);
            order.setIntegrationAmount(new BigDecimal(0));
        } else {
            order.setIntegration(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }
        //转化为订单信息并插入数据库
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        order.setMemberId(currentMember.getId());
        //支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        //订单来源：0->PC订单；5->app订单 2 h5 3微信小程序 4 支付宝小程序
        order.setSourceType(orderParam.getSource());
        //订单状态：订单状态：1->待付款；2->待发货；3->已发货；4->已完成；5->售后订单 6->已关闭；
        order.setStatus(OrderStatus.INIT.getValue());
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(orderParam.getOrderType());
        //收货人信息：姓名、电话、邮编、地址

        if (orderParam.getOrderType() == 7) {
            order.setGrowth(orderParam.getShopId());
            order.setReceiverName(orderParam.getLading_name());
            order.setReceiverPhone(orderParam.getLading_mobile());
        } else {
            UmsMemberReceiveAddress address = addressService.getById(orderParam.getAddressId());
            if (address != null) {
                order.setReceiverId(address.getId());
                order.setReceiverName(address.getName());
                order.setReceiverPhone(address.getPhoneNumber());
                order.setReceiverPostCode(address.getPostCode());
                order.setReceiverProvince(address.getProvince());
                order.setReceiverCity(address.getCity());
                order.setReceiverRegion(address.getRegion());
                order.setReceiverDetailAddress(address.getDetailAddress());
            }
        }

        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        order.setMemberId(memberService.getNewCurrentMember().getId());
        //生成订单号
        order.setOrderSn(generateOrderSn(order));
        return order;
    }

    @Override
    @Transactional
    public boolean closeOrder(OmsOrder order) {
        releaseStock(order);
        order.setStatus(OrderStatus.CLOSED.getValue());

        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(order.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(OrderStatus.INIT.getValue());
        history.setOrderStatus(OrderStatus.CLOSED.getValue());
        history.setNote("关闭订单");
        orderOperateHistoryService.save(history);

        return orderMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object confimDelivery(Long id) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        OmsOrder order = this.orderMapper.selectById(id);
        if (order.getStatus() != OrderStatus.DELIVERED.getValue()) {
            return new CommonResult().paramFailed("已发货订单才能确认收货");
        }
        OmsOrderOperateHistory history = updateOrderInfo(id, order, OrderStatus.TO_COMMENT);
        history.setNote("确认收货");
        orderOperateHistoryService.save(history);

        return new CommonResult().success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object applyRefund(Long id) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        OmsOrder order = this.orderMapper.selectById(id);
        try {
            if (order.getStatus() > 9) {
                return new CommonResult().paramFailed("已支付的订单才能申请退款");
            }
            OmsOrderOperateHistory history = updateOrderInfo(id, order, OrderStatus.REFUNDING);
            history.setNote("申请退款");
            orderOperateHistoryService.save(history);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CommonResult().success();
    }

    private OmsOrderOperateHistory updateOrderInfo(Long id, OmsOrder oldOrder, OrderStatus newStatus) {
        String key = Rediskey.orderDetail + "orderid" + id;
        redisService.remove(key);
        oldOrder.setStatus(newStatus.getValue());
        orderMapper.updateById(oldOrder);

        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(oldOrder.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(oldOrder.getStatus());
        history.setOrderStatus(newStatus.getValue());
        return history;
    }

    @Override
    public Object orderComment(Long orderId, String items) {
        UmsMember member = memberService.getNewCurrentMember();
        List<ProductConsultParam> itemss = null;
        try {
            itemss = JsonUtils.json2list(items, ProductConsultParam.class);
            for (ProductConsultParam param : itemss) {
                PmsProductConsult productConsult = new PmsProductConsult();
                if (member != null) {
                    productConsult.setPic(member.getIcon());
                    productConsult.setMemberName(member.getNickname());
                    productConsult.setMemberId(member.getId());
                } else {
                    return new CommonResult().failed("请先登录");
                }
                productConsult.setGoodsId(param.getGoodsId());
                productConsult.setOrderId(orderId);
                productConsult.setConsultContent(param.getTextarea());
                productConsult.setStars(param.getScore());
                productConsult.setEmail(Arrays.toString(param.getImages()));
                productConsult.setConsultAddtime(new Date());
                productConsult.setType(AllEnum.ConsultType.ORDER.code());
                if (ValidatorUtils.empty(param.getTextarea()) && ValidatorUtils.empty(param.getImages())) {

                } else {
                    pmsProductConsultService.save(productConsult);
                }

            }
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setId(orderId);
            omsOrder.setIsComment(2);
            omsOrder.setStatus(OrderStatus.TRADE_SUCCESS.getValue());
            if (orderService.updateById(omsOrder)) {
                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(omsOrder.getId());
                history.setCreateTime(new Date());
                history.setOperateMan("shop");
                history.setPreStatus(OrderStatus.TO_COMMENT.getValue());
                history.setOrderStatus(OrderStatus.TRADE_SUCCESS.getValue());
                history.setNote("订单评价");
                orderOperateHistoryService.save(history);
                return new CommonResult().success(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CommonResult().failed();
    }

    @Transactional
    @Override
    public Object applyMember(Long memberLevelId) {
        UmsMember userDO = memberService.getNewCurrentMember();
        UmsMemberLevel memberLevel = memberLevelService.getById(memberLevelId);
        if (memberLevel != null && memberLevel.getPrice().compareTo(userDO.getBlance()) > 0) {
            throw new ApiMallPlusException("余额不足！");
        }
        userDO.setMemberLevelId(memberLevel.getId());
        userDO.setMemberLevelName(memberLevel.getName());
        userDO.setBlance(userDO.getBlance().subtract(memberLevel.getPrice()));

        memberService.updateById(userDO);
        UmsMemberBlanceLog blog = new UmsMemberBlanceLog();
        blog.setMemberId(userDO.getId());
        blog.setCreateTime(new Date());
        blog.setNote("会员等级升级：" + memberLevel.getId());
        blog.setPrice(memberLevel.getPrice());
        blog.setType(1);
        memberBlanceLogService.save(blog);
        return new CommonResult().success();
    }

    @Transactional
    @Override
    public Object blancePay(PayParam payParam) {

        OmsOrder order = orderMapper.selectById(payParam.getOrderId());
        this.blancePay(order);
        return order;
    }

    @Transactional
    @Override
    public Object blancePay(OmsOrder order) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        UmsMember userDO = memberService.getById(currentMember.getId());
        if (order.getPayAmount().compareTo(userDO.getBlance()) > 0) {
            throw new ApiMallPlusException("余额不足！");
        }
        Integer to_deleter =OrderStatus.TO_DELIVER.getValue();
        order.setStatus(to_deleter);
        order.setPayType(AllEnum.OrderPayType.balancePay.code());
        order.setPaymentTime(new Date());
        if (order.getPayAmount().compareTo(BigDecimal.ZERO) <= 0) {
            order.setPayAmount(new BigDecimal("0.01"));
        }

        if (ValidatorUtils.notEmpty(order.getGroupId())) {
            to_deleter =OrderStatus.PINTUAN_ORDER.getValue();
            SmsGroupMember member = new SmsGroupMember();
            member.setId(order.getGroupId());
            member.setStatus(2);
            groupMemberMapper.updateById(member);

            SmsGroupMember groupMember = groupMemberMapper.selectById(order.getGroupId());
            SmsGroupRecord groupRecord =  new SmsGroupRecord();
            groupRecord.setId(groupMember.getGroupRecordId());
            SmsGroup group = groupMapper.selectById(groupMember.getGroupId());
            List<SmsGroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_record_id", groupRecord.getId()).eq("status", 2));

            if (groupMembers != null && groupMembers.size() == group.getMaxPeople()) {
                groupRecord.setStatus("2");
                groupRecordMapper.updateById(groupRecord);
            }
        }
        orderService.updateById(order);
        if (ValidatorUtils.isEmpty(order.getPid()) || order.getPid() < 1) {
            OmsOrder childOrder = new OmsOrder();
            childOrder.setStatus(to_deleter);
            childOrder.setPayType(AllEnum.OrderPayType.balancePay.code());
            childOrder.setPaymentTime(new Date());
            orderService.update(childOrder, new QueryWrapper<OmsOrder>().eq("pid", order.getId()));
        }
        userDO.setBlance(userDO.getBlance().subtract(order.getPayAmount()));
        memberService.updateById(userDO);
         // 余额日志记录
        UmsMemberBlanceLog blog = new UmsMemberBlanceLog();
        blog.setMemberId(userDO.getId());
        blog.setCreateTime(new Date());
        blog.setNote("订单：" + order.getId()+",消费:"+order.getPayAmount());
        blog.setPrice(order.getPayAmount());
        blog.setType(1);
        memberBlanceLogService.save(blog);
        // 订单日志记录
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(order.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(OrderStatus.INIT.getValue());
        history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
        history.setNote("余额支付");
        orderOperateHistoryService.save(history);
        // 积分日志记录
        memberService.addIntegration(userDO.getId(), order.getPayAmount().multiply(new BigDecimal("0.1")).intValue(),
                1, "余额支付添加积分", AllEnum.ChangeSource.order.code(), userDO.getUsername());
        OmsOrderItem queryO = new OmsOrderItem();
        queryO.setOrderId(order.getId());
        queryO.setType(AllEnum.OrderItemType.GOODS.code());
        List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
        // 分拥计算
        recordFenxiaoMoney(list, userDO);
        return order;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancleDelivery(OmsOrder order, String remark) {
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(order.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(order.getStatus());
        history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
        history.setNote("取消发货");
        orderOperateHistoryService.save(history);

        String key = Rediskey.orderDetail + "orderid" + order.getId();
        redisService.remove(key);
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        return orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public Object jifenPay(OrderParam orderParam) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        UmsMember member = memberService.getById(memberService.getNewCurrentMember().getId());
        PmsGifts gifts = giftsService.getById(orderParam.getGoodsId());
        if (gifts.getPrice().intValue() > member.getIntegration()) {
            return new CommonResult().failed("积分不足！");
        } else {
            // 插入订单
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(orderParam.getGoodsId());
            orderItem.setProductName(gifts.getTitle());
            orderItem.setProductPic(gifts.getIcon());
            orderItem.setProductPrice(gifts.getPrice());
            orderItem.setProductQuantity(1);
            orderItem.setProductCategoryId(gifts.getCategoryId());
            orderItem.setType(AllEnum.OrderItemType.GOODS.code());
            List<OmsOrderItem> omsOrderItemList = new ArrayList<>();
            omsOrderItemList.add(orderItem);
            OmsOrder order = new OmsOrder();
            createOrderObj(order, orderParam, member, omsOrderItemList, null);
            order.setOrderType(AllEnum.OrderType.JIFEN.code());
            order.setStatus(OrderStatus.TO_DELIVER.getValue());
            order.setPayType(AllEnum.OrderPayType.jifenPay.code());
            order.setPaymentTime(new Date());
            order.setOrderSn(generateOrderSn(order));
            order.setGoodsId(orderParam.getGoodsId());
            order.setGoodsName(gifts.getTitle());
            orderService.save(order);
            orderItem.setOrderId(order.getId());
            orderItemService.save(orderItem);

            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(order.getId());
            history.setCreateTime(new Date());
            history.setOperateMan("shop");
            history.setPreStatus(OrderStatus.INIT.getValue());
            history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
            history.setNote("积分兑换");
            orderOperateHistoryService.save(history);

            //修改会员积分
            member.setIntegration(member.getIntegration() - gifts.getPrice().intValue());
            memberService.updateById(member);
            // 插入积分日志表
            UmsIntegrationChangeHistory historyChange = new UmsIntegrationChangeHistory(member.getId(), new Date(), AllEnum.ChangeType.Min.code(), gifts.getPrice().intValue()
                    , member.getUsername(), order.getId() + "", AllEnum.ChangeSource.order.code());
            integrationChangeHistoryMapper.insert(historyChange);
            // 删除订单缓存
            String key = Rediskey.orderDetail + "orderid" + order.getId();
            redisService.remove(key);

        }
        return new CommonResult().success("兑换成功");
    }

    @Override
    public CommonResult paySuccess(Long orderId) {
        //修改订单支付状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        order.setPaymentTime(new Date());
        orderService.updateById(order);
        //恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderItem queryO = new OmsOrderItem();
        queryO.setOrderId(orderId);
        String key = Rediskey.orderDetail + "orderid" + orderId;
        redisService.remove(key);
        List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
        int count = orderMapper.updateSkuStock(list);
        return new CommonResult().success("支付成功", count);
    }

    /**
     * 推送消息
     */
    public void push(UmsMember umsMember, OmsOrder order, String page, String formId, String name) {
        boolean flag = true;
        SysAppletSet appletSet = memberService.getSysAppletSet(0);
        if (null == appletSet) {
            log.error("没有设置支付配置：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
            flag = false;
        }
        log.info("发送模版消息：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
        if (StringUtils.isEmpty(formId)) {
            flag = false;
            log.error("发送模版消息：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
        }
        if (flag) {
            String accessToken = null;
            try {
                accessToken = wechatApiService.getAccessToken();

                String templateId = appletSet.getTemplateid1();
                Map<String, TemplateData> param = new HashMap<String, TemplateData>();
                param.put("keyword1", new TemplateData(DateUtils.format(order.getCreateTime(), "yyyy-MM-dd"), "#EE0000"));

                param.put("keyword2", new TemplateData(name, "#EE0000"));
                param.put("keyword3", new TemplateData(order.getOrderSn(), "#EE0000"));
                param.put("keyword3", new TemplateData(order.getPayAmount() + "", "#EE0000"));

                JSONObject jsonObject = JSONObject.fromObject(param);
                //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
                WX_TemplateMsgUtil.sendWechatMsgToUser(umsMember.getWeixinOpenid(), templateId, page + "?id=" + order.getId(),
                        formId, jsonObject, accessToken);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CommonResult autoDeliveryOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectOne(new QueryWrapper<>());
        if (orderSetting != null) {
            List<OmsOrder> list = orderService.list(new QueryWrapper<OmsOrder>().eq("status", OrderStatus.DELIVERED.getValue()));
            for (OmsOrder order : list) {
                if (order.getPaymentTime().getTime()+ orderSetting.getConfirmOvertime() * 12*3600 > System.currentTimeMillis()) {
                    OmsOrderOperateHistory history = updateOrderInfo(order.getId(), order, OrderStatus.TO_COMMENT);
                    history.setNote("Task确认收货");
                    orderOperateHistoryService.save(history);
                }
            }
        }
        return new CommonResult().success(null);
    }

    @Override
    public CommonResult autoCommentOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectOne(new QueryWrapper<>());
        if (orderSetting != null) {
            List<OmsOrder> list = orderService.list(new QueryWrapper<OmsOrder>().eq("status", OrderStatus.TO_COMMENT.getValue()));
            for (OmsOrder order : list) {
                if (order.getPaymentTime().getTime() + orderSetting.getCommentOvertime() * 12* 3600> (System.currentTimeMillis() )) {
                    OmsOrderOperateHistory history = updateOrderInfo(order.getId(), order, OrderStatus.TRADE_SUCCESS);
                    history.setNote("Task自动好评");
                    orderOperateHistoryService.save(history);
                }
            }
        }
        return new CommonResult().success(null);
    }

    @Override
    public CommonResult autoSucessOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectOne(new QueryWrapper<>());
        if (orderSetting != null) {
            List<OmsOrder> list = orderService.list(new QueryWrapper<OmsOrder>().eq("status", OrderStatus.TRADE_SUCCESS.getValue()));
            for (OmsOrder order : list) {
                if (order.getPaymentTime().getTime()+ orderSetting.getFinishOvertime() *12* 3600 > (System.currentTimeMillis() )) {
                    OmsOrderOperateHistory history = updateOrderInfo(order.getId(), order, OrderStatus.TO_SHARE);
                    history.setNote("Task自动完成交易");
                    orderOperateHistoryService.save(history);
                }
            }
        }
        return new CommonResult().success(null);
    }

    @Override
    public CommonResult cancelTimeOutOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectOne(new QueryWrapper<>());
        if (orderSetting != null) {
            //查询超时、未支付的订单及订单详情
            List<OmsOrderDetail> timeOutOrders = orderMapper.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
            if (CollectionUtils.isEmpty(timeOutOrders)) {
                return new CommonResult().failed("暂无超时订单");
            }
            //修改订单状态为交易取消
            List<Long> ids = new ArrayList<>();
            for (OmsOrderDetail timeOutOrder : timeOutOrders) {
                ids.add(timeOutOrder.getId());
            }
            orderMapper.updateOrderStatus(ids, OrderStatus.CLOSED.getValue());
            for (OmsOrderDetail timeOutOrder : timeOutOrders) {
                //解除订单商品库存锁定
                // orderMapper.releaseSkuStockLock(timeOutOrder.getOrderItemList());
                releaseStock(timeOutOrder);
                //修改优惠券使用状态
                updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
                //返还使用积分
                if (timeOutOrder.getUseIntegration() != null) {
                    UmsMember member = memberService.getById(timeOutOrder.getMemberId());
                    //    memberService.updateIntegration(timeOutOrder.getMemberId(), member.getIntegration() + timeOutOrder.getUseIntegration());
                }
            }
        }
        return new CommonResult().success(null);
    }

    @Override
    public void cancelOrder(Long orderId) {
        //查询为付款的取消订单
        OmsOrder cancelOrder = orderMapper.selectById(orderId);
        if (cancelOrder != null) {
            //修改订单状态为取消
            cancelOrder.setStatus(OrderStatus.CLOSED.getValue());
            orderMapper.updateById(cancelOrder);
            OmsOrderItem queryO = new OmsOrderItem();
            queryO.setOrderId(orderId);
            //  List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
            //解除订单商品库存锁定
            //  orderMapper.releaseSkuStockLock(list);
            releaseStock(cancelOrder);
            //修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            //返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(cancelOrder.getMemberId());
                memberService.updateIntegration(cancelOrder.getMemberId(), member.getIntegration() + cancelOrder.getUseIntegration());
            }
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan("shop");
            history.setPreStatus(cancelOrder.getStatus());
            history.setOrderStatus(OrderStatus.CLOSED.getValue());
            history.setNote("取消订单");
            orderOperateHistoryService.save(history);
        }
    }

    /**
     * 推送消息
     */
    public void push(GroupAndOrderVo umsMember, OmsOrder order, String page, String formId) {
        log.info("发送模版消息：userId=" + umsMember.getMemberId() + ",orderId=" + order.getId() + ",formId=" + formId);
        boolean flag = true;
        SysAppletSet appletSet = memberService.getSysAppletSet(0);
        if (null == appletSet) {
            log.error("没有设置支付配置：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
            flag = false;
        }
        log.info("发送模版消息：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
        if (StringUtils.isEmpty(formId)) {
            flag = false;
            log.error("发送模版消息：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
        }
        if (flag) {
            String accessToken = null;
            try {
                accessToken = wechatApiService.getAccessToken();

                String templateId = appletSet.getTemplateid1();
                Map<String, TemplateData> param = new HashMap<String, TemplateData>();
                param.put("keyword1", new TemplateData(DateUtils.format(order.getCreateTime(), "yyyy-MM-dd"), "#EE0000"));

                param.put("keyword2", new TemplateData(order.getGoodsName(), "#EE0000"));
                param.put("keyword3", new TemplateData(order.getOrderSn(), "#EE0000"));
                param.put("keyword3", new TemplateData(order.getPayAmount() + "", "#EE0000"));

                JSONObject jsonObject = JSONObject.fromObject(param);
                //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
                WX_TemplateMsgUtil.sendWechatMsgToUser(umsMember.getWxid(), templateId, page + "?id=" + order.getId(),
                        formId, jsonObject, accessToken);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {

        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        sb.append(order.getMemberId());
        return sb.toString();
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * 锁定下单商品的所有库存
     */
    public void lockStockByOrder(List<OmsOrderItem> cartPromotionItemList, String type) {
        log.info("lockStockByOrder");
        for (OmsOrderItem item : cartPromotionItemList) {
            if (item.getType().equals(AllEnum.OrderItemType.GOODS.code())) {
                if (type != null && "6".equals(type)) {
                    SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(item.getGiftIntegration());
                    if ((relation.getFlashPromotionCount() - item.getProductQuantity()) < 0) {
                        throw new ApiMallPlusException("SmsFlashPromotionProductRelation is stock out. goodsId=" + item.getProductId() + ", relation=" + relation.getId());
                    }
                    relation.setFlashPromotionCount(relation.getFlashPromotionCount() - item.getProductQuantity());
                    smsFlashPromotionProductRelationService.updateById(relation);
                }
                PmsProduct goods = productService.getById(item.getProductId());
                if (goods != null && goods.getId() != null) {
                    PmsProduct newGoods = new PmsProduct();
                    newGoods.setId(goods.getId());
                    redisService.remove(String.format(Rediskey.GOODSDETAIL, goods.getId() + ""));
                    if (!ValidatorUtils.empty(item.getProductSkuId()) && item.getProductSkuId() > 0) {
                        PmsSkuStock skuStock = skuStockMapper.selectById(item.getProductSkuId());
                        if ((skuStock.getStock() - item.getProductQuantity()) < 0) {
                            throw new ApiMallPlusException("goods is stock out. goodsId=" + item.getProductId() + ", skuId=" + item.getProductSkuId());
                        } else {
                            skuStock.setId(item.getProductSkuId());
                            skuStock.setStock(skuStock.getStock() - item.getProductQuantity());
                            skuStockMapper.updateById(skuStock);
                        }
                    } else {
                        if ((goods.getStock() - item.getProductQuantity()) < 0) {
                            throw new ApiMallPlusException("goods is stock out. goodsId=" + item.getProductId() + ", goodsId=" + item.getProductSkuId());
                        }
                    }
                    newGoods.setSale(goods.getSale() + item.getProductQuantity());
                    newGoods.setStock(goods.getStock() - item.getProductQuantity());
                    productService.updateById(newGoods);
                }
            } else {
                PmsGifts goods = pmsGiftsService.getById(item.getProductId());
                if (goods != null && goods.getId() != null) {
                    PmsGifts newGoods = new PmsGifts();
                    newGoods.setId(goods.getId());
                    if ((goods.getStock() - item.getProductQuantity()) < 0) {
                        throw new ApiMallPlusException("赠礼商品 goods is stock out. goodsId=" + item.getProductId() + ", goodsId=" + item.getProductSkuId());
                    }
                    newGoods.setStock(goods.getStock() - item.getProductQuantity());
                    pmsGiftsService.updateById(newGoods);
                }

            }

        }
    }

    /**
     * 锁定下单商品的所有库存
     */
    public void lockStock(List<OmsCartItem> cartPromotionItemList) {
        log.info("lockStock");
        for (OmsCartItem item : cartPromotionItemList) {
            PmsProduct goods = productService.getById(item.getProductId());
            if (goods != null && goods.getId() != null) {
                PmsProduct newGoods = new PmsProduct();
                newGoods.setId(goods.getId());
                if (true) {
                    redisService.remove(String.format(Rediskey.GOODSDETAIL, goods.getId() + ""));
                    if (!ValidatorUtils.empty(item.getProductSkuId()) && item.getProductSkuId() > 0) {
                        PmsSkuStock skuStock = skuStockMapper.selectById(item.getProductSkuId());
                        if ((skuStock.getStock() - item.getQuantity()) < 0) {
                            throw new ApiMallPlusException("goods is stock out. goodsId=" + item.getProductId() + ", skuId=" + item.getProductSkuId());
                        } else {
                            skuStock.setId(item.getProductSkuId());
                            skuStock.setStock(skuStock.getStock() - item.getQuantity());
                            skuStockMapper.updateById(skuStock);
                        }
                    } else {
                        if ((goods.getStock() - item.getQuantity()) < 0) {
                            throw new ApiMallPlusException("goods is stock out. goodsId=" + item.getProductId() + ", goodsId=" + item.getProductSkuId());
                        }
                    }
                }
                newGoods.setSale(goods.getSale() + item.getQuantity());
                newGoods.setStock(goods.getStock() - item.getQuantity());
                productService.updateById(newGoods);
            }
        }
    }

    /**
     * 解锁下单商品的所有库存
     */
    public void releaseStock(OmsOrder order) {
        List<OmsOrderItem> orderItemList = orderItemService.list(new QueryWrapper<OmsOrderItem>().eq("order_id", order.getId()));
        log.info("releaseStock");
        for (OmsOrderItem item : orderItemList) {
            if (item.getType().equals(AllEnum.OrderItemType.GOODS.code())) {
                if ("6".equals(order.getOrderType())) {
                    SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(item.getGiftIntegration().longValue());
                    relation.setFlashPromotionCount(relation.getFlashPromotionCount() + item.getProductQuantity());
                    smsFlashPromotionProductRelationService.updateById(relation);
                }
                PmsProduct goods = productService.getById(item.getProductId());
                if (goods != null && goods.getId() != null) {
                    PmsProduct newGoods = new PmsProduct();
                    newGoods.setId(goods.getId());
                    if (true) {
                        redisService.remove(String.format(Rediskey.GOODSDETAIL, goods.getId() + ""));
                        if (!ValidatorUtils.empty(item.getProductSkuId()) && item.getProductSkuId() > 0) {
                            PmsSkuStock skuStock = skuStockMapper.selectById(item.getProductSkuId());
                            skuStock.setId(item.getProductSkuId());
                            skuStock.setStock(skuStock.getStock() + item.getProductQuantity());
                            skuStockMapper.updateById(skuStock);
                        }
                    }
                    goods.setSale(goods.getSale() - item.getProductQuantity());
                    newGoods.setStock(goods.getStock() + item.getProductQuantity());
                    productService.updateById(newGoods);
                }
            } else {
                PmsGifts goods = pmsGiftsService.getById(item.getProductId());
                if (goods != null && goods.getId() != null) {
                    PmsGifts newGoods = new PmsGifts();
                    newGoods.setId(goods.getId());
                    if ((goods.getStock() + item.getProductQuantity()) < 0) {
                        throw new ApiMallPlusException("goods is stock out. goodsId=" + item.getProductId() + ", goodsId=" + item.getProductSkuId());
                    }
                    newGoods.setStock(goods.getStock() + item.getProductQuantity());
                    pmsGiftsService.updateById(newGoods);
                }
            }
        }
    }

    /**
     * 判断下单商品是否都有库存
     */
    private boolean hasStock(List<OmsCartItem> cartPromotionItemList) {

        return true;
    }

    /**
     * 计算购物车中商品的价格
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<OmsCartItem> cartPromotionItemList, BigDecimal transFee, BigDecimal payAmount) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(transFee);


        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        CartMarkingVo vo = new CartMarkingVo();
        vo.setCartList(cartPromotionItemList);
        SmsBasicMarking basicMarking = basicMarkingService.matchOrderBasicMarking(vo);
        log.info("basicMarking=" + com.alibaba.fastjson.JSONObject.toJSONString(basicMarking));
        if (basicMarking != null) {
            promotionAmount = basicMarking.getMinAmount();
        }
        if (promotionAmount == null) {
            promotionAmount = BigDecimal.ZERO;
        }
        for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
            totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            //  promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
            calcAmount.setPayAmount(payAmount.subtract(promotionAmount).add(transFee));
        } else {
            calcAmount.setPayAmount(totalAmount.subtract(promotionAmount).add(transFee));
        }
        if (calcAmount.getPayAmount().compareTo(BigDecimal.ZERO) <= 0) {
            calcAmount.setPayAmount(new BigDecimal("0.01"));
        }
        return calcAmount;
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<OmsCartItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        cartItemService.delete(currentMember.getId(), ids);
    }

    /**
     * 将优惠券信息更改为指定状态
     *
     * @param couponId  优惠券id
     * @param memberId  会员id
     * @param useStatus 0->未使用；1->已使用
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) {
            return;
        }
        //查询第一张优惠券
        SmsCouponHistory queryC = new SmsCouponHistory();
        queryC.setCouponId(couponId);
        if (useStatus == 0) {
            queryC.setUseStatus(1);
        } else {
            queryC.setUseStatus(0);
        }
        List<SmsCouponHistory> couponHistoryList = couponHistoryService.list(new QueryWrapper<>(queryC));
        if (!CollectionUtils.isEmpty(couponHistoryList)) {
            SmsCouponHistory couponHistory = couponHistoryList.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryService.updateById(couponHistory);
        }
    }

    /**
     * 获取订单促销信息
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName());
            sb.append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 计算订单应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        if (order.getPromotionAmount() == null) {
            order.setPromotionAmount(BigDecimal.ZERO);
        }
        if (order.getFreightAmount() == null) {
            order.setFreightAmount(BigDecimal.ZERO);
        }
        //总金额+运费-促销优惠-优惠券优惠-积分抵扣
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount())
                .subtract(order.getVipAmount());
        return payAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算订单活动优惠
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }

    /**
     * 获取可用积分抵扣金额
     *
     * @param useIntegration 使用的积分数量
     * @param totalAmount    订单总金额
     * @param currentMember  使用的用户
     * @param hasCoupon      是否已经使用优惠券
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal(0);
        //判断用户是否有这么多积分
        if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
            return zeroAmount;
        }
        //根据积分使用规则判断使用可用
        //是否可用于优惠券共用
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            //不可与优惠券共用
            return zeroAmount;
        }
        //是否达到最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }
        //是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration).divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    /**
     * 对优惠券优惠进行处理
     *
     * @param orderItemList       order_item列表
     * @param couponHistoryDetail 可用优惠券详情
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            //全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            //指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            //指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对每个下单商品进行优惠券金额分摊的计算
     *
     * @param orderItemList 可用优惠券的下单商品商品
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            //(商品价格/可用商品总价)*优惠券面额
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * 获取与优惠券有关系的下单商品
     *
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList       下单商品
     * @param type                使用关系类型：0->相关分类；1->指定商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                categoryIdList.add(productCategoryRelation.getProductCategoryId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (categoryIdList.contains(orderItem.getProductCategoryId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                productIdList.add(productRelation.getProductId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (productIdList.contains(orderItem.getProductId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return result;
    }

    @Override
    public Object addGroup(OrderParam orderParam) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        List<OmsCartItem> list = new ArrayList<>();
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        OmsCartItem cartItem = new OmsCartItem();
        PmsProduct pmsProduct = productService.getById(orderParam.getGoodsId());
        createCartObj(orderParam, list, cartItem, pmsProduct);
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        List<OmsCartItem> newCartList = new ArrayList<>();
        // 取商品大的运费
        BigDecimal transFee = BigDecimal.ZERO;
        for (OmsCartItem cart : list) {
            PmsProduct goods = productService.getById(cart.getProductId());
            if (goods != null && goods.getStock() > 0 && goods.getStock() >= cart.getQuantity()) {
                if (goods.getTransfee().compareTo(transFee) > 0) {
                    transFee = goods.getTransfee();
                }
            }

            newCartList.add(cart);
        }
        result.setCartPromotionItemList(newCartList);
        //获取用户收货地址列表
        UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
        if (memberService.getNewCurrentMember() == null) {
            return new CommonResult().fail(100);
        }
        queryU.setMemberId(memberService.getNewCurrentMember().getId());
        List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        UmsMemberReceiveAddress address = addressService.getDefaultItem();
        //获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(newCartList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        UmsMember member = memberService.getById(memberService.getNewCurrentMember().getId());
        if (member == null) {
            return new CommonResult().fail(100);
        }

        //获取用户积分
        result.setMemberIntegration(member.getIntegration());
        //获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);
        //计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(newCartList, transFee, BigDecimal.ZERO);
        result.setCalcAmount(calcAmount);
        result.setAddress(address);
        return result;
    }


    @Override
    public Object addCart(CartParam cartParam) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        if (ValidatorUtils.empty(cartParam.getTotal())) {
            cartParam.setTotal(1);
        }
        OmsCartItem cartItem = new OmsCartItem();
        PmsProduct pmsProduct = productService.getById(cartParam.getGoodsId());
        if (ValidatorUtils.empty(pmsProduct.getStoreId())) {
            throw new ApiMallPlusException("商品编号不存在");
        }
        Integer memberRate = 0; // 会员折扣
        UmsMemberLevel memberLevel = new UmsMemberLevel();
        if (pmsProduct.getIsVip() != null && pmsProduct.getIsVip() == 1) {
            memberLevel = memberLevelService.getById(currentMember.getMemberLevelId());
            if (memberLevel != null && memberLevel.getPriviledgeMemberPrice() > 0) {
                memberRate = memberLevel.getPriviledgeMemberPrice();
            }
        }
        if (ValidatorUtils.notEmpty(cartParam.getSkuId()) && cartParam.getSkuId() > 0) {
            PmsSkuStock pmsSkuStock = skuStockMapper.selectById(cartParam.getSkuId());
            checkGoods(pmsProduct, false, cartParam.getTotal());
            checkSkuGoods(pmsSkuStock, cartParam.getTotal());
            cartItem.setProductId(pmsSkuStock.getProductId());
            cartItem.setMemberId(currentMember.getId());
            cartItem.setProductSkuId(pmsSkuStock.getId());
            OmsCartItem existCartItem = cartItemMapper.selectOne(new QueryWrapper<>(cartItem));
            if (existCartItem == null) {
                cartItem.setChecked(1);
                cartItem.setIsFenxiao(pmsProduct.getIsFenxiao());
                cartItem.setMemberId(currentMember.getId());
                if (memberRate > 0) {
                    cartItem.setPrice(pmsSkuStock.getPrice().multiply(new BigDecimal(memberRate)).divide(BigDecimal.valueOf(10)));
                } else {
                    cartItem.setPrice(pmsSkuStock.getPrice());
                }

                cartItem.setProductSkuCode(pmsSkuStock.getSkuCode());
                cartItem.setQuantity(cartParam.getTotal());
                cartItem.setProductAttr(pmsSkuStock.getMeno());
                if (ValidatorUtils.empty(pmsSkuStock.getPic())) {
                    cartItem.setProductPic(pmsProduct.getPic());
                } else {
                    cartItem.setProductPic(pmsSkuStock.getPic());
                }

                cartItem.setSp1(pmsSkuStock.getSp1());
                cartItem.setSp2(pmsSkuStock.getSp2());
                cartItem.setSp3(pmsSkuStock.getSp3());
                cartItem.setProductName(pmsSkuStock.getProductName());
                cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
                cartItem.setProductBrand(pmsProduct.getBrandName());
                cartItem.setCreateDate(new Date());
                cartItem.setStoreId(pmsProduct.getStoreId());
                cartItem.setStoreName(pmsProduct.getStoreName());
                cartItem.setIsFenxiao(pmsProduct.getIsFenxiao());
                cartItemMapper.insert(cartItem);
            } else {
                if (memberRate > 0) {
                    cartItem.setPrice(pmsSkuStock.getPrice().multiply(new BigDecimal(memberRate)).divide(BigDecimal.valueOf(10)));
                } else {
                    cartItem.setPrice(pmsSkuStock.getPrice());
                }
                existCartItem.setModifyDate(new Date());
                existCartItem.setQuantity(existCartItem.getQuantity() + cartParam.getTotal());
                cartItemMapper.updateById(existCartItem);
                return new CommonResult().success(existCartItem);
            }
        } else {
            checkGoods(pmsProduct, true, cartParam.getTotal());
            cartItem.setProductId(cartParam.getGoodsId());
            cartItem.setMemberId(currentMember.getId());
            OmsCartItem existCartItem = cartItemMapper.selectOne(new QueryWrapper<>(cartItem));
            if (existCartItem == null) {
                cartItem.setChecked(1);
                if (memberRate > 0) {
                    cartItem.setPrice(pmsProduct.getPrice().multiply(new BigDecimal(memberRate)).divide(BigDecimal.valueOf(10)));
                } else {
                    cartItem.setPrice(pmsProduct.getPrice());
                }
                cartItem.setProductName(pmsProduct.getName());
                cartItem.setQuantity(cartParam.getTotal());
                cartItem.setProductPic(pmsProduct.getPic());
                cartItem.setCreateDate(new Date());
                cartItem.setMemberId(currentMember.getId());
                cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
                cartItem.setProductBrand(pmsProduct.getBrandName());
                cartItem.setStoreId(pmsProduct.getStoreId());
                cartItem.setStoreName(pmsProduct.getStoreName());
                cartItem.setIsFenxiao(pmsProduct.getIsFenxiao());
                cartItemMapper.insert(cartItem);
            } else {
                if (memberRate > 0) {
                    existCartItem.setPrice(pmsProduct.getPrice().multiply(new BigDecimal(memberRate)).divide(BigDecimal.valueOf(10)));
                } else {
                    existCartItem.setPrice(pmsProduct.getPrice());
                }
                existCartItem.setModifyDate(new Date());
                existCartItem.setQuantity(existCartItem.getQuantity() + cartParam.getTotal());
                cartItemMapper.updateById(existCartItem);
                return new CommonResult().success(existCartItem);
            }
        }
        return new CommonResult().success(cartItem);
    }

    private void checkGoods(PmsProduct goods, boolean falg, int count) {
        if (goods == null || goods.getId() == null) {
            throw new ApiMallPlusException("商品已删除");
        }
        if (falg && (goods.getStock() <= 0 || goods.getStock() < count)) {
            throw new ApiMallPlusException("库存不足!");
        }
    }

    private void checkSkuGoods(PmsSkuStock goods, int count) {
        if (goods == null || goods.getId() == null) {
            throw new ApiMallPlusException("商品已删除");
        }
        if (goods.getStock() <= 0 || goods.getStock() < count) {
            throw new ApiMallPlusException("库存不足!");
        }
    }

    @Override
    public CommonResult generateStoreOrder(OrderParam orderParam) {
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        String type = orderParam.getType();
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }

        // 1. 获取购物车商品
        List<OmsCartItem> cartPromotionItemList = new ArrayList<>();

        if (ValidatorUtils.empty(orderParam.getAddressId())) {
            return new CommonResult().failed("address is null");
        }
        //团购订单
        if (orderParam.getOrderType() == 3) {
            SmsGroupActivity smsGroupActivity = smsGroupActivityService.getById(orderParam.getGroupActivityId());
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                        Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                if (productList != null && productList.size() > 0) {
                    //  order.setFreightAmount(smsGroupActivity.getTransfee());
                    //获取购物车信息
                    cartPromotionItemList = goodsToCartList(productList);
                }
            }
        } else {
            if ("3".equals(type)) { // 1 商品详情 2 勾选购物车 3全部购物车的商品
                cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
            }
            if ("1".equals(type)) {

                OmsCartItem omsCartItem = getOmsCartItemByGoodsDetail(orderParam, currentMember);
                List<OmsCartItem> list = new ArrayList<>();
                if (omsCartItem != null) {
                    list.add(omsCartItem);
                } else {
                    throw new ApiMallPlusException("订单已提交");
                }
                if (!CollectionUtils.isEmpty(list)) {
                    cartPromotionItemList = cartItemService.calcCartPromotion(list);
                }
            } else if ("2".equals(type)) {
                String cart_id_list1 = orderParam.getCartIds();
                String[] ids1 = cart_id_list1.split(",");
                List<Long> resultList = new ArrayList<>(ids1.length);
                for (String s : ids1) {
                    resultList.add(Long.valueOf(s));
                }
                cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), resultList);
                if (cartPromotionItemList == null || cartPromotionItemList.size() < 1) {
                    throw new ApiMallPlusException("没有下单的商品！");
                }
                Map<Integer, List<OmsCartItem>> map = cartPromotionItemList.stream().collect(Collectors.groupingBy(OmsCartItem::getStoreId));
                Map<String, Object> result = new HashMap<>();

                if (map.size() > 1) {
                    BookOrder bookOrder1 = new BookOrder(orderParam, currentMember, cartPromotionItemList, null, false).invoke();
                    if (bookOrder1.is())
                        return new CommonResult().failed("没有下单的商品");
                    OmsOrder pidOrder = bookOrder1.order;
                    List<OmsOrderItem> orderItemList = bookOrder1.orderItemList;
                    Long pid = pidOrder.getId();
                    for (Map.Entry<Integer, List<OmsCartItem>> entry : map.entrySet()) {
                        BookOrder bookOrder = new BookOrder(orderParam, currentMember, entry.getValue(), pid, true).invoke();
                        if (bookOrder.is())
                            return new CommonResult().failed("没有下单的商品");
                    }
                    result.put("order", pidOrder);
                    result.put("orderItemList", orderItemList);
                    return new CommonResult().success("下单成功", result);
                }
            } else if ("6".equals(type)) { // 秒杀
                SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
                PmsProduct product = productService.getById(relation.getProductId());
                OmsCartItem omsCartItem = new OmsCartItem();
                omsCartItem.setQuantity(orderParam.getTotal());
                if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                    throw new ApiMallPlusException("超过秒杀个数！");
                }
                omsCartItem.setPrice(relation.getFlashPromotionPrice());
                omsCartItem.setProductBrand(product.getBrandId() + "");
                omsCartItem.setProductCategoryId(product.getProductCategoryId());
                omsCartItem.setProductName(product.getName());
                omsCartItem.setProductPic(product.getPic());
                omsCartItem.setProductId(product.getId());
                omsCartItem.setChecked(1);
                omsCartItem.setProductSn(product.getProductSn());
                cartPromotionItemList.add(omsCartItem);
            }
        }

        BookOrder bookOrder = new BookOrder(orderParam, currentMember, cartPromotionItemList, null, true).invoke();
        if (bookOrder.is())
            return new CommonResult().failed("没有下单的商品");
        List<OmsOrderItem> orderItemList = bookOrder.getOrderItemList();
        OmsOrder order = bookOrder.order;
        String name = bookOrder.getName();
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);

        if (ValidatorUtils.empty(orderParam.getFormId())) {
            push(currentMember, order, orderParam.getPage(), orderParam.getFormId(), name);
        }
        return new CommonResult().success("下单成功", result);
    }

    /**
     *  商品详情购买 转换微购物车数据
     * @param orderParam
     * @param currentMember
     * @return
     */
    private OmsCartItem getOmsCartItemByGoodsDetail(OrderParam orderParam, UmsMember currentMember) {
        Long goodsId = orderParam.getGoodsId();
        Long skuId = orderParam.getSkuId();
        if (goodsId == null) {
            throw new ApiMallPlusException("参数为空");
        }
        OmsCartItem omsCartItem = new OmsCartItem();
        PmsProduct product = productService.getById(goodsId);
        Integer memberRate = 0; // 会员折扣
        UmsMemberLevel memberLevel = new UmsMemberLevel();
        /*if (product.getIsVip() != null && product.getIsVip() == 1) {
            memberLevel = memberLevelService.getById(currentMember.getMemberLevelId());
            if (memberLevel != null && memberLevel.getPriviledgeMemberPrice() > 0) {
                memberRate = memberLevel.getPriviledgeMemberPrice();
            }
        }*/
        if (memberRate > 0) {
            omsCartItem.setPrice(product.getPrice().multiply(new BigDecimal(memberRate)).divide(BigDecimal.valueOf(10)));
        } else {
            omsCartItem.setPrice(product.getPrice());
        }
        omsCartItem.setProductPic(product.getPic());
        if (ValidatorUtils.notEmpty(orderParam.getSkuId()) && orderParam.getSkuId()>0) {
            PmsSkuStock skuStock = skuStockMapper.selectById(skuId);
            if (memberRate > 0) {
                omsCartItem.setPrice(skuStock.getPrice().multiply(new BigDecimal(memberRate)).divide(BigDecimal.valueOf(10)));
            } else {
                omsCartItem.setPrice(skuStock.getPrice());
            }
            omsCartItem.setProductPic(skuStock.getPic());
            omsCartItem.setProductSkuId(skuId);
            omsCartItem.setProductSkuCode(skuStock.getSkuCode());
            omsCartItem.setProductAttr(skuStock.getMeno());
            omsCartItem.setSp1(skuStock.getSp1());
            omsCartItem.setSp2(skuStock.getSp2());
            omsCartItem.setSp3(skuStock.getSp3());
        }
        omsCartItem.setIsFenxiao(product.getIsFenxiao());
        omsCartItem.setProductId(product.getId());
        omsCartItem.setChecked(1);
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setProductBrand(product.getBrandName());
        omsCartItem.setProductCategoryId(product.getProductCategoryId());
        omsCartItem.setProductName(product.getName());
        omsCartItem.setProductSn(product.getProductSn());
        omsCartItem.setQuantity(orderParam.getTotal());
        omsCartItem.setMemberId(currentMember.getId());
        omsCartItem.setStoreId(product.getStoreId());
        omsCartItem.setStoreName(product.getStoreName());
        return omsCartItem;
    }

    @Transactional
    @Override
    public Object applyRe(ApplyRefundVo itemss) {
        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null || currentMember.getId() == null) {
            return new CommonResult().fail(100);
        }
        try {
            if (ValidatorUtils.empty(itemss.getItems()) || itemss.getItems().split(",").length < 1) {
                return new CommonResult().failed("添加失败，请勾选需要售后的商品");
            }
            for (String id : itemss.getItems().split(",")) {
                OmsOrderItem item = orderItemService.getById(id);
                OmsOrder order = orderMapper.selectById(item.getOrderId());
                if (order==null){
                    return new CommonResult().failed("订单不存在");
                }
                if (order.getOrderType().equals(AllEnum.OrderType.PIN_GROUP.code())){
                    if (order.getStatus()!=OrderStatus.PINTUAN_ORDER.getValue()){
                        return new CommonResult().failed("拼团订单已成功，不能售后操作");
                    }else {
                      //  SmsGroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<SmsGroupMember>().eq("order_id",order.getId()));

                    }
                }
                OmsOrderReturnApply apply = new OmsOrderReturnApply();
                UmsMember member = memberService.getNewCurrentMember();
                apply.setStatus(AllEnum.OmsOrderReturnApplyStatus.INIT.code());
                apply.setCreateTime(new Date());
                apply.setReturnAmount(itemss.getReturnAmount());
                apply.setDescription(itemss.getDesc());
                apply.setOrderId(item.getOrderId());
                apply.setOrderSn(item.getOrderSn());
                apply.setMemberUsername(member.getUsername());
                apply.setProductAttr(item.getProductAttr());
                apply.setProductCount(item.getProductQuantity());
                apply.setProductBrand(item.getProductBrand());
                apply.setProductId(item.getProductId());
                apply.setProductName(item.getProductName());
                apply.setProductPic(item.getProductPic());
                apply.setProductPrice(item.getRealAmount());
                apply.setProductRealPrice(item.getProductPrice());
                apply.setProofPics(org.apache.commons.lang3.StringUtils.join(itemss.getImages(), ","));
                apply.setReason(itemss.getDesc());
                apply.setType(itemss.getType());
                apply.setReturnPhone(member.getPhone());
                apply.setReturnName(member.getId()+"");
                orderReturnApplyMapper.insert(apply);

                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(item.getOrderId());
                history.setCreateTime(new Date());
                history.setOperateMan("shop");
                //  //0换货 1退钱 2退货3 退钱退货
             if (apply.getType()==0){
                 history.setNote("换货");
             }else  if (apply.getType()==1){
                 history.setNote("申请退款");
             }else  if (apply.getType()==2){
                 history.setNote("申请退货");
             }else  if (apply.getType()==3){
                 history.setNote("申请退货退款");
             }

            orderOperateHistoryService.save(history);
            }

        } catch (Exception e) {
            return new CommonResult().failed("已经存在售后记录");
        }

        return new CommonResult().success();
    }

    private class BookOrder {
        private Long pid;
        // 合成订单不插入店铺编号，不属于某个商户
        private Boolean flagStore;
        private boolean myResult;
        private OrderParam orderParam;
        private UmsMember currentMember;
        private OmsOrder order;
        private List<OmsCartItem> cartPromotionItemList;

        private List<OmsOrderItem> orderItemList;
        private String name;

        public BookOrder(OrderParam orderParam, UmsMember currentMember, List<OmsCartItem> cartPromotionItemList, Long pid, Boolean flagStore) {
            this.orderParam = orderParam;
            this.currentMember = currentMember;
            this.cartPromotionItemList = cartPromotionItemList;
            this.pid = pid;
            order = new OmsOrder();
            this.flagStore = flagStore;

        }

        boolean is() {
            return myResult;
        }

        public List<OmsOrderItem> getOrderItemList() {
            return orderItemList;
        }

        public String getName() {
            return name;
        }

        public BookOrder invoke() {
            if (cartPromotionItemList == null || cartPromotionItemList.size() < 1) {
                myResult = true;
                return this;
            }
            orderItemList = new ArrayList<>();
            //获取购物车及优惠信息
            name = "";
            BigDecimal transFee = BigDecimal.ZERO;
            List<OmsCartItem> newCartItemList = new ArrayList<>();
            Integer isFirst = 1;
            Integer storeId = 0;
            String storeName = null;
            BigDecimal totalAmount = BigDecimal.ZERO;  // 商品总价
            BigDecimal vipPrice = BigDecimal.ZERO; // 会员折扣优惠总价
            BigDecimal singVipPrice = BigDecimal.ZERO; // 会员折扣优惠
            int memberRate = 10;
            UmsMemberLevel memberLevel = memberLevelService.getById(currentMember.getMemberLevelId());
            if (memberLevel != null && memberLevel.getPriviledgeMemberPrice() > 0) {
                memberRate = memberLevel.getPriviledgeMemberPrice();
            }
            // 2. 校验商品库存，舍弃商品不存或没有库存 计算运费
            for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
                boolean flag = false;
                PmsProduct goods = productService.getById(cartPromotionItem.getProductId());

                if (!ValidatorUtils.empty(cartPromotionItem.getProductSkuId()) && cartPromotionItem.getProductSkuId() > 0) {
                    if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() > cartPromotionItem.getQuantity()) {
                        PmsSkuStock skuStock = skuStockMapper.selectById(cartPromotionItem.getProductSkuId());
                        if (skuStock.getStock() > 0 && skuStock.getStock() >= cartPromotionItem.getQuantity()) {
                            flag = true;
                            if (goods.getIsVip() != null && goods.getIsVip() == 1) {
                                vipPrice = vipPrice.add(skuStock.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10)))).setScale(2);
                                singVipPrice = skuStock.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10))).setScale(2);
                            }
                            totalAmount = totalAmount.add(skuStock.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
                        }
                    }
                } else {
                    if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() >= cartPromotionItem.getQuantity()) {
                        flag = true;
                        if (goods.getIsVip() != null && goods.getIsVip() == 1) {
                            singVipPrice = goods.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10))).setScale(2);
                            vipPrice = vipPrice.add(goods.getPrice().multiply(new BigDecimal((10 - memberRate)).divide(new BigDecimal(10)))).setScale(2);
                        }
                        totalAmount = totalAmount.add(goods.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
                    }
                }
                if (flag) {
                    if (ValidatorUtils.notEmpty(pid)) {
                        if (goods.getTransfee().compareTo(transFee) > 0) {
                            transFee = goods.getTransfee();
                        }
                    }
                    //生成下单商品信息
                    OmsOrderItem orderItem = createOrderItem(cartPromotionItem);
                    orderItem.setType(AllEnum.OrderItemType.GOODS.code());
                    orderItem.setPromotionAmount(singVipPrice);
                    orderItem.setRealAmount(goods.getPrice());
                    orderItem.setIsFenxiao(goods.getIsFenxiao());
                    orderItemList.add(orderItem);
                    if (isFirst == 1) {
                        storeId = cartPromotionItem.getStoreId();
                        storeName = cartPromotionItem.getStoreName();
                        name = cartPromotionItem.getProductName();
                        order.setGoodsId(cartPromotionItem.getProductId());
                        order.setGoodsName(cartPromotionItem.getProductName());
                    }
                    isFirst++;
                    newCartItemList.add(cartPromotionItem);
                }
            }
            if (ValidatorUtils.empty(pid)) {

                BigDecimal transFee1 = BigDecimal.ZERO;
                Map<Integer, List<OmsCartItem>> map = cartPromotionItemList.stream().collect(Collectors.groupingBy(OmsCartItem::getStoreId));

                for (Map.Entry<Integer, List<OmsCartItem>> entry : map.entrySet()) {
                    List<OmsCartItem> list = entry.getValue();
                    for (OmsCartItem cartPromotionItem : list) {
                        PmsProduct goods = productService.getById(cartPromotionItem.getProductId());
                        if (goods.getTransfee().compareTo(transFee1) > 0) {
                            transFee1 = goods.getTransfee();
                        }
                    }
                    transFee = transFee.add(transFee1);
                }

            }
            if (newCartItemList == null || newCartItemList.size() < 1) {
                myResult = true;
                return this;
            }

            //3.计算优惠券
            SmsCouponHistory couponHistory = null;
            SmsCoupon coupon = null;
            if (orderParam.getCouponId() != null) {
                couponHistory = couponHistoryService.getById(orderParam.getMemberCouponId());
                coupon = couponService.getById(orderParam.getCouponId());
            }

            //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额

            createOrderObj(order, orderParam, currentMember, orderItemList, orderParam.getAddressId());
            if (orderParam.getOrderType() != 3) {
                order.setFreightAmount(transFee);
            }
            if (orderParam.getCouponId() == null || orderParam.getCouponId() == 0) {
                order.setCouponAmount(new BigDecimal(0));
            } else {
                order.setCouponId(orderParam.getCouponId());
                order.setCouponAmount(coupon.getAmount());
            }
            //获取积分使用规则
       /* UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectOne(new QueryWrapper<>());

        if (integrationConsumeSetting!=null && currentMember.getIntegration()>0){
            order.setUseIntegration(currentMember.getIntegration()*integrationConsumeSetting.getMaxPercentPerOrder()/100);
            order.setIntegrationAmount(BigDecimal.valueOf((currentMember.getIntegration()*integrationConsumeSetting.getMaxPercentPerOrder()/100/integrationConsumeSetting.getDeductionPerAmount())));
        }*/

            /*CartMarkingVo vo = new CartMarkingVo();
            vo.setCartList(newCartItemList);
            SmsBasicMarking basicMarking = basicMarkingService.matchOrderBasicMarking(vo);
            log.info("basicMarking=" + com.alibaba.fastjson.JSONObject.toJSONString(basicMarking));
            if (basicMarking != null) {
                order.setPromotionAmount(basicMarking.getMinAmount());
            }

            if (ValidatorUtils.notEmpty(orderParam.getBasicGiftsVar())) {
                String[] basicGiftsList = orderParam.getBasicGiftsVar().split("@");
                if (basicGiftsList != null && basicGiftsList.length > 0) {
                    for (String basicGifts : basicGiftsList) {
                        if (ValidatorUtils.notEmpty(basicGifts)) {
                            String[] beanKv = basicGifts.split(":");
                            if (beanKv != null && beanKv.length > 1) {
                                String[] ids = beanKv[1].split(",");
                                if (ids != null && ids.length > 0) {
                                    for (String id : ids) {
                                        PmsGifts pmsGifts = pmsGiftsService.getById(id);
                                        if (pmsGifts != null) {
                                            OmsOrderItem orderItem = new OmsOrderItem();
                                            orderItem.setOrderSn(beanKv[0]);
                                            orderItem.setProductId(pmsGifts.getId());
                                            orderItem.setProductName(pmsGifts.getTitle());
                                            orderItem.setProductPic(pmsGifts.getIcon());
                                            orderItem.setProductPrice(pmsGifts.getPrice());
                                            orderItem.setProductQuantity(1);
                                            orderItem.setType(AllEnum.OrderItemType.GIFT.code());
                                            orderItemList.add(orderItem);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }*/
            /**
             * 商品总价
             */
            order.setTotalAmount(totalAmount);
            order.setVipAmount(vipPrice);
            order.setPayAmount(calcPayAmount(order));
            if (order.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
                order.setPayAmount(new BigDecimal("0.01"));
            }
            // TODO: 2018/9/3 bill_*,delivery_*
            if (pid != null) {
                order.setPid(pid);
            }
            if (flagStore) {
                if (ValidatorUtils.empty(storeId)) {
                    throw new ApiMallPlusException("商品编号不存在");
                }
                order.setStoreId(storeId);
                order.setStoreName(storeName);
            }
            //插入order表和order_item表
            orderService.save(order);
            for (OmsOrderItem orderItem : orderItemList) {
                if (flagStore) {
                    orderItem.setStoreId(storeId);
                }
                orderItem.setOrderId(order.getId());
                orderItem.setOrderSn(order.getOrderSn());
                if (ValidatorUtils.notEmpty(orderParam.getSkillId())) {
                    orderItem.setGiftIntegration(orderParam.getSkillId().intValue());
                }
            }
            orderItemService.saveBatch(orderItemList);


            //如使用优惠券更新优惠券使用状态
            if (orderParam.getCouponId() != null && orderParam.getCouponId() > 0) {
                couponHistory.setUseStatus(1);
                couponHistory.setUseTime(new Date());
                couponHistory.setOrderId(order.getId());
                couponHistory.setOrderSn(order.getOrderSn());
                couponHistoryService.updateById(couponHistory);
            }
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(order.getId());
            history.setCreateTime(new Date());
            history.setOperateMan("shop");
            history.setPreStatus(-1);
            history.setOrderStatus(OrderStatus.INIT.getValue());
            history.setNote("创建订单");
            orderOperateHistoryService.save(history);

            //如使用积分需要扣除积分
            if (order.getUseIntegration() != null) {
                memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - order.getUseIntegration());
            }
            if (pid == null) {
                lockStockByOrder(orderItemList, orderParam.getType());
                //删除购物车中的下单商品
                if (!"1".equals(orderParam.getType())){
                    deleteCartItemList(cartPromotionItemList, currentMember);
                }
            }
            myResult = false;
            return this;
        }
    }
}
