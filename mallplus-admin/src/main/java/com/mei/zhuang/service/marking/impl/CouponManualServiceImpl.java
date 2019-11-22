package com.mei.zhuang.service.marking.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.controller.marking.TestCSVUtil;
import com.mei.zhuang.dao.marking.*;
import com.mei.zhuang.dao.member.EsMemberMapper;
import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.service.marking.CouponManualService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
     *  手工发券
 * </p>
 *
 * @author arvato team
 * @since 2019-05-18
 */

@Service
@Configuration
@EnableAsync
public class CouponManualServiceImpl extends ServiceImpl<EsShopCouponManualMapper, EsShopCouponManual> implements CouponManualService {

    @Resource
    private EsShopCouponManualMapper couponManualMapper;
    @Resource
    private EsShopCouponNewRuleMapper couponNewRuleMapper;
    @Resource
    private EsShopCouponGoodsMapMapper goodsMapMapper;
    @Resource
    private EsShopCouponsTopupGoodsMapper couponsTopupGoodsMapper;
    @Resource
    private EsShopCouponManualUserMapper manualUserMapper;
    @Resource
    private EsMemberCouponMapper couponMapper;

    @Resource
    private EsShopCouponMapper escouponMapper;
    @Resource
    private EsMemberMapper memberMapper;

    private int cou;

    public void SetCou(int cou){
        this.cou=cou;
    }

    public int getCou() {
        return cou;
    }

    MemberCouponServiceImpl memberCouponService=new MemberCouponServiceImpl();
    public void datetime(EsShopCouponManual en) throws Exception {
        // 1.发送 2.停止  1，开启，2指定时间，3关闭
        if (en.getActivityOpen() == 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            en.setTime(sdf.format(new Date()));
            en.setStatus(2);
            en.setStatusissue(1);
        } else {
            if (en.getTime() != null && !en.getTime().equals("")) {
                //  状态 1 已完成，2 发送中 3，未开 始 4.停止
                en.setStatusissue(2);
                en.setStatus(3);
            } else {
                en.setStatusissue(2);
                en.setStatus(3);
            }
        }
    }
    //读取上传用户文件
    public void csvfile(EsShopCouponManual entity) throws IOException {
        TestCSVUtil csvUtil=new TestCSVUtil();
        if(entity.getSendObject()==2) {
            List<String> source = csvUtil.readcsvFile(entity.getPath());
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<source.size();i++){
                if(i==source.size()-1){
                    builder.append(source.get(i));
                    break;
                }
                builder.append(source.get(i)).append(",");
                System.out.println(builder.toString());
            }
            String value=builder.toString();
            String st[]=value.split(",");
          for(String str: st){
               EsShopCouponManualUser manualUser=new EsShopCouponManualUser();
               manualUser.setUserId(str);
               manualUser.setManualId(entity.getId());
              manualUserMapper.insert(manualUser);
          }
        }
    }


    @Transactional(rollbackFor =Exception.class )
    @Override
    public boolean save(EsShopCouponManual entity)  {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,20);
        entity.setCouponManualid(uuid);
        try {
            datetime(entity);
            couponManualMapper.insert(entity);
            csvfile(entity);
            addsave(entity);
            if(entity.getMessage()==3){
                sendManualCoupon(entity.getMessage(), entity.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    //立即发放
    @Async
    public  void makeRealData (EsShopCouponManual topup){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            EsMemberCoupon member = new EsMemberCoupon();
            Timer time2=new Timer();
            time2.schedule(new TimerTask() {
                boolean b=false;
                Integer current=0;
                List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponManualid()));
                @Override
                public void run() {
                    EsShopCoupon escoupon = escouponMapper.selectById(couponNewRules.get(0).getCouponid());
                    if(escoupon.getStock()==0){
                        ShopCouponManual(topup);
                        time2.cancel();
                    }
                    if(escoupon.getStock()<topup.getStampsNumber()){
                        Integer namesum = couponMapper.namesum(couponNewRules.get(0).getCouponName(),topup.getId());
                        System.out.println("数量发券--"+namesum);
                        EsShopCouponManual manuall = new EsShopCouponManual();
                        manuall.setId(topup.getId());
                        manuall.setStatus(4);
                        manuall.setStatusissue(2);
                        manuall.setSendNumber(namesum);
                        couponManualMapper.updateById(manuall);
                        time2.cancel();
                    }
                    if(cou==1){
                        Integer namesum = couponMapper.namesum(couponNewRules.get(0).getCouponName(),topup.getId());
                        System.out.println("数量发券--"+namesum);
                        EsShopCouponManual manuall = new EsShopCouponManual();
                        manuall.setId(topup.getId());
                        manuall.setStatus(4);
                        manuall.setSendNumber(namesum);
                        couponManualMapper.updateById(manuall);
                        time2.cancel();
                    }
                    if (cou == 0) {
                        List<EsMember> memberselect = memberMapper.memberselect(10,current);
                        Integer sum = memberselect.size();
                        current += 10;
                        if(memberselect.size()==0){
                            ShopCouponManual(topup);
                            time2.cancel();
                        }else {
                            for (EsMember me : memberselect) {
                                if (sum < 10) {
                                    System.out.println("sumsdsadasd" + sum);
                                    b = true;
                                    break;
                                } else {
                                    EsShopCouponManual man = couponManualMapper.selectById(topup.getId());
                                    topup.setSendNumber(man.getSendNumber());
                                    addManualCoupon(member, sdf, topup, me.getId(), me.getOpenid());
                                }
                            }
                            if (b==true) {

                                if (memberselect.size() < 0 && memberselect == null) {
                                    time2.cancel();
                                } else {
                                    for (EsMember ms : memberselect) {
                                        addManualCoupon(member, sdf, topup, ms.getId(), ms.getOpenid());
                                    }
                                    ShopCouponManual(topup);
                                    time2.cancel();
                                }
                            }
                        }
                    }
                }
            },1000,5000);

        }catch(Throwable t){}
    }
    //指定发放
    public synchronized void specifiedCoupon(EsShopCouponManual topup){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EsMemberCoupon member = new EsMemberCoupon();
        Timer time2=new Timer();
        time2.schedule(new TimerTask() {
            Integer current=0;
            List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid",topup.getCouponManualid()));
            @Override
            public void run() {
                EsShopCoupon escoupon = escouponMapper.selectById(couponNewRules.get(0).getCouponid());
                if(escoupon.getStock()==0){
                    Integer namesum = couponMapper.namesum(couponNewRules.get(0).getCouponName(),topup.getId());
                    System.out.println("数量发券--"+namesum);
                    EsShopCouponManual manuall = new EsShopCouponManual();
                    manuall.setId(topup.getId());
                    manuall.setStatus(4);
                    manuall.setStatusissue(2);
                    manuall.setSendNumber(namesum);
                    couponManualMapper.updateById(manuall);
                    time2.cancel();
                }
                if(escoupon.getStock()<topup.getStampsNumber()){
                    Integer namesum = couponMapper.namesum(couponNewRules.get(0).getCouponName(),topup.getId());
                    System.out.println("数量发券--"+namesum);
                    EsShopCouponManual manuall = new EsShopCouponManual();
                    manuall.setId(topup.getId());
                    manuall.setStatus(4);
                    manuall.setStatusissue(2);
                    manuall.setSendNumber(namesum);
                    couponManualMapper.updateById(manuall);
                    time2.cancel();
                }
                if(cou==1){
                    Integer namesum = couponMapper.namesum(couponNewRules.get(0).getCouponName(),topup.getId());
                    System.out.println("数量发券--"+namesum);
                    EsShopCouponManual manuall = new EsShopCouponManual();
                    manuall.setId(topup.getId());
                    manuall.setStatus(4);
                    manuall.setSendNumber(namesum);
                    couponManualMapper.updateById(manuall);
                    time2.cancel();
                }
                if(cou==0){
                    List<EsMember> memberselect = memberMapper.memberselect(0, 0);
                    //current += 100;
                /*    if(memberselect.size()==0){
                        System.out.println("用户已发完---");
                        ShopCouponManual(topup);
                        time2.cancel();
                    }else{*/
                        List<EsShopCouponManualUser> userList = manualUserMapper.selectList(new QueryWrapper<EsShopCouponManualUser>().eq("manual_id",topup.getId()).eq("status","1"));
                        if (userList != null && userList.size() > 0) {
                            for (EsShopCouponManualUser me : userList) {
                                for (EsMember mes : memberselect) {
                                    if (me.getUserId().contains(mes.getOpenid())) {
                                        System.out.println("指定用户已执行");
                                        manualUserMapper.updateuser(me.getUserId(),me.getManualId());
                                        addManualCoupon(member, sdf,topup, mes.getId(), mes.getOpenid());
                                    }
                                }
                            }
                        }else{
                            ShopCouponManual(topup);
                            time2.cancel();
                        }

                }
            }
        },1000,6000);
    }

    public void ShopCouponManual(EsShopCouponManual topup){
        List<EsShopCouponNewRule> couponNewRules2 = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponManualid()));
        Integer namesum = couponMapper.namesum(couponNewRules2.get(0).getCouponName(),topup.getId());
        System.out.println("数量发券--"+namesum);
        EsShopCouponManual manuall = new EsShopCouponManual();
        manuall.setId(topup.getId());
        manuall.setStatus(1);
        manuall.setStatusissue(2);
        manuall.setSendNumber(namesum);
        couponManualMapper.updateById(manuall);
    }



    void sendManualCoupon(Integer message,long id) throws ParseException {
        EsMemberCoupon member = new EsMemberCoupon();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<EsShopCouponManual> list = couponManualMapper.selectList(new QueryWrapper<EsShopCouponManual>().eq("status", 2).eq("message",message).eq("id",id));
        for (EsShopCouponManual topup : list) {
            // 活动开启 1，开启，2指定时间，3关闭
            if (topup.getActivityOpen()== 3||topup.getActivityOpen()== 2) {
                continue;
            }else{
                //发券对象 1,全部用户2，指定用户，3仅会员4，会员等级
                if (topup.getSendObject() == 2) {
                    new Thread(){
                        public void run(){
                            specifiedCoupon(topup);
                        }
                    }.start();

                } else {
                    System.out.println("执行立即发");
                    new Thread(){
                        public void run(){
                            makeRealData(topup);
                        }
                    }.start();
                }
            }
        }
    }


    @Async
   public  void addManualCoupon( EsMemberCoupon member, SimpleDateFormat sdf, EsShopCouponManual topup,long memberid,String openid) {
        List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", topup.getCouponManualid()));
        for (EsShopCouponNewRule rule : couponNewRules) {
            EsShopCoupon coupon = escouponMapper.selectById(rule.getCouponid());
            Integer coupon3 = 0;
            if (coupon.getStock() > 0) {
                if (coupon.getStock() < topup.getStampsNumber()) {
                   continue;
                } else {
                    coupon3 = topup.getStampsNumber();
                }
                for (int i = 0; i < coupon3; i++) {
                    member.setFroms(5);
                    member.setCreateTime(sdf.format(new Date()));
                    member.setCouponId(rule.getCouponid());
                    member.setTitle(rule.getCouponName());
                    member.setStartTime(coupon.getExpiryBeginTime());
                    member.setEndTime(coupon.getExpiryEndTime());
                    member.setUserOpenid(openid);
                    //有效期 1固定，2当前，3次日
                    Date createTime = new Date();
                    if (coupon.getAmount() == 2) {
                        member.setStartTime(createTime);
                        member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
                    }
                    if (coupon.getAmount() == 3) {
                        member.setStartTime(this.addDays(createTime, 1));
                        member.setEndTime(this.addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
                    }
                    member.setConditions(1);
                    //用户id
                    member.setMemberId(memberid);
                    //金额或折扣
                    member.setAmount(coupon.getEnough());
                    member.setShopId((long) 1);
                    member.setType(coupon.getType());
                    //有效期状态
                    member.setEffective(coupon.getStatus());
                    member.setTypeId(topup.getId());
                    //券说明
                    member.setDescription(coupon.getDescription());
                    member.setStatus(1);
                    couponMapper.insert(member);

                }

                coupon.setStock((coupon.getStock() - coupon3) > 0 ? (coupon.getStock() - coupon3) : 0);
                escouponMapper.updateById(coupon);
            }
        }
    }

    public static String addDay(Date s, int n) {
        SimpleDateFormat FORMATER_DATE_YMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cd = Calendar.getInstance();
        cd.setTime(s);
        cd.add(5, n);
        return FORMATER_DATE_YMD.format(cd.getTime());
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


    //连表删除
    @Transactional(rollbackFor =Exception.class )
    @Override
    public Integer deleteManual(long id) {
        EsShopCouponManual esManual = couponManualMapper.selectById(id);
        couponNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid",esManual.getCouponManualid()));
        couponManualMapper.delete(new QueryWrapper<EsShopCouponManual>().eq("coupon_manualid",esManual.getCouponManualid()));
        return 1;
    }

    @Override
    public boolean update(EsShopCouponManual CouponManual) throws Exception {
        datetime(CouponManual);
        couponManualMapper.updateById(CouponManual);
        manualUserMapper.delete(new QueryWrapper<EsShopCouponManualUser>().eq("manual_id", CouponManual.getId()));
        csvfile(CouponManual);
        EsShopCouponManual manual = couponManualMapper.selectById(CouponManual.getId());
        //实物发券查询和赠品
        List<EsShopCouponNewRule> physicalId = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", manual.getCouponManualid()));

        //实物商品和赠品
         couponsTopupGoodsMapper.delete(new QueryWrapper<EsShopCouponsTopupGoods>().eq("physical_id", physicalId.get(0).getId()));
         couponNewRuleMapper.delete(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", manual.getCouponManualid()));
        CouponManual.setCouponManualid(manual.getCouponManualid());
        addsave(CouponManual);
        if(CouponManual.getMessage()==3){
            sendManualCoupon(CouponManual.getMessage(),CouponManual.getId());
        }
        return true;
    }

    @Override
    public Integer updatestatus(long id,Integer  statusissue) {
           //  1.发送 2.停止  statusissue   1 已完成，2 发送中 3，未开始 4.停止
        if( statusissue==1) {
            EsShopCouponManual manual = new EsShopCouponManual();
            manual.setId(id);
            manual.setStatusissue(1);
            manual.setStatus(2);
            Calendar c = new GregorianCalendar();
            Date date=new Date();
            c.setTime(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            c.add(Calendar.SECOND,+60);
            date=c.getTime();
            manual.setTime(sdf.format(date));
            return couponManualMapper.updateById(manual);
        }else{
            EsShopCouponManual manual = new EsShopCouponManual();
            manual.setId(id);
            manual.setStatusissue(2);
            manual.setStatus(4);
            return couponManualMapper.updateById(manual);
        }
    }

    @Override
    public EsShopCouponManual ManualList(long id) {
        EsShopCouponManual manual = couponManualMapper.selectById(id);
        List<EsShopCouponNewRule> couponid = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid",manual.getCouponManualid()));
        manual.setNewRuleList(couponid);
        return manual;
    }

    //手工券查询
    @Override
    public List<Map<String, Object>> selectmanual(EsShopCouponManual esShopCouponManual) throws Exception {

        return couponManualMapper.selectmanual(esShopCouponManual );
    }



    public void addsave(EsShopCouponManual entity) throws Exception {
        if(entity.getNewRuleList()!=null&&entity.getNewRuleList().size()>0){
            for(EsShopCouponNewRule cnr:entity.getNewRuleList()){
                EsShopCouponNewRule  ecn=new EsShopCouponNewRule();
                ecn.setCouponid(cnr.getCouponid());
                ecn.setInventory(cnr.getInventory());
                ecn.setCouponName(cnr.getCouponName());
                ecn.setPublicCouponid(entity.getCouponManualid());
                ecn.setCouponTypes(cnr.getCouponTypes());
                couponNewRuleMapper.insert(ecn);
                EsShopCouponNewRule NewRule = couponNewRuleMapper.selectById(ecn.getId());
                //赠品券
                if(NewRule.getCouponTypes()==4){
                    List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selcetcoupongoods(NewRule.getCouponid());
                    for(EsShopCouponGoodsMap goods:GoodsMaps) {
                        EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                        trg.setGoodId(goods.getGoodsId());
                        trg.setSpecificationsId(goods.getSpecIds());
                        trg.setPhysicalId(NewRule.getId());
                        couponsTopupGoodsMapper.insert(trg);
                    }
                }
                //商品券
                if(NewRule.getCouponTypes()==3){
                    List<EsShopCouponGoodsMap> GoodsMaps = goodsMapMapper.selectgoods2(NewRule.getCouponid());
                    for(EsShopCouponGoodsMap goods:GoodsMaps) {
                        EsShopCouponsTopupGoods trg = new EsShopCouponsTopupGoods();
                        trg.setGoodId(goods.getGoodsId());
                        trg.setSpecificationsId(goods.getSpecIds());
                        trg.setPhysicalId(NewRule.getId());
                        couponsTopupGoodsMapper.insert(trg);
                    }
                }
            }
        }
    }



}
