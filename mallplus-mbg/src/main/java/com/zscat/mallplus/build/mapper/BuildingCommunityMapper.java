package com.zscat.mallplus.build.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.build.entity.BuildingCommunity;
import com.zscat.mallplus.sys.entity.SysShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小区 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
public interface BuildingCommunityMapper extends BaseMapper<BuildingCommunity> {

    List<BuildingCommunity> selectNearCommunity(@Param("distance") Integer distance,
                                @Param("latitude") double latitude,
                                @Param("longitude") double longitude,
                                @Param("pageSize") Integer pageSize);
    List<BuildingCommunity> selectNearCommunity1(@Param("distance") Integer distance,
                                 @Param("latitude") double latitude,
                                 @Param("longitude") double longitude,
                                 @Param("pageSize") Integer pageSize);
}
