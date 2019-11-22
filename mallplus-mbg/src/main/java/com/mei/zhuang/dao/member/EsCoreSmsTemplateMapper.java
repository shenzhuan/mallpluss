package com.mei.zhuang.dao.member;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.member.EsCoreSmsTemplate;
import com.mei.zhuang.vo.SmsParam;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-05-30
 */
public interface EsCoreSmsTemplateMapper extends BaseMapper<EsCoreSmsTemplate> {

    List<EsCoreSmsTemplate> selectSmsTemplateList(SmsParam smsParam);

    /*
        EsCoreSmsTemplate selectById(Long id);
    */
    Integer selectSmsTemplateCount(SmsParam smsParam);

}
