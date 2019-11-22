package com.mei.zhuang.service.order;

import com.mei.zhuang.entity.member.EsMember;
import com.mei.zhuang.vo.EsMiniprogram;
import com.mei.zhuang.vo.data.trade.TradeAnalyzeParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:会员服务接口
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2018/11/26
 */

public interface MembersFegin {

    @PostMapping(value = "/api/member/miniprogram", params = "shopId")
    EsMiniprogram getByShopId(@RequestParam("shopId") Long shopId);

    @PostMapping(value = "/applet/getMemberById", params = "id")
    EsMember getMemberById(@RequestParam("id") Long id);

    @PostMapping(value = "/applet/updateMemberOrderById")
    void updateMemberOrderById(@RequestBody EsMember member);

    @PostMapping(value = "/api/member/memberNumber")
     Integer memberNumber(@RequestBody TradeAnalyzeParam param);


}
