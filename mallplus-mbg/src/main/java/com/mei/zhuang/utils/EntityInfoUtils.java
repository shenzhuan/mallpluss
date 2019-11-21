package com.mei.zhuang.utils;



import com.mei.zhuang.entity.Table.TableColumnInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-06-24 14:23
 * @Description:
 */
public class EntityInfoUtils {

    /**
     * 获得一个类的属性和注释
     * @param clazz
     * @return
     */
    public static List<TableColumnInfo>  getTableColumnInfo(Class<?> clazz){
        try{
            if(clazz == null){
                return null;
            }
            Field[] fields = clazz.getDeclaredFields();
            List<TableColumnInfo> list = new ArrayList<>();
            for(Field field : fields){
                TableColumnInfo temp = new TableColumnInfo();
                temp.setColumnName(field.getName());
             //   temp.setComment(field.getDeclaredAnnotation(FieldText.class).value());
                list.add(temp);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得一个类的属性和注释（排除不允许导出去的字段）
     * @param clazz
     * @param fieldNameS
     * @return
     */
    public static List<TableColumnInfo>  getTableColumnInfoByCondition(Class<?> clazz, String fieldNameS){
        try{
            if(clazz == null){
                return null;
            }
            String[] split = fieldNameS.split(",");
            Field[] fields = clazz.getDeclaredFields();
            List<TableColumnInfo> list = new ArrayList<>();
            boolean flag = false;
            for(Field field : fields){
                flag = false;
                TableColumnInfo temp = new TableColumnInfo();
                String propName = field.getName();
                for(String str : split){
                    if(propName.equals(str)){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    continue;
                }
                temp.setColumnName(propName);
            //    temp.setComment(field.getDeclaredAnnotation(FieldText.class).value());
                list.add(temp);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        //测试
        System.out.println(getTableColumnInfo(TableColumnInfo.class));
//        [TableColumnInfo(columnName=columnName, comment=字段), TableColumnInfo(columnName=comment, comment=注释)]

    }



}
