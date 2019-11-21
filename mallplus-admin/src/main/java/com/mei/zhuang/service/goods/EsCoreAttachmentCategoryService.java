package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsCoreAttachmentCategory;

import java.util.List;

public interface EsCoreAttachmentCategoryService extends IService<EsCoreAttachmentCategory> {

    List<EsCoreAttachmentCategory> selectLists(EsCoreAttachmentCategory entity);
}
