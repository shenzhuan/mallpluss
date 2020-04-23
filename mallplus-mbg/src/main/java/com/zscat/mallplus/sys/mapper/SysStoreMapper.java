package com.zscat.mallplus.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.SysStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-05-18
 */
public interface SysStoreMapper extends BaseMapper<SysStore> {


    List<SysStore> selectDisStore(@Param("distance") Integer distance,
                                  @Param("address_lat") double address_lat,
                                  @Param("address_lng") double address_lng,
                                  @Param("pageSize") Integer pageSize);
    List<SysStore> selectDisStore1(@Param("distance") Integer distance,
                                  @Param("address_lat") double address_lat,
                                  @Param("address_lng") double address_lng,
                                  @Param("pageSize") Integer pageSize);
}
