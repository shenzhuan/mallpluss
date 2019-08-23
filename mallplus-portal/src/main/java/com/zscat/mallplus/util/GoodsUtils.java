package com.zscat.mallplus.util;

import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.vo.SamplePmsProduct;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/8/6.
 */
public class GoodsUtils {
    public static List<SamplePmsProduct> sampleGoodsList(List<PmsProduct> list) {
        List<SamplePmsProduct> products = new ArrayList<>();
        for (PmsProduct product : list) {
            SamplePmsProduct en = new SamplePmsProduct();
            BeanUtils.copyProperties(product, en);
            products.add(en);
        }
        return products;
    }

    public static SamplePmsProduct sampleGoods(PmsProduct list) {
        SamplePmsProduct en = new SamplePmsProduct();
        BeanUtils.copyProperties(list, en);
        return en;
    }

    public static void main(String[] args) {
        // create 2 BigDecimal Objects
        BigDecimal bg1, bg2;

        bg1 = new BigDecimal(123.044);
        bg2 = new BigDecimal("-1.123");

        // create two int objects
        int i1,i2;

        // assign the result of scale on bg1, bg2 to i1,i2
        i1 = bg1.scale();
        i2 = bg2.scale();

        String str1 = "The scale of " + bg1 + " is " + i1;
        String str2 = "The scale of " + bg2 + " is " + i2;

        // print the values of i1,i2;
        System.out.println( str1 );
        System.out.println( str2 );

    }
}
