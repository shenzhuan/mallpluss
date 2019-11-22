package com.mei.zhuang.controller.order;

import com.mei.zhuang.util.DateUtils;
import com.mei.zhuang.utils.DateCalendarUtils;
import com.mei.zhuang.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: Tiger
 * @Date: 2019-05-18 17:22
 * @Description:
 */
public class TestMain {


    public static void main(String[] args) throws Exception {
        /*if(DateUtils.format(DateUtils.toDate("2019-05-18 18:14:19.726")).equals(DateUtils.format(new Date()))){
            System.out.println("true");
        }
        BigDecimal price = new BigDecimal("-0.1");
        int compare = price.compareTo(BigDecimal.ZERO);
        if(compare != 1){
            System.out.println("负数 空1");
        }
        BigDecimal price1 = new BigDecimal("0");
        int compare1 = price1.compareTo(BigDecimal.ZERO);
        if(compare1 != 1){
            System.out.println("负数 空2");
        }*/
 /*       BigDecimal totatl = new BigDecimal("1");
        BigDecimal price = new BigDecimal("99.99");
        BigDecimal multiply = price.multiply(totatl);
        System.out.println(multiply);
        BigDecimal bignum1 = new BigDecimal("10");
        BigDecimal bignum2 = new BigDecimal("5");
        BigDecimal bignum3 = null;

//加法
        bignum3 = bignum1.add(bignum2);
        System.out.println("和 是：" + bignum3);

//减法
        bignum3 = bignum1.subtract(bignum2);
        System.out.println("差  是：" + bignum3);

//乘法
        bignum3 = bignum1.multiply(bignum2);
        System.out.println("积  是：" + bignum3);

//除法
        bignum3 = bignum1.divide(bignum2);
        System.out.println("商  是：" + bignum3);


        String text = "{\n" +
                "\t\"1\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t},\n" +
                "\t\"2\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t},\n" +
                "\t\n" +
                "\t\"3\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t},\n" +
                "\t\n" +
                "\t\"4\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t}\n" +
                "}\n";

        String mobile = "12345612345";
        String regex = "^1\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Boolean flag = pattern.matcher(mobile).matches();
        System.out.println(flag);

//        goodsTotalPrice = goodsTotalPrice.add(cart.getPrice().multiply(new BigDecimal(cart.getTotal())).multiply(orderItem.getPriceDiscount()));

        BigDecimal total = new BigDecimal("11.0");
        BigDecimal priceUnit = new BigDecimal("11.11");
        BigDecimal discount = new BigDecimal("1.0");
        BigDecimal finalPrice = new BigDecimal(0);
        BigDecimal bigDecimal = MathOperation.BigDeciamlToIntger(priceUnit, 5);
        BigDecimal bigDecimal1 = MathOperation.BigDeciamlToBigDecimal(bigDecimal, discount);
        System.out.println("MathOpe ： " + bigDecimal1);

        finalPrice = finalPrice.add(priceUnit.multiply(total).multiply(discount));
        System.out.println(finalPrice);
        System.out.println("/r/n" + priceUnit.multiply(BigDecimal.valueOf(11)));

        System.out.println("/r/n" + priceUnit.multiply(BigDecimal.valueOf(11)).setScale(2, BigDecimal.ROUND_HALF_UP));


        String jsonData = "{\n" +
                "\t\"1\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t},\n" +
                "\t\"2\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t},\n" +
                "\t\n" +
                "\t\"3\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t},\n" +
                "\t\n" +
                "\t\"4\":{\n" +
                "\t\t\"variableName\":\"\",\n" +
                "\t\t\"smsTypeId\":\"\"\n" +
                "\t}\n" +
                "}\n" +
                "\n";
        System.out.println("原json数据 ->> " + jsonData);
        Map<Integer, SmsVariable> templateVariMap = (Map<Integer, SmsVariable>) JSONObject.parse(jsonData);
        System.out.println("json转MAp数据 ->> " + templateVariMap);
//        System.out.println(templateVariMap.get(1).getVariableName());
        Iterator<Map.Entry<Integer, SmsVariable>> entries = templateVariMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, SmsVariable> entry = entries.next();
//            SmsVariable smsVariable = entry.getValue();
//            System.out.println(smsVariable);
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }


       *//* stringbuffer value = new stringbuffer("a,b,c");
        value = value.deletecharat(value.length() - 1);
        system.out.println(value);*//*

        String data = "[{\"variableName\":\"a\",\"smsTypeFunctionId\":1,\"name\":\"{验证码}\"},{\"variableName\":\"ae\",\"smsTypeFunctionId\":\"\",\"name\":\"{商城变量} \"}]";
        List<SmsVariable> list = JSONObject.parseArray(data, SmsVariable.class);
        System.out.println(list);


        BigDecimal total2 = new BigDecimal("1738.01");
        Integer count2 = 7;
//        System.out.println(total2.divide(BigDecimal.valueOf(count2).setScale(2,BigDecimal.ROUND_HALF_UP)));
//        System.out.println(total2.divide(BigDecimal.valueOf(count2).setScale(2,RoundingMode.FLOOR)).doubleValue());
//        System.out.println(total2.divide(BigDecimal.valueOf(count2).setScale(2,BigDecimal.ROUND_HALF_UP)).doubleValue());

//        System.out.println(total2.divide(BigDecimal.valueOf(count2).setScale(2)));
        double total_d = 1738.01;
        System.out.println(BigDecimal.valueOf(total_d / count2).setScale(2, BigDecimal.ROUND_HALF_UP));

        System.out.println(BigDecimal.valueOf(0).doubleValue() / 1);

        double scale = (double) 5 / 7 * 100;
        scale = BigDecimal.valueOf(scale).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        System.out.println(scale + "scale");


        TradeDataByHeadVo temp = new TradeDataByHeadVo();
        BigDecimal a = BigDecimal.valueOf(0.0);
        BigDecimal b = BigDecimal.valueOf(0.00);
        BigDecimal c = new BigDecimal(0.0);
        BigDecimal d = new BigDecimal(0.00);

        int num1 = 77;
        int totalCount = 9999;
        System.out.println(77 / 9999);


        String json = "{\n" +
                "  \"list\": [\n" +
                "    {\n" +
                "      \"ref_date\": \"201703\",\n" +
                "      \"session_cnt\": 126513,\n" +
                "      \"visit_pv\": 426113,\n" +
                "      \"visit_uv\": 48659,\n" +
                "      \"visit_uv_new\": 6726,\n" +
                "      \"stay_time_session\": 56.4112,\n" +
                "      \"visit_depth\": 2.0189\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        double scale1 = 0.11;

        System.out.println((1-scale1));

        double aa = (double)5/(7) * 100;
        System.out.println(aa);


        String startTIme = "2019-01-01";
        System.out.println(DateUtil.format(startTIme, DateUtil.YYYY_MM_DD, DateUtil.YYYYMMDD));
*/

       /* String dt="2019-06-05";
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c=Calendar.getInstance(Locale.CHINA);
        System.out.println(Locale.CHINA);
        c.setTime(f.parse(dt));
//        c.set(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.DAY_OF_MONTH, -1);
//        c.add(Calendar.YEAR, -1);//拨回去年
        c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));//最后一天
        System.out.println("最后一天的日期:"+f.format(c.getTime()));


        String str = "12fff34_&56AA";
        String regex = "^\\w+$";
        Pattern pattern = Pattern.compile(regex);
        Boolean matcher = pattern.matcher(str).matches();
        if(!matcher){
            System.out.println("不能输入非法字符");
        }

        BigDecimal payAmount = BigDecimal.valueOf(3061.89);//支付金额
        int payCount = 228;//支付订单数 （后台支付 + 实际付款数）

//        BigDecimal unitPriceByOne = payCount != 0 ? payAmount.divide(BigDecimal.valueOf(payCount)).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.valueOf(0.0);
//        System.out.println(payAmount.divide(BigDecimal.valueOf(payCount)));
        double pp  = 3061.89;
        System.out.println(BigDecimal.valueOf(3061.89 / 228).setScale(2,BigDecimal.ROUND_HALF_UP));

//        System.out.println(unitPriceByOne);

        BigDecimal price = new BigDecimal(-1000);
        System.out.println("小于0"+price.compareTo(BigDecimal.ZERO));

        BigDecimal price2 = new BigDecimal(0);
        System.out.println("=0" + price2.compareTo(BigDecimal.ZERO));


//        if(DateCalendarUtils.isFristDayByDate(startTime, DateUtil.YYYY_MM_DD) && DateCalendarUtils.isLastDayByDate(endTime, DateUtil.YYYY_MM_DD)){//自然月
        System.out.println("開始時間：" + DateCalendarUtils.isFristDayByDate("2019-07-01",DateUtil.YYYY_MM_DD));
        System.out.println("結束時間：" + DateCalendarUtils.isLastDayByDate("2019-07-31",DateUtil.YYYY_MM_DD));

        String startTime = "2019-07-30";
//        condition.le("create_time", Timestamp.valueOf(DateUtil.format(param.getEndTime(), DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));

        System.out.println(Timestamp.valueOf(DateUtil.format(startTime, DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));
        startTime += " 23:59:59.0";
        System.out.println(startTime);

        System.out.println(Timestamp.valueOf(startTime));
        System.out.println(Timestamp.valueOf(DateUtil.format(startTime, DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD_HH_MM_SS)));

        double v = BigDecimal.valueOf((double)(3 - 4) / 4)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
        System.out.println(v);
        double i = (double)(3 - 4) / 4;
        System.out.println(i);


        System.out.println((int)DateUtil.getDaySub("2019-08-01", "2019-08-02", DateUtil.YYYY_MM_DD));

*/
        /*String var = "id:40093;";
        String var2 = "id:41;";
        String sub1 = var.substring(var.indexOf(":") + 1, var.indexOf(";"));
        String sub2 = var2.substring(var2.indexOf(":") + 1, var2.indexOf(";"));
        System.out.println(sub1);
        System.out.println(sub2);*/

        //集合测试。
        /*

        Map<String, Set<String>> goodsUVMap = new HashMap<>();

        Set<String>  setS  = new HashSet<>();

        setS.add("1");
        setS.add("1");
        System.out.println(setS.size());


        */

        String startTime = "2019-7-1";
        String endTime = "2019-7-31";

        if(DateCalendarUtils.isFristDayByDate(startTime, DateUtil.YYYY_MM_DD) &&
                DateCalendarUtils.isLastDayByDate(endTime, DateUtil.YYYY_MM_DD)) {//自然月
            System.out.println(true);

        }else{
            System.out.println(false);
        }

//        "2019-8-7"
//                2019-12-21

        String date = "2019-12-17";
        System.out.println(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));

        System.out.println(date.substring(date.lastIndexOf("-") + 1, date.length()));

        System.out.println(DateCalendarUtils.isFristDayOfMonth("2019-7-1"));
        String handleDatePatter = DateCalendarUtils.handleDatePatter("2019-7-1");
        System.out.println(handleDatePatter);


        System.out.println(DateCalendarUtils.isLastDayOfMonth("2019-7-31"));

        String format = DateUtil.format("2019-7-31", DateUtil.YYYY_MM_DD, DateUtil.YYYY_MM_DD);

        SimpleDateFormat dateFormat = new SimpleDateFormat("");


        String format1 = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
        System.out.println(format1);

        String province = "湖南";
        province = province.replaceAll("省", "");
        System.out.println(province);




    }
}
