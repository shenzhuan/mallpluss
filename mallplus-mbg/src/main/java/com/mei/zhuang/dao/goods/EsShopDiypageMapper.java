package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsShopDiypage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-06
 */
public interface EsShopDiypageMapper extends BaseMapper<EsShopDiypage> {

    //查询自定义列表
    //  List<EsShopDiypage> selDivpage(Pagination page, @Param("current") Integer current, @Param("size") Integer size, @Param("name") String name, @Param("type") Integer type);

    //查询自定义详情列表
    //  List<EsShopDiypage> selDivpageDetail(Pagination page, @Param("current") Integer current, @Param("size") Integer size, @Param("name") String name, @Param("type") Integer type);
    //查询详情总条数
    Integer selDivPageDetailCount(@Param("name") String name, @Param("type") Integer type);

    //查询详情总条数
    Integer selDivPageCount(@Param("name") String name, @Param("type") Integer type);

    Integer selDiyPageTypeId(@Param("typeId") Integer typeId, @Param("id") Long id);

    List<EsShopDiypage> selectAll();

    EsShopDiypage selDiyDetail(@Param("id") Integer id);

    Integer selectCounts(@Param("id") Long id, @Param("name") String name);
}
