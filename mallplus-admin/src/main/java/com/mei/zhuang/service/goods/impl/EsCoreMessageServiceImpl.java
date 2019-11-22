package com.mei.zhuang.service.goods.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.goods.EsCoreMessageMapper;
import com.mei.zhuang.entity.goods.EsCoreMessage;
import com.mei.zhuang.service.goods.EsCoreMessageService;
import org.springframework.stereotype.Service;

@Service
public class EsCoreMessageServiceImpl extends ServiceImpl<EsCoreMessageMapper, EsCoreMessage> implements EsCoreMessageService {
}
