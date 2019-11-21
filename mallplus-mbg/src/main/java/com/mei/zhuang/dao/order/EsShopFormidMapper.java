package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsShopFormid;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小程序formid Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-06-26
 */
public interface EsShopFormidMapper extends BaseMapper<EsShopFormid> {

    List<EsShopFormid> getFormIdByMemberId(@Param("userId") Long userId, @Param("createTime") String createTime);
}
