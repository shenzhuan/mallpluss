/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mei.zhuang.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class DateUtil {

	public final static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public final static String YYYYMMDDHHMM = "yyyyMMddHHmm";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYYMMDD = "yyyyMMdd";
	public final static String YYYY_MM = "yyyy-MM";
	public final static String YYYYMM = "yyyyMM";
	public final static String YYYY = "yyyy";
	public final static String MONTH = "MM";
	public final static String DAY = "dd";
	public final static String HH_MM_SS = "HH:mm:ss";
	public final static String HHMMSS = "HHmmss";
	public final static String HH_MM = "HH:mm";
	public final static String HHMM = "HHmm";
	public final static String HH = "HH";
	public final static String HOUR_24 = "HH";
	public final static String MIMUTE = "mm";
	public final static String SECOND = "ss";
	public final static String MILLISECOND = "SS";

	public final static SimpleDateFormat sdfYYYY_MM_DD_HH_MM_SS_SSS = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS);
	public final static SimpleDateFormat sdfYYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
	public final static SimpleDateFormat sdfYYYYMMDDHHMMSS = new SimpleDateFormat(YYYYMMDDHHMMSS);
	public final static SimpleDateFormat sdfYYYY_MM_DD_HH_MM = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
	public final static SimpleDateFormat sdfYYYYMMDDHHMM = new SimpleDateFormat(YYYYMMDDHHMM);
	public final static SimpleDateFormat sdfYYYY_MM_DD = new SimpleDateFormat(YYYY_MM_DD);
	public final static SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat(YYYYMMDD);
	public final static SimpleDateFormat sdfYYYY_MM = new SimpleDateFormat(YYYY_MM);
	public final static SimpleDateFormat sdfYYYYMM = new SimpleDateFormat(YYYYMM);
	public final static SimpleDateFormat sdfYYYY = new SimpleDateFormat(YYYY);
	public final static SimpleDateFormat sdfHH_MM_SS = new SimpleDateFormat(HH_MM_SS);
	public final static SimpleDateFormat sdfHHMMSS = new SimpleDateFormat(HHMMSS);
	public final static SimpleDateFormat sdfHH_MM = new SimpleDateFormat(HH_MM);
	public final static SimpleDateFormat sdfHHMM = new SimpleDateFormat(HHMM);
	public final static SimpleDateFormat sdfHH = new SimpleDateFormat(HH);

	/*************************************获取时间字符串格式*****************************************/
	/**
	 * 获取当前时间yyyy-MM-dd HH:mm:ss.SSS 格式
	 * @return
	 */
	public static String getYYYY_MM_DD_HH_MM_SS_SSSFormat() {
		return sdfYYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
	}
	public static String getYYYY_MM_DD_HH_MM_SS_SSSFormat(Date date) {
		return sdfYYYY_MM_DD_HH_MM_SS_SSS.format(date);
	}

	/**
	 * 获取当前时间yyyy-MM-dd HH:mm:ss 格式
	 * @return
	 */
	public static String getYYYY_MM_DD_HH_MM_SSFormat() {
		return sdfYYYY_MM_DD_HH_MM_SS.format(new Date());
	}
	public static String getYYYY_MM_DD_HH_MM_SSFormat(Date date) {
		return sdfYYYY_MM_DD_HH_MM_SS.format(date);
	}


	/**
	 * 获取当前时间YYYYMMDDHHmmss格式
	 *
	 * @return
	 */
	public static String getYYYYMMDDHHMMSSFormat() {
		return sdfYYYYMMDDHHMMSS.format(new Date());
	}
	public static Date getYYYYMMDDHHMMSSFormat(String dateAndTime) {
		try{
			return sdfYYYYMMDDHHMMSS.parse(dateAndTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm 格式
	 * @return
	 */
	public static String getYYYY_MM_DD_HH_MMFormat() {
		return sdfYYYY_MM_DD_HH_MM.format(new Date());
	}
	public static String getYYYY_MM_DD_HH_MMFormat(Date date) {
		return sdfYYYY_MM_DD_HH_MM.format(date);
	}

	/**
	 * 获取当前时间yyyyMMddHHmm 格式
	 * @return
	 */
	public static String getYYYYMMDDHHMMFormat() {
		return sdfYYYYMMDDHHMM.format(new Date());
	}
	public static String getYYYYMMDDHHMMFormat(Date date) {
		return sdfYYYYMMDDHHMM.format(date);
	}


	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getYYYY_MM_DDFormat() {
		return sdfYYYY_MM_DD.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getYYYY_MM_DDFormat(Date date) {
		return sdfYYYY_MM_DD.format(date);
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getYYYYMMDDFormat() {
		return sdfYYYYMMDD.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return
	 */
	public static String getYYYYMMDDFormat(Date date) {
		return sdfYYYYMMDD.format(date);
	}

	/**
	 * 获取YYYY-MM格式
	 * @return
	 */
	public static String getYYYY_MMFormat() {
		return sdfYYYY_MM.format(new Date());
	}
	public static String getYYYY_MMFormat(Date date) {
		return sdfYYYY_MM.format(date);
	}

	/**
	 * 获取YYYYMM格式
	 * @return
	 */
	public static String getYYYYMMFormat(Date date) {
		return sdfYYYYMM.format(date);
	}
	public static String getYYYYMMFormat() {
		return sdfYYYYMM.format(new Date());
	}

	/**
	 * 获取当前时间YYYY格式
	 * @return
	 */
	public static String getYYYYFormat() {
		return sdfYYYY.format(new Date());
	}
	public static String getYYYYFormat(Date date) {
		return sdfYYYY.format(date);
	}


	/**
	 * 获取HH:MM:SS格式
	 * @return
	 */
	public static String getHH_MM_SSFormat() {
		return sdfHH_MM_SS.format(new Date());
	}
	public static String getHH_MM_SSFormat(Date date) {
		return sdfHH_MM_SS.format(date);
	}

	/**
	 * 获取HHMMSS格式
	 * @return
	 */
	public static String getHHMMSSFormat() {
		return sdfHHMMSS.format(new Date());
	}
	public static String getHHMMSSFormat(Date date) {
		return sdfHHMMSS.format(date);
	}

	public static String getHH_MMFormat() {
		return sdfHH_MM.format(new Date());
	}
	public static String getHH_MMFormat(Date date) {
		return sdfHH_MM.format(date);
	}

	public static String getHHMMFormat() {
		return sdfHHMM.format(new Date());
	}
	public static String getHHMMFormat(Date date) {
		return sdfHHMM.format(date);
	}

	public static String getHHFormat() {
		return sdfHH.format(new Date());
	}
	public static String getHHFormat(Date date) {
		return sdfHH.format(date);
	}

	/**
	 * 根据格式获取当前时间
	 * @param pattern:时间格式
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * 获取当前时间戳,当前多少秒
	 */
	public static Long getTimeStamp() {
		return new Date().getTime() / 1000l;
	}
	public static Long getTimeStamp(Date date) {
		return date.getTime() / 1000l;
	}

    /*************************************获取时间字符串格式end*****************************************/

	/**
	 * 根据 pattern 格式 将字符串转化成 日期Date
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		if (pattern==null){
			pattern=YYYY_MM_DD_HH_MM_SS;
		}
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据 pattern 格式 将Date 转化成 pattern 格式的字符串
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if(date==null){
			return StringUtils.EMPTY;
		}
		DateFormat fmt = new SimpleDateFormat(pattern);
		return fmt.format(date);
	}


	/**
	 * 将日期字符串原格式转成目标格式
	 * @param date
	 * @param orgPattern:日期原来格式
	 * @param discPattern:日期目标格式
	 * @return 返回目标日期格式字符串
	 */
	public static String format(String date,String orgPattern,String discPattern){
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		try {
			Date fmtDate = parse(date,orgPattern);
			return format(fmtDate,discPattern);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 把日期转换为Timestamp
	 * @param date
	 * @return
	 */
	public static Timestamp formatTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}


	/**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	/**
	 * @Description:(日期比较,变换格式为整日期，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 */
	public static boolean compareDate(String s, String e,String pattern) {
		if (StringUtils.isAnyEmpty(s,e)) {
			return false;
		}
		return getTimeStamp(parse(s,pattern))  >= getTimeStamp(parse(e,pattern));
	}

	/**
	 * (日期比较,变换格式为整日期，如果s>=e 返回true 否则返回false)
	 * @param s:被减数
	 * @param sPattern：被减数日期格式
	 * @param e:减数
	 * @param ePatterm:减数日期格式
	 * @return
	 */
	public static boolean compareDate(String s,String sPattern, String e,String ePatterm) {
		if (StringUtils.isAnyEmpty(s,e)) {
			return false;
		}
		return getTimeStamp(parse(s,sPattern))  >= getTimeStamp(parse(e,ePatterm));
	}

	/**
	 * 时间相减得到年
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param pattern 日期格式
	 * @return
	 */
	public static int getYearSub(String startTime, String endTime,String pattern) {
		try {
			return getYearSub(parse(startTime,pattern),parse(endTime,pattern));
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * 时间相减得到年
	 * @param startTime 开始时间
	 * @param startTimePattern 开始时间日期格式
	 * @param endTime 结束时间
	 * @param endTimePattern 结束时间日期格式
	 * @return
	 */
	public static int getYearSub(String startTime,String startTimePattern, String endTime,String endTimePattern) {
		try {
			return getYearSub(parse(startTime,endTimePattern),parse(endTime,startTimePattern));
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * 时间相减得到年
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public static int getYearSub(Date startTime, Date endTime) {
		try {
			int years = (int) (((startTime.getTime() - endTime.getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * 时间相减得到天数
	 * @param beginDateStr：开始时间
	 * @param endDateStr：结束时间
	 * @param pattern :日期格式
	 * @return long
	 */
	public static long getDaySub(String beginDateStr, String endDateStr,String pattern) {
		long day = 0;
		Date beginDate = parse(beginDateStr,pattern);
		Date endDate = parse(endDateStr,pattern);
		return getDaySub(endDate,beginDate);
	}

	/**
	 * 时间相减得到天数
	 * @param beginDateStr：开始时间
	 * @param beginDatePattern：开始时间格式
	 * @param endDateStr：结束时间
	 * @param endDatePattern：结束时间格式
	 * @return
	 */
	public static long getDaySub(String beginDateStr,String beginDatePattern, String endDateStr,String endDatePattern) {
		long day = 0;
		Date beginDate = parse(beginDateStr,beginDatePattern);
		Date endDate = parse(endDateStr,endDatePattern);
		return day = getDaySub(endDate,beginDate);
	}

	/**
	 * 日期相差天
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long getDaySub(Date beginDate,Date endDate) {
		long day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
		return day;
	}

	/**
	 * 得到n天之后的日期
	 * @param days
	 * @return
	 */
	public static String getDateAfterDay(Integer days,String pattern) {
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();
		String dateStr = format(date,pattern);
		return dateStr;
	}
	/**
	 * 得到n天之后的日期
	 * @param days
	 * @return
	 */
	public static String getDateAfterDay(String days,String pattern) {
		int daysInt = Integer.parseInt(days);
		return getDateAfterDay(daysInt,pattern);
	}
	/**
	 * 得到n天之后的日期
	 * @param time : 时间
	 * @param days
	 * @param pattern
	 * @return
	 */
	public static String getDateAfterDay(String time,Integer days,String pattern) {
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.setTime(parse(time,pattern));
		canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();
		String dateStr = format(date,pattern);
		return dateStr;
	}

	/**
	 * 得到n天之后的日期
	 * @param time : 时间
	 * @param days
	 * @param timePatterm：开始的日期格式
	 * @param retrunPattern : 返回的日期格式
	 * @return
	 */
	public static String getDateAfterDay(String time,String timePatterm,Integer days,String retrunPattern) {
		Calendar canlendar = Calendar.getInstance();
		canlendar.setTime(parse(time,timePatterm));
		canlendar.add(Calendar.DATE, days);
		Date date = canlendar.getTime();
		String dateStr = format(date,retrunPattern);
		return dateStr;
	}

	public static Date getDateAfterDay(Date d, Integer days) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.add(Calendar.DATE,days);
		return now.getTime();
	}

	public static Date getDateBeforeDay(Date d, Integer day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.add(Calendar.DATE,-day);
		return now.getTime();
	}

	/**
	 * 得到n天之后 是 周几
	 * @param days
	 * @return
	 */
	public static int getDateAfterOfWeek(String days) {
		log.info("得到{}天后是周几",days);
		int daysInt = Integer.parseInt(days);
		return getDateAfterOfWeek(daysInt);
	}

	public static int getDateAfterOfWeek(Integer days) {
		log.info("得到{}天后是周几",days);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		//一周第一天是否为星期天
		boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		//获取周几
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		//若一周第一天为星期天，则-1
		if(isFirstSunday){
			weekDay = weekDay - 1;
			if(weekDay == 0){
				weekDay = 7;
			}
		}
		log.info("{} 天后是周 {}",weekDay);
		return weekDay;
	}

	public static int getDateAfterOfWeek(String time,String fromat,Integer days) {
		log.info("得到时间：{}的{}天后是周几",time,days);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(time,fromat));
		calendar.add(Calendar.DATE, days);
		//一周第一天是否为星期天
		boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		//获取周几
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		//若一周第一天为星期天，则-1
		if(isFirstSunday){
			weekDay = weekDay - 1;
			if(weekDay == 0){
				weekDay = 7;
			}
		}
		log.info("{} 天后是周 {}",weekDay);
		return weekDay;
	}

	/**
	 * 获取 x 分钟后的时间
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date getDateAfterMin(Date date,int minute){
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.setTime(date);
		canlendar.add(Calendar.MINUTE, minute);
		return canlendar.getTime();
	}

	/**
	 * 获取 x 分钟之前的时间
	 * @param minute
	 * @return
	 */
	public static Date getDateBeforerMin(int minute){
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.MINUTE, -minute);
		return canlendar.getTime();
	}

	/**
	 * 获取指定分钟之前的时间
	 * @param minute 指定分钟
	 * @return 指定分钟之前格式化后的时间 yyyy-MM-dd HH:MM
	 * 保留，先，admin中
	 */
	public static String getDateOfSpecifyMinuteBeFore(Long minute) {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime after = today.minus(minute, ChronoUnit.MINUTES);
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(after);
	}

	/**
	 * x 小时后的时间
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateAfterHour(Date date,int hour){
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.setTime(date);
		canlendar.add(Calendar.HOUR_OF_DAY, hour);
		return canlendar.getTime();
	}

	/**
	 * x 月份后的时间
	 * @param d：日期
	 * @param month：月份
	 * @return
	 */
	public static Date getDateAfterMonth(Date d, int month) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.add(Calendar.MONTH, month);
		return now.getTime();
	}

	/**
	 * 获取当前年月
	 * @return
	 */
	public static String getYear(String date,String dft){
		SimpleDateFormat smp=new SimpleDateFormat(dft,Locale.CHINA);
		try {
			smp.parse(date);
			int year=smp.getCalendar().get(Calendar.YEAR);
			return year+"";
		} catch (ParseException e) {
			return null;
		}

	}

	/**
	 * 得到 某个 日期的月份
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH)+1;
	}
	/**
	 * 获取当前年月
	 * @return
	 */
	public static String getMonth(String date,String dft){
		SimpleDateFormat smp=new SimpleDateFormat(dft,Locale.CHINA);
		try {
			smp.parse(date);
			int month = smp.getCalendar().get(Calendar.MONTH )+1;
			return month+"";
		} catch (ParseException e) {
			return null;
		}

	}

	/**
	 * 得到 某个 日期的小时
	 */
	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 得到 某个日期的 分钟
	 */
	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/***
	 * 设置小时*/
	public static Date setHour(int hour, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}

	/**
	 * 设置分钟
	 * */
	public static Date setMinute(int minute, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	 /**
     * 返回一个时间集合
     */
    public static List<Date> getDateList(Date startDate, Date endDate) {
		ArrayList<Date> dates=new ArrayList<Date>();
	    dates.add(startDate);
	    if(startDate.compareTo(endDate)==0){
	    	return dates;
	    }
		if(getDateAfterDay(startDate, 1).compareTo(endDate)==0){
			dates.add(endDate);
			return dates;
		}
		Date date=startDate;
		boolean flag=true;
		while(flag){
			date = getDateAfterDay(date, 1);
			if(endDate.compareTo(date)==0){
				dates.add(endDate);
				flag=false;
				continue;
			}
			dates.add(date);
		}
		return dates;
	}

	/**
	 * 相差月份
	 * @Title: getMonthSpace
	 * @param date1
	 * @param date2
	 */
	public static int getMonthSpace(String date1, String date2) {
        int result = 0;
        int month = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
			result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
			month = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return month + result;
    }

	/**
	 * 获取当年的第一天
	 * @return
	 */
	public static Date getCurrYearFirst(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取当年的最后一天
	 * @return
	 */
	public static Date getCurrYearLast(){
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return currYearLast;
	}

	/**
	 * 通过生日获取年龄
	 * @param birthday:格式：yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public static int getAge(String birthday) throws Exception {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateOfBirth = myFormatter.parse(birthday);
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			if (born.after(now)) {
				return age;
			}
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
			int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
			if (nowDayOfYear < bornDayOfYear) {
				age -= 1;
			}
		}
		return age;
	}

	/**
	 * 功能：判断输入年份是否为闰年<br>
	 * @param year
	 * @return 是：true 否：false
	 * @author pure
	 */
	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					leap = true;
				} else {
					leap = false;
				}
			} else {
				leap = true;
			}
		} else
			leap = false;
		return leap;
	}

	/**
	 * 功能：得到指定月份的月底 格式为：xxxx-yy-zz (eg: 2007-12-31)<br>
	 * @return String
	 */
	public static String getEndOfMonth(String str) {
		int tyear = Integer.parseInt(getYear(str,YYYY_MM_DD));
		int tmonth = Integer.parseInt(getMonth(str,YYYY_MM_DD));
		String strtmonth = null;
		String strZ = null;
		if (tmonth == 1 || tmonth == 3 || tmonth == 5 || tmonth == 7
				|| tmonth == 8 || tmonth == 10 || tmonth == 12) {
			strZ = "31";
		}
		if (tmonth == 4 || tmonth == 6 || tmonth == 9 || tmonth == 11) {
			strZ = "30";
		}
		if (tmonth == 2) {
			if (leapYear(tyear)) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strtmonth = tmonth >= 10 ? String.valueOf(tmonth) : ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + strZ;
	}

	/**
	 * 通过传入的时间来获得所属周内的时间
	 *
	 * @param start
	 * @param num
	 * @return
	 */
	public static String getDateOFWeekByDate(String start, int num) {
		Date dd = parse(start,YYYY_MM_DD);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num == 1) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num == 2) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num == 3) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num == 4) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num == 5) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num == 6) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num == 0) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return getYYYY_MM_DDFormat(c.getTime());
	}

//	/**
//	 * 获得时间序列 EG:2008-01-01~2008-01-31,2008-02-01~2008-02-29
//	 */
//	public static List getMonthSqu(String fromDate, String toDate) {
//		List list = new ArrayList();
//		int count = getMonthCountBySQU(fromDate, toDate);
//		int syear = Integer.parseInt(getDateByFormat(
//				getDateByStrToYMD(fromDate), YEAR));
//		int smonth = Integer.parseInt(getDateByFormat(
//				getDateByStrToYMD(fromDate), MONTH));
//		int eyear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(toDate),
//				YEAR));
//		String startDate = fromDate;
//		String endDate = "";
//		for (int i = 1; i <= count; i++) {
//			if (syear <= eyear) {
//				startDate = getStartOfMonth(syear, smonth);
//				endDate = getEndOfMonth(syear, smonth);
//				list.add(startDate + "~" + endDate);
//				System.out.println(startDate + "~" + endDate);
//				if (smonth == 13) {
//					smonth = 1;
//					syear++;
//				}
//				smonth++;
//			}
//		}
//		return list;
//	}

//	/**
//	 * 功能：得到指定月份的月初 格式为：xxxx-yy-zz (eg: 2007-12-01)<br>
//	 * @param start
//	 * @return String
//	 */
//	public static int getMonthCountBySQU(String start, String end) {
//		int syear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
//				YEAR));
//		int smonth = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
//				MONTH));
//		int eyear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
//				YEAR));
//		int emonth = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
//				MONTH));
//		return (eyear - syear) * 12 + (emonth - smonth) + 1;
//	}

	/**
	 * 获取日期月份的第一天
	 * @param str
	 * @return
	 */
	public static String getStartOfMonth(String str) {
		int tyear = Integer.parseInt(getYear(str,YYYY_MM_DD));
		int tmonth = Integer.parseInt(getMonth(str,YYYY_MM_DD));
		String strtmonth = tmonth >= 10 ? String.valueOf(tmonth)
				: ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + "01";
	}

	/**
	 * 功能：得到指定月份的月初 格式为：xxxx-yy-zz (eg: 2007-12-01)<br>
	 * @param tyear
	 * @return String
	 */
	public static String getStartOfMonth(int tyear, int tmonth) {
		String strtmonth = tmonth >= 10 ? String.valueOf(tmonth)
				: ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + "01";
	}


	public static String formatDate(String date,String time) {
		if(StringUtils.isNotEmpty(date) && date.length()==8 && StringUtils.isNotEmpty(time) && time.length()==6) {
			String fdate = date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8);
			String ftime = time.substring(0, 2)+":"+time.substring(2, 4)+":"+time.substring(4, 6);
			return fdate+" "+ftime;
		}

		return null;
	}

}
