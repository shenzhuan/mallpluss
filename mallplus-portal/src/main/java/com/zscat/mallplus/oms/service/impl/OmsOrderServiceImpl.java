package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import com.zscat.mallplus.ums.service.IUmsMemberBlanceLogService;
import com.zscat.mallplus.ums.service.IUmsMemberReceiveAddressService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.RedisService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ????????? ???????????????
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

    @Override
    public int payOrder(TbThanks tbThanks) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        tbThanks.setTime(time);
        tbThanks.setDate(new Date());
        /*TbMember tbMember=tbMemberMapper.selectByPrimaryKey(Long.valueOf(tbThanks.getUserId()));
        if(tbMember!=null){
            tbThanks.setUsername(tbMember.getUsername());
        }
        if(tbThanksMapper.insert(tbThanks)!=1){
            throw new XmallException("??????????????????????????????");
        }*/

        //????????????????????????
        OmsOrder tbOrder = orderMapper.selectById(tbThanks.getOrderId());
        if (tbOrder == null) {
            throw new ApiMallPlusException("???????????????");
        }
        tbOrder.setStatus(OrderStatus.TO_DELIVER.getValue());
        tbOrder.setPayType(tbThanks.getPayType());
        tbOrder.setPaymentTime(new Date());
        tbOrder.setModifyTime(new Date());
        if (orderMapper.updateById(tbOrder) != 1) {
            throw new ApiMallPlusException("??????????????????");
        }
        //????????????????????????????????????????????????????????????
        OmsOrderItem queryO = new OmsOrderItem();
        queryO.setOrderId(tbThanks.getOrderId());
        List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));

        int count = orderMapper.updateSkuStock(list);
        //????????????????????????
        String tokenName = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString();

        // emailUtil.sendEmailDealThank(EMAIL_SENDER,"???mallcloud??????????????????????????????",tokenName,token,tbThanks);
        return count;
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        //????????????????????????
        OmsOrderSetting orderSetting = orderSettingMapper.selectById(1L);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        //??????????????????
        //  cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    @Override
    public Object preGroupActivityOrder(OrderParam orderParam) {
        SmsGroupActivity smsGroupActivity = smsGroupActivityService.getById(orderParam.getGroupId());
        if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
            List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                    Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            if (productList != null && productList.size() > 0) {
                UmsMember currentMember = memberService.getNewCurrentMember();
                ConfirmOrderResult result = new ConfirmOrderResult();
                // ??????
                BigDecimal transFee = BigDecimal.ZERO;
                //?????????????????????
                List<OmsCartItem> list = new ArrayList<>();
                for (PmsProduct product : productList) {
                    if (product == null && product.getId() == null) {
                        return new CommonResult().failed("????????????");
                    }
                    if (product != null && product.getStock() < 1) {
                        return new CommonResult().failed("????????????");
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
                //??????????????????????????????
                UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
                if (currentMember == null) {
                    return new CommonResult().fail(100);
                }
                queryU.setMemberId(currentMember.getId());
                List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
                result.setMemberReceiveAddressList(memberReceiveAddressList);
                UmsMemberReceiveAddress address = addressService.getDefaultItem();
                //?????????????????????????????????
                List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(list, 1);
                result.setCouponHistoryDetailList(couponHistoryDetailList);
                //??????????????????
                result.setMemberIntegration(currentMember.getIntegration());
                result.setBlance(currentMember.getBlance());

                //????????????????????????
                UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
                result.setIntegrationConsumeSetting(integrationConsumeSetting);
                //?????????????????????????????????????????????
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
        if ("3".equals(type)) { // 1 ???????????? 2 ??????????????? 3????????????????????????
            list = cartItemService.list(currentMember.getId(), null);
        } else if ("1".equals(type)) {
            String cartId = orderParam.getCartId();
            if (org.apache.commons.lang.StringUtils.isBlank(cartId)) {
                throw new ApiMallPlusException("????????????");
            }
            OmsCartItem omsCartItem = cartItemService.selectById(Long.valueOf(cartId));
            if (omsCartItem == null) {
                return null;
            }
            list.add(omsCartItem);
        } else if ("2".equals(type)) {
            String cart_id_list1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cart_id_list1)) {
                throw new ApiMallPlusException("????????????");
            }
            String[] ids1 = cart_id_list1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            list = cartItemService.list(currentMember.getId(), resultList);
        } else if ("6".equals(type)) { // ??????
            SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
            PmsProduct product = productService.getById(relation.getProductId());
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setQuantity(orderParam.getTotal());
            if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                throw new ApiMallPlusException("?????????????????????");
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

        //?????????????????????????????????
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(list, 1);
        return new CommonResult().success(couponHistoryDetailList);
    }

    /**
     * @return
     */
    @Override
    public ConfirmOrderResult submitPreview(OrderParam orderParam) {
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        String type = orderParam.getType();
        StopWatch stopWatch = new StopWatch("??????orderType=" + orderParam.getOrderType());
        stopWatch.start("1. ?????????????????????");
        UmsMember currentMember = memberService.getNewCurrentMember();
        List<OmsCartItem> list = new ArrayList<>();
        if ("3".equals(type)) { // 1 ???????????? 2 ??????????????? 3????????????????????????
            list = cartItemService.list(currentMember.getId(), null);
        } else if ("1".equals(type)) {
            String cartId = orderParam.getCartId();
            if (org.apache.commons.lang.StringUtils.isBlank(cartId)) {
                throw new ApiMallPlusException("????????????");
            }
            OmsCartItem omsCartItem = cartItemService.selectById(Long.valueOf(cartId));
            if (omsCartItem == null) {
                return null;
            }
            list.add(omsCartItem);
        } else if ("2".equals(type)) {
            String cart_id_list1 = orderParam.getCartIds();
            if (org.apache.commons.lang.StringUtils.isBlank(cart_id_list1)) {
                throw new ApiMallPlusException("????????????");
            }
            String[] ids1 = cart_id_list1.split(",");
            List<Long> resultList = new ArrayList<>(ids1.length);
            for (String s : ids1) {
                resultList.add(Long.valueOf(s));
            }
            list = cartItemService.list(currentMember.getId(), resultList);
        } else if ("6".equals(type)) { // ??????
            SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
            PmsProduct product = productService.getById(relation.getProductId());
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setQuantity(orderParam.getTotal());
            if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                throw new ApiMallPlusException("?????????????????????");
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
            throw new ApiMallPlusException("???????????????");
        }
        List<OmsCartItem> newCartList = new ArrayList<>();
        // ?????????????????????
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
        stopWatch.start("??????????????????");
        ConfirmOrderResult result = new ConfirmOrderResult();
        //?????????????????????
        CartMarkingVo vo = new CartMarkingVo();
        vo.setCartList(newCartList);
        //????????????
        int firstOrder = orderMapper.selectCount(new QueryWrapper<OmsOrder>().eq("member_id", currentMember.getId()));
        vo.setType(1);
        if (firstOrder > 0) {
            vo.setType(2);
        }
        List<SmsBasicGifts> basicGiftsList = basicGiftsService.matchOrderBasicGifts(vo);
        log.info(com.alibaba.fastjson.JSONObject.toJSONString(basicGiftsList));
        result.setBasicGiftsList(basicGiftsList);
        stopWatch.stop();
        stopWatch.start("??????????????????");
        result.setCartPromotionItemList(newCartList);
        //??????????????????????????????
        UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
        queryU.setMemberId(currentMember.getId());
        List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        UmsMemberReceiveAddress address = addressService.getDefaultItem();
        //?????????????????????????????????
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(newCartList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        //??????????????????

        result.setBlance(currentMember.getBlance());

        //????????????????????????
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectOne(new QueryWrapper<>());

        if (integrationConsumeSetting != null && currentMember.getIntegration() > 0) {
            result.setMemberIntegration(currentMember.getIntegration() * integrationConsumeSetting.getMaxPercentPerOrder() / 100);
            result.setIntegrationAmount(BigDecimal.valueOf((currentMember.getIntegration() * integrationConsumeSetting.getMaxPercentPerOrder() / 100 / integrationConsumeSetting.getDeductionPerAmount())));
        }

        //?????????????????????????????????????????????
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
    public ConfirmListOrderResult submitStorePreview(OrderParam orderParam) {
        return null;
    }

    @Override
    public CommonResult generateOrder(OrderParam orderParam) {

        String type = orderParam.getType();
        UmsMember currentMember = memberService.getNewCurrentMember();
        OmsOrder order = new OmsOrder();
        // 1. ?????????????????????
        List<OmsCartItem> cartPromotionItemList = new ArrayList<>();
        StopWatch stopWatch = new StopWatch("??????orderType=" + orderParam.getOrderType());
        stopWatch.start("1. ?????????????????????");
        if (ValidatorUtils.empty(orderParam.getAddressId())) {
            return new CommonResult().failed("address is null");
        }
        //????????????
        SmsGroupActivity smsGroupActivity = null;
        if (orderParam.getOrderType() == 3) {
            smsGroupActivity = smsGroupActivityService.getById(orderParam.getGroupActivityId());
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                        Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                if (productList != null && productList.size() > 0) {
                    order.setFreightAmount(smsGroupActivity.getTransfee());
                    //?????????????????????
                    cartPromotionItemList = goodsToCartList(productList);
                }
            }
        } else {
            if ("3".equals(type)) { // 1 ???????????? 2 ??????????????? 3????????????????????????
                cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
            }
            if ("1".equals(type)) {
                Long cartId = Long.valueOf(orderParam.getCartId());
                OmsCartItem omsCartItem = cartItemService.selectById(cartId);
                List<OmsCartItem> list = new ArrayList<>();
                if (omsCartItem != null) {
                    list.add(omsCartItem);
                } else {
                    throw new ApiMallPlusException("???????????????");
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
            } else if ("6".equals(type)) { // ??????
                SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
                PmsProduct product = productService.getById(relation.getProductId());
                OmsCartItem omsCartItem = new OmsCartItem();
                if (ValidatorUtils.empty(orderParam.getTotal())) {
                    orderParam.setTotal(1);
                }
                omsCartItem.setQuantity(orderParam.getTotal());
                if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                    throw new ApiMallPlusException("?????????????????????");
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
            return new CommonResult().failed("?????????????????????");
        }
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        //??????????????????????????????
        String name = "";
        BigDecimal transFee = BigDecimal.ZERO;
        List<OmsCartItem> newCartItemList = new ArrayList<>();
        Integer isFirst = 1;
        stopWatch.start("2. ?????????????????????????????????????????????????????? ????????????");
        // 2. ?????????????????????????????????????????????????????? ????????????
        for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
            boolean flag = false;
            PmsProduct goods = productService.getById(cartPromotionItem.getProductId());
            if (!ValidatorUtils.empty(cartPromotionItem.getProductSkuId()) && cartPromotionItem.getProductSkuId() > 0) {
                if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() > cartPromotionItem.getQuantity()) {
                    PmsSkuStock skuStock = skuStockMapper.selectById(cartPromotionItem.getProductSkuId());
                    if (skuStock.getStock() > 0 && skuStock.getStock() >= cartPromotionItem.getQuantity()) {
                        flag = true;
                    }
                }
            } else {
                if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() >= cartPromotionItem.getQuantity()) {
                    flag = true;
                }
            }
            if (flag) {
                if (goods.getTransfee().compareTo(transFee) > 0) {
                    transFee = goods.getTransfee();
                }
                //????????????????????????
                OmsOrderItem orderItem = createOrderItem(cartPromotionItem);
                orderItem.setStatus(OrderStatus.TO_DELIVER.getValue());
                orderItem.setType(AllEnum.OrderItemType.GOODS.code());
                orderItemList.add(orderItem);
                if (isFirst == 1) {
                    name = cartPromotionItem.getProductName();
                    order.setGoodsId(cartPromotionItem.getProductId());
                    order.setGoodsName(cartPromotionItem.getProductName());
                }

                newCartItemList.add(cartPromotionItem);
            }
        }
        if (newCartItemList == null || newCartItemList.size() < 1) {
            return new CommonResult().failed("?????????????????????");
        }

        //3.???????????????
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        SmsCoupon coupon = null;
        if (orderParam.getCouponId() != null) {
            //   couponHistory = couponHistoryService.getById(orderParam.getMemberCouponId());
            coupon = couponService.getById(orderParam.getCouponId());
        }
        UmsMemberReceiveAddress address = addressService.getById(orderParam.getAddressId());
        //?????????????????????????????????????????????????????????????????????????????????

        createOrderObj(order, orderParam, currentMember, orderItemList, address);
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
        //????????????????????????
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
        order.setPayAmount(calcPayAmount(order));
        if (order.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            order.setPayAmount(new BigDecimal("0.01"));
        }
        stopWatch.stop();

        stopWatch.start("3.??????????????????");
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
        }

        stopWatch.stop();
        stopWatch.start("4.??????????????? ????????????");
        // TODO: 2018/9/3 bill_*,delivery_*
        //??????order??????order_item???
        orderService.save(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
            if (ValidatorUtils.notEmpty(orderParam.getSkillId())) {
                orderItem.setGiftIntegration(orderParam.getSkillId().intValue());
            }
        }
        orderItemService.saveBatch(orderItemList);
        // ????????????
        for (OmsCartItem cartPromotionItem : newCartItemList) {
            if (ValidatorUtils.notEmpty(cartPromotionItem.getIsFenxiao()) && cartPromotionItem.getIsFenxiao() == 1) {
                FenxiaoConfig fenxiaoConfig = fenxiaoConfigMapper.selectOne(new QueryWrapper<>());
                if (fenxiaoConfig != null && fenxiaoConfig.getStatus() == 1 && ValidatorUtils.notEmpty(currentMember.getInvitecode()) && fenxiaoConfig.getOnePercent() > 0) {
                    //  UmsMember member = memberService.getById(currentMember.getInvitecode());
                    //?????? ??????
                    FenxiaoRecords records1 = new FenxiaoRecords();
                    records1.setCreateTime(new Date());
                    records1.setStatus("1");
                    records1.setGoodsId(cartPromotionItem.getProductId());
                  //  records1.setStoreId(cartPromotionItem.getStoreId());
                    records1.setLevel("1");
                    records1.setType(fenxiaoConfig.getType());
                    records1.setMemberId(Long.valueOf(currentMember.getInvitecode()));
                    records1.setInviteId(currentMember.getId());
                    records1.setOrderId(order.getId());
                    records1.setMoney(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity()).multiply(new BigDecimal(fenxiaoConfig.getOnePercent()))).divide(BigDecimal.valueOf(100)));
                    fenxiaoRecordsMapper.insert(records1);
                    //?????? ??????
                    UmsMember member = memberService.getById(currentMember.getInvitecode());
                    if (member != null && ValidatorUtils.notEmpty(currentMember.getInvitecode()) && fenxiaoConfig.getTwoPercent() > 0) {
                        FenxiaoRecords records = new FenxiaoRecords();
                        records.setCreateTime(new Date());
                        records.setStatus("1");
                        records.setGoodsId(cartPromotionItem.getProductId());
                    //    records.setStoreId(cartPromotionItem.getStoreId());
                        records.setLevel("2");
                        records.setType(fenxiaoConfig.getType());
                        records.setMemberId(Long.valueOf(member.getInvitecode()));
                        records.setInviteId(member.getId());
                        records.setOrderId(order.getId());
                        records.setMoney(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity()).multiply(new BigDecimal(fenxiaoConfig.getTwoPercent()))).divide(BigDecimal.valueOf(100)));
                        fenxiaoRecordsMapper.insert(records);
                    }
                }
            }

        }
        //?????????????????????????????????????????????
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
        history.setNote("????????????");
        orderOperateHistoryService.save(history);

        memberService.addIntegration(currentMember.getId(),order.getPayAmount().intValue(), 1, "??????????????????", AllEnum.ChangeSource.order.code(), currentMember.getUsername());

        stopWatch.stop();
        //?????????????????????????????????
        if (order.getUseIntegration() != null) {
            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - order.getUseIntegration());
        }
        stopWatch.start("5.????????? ???????????????");
        lockStockByOrder(orderItemList, type);
        //?????????????????????????????????
        deleteCartItemList(cartPromotionItemList, currentMember);
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        stopWatch.stop();
        if (ValidatorUtils.empty(orderParam.getFormId())) {
            stopWatch.start("5.?????????????????????");
            push(currentMember, order, orderParam.getPage(), orderParam.getFormId(), name);
            stopWatch.stop();
        }
        log.info(stopWatch.prettyPrint());
        return new CommonResult().success("????????????", result);
    }

    @Override
    public Object quitGroup(Long id) {
        groupMemberMapper.deleteById(id);

        return new CommonResult().success("????????????");
    }

    @Override
    public CommonResult acceptGroup(OrderParam orderParam) {
        if (orderParam.getGroupType() == 2) {

        }
        OmsOrder order = new OmsOrder();

        UmsMember currentMember = memberService.getNewCurrentMember();
        if (currentMember == null) {
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
        //??????????????????????????????
        String name = "";
        BigDecimal transFee = BigDecimal.ZERO;
        for (OmsCartItem cartPromotionItem : list) {
            PmsProduct goods = productService.getById(cartPromotionItem.getProductId());
            if (!ValidatorUtils.empty(cartPromotionItem.getProductSkuId()) && cartPromotionItem.getProductSkuId() > 0) {
                checkGoods(goods, false, cartPromotionItem.getQuantity());
                PmsSkuStock skuStock = skuStockMapper.selectById(cartPromotionItem.getProductSkuId());
                checkSkuGoods(skuStock, cartPromotionItem.getQuantity());
            } else {
                checkGoods(goods, true, cartPromotionItem.getQuantity());
            }
            if (goods.getTransfee().compareTo(transFee) > 0) {
                transFee = goods.getTransfee();
            }
            //????????????????????????
            OmsOrderItem orderItem = createOrderItem(cartPromotionItem);
            orderItem.setType(AllEnum.OrderItemType.GOODS.code());
            orderItem.setStatus(OrderStatus.TO_DELIVER.getValue());
            orderItemList.add(orderItem);
        }
        //??????????????????
        lockStock(list);
        //?????????????????????????????????????????????????????????????????????????????????
        UmsMemberReceiveAddress address = addressService.getById(orderParam.getAddressId());
        createOrderObj(order, orderParam, currentMember, orderItemList, address);
        order.setMemberId(currentMember.getId());
        SmsGroup group = groupMapper.getByGoodsId(orderParam.getGoodsId());
        Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()), "yyyy-MM-dd HH:mm:ss");

        // TODO: 2018/9/3 bill_*,delivery_*
        //??????order??????order_item???
        order.setPayAmount(group.getGroupPrice().add(transFee));
        orderService.save(order);
        Long nowT = System.currentTimeMillis();
        if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
            SmsGroupMember groupMember = new SmsGroupMember();
            SmsGroupRecord groupRecord = new SmsGroupRecord();
            if (orderParam.getGroupType() == 1) { // 1 ???????????? 2 ????????????
                groupRecord.setCreateTime(new Date());
                groupRecord.setGroupId(group.getId());
                groupRecord.setStatus("1");
                groupRecordMapper.insert(groupRecord);

                groupMember.setGoodsId(orderParam.getGoodsId());
                groupMember.setName(currentMember.getNickname());
                groupMember.setMemberId(currentMember.getId());
                groupMember.setPic(currentMember.getIcon());
                groupMember.setStatus(1);
                groupMember.setOrderId(order.getId() + "");
                groupMember.setCreateTime(new Date());
                groupMember.setGroupRecordId(groupRecord.getId());
                groupMemberMapper.insert(groupMember);
            } else {
                List<SmsGroupMember> list1 = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_record_id", orderParam.getMgId()));
                if (list1 != null && list1.size() > group.getMaxPeople()) {
                    return new CommonResult().failed("???????????????????????????");
                }
                for (SmsGroupMember smsGroupMember : list1) {
                    if (smsGroupMember.getMemberId().equals(currentMember.getId())) {
                        return new CommonResult().failed("????????????????????????");
                    }
                }
                groupMember.setGoodsId(orderParam.getGoodsId());
                groupMember.setName(currentMember.getNickname());
                groupMember.setMemberId(currentMember.getId());
                groupMember.setPic(currentMember.getIcon());
                groupMember.setStatus(1);
                groupMember.setOrderId(order.getId() + "");
                groupMember.setCreateTime(new Date());
                groupMember.setGroupRecordId(orderParam.getMgId());
                groupMemberMapper.insert(groupMember);
            }
            //3.???????????????
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
            return new CommonResult().failed("??????????????????");
        }
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }

        orderItemService.saveBatch(orderItemList);
        //?????????????????????????????????????????????
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        //?????????????????????????????????
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
        return new CommonResult().success("????????????", result);
    }

    private void createCartObj(OrderParam orderParam, List<OmsCartItem> list, OmsCartItem cartItem, PmsProduct pmsProduct) {
        if (ValidatorUtils.notEmpty(orderParam.getSkuId())) {
            PmsSkuStock pmsSkuStock = skuStockMapper.selectById(orderParam.getSkuId());
            checkGoods(pmsProduct, false, 1);
            checkSkuGoods(pmsSkuStock, 1);
            cartItem.setProductId(pmsSkuStock.getProductId());
            cartItem.setMemberId(memberService.getNewCurrentMember().getId());
            cartItem.setProductSkuId(pmsSkuStock.getId());
            cartItem.setChecked(1);
            cartItem.setPrice(pmsSkuStock.getPrice());
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
            if (ValidatorUtils.notEmpty(orderParam.getGroupId())) {
                SmsGroup group = groupMapper.selectById(orderParam.getGroupId());
                if (group != null) {
                    cartItem.setPrice(group.getGroupPrice());
                } else {
                    cartItem.setPrice(pmsProduct.getPrice());
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
        //orderItem.setStoreName(cartPromotionItem.getStoreName());
        orderItem.setStatus(OrderStatus.TO_DELIVER.getValue());
           /* orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());*/
        return orderItem;
    }

    private OmsOrder createOrderObj(OmsOrder order, OrderParam orderParam, UmsMember currentMember, List<OmsOrderItem> orderItemList, UmsMemberReceiveAddress address) {
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
        //???????????????????????????????????????
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        order.setMemberId(currentMember.getId());
        //???????????????0->????????????1->????????????2->??????
        order.setPayType(orderParam.getPayType());
        //???????????????0->PC?????????5->app?????? 2 h5 3??????????????? 4 ??????????????????
        order.setSourceType(orderParam.getSource());
        //??????????????????????????????1->????????????2->????????????3->????????????4->????????????5->???????????? 6->????????????
        order.setStatus(OrderStatus.INIT.getValue());
        //???????????????0->???????????????1->????????????
        order.setOrderType(orderParam.getOrderType());
        //???????????????????????????????????????????????????
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
        //0->????????????1->?????????
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        order.setMemberId(memberService.getNewCurrentMember().getId());
        //???????????????
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
        history.setNote("????????????");
        orderOperateHistoryService.save(history);

        return orderMapper.updateById(order) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object confimDelivery(Long id) {
        OmsOrder order = this.orderMapper.selectById(id);
        if (order.getStatus() != OrderStatus.DELIVERED.getValue()) {
            return new CommonResult().paramFailed("?????????????????????????????????");
        }
        OmsOrderOperateHistory history = updateOrderInfo(id, order, OrderStatus.TO_COMMENT);
        history.setNote("????????????");
        orderOperateHistoryService.save(history);

        return new CommonResult().success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object applyRefund(Long id) {
        OmsOrder order = this.orderMapper.selectById(id);
        try {
            if (order.getStatus() > 9) {
                return new CommonResult().paramFailed("????????????????????????????????????");
            }
            OmsOrderOperateHistory history = updateOrderInfo(id, order, OrderStatus.REFUNDING);
            history.setNote("????????????");
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
                    return new CommonResult().failed("????????????");
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
                history.setNote("????????????");
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
    public OmsOrder blancePay(PayParam payParam) {
        OmsOrder order = orderMapper.selectById(payParam.getOrderId());
        UmsMember userDO = memberService.getNewCurrentMember();
        if (order.getPayAmount().compareTo(userDO.getBlance()) > 0) {
            throw new ApiMallPlusException("???????????????");
        }
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        order.setPayType(AllEnum.OrderPayType.balancePay.code());
        order.setPaymentTime(new Date());
        orderService.updateById(order);
        if (ValidatorUtils.isEmpty(order.getPid()) || order.getPid() < 1) {
            OmsOrder childOrder = new OmsOrder();
            childOrder.setStatus(OrderStatus.TO_DELIVER.getValue());
            childOrder.setPayType(AllEnum.OrderPayType.balancePay.code());
            childOrder.setPaymentTime(new Date());
            orderService.update(childOrder, new QueryWrapper<OmsOrder>().eq("pid", order.getId()));
        }
        if (ValidatorUtils.notEmpty(order.getGroupId())) {
            SmsGroupMember member = new SmsGroupMember();
            member.setId(order.getGroupId());
            member.setStatus(2);
            groupMemberMapper.updateById(member);

            SmsGroupMember groupMember = groupMemberMapper.selectById(order.getGroupId());
            SmsGroupRecord groupRecord = groupRecordMapper.selectById(groupMember.getGroupRecordId());
            SmsGroup group = groupMapper.selectById(groupRecord.getGroupId());

            List<SmsGroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_record_id", groupRecord.getId()).eq("status", 2));
            groupRecord.setList(groupMembers);
            if (groupMembers != null && groupMembers.size() == group.getMaxPeople()) {
                groupRecord.setStatus("2");
                groupRecordMapper.updateById(groupRecord);
            }

        }
        if (order.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            order.setPayAmount(new BigDecimal("0.01"));
        }
        userDO.setBlance(userDO.getBlance().subtract(order.getPayAmount()));
        memberService.updateById(userDO);
        UmsMemberBlanceLog blog = new UmsMemberBlanceLog();
        blog.setMemberId(userDO.getId());
        blog.setCreateTime(new Date());
        blog.setNote("???????????????" + order.getId());
        blog.setPrice(order.getPayAmount());
        blog.setType(1);
        memberBlanceLogService.save(blog);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(order.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(OrderStatus.INIT.getValue());
        history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
        history.setNote("????????????");
        orderOperateHistoryService.save(history);
        memberService.addIntegration(userDO.getId(), order.getPayAmount().multiply(new BigDecimal("0.1")).intValue(), 1, "????????????????????????", AllEnum.ChangeSource.order.code(), userDO.getUsername());

        return order;
    }

    @Transactional
    @Override
    public OmsOrder blancePay(OmsOrder order) {
        UmsMember userDO = memberService.getNewCurrentMember();
        if (order.getPayAmount().compareTo(userDO.getBlance()) > 0) {
            throw new ApiMallPlusException("???????????????");
        }
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        order.setPayType(AllEnum.OrderPayType.balancePay.code());
        order.setPaymentTime(new Date());
        orderService.updateById(order);
        if (ValidatorUtils.notEmpty(order.getGroupId())) {
            SmsGroupMember member = new SmsGroupMember();
            member.setId(order.getGroupId());
            member.setStatus(2);
            groupMemberMapper.updateById(member);
        }
        if (order.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            order.setPayAmount(new BigDecimal("0.01"));
        }
        userDO.setBlance(userDO.getBlance().subtract(order.getPayAmount()));
        memberService.updateById(userDO);
        UmsMemberBlanceLog blog = new UmsMemberBlanceLog();
        blog.setMemberId(userDO.getId());
        blog.setCreateTime(new Date());
        blog.setNote("???????????????" + order.getId());
        blog.setPrice(order.getPayAmount());
        blog.setType(1);
        memberBlanceLogService.save(blog);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(order.getId());
        history.setCreateTime(new Date());
        history.setOperateMan("shop");
        history.setPreStatus(OrderStatus.INIT.getValue());
        history.setOrderStatus(OrderStatus.TO_DELIVER.getValue());
        history.setNote("????????????");
        orderOperateHistoryService.save(history);
        memberService.addIntegration(userDO.getId(), order.getPayAmount().multiply(new BigDecimal("0.1")).intValue(), 1, "????????????????????????", AllEnum.ChangeSource.order.code(), userDO.getUsername());

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
        history.setNote("????????????");
        orderOperateHistoryService.save(history);

        String key = Rediskey.orderDetail + "orderid" + order.getId();
        redisService.remove(key);
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        return orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public Object jifenPay(OrderParam orderParam) {
        UmsMember member = memberService.getById(memberService.getNewCurrentMember().getId());
        PmsGifts gifts = giftsService.getById(orderParam.getGoodsId());
        if (gifts.getPrice().intValue() > member.getIntegration()) {
            return new CommonResult().failed("???????????????");
        } else {
            // ????????????
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
            order.setOrderSn(gifts.getIcon());
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
            history.setNote("????????????");
            orderOperateHistoryService.save(history);

            //??????????????????
            member.setIntegration(member.getIntegration() - gifts.getPrice().intValue());
            memberService.updateById(member);
            // ?????????????????????
            UmsIntegrationChangeHistory historyChange = new UmsIntegrationChangeHistory(member.getId(), new Date(), AllEnum.ChangeType.Min.code(), gifts.getPrice().intValue()
                    , member.getUsername(), order.getId() + "", AllEnum.ChangeSource.order.code());
            integrationChangeHistoryMapper.insert(historyChange);
            // ??????????????????
            String key = Rediskey.orderDetail + "orderid" + order.getId();
            redisService.remove(key);

        }
        return new CommonResult().success("????????????");
    }

    @Override
    public CommonResult paySuccess(Long orderId) {
        //????????????????????????
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(OrderStatus.TO_DELIVER.getValue());
        order.setPaymentTime(new Date());
        orderService.updateById(order);
        //????????????????????????????????????????????????????????????
        OmsOrderItem queryO = new OmsOrderItem();
        queryO.setOrderId(orderId);
        String key = Rediskey.orderDetail + "orderid" + orderId;
        redisService.remove(key);
        List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
        int count = orderMapper.updateSkuStock(list);
        return new CommonResult().success("????????????", count);
    }

    /**
     * ????????????
     */
    public void push(UmsMember umsMember, OmsOrder order, String page, String formId, String name) {
        boolean flag = true;
        SysAppletSet appletSet = appletSetMapper.selectOne(new QueryWrapper<>());
        if (null == appletSet) {
            log.error("???????????????????????????userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
            flag = false;
        }
        log.info("?????????????????????userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
        if (StringUtils.isEmpty(formId)) {
            flag = false;
            log.error("?????????????????????userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
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
                //??????????????????????????????????????????    ********???????????????????????????????????????????????????ID
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
                if (order.getPaymentTime().getTime() > (System.currentTimeMillis() + orderSetting.getConfirmOvertime() * 3600)) {
                    OmsOrderOperateHistory history = updateOrderInfo(order.getId(), order, OrderStatus.TO_COMMENT);
                    history.setNote("Task????????????");
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
                if (order.getPaymentTime().getTime() > (System.currentTimeMillis() + orderSetting.getCommentOvertime() * 3600)) {
                    OmsOrderOperateHistory history = updateOrderInfo(order.getId(), order, OrderStatus.TRADE_SUCCESS);
                    history.setNote("Task????????????");
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
                if (order.getPaymentTime().getTime() > (System.currentTimeMillis() + orderSetting.getFinishOvertime() * 3600)) {
                    OmsOrderOperateHistory history = updateOrderInfo(order.getId(), order, OrderStatus.TO_SHARE);
                    history.setNote("Task??????????????????");
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
            //????????????????????????????????????????????????
            List<OmsOrderDetail> timeOutOrders = orderMapper.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
            if (CollectionUtils.isEmpty(timeOutOrders)) {
                return new CommonResult().failed("??????????????????");
            }
            //?????????????????????????????????
            List<Long> ids = new ArrayList<>();
            for (OmsOrderDetail timeOutOrder : timeOutOrders) {
                ids.add(timeOutOrder.getId());
            }
            orderMapper.updateOrderStatus(ids, OrderStatus.CLOSED.getValue());
            for (OmsOrderDetail timeOutOrder : timeOutOrders) {
                //??????????????????????????????
                // orderMapper.releaseSkuStockLock(timeOutOrder.getOrderItemList());
                releaseStock(timeOutOrder);
                //???????????????????????????
                updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
                //??????????????????
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
        //??????????????????????????????
        OmsOrder cancelOrder = orderMapper.selectById(orderId);
        if (cancelOrder != null) {
            //???????????????????????????
            cancelOrder.setStatus(OrderStatus.CLOSED.getValue());
            orderMapper.updateById(cancelOrder);
            OmsOrderItem queryO = new OmsOrderItem();
            queryO.setOrderId(orderId);
            //  List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
            //??????????????????????????????
            //  orderMapper.releaseSkuStockLock(list);
            releaseStock(cancelOrder);
            //???????????????????????????
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            //??????????????????
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
            history.setNote("????????????");
            orderOperateHistoryService.save(history);
        }
    }

    /**
     * ????????????
     */
    public void push(GroupAndOrderVo umsMember, OmsOrder order, String page, String formId) {
        log.info("?????????????????????userId=" + umsMember.getMemberId() + ",orderId=" + order.getId() + ",formId=" + formId);
        boolean flag = true;
        SysAppletSet appletSet = appletSetMapper.selectOne(new QueryWrapper<>());
        if (null == appletSet) {
            log.error("???????????????????????????userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
            flag = false;
        }
        log.info("?????????????????????userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
        if (StringUtils.isEmpty(formId)) {
            flag = false;
            log.error("?????????????????????userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
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
                //??????????????????????????????????????????    ********???????????????????????????????????????????????????ID
                WX_TemplateMsgUtil.sendWechatMsgToUser(umsMember.getWxid(), templateId, page + "?id=" + order.getId(),
                        formId, jsonObject, accessToken);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * ??????18???????????????:8?????????+2???????????????+2???????????????+6???????????????id
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
     * ???????????????
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * ?????????????????????????????????
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
                        throw new ApiMallPlusException("???????????? goods is stock out. goodsId=" + item.getProductId() + ", goodsId=" + item.getProductSkuId());
                    }
                    newGoods.setStock(goods.getStock() - item.getProductQuantity());
                    pmsGiftsService.updateById(newGoods);
                }

            }

        }
    }

    /**
     * ?????????????????????????????????
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
     * ?????????????????????????????????
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
     * ????????????????????????????????????
     */
    private boolean hasStock(List<OmsCartItem> cartPromotionItemList) {

        return true;
    }

    /**
     * ?????????????????????????????????
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
        if (calcAmount.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            calcAmount.setPayAmount(new BigDecimal("0.01"));
        }
        return calcAmount;
    }

    /**
     * ????????????????????????????????????
     */
    private void deleteCartItemList(List<OmsCartItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        cartItemService.delete(currentMember.getId(), ids);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param couponId  ?????????id
     * @param memberId  ??????id
     * @param useStatus 0->????????????1->?????????
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) {
            return;
        }
        //????????????????????????
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
     * ????????????????????????
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
     * ????????????????????????
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        if (order.getPromotionAmount() == null) {
            order.setPromotionAmount(BigDecimal.ZERO);
        }
        if (order.getFreightAmount() == null) {
            order.setFreightAmount(BigDecimal.ZERO);
        }
        //?????????+??????-????????????-???????????????-????????????
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
        return payAmount;
    }

    /**
     * ???????????????????????????
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
     * ???????????????????????????
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
     * ????????????????????????
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
     * ??????????????????????????????
     *
     * @param useIntegration ?????????????????????
     * @param totalAmount    ???????????????
     * @param currentMember  ???????????????
     * @param hasCoupon      ???????????????????????????
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal(0);
        //????????????????????????????????????
        if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
            return zeroAmount;
        }
        //??????????????????????????????????????????
        //??????????????????????????????
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            //????????????????????????
            return zeroAmount;
        }
        //????????????????????????????????????
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }
        //???????????????????????????????????????
        BigDecimal integrationAmount = new BigDecimal(useIntegration).divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    /**
     * ??????????????????????????????
     *
     * @param orderItemList       order_item??????
     * @param couponHistoryDetail ?????????????????????
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            //????????????
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            //????????????
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            //????????????
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param orderItemList ????????????????????????????????????
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            //(????????????/??????????????????)*???????????????
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param couponHistoryDetail ???????????????
     * @param orderItemList       ????????????
     * @param type                ?????????????????????0->???????????????1->????????????
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
        List<OmsCartItem> list = new ArrayList<>();
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        OmsCartItem cartItem = new OmsCartItem();
        PmsProduct pmsProduct = productService.getById(orderParam.getGoodsId());
        createCartObj(orderParam, list, cartItem, pmsProduct);
        ConfirmOrderResult result = new ConfirmOrderResult();
        //?????????????????????
        List<OmsCartItem> newCartList = new ArrayList<>();
        // ?????????????????????
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
        //??????????????????????????????
        UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
        if (memberService.getNewCurrentMember() == null) {
            return new CommonResult().fail(100);
        }
        queryU.setMemberId(memberService.getNewCurrentMember().getId());
        List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        UmsMemberReceiveAddress address = addressService.getDefaultItem();
        //?????????????????????????????????
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(newCartList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        UmsMember member = memberService.getById(memberService.getNewCurrentMember().getId());
        if (member == null) {
            return new CommonResult().fail(100);
        }

        //??????????????????
        result.setMemberIntegration(member.getIntegration());
        //????????????????????????
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);
        //?????????????????????????????????????????????
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(newCartList, transFee, BigDecimal.ZERO);
        result.setCalcAmount(calcAmount);
        result.setAddress(address);
        return result;
    }

    @Override
    public Object addCart(CartParam cartParam) {
        if (ValidatorUtils.empty(memberService.getNewCurrentMember().getId())) {
            return new CommonResult().fail(100);
        }
        if (ValidatorUtils.empty(cartParam.getTotal())) {
            cartParam.setTotal(1);
        }
        OmsCartItem cartItem = new OmsCartItem();
        PmsProduct pmsProduct = productService.getById(cartParam.getGoodsId());
        if (ValidatorUtils.notEmpty(cartParam.getSkuId()) && cartParam.getSkuId() > 0) {
            PmsSkuStock pmsSkuStock = skuStockMapper.selectById(cartParam.getSkuId());
            checkGoods(pmsProduct, false, cartParam.getTotal());
            checkSkuGoods(pmsSkuStock, cartParam.getTotal());
            cartItem.setProductId(pmsSkuStock.getProductId());
            cartItem.setMemberId(memberService.getNewCurrentMember().getId());
            cartItem.setProductSkuId(pmsSkuStock.getId());
            OmsCartItem existCartItem = cartItemMapper.selectOne(new QueryWrapper<>(cartItem));
            if (existCartItem == null) {
                cartItem.setChecked(1);
                cartItem.setIsFenxiao(pmsProduct.getIsFenxiao());
                cartItem.setMemberId(memberService.getNewCurrentMember().getId());
                cartItem.setPrice(pmsSkuStock.getPrice());
                cartItem.setProductSkuCode(pmsSkuStock.getSkuCode());
                cartItem.setQuantity(cartParam.getTotal());
                cartItem.setProductAttr(pmsSkuStock.getMeno());
                cartItem.setProductPic(pmsSkuStock.getPic());
                cartItem.setSp1(pmsSkuStock.getSp1());
                cartItem.setSp2(pmsSkuStock.getSp2());
                cartItem.setSp3(pmsSkuStock.getSp3());
                cartItem.setProductName(pmsSkuStock.getProductName());
                cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
                cartItem.setProductBrand(pmsProduct.getBrandName());
                cartItem.setCreateDate(new Date());
                //   cartItem.setStoreId(pmsProduct.getStoreId());
                cartItem.setStoreName(pmsProduct.getStoreName());
                cartItemMapper.insert(cartItem);
            } else {
                existCartItem.setPrice(pmsSkuStock.getPrice());
                existCartItem.setModifyDate(new Date());
                existCartItem.setQuantity(existCartItem.getQuantity() + cartParam.getTotal());
                cartItemMapper.updateById(existCartItem);
                return new CommonResult().success(existCartItem);
            }
        } else {
            checkGoods(pmsProduct, true, cartParam.getTotal());
            cartItem.setProductId(cartParam.getGoodsId());
            cartItem.setMemberId(memberService.getNewCurrentMember().getId());
            OmsCartItem existCartItem = cartItemMapper.selectOne(new QueryWrapper<>(cartItem));
            if (existCartItem == null) {
                cartItem.setChecked(1);
                cartItem.setPrice(pmsProduct.getPrice());
                cartItem.setProductName(pmsProduct.getName());
                cartItem.setQuantity(cartParam.getTotal());
                cartItem.setProductPic(pmsProduct.getPic());
                cartItem.setCreateDate(new Date());
                cartItem.setMemberId(memberService.getNewCurrentMember().getId());
                cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
                cartItem.setProductBrand(pmsProduct.getBrandName());
                //  cartItem.setStoreId(pmsProduct.getStoreId());
                cartItem.setStoreName(pmsProduct.getStoreName());
                cartItemMapper.insert(cartItem);
            } else {
                existCartItem.setPrice(pmsProduct.getPrice());
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
            throw new ApiMallPlusException("???????????????");
        }
        if (falg && (goods.getStock() <= 0 || goods.getStock() < count)) {
            throw new ApiMallPlusException("????????????!");
        }
    }

    private void checkSkuGoods(PmsSkuStock goods, int count) {
        if (goods == null || goods.getId() == null) {
            throw new ApiMallPlusException("???????????????");
        }
        if (goods.getStock() <= 0 || goods.getStock() < count) {
            throw new ApiMallPlusException("????????????!");
        }
    }

    @Override
    public CommonResult generateStoreOrder(OrderParam orderParam) {
        if (ValidatorUtils.empty(orderParam.getTotal())) {
            orderParam.setTotal(1);
        }
        String type = orderParam.getType();
        UmsMember currentMember = memberService.getNewCurrentMember();

        // 1. ?????????????????????
        List<OmsCartItem> cartPromotionItemList = new ArrayList<>();

        if (ValidatorUtils.empty(orderParam.getAddressId())) {
            return new CommonResult().failed("address is null");
        }
        //????????????
        if (orderParam.getOrderType() == 3) {
            SmsGroupActivity smsGroupActivity = smsGroupActivityService.getById(orderParam.getGroupActivityId());
            if (ValidatorUtils.notEmpty(smsGroupActivity.getGoodsIds())) {
                List<PmsProduct> productList = (List<PmsProduct>) productService.listByIds(
                        Arrays.asList(smsGroupActivity.getGoodsIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
                if (productList != null && productList.size() > 0) {
                    //  order.setFreightAmount(smsGroupActivity.getTransfee());
                    //?????????????????????
                    cartPromotionItemList = goodsToCartList(productList);
                }
            }
        } else {
            if ("3".equals(type)) { // 1 ???????????? 2 ??????????????? 3????????????????????????
                cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
            }
            if ("1".equals(type)) {
                Long cartId = Long.valueOf(orderParam.getCartId());
                OmsCartItem omsCartItem = cartItemService.selectById(cartId);
                List<OmsCartItem> list = new ArrayList<>();
                if (omsCartItem != null) {
                    list.add(omsCartItem);
                } else {
                    throw new ApiMallPlusException("???????????????");
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
                    throw new ApiMallPlusException("????????????????????????");
                }

                BookOrder bookOrder1 = new BookOrder(orderParam, currentMember, cartPromotionItemList, null, false).invoke();
                if (bookOrder1.is())
                    return new CommonResult().failed("?????????????????????");


                OmsOrder pidOrder = bookOrder1.order;
                List<OmsOrderItem> orderItemList = bookOrder1.orderItemList;
                Long pid = pidOrder.getId();
                String name = bookOrder1.getName();
                Map<String, Object> result = new HashMap<>();


                if (ValidatorUtils.empty(orderParam.getFormId())) {
                    push(currentMember, pidOrder, orderParam.getPage(), orderParam.getFormId(), name);
                }
                Map<Integer, List<OmsCartItem>> map = null;
                //cartPromotionItemList.stream().collect(Collectors.groupingBy(OmsCartItem::getStoreId));
                if (map.size() > 1) {
                    for (Map.Entry<Integer, List<OmsCartItem>> entry : map.entrySet()) {
                        BookOrder bookOrder = new BookOrder(orderParam, currentMember, entry.getValue(), pid, true).invoke();
                        if (bookOrder.is())
                            return new CommonResult().failed("?????????????????????");
                    }
                }
                result.put("order", pidOrder);
                result.put("orderItemList", orderItemList);
                return new CommonResult().success("????????????", result);
            } else if ("6".equals(type)) { // ??????
                SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getById(orderParam.getSkillId());
                PmsProduct product = productService.getById(relation.getProductId());
                OmsCartItem omsCartItem = new OmsCartItem();
                omsCartItem.setQuantity(orderParam.getTotal());
                if (orderParam.getTotal() > relation.getFlashPromotionLimit()) {
                    throw new ApiMallPlusException("?????????????????????");
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

        BookOrder bookOrder = new BookOrder(orderParam, currentMember, cartPromotionItemList, null, false).invoke();
        if (bookOrder.is())
            return new CommonResult().failed("?????????????????????");
        List<OmsOrderItem> orderItemList = bookOrder.getOrderItemList();
        OmsOrder order = bookOrder.order;
        String name = bookOrder.getName();
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);

        if (ValidatorUtils.empty(orderParam.getFormId())) {
            push(currentMember, order, orderParam.getPage(), orderParam.getFormId(), name);
        }

        return new CommonResult().success("????????????", result);
    }

    @Transactional
    @Override
    public Object applyRe(ApplyRefundVo itemss) {

        try {
            if (ValidatorUtils.empty(itemss.getItems()) ||itemss.getItems().split(",").length<1){
                return new CommonResult().failed("????????????");
            }
            for (String id : itemss.getItems().split(",")){
                OmsOrderItem item = orderItemService.getById(id);
                OmsOrderReturnApply apply = new OmsOrderReturnApply();
                UmsMember member = memberService.getNewCurrentMember();
                apply.setStatus(AllEnum.OmsOrderReturnApplyStatus.INIT.code());
                apply.setCreateTime(new Date());
                apply.setReturnAmount(itemss.getReturnAmount());
                apply.setDescription(itemss.getDesc());
                apply.setOrderId(item.getOrderId());
                apply.setMemberUsername(member.getUsername());
                apply.setProductAttr(item.getProductAttr());
                apply.setProductCount(item.getProductQuantity());
                apply.setProductId(item.getProductId());
                apply.setProductName(item.getProductName());
                apply.setProductPic(item.getProductPic());
                apply.setProductPrice(item.getProductPrice());
                apply.setProductRealPrice(item.getRealAmount());
                apply.setProofPics(org.apache.commons.lang3.StringUtils.join(itemss.getImages(), ","));
                apply.setReason(itemss.getDesc());
                apply.setType(itemss.getType());
                apply.setReturnPhone(member.getPhone());
                apply.setReturnName(member.getNickname());
                orderReturnApplyMapper.insert(apply);

                OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                history.setOrderId(item.getOrderId());
                history.setCreateTime(new Date());
                history.setOperateMan("shop");
                //  //0?????? 1?????? 2??????3 ????????????
             if (apply.getType()==0){
                 history.setNote("??????");
             }else  if (apply.getType()==1){
                 history.setNote("????????????");
             }else  if (apply.getType()==2){
                 history.setNote("????????????");
             }else  if (apply.getType()==3){
                 history.setNote("??????????????????");
             }

            orderOperateHistoryService.save(history);
            }

        } catch (Exception e) {
            return new CommonResult().failed("????????????????????????");
        }

        return new CommonResult().success();
    }

    private class BookOrder {
        private Long pid;
        // ?????????????????????????????????????????????????????????
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
            //??????????????????????????????
            name = "";
            BigDecimal transFee = BigDecimal.ZERO;
            List<OmsCartItem> newCartItemList = new ArrayList<>();
            Integer isFirst = 1;
            Integer storeId = 0;
            String storeName = null;
            // 2. ?????????????????????????????????????????????????????? ????????????
            for (OmsCartItem cartPromotionItem : cartPromotionItemList) {
                boolean flag = false;
                PmsProduct goods = productService.getById(cartPromotionItem.getProductId());
                if (!ValidatorUtils.empty(cartPromotionItem.getProductSkuId()) && cartPromotionItem.getProductSkuId() > 0) {
                    if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() > cartPromotionItem.getQuantity()) {
                        PmsSkuStock skuStock = skuStockMapper.selectById(cartPromotionItem.getProductSkuId());
                        if (skuStock.getStock() > 0 && skuStock.getStock() >= cartPromotionItem.getQuantity()) {
                            flag = true;
                        }
                    }
                } else {
                    if (goods != null && goods.getId() != null && goods.getStock() > 0 && goods.getStock() >= cartPromotionItem.getQuantity()) {
                        flag = true;
                    }
                }
                if (flag) {
                    if (goods.getTransfee().compareTo(transFee) > 0) {
                        transFee = goods.getTransfee();
                    }
                    //????????????????????????
                    OmsOrderItem orderItem = createOrderItem(cartPromotionItem);

                    orderItem.setType(AllEnum.OrderItemType.GOODS.code());
                    orderItemList.add(orderItem);
                    if (isFirst == 1) {
                        // storeId = cartPromotionItem.getStoreId();
                        storeName = cartPromotionItem.getStoreName();
                        name = cartPromotionItem.getProductName();
                        order.setGoodsId(cartPromotionItem.getProductId());
                        order.setGoodsName(cartPromotionItem.getProductName());
                    }

                    newCartItemList.add(cartPromotionItem);
                }
            }
            if (newCartItemList == null || newCartItemList.size() < 1) {
                myResult = true;
                return this;
            }

            //3.???????????????
            SmsCouponHistory couponHistory = null;
            SmsCoupon coupon = null;
            if (orderParam.getCouponId() != null) {
                couponHistory = couponHistoryService.getById(orderParam.getMemberCouponId());
                coupon = couponService.getById(orderParam.getCouponId());
            }
            UmsMemberReceiveAddress address = addressService.getById(orderParam.getAddressId());
            //?????????????????????????????????????????????????????????????????????????????????

            createOrderObj(order, orderParam, currentMember, orderItemList, address);
            if (orderParam.getOrderType() != 3) {
                order.setFreightAmount(transFee);
            }
            if (orderParam.getCouponId() == null || orderParam.getCouponId() == 0) {
                order.setCouponAmount(new BigDecimal(0));
            } else {
                order.setCouponId(orderParam.getCouponId());
                order.setCouponAmount(coupon.getAmount());
            }
            //????????????????????????
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
            order.setPayAmount(calcPayAmount(order));
            if (order.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
                order.setPayAmount(new BigDecimal("0.01"));
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
            }

            // TODO: 2018/9/3 bill_*,delivery_*
            if (pid != null) {
                order.setPid(pid);
            }
            if (flagStore) {
                // order.setStoreId(storeId);
                order.setStoreName(storeName);
            }
            //??????order??????order_item???
            orderService.save(order);
            for (OmsOrderItem orderItem : orderItemList) {
                if (flagStore) {
                    //      orderItem.setStoreId(storeId);
                }
                orderItem.setOrderId(order.getId());
                orderItem.setOrderSn(order.getOrderSn());
                if (ValidatorUtils.notEmpty(orderParam.getSkillId())) {
                    orderItem.setGiftIntegration(orderParam.getSkillId().intValue());
                }
            }
            orderItemService.saveBatch(orderItemList);

            // ????????????
            for (OmsCartItem cartPromotionItem : newCartItemList) {
                if (ValidatorUtils.notEmpty(cartPromotionItem.getIsFenxiao()) && cartPromotionItem.getIsFenxiao() == 1) {
                    FenxiaoConfig fenxiaoConfig = fenxiaoConfigMapper.selectOne(new QueryWrapper<>());
                    if (fenxiaoConfig != null && fenxiaoConfig.getStatus() == 1 && ValidatorUtils.notEmpty(currentMember.getInvitecode()) && fenxiaoConfig.getOnePercent() > 0) {
                        //  UmsMember member = memberService.getById(currentMember.getInvitecode());
                        //?????? ??????
                        FenxiaoRecords records1 = new FenxiaoRecords();
                        records1.setCreateTime(new Date());
                        records1.setStatus("1");
                        records1.setGoodsId(cartPromotionItem.getProductId());
                     //   records1.setStoreId(cartPromotionItem.getStoreId());
                        records1.setLevel("1");
                        records1.setType(fenxiaoConfig.getType());
                        records1.setMemberId(Long.valueOf(currentMember.getInvitecode()));
                        records1.setInviteId(currentMember.getId());
                        records1.setOrderId(order.getId());
                        records1.setMoney(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity()).multiply(new BigDecimal(fenxiaoConfig.getOnePercent()))).divide(BigDecimal.valueOf(100)));
                        fenxiaoRecordsMapper.insert(records1);
                        //?????? ??????
                        UmsMember member = memberService.getById(currentMember.getInvitecode());
                        if (member != null && ValidatorUtils.notEmpty(currentMember.getInvitecode()) && fenxiaoConfig.getTwoPercent() > 0) {
                            FenxiaoRecords records = new FenxiaoRecords();
                            records.setCreateTime(new Date());
                            records.setStatus("1");
                            records.setGoodsId(cartPromotionItem.getProductId());
                          //  records.setStoreId(cartPromotionItem.getStoreId());
                            records.setLevel("2");
                            records.setType(fenxiaoConfig.getType());
                            records.setMemberId(Long.valueOf(member.getInvitecode()));
                            records.setInviteId(member.getId());
                            records.setOrderId(order.getId());
                            records.setMoney(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity()).multiply(new BigDecimal(fenxiaoConfig.getTwoPercent()))).divide(BigDecimal.valueOf(100)));
                            fenxiaoRecordsMapper.insert(records);
                        }
                    }
                }

            }
            //?????????????????????????????????????????????
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
            history.setNote("????????????");
            orderOperateHistoryService.save(history);

            //?????????????????????????????????
            if (order.getUseIntegration() != null) {
                memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - order.getUseIntegration());
            }
            if (pid == null) {
                memberService.addIntegration(currentMember.getId(), order.getPayAmount().multiply(new BigDecimal("0.1")).intValue(), 1, "????????????????????????", AllEnum.ChangeSource.order.code(), currentMember.getUsername());

                lockStockByOrder(orderItemList, orderParam.getType());
                //?????????????????????????????????
                deleteCartItemList(cartPromotionItemList, currentMember);
            }
            myResult = false;
            return this;
        }
    }
}
