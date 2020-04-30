package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.ExportGoods;
import com.zscat.mallplus.ExportSubject;
import com.zscat.mallplus.ExportUser;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.bo.HomeOrderData;
import com.zscat.mallplus.cms.entity.CmsSubject;
import com.zscat.mallplus.cms.mapper.CmsSubjectCategoryMapper;
import com.zscat.mallplus.cms.mapper.CmsSubjectMapper;
import com.zscat.mallplus.enums.OrderStatus;
import com.zscat.mallplus.oms.entity.OmsOrder;
import com.zscat.mallplus.oms.service.IOmsOrderService;
import com.zscat.mallplus.oms.vo.OrderStstic;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.mapper.PmsFavoriteMapper;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.sys.entity.SysArea;
import com.zscat.mallplus.sys.entity.SysSchool;
import com.zscat.mallplus.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.sys.mapper.SysSchoolMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.util.JwtTokenUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.OrderStatusCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther: shenzhuan
 * @Date: 2019/3/27 18:57
 * @Description:
 */
@Slf4j
@RestController
@Api(tags = "HomeController", description = "首页管理")
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Resource
    CmsSubjectCategoryMapper categoryMapper;
    @Resource
    CmsSubjectMapper subjectMapper;
    @Resource
    SysSchoolMapper schoolMapper;
    @Resource
    SysAreaMapper sysAreaMapper;
    @Resource
    private IOmsOrderService orderService;
    @Resource
    private IPmsProductService productService;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private PmsFavoriteMapper favoriteMapper;

    @ApiOperation("商品统计")
    @SysLog(MODULE = "home", REMARK = "商品销量或浏览统计")
    @RequestMapping(value = "/goodsSort", method = RequestMethod.GET)
    public Object goodsSort(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                            @RequestParam String sortCol,
                            @RequestParam String cols) throws Exception {
        return new CommonResult().success(productService.page(new Page<PmsProduct>(0, pageSize), new QueryWrapper<PmsProduct>(new PmsProduct())
                .orderByDesc(sortCol).select(cols)));
    }

    @ApiOperation("商品收藏统计")
    @SysLog(MODULE = "home", REMARK = "商品收藏统计")
    @RequestMapping(value = "/goodsCollect", method = RequestMethod.GET)
    public Object goodsCollect(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               @RequestParam(value = "date", required = false) String date) throws Exception {
        return new CommonResult().success(favoriteMapper.selectCollectStatics(date, pageSize));
    }

    @ApiOperation("订单状态统计统计")
    @SysLog(MODULE = "home", REMARK = "订单状态统计统计")
    @RequestMapping(value = "/orderStatusStatics", method = RequestMethod.GET)
    public Object orderStatusStatics() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("待付款", orderService.orderMonthStatic(DateUtils.currentDay(), OrderStatus.INIT.getValue()));
        map.put("待发货", orderService.orderMonthStatic(DateUtils.currentDay(), OrderStatus.TO_DELIVER.getValue()));
        map.put("待收货", orderService.orderMonthStatic(DateUtils.currentDay(), OrderStatus.DELIVERED.getValue()));
        map.put(" 已完成", orderService.orderMonthStatic(DateUtils.currentDay(), OrderStatus.TRADE_SUCCESS.getValue()));
        map.put("已退款", orderService.orderMonthStatic(DateUtils.currentDay(), OrderStatus.REFUND.getValue()));
        return new CommonResult().success(map);
    }

    @ApiOperation("会员注册统计")
    @SysLog(MODULE = "home", REMARK = "会员注册统计")
    @RequestMapping(value = "/memberMonthStatic", method = RequestMethod.GET)
    public Object memberMonthStaticmemberMonthStatic() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("INIT", memberService.memberMonthStatic(DateUtils.currentDay()));

        return new CommonResult().success(map);
    }

    @ApiOperation("按时间统计订单 会员和商品")
    @SysLog(MODULE = "home", REMARK = "首页订单日统计")
    @RequestMapping(value = "/dayStatic", method = RequestMethod.GET)
    public Object dayStatic(@RequestParam String date, @RequestParam Integer type) throws Exception {
        return new CommonResult().success(orderService.dayStatic(date, type));
    }

    @ApiOperation("首页订单日统计")
    @SysLog(MODULE = "home", REMARK = "首页订单日统计")
    @RequestMapping(value = "/orderDayStatic", method = RequestMethod.GET)
    public Object orderDayStatic(@RequestParam String date) throws Exception {
        if (date.startsWith("--")) {
            date = DateUtils.currentDay();
        }
        return new CommonResult().success(orderService.orderDayStatic(date));
    }

    @ApiOperation("首页订单月统计")
    @SysLog(MODULE = "home", REMARK = "首页订单月统计")
    @RequestMapping(value = "/orderMonthStatic", method = RequestMethod.GET)
    public Object orderMonthStatic(@RequestParam String date) throws Exception {
        if (date.startsWith("--")) {
            date = DateUtils.currentDay();
        }
        return new CommonResult().success(orderService.orderMonthStatic(date, null));
    }

    /**
     * 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     *
     * @return
     */
    @ApiOperation("首页订单统计")
    @SysLog(MODULE = "home", REMARK = "首页订单统计")
    @RequestMapping(value = "/orderStatic", method = RequestMethod.GET)
    public Object orderStatic() throws Exception {

        HomeOrderData data = new HomeOrderData();
        List<OmsOrder> orderList = orderService.list(new QueryWrapper<>());
        int nowOrderCount = 0; // 今日订单 一支付
        BigDecimal nowOrderPay = new BigDecimal(0); //今日销售总额

        int nowTotalOrderCount = 0; // 今日订单
        BigDecimal nowToatlOrderPay = new BigDecimal(0); //今日销售总额

        int yesOrderCount = 0; // 昨日订单
        BigDecimal yesOrderPay = new BigDecimal(0); //日销售总额

        int yesToatlOrderCount = 0; // 昨日订单
        BigDecimal yesTotalOrderPay = new BigDecimal(0); //日销售总额

        int qiOrderCount = 0; // 7日订单
        BigDecimal qiOrderPay = new BigDecimal(0); //7日销售总额

        int monthOrderCount = 0; // 本月订单
        BigDecimal monthOrderPay = new BigDecimal(0); //本月销售总额

        int weekOrderCount = 0; // 本周订单
        BigDecimal weekOrderPay = new BigDecimal(0); //本周销售总额

        int lastWeekOrderCount = 0; // 上周订单
        BigDecimal lastWeekOrderPay = new BigDecimal(0); //上周销售总额

        int lastMonthOrderCount = 0; // 上月订单
        BigDecimal lastMonthOrderPay = new BigDecimal(0); //上月销售总额

        int yesOrderCount1 = 0;
        BigDecimal yesOrderPay1 = new BigDecimal(0);

        int yesOrderCount2 = 0;
        BigDecimal yesOrderPay2 = new BigDecimal(0);

        int yesOrderCount3 = 0;
        BigDecimal yesOrderPay3 = new BigDecimal(0);

        int yesOrderCount4 = 0;
        BigDecimal yesOrderPay4 = new BigDecimal(0);
        int yesOrderCount5 = 0;
        BigDecimal yesOrderPay5 = new BigDecimal(0);
        int status0 = 0;
        int status1 = 0;
        int status2 = 0;
        int status3 = 0;
        int status4 = 0;
        int status5 = 0;
        int status6 = 0;
        OrderStatusCount count = new OrderStatusCount();
        List<OrderStstic> orderStsticList = new ArrayList<>(); // 7日销售
        for (OmsOrder order : orderList) {
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.format(new Date()))) {
                if (order.getStatus() < 9) {
                    nowOrderCount++;
                    nowOrderPay = nowOrderPay.add(order.getPayAmount());
                }
                nowTotalOrderCount++;
                nowToatlOrderPay = nowToatlOrderPay.add(order.getPayAmount());
            }
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -1))) {
                if (order.getStatus() < 9) {
                    yesOrderCount++;
                    yesOrderPay = yesOrderPay.add(order.getPayAmount());
                }
                yesToatlOrderCount++;
                yesTotalOrderPay = yesTotalOrderPay.add(order.getPayAmount());
            }
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -2))) {
                if (order.getStatus() < 9) {
                    yesOrderCount1++;
                    yesOrderPay1 = yesOrderPay1.add(order.getPayAmount());
                }
            }
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -3))) {
                if (order.getStatus() < 9) {
                    yesOrderCount2++;
                    yesOrderPay2 = yesOrderPay2.add(order.getPayAmount());
                }
            }
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -4))) {
                if (order.getStatus() < 9) {
                    yesOrderCount3++;
                    yesOrderPay3 = yesOrderPay3.add(order.getPayAmount());
                }
            }
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -5))) {
                if (order.getStatus() < 9) {
                    yesOrderCount4++;
                    yesOrderPay4 = yesOrderPay4.add(order.getPayAmount());
                }
            }
            if (DateUtils.format(order.getCreateTime()).equals(DateUtils.addDay(new Date(), -6))) {
                if (order.getStatus() < 9) {
                    yesOrderCount5++;
                    yesOrderPay5 = yesOrderPay5.add(order.getPayAmount());
                }
            }
            if (DateUtils.calculateDaysNew(order.getCreateTime(), new Date()) >= 7
                    && (order.getStatus() < 9)) {
                qiOrderCount++;
                qiOrderPay = qiOrderPay.add(order.getPayAmount());

            }
            if (order.getCreateTime().getTime() >= DateUtils.getLastMonthFirstDay().getTime()
                    && order.getCreateTime().getTime() <= DateUtils.getLastMonthLastDay().getTime()
                    && (order.getStatus() < 9)) {
                lastMonthOrderCount++;
                lastMonthOrderPay = lastMonthOrderPay.add(order.getPayAmount());
            }
            if (order.getCreateTime().getTime() >= DateUtils.getPreviousMonday().getTime()
                    && order.getCreateTime().getTime() <= DateUtils.getSunday().getTime()
                    && (order.getStatus() < 9)) {
                lastWeekOrderCount++;
                lastWeekOrderPay = lastWeekOrderPay.add(order.getPayAmount());
            }
            if (order.getCreateTime().getTime() >= DateUtils.geFirstDayDateByMonth().getTime()
                    && (order.getStatus() < 9)) {
                monthOrderCount++;
                monthOrderPay = monthOrderPay.add(order.getPayAmount());
            }
            if (order.getCreateTime().getTime() >= DateUtils.getFirstDayOfWeek().getTime()
                    && (order.getStatus() < 9)) {
                weekOrderCount++;
                weekOrderPay = weekOrderPay.add(order.getPayAmount());
            }
            if (order.getStatus() == OrderStatus.INIT.getValue()) {
                status0++;
            }
            if (order.getStatus() == OrderStatus.TO_DELIVER.getValue()) {
                status1++;
            }
            if (order.getStatus() == OrderStatus.DELIVERED.getValue()) {
                status2++;
            }
            if (order.getStatus() == OrderStatus.TO_COMMENT.getValue()) {
                status3++;
            }
            if (order.getStatus() == OrderStatus.TRADE_SUCCESS.getValue()) {
                status4++;
            }
            if (order.getStatus() == OrderStatus.REFUNDING.getValue()) {
                status5++;
            }
            if (order.getStatus() == OrderStatus.CLOSED.getValue()) {
                status6++;
            }

        }
        OrderStstic ststic =new OrderStstic();
        ststic.setTime(DateUtils.currentDay());
        ststic.setTotalCount(nowOrderCount);
        ststic.setTotalPayAmount(nowOrderPay);
        orderStsticList.add(ststic);

        OrderStstic ststic0 =new OrderStstic();
        ststic0.setTime(DateUtils.addDay(new Date(), -1));
        ststic0.setTotalCount(yesOrderCount);
        ststic0.setTotalPayAmount(yesOrderPay);
        orderStsticList.add(ststic0);

        OrderStstic ststic1 =new OrderStstic();
        ststic1.setTime(DateUtils.addDay(new Date(), -2));
        ststic1.setTotalCount(yesOrderCount1);
        ststic1.setTotalPayAmount(yesOrderPay1);
        orderStsticList.add(ststic1);

        OrderStstic ststic2 =new OrderStstic();
        ststic2.setTime(DateUtils.addDay(new Date(), -3));
        ststic2.setTotalCount(yesOrderCount2);
        ststic2.setTotalPayAmount(yesOrderPay2);
        orderStsticList.add(ststic2);

        OrderStstic ststic3 =new OrderStstic();
        ststic3.setTime(DateUtils.addDay(new Date(), -4));
        ststic3.setTotalCount(yesOrderCount3);
        ststic3.setTotalPayAmount(yesOrderPay3);
        orderStsticList.add(ststic3);

        OrderStstic ststic4 =new OrderStstic();
        ststic4.setTime(DateUtils.addDay(new Date(), -5));
        ststic4.setTotalCount(yesOrderCount4);
        ststic4.setTotalPayAmount(yesOrderPay4);
        orderStsticList.add(ststic4);

        OrderStstic ststic5 =new OrderStstic();
        ststic5.setTime(DateUtils.addDay(new Date(), -6));
        ststic5.setTotalCount(yesOrderCount5);
        ststic5.setTotalPayAmount(yesOrderPay5);
        orderStsticList.add(ststic5);

        count.setStatusAll(orderList.size());
        count.setStatus0(status0);
        count.setStatus1(status1);
        count.setStatus2(status2);
        count.setStatus3(status3);
        count.setStatus4(status4);
        count.setStatus5(status5);
        count.setStatus14(status6);

        data.setYesToatlOrderCount(yesToatlOrderCount);
        data.setYesTotalOrderPay(yesTotalOrderPay);
        data.setNowToatlOrderPay(nowToatlOrderPay);
        data.setNowTotalOrderCount(nowTotalOrderCount);
        data.setNowOrderCount(nowOrderCount);
        data.setNowOrderPay(nowOrderPay);
        data.setYesOrderCount(yesOrderCount);
        data.setYesOrderPay(yesOrderPay);
        data.setQiOrderCount(qiOrderCount);
        data.setQiOrderPay(qiOrderPay);
        data.setOrderStatusCount(count);
        data.setMonthOrderCount(monthOrderCount);
        data.setMonthOrderPay(monthOrderPay);
        data.setWeekOrderCount(weekOrderCount);
        data.setWeekOrderPay(weekOrderPay);
        data.setLastMonthOrderCount(lastMonthOrderCount);
        data.setLastMonthOrderPay(lastMonthOrderPay);
        data.setLastWeekOrderCount(lastWeekOrderCount);
        data.setLastWeekOrderPay(lastWeekOrderPay);
        data.setOrderStsticList(orderStsticList);
        return new CommonResult().success(data);
    }

    @ApiOperation("首页商品统计")
    @SysLog(MODULE = "home", REMARK = "首页商品统计")
    @RequestMapping(value = "/goodsStatic", method = RequestMethod.GET)
    public Object goodsStatic() throws Exception {
        StopWatch stopWatch = new StopWatch("首页商品统计");
        stopWatch.start("首页商品列表2");
        List<PmsProduct> goodsList = productService.list(new QueryWrapper<>(new PmsProduct()).select("publish_status", "create_time"));

        stopWatch.stop();
        stopWatch.start("首页商品");
        int onCount = 0;
        int offCount = 0;
        int nowCount = 0;
        int noStock = 0;
        int yesCount = 0;
        for (PmsProduct goods : goodsList) {
            if (goods.getPublishStatus() == 1) { // 上架状态：0->下架；1->上架
                onCount++;
            }
            if (goods.getPublishStatus() == 0) { // 上架状态：0->下架；1->上架
                offCount++;
            }
            if (ValidatorUtils.empty(goods.getStock()) || goods.getStock() < 1) { // 上架状态：0->下架；1->上架
                noStock++;
            }
            if (DateUtils.format(goods.getCreateTime()).equals(DateUtils.format(new Date()))) {
                nowCount++;
            }
            if (DateUtils.format(goods.getCreateTime()).equals(DateUtils.addDay(new Date(), -1))) {
                yesCount++;

            }
        }
        Map<String, Object> map = new HashMap();
        map.put("onCount", onCount);
        map.put("offCount", offCount);
        map.put("noStock", noStock);
        map.put("nowCount", nowCount);
        map.put("yesCount", yesCount);
        map.put("allCount", goodsList.size());
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return new CommonResult().success(map);
    }

    @ApiOperation("首页会员统计")
    @SysLog(MODULE = "home", REMARK = "首页会员统计")
    @RequestMapping(value = "/userStatic", method = RequestMethod.GET)
    public Object userStatic() throws Exception {
        List<UmsMember> memberList = memberService.list(new QueryWrapper<>());
        int nowCount = 0;
        int yesUserCount = 0; // 昨日
        int qiUserCount = 0; // 当日
        int mallCount = 0; // 当日
        int femallount = 0; // 当日
        for (UmsMember member : memberList) {
            if (DateUtils.format(member.getCreateTime()).equals(DateUtils.addDay(new Date(), -1))) {
                yesUserCount++;
            }
            if (member.getCreateTime().getTime() >= DateUtils.geFirstDayDateByMonth().getTime()) {
                qiUserCount++;
            }
            if (DateUtils.format(member.getCreateTime()).equals(DateUtils.format(new Date()))) {
                nowCount++;
            }
            if (member.getGender() == null || member.getGender() == 1) {
                mallCount++;
            } else {
                femallount++;
            }
        }
        Map<String, Object> map = new HashMap();
        map.put("qiUserCount", qiUserCount);
        map.put("yesUserCount", yesUserCount);
        map.put("nowCount", nowCount);
        map.put("allCount", memberList.size());
        map.put("mallCount", mallCount);
        map.put("femallount", femallount);
        return new CommonResult().success(map);
    }

    /**
     * 导出：http://localhost:8085/home/export/users
     * 导入：http://localhost:8085/demo/import/users (用postman或者curl测试吧)
     * http://localhost:8085/home/import/goods
     *
     * @param response
     */
    @RequestMapping(value = "/export/users", method = RequestMethod.GET)
    public void exportUsers(HttpServletResponse response) {
        List<ExportUser> userList = getUserList();
        EasyPoiUtils.exportExcel(getUserList(), "用户列表", "用户报表", ExportUser.class, "用户明细报表.xls", response);
    }

    @RequestMapping("/import/users")
    @ResponseBody
    public List<ExportUser> importUsers(@RequestParam MultipartFile file) {
        List<ExportUser> d = EasyPoiUtils.importExcel(file, ExportUser.class);
        return EasyPoiUtils.importExcel(file, ExportUser.class);
    }

    @RequestMapping("/import/goods")
    @ResponseBody
    public void importgoods(@RequestParam MultipartFile file) {
        List<ExportGoods> list = EasyPoiUtils.importExcel(file, ExportGoods.class);
        for (ExportGoods gg : list) {
            createG(gg);
        }
    }

    @RequestMapping("/import/subject")
    @ResponseBody
    public void importSubject(@RequestParam MultipartFile file) {
        List<SysArea> areas = sysAreaMapper.selectList(new QueryWrapper<>());
        List<SysSchool> schools = schoolMapper.selectList(new QueryWrapper<>());
        List<ExportSubject> list = EasyPoiUtils.importExcel(file, ExportSubject.class);
        for (ExportSubject subject1 : list) {
            try {
                CmsSubject subject = new CmsSubject();
                subject.setTitle(subject1.getTitle());
                subject.setContent(subject1.getContent());
                subject.setPic(subject1.getPic());
                subject.setAlbumPics(subject1.getPic());


                Random r = new Random();
                Integer a = r.nextInt(100);
                Integer c = r.nextInt(3);
                Integer d = r.nextInt(5);
                subject.setCollectCount(r.nextInt(100));
                subject.setReadCount(r.nextInt(100));
                subject.setForwardCount(r.nextInt(100));

                subject.setCategoryName("游戏专题");
                subject.setCategoryId(2L);


                subject.setType(c);
                Integer b = r.nextInt(100);
                SysSchool school = schools.get(a);
                if (school != null) {
                    subject.setSchoolId(school.getId());
                    subject.setSchoolName(school.getName());
                } else {
                    SysSchool school1 = schools.get(b);
                    if (school1 != null) {
                        subject.setSchoolId(school1.getId());
                        subject.setSchoolName(school1.getName());
                    }
                }

                SysArea area = areas.get(b);
                if (area != null) {
                    subject.setAreaId(area.getId());
                    subject.setAreaName(area.getName());
                } else {
                    SysArea area1 = areas.get(a);
                    if (area1 != null) {
                        subject.setAreaId(area1.getId());
                        subject.setAreaName(area1.getName());
                    }
                }
                subjectMapper.insert(subject);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    void createG(ExportGoods gg) {
        PmsProduct g = new PmsProduct();
        g.setName(gg.getUsername());
        g.setSubTitle(gg.getUsername());
        g.setDescription(gg.getDetail1());
        g.setDetailHtml(gg.getDetail());
        g.setDetailMobileHtml(gg.getDetail());
        g.setDetailTitle(gg.getUsername());
        g.setDetailDesc(gg.getUsername());

        g.setPic(gg.getImg());
        g.setAlbumPics(gg.getImg());
        if (ValidatorUtils.notEmpty(gg.getPrice())) {
            g.setPrice(new BigDecimal(gg.getPrice().substring(1)));
        }
        if (ValidatorUtils.notEmpty(gg.getOriginprice())) {
            g.setOriginalPrice(new BigDecimal(gg.getOriginprice().substring(1)));
        }


        g.setSale(0);
        g.setStock(0);
        g.setLowStock(0);
        g.setGiftPoint(0);
        g.setGiftGrowth(0);
        g.setDeleteStatus(1);
        g.setPublishStatus(1);
        g.setPromotionType(0);
        g.setVerifyStatus(1);
        g.setProductSn("X" + System.currentTimeMillis());
        g.setQsType(1);
        g.setNewStatus(1);
        g.setCreateTime(new Date());

        g.setBrandId(64L);
        g.setBrandName("红蜻蜓");
        g.setProductCategoryId(61L);
        g.setProductCategoryName("品牌男鞋");
        g.setProductAttributeCategoryId(11L);
        productService.save(g);
    }

    private List<ExportUser> getUserList() {
        List<ExportUser> userList = new ArrayList<>();
        userList.add(new ExportUser("tom", new Date()));
        userList.add(new ExportUser("jack", new Date()));
        userList.add(new ExportUser("123", new Date()));
        return userList;
    }

}
