package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsAppletTemplates;

import java.util.Map;

public interface EsAppletTemplateService extends IService<EsAppletTemplates> {

    Map<String, Object> select(EsAppletTemplates entity);
}
