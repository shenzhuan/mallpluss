package com.mei.zhuang.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class SegmentJson2ESUtil {

    /**
     * 查询主表
     */
    public static String tableName_crm_member_tags = "crm_member_tags";
    public static String tableName_crm_order_tags = "crm_order_tags";
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
     * 部门权限
     *
     * @param jsonStr
     * @param defaultCondition
     * @param deptIds
     * @return
     */
    public static String getSql(String jsonStr, String defaultCondition, List<Integer> deptIds, Set<Integer> hypersTagIds) {
        String whereCondition = "";
        if (StringUtils.isEmpty(jsonStr)) {
            return whereCondition;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONObject filterObj = jsonObject.getJSONObject("filter");
        whereCondition += getSingleFilter(filterObj, hypersTagIds);
        if (deptIds != null) {
            if (defaultCondition != null && defaultCondition.trim().length() > 0) {
                defaultCondition += ",";
            }
            defaultCondition += "{\"terms\": {\"78\": [" + StringUtils.join(deptIds, ",") + "] }}";
        }
        if (defaultCondition != null && defaultCondition.trim().length() > 0) {
            whereCondition = "{\"bool\":{\"must\":[" + defaultCondition + "," + whereCondition + "]}}";
        } else {
            whereCondition = "{\"bool\":{\"must\":[" + whereCondition + "]}}";
        }
        return whereCondition;
    }

    private static String getSingleFilter(JSONObject filterObj, Set<Integer> hypersTagIds) {
        String whereCondition = recursionSql(filterObj, hypersTagIds);
        if (json_null.equals(whereCondition.trim())) {
            whereCondition = null;
        }

        return whereCondition;
    }

    private static String recursionSql(JSONObject obj, Set<Integer> hypersTagIds) {
        if (obj.containsKey("item")) {
            JSONArray array = obj.getJSONArray("item");
            if (array.size() == 1) {
                return recursionSql(array.getJSONObject(0), hypersTagIds);
            } else {
                String operator = obj.getString("operator");
                if ("or".equalsIgnoreCase(operator)) {
                    operator = "should";
                } else {
                    operator = "must";
                }
                String tempSql = "";
                for (int i = 0; i < array.size(); i++) {
                    if (i == 0) {
                        tempSql = recursionSql(array.getJSONObject(i), hypersTagIds);
                    } else {
                        tempSql = tempSql + "," + recursionSql(array.getJSONObject(i), hypersTagIds);
                    }
                }
                return "{\"bool\":{ \"" + operator + "\": [" + tempSql + "]}}";
            }
        } else {
            return singleSql(obj, hypersTagIds);
        }
    }

    private static String singleSql(JSONObject obj, Set<Integer> hypersTagIds) {
        String op = "";
        String operation = obj.getString("operation");
        if ("eq".equals(operation)) {
            //等于
            op = "";
        } else if ("le".equals(operation)) {
            //小于等于
            op = "lte";
        } else if ("lt".equals(operation)) {
            //小于
            op = "lt";
        } else if ("ge".equals(operation)) {
            //大于等于
            op = "gte";
        } else if ("gt".equals(operation)) {
            //大于
            op = "gt";
        } else if ("ne".equals(operation)) {
            //不等于
            op = "";
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
        String value = obj.getString("value");
        if (tagDbDefine_datestring.equals(dbType)) {
            value = value.replaceAll("/", "").replaceAll("-", "");
        } else if (tagDbDefine_datetimestring.equals(dbType)) {
            value = value.replaceAll("/", "").replaceAll("-", "")
                    .replaceAll(":", "").replaceAll(" ", "");
        }
        if (!tagDbDefine_int.equals(dbType)) {
            value = "\"" + value + "\"";
        }

        String returnStr;
        String tag = obj.getString("tag");
        String tableName = obj.getString("tagDsTable");
        if (tableName != null && tableName_crm_order_tags.equals(tableName)) {
            tag = "orders." + tag;
        }
        if ("".equals(op)) {
            if ("eq".equals(operation) || "ne".equals(operation)) {
                returnStr = "{ \"term\": { \"" + tag + "\":" + value + "}}";
                if (hypersTagIds != null && hypersTagIds.size() > 0) {
                    if (hypersTagIds.contains(Integer.parseInt(tag))) {
                        log.info("id: {} 标签为hypers标签", tag);
                        returnStr = "{ \"terms\": { \"tag_id_list\":[" + tag + "]}}";
                    }
                }
            } else {
                returnStr = "{ \"match\": { \"" + tag + "\":" + value + "}}";
            }

            if ("notlike".equals(operation) || "ne".equals(operation)) {
                returnStr = "{\"bool\":{ \"must_not\": [" + returnStr + "]}}";
            }
        } else {
            returnStr = "{\"range\":{\"" + tag + "\":{\"" + op + "\":" + value + "}}}";
        }

        if (tableName != null && tableName_crm_order_tags.equals(tableName)) {
            returnStr = "{\"nested\": {\"path\": \"orders\", \"query\": " + returnStr + "}}";
        }

        return returnStr;
    }

    private static String haveSql(JSONObject obj) {
        //TODO 待完成
//        String tag = obj.getString("tag");
//        String operation = obj.getString("operation");
//        if("have".equals(operation)){
//            //拥有和不拥有
//            return " not tags?'"+tag+"'";
//        }else if( "nothave".equals(operation)){
//            return " tags?'"+tag+"'";
//        }
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
            value1 = value1.replaceAll("/", "").replaceAll("-", "")
                    .replaceAll(":", "").replaceAll(" ", "");
            value2 = value2.replaceAll("/", "").replaceAll("-", "")
                    .replaceAll(":", "").replaceAll(" ", "");
        }

        if (!tagDbDefine_int.equals(dbType)) {
            value1 = "\"" + value1 + "\"";
            value2 = "\"" + value2 + "\"";
        }

        String tableName = obj.getString("tagDsTable");
        if (tableName != null && tableName_crm_member_tags.equals(tableName)) {
            return "{\"range\": {\"" + tag + "\": {\"gte\":" + value1 + ",\"lte\":" + value2 + "}}}";
        } else {
            return "{\"nested\": {\"path\": \"orders\", \"query\": {\"bool\": {\"must\": [ {\"range\":{\"orders." +
                    tag + "\": {\"gte\":" + value1 + ",\"lte\":" + value2 + "} }}]}}}}";
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
        String tableName = obj.getString("tagDsTable");
        String tag = obj.getString("tag");
        String ids;
        if (ToolUtil.isNum(tag)) {
            //如果是数字，则认为是json字段
            ids = SegmentJson2SqlUtil.getIds(values, "'");
        } else {
            String tagDbDefine = obj.getString("tagDbDefine");

            if (tagDbDefine_array.equals(tagDbDefine)) {
                //如果是数组，则按数组的方式处理
                ids = SegmentJson2SqlUtil.getIds(values, "");
            } else {
                //否则字段中只存单个值，则用in的方式查询
                ids = SegmentJson2SqlUtil.getIds(values, "'");
            }
        }

        if (tagDbDefine_int.equals(obj.getString("tagDbDefine"))) {
            ids = ids.replaceAll("'", "");
        } else {
            ids = ids.replaceAll("'", "\"");
        }

        if (tableName != null && tableName_crm_order_tags.equals(tableName)) {
            tag = "orders." + tag;
        }
        String returnStr = "{\"terms\":{\"" + tag + "\":[" + ids + "] }}";

        if (operation_notcontain.equals(operation)) {
            returnStr = "{\"bool\":{ \"must_not\": [" + returnStr + "]}}";
        }
        if (tableName != null && tableName_crm_order_tags.equals(tableName)) {
            returnStr = "{\"nested\": {\"path\": \"orders\", \"query\": " + returnStr + "}}";
        }

        return returnStr;
    }

    public static void main(String[] args) {
        String segmentJson = "{\"filter\":{\"operator\":\"and\",\"item\":[{\"level\":3,\"operator\":\"or\",\"item\":[{\"group\":\"自定义标签\",\"tag\":1700000141,\"tagDbDefine\":\"int\",\"tagDsTable\":\"crm_member_tags\",\"operation\":\"eq\",\"value\":\"1\"},{\"operator\":\"and\",\"item\":[{\"group\":\"自定义标签\",\"tag\":86,\"tagDbDefine\":\"int\",\"tagDsTable\":\"crm_member_tags\",\"operation\":\"eq\",\"value\":\"1\"},{\"group\":\"自定义标签\",\"tag\":1700000121,\"tagDbDefine\":\"int\",\"tagDsTable\":\"crm_member_tags\",\"operation\":\"eq\",\"value\":\"1\"}]}]}]}}";
        String whereConditionSql = SegmentJson2SqlUtil.getSql(segmentJson);
        System.out.println("" + whereConditionSql);
        String defaultCondition = "";
        Set<Integer> hypersTagIds = new HashSet<>();
        hypersTagIds.add(68);
        hypersTagIds.add(86);
        whereConditionSql = SegmentJson2ESUtil.getSql(segmentJson, defaultCondition, null, hypersTagIds);
        System.out.println("......whereConditionSql::" + whereConditionSql);
        String sourceJson = "{ \"_source\": {\"include\": [\"memberId\"]},\"from\" :" + 0 +
                ", \"size\" : " + 10 + ",\"query\": " + whereConditionSql + ",\"sort\" : [\"_id\"]}";
        log.info(sourceJson);
//        try {
//            String sourceJson = "{ \"_source\": {\"include\": [\"memberId\"]},\"from\" :9900, \"size\" : 10,\"query\": "+whereConditionSql+",\"sort\" : [\"_doc\"]}";
//            HttpClient client = HttpClients.createDefault();
//            String uri = "http://172.21.11.30:8090/test002/membertags/_search?pretty";
//            HttpPost httpPost = new HttpPost(uri);
//            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
//            StringEntity se = new StringEntity(sourceJson);
//            httpPost.setEntity(se);
//            HttpResponse response = client.execute(httpPost);
//            String responseStr = EntityUtils.toString(response.getEntity());
//            System.out.println("...................");
//            System.out.println("" + responseStr);
//            System.out.println("...................");
//
//            JSONObject jsonObject= JSONObject.parseObject(responseStr);
//            JSONObject hits = jsonObject.getJSONObject("hits");
//            System.out.println("............total:" + hits.getInteger("total"));
//            JSONArray hitsArray = hits.getJSONArray("hits");
//            for (int i = 0; i < hitsArray.size(); i++){
//                JSONObject obj = hitsArray.getJSONObject(i);
//                JSONObject source = obj.getJSONObject("_source");
//                System.out.println("" + source + "...:" + source.getString("memberId"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
