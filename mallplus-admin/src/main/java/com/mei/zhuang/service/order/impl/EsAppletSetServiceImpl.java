package com.mei.zhuang.service.order.impl;

import com.arvato.service.order.api.orm.dao.EsAppletSetMapper;
import com.arvato.service.order.api.orm.dao.EsCoreMessageTemplateMapper;
import com.arvato.service.order.api.service.EsAppletSetService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.order.EsAppletSet;
import com.mei.zhuang.entity.order.EsCoreMessageTemplate;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "小程序设置", description = "", tags = {"小程序设置"})
@Service
public class EsAppletSetServiceImpl extends ServiceImpl<EsAppletSetMapper, EsAppletSet> implements EsAppletSetService {

    @Resource
    private EsAppletSetMapper esAppletSetMapper;
    @Resource
    private EsCoreMessageTemplateMapper esCoreMessageTemplateMapper;

    @Override
    public Integer save(EsAppletSet entity) {
        return esAppletSetMapper.insert(entity);
    }

    @Override
    public List<EsAppletSet> select() {
        EsAppletSet esAppletSet = new EsAppletSet();
        List<EsAppletSet> list= esAppletSetMapper.selectList(new QueryWrapper<>(esAppletSet));
        for (EsAppletSet appletSet : list){
            EsCoreMessageTemplate template = new EsCoreMessageTemplate();
            template.setTitle("下单成功通知");
            appletSet.setListOrderSuccessNotification(esCoreMessageTemplateMapper.selectList(new QueryWrapper<>(template)));
            template.setTitle("支付成功通知");
            appletSet.setListOrderPaySuccess(esCoreMessageTemplateMapper.selectList(new QueryWrapper<>(template)));
            template.setTitle("订单取消通知");
            appletSet.setListOrderCancellationNotice(esCoreMessageTemplateMapper.selectList(new QueryWrapper<>(template)));
            template.setTitle("订单发货通知");
            appletSet.setListOrderSend(esCoreMessageTemplateMapper.selectList(new QueryWrapper<>(template)));
            template.setTitle("订单状态更新通知");
            appletSet.setListOrderStatusChangeNotification(esCoreMessageTemplateMapper.selectList(new QueryWrapper<>(template)));
        }
        return list;
    }

    @Override
    public Integer update(EsAppletSet entity) {
        return esAppletSetMapper.updateById(entity);
    }
}
