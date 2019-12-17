package com.zscat.mallplus.jifen.service.impl;

import com.zscat.mallplus.jifen.entity.JifenDonateRule;
import com.zscat.mallplus.jifen.mapper.JifenDonateRuleMapper;
import com.zscat.mallplus.jifen.service.IJifenDonateRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author mallplus
* @date 2019-12-17
*/
@Service
public class JifenDonateRuleServiceImpl extends ServiceImpl
<JifenDonateRuleMapper, JifenDonateRule> implements IJifenDonateRuleService {

@Resource
private  JifenDonateRuleMapper jifenDonateRuleMapper;


}
