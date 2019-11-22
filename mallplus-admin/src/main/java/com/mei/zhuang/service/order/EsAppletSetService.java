package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsAppletSet;

import java.util.List;

public interface EsAppletSetService extends IService<EsAppletSet> {

    boolean save(EsAppletSet entity);

    List<EsAppletSet> select();

    Integer update(EsAppletSet entity);
}
