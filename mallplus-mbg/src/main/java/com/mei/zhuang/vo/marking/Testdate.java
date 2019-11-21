package com.mei.zhuang.vo.marking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Testdate {
    //根据日期取得星期几
    public static String getWeek(Date date){
        String[] weeks = {"7","1","2","3","4","5","6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }


    public  List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
// 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
// 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
// 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }
/*
   // b、使用SimpleDateFormat类
    //根据日期取得星期几
    public static String getWeek2(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }*/






    public static void main(String[] args) throws Exception {


            //当前时间++++天数
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
      /*  String da="2019-6-4";
        System.out.println("当前日期:"+sf.format(c.getTime()));
        System.out.println(getWeek(sf.parse(da)));*/
      /*  c.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println("增加一天后日期:"+sf.format(c.getTime()));*/
        /*c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);*/
        //System.out.println(sf.format(c.getTime()));
     /*   Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        System.out.println(sf.format(c.getTime()));
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(ss));
        System.out.println(sf.format(c.getTime()));*/


    }

}
