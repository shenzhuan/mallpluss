package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponManual;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-18
 */
public interface EsShopCouponManualMapper extends BaseMapper<EsShopCouponManual> {

    //手工发券查询
    List<Map<String, Object>> selectmanual(@Param("manual") EsShopCouponManual manual);

}
