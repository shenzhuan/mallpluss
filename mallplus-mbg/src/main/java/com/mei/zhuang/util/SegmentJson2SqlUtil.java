package com.mei.zhuang.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class SegmentJson2SqlUtil {

    /**
     * 查询主表
     */
    public static String tableName_crm_member_tags = "crm_member_tags";
    /**
     * 查询主表
     */
    public static String tableName_ex_h_member_tags = "ex_h_member_tags";
    /**
     * 运算符类型
     */
    public static String operation_contain = "contain";
    public static String operation_notcontain = "notcontain";
    /**
     * 查询字段数据类型
     */
    public static String tagDbDefine_int = "int";
    public static String tagDbDefine_array = "array";
    public static String tagDbDefine_datestring = "datestring";
    public static String tagDbDefine_datetimestring = "datetimestring";

    public static String json_null = "()";

    /**
     * 把json转换成sql
     * crm_member_tags为查询主表，不用多表关联，若是查询其它表数据，用exists的方式查询
     *
     * @param jsonStr
     * @return
     */
//    public static String getSql(String jsonStr){
//        JSONObject jsonObject= JSONObject.parseObject(jsonStr);
//        JSONObject filterObj = jsonObject.getJSONObject("filter");
//        String whereCondition = recursionSql(filterObj);
//        if(json_null.equals(whereCondition.trim())){
//            whereCondition = null;
//        }
//
//        return whereCondition;
//    }
    public static String getSql(String jsonStr) {
        String whereCondition = "";
        if (StringUtils.isEmpty(jsonStr)) {
            return whereCondition;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if (jsonObject != null) {
            JSONObject filterObj = jsonObject.getJSONObject("filter");
//        JSONArray filterArray = filterObj.getJSONArray("item");
//        for (int i = 0; i < filterArray.size(); i++) {
//            if(i == 0){
//                whereCondition = getSingleFilter(filterArray.getJSONObject(i));
//            }else{
//                whereCondition = whereCondition + " and " + getSingleFilter(filterArray.getJSONObject(i));
//            }
//        }
            whereCondition = getSingleFilter(filterObj);
        }
        return whereCondition;
    }

    private static String getSingleFilter(JSONObject filterObj) {
        String whereCondition = recursionSql(filterObj);
        if (json_null.equals(whereCondition.trim())) {
            whereCondition = null;
        }

        return whereCondition;
    }

    private static String recursionSql(JSONObject obj) {
        if (obj.containsKey("item")) {
            JSONArray array = obj.getJSONArray("item");
            if (array.size() == 1) {
                return recursionSql(array.getJSONObject(0));
            } else {
                String operator = obj.getString("operator");
                String tempSql = "";
                for (int i = 0; i < array.size(); i++) {
                    if (i == 0) {
                        tempSql = recursionSql(array.getJSONObject(i));
                    } else {
                        tempSql = tempSql + " " + operator + " " + recursionSql(array.getJSONObject(i));
                    }
                }
                tempSql = "(" + tempSql + ")";
                return tempSql;
            }
        } else {
            return singleSql(obj);
        }
    }

    private static String singleSql(JSONObject obj) {
        String op = "";
        String operation = obj.getString("operation");
        if ("eq".equals(operation)) {
            //等于
            op = "=";
        } else if ("le".equals(operation)) {
            //小于等于
            op = "<=";
        } else if ("lt".equals(operation)) {
            //小于
            op = "<";
        } else if ("ge".equals(operation)) {
            //大于等于
            op = ">=";
        } else if ("gt".equals(operation)) {
            //大于
            op = ">";
        } else if ("ne".equals(operation)) {
            //不等于
            op = "<>";
        } else if ("like".equals(operation)) {
            //包含
            op = "";
        } else if ("notlike".equals(operation)) {
            //不包含
            op = "";
        } else if ("in".equals(operation)) {
            return inSql(obj);
        } else if (operation_contain.equals(operation) || operation_notcontain.equals(operation)) {
            return ifContainSql(obj);
        } else if ("have".equals(operation) || "nothave".equals(operation)) {
            //拥有和不拥有
            return haveSql(obj);
        }

        String dbType = obj.getString("tagDbDefine");

        if (tagDbDefine_datestring.equals(dbType)) {
            String value = obj.getString("value");
            value = value.replaceAll("/", "").replaceAll("-", "");
            obj.put("value", value);
        } else if (tagDbDefine_datetimestring.equals(dbType)) {
            String value = obj.getString("value");
            value = value.replaceAll("/", "").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
            obj.put("value", value);
        }

        String tag = obj.getString("tag");
        String tableName = obj.getString("tagDsTable");
        if (tableName != null && tableName_crm_member_tags.equals(tableName)) {
            if ("BIR_DD".equals(obj.get("value"))) {
                SimpleDateFormat df = new SimpleDateFormat("MMdd");//设置日期格式
                String tagValue = df.format(new Date());// new Date()为获取当前系统时间
                op = "=";
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + tagValue + "'";
            }
            if ("BIR_DD_NOT".equals(obj.get("value"))) {
                SimpleDateFormat df = new SimpleDateFormat("MMdd");//设置日期格式
                String tagValue = df.format(new Date());// new Date()为获取当前系统时间
                op = "<>";
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + tagValue + "'";
            } else if ("BIR_MM".equals(obj.get("value"))) {
                SimpleDateFormat df = new SimpleDateFormat("MM");//设置日期格式
                String tagValue = df.format(new Date());// new Date()为获取当前系统时间
                op = "=";
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + tagValue + "'";
            }
            if ("BIR_MM_NOT".equals(obj.get("value"))) {
                SimpleDateFormat df = new SimpleDateFormat("MM");//设置日期格式
                String tagValue = df.format(new Date());// new Date()为获取当前系统时间
                //不等于
                op = "<>";
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + tagValue + "'";
            } else if ("BIR_NEXT_MM".equals(obj.get("value"))) {
                LocalDate now = LocalDate.now();

                String tagValue = now.minusMonths(-1).format(DateTimeFormatter.ISO_LOCAL_DATE).substring(5, 7);// new Date()为获取当前系统时间
                op = "=";
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + tagValue + "'";
            }
            if ("BIR_NEXT_MM_NOT".equals(obj.get("value"))) {
                LocalDate now = LocalDate.now();

                String tagValue = now.minusMonths(-1).format(DateTimeFormatter.ISO_LOCAL_DATE).substring(5, 7);// new Date()为获取当前系统时间
                //不等于
                op = "<>";
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + tagValue + "'";
            } else {
                tag = "tags->>'" + tag + "'";

                if (tagDbDefine_int.equals(dbType)) {
                    tag = "CAST(" + tag + " AS numeric)";
                }
                return tag + op + "'" + obj.get("value") + "'";
            }

            //return tag + op + "'" + obj.get("value") + "'";
        } else if (tableName_ex_h_member_tags.equals(tableName)) {
            JSONArray tagValue = (JSONArray) obj.getJSONArray("value");
        	/*String[] tagValues = tagValue.split(",");
        	if(tagValues.length == 0){
        		return "";
        	}
        	tag = tagValues[0] + " = ANY (t.member_tags)";
        	for (int i = 1; i < tagValues.length; i++) {
        		tag = tag + " AND " + tagValues[i]  + " = ANY (t.member_tags)";
			}*/
            //tag = tag + op + "'" + obj.get("value") + "'";
            tag = " t.member_tags @> '{" + tagValue.toJSONString().replace("[", "").replace("]", "") + "}'::int4[] ";
            return "EXISTS(select 1 from " + tableName + " t where t.member_id=m.member_id and " + tag + ")";
        } else {
            tag = tag + op + "'" + obj.get("value") + "'";
            return "EXISTS(select 1 from " + tableName + " t where t.member_id=m.member_id and t." + tag + ")";
        }
    }

    /**
     * tags 是 否包含标签
     *
     * @param obj
     * @return
     */
    private static String haveSql(JSONObject obj) {
        String tag = obj.getString("tag");
        String operation = obj.getString("operation");
        String dbType = obj.getString("tagDbDefine");
        if ("array".equals(dbType)) {
            //值是数组，是否包含
            JSONArray values = obj.getJSONArray("value");
            List<Integer> selectIds = new LinkedList<>();
            if (values != null && values.size() > 0) {
                for (int i = 0; i < values.size(); i++) {
                    JSONObject object = (JSONObject) values.get(i);
                    Integer vid = object.getInteger("id");
                    selectIds.add(vid);
                }
            }
            if ("have".equals(operation)) {
                //拥有和不拥有
                return "  tags @> '{\"" + tag + "\": [" + StringUtils.join(selectIds, ",") + "]}'";
            } else if ("nothave".equals(operation)) {
                return " not tags @> '{\"" + tag + "\": [" + StringUtils.join(selectIds, ",") + "]}'";
            }
        } else {
            //值是普通单值
            String value = obj.getString("value");
            if ("have".equals(operation)) {
                //拥有和不拥有
                return "  tags @> '{\"" + tag + "\": " + value + "}'";
            } else if ("nothave".equals(operation)) {
                return " not tags @> '{\"" + tag + "\": " + value + "}'";
            }
        }
        return " ";
    }

    /**
     * 以区间方式处理
     *
     * @param obj
     * @return
     */
    private static String inSql(JSONObject obj) {
        String tag = obj.getString("tag");

        String values = obj.getString("value");
        String[] value = values.split("-");
        String value1 = value[0].trim();
        String value2 = value[1].trim();

        String dbType = obj.getString("tagDbDefine");

        if (tagDbDefine_datestring.equals(dbType)) {
            value1 = value1.replaceAll("/", "").replaceAll("-", "");
            value2 = value2.replaceAll("/", "").replaceAll("-", "");
        } else if (tagDbDefine_datetimestring.equals(dbType)) {
            value1 = value1.replaceAll("/", "").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
            value2 = value2.replaceAll("/", "").replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        }

        String tableName = obj.getString("tagDsTable");
        if (tableName != null && tableName_crm_member_tags.equals(tableName)) {
            tag = "tags->>'" + tag + "'";
            if (tagDbDefine_int.equals(dbType)) {
                tag = "CAST(" + tag + " AS numeric)";
            }
            return "(" + tag + ">='" + value1 + "' and " + tag + "<='" + value2 + "')";
        } else {
            tag = tag + ">='" + value1 + "' and t." + tag + "<='" + value2 + "'";
            return "EXISTS(select 1 from " + tableName + " t where t.member_id=m.member_id and t." + tag + ")";
        }
    }

    /**
     * 以多选方式处理
     *
     * @param obj
     * @return
     */
    private static String ifContainSql(JSONObject obj) {
        String valueStr = obj.getString("value");
        JSONArray values = JSONObject.parseArray(valueStr);

        String operation = obj.getString("operation");
        String tag = obj.getString("tag");
        if (ToolUtil.isNum(tag)) {
            //如果是数字，则认为是json字段
            String returnStr;
            String ids = getIds(values, "'");

            String tableName = obj.getString("tagDsTable");
            if (tableName != null && tableName_crm_member_tags.equals(tableName)) {
                tag = "tags->>'" + tag + "'";
            }
            if (operation_contain.equals(operation)) {
                returnStr = tag + " in (" + ids + ")";
            } else {
                returnStr = tag + " not in (" + ids + ")";
            }
            if (tableName != null && !tableName_crm_member_tags.equals(tableName)) {
                returnStr = "EXISTS(select 1 from " + tableName + " t where t.member_id=m.member_id and t." + returnStr + ")";
            }

            return returnStr;
        } else {
            String returnStr;
            String tagDbDefine = obj.getString("tagDbDefine");
            String tableName = obj.getString("tagDsTable");

            if (tagDbDefine_array.equals(tagDbDefine)) {
                //如果是数组，则按数组的方式处理
                returnStr = tag + " && ARRAY[" + getIds(values, "") + "]::bigint[]";
            } else {
                //否则字段中只存单个值，则用in的方式查询
                returnStr = tag + " in (" + getIds(values, "'") + ")";
            }

            if (tableName != null && tableName_crm_member_tags.equals(tableName)) {
                if (operation_contain.equals(operation)) {
                    return returnStr;
                } else {
                    return "not EXISTS(select 1 from crm_member_tags mt where mt.member_id=m.member_id and mt." + returnStr + ")";
                }
            } else {
                if (operation_contain.equals(operation)) {
                    return "EXISTS(select 1 from " + tableName + " t where t.member_id=m.member_id and t." + returnStr + ")";
                } else {
                    return "not EXISTS(select 1 from " + tableName + " t where t.member_id=m.member_id and t." + returnStr + ")";
                }
            }
        }
    }

    /**
     * 根据surroundBy确定字符串还是数字类型
     *
     * @param values
     * @param surroundBy
     * @return
     */
    public static String getIds(JSONArray values, String surroundBy) {
        String splitStr = ",";
        String ids = "";
        for (int i = 0; i < values.size(); i++) {
            JSONObject val = values.getJSONObject(i);
            ids = ids + splitStr + surroundBy + val.getString("id") + surroundBy;
        }

        if (ids.length() > 0) {
            ids = ids.substring(1);
        }

        return ids;
    }

    public static void main(String[] args) {
        //String str = "{\"filter\":{\"operator\":\"and\",\"item\":[{\"level\":1,\"group\":\"会员属性-基本\",\"tagId\":1700000298,\"tag\":\"1700000298\",\"tagDbDefine\":\"string\",\"tagDsTable\":\"crm_member_tags\",\"operation\":\"eq\",\"value\":\"2\"}]}}";
        String str = "{\"filter\":{\"operator\":\"and\",\"item\":[{\"level\":1,\"group\":\"会员\",\"tag\":1700000297,\"tagDbDefine\":\"datestring\",\"tagDsTable\":\"ex_h_member_tags\",\"operation\":\"eq\",\"value\":\"20,74\"}]}}";
        System.out.println(getSql(str));
    }

}
