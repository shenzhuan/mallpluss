package com.zscat.mallplus.oms.vo;


import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.sms.entity.SmsHomeAdvertise;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 首页内容返回信息封装
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
@Getter
@Setter
public class StoreContentResult {
    List<PmsProductAttributeCategory> categoryList;
    List<PmsProductAttributeCategory> categoryList1;
    //轮播广告
    private List<SmsHomeAdvertise> advertiseList;
    //人气推荐
    private List<PmsProduct> hotProductList;
}
