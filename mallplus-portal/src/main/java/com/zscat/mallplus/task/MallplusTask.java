package com.zscat.mallplus.task;


import com.alipay.api.domain.AlipayTradeRefundModel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.enums.OrderStatus;
import com.zscat.mallplus.fenxiao.entity.FenxiaoRecords;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoRecordsMapper;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.service.IOmsOrderReturnApplyService;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.pay.controller.alipay.AliPayController;
import com.zscat.mallplus.pay.controller.wxpay.WxPayController;
import com.zscat.mallplus.pms.entity.CmsSubjectProductRelation;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.sms.entity.SmsGroupMember;
import com.zscat.mallplus.sms.entity.SmsGroupRecord;
import com.zscat.mallplus.sms.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.sms.mapper.SmsGroupRecordMapper;
import com.zscat.mallplus.sms.service.ISmsHomeBrandService;
import com.zscat.mallplus.sms.service.ISmsHomeNewProductService;
import com.zscat.mallplus.sms.service.ISmsHomeRecommendProductService;
import com.zscat.mallplus.sms.service.ISmsHomeRecommendSubjectService;

import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.ums.service.impl.RedisUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.DateUtils;
import com.zscat.mallplus.vo.Rediskey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://github.com/shenzhuan/mallplus on 2018/8/24.
 * 订单超时取消并解锁库存的定时器
 */
@Slf4j
@Component
public class MallplusTask {
    private Logger logger = LoggerFactory.getLogger(MallplusTask.class);
    @Autowired
    private IOmsOrderService portalOrderService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private ICmsSubjectService subjectService;
    @Resource
    private OmsOrderMapper orderMapper;
    @Resource
    private IUmsMemberService IUmsMemberService;
    @Resource
    private FenxiaoRecordsMapper fenxiaoRecordsMapper;
    @Resource
    private SysStoreMapper storeMapper;

    @Resource
    private ISmsHomeNewProductService homeNewProductService;
    @Resource
    private ISmsHomeRecommendProductService homeRecommendProductService;
    @Resource
    private ISmsHomeBrandService homeBrandService;
    @Resource
    private ISmsHomeRecommendSubjectService homeRecommendSubjectService;
    @Resource
    private IOmsOrderReturnApplyService orderReturnApplyService;
    @Resource
    private SmsGroupRecordMapper groupRecordMapper;
    @Resource
    private SmsGroupMemberMapper groupMemberMapper;
    /**
     * 拼团成功后 将订单状态改为发货
     * 查询所有拼团成功且未处理的拼团记录
     * 更新订单状态为发货 拼团记录为已处理
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void pinTuanOrder() {
      List<SmsGroupRecord> list = groupRecordMapper.selectList(new QueryWrapper<SmsGroupRecord>().eq("status",2).eq("handle_status",1));
       for (SmsGroupRecord groupRecord:list){
           List<SmsGroupMember> groupMembers = groupMemberMapper.selectList(new QueryWrapper<SmsGroupMember>().eq("group_record_id", groupRecord.getId()).eq("status", 2));
          if (groupMembers!=null && groupMembers.size()>0){
              List<Long> ids = groupMembers.stream()
                      .map(SmsGroupMember::getOrderId)
                      .collect(Collectors.toList());
              if (ids.size()>0){
                  OmsOrder order = new OmsOrder();
                  order.setStatus(OrderStatus.TO_DELIVER.getValue());
                  orderMapper.update(order,new QueryWrapper<OmsOrder>().in("status",ids));
                  groupRecord.setHandleStatus(2);
                  groupRecordMapper.updateById(groupRecord);
              }
          }
       }
    }
    /**
     * 退款
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void refund() {
        List<Integer> ids = Arrays.asList(1,3);
      List<OmsOrderReturnApply> returnApplyList=  orderReturnApplyService.list(new QueryWrapper<OmsOrderReturnApply>().
              eq("status", AllEnum.OmsOrderReturnApplyStatus.REFUNDING.code()).eq("refund_status",1).in("type",ids));
        for (OmsOrderReturnApply orderReturnApply:returnApplyList){
                portalOrderService.refund(orderReturnApply);
        }
    }
    /**
     * 清理 因为商品被删除 导致的数据关联问题
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void clearGoodsRelate() {
        //   CommonResult result = portalOrderService.cancelTimeOutOrder();
    }

    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     * 每10分钟扫描一次，扫描设定超时时间之前下的订单，如果没支付则取消该订单
     * 正常订单超时时间(分)
     */
    @Scheduled(cron = "0 0/10 * ? * ?")
    private void cancelTimeOutOrder() {
        CommonResult result = portalOrderService.cancelTimeOutOrder();
        logger.info("取消订单，并根据sku编号释放锁定库存:{}", result);
    }

    /**
     * 自动收货
     * 发货后自动确认收货时间（天）
     */
    @Scheduled(cron = "0 0/15 * ? * ?")
    private void autoDeliveryOrder() {
        CommonResult result = portalOrderService.autoDeliveryOrder();
        logger.info("取消订单，并根据sku编号释放锁定库存:{}", result);
    }

    /**
     * 自动完成交易时间，不能申请售后（天）
     */
    @Scheduled(cron = "0 0/18 * ? * ?")
    private void autoSucessOrder() {
        CommonResult result = portalOrderService.autoSucessOrder();
        logger.info("取消订单，并根据sku编号释放锁定库存:{}", result);
    }

    /**
     * 订单完成后自动好评时间（天）
     */
    @Scheduled(cron = "0 0/13 * ? * ?")
    private void autoCommentOrder() {
        CommonResult result = portalOrderService.autoCommentOrder();
        logger.info("取消订单，并根据sku编号释放锁定库存:{}", result);
    }

    /**
     * 会员等级计算
     */
    @Scheduled(cron = "0 0/55 * ? * ?")
    private void memberlevelCalator() {
        //   IUmsMemberService.updataMemberOrderInfo();
        logger.info("会员等级计算");
    }

    /**
     * 佣金计算 计算前一天的订单 凌晨1点
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void memberlevelCalator1() {
        logger.info("佣金计算 计算前一天的订单 start....");
        Long t1 = System.currentTimeMillis();
        String yesteday = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(new Date(), -1));
        List<OmsOrder> orders = orderMapper.listByDate(yesteday, 1);
        // 获取订单为 待评价和已完成的
        List<Long> ids = new ArrayList<>();
        for (OmsOrder order : orders) {
            if (order.getStatus() == 4 || order.getStatus() == 5) {
                ids.add(order.getId());
            }
        }
        // 将条件合适的订单的 佣金记录更改状态
        if (ids != null && ids.size() > 0) {
            FenxiaoRecords records = new FenxiaoRecords();
            records.setStatus("2");
            fenxiaoRecordsMapper.update(records, new QueryWrapper<FenxiaoRecords>().in("order_id", ids));
        }
        // 分组佣金记录 更改用户的余额或积分
        List<FenxiaoRecords> fenxiaoRecordss = fenxiaoRecordsMapper.listRecordsGroupByMemberId();
        for (FenxiaoRecords fenxiaoRecords : fenxiaoRecordss) {
            UmsMember member = IUmsMemberService.getById(fenxiaoRecords.getMemberId());
            if (member != null) {
                if (fenxiaoRecords.getType() == 1) { // 余额
                    member.setBlance(member.getBlance().add(fenxiaoRecords.getMoney()));
                } else {
                    member.setIntegration(member.getIntegration() + fenxiaoRecords.getMoney().intValue());
                }
                IUmsMemberService.updateById(member);
            }

        }
        logger.info("佣金计算 计算前一天的订单end..,耗时" + (System.currentTimeMillis() - t1) / 1000 + "秒");
    }

    /**
     * 文章浏览量
     */
    @Scheduled(cron = "0 0/50 * * * ? ")//每1分钟
    public void SyncNodesAndShips() {
        logger.info("开始保存点赞数 、浏览数SyncNodesAndShips");
        try {
            //先获取这段时间的浏览数
            Map<Object, Object> viewCountItem = redisUtil.hGetAll(Rediskey.ARTICLE_VIEWCOUNT_KEY);
            //然后删除redis里这段时间的浏览数
            redisUtil.delete(Rediskey.ARTICLE_VIEWCOUNT_KEY);
            if (!viewCountItem.isEmpty()) {
                for (Object item : viewCountItem.keySet()) {
                    String articleKey = item.toString();//viewcount_1
                    String[] kv = articleKey.split("_");
                    Long articleId = Long.parseLong(kv[1]);
                    Integer viewCount = Integer.parseInt(viewCountItem.get(articleKey).toString());
                    CmsSubject subject = subjectService.getById(articleId);
                    if (subject != null) {
                        CmsSubject cms = new CmsSubject();
                        cms.setId(articleId);
                        cms.setReadCount(subject.getReadCount() + viewCount);
                        logger.info("SyncNodesAndShips" + articleId + "," + viewCount);
                        //更新到数据库
                        subjectService.updateById(cms);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("结束保存点赞数 、浏览数");
    }

    /**
     * 商品浏览量
     */
    @Scheduled(cron = "0 0/50 * * * ? ")//每1分钟
    public void SyncGoodsView() {
        logger.info("开始保存点赞数 、浏览数SyncGoodsView");
        try {
            //先获取这段时间的浏览数
            Map<Object, Object> viewCountItem = redisUtil.hGetAll(Rediskey.GOODS_VIEWCOUNT_KEY);
            //然后删除redis里这段时间的浏览数
            redisUtil.delete(Rediskey.GOODS_VIEWCOUNT_KEY);
            if (!viewCountItem.isEmpty()) {
                for (Object item : viewCountItem.keySet()) {
                    String articleKey = item.toString();//viewcount_1
                    String[] kv = articleKey.split("_");
                    Long articleId = Long.parseLong(kv[1]);
                    Integer viewCount = Integer.parseInt(viewCountItem.get(articleKey).toString());
                    PmsProduct subject = productMapper.selectById(articleId);
                    if (subject != null) {
                        PmsProduct p = new PmsProduct();
                        p.setId(articleId);
                        p.setHit(subject.getHit() + viewCount);
                        logger.info("SyncGoodsView" + articleId + "," + viewCount);
                        //更新到数据库
                        productMapper.updateById(p);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.info("结束保存点赞数 、浏览数");
    }


}
