package com.zscat.mallplus.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2019/8/9.
 */
public class ConstansValue {
    public static final List<String> IGNORE_TENANT_TABLES = Lists.newArrayList("sys_admin_log", "sys_web_log", "sys_permission_category", "columns", "tables", "information_schema.columns",
            "information_schema.tables", "oms_payments", "oms_order_return_reason", "sys_role", "ums_integration_consume_setting", "sys_user", "sys_store", "sys_area", "sys_school", "sys_permission", "pms_product_attribute", "pms_product_category_attribute_relation", "pms_product_attribute_value",
            "pms_product_category_attribute_relation", "admin_day_statics", "ums_member_tag","sys_store_rank", "pms_small_navicon_category", "bak_category", "es_shop_goods_group_map", "sys_applet_set", "bak_goods", "pms_product_category", "bak_brand", "oms_order_setting", "ums_member", "ums_member_level", "building_user_community", "gen_config");

    public static final String sampleGoodsList = "id, brand_id, product_category_id, feight_template_id, product_attribute_category_id, name, pic, product_sn,\n" +
            "        delete_status, publish_status, new_status, recommand_status, verify_status, sort, sale, price, promotion_price,\n" +
            "        original_price, stock, low_stock, store_name,sub_title,store_id,unit,\n" +
            "        weight, preview_status, service_ids,is_fenxiao, tags, brand_name,\n" +
            "        product_category_name, supply_id, create_time,tags, school_id,area_id,is_vip";
    public static final String sampleGoodsList1 = "id, brand_id, product_category_id,  product_attribute_category_id, name,area_id, pic ,\n" +
            "         sale, price,\n" +
            "        original_price,\n" +
            "        weight \n" +
            "        ";
    public static final String sampleOrderList = "id,member_id, order_sn, create_time, member_username, total_amount, pay_amount,status, order_type,pay_type,source_type,goods_id,goods_name";

    public static final String sampleSubjectList = "id, category_id, title, pic, product_count, recommend_status, create_time, collect_count, read_count, comment_count, album_pics, description, show_status,  forward_count, category_name, area_id, school_id, member_id, area_name, school_name, reward, member_name, video_src, type";

    public static final String sampleCommunityList = "id, company_id, name, pic, price,wuyecompany,wuyefei,latitude,longitude,create_time ";

}
