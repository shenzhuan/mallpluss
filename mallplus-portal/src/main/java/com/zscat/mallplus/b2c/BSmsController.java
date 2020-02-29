package com.zscat.mallplus.b2c;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.IgnoreAuth;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.BargainEnum;
import com.zscat.mallplus.enums.ConstansValue;
import com.zscat.mallplus.pms.entity.PmsFavorite;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsFavoriteService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.vo.GoodsDetailResult;
import com.zscat.mallplus.single.ApiBaseAction;
import com.zscat.mallplus.sms.entity.*;
import com.zscat.mallplus.sms.mapper.SmsBargainConfigMapper;
import com.zscat.mallplus.sms.mapper.SmsBargainMemberMapper;
import com.zscat.mallplus.sms.mapper.SmsBargainRecordMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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

    private static Lock lock = new ReentrantLock(false);
    @Value("${file.path}")
    private String path;
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
    @Resource
    private SmsBargainConfigMapper bargainConfigMapper;
    @Resource
    private SmsBargainRecordMapper bargainRecordMapper;
    @Resource
    private SmsBargainMemberMapper bargainMemberMapper;

    /**
     * 生成 [m,n] 的数字
     * int i1 = random.nextInt() * (n-m+1)+m;
     */
    public static void main(String[] args) {
        int max = 5;
        int min = 1;
        int num = new Random().nextInt(max - min + 1) + min;
        System.out.println(num);
        Random random = new Random();
        int i1 = new Random().nextInt() * (5 - 1 + 1) + 1;
        System.out.println(i1);
        String group = "1,5";
        String[] rands = group.split(",");
        double dd = Double.valueOf(rands[1]) - Double.valueOf(rands[0]) + 1;
        System.out.println(Double.valueOf(rands[1]) - Double.valueOf(rands[0]) + 1);
        System.out.println(random.nextInt());
        System.out.println(random.nextInt() * (Double.valueOf(rands[1]) - Double.valueOf(rands[0]) + 1));
        System.out.println(Double.valueOf(rands[0]));
        double pp = random.nextInt() * dd + Double.valueOf(rands[0]);
        System.out.println(pp);
    }

    @SysLog(MODULE = "pms", REMARK = "查询砍价商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询砍价商品列表")
    @ResponseBody
    @PostMapping(value = "/bargain/list")
    public Object groupHotGoods(PmsProduct product,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        List<SmsBargainConfig> groupList = bargainConfigMapper.selectList(new QueryWrapper<>());
        List<SmsBargainConfig> result = new ArrayList<>();
        for (SmsBargainConfig group : groupList) {

            Long nowT = System.currentTimeMillis();
            if (nowT < group.getInvalidTime().getTime()) {
                PmsProduct g = pmsProductService.getById(group.getGoodsId());
                if (g != null) {
                    group.setGoods(g);
                    result.add(group);
                }

            }
        }
        return new CommonResult().success(result);
    }

    @SysLog(MODULE = "pms", REMARK = "查询砍价商品列表")
    @IgnoreAuth
    @ApiOperation(value = "查询砍价商品列表")
    @ResponseBody
    @PostMapping(value = "/sample/bargain/list")
    public Object pintuanList(PmsProduct product,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        List<SmsBargainConfig> groupList = bargainConfigMapper.selectList(new QueryWrapper<>());
        if (groupList != null && groupList.size() > 0) {
            List<Long> ids = groupList.stream()
                    .map(SmsBargainConfig::getGoodsId)
                    .collect(Collectors.toList());
            product.setPublishStatus(1);
            product.setVerifyStatus(1);
            product.setMemberId(null);
            IPage<PmsProduct> list = pmsProductService.page(new Page<PmsProduct>(pageNum, pageSize), new QueryWrapper<>(product).in("id", ids).select(ConstansValue.sampleGoodsList));
            return new CommonResult().success(list);
        }
        return new CommonResult().success(new ArrayList<>());
    }

    @SysLog(MODULE = "pms", REMARK = "砍价商品详情")
    @IgnoreAuth
    @PostMapping(value = "/bargain/detail")
    @ApiOperation(value = "砍价商品详情")
    public Object groupGoodsDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        SmsBargainConfig group = bargainConfigMapper.selectById(id);
        Long goodsId = group.getGoodsId();

        GoodsDetailResult goods = null;
        try {
            goods = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.GOODSDETAIL, goodsId + "")), GoodsDetailResult.class);
            if (ValidatorUtils.empty(goods)) {
                log.info("redis缓存失效：" + goodsId);
                goods = pmsProductService.getGoodsRedisById(goodsId);
            }
        } catch (Exception e) {
            log.info("redis缓存失效：" + goodsId);
            goods = pmsProductService.getGoodsRedisById(goodsId);
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        UmsMember umsMember = memberService.getNewCurrentMember();

        if (group != null && umsMember != null) {
            // group.setPintuan_start_status(1);
            // group.setTimeSecound(ValidatorUtils.getTimeSecound(group.getEndTime()));
            Long nowT = System.currentTimeMillis();

            /*if (nowT > endTime.getTime()) {
                group.setPintuan_start_status(3);
            }*/
            SmsBargainRecord groupRecords = bargainRecordMapper.selectOne(new QueryWrapper<SmsBargainRecord>().eq("bargin_id", group.getId()).eq("member_id", umsMember.getId()));
            if (groupRecords != null) {
                List<SmsBargainMember> groupMembers = bargainMemberMapper.selectList(new QueryWrapper<SmsBargainMember>().eq("bargin_record_id", groupRecords.getId()));
                groupRecords.setList(groupMembers);
            } else {
                groupRecords = new SmsBargainRecord();
                groupRecords.setBarginId(group.getId());
                groupRecords.setCreateTime(new Date());
                groupRecords.setMemberId(umsMember.getId());
                groupRecords.setName(umsMember.getUsername());
                groupRecords.setStatus(BargainEnum.Status.INIT.code());

                SmsBargainMember query = new SmsBargainMember();
                query.setMemberId(umsMember.getId());
                query.setBargainId(group.getId());
                query.setGoodsId(group.getGoodsId());
                query.setBarginRecordId(groupRecords.getId());
                query.setCreateTime(new Date());
                query.setMemberId(umsMember.getId());
                query.setName(umsMember.getUsername());
                /**
                 * 生成 [m,n] 的数字
                 * int i1 = random.nextInt() * (n-m+1)+m;
                 * */
                String[] rands = group.getParameter().split(",");
                int max = Integer.parseInt(rands[1]);
                int min = Integer.parseInt(rands[0]);
                int pp = new Random().nextInt(max - min + 1) + min;
                query.setPrice(new BigDecimal(pp).setScale(2, RoundingMode.HALF_UP));

                couponService.insertBarginRe(groupRecords, query);

            }
            map.put("memberGroupList", groupRecords);
            map.put("group", group);
        }
        map.put("goods", goods);
        return new CommonResult().success(map);
    }

    /**
     * 帮助好友砍价
     */
    @PostMapping("/bargain/help")
    @ApiOperation(value = "帮助好友砍价", notes = "帮助好友砍价")
    public Object help(@RequestParam(value = "bargainId", required = false, defaultValue = "0") Long bargainId,
                       @RequestParam(value = "barginRecordId", required = false, defaultValue = "0") Long barginRecordId
    ) {
        if (ObjectUtil.isNull(bargainId)) return new CommonResult().failed("参数错误");
        Map<String, Object> map = new LinkedHashMap<>();
        UmsMember member = memberService.getNewCurrentMember();
        if (member != null) {
            SmsBargainMember query = new SmsBargainMember();
            query.setMemberId(member.getId());
            query.setBargainId(bargainId);
            if (bargainMemberMapper.selectCount(new QueryWrapper<>(query)) > 0) {
                map.put("status", "SUCCESSFUL");
                return new CommonResult().success(map);
            }
            try {
                lock.lock();
                SmsBargainConfig group = bargainConfigMapper.selectById(bargainId);
                if (group == null) {
                    return new CommonResult().failed("参数错误");
                }
                query.setCreateTime(new Date());
                query.setMemberId(member.getId());
                query.setName(member.getUsername());
                /**
                 * 生成 [m,n] 的数字
                 * int i1 = random.nextInt() * (n-m+1)+m;
                 * */
                String[] rands = group.getParameter().split(",");
                int max = Integer.parseInt(rands[1]);
                int min = Integer.parseInt(rands[0]);
                int pp = new Random().nextInt(max - min + 1) + min;
                query.setPrice(new BigDecimal(pp).setScale(2, RoundingMode.HALF_UP));
                query.setGoodsId(group.getGoodsId());
                query.setBarginRecordId(barginRecordId);
                bargainMemberMapper.insert(query);

                BigDecimal alreadPrice = BigDecimal.ZERO; //已经砍了的价格
                List<SmsBargainMember> groupMembers = bargainMemberMapper.selectList(new QueryWrapper<SmsBargainMember>().eq("bargin_record_id", barginRecordId));
                for (SmsBargainMember bargainMember : groupMembers) {
                    alreadPrice = alreadPrice.add(bargainMember.getPrice());
                }
                if (alreadPrice.compareTo(group.getPrice()) > 0) {
                    group.setPepoles(group.getPepoles() + 1);
                    bargainConfigMapper.updateById(group);

                    SmsBargainRecord record = new SmsBargainRecord();
                    record.setId(barginRecordId);
                    record.setStatus(BargainEnum.Status.SUCESS.code());
                    bargainRecordMapper.updateById(record);
                }
            } finally {
                lock.unlock();
            }

            map.put("status", "SUCCESS");
            return new CommonResult().success(map);
        }


        return new CommonResult().fail(100);
    }

    /**
     * 拼团海报
     */
    @PostMapping("/bargain/poster")
    @ApiOperation(value = "拼团海报", notes = "拼团海报")
    public Object poster(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

        if (ObjectUtil.isNull(id)) return new CommonResult().failed("参数有误");

        SmsBargainRecord group = bargainRecordMapper.selectById(id);
        return new CommonResult().success(group);
        /*String siteUrl = "siteUrl";
        if(StrUtil.isEmpty(siteUrl)){
            return new CommonResult().failed("未配置h5地址");
        }
        String apiUrl = "apiUrl";
        if(StrUtil.isEmpty(siteUrl)){
            return new CommonResult().failed("未配置api地址");
        }
        UmsMember member = memberService.getNewCurrentMember();
        if (member!=null){
            Long uid=member.getId();

            BigDecimal alreadPrice=BigDecimal.ZERO; //已经砍了的价格
            SmsBargainRecord groupRecords = bargainRecordMapper.selectOne(new QueryWrapper<SmsBargainRecord>().eq("bargin_id", group.getId()).eq("member_id",member.getId()));
            if (groupRecords!=null){
                List<SmsBargainMember> groupMembers = bargainMemberMapper.selectList(new QueryWrapper<SmsBargainMember>().eq("bargin_record_id", groupRecords.getId()));
                groupRecords.setList(groupMembers);
                for (SmsBargainMember bargainMember: groupMembers){
                    alreadPrice=alreadPrice.add(bargainMember.getPrice());
                }
            }else {
                return new CommonResult().success("没有参加");
            }

            //用户可以砍掉的金额 好友砍价之前获取可以砍价金额
            double coverPrice = NumberUtil.sub(group.getOriginPrice()
                    ,group.getPrice()).doubleValue();
            //用户剩余要砍掉的价格
            double surplusPrice = NumberUtil.sub(group.getOriginPrice(),
                    alreadPrice).doubleValue();


            String name = id+"_"+uid + "_"+"_bargain_share_wap.jpg";

            String fileDir = path+"qrcode"+ File.separator;
            String qrcodeUrl = "";
            if(ObjectUtil.isNull(groupRecords.getShareimg())){
                //生成二维码
                File file = FileUtil.mkdir(new File(fileDir));
                QrCodeUtil.generate(siteUrl+"/activity/dargain_detail/"+id+"/"+uid+"?spread="+uid, 180, 180,
                        FileUtil.file(fileDir+name));


                qrcodeUrl = fileDir+name;
            }else{
                qrcodeUrl = groupRecords.getShareimg();
            }

            String spreadPicName = id+"_"+uid + "_"+"_bargain_user_spread.jpg";
            String spreadPicPath = fileDir+spreadPicName;


            String spreadUrl = "";
            InputStream stream =  getClass().getClassLoader().getResourceAsStream("poster.jpg");
            InputStream streamT =  getClass().getClassLoader()
                    .getResourceAsStream("simsunb.ttf");
            File newFile = new File("poster.jpg");
            File newFileT = new File("simsunb.ttf");
            try {
                FileUtils.copyInputStreamToFile(stream, newFile);
                FileUtils.copyInputStreamToFile(streamT, newFileT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(ObjectUtil.isNull(groupRecords.getSpreadpic())){
                try {

                    //第一步标题
                    Font font =  Font.createFont(Font.TRUETYPE_FONT, newFileT);
                    Font f= font.deriveFont(Font.PLAIN,40);
                    //font.
                    ImgUtil.pressText(//
                            newFile,
                            FileUtil.file(spreadPicPath),
                            group.getGoodsName(),
                            Color.BLACK,
                            f, //字体
                            0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            -480, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                    );

                    Font f2= font.deriveFont(Font.PLAIN,45);
                    //第2步价格
                    ImgUtil.pressText(//
                            FileUtil.file(spreadPicPath),
                            FileUtil.file(spreadPicPath),
                            NumberUtil.sub(group.getPrice(),
                                    group.getPrice()).toString(),
                            Color.RED,
                            f2, //字体
                            -160, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            -380, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                    );

                    Font f3= font.deriveFont(Font.PLAIN,30);
                    //第3步几人团
                    ImgUtil.pressText(//
                            FileUtil.file(spreadPicPath),
                            FileUtil.file(spreadPicPath),
                            "已砍至",
                            Color.WHITE,
                            f3, //字体
                            90, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            -385, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                    );

                    //第4步介绍
                    String pro = "还差还差" + surplusPrice + "即可砍价成功";
                    ImgUtil.pressText(//
                            FileUtil.file(spreadPicPath),
                            FileUtil.file(spreadPicPath),
                            pro,
                            Color.BLACK,
                            f3, //字体
                            -50, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            -300, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                    );

                    //第5步商品图片
                    //下载图片
                    String picImage = fileDir+id+"_bargain_image.jpg";
                    HttpUtil.downloadFile(groupRecords.getSpreadpic(),
                            FileUtil.file(picImage));

                    ImgUtil.scale(
                            FileUtil.file(picImage),
                            FileUtil.file(picImage),
                            0.5f//缩放比例
                    );

                    ImgUtil.pressImage(
                            FileUtil.file(spreadPicPath),
                            FileUtil.file(spreadPicPath),
                            ImgUtil.read(FileUtil.file(picImage)), //水印图片
                            0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            -80, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            0.8f
                    );

                    ImgUtil.pressImage(
                            FileUtil.file(spreadPicPath),
                            FileUtil.file(spreadPicPath),
                            ImgUtil.read(FileUtil.file(qrcodeUrl)), //水印图片
                            0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            390, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            0.8f
                    );


                    spreadUrl = apiUrl + "/api/file/qrcode/"+spreadPicName;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                spreadUrl = apiUrl + "/api/file/" ;
            }
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("url",spreadUrl);
            return new CommonResult().success(map);
        }

       return new CommonResult().success();*/
    }

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
