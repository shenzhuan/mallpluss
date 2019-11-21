package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsCoreAttachmentCategory;

import java.util.List;

public interface EsCoreAttachmentCategoryMapper extends BaseMapper<EsCoreAttachmentCategory> {

    List<EsCoreAttachmentCategory> selectLists(EsCoreAttachmentCategory entity);
}
