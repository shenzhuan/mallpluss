package com.zscat.mallplus.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.cms.service.ICmsSubjectService;
import com.zscat.mallplus.fenxiao.entity.FenxiaoRecords;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoRecordsMapper;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.sys.entity.AdminDayStatics;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.entity.SysStoreDepositLog;
import com.zscat.mallplus.sys.mapper.AdminDayStaticsMapper;
import com.zscat.mallplus.sys.mapper.SysStoreDepositLogMapper;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    private AdminDayStaticsMapper dayStaticsMapper;
    @Resource
    private SysStoreDepositLogMapper sysStoreDepositLogMapper;

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
    /**
     * 商户数据日统计 凌晨1点  0 0 1 * * ?
     */
    @Scheduled(cron = "0 1 1 * * ?")
    //@Scheduled(cron = "0 0/5 * ? * ?")
    public void storeDayStatics() throws InterruptedException {

        List<SysStore> storeList = storeMapper.selectList(new QueryWrapper<SysStore>().eq("status",3));
        log.info("商户数据日统计：{}，共{}个商户需要需要同步", DateUtils.getNowDate(), storeList.size());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        String endTime =DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, calendar.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        String startTime =DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, calendar.getTime());

        //  dayStaticsService.deleteAdminDayStaticsByDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, calendar.getTime()));

        System.out.println(startTime+","+endTime);
        Thread.sleep(3*1000);
        for (SysStore store : storeList) {
         //   List<UmsMember> memberList = IUmsMemberService.list(new QueryWrapper<UmsMember>().eq("store_id",store.getId()));
            List<PmsProduct> productList = productMapper.selectList(new QueryWrapper<PmsProduct>().eq("store_id",store.getId()).between("create_time",startTime,endTime));
            List<OmsOrder> orderList = orderMapper.selectList(new QueryWrapper<OmsOrder>().eq("store_id",store.getId()).between("create_time",startTime,endTime));

            AdminDayStatics dayStatics = new AdminDayStatics();
            dayStatics.setGoodsCount(productList.size());
          //  dayStatics.setMemberCount(memberList.size());

            Integer payOrderCount = 0;
            BigDecimal payAmount = BigDecimal.ZERO;
            Integer notPayOrderCount = 0;
            BigDecimal notPayAmount = BigDecimal.ZERO;
            for (OmsOrder omsOrder : orderList) {
                if (omsOrder.getPayAmount() != null) {
                    if (omsOrder.getStatus() < 9) {
                        payOrderCount++;
                        payAmount = payAmount.add(omsOrder.getPayAmount());
                    } else {
                        notPayOrderCount++;
                        notPayAmount = notPayAmount.add(omsOrder.getPayAmount());
                    }
                }
            }
            dayStatics.setPayAmount(payAmount);
            dayStatics.setPayOrderCount(payOrderCount);
            dayStatics.setNotPayAmount(notPayAmount);
            dayStatics.setNotPayOrderCount(notPayOrderCount);
            dayStatics.setStoreId(store.getId());
            dayStatics.setStoreName(store.getName());
            dayStatics.setCreateTime(new Date());

            if (dayStatics.getPayOrderCount()>0 ||dayStatics.getNotPayOrderCount()>0 ||
                    dayStatics.getGoodsCount()>0  ){
                dayStaticsMapper.insert(dayStatics);
            }
            log.info("商户数据日统计end====：{}，商户ID={},商户名称={}", DateUtils.getNowDate(), store.getId(), store.getName());
        }
        log.info("商户数据日统计end====：{}", DateUtils.getNowDate());
    }

    /**
     * 商户统计 凌晨2点  0 0 2 * * ?
     */
     @Scheduled(cron = "0 32 9 * * ?")
  //  @Scheduled(cron = "0 0/10 * ? * ?")
    public void storeStatics() throws InterruptedException {
        List<AdminDayStatics> dayStatics = dayStaticsMapper.selectAdminDayStaticsGroupBySId();
        log.info("商户统计：{}，共{}个商户需要需要同步", DateUtils.getNowDate(), dayStatics.size());
        for (AdminDayStatics dayStatics1 : dayStatics){
            SysStore store = new SysStore();
            store.setMemberCount(dayStatics1.getMemberCount());
            store.setId(dayStatics1.getStoreId());
            store.setGoodsCount(dayStatics1.getGoodsCount());
            store.setArticleCount(dayStatics1.getArticleCount());
            store.setOrderCount(dayStatics1.getPayOrderCount());
            store.setPayAmount(dayStatics1.getPayAmount());
            if (dayStatics1.getPayOrderCount()>0){
                SysStore sysStore =storeMapper.selectById(dayStatics1.getStoreId());
                if (sysStore!=null){
                    SysStoreDepositLog entity = new SysStoreDepositLog();
                    if (sysStore.getAmount()==null || sysStore.getAmount().compareTo(BigDecimal.ZERO)==0){
                        entity.setBalance(dayStatics1.getPayAmount());
                        store.setAmount(dayStatics1.getPayAmount());
                    }else {
                        entity.setBalance(sysStore.getAmount().add(dayStatics1.getPayAmount()));
                        store.setAmount(sysStore.getAmount().add(dayStatics1.getPayAmount()));
                    }
                    entity.setCredit(dayStatics1.getPayAmount());
                    entity.setStoreId(dayStatics1.getStoreId());
                    entity.setCreateTime(new Date());
                    entity.setUpdateTime(new Date());
                    entity.setType(6);
                    sysStoreDepositLogMapper.insert(entity);
                }

            }
            storeMapper.updateById(store);
        }
        log.info("商户统计end====：{}", DateUtils.getNowDate());
    }

}
