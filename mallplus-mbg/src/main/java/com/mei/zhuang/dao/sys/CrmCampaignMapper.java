package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmCampaign;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2017-10-11
 */
public interface CrmCampaignMapper extends BaseMapper<CrmCampaign> {

	/*@Update("update public.crm_campaign set campaign_name='11' where campaign_id=581")
    Integer updateById();*/


    List<Map<String, Object>> getCalendarData(@Param("statusList") List<Integer> statusList);
}
