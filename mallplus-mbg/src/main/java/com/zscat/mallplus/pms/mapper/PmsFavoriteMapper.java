package com.zscat.mallplus.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.pms.entity.PmsFavorite;
import com.zscat.mallplus.pms.entity.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
public interface PmsFavoriteMapper extends BaseMapper<PmsFavorite> {

    List<PmsFavorite> selectCollectStatics(@Param("date") String date, @Param("pageSize") Integer pageSize);
}
