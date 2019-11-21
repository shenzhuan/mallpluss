package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsCoreAttachment;

import java.util.Map;

public interface EsCoreAttachmentService extends IService<EsCoreAttachment> {


    Map<String,Object> selPageList(EsCoreAttachment entity);
}
