package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.service.sys.biz.CalendarBiz;
import com.mei.zhuang.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.*;

@Api(description = "CRUD-日历控制层", tags = {"日历控件信息"})
@ApiIgnore
@RestController
@RequestMapping("calendar")
public class CalendarController extends BaseController {

    @Resource
    private CalendarBiz calendarBiz;

    @SysLog(MODULE = "CRUD-日历控制层", REMARK = "获取日期数据")
    @ApiOperation("获取日期数据")
    @GetMapping(value = "/data")
    public JSONObject data(@ApiParam("状态") String status) {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> calendarData = calendarBiz.getCalendarData(status);
        result.put("month", calendarData);
        result.put("week", convertCampaign(calendarData));
        return result;
    }

    @SysLog(MODULE = "CRUD-日历控制层", REMARK = "获取当前状态颜色")
    @ApiOperation("获取当前状态颜色")
    @GetMapping(value = "/status/color")
    public JSONObject getStatusColor() {
        JSONObject result = new JSONObject();
        result.put("color", calendarBiz.getStatusColor());
        return result;
    }

    private List<Map<String, Object>> convertCampaign(List<Map<String, Object>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            for (Map<String, Object> map2 : list) {
                int id = (int) map2.get("id");
                String title = Objects.toString(map2.get("title"), "");
                String stime = Objects.toString(map2.get("stime"), "");
                String color = Objects.toString(map2.get("color"), "");
                String url = Objects.toString(map2.get("url"), "");
                Date start = (Date) map2.get("start");
                Date end = (Date) map2.get("end");
                String start1 = DateUtil.format(start, DateUtil.YYYY_MM_DD) + " " + stime;
                String end1 = DateUtil.format(end, DateUtil.YYYY_MM_DD) + " " + stime;
                Date startdate = DateUtil.parse(start1, DateUtil.YYYY_MM_DD_HH_MM);
                Date enddate = DateUtil.parse(end1, DateUtil.YYYY_MM_DD_HH_MM);
                List<Date> dateList = DateUtil.getDateList(startdate, enddate);
                for (Date date : dateList) {
                    Map<String, Object> m = new HashMap<>();
                    int hour = DateUtil.getHour(date);
                    Date setHour = DateUtil.setHour(hour + 2, date);
                    String start2 = DateUtil.format(date, DateUtil.YYYY_MM_DD_HH_MM);
                    String end2 = DateUtil.format(setHour, DateUtil.YYYY_MM_DD_HH_MM);
                    m.put("id", id);
                    m.put("title", title);
                    m.put("color", color);
                    m.put("url", url);
                    m.put("start", start2.replace(" ", "T") + "Z");
                    m.put("end", end2.replace(" ", "T") + "Z");
                    result.add(m);
                }
            }
        } catch (Exception e) {

        }
        return result;
    }
}
