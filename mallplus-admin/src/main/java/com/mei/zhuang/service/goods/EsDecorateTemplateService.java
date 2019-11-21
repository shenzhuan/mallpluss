package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsDecorateTemplate;

/**
 * @Auther: shenzhuan
 * @Date: 2019/5/6 11:14
 * @Description:
 */
public interface EsDecorateTemplateService extends IService<EsDecorateTemplate> {
    Object save(EsDecorateTemplate entity);

    Object release(EsDecorateTemplate entity);
}
