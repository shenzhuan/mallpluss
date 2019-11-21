package com.mei.zhuang.controller.marking;

import com.arvato.service.marking.api.feigin.MembersFegin;
import com.arvato.service.marking.api.orm.dao.*;
import com.arvato.service.marking.api.service.EsShopShareMemberService;
import com.arvato.service.marking.api.service.EsShopShareService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.util.ValidatorUtils;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.mei.zhuang.entity.marking.*;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.entity.order.EsMemberCoupon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/26 10:30
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/applet")
@Api(value = "小程序分享助力管理", description = "", tags = {"小程序分享助力管理"})
public class AppletShareController {

    @Resource
    private EsShopShareService esShopShareService;
    @Resource
    private EsShopShareMemberService esShopShareMemberService;
    @Resource
    private EsMemberCouponMapper esMemberCouponMapper;
    @Resource
    private EsShopCouponMapper esShopCouponMapper;
    @Resource
    private EsShopShareMapMapper esShopShareMapMapper;
    @Resource
    private EsShopShareMemberAssistanceMapper esShopShareMemberAssistanceMapper;
    @Resource
    private MembersFegin memberFegin;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");

    @ApiOperation("查询正在进行的分享助力活动")
    @PostMapping(value = "/selShareList")
    public Object preOrderMarking(@RequestParam("id") Long id,@RequestParam("memberId")Long memberId) {
        try {

            EsShopShare esShopShare = esShopShareService.selectById(id);
            if(esShopShare != null){
                //判断活动是否开启
                if(esShopShare.getStatus() == 1){
                    //判断是否在时间段内
                    int numEnd = esShopShare.getActivityendTime().compareTo(new Date());
                    if(numEnd == 1 ){
                        int numStart = esShopShare.getActivitystartTime().compareTo(new Date());
                        if(numStart != 1){
                            //判断是否在当前周期内
                            Integer timeNum =0;
                            String[] attr =esShopShare.getWeekday().split(",");
                            int timeInt = getWeekOfDate(new Date());
                            for (int i = 0; i < attr.length; i++) {
                                if(timeInt == Integer.parseInt(attr[i])){
                                    timeNum=1;
                                }
                            }
                            if(timeNum == 0){
                                return new CommonResult().failed("活动未开始");
                            }
                            //小时判断
                            if(esShopShare.getEndTime() != null&& !esShopShare.getEndTime().equals("") && esShopShare.getStartTime() != null && !esShopShare.getStartTime().equals("")){
                                long num1=sdfHMS.parse(esShopShare.getEndTime()).getTime();
                                long num2=sdfHMS.parse(sdfHMS.format(new Date())).getTime();
                                if(num1>num2) {
                                    numStart = esShopShare.getStartTime().compareTo(sdfHMS.format(new Date()));
                                }
                            }
                            if(numStart != 1){
                                    //判断用户是否有发起过活动
                                    EsShopShareMember shareMember = new EsShopShareMember();
                                    shareMember.setShareId(id);
                                    shareMember.setLaunchMemberId(memberId);
                                    EsShopShareMember esShopShareMember = esShopShareMemberService.selListOrderBy(shareMember);
                                    esShopShare.setMemberNumberCount(esShopShareMemberService.selectCount(new QueryWrapper<>(shareMember)));//用户发起次数
                                    if(esShopShareMember != null && !esShopShareMember.equals("") ){
                                        esShopShare.setShareMemberId(esShopShareMember.getId());
                                        EsShopShareMemberAssistance assistance = new EsShopShareMemberAssistance();
                                        assistance.setShareMemberId(esShopShareMember.getId());
                                        List<EsShopShareMemberAssistance> assistanceList = esShopShareMemberAssistanceMapper.selectList(new QueryWrapper<>(assistance));
                                        List<EsMember> list = new ArrayList<EsMember>();
                                        for (EsShopShareMemberAssistance ass:assistanceList) {
                                            EsMember member =memberFegin.detail(ass.getAssisMemberId());
                                            list.add(member);
                                        }
                                        esShopShare.setShareMemberId(esShopShareMember.getId());
                                        esShopShare.setMemberList(list);
                                        if(esShopShareMember.getIsClose() == 0){
                                            esShopShare.setMember(memberFegin.detail(memberId));
                                            esShopShare.setIsClose(0);//1任务完成 0没完成
                                            esShopShare.setIsConfirm(esShopShareMember.getIsConfirm());
                                            return new CommonResult().success("success",esShopShare);
                                        }
                                        if(esShopShareMember.getIsConfirm() ==1){
                                            esShopShare.setIsConfirm(1);
                                            esShopShare.setIsClose(1);//1任务完成 0没完成
                                            //是否可以发起下一轮
                                            shareMember = new EsShopShareMember();
                                            shareMember.setShareId(id);
                                            shareMember.setLaunchMemberId(memberId);
                                            List<EsShopShareMember> lists =esShopShareMemberService.selectList(new QueryWrapper<>(shareMember));
                                            //判断是否已达上限
                                            if(esShopShare.getMemberNumber()<=lists.size()){
                                                esShopShare.setCountNumber(3);
                                            }
                                            return new CommonResult().success("success",esShopShare);//提示发起者奖品图
                                        }
                                        esShopShare.setIsConfirm(0);
                                        esShopShare.setIsClose(1);//1任务完成 0没完成
                                        //是否可以发起下一轮
                                        shareMember = new EsShopShareMember();
                                        shareMember.setShareId(id);
                                        shareMember.setLaunchMemberId(memberId);
                                        List<EsShopShareMember> lists =esShopShareMemberService.selectList(new QueryWrapper<>(shareMember));
                                        //判断是否已达上限
                                        if(esShopShare.getMemberNumber()<=lists.size()){
                                            esShopShare.setCountNumber(3);
                                        }
                                        System.out.println("数据打印+"+esShopShare.getIsClose());
                                        return new CommonResult().success("success",esShopShare);
                                    }else{
                                        shareMember.setCreateTime(new Date());
                                        shareMember.setShareNum(0);
                                        shareMember.setIsClose(0);
                                        shareMember.setIsConfirm(1);
                                        esShopShareMemberService.insert(shareMember);
                                        esShopShare.setShareMemberId(shareMember.getId());
                                        esShopShare.setMember(memberFegin.detail(memberId));
                                        esShopShare.setIsClose(0);
                                        esShopShare.setIsConfirm(1);
                                        return new CommonResult().success("success",esShopShare);
                                    }
                                }
                        }
                    }
                    return new CommonResult().failed("活动未开始");
                }
                return new CommonResult().failed("活动未开始");
            }
            return new CommonResult().failed("活动不存在");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询正在进行的分享助力活动：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("分享即得")
    @PostMapping(value = "/shareAndGive")
    public Object shareAndGive(@RequestParam("shareMemberId") Long shareMemberId) {
        try{
            //分享次数+1
            EsShopShareMember shareMember =esShopShareMemberService.selectById(shareMemberId);
            EsShopShare share = esShopShareService.selectById(shareMember.getShareId());
            if(shareMember.getShareNum() >=share.getGiftNumber()){
                EsShopShareMember shareMember1 = new EsShopShareMember();
                shareMember1.setId(shareMemberId);
                shareMember1.setIsClose(1);
                esShopShareMemberService.updateById(shareMember1);
                return new CommonResult().failed("已达上限");
            }
            EsShopShareMember shopShareMember = new EsShopShareMember();
            shopShareMember.setId(shareMemberId);
            shopShareMember.setShareNum(shareMember.getShareNum()+1);
            esShopShareMemberService.updateById(shopShareMember);
            EsMemberCoupon coupon = new EsMemberCoupon();
            coupon.setTypeId(shareMemberId);
            Integer count =esMemberCouponMapper.selectCount(new QueryWrapper<>(coupon));
            EsShopShareMap shareMap = new EsShopShareMap();
            if(share.getGiftNumber() > count){
                //发券
                shareMap.setShareId(share.getId());
                shareMap.setWinningType(1);
                shareMap = esShopShareMapMapper.selectOne(shareMap);
                shareMap.setMemberId(shareMember.getLaunchMemberId());
                shareMap.setShareMemberId(shareMemberId);
                Boolean bool = giveCoupop(shareMap);
                if(bool == false){
                    //库存不足
                    return new CommonResult().failed(10,"助力成功,奖品已发完");
                }
                return new CommonResult().failed(4,shareMap);
            }
            return new CommonResult().success("success","分享成功");
        }catch (Exception e){
            log.error("分享即得：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("助力页面")
    @PostMapping(value = "/selShareDetail")
    public Object selShareDetail(@RequestParam("shareMemberId") Long shareMemberId) {
        try{
            EsShopShareMember shareMember =esShopShareMemberService.selectById(shareMemberId);
            EsShopShare share = esShopShareService.selectById(shareMember.getShareId());
            share.setShareMemberId(shareMemberId);
            return new CommonResult().success("success",share);
        }catch (Exception e){
            log.error("助力页面：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("确认发起者奖品已发放")
    @PostMapping(value = "/confirm")
    public Object confirm(@RequestParam("shareMemberId") Long shareMemberId) {
        try{
            if(ValidatorUtils.empty(shareMemberId)){
                return new CommonResult().failed("请指定用户发起活动id");
            }
            EsShopShareMember shareMember = esShopShareMemberService.selectById(shareMemberId);
            EsShopShare share =  esShopShareService.selectById(shareMember.getShareId());
            if(share.getHelpNumber().equals(shareMember.getShareNum())){
                //判断是否已发送奖品
                EsMemberCoupon coupon = new EsMemberCoupon();
                coupon.setMemberId(shareMember.getLaunchMemberId());
                coupon.setTypeId(shareMemberId);
                Integer countNum = esMemberCouponMapper.selectCount(new QueryWrapper<>(coupon));
                if(countNum == 0) {
                    EsShopShareMap shareMap = new EsShopShareMap();
                    //发送优惠卷
                    shareMap.setShareId(shareMember.getShareId());
                    shareMap.setWinningType(1);
                    shareMap = esShopShareMapMapper.selectOne(shareMap);
                    shareMap.setMemberId(shareMember.getLaunchMemberId());
                    shareMap.setShareMemberId(shareMemberId);
                    for (int i = 0; i < share.getGiftNumber(); i++) {
                        Boolean bool = giveCoupop(shareMap);
                        if(bool == false){
                            //库存不足
                            return new CommonResult().failed(10,"助力成功,奖品已发完");
                        }
                    }
                    return new CommonResult().failed(4, shareMap);
                }
            }


            shareMember = new EsShopShareMember();
            shareMember.setId(shareMemberId);
            shareMember.setIsConfirm(0);
            return new CommonResult().success("success",esShopShareMemberService.updateById(shareMember));
        }catch (Exception e){
            log.error("确认发起者奖品已发放：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("发起新一轮助力")
    @PostMapping(value = "/startAssis")
    public Object startAssis(@RequestParam("shareId") Long shareId,@RequestParam("memberId")Long memberId) {
        try{
            if(ValidatorUtils.empty(shareId)){
                return new CommonResult().failed("请指定活动id");
            }
            if(ValidatorUtils.empty(memberId)){
                return new CommonResult().failed("请指定用户id");
            }
            EsShopShareMember shareMember = new EsShopShareMember();
            shareMember.setShareId(shareId);
            shareMember.setLaunchMemberId(memberId);
            List<EsShopShareMember> list =esShopShareMemberService.selectList(new QueryWrapper<>(shareMember));
            EsShopShare esShopShare = esShopShareService.selectById(shareId);
            //判断是否已达上限
            if(esShopShare.getMemberNumber()<=list.size()){
                return new CommonResult().failed("发起助力次数已达上限");
            }
            //判断是否有正在进行的助力活动
            for (EsShopShareMember member :list) {
                if(member.getIsClose() == 0){
                    return new CommonResult().failed("已有正在进行的助力活动");
                }
            }
            shareMember.setCreateTime(new Date());
            shareMember.setIsClose(0);
            shareMember.setShareNum(0);
            shareMember.setIsConfirm(1);
            esShopShareMemberService.insert(shareMember);
            return new CommonResult().success("success",shareMember);
        }catch (Exception e){
            log.error("发起新一轮助力：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @ApiOperation("好友助力")
    @PostMapping(value = "/helperSetUp")
    public Object helperSetUp(@RequestParam("shareMemberId") Long shareMemberId,@RequestParam("memberId")Long memberId) {
        try{

            EsShopShareMember shareMember =esShopShareMemberService.selectById(shareMemberId);
            EsShopShare share = esShopShareService.selectById(shareMember.getShareId());
            if(shareMember.getLaunchMemberId().equals(memberId)){
                return new CommonResult().failed("不能给自己助力");
            }

            EsShopShareMemberAssistance assistance = new EsShopShareMemberAssistance();
            assistance.setAssisMemberId(memberId);
            Integer count =esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));

            //1.助力位是否已满
            EsShopShareMember shareMember1 = esShopShareMemberService.selectById(shareMemberId);
            if(share.getHelpNumber().equals(shareMember1.getShareNum())){
                return new CommonResult().failed(5,"好友助力位已满");
            }

            //2.最多重复助力次数是否已满
            shareMember1 = new EsShopShareMember();
            shareMember1.setLaunchMemberId(shareMember.getLaunchMemberId());
            List<EsShopShareMember> shareMemberList = esShopShareMemberService.selectList(new QueryWrapper<>(shareMember1));
            Integer countNum=0;
            for (EsShopShareMember esShopShareMember:shareMemberList) {
                assistance = new EsShopShareMemberAssistance();
                assistance.setAssisMemberId(memberId);
                assistance.setShareMemberId(esShopShareMember.getId());
                countNum += esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));
            }
            if(share.getRepeatNumber() <= countNum){
                //最多重复助力次数
                EsShopShare shopShare = new EsShopShare();
                shopShare.setMemberTotalAssis(count);//前用户活动总助力次数
                assistance = new EsShopShareMemberAssistance();
                assistance.setAssisMemberId(memberId);
                assistance.setShareMemberId(shareMember.getId());
                shopShare.setMemberThisAssis(countNum);//当前用户发起活动助力次数
                count =esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));

                shopShare.setMostPrize(share.getMostPrize());//最多获取次奖品
                EsMemberCoupon coupon = new EsMemberCoupon();
                coupon.setMemberId(memberId);
                shopShare.setThisPrize(esMemberCouponMapper.selectCounts(coupon));//已获得奖品件数
                shopShare.setTotalPower(share.getTotalPower());//最大助力次数
                shopShare.setRepeatNumber(share.getRepeatNumber());//最多重复助力次数
                return new CommonResult().failed(9,shopShare);
            }

            //3.最大助力次数
            if(share.getTotalPower() != 0 && share.getTotalPower() <= count){
                EsShopShare shopShare = new EsShopShare();
                shopShare.setMemberTotalAssis(count);//前用户活动总助力次数
                assistance = new EsShopShareMemberAssistance();
                assistance.setAssisMemberId(memberId);
                assistance.setShareMemberId(shareMember.getId());
                shopShare.setMemberThisAssis(count);//当前用户发起活动助力次数
                count =esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));
                shopShare.setMostPrize(share.getMostPrize());//最多获取次奖品
                shopShare.setTotalPower(share.getTotalPower());//最大助力次数
                shopShare.setRepeatNumber(share.getRepeatNumber());//最多重复助力次数
                EsMemberCoupon coupon = new EsMemberCoupon();
                coupon.setMemberId(memberId);
                shopShare.setThisPrize(esMemberCouponMapper.selectCounts(coupon));//已获得奖品件数
                return new CommonResult().failed(9,shopShare);
            }else{
                assistance = new EsShopShareMemberAssistance();
                assistance.setAssisMemberId(memberId);
                assistance.setShareMemberId(shareMember.getId());
                count = esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));
            }


            EsShopShareMember shopShareMember = new EsShopShareMember();
            //判断助力次数是否已满
            if(share.getHelpNumber()>shareMember.getShareNum()){
                assistance = new EsShopShareMemberAssistance();
                assistance.setAssisMemberId(memberId);
                count = esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));
                if(count == 0){//判断助力者是否助力过
                    EsShopShareMemberAssistance assistance1 = new EsShopShareMemberAssistance();
                    assistance1.setShareMemberId(shareMemberId);
                    assistance1.setAssisMemberId(memberId);
                    esShopShareMemberAssistanceMapper.insert(assistance1);//助力一次

                    shopShareMember = new EsShopShareMember();
                    shopShareMember.setId(shareMemberId);
                    shopShareMember.setShareNum(shareMember.getShareNum()+1);
                    esShopShareMemberService.updateById(shopShareMember);//助力次数+1

                    //判断助力次数是否已满
                    if(share.getHelpNumber().equals(shopShareMember.getShareNum())){
                        shopShareMember = new EsShopShareMember();
                        shopShareMember.setId(shareMemberId);
                        shopShareMember.setIsClose(1);
                        esShopShareMemberService.updateById(shopShareMember);
                    }

                    //判断助力是否有奖
                    if(share.getIsPrize() == 1){
                        //获取用户累计助力次数
                        assistance = new EsShopShareMemberAssistance();
                        assistance.setAssisMemberId(memberId);
                        count = esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));
                        EsMemberCoupon coupon = new EsMemberCoupon();
                        coupon.setMemberId(memberId);
                        //判断奖品是否已达上限
                        EsMemberCoupon coupon1 = new EsMemberCoupon();
                        coupon1.setMemberId(memberId);
                        Integer countMemberCoupon =esMemberCouponMapper.selectCounts(coupon1);
                        if(share.getMostPrize() > countMemberCoupon){

                            if(share.getPowerNumber()==1 || share.getPowerNumber()%count == 0){
                                //发放奖品
                                EsShopShareMap shareMap = new EsShopShareMap();
                                shareMap.setShareId(share.getId());
                                shareMap.setWinningType(2);
                                shareMap = esShopShareMapMapper.selectOne(shareMap);
                                shareMap.setMemberId(memberId);
                                shareMap.setShareMemberId(shareMemberId);
                                for (int i = 0; i <share.getPowerGiven() ; i++) {
                                    Boolean bool = giveCoupop(shareMap);
                                    if(bool == false){
                                        //库存不足
                                        return new CommonResult().failed(10,"助力成功,奖品已发完");
                                    }
                                }
                                return new CommonResult().failed(4,shareMap);
                            }else{
                                return new CommonResult().failed(1,"助力成功");
                            }
                        }else{
                            return new CommonResult().failed(1,"助力成功");
                        }
                    }else{
                        return new CommonResult().failed(1,"助力成功");
                    }

                }else{
                    //助力者重复助力 1,可以 2,不可以
                    if(share.getIsHelp().equals("1")){
                        //判断是否满足最多重复助力次数
                        assistance = new EsShopShareMemberAssistance();
                        assistance.setAssisMemberId(memberId);
                        assistance.setShareMemberId(shareMemberId);
                        count =esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));

                        if(share.getRepeatNumber()> countNum){
                            EsShopShareMemberAssistance assistance1 = new EsShopShareMemberAssistance();
                            assistance1.setShareMemberId(shareMemberId);
                            assistance1.setAssisMemberId(memberId);
                            esShopShareMemberAssistanceMapper.insert(assistance1);//助力一次

                            shopShareMember = new EsShopShareMember();
                            shopShareMember.setId(shareMemberId);
                            shopShareMember.setShareNum(shareMember.getShareNum()+1);
                            esShopShareMemberService.updateById(shopShareMember);//助力次数+1

                            //判断助力次数是否已满
                            if(share.getHelpNumber().equals(shopShareMember.getShareNum())){
                                shopShareMember = new EsShopShareMember();
                                shopShareMember.setId(shareMemberId);
                                shopShareMember.setIsClose(1);
                                esShopShareMemberService.updateById(shopShareMember);
                            }

                            //判断助力是否有奖
                            if(share.getIsPrize() == 1){
                                //获取用户累计助力次数
                                assistance = new EsShopShareMemberAssistance();
                                assistance.setAssisMemberId(memberId);
                                count = esShopShareMemberAssistanceMapper.selectCount(new QueryWrapper<>(assistance));
                                //判断奖品是否已达上限
                                EsMemberCoupon coupon1 = new EsMemberCoupon();
                                coupon1.setMemberId(memberId);
                                Integer countMemberCoupon =esMemberCouponMapper.selectCounts(coupon1);
                                if(share.getMostPrize() > countMemberCoupon){
                                    if(share.getPowerNumber()==1 || share.getPowerNumber()%count == 0){
                                        //发放奖品
                                        EsShopShareMap shareMap = new EsShopShareMap();
                                        shareMap.setShareId(share.getId());
                                        shareMap.setWinningType(2);
                                        shareMap = esShopShareMapMapper.selectOne(shareMap);
                                        shareMap.setMemberId(memberId);
                                        shareMap.setShareMemberId(shareMemberId);
                                        for (int i = 0; i <share.getPowerGiven() ; i++) {
                                            Boolean bool = giveCoupop(shareMap);
                                            if(bool == false){
                                                //库存不足
                                                return new CommonResult().failed(10,"助力成功,奖品已发完");
                                            }
                                        }
                                        return new CommonResult().failed(4,shareMap);
                                    }else{
                                        return new CommonResult().failed(1,"助力成功");
                                    }
                                }else{
                                    return new CommonResult().failed(1,"助力成功");
                                }
                            }else{

                                return new CommonResult().failed(1,"助力成功");
                            }
                        }else{
                            //判断好友助力位是否已满
                            if(share.getHelpNumber().equals(shopShareMember.getShareNum())){
                                return new CommonResult().failed(5,"好友助力位已满");
                            }else{
                                return new CommonResult().failed(5,"好友重复助力次数已满");
                            }
                        }
                    }else{
                        return new CommonResult().failed(5,"无法助力");
                    }
                }

            }else{
                return new CommonResult().failed(5,"好友助力位已满");
            }


        }catch (Exception e){
            log.error("好友助力：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    //发放优惠卷
    public Boolean giveCoupop(EsShopShareMap entity){
        //已发放奖品数量
        EsMemberCoupon esMemberCoupon = new EsMemberCoupon();
        esMemberCoupon.setMemberId(entity.getMemberId());
        esMemberCoupon.setCouponId(entity.getWinningId());
        EsShopCoupon coupon = esShopCouponMapper .selectById(entity.getWinningId());

        esMemberCoupon.setFroms(6);
        esMemberCoupon.setCreateTime(sdf.format(new Date()));
        esMemberCoupon.setCouponId(coupon.getId());
        esMemberCoupon.setTitle(coupon.getCouponsName());
        esMemberCoupon.setStartTime(coupon.getExpiryBeginTime());
        esMemberCoupon.setEndTime(coupon.getExpiryEndTime());
        //有效期 1固定，2当前，3次日
        Date createTime = new Date();
        if (coupon.getAmount() == 2) {
            esMemberCoupon.setStartTime(createTime);
            esMemberCoupon.setEndTime(addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
        }
        if (coupon.getAmount() == 3) {
            esMemberCoupon.setStartTime(addDays(createTime, 1));
            esMemberCoupon.setEndTime(addDays(createTime, Integer.parseInt(coupon.getTimeInterval())));
        }
        //用户id
        esMemberCoupon.setMemberId(entity.getMemberId());
        esMemberCoupon.setConditions(1);
        //金额或折扣
        esMemberCoupon.setAmount(coupon.getEnough());
        esMemberCoupon.setShopId((long) 1);
        esMemberCoupon.setType(coupon.getType());
        //有效期状态
        esMemberCoupon.setEffective(coupon.getStatus());
        //券说明
        esMemberCoupon.setDescription("分享助力优惠卷：" + coupon.getId() + "，推送节点" );
        esMemberCoupon.setStatus(1);
        esMemberCoupon.setTypeId(entity.getShareMemberId());
        esMemberCouponMapper.insert(esMemberCoupon);
        //判断库存是否充足
        Boolean bool;
        EsShopCoupon shopCoupon = esShopCouponMapper.selectById(coupon.getId());
        if(shopCoupon.getStock() > 0){
            EsShopCoupon esShopCoupon = new EsShopCoupon();
            esShopCoupon.setId(coupon.getId());
            esShopCoupon.setStock(coupon.getStock()-1);
            esShopCouponMapper.updateById(esShopCoupon);
            bool=true;
        }else{
            bool=false;
        }

        return bool;
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
    public static int getWeekOfDate(Date dt) {
        int[] weekDays = {0, 1, 2, 3, 4, 5, 6};//周日到周六
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

}
