package com.mei.zhuang.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Description:List结合的工具类
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2018/12/24
 */
public class ListUtils {

    /**
     * 字符传转集合，Integer类型
     * @param arrayStr
     * @return
     */
    public static List<Integer> parseStrToListForInt(String arrayStr){
        List<Integer> list = null;
        if(!StringUtils.isEmpty(arrayStr.trim())){
            String[] deptIdArrary = arrayStr.split(",");
            if(deptIdArrary.length>0){
                for(int i = 0;i<deptIdArrary.length;i++){
                    list.add(Integer.parseInt(deptIdArrary[i]));
                }
            }
        }
        return list;
    }

    /**
     * 结合转字符串，Integer类型
     * @return
     */
    public static String parseIntListToStr(List<Integer> list,String separator){
        return  StringUtils.join(list,separator);
    }
}
