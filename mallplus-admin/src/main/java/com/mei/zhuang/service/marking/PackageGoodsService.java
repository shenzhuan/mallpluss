package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopPackageGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackageGoodsService extends IService<EsShopPackageGoods> {
    boolean save(EsShopPackageGoods entity) ;

    boolean update(EsShopPackageGoods entity) ;

    Integer deletePackage(String id);
    //批量修改状态
    Integer updatestatus(Integer status, String pid);

    EsShopPackageGoods packageList(long id);

    List<EsShopPackageGoods> packageList(EsShopPackageGoods goods);

    //商品同步删除
    Integer delete(@Param("good_id") long goodId, @Param("according") int according);

    Integer updatePackage(@Param("package_name") String packageName, @Param("good_id") long goodId);
}
