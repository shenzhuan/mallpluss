package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopCouponsTopup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-05-13
 */
public interface EsShopCouponsTopupMapper extends BaseMapper<EsShopCouponsTopup> {


    //查询满额
    List<Map<String, Object>> selectTopup(EsShopCouponsTopup esShopCouponsTopup);

    //修改满额状态
    Integer updatestatusid(Integer param1, Long param2);


}
