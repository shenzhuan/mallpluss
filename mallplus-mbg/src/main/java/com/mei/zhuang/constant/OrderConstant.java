package com.mei.zhuang.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Auther: Tiger
 * @Date: 2019-06-10 9:57
 * @Description:
 */
public class OrderConstant {



    public static  int EXPRESS_SN_LENGTH = 20;//快递单号长度

    public static  int AUTOC_LOSE_TIME_MAX = 10;//自动关闭订单分钟最大时间 minute

    public static  int AUTOFI_NSH_DAY_MAX = 30;//自动确认最大天数收货时间 day

    public static  int AUTOC_LOSE_TIME_MIN = 5;//自动关闭订单分钟最大时间 minute

    public static  int AUTOFI_NSH_DAY_MIN = 7;//自动确认最小天数收货时间 day


}
