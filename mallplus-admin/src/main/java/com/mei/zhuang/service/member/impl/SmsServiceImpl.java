package com.mei.zhuang.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.member.*;
import com.mei.zhuang.entity.member.*;
import com.mei.zhuang.service.member.ISmsService;
import com.mei.zhuang.vo.SmsParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-30 13:42
 * @Description:
 */
@Service
public class SmsServiceImpl extends ServiceImpl<EsCoreSmsTemplateMapper, EsCoreSmsTemplate> implements ISmsService {

    @Resource
    private EsCoreSmsTemplateMapper smsTemplateMapper;
    @Resource
    private EsCoreSmsTypeMapper smsTypeMapper;
    @Resource
    private EsCoreSmsMapper smsMapper;
    @Resource
    private EsCoreSmsSignatureMapper smsSignatureMapper;
    @Resource
    private EcSmsFunctionMapper smsFunctionMapper;

    @Override
    public Page<EsCoreSmsTemplate> selectPageExt(SmsParam entity) {
        Page<EsCoreSmsTemplate> page = new Page<>(entity.getCurrent(), entity.getSize());
        List<EsCoreSmsTemplate> list = smsTemplateMapper.selectSmsTemplateList(entity);
        // page.setAsc(entity.getIsAsc() == 0 ? false : true);
        page.setTotal(smsTemplateMapper.selectSmsTemplateCount(entity));
        page.setRecords(list);
        return page;
    }

    @Override
    public List<EsCoreSms> getServerInfo() {
        return smsMapper.selectList(new QueryWrapper<>(new EsCoreSms()));
    }

    @Override
    public List<EsCoreSmsType> getSmsTypeInfo() {
        return smsTypeMapper.selectList(new QueryWrapper<>(new EsCoreSmsType()));
    }

    @Override
    public boolean deleteById(Long id) {
        EsCoreSmsTemplate template = this.selectById(id);
        template.setIsDelete(1);
        return this.updateById(template);
    }

    @Override
    public EsCoreSmsTemplate selectById(Long id) {
        EsCoreSmsTemplate smsTemplate = smsTemplateMapper.selectById(id);
        smsTemplate.setServerInfo(smsMapper.selectById(smsTemplate.getServerId()));
        return smsTemplate;
    }

    @Override
    @Transactional
    public boolean updSmsServerInfopAllById(List<EsCoreSms> list) {
        for (EsCoreSms temp : list) {
            smsMapper.updateById(temp);
        }
        //待完善
        return true;
    }

    @Override
    public List<EcSmsFunction> getSmsFuncBySmsTypeId(Long smsTypeId) {
        return smsFunctionMapper.selectList(new QueryWrapper<>(new EcSmsFunction()).eq("sms_type_id", smsTypeId));
    }


    @Override
    public boolean isExistTemplateByStatus(EsCoreSmsTemplate entity) {
        if (entity.getStatus() != 1) {
            return false;
        }
        List<SmsVariable> smsVariableList = JSON.parseArray(entity.getData(), SmsVariable.class);
        EcSmsFunction temp = smsFunctionMapper.selectById(smsVariableList.get(0).getSmsTypeFunctionId());
        if (temp != null) {
//            根据模板类型id查找改模板类型的数量
            entity.setTemplateType(temp.getSmsTypeId());//设置该模板是什么类型
            Integer templateCount = smsTemplateMapper.selectCount(new QueryWrapper<>(new EsCoreSmsTemplate())
                    .eq("template_type", temp.getSmsTypeId())
                    .eq("status", 1)
                    .eq("is_delete", 0)
                    .eq("server_id", entity.getServerId())
            );
            return templateCount > 0;
        }
        return false;
    }

    @Override
    public boolean isExistRepeatName(String templateName) {
        return this.smsTemplateMapper.selectCount(
                new QueryWrapper<>(
                        new EsCoreSmsTemplate()).eq("sms_name", templateName)) > 0;
    }

}
