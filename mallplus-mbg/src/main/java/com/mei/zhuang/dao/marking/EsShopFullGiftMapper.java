package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopFullGift;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-02
 */
public interface EsShopFullGiftMapper extends BaseMapper<EsShopFullGift> {

    void stopFullGift();
    //选赠礼状态数量
    Integer selectstatus();
    //满赠礼
    Integer selectstatus2();

    EsShopFullGift selectfull();

}
