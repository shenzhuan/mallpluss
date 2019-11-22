package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsCoreAttachmentMapper;
import com.mei.zhuang.entity.goods.EsCoreAttachment;
import com.mei.zhuang.service.goods.EsCoreAttachmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EsCoreAttachmentServiceImpl  extends ServiceImpl<EsCoreAttachmentMapper, EsCoreAttachment> implements EsCoreAttachmentService {

    @Resource
    private EsCoreAttachmentMapper esCoreAttachmentMapper;



}
