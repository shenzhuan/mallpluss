package com.mei.zhuang.vo.marking;

import com.mei.zhuang.entity.marking.EsShopCodeGiftGoodsMap;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/7/10.
 */
@Data
public class CodeResult implements Serializable{
    private List<EsShopCodeGiftGoodsMap> giftGoodsMaps;
    /**
     * 1 验证码不存在 2 该商品不可使用促销码 3 正常 4 已使用 5 活動禁用
     */
    private int status;
}
