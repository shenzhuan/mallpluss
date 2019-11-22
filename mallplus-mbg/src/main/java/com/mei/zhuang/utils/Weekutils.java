package com.mei.zhuang.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/29 08:49
 * @Description:
 */
public class Weekutils {
    /**
     *      * 获取当前日期是星期几<br>
     *      *
     *      * @param dt
     *      * @return 当前日期是星期几
     *     
     */

    public static int getWeekofDay(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            w = 0;
        } else {
            w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return w;
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(getWeekofDay(new Date()));
        String format = "yyyy-MM-dd";
        Date nowTime = new SimpleDateFormat(format).parse("2019-06-16");
        System.out.println(getWeekofDay(nowTime));
        Date startTime = new SimpleDateFormat(format).parse("09:27:00");
        Date endTime = new SimpleDateFormat(format).parse("09:27:59");
        System.out.println(isEffectiveDate(nowTime, startTime, endTime));
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author zhuan.shen
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


}
