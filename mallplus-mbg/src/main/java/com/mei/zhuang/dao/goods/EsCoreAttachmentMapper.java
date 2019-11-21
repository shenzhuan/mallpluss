package com.mei.zhuang.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsCoreAttachment;

public interface EsCoreAttachmentMapper extends BaseMapper<EsCoreAttachment> {

  //  List<EsCoreAttachment> selPageList(Pagination page, EsCoreAttachment entity);

    Integer count(EsCoreAttachment entity);
}
