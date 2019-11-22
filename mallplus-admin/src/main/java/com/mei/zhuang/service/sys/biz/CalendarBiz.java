package com.mei.zhuang.service.sys.biz;

import com.mei.zhuang.dao.sys.CrmCampaignMapper;
import com.mei.zhuang.dao.sys.CrmSysStatusColorMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CalendarBiz {

    @Resource
    private CrmCampaignMapper crmCampaignMapper;

    @Resource
    private CrmSysStatusColorMapper crmSysStatusColorMapper;

    /**
     * 获取日程数据
     *
     * @return 活动数据
     */
    public List<Map<String, Object>> getCalendarData(String status) {
        List<Integer> statusResult = new ArrayList<>();
        if (StringUtils.isNotEmpty(status)) {
            String[] result = status.split(",");
            List<String> statusList = Arrays.asList(result);
            statusList.forEach((String item) -> statusResult.add(Integer.valueOf(item)));
        }
        return crmCampaignMapper.getCalendarData(statusResult);
    }

    public List<Map<String, Object>> getStatusColor() {
        return crmSysStatusColorMapper.getStatusColor();
    }
}
