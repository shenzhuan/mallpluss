package com.mei.zhuang.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Auther: Tiger
 * @Date: 2019-07-04 17:07
 * @Description:
 */
public class DateCalendarUtils {

    /**
     *
     */
    /**
     * 获得指定的日期的那个月中的最后一天
     *
     * @param date
     * @param orgPattern
     * @return
     */
    public static String getLastDayByDate(String date, String orgPattern) {
        SimpleDateFormat f = new SimpleDateFormat(orgPattern, Locale.CHINA);
        Calendar c = Calendar.getInstance(Locale.CHINA);
        try {
            c.setTime(f.parse(date));
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f.format(c.getTime());
    }

    /**
     * 获得指定的日期的那个月中的第一天  error
     *
     * @param date
     * @param orgPattern
     * @return
     */
    public static String getFristDayByDate(String date, String orgPattern) {
        SimpleDateFormat f = new SimpleDateFormat(orgPattern, Locale.CHINA);
        Calendar c = Calendar.getInstance(Locale.CHINA);
        try {
            c.setTime(f.parse(date));
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));//第一天
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f.format(c.getTime());
    }

    /**
     * 传入的日期是否是传入的月份中的最后一天
     *
     * @param date
     * @param pattern
     * @return
     */
    public static boolean isLastDayByDate(String date, String pattern) {
        if (date == null) {
            return false;
        }
        return getLastDayByDate(date, pattern).equals(date);
    }

    /**
     * 传入的日期是否是传入的月份中的最后一天
     *
     * @param date
     * @param pattern
     * @return
     */
    public static boolean isFristDayByDate(String date, String pattern) {
        if (date == null) {
            return false;
        }
        return getFristDayByDate(date, pattern).equals(date);
    }


    /**
     * 判断传过来的日期是不是星期一 1
     *
     * @param startTime
     * @return
     * @throws Exception
     */
    public static boolean isMonday(String startTime) throws Exception {
        return dayForWeek(startTime) == 1;
    }

    /**
     * 判断传过来的日期是不是星期天 7
     *
     * @param startTime
     * @return
     * @throws Exception
     */
    public static boolean isSunday(String startTime) throws Exception {
        return dayForWeek(startTime) == 7;
    }

    /**
     * 判断当前日期是星期几   星期一至星期日 对应数字 （1 - 7)
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 判断传过来的时间是不是昨天（已今天（now）为参照）
     * 判断逻辑：传过来的时间 +1 与 现在时间是否相等
     * - @param time
     *
     * @return
     */
    public static boolean
    isYestaday(String time) throws Exception {

        String nowTime = DateUtils.addDay(DateUtils.toDate(time), 1);
        String sysNowTime = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        return sysNowTime.equals(nowTime);

    }

    /**
     * 判断目标时间 是不是 ：现在时间 还是 未来时间
     * true  不是  false：是现在时间|未来时间
     *
     * @param target
     * @param targetPattern
     * @return
     */
    public static boolean isNotNowTimeAndFutureTime(String target, String targetPattern) {
        if (target == null) return true;
        String nowDate = DateUtils.format(new Date(), targetPattern);
        if (target.equals(nowDate) || DateUtil.getDaySub(target, nowDate, targetPattern) > 1) {
            return false;
        }

        return true;
    }


    /**
     * 判断目标时间是不是未来时间 ： true： 是  false：不是
     *
     * @param target
     * @param targetPattern
     * @return
     */
    public static boolean isFutureTime(String target, String targetPattern) {
        if (target == null) return false;
        String nowDate = DateUtils.format(new Date(), targetPattern);
        if (DateUtil.getDaySub(target, nowDate, targetPattern) > 1) {
            return true;
        }
        return false;
    }

   /* private static boolean isRecentlyMonth(String startTime, String endTime){

        try{
            DateUtils.addDay(DateUtils.toDate(startTime),)
            boolean yesteDay = isYestaday(endTime);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

    /**
     * 获取某年某月的第一天
     *
     * @param date (参数格式指定yyyy-mm-dd)
     * @return
     */
    public static String getFisrtDayOfMonth(String date) {
        int year = 0;
        int month = 0;
        year = Integer.valueOf(date.substring(0, date.indexOf("-")));
        month = Integer.valueOf(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());

        return firstDayOfMonth;
    }


    /**
     * 获取某年某月的最后一天
     *
     * @param date (参数格式指定yyyy-mm-dd)
     * @return
     */
    public static String getLastDayOfMonth(String date) {
        int year = 0;
        int month = 0;

        year = Integer.valueOf(date.substring(0, date.indexOf("-")));
        month = Integer.valueOf(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }


    /**
     * 是否是日期中的所指月份的第一天 格式：必须yyyy-mm-dd
     *
     * @param date
     * @return
     */
    public static boolean isFristDayOfMonth(String date) {
        if (date == null) {
            return false;
        }
        String fisrtDayOfMonth = getFisrtDayOfMonth(date);

        if (date.equals(fisrtDayOfMonth)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是日期中的所指月份的第一天 格式：必须yyyy-mm-dd
     *
     * @param date
     * @return
     */
    public static boolean isLastDayOfMonth(String date) {
        if (date == null) {
            return false;
        }
        if (date.equals(getLastDayOfMonth(date))) {
            return true;
        }
        return false;
    }

    /**
     * @param date
     * @return
     */
    public static String handleDatePatter(String date) {

//        "2019-6-1";
        int startIndexOf = date.indexOf("-");
        String year = date.substring(0, startIndexOf);
        int endIndexOf = date.lastIndexOf("-");
        String month = date.substring(startIndexOf + 1, endIndexOf);
        if (month.length() != 2) {
            month = "0" + month;
        }

        String day = date.substring(endIndexOf + 1, date.length());
        if (day.length() != 2) {
            day = "0" + day;
        }
        System.out.println(year + month + day);
        return year + month + day;
    }


    public static void main(String[] args) throws Exception {
        int mon = dayForWeek("2019-07-07");
        System.out.println(mon);
        int sun = dayForWeek("2019-07-08");
        System.out.println(sun);

        String nowTime = DateUtil.format(DateUtils.addDay(DateUtils.toDate("2019-07-07"), 1), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD);
        System.out.println(DateUtils.addDay(DateUtils.toDate("2019-07-07"), 1));
        System.out.println(nowTime);

        boolean yestaday = DateCalendarUtils.isYestaday("2019-07-07");
        String sysNowTime = DateUtil.format(new Date(), DateUtil.YYYY_MM_DD);
        System.out.println(yestaday);

        System.out.println(DateCalendarUtils.isYestaday("2019-07-16"));


        boolean flag = DateCalendarUtils.isNotNowTimeAndFutureTime("2019-07-31", DateUtil.YYYY_MM_DD);
        System.out.println("is not now and future:  " + flag);


    }

}
