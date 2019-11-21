package com.mei.zhuang.utils;



import com.mei.zhuang.entity.order.EsShopPayment;
import com.mei.zhuang.vo.CommonResult;

import java.lang.reflect.Field;

/**
 * @Auther: Tiger
 * @Date: 2019-06-11 18:21
 * @Description:
 */
public class VeryFieldEmptyUtils {

    /**
     * 验证一些必须的字段
     * @param entity 实体
     * @param colmunS  已逗号(,)分隔开的字段
     * @return
     */
    private static <T> Object veryFieldEmpty(T entity, String colmunS){
        try{
            String[] colmuns = colmunS.split(",");
            int len = colmuns.length;
            Class<?> clazz = entity.getClass();
            for(int i = 0; i < len; i++){
                String fieldS = colmuns[i];
                if(fieldS != null){
                    fieldS = fieldS.trim();
                    Field field = clazz.getDeclaredField(fieldS);
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if(ValidatorUtils.empty(value) && !value.equals(0)){
                        String annoValue =null;
                        return new CommonResult().failed(annoValue == null ? fieldS : annoValue + " 不能为空");
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        EsShopPayment entity = new EsShopPayment();
        entity.setStatus(0);
        System.out.println(veryFieldEmpty(entity,"id,status"));
        System.out.println(11);



    }


}
