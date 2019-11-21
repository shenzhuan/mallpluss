package com.mei.zhuang.vo.marking;

import com.mei.zhuang.entity.marking.EsShopDiscount;
import com.mei.zhuang.entity.marking.EsShopFirstPurchase;
import com.mei.zhuang.entity.marking.EsShopFullGift;
import com.mei.zhuang.entity.marking.EsShopManjian;
import lombok.Data;

import java.util.List;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/28 09:54
 * @Description:
 */
@Data
public class MarkingResult {
    private List<EsShopFullGift> shopFullGiftList;
    private List<EsShopDiscount> discountList;
    private List<EsShopFirstPurchase> firstPurchasesList;
    private List<EsShopManjian> manjianList;
}
