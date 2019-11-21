package com.mei.zhuang.service.goods.impl;

import com.arvato.service.goods.api.orm.dao.EsCoreMessageMapper;
import com.arvato.service.goods.api.service.EsCoreMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.goods.EsCoreMessage;
import org.springframework.stereotype.Service;

@Service
public class EsCoreMessageServiceImpl extends ServiceImpl<EsCoreMessageMapper, EsCoreMessage> implements EsCoreMessageService {
}
