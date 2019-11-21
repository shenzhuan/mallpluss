package com.mei.zhuang.service.member;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.LoginVo;
import com.mei.zhuang.vo.data.customer.CustGroupIndexParam;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;

import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/4/13 06:49
 * @Description:
 */
public interface EsMemberService extends IService<EsMember> {
    Object loginByWeixin(LoginVo entity);

    EsMiniprogram getByShopId(Long shopId);

    Object bindPhone(String phone, String code, Long memberId);

    Object generateCode(String phone);

    EsMember selectmember(String mobile);

    boolean updateMemberOrderInfo();

    Integer selTotalMember(CustGroupIndexParam param);

    Integer membercount();

    Integer memberNumber(TradeAnalyzeParam param);

    List<EsMember> memberselect(Integer param1, Integer param2);

}
