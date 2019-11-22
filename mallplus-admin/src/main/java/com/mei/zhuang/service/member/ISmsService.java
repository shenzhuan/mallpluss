package com.mei.zhuang.service.member;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.member.EcSmsFunction;
import com.mei.zhuang.entity.member.EsCoreSms;
import com.mei.zhuang.entity.member.EsCoreSmsTemplate;
import com.mei.zhuang.entity.member.EsCoreSmsType;
import com.mei.zhuang.vo.SmsParam;

import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-05-30 10:01
 * @Description:
 */
public interface ISmsService extends IService<EsCoreSmsTemplate> {

    Page<EsCoreSmsTemplate> selectPageExt(SmsParam entity);

    /**
     * 获得短信服务商信息
     *
     * @return
     */
    List<EsCoreSms> getServerInfo();

    /**
     * 获得短信类型信息
     *
     * @return
     */
    List<EsCoreSmsType> getSmsTypeInfo();


    boolean deleteById(Long id);

    EsCoreSmsTemplate selectById(Long id);

    boolean updSmsServerInfopAllById(List<EsCoreSms> list);

    List<EcSmsFunction> getSmsFuncBySmsTypeId(Long smsTypeId);


    //true : 存在改类别的模板  false:不存在
    boolean isExistTemplateByStatus(EsCoreSmsTemplate entity);

    boolean isExistRepeatName(String templateName);
}
