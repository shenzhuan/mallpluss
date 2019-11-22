package com.mei.zhuang.controller.marking;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.marking.*;
import com.mei.zhuang.dao.member.EsMemberMapper;
import com.mei.zhuang.entity.marking.EsShopCoupon;
import com.mei.zhuang.entity.marking.EsShopCouponManual;
import com.mei.zhuang.entity.marking.EsShopCouponManualUser;
import com.mei.zhuang.entity.marking.EsShopCouponNewRule;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import com.mei.zhuang.service.marking.impl.CouponManualServiceImpl;
import com.mei.zhuang.service.order.MembersFegin;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@Component
@Configuration
@EnableAsync
@Slf4j
@Data
public class CouponTime {

    @Resource
    private EsShopCouponMapper couponMapper;
    @Resource
    private EsMemberCouponMapper couponMemberMapper;
    @Resource
    private EsShopCouponManualMapper manualMapper;
    @Resource
    private MembersFegin membersFegin;
    @Resource
    private EsShopCouponManualUserMapper manualUserMapper;
    @Resource
    CouponManualServiceImpl service;
    @Resource
    private EsShopCouponMapper escouponMapper;
    @Resource
    private EsShopCouponNewRuleMapper couponNewRuleMapper;
    @Resource
    private EsMemberMapper memberMapper;

    private Integer coupon=0;

    // 每1分钟执行一次调度任务 修改有效期
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduPutAwayTime(){
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tim = sdf.parse(sdf.format(new Date()));
            List<EsShopCoupon> coupontime = couponMapper.coupontime();
            for(EsShopCoupon entity:coupontime){
                //状态1有效，2未生效，3失效
                if (tim.before(entity.getExpiryBeginTime())) {
                    entity.setStatus(2);
                } else if (tim.after(entity.getExpiryBeginTime()) && tim.before(entity.getExpiryEndTime())) {
                    entity.setStatus(1);
                } else if (tim.after(entity.getExpiryEndTime())) {
                    entity.setStatus(3);
                }
                couponMapper.updateById(entity);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("定时任务出现异常："+e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public synchronized void schedulcouponTime() {
        EsMemberCoupon member = new EsMemberCoupon();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date da=new Date();
        List<EsShopCouponManual> sendObject2 = manualMapper.selectList(new QueryWrapper<EsShopCouponManual>().eq("activity_open", 2).eq("status",3));
       if(sendObject2!=null&&sendObject2.size()>0) {
           for (EsShopCouponManual manual2 : sendObject2){
               String substring = manual2.getTime().substring(0, 16);
               if (sdf.format(da).equals(substring)) {
                   manual2.setStatus(2);
                   manualMapper.updateById(manual2);
               }
           }
       }
        List<EsShopCouponManual> sendObject = manualMapper.selectList(new QueryWrapper<EsShopCouponManual>().eq("activity_open", 2).eq("status",2));
       if(sendObject!=null&&sendObject.size()>0) {
           for (EsShopCouponManual manual : sendObject) {
               String substring = manual.getTime().substring(0, 16);
               if (sdf.format(da).equals(substring)) {
                   if (manual.getSendObject() == 2) {
                       Timer time2=new Timer();
                       time2.schedule(new TimerTask() {
                           //Integer current=0;
                           List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", manual.getCouponManualid()));
                           @Override
                           public void run() {
                               System.out.println("路过指定------");
                               EsShopCoupon escoupon = escouponMapper.selectById(couponNewRules.get(0).getCouponid());
                               if(escoupon.getStock()==0){
                                   ShopCouponManual2(manual);
                                   time2.cancel();
                               }
                               if(escoupon.getStock()<manual.getStampsNumber()){
                                   ShopCouponManual2(manual);
                                   time2.cancel();
                               }
                               if(coupon==1){
                                   Integer namesum = couponMemberMapper.namesum(couponNewRules.get(0).getCouponName(),manual.getId());
                                   System.out.println("数量发券--"+namesum);
                                   EsShopCouponManual manuall = new EsShopCouponManual();
                                   manuall.setId(manual.getId());
                                   manuall.setStatus(4);
                                   manuall.setSendNumber(namesum);
                                   manualMapper.updateById(manuall);
                                   time2.cancel();
                               }
                               if(coupon==0){
                                   List<EsMember> memberselect =  memberMapper.memberselect(0,0);
                                  // current += 10;
                                 /*  if(memberselect.size()==0){
                                       System.out.println("用户已发完---");
                                       ShopCouponManual(manual);
                                       time2.cancel();
                                   }else{*/
                                       List<EsShopCouponManualUser> userList = manualUserMapper.selectList(new QueryWrapper<EsShopCouponManualUser>().eq("manual_id",manual.getId()).eq("status","1"));
                                       if (userList != null && userList.size() > 0) {
                                           for (EsShopCouponManualUser me : userList) {
                                               for (EsMember mes : memberselect) {
                                                   if (me.getUserId().contains(mes.getOpenid())) {
                                                       System.out.println("指定用户");
                                                       manualUserMapper.updateuser(me.getUserId(),me.getManualId());
                                                       service.addManualCoupon(member, sdf,manual, mes.getId(), mes.getOpenid());
                                                   }
                                               }
                                           }
                                       }else{
                                            ShopCouponManual(manual);
                                            time2.cancel();
                                       }
                                //   }
                               }

                           }
                       },1000,6000);

                   } else {
                       //status  1 已完成，2 发送中 3，未开始
                       //statusissue  1.发送 2.停止
                       Timer time2=new Timer();
                       time2.schedule(new TimerTask() {
                           boolean b=false;
                           Integer current=0;
                           List<EsShopCouponNewRule> couponNewRules = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", manual.getCouponManualid()));
                           @Override
                           public void run() {
                               System.out.println("路过------");
                               EsShopCoupon escoupon = escouponMapper.selectById(couponNewRules.get(0).getCouponid());
                               if(escoupon.getStock()==0){
                                   ShopCouponManual2(manual);
                                   time2.cancel();
                               }
                               if(escoupon.getStock()<manual.getStampsNumber()){
                                   ShopCouponManual2(manual);
                                   time2.cancel();
                               }
                               if(coupon==1){
                                   Integer namesum = couponMemberMapper.namesum(couponNewRules.get(0).getCouponName(),manual.getId());
                                   System.out.println("数量发券--"+namesum);
                                   EsShopCouponManual manuall = new EsShopCouponManual();
                                   manuall.setId(manual.getId());
                                   manuall.setStatus(4);
                                   manuall.setSendNumber(namesum);
                                   manualMapper.updateById(manuall);
                                   time2.cancel();
                               }
                               if (coupon == 0) {
                                   List<EsMember> memberselect =  memberMapper.memberselect(10,current);
                                   Integer sum = memberselect.size();
                                   current += 10;
                                   if(memberselect.size()==0){
                                       System.out.println("用户已发完---");
                                       ShopCouponManual(manual);
                                       time2.cancel();
                                   }else {
                                       for (EsMember me : memberselect) {
                                           if (sum < 10) {
                                               System.out.println("sumsdsadasd" + sum);
                                               b = true;
                                               break;
                                           } else {
                                               service.addManualCoupon(member, sdf, manual, me.getId(), me.getOpenid());
                                           }
                                       }

                                   if (b==true) {

                                       if (memberselect.size() < 0 && memberselect == null) {
                                           time2.cancel();
                                       } else {
                                           for (EsMember ms : memberselect) {
                                               EsShopCouponManual man = manualMapper.selectById(manual.getId());
                                               manual.setSendNumber(man.getSendNumber());
                                               service.addManualCoupon(member, sdf, manual, ms.getId(), ms.getOpenid());
                                           }
                                           ShopCouponManual(manual);
                                           time2.cancel();
                                       }
                                   }
                                   }
                               }
                           }
                       },1000,5000);

                   }
               }
           }
       }
    }

  public void ShopCouponManual(EsShopCouponManual manual){
      List<EsShopCouponNewRule> couponNewRules2 = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", manual.getCouponManualid()));
      Integer namesum = couponMemberMapper.namesum(couponNewRules2.get(0).getCouponName(),manual.getId());
      System.out.println("数量发券--"+namesum);
      EsShopCouponManual manuall = new EsShopCouponManual();
      manuall.setId(manual.getId());
      manuall.setStatus(1);
      manuall.setStatusissue(2);
      manuall.setSendNumber(namesum);
      manualMapper.updateById(manuall);
  }
    public void ShopCouponManual2(EsShopCouponManual manual){
        List<EsShopCouponNewRule> couponNewRules2 = couponNewRuleMapper.selectList(new QueryWrapper<EsShopCouponNewRule>().eq("public_couponid", manual.getCouponManualid()));
        Integer namesum = couponMemberMapper.namesum(couponNewRules2.get(0).getCouponName(),manual.getId());
        System.out.println("数量发券--"+namesum);
        EsShopCouponManual manuall = new EsShopCouponManual();
        manuall.setId(manual.getId());
        manuall.setStatus(4);
        manuall.setStatusissue(2);
        manuall.setSendNumber(namesum);
        manualMapper.updateById(manuall);
    }


























    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date da=new Date();
        String st=sdf.format(da);
        System.out.println(st);
       /* ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("0/5 * * * * *"));
        System.out.println(schedule);*/
    }
}
