package com.mei.zhuang.vo.goods;

import lombok.Data;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/8 22:22
 * @Description:
 */
@Data
public class GoodsQuery {
    /**
     * 分类id
     */
    private String categoryId;
    /**
     * 渠道
     */
    private String putawayType;
    /**
     * 商品编号或者名称
     */
    private String idAndName;

    /**
     * 状态 商品状态（1为出售中，3为已售罄，-2为仓库中，-1为回收站）
     */
    private Integer status;


    private Integer current = 1;
    private Integer size = 10;
    //  状态 商品状态（1为出售中，3为已售罄
    private Integer appletstatus;

    //  状态 商品状态（1 上架商品
    private Integer needPutAway;

    private String type;//商品类型 1实物商品 2 虚拟商品 3 赠品

}
