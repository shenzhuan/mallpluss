package com.mei.zhuang.service.order;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsCoreMessageTemplate;

import java.util.Map;

public interface EsCoreMessageTemplateService extends IService<EsCoreMessageTemplate> {

    Map<String,Object> select(EsCoreMessageTemplate entity);
}
