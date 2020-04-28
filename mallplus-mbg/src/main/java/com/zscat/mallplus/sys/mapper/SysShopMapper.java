package com.zscat.mallplus.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysShop;
import com.zscat.mallplus.sys.entity.SysStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mallplus
 * @date 2020-03-29
 */
public interface SysShopMapper extends BaseMapper<SysShop> {

    List<SysShop> selectDisShop(@Param("distance") Integer distance,
                                  @Param("latitude") double latitude,
                                  @Param("longitude") double longitude,
                                  @Param("pageSize") Integer pageSize);
    List<SysShop> selectDisShop1(@Param("distance") Integer distance,
                                   @Param("latitude") double latitude,
                                   @Param("longitude") double longitude,
                                   @Param("pageSize") Integer pageSize);
}
