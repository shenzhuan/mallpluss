package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopPackageGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-06-24
 */
public interface EsShopPackageGoodsMapper extends BaseMapper<EsShopPackageGoods> {

    Integer updatestatus(Integer param1, long param2);

    List<EsShopPackageGoods> packageList(@Param("goods") EsShopPackageGoods goods);

    Integer updatePackage(@Param("packageName") String packageName, @Param("goodId") long goodId);
}
