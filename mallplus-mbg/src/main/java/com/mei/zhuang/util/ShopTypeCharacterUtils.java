package com.mei.zhuang.util;

import com.mei.zhuang.enums.ShopType;

/**
 * @Auther: Tiger
 * @Date: 2019-05-30 16:31
 * @Description:
 */
public class ShopTypeCharacterUtils {


    public static String getChByShopType(Integer shopType) {
        if (ShopType.APPLET.getValue() == shopType) return "微信小程序";
        if (ShopType.OTHER.getValue() == shopType) return "其他";
        return "";
    }


}
