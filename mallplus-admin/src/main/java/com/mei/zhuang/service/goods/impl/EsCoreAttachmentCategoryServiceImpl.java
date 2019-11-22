package com.mei.zhuang.service.goods.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsCoreAttachmentCategoryMapper;
import com.mei.zhuang.entity.goods.EsCoreAttachmentCategory;
import com.mei.zhuang.service.goods.EsCoreAttachmentCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EsCoreAttachmentCategoryServiceImpl extends ServiceImpl<EsCoreAttachmentCategoryMapper, EsCoreAttachmentCategory> implements EsCoreAttachmentCategoryService {

    @Resource
    private EsCoreAttachmentCategoryMapper esCoreAttachmentCategoryMapper;

    @Override
    public List<EsCoreAttachmentCategory> selectLists(EsCoreAttachmentCategory entity) {
        return esCoreAttachmentCategoryMapper.selectLists(entity);
    }
}
