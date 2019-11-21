package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopActivity;

import java.util.Map;

public interface EsShopActivityService extends IService<EsShopActivity> {

    Map<String,Object> selPageList(EsShopActivity entity);

    boolean save(EsShopActivity entity);

    boolean updates(EsShopActivity entity);

    boolean deletes(Long id);

    EsShopActivity detail(Long id);

}
