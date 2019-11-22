package com.mei.zhuang.service.order.impl;

import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.service.member.EsMemberService;
import com.mei.zhuang.service.order.MembersFegin;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberFeiginImpl implements MembersFegin {
    @Resource
    private EsMemberService memberService;

    @Override
    public EsMiniprogram getByShopId(Long shopId) {
        return memberService.getByShopId(shopId);
    }

    @Override
    public EsMember getMemberById(Long id) {
        return memberService.getById(id);
    }

    @Override
    public void updateMemberOrderById(EsMember member) {
        memberService.updateMemberOrderInfo();
    }

    @Override
    public Integer memberNumber(TradeAnalyzeParam param) {
        return memberService.memberNumber(param);
    }
}
