package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopShare;

import java.util.List;

public interface EsShopShareService extends IService<EsShopShare> {

    List<EsShopShare> ShareList();

    Integer saveShare(EsShopShare share) throws Exception;

    Integer delete(long id);

    Integer update(EsShopShare share) throws Exception;

    Integer updatestatus(long id, Integer status);

    EsShopShare sharedetail(long id);

    //仅开启一个
    Integer status(Integer status);
}
