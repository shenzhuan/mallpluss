package com.mei.zhuang.dao.member;

import com.arvato.ec.common.vo.data.customer.CustGroupIndexParam;
import com.arvato.ec.common.vo.data.trade.TradeAnalyzeParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.member.EsMember;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-04-15
 */
public interface EsMemberMapper extends BaseMapper<EsMember> {


    Integer selTotalMember(@PathParam("param") CustGroupIndexParam param);

    Integer membercount();

    EsMember selectmember(@Param("mobile") String mobile);

    Integer memberNumber(@Param("param") TradeAnalyzeParam param);

    //每次查十個用戶
    List<EsMember> memberselect(Integer param1, Integer param2);
}
