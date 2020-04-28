package com.zscat.mallplus.build.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.build.BuildHomeResult;
import com.zscat.mallplus.build.entity.BuildingCommunity;
import com.zscat.mallplus.sys.entity.SysShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小区 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-11-27
 */
public interface IBuildingCommunityService extends IService<BuildingCommunity> {

    /**
     * 添加社区
     *
     * @param entity
     * @return
     */
    boolean saveCommunity(BuildingCommunity entity);

    /**
     * 所有社区和房间
     *
     * @return
     */
    Object allCommunity();

    /**
     * 绑定小区和房间
     *
     * @param selectedMore2
     * @return
     */
    Object bindCommunity(String selectedMore2);

    /**
     * 小区首页
     *
     * @param id
     * @return
     */
    BuildHomeResult singleContent(Long id);

    List<BuildingCommunity> selectNearCommunity( Integer distance,
                                      double latitude,
                                       double longitude,
                                     Integer pageSize);
}
