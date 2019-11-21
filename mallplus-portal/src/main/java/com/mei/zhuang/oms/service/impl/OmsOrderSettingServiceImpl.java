package com.mei.zhuang.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.oms.service.IOmsOrderSettingService;
import com.zscat.mallplus.oms.entity.OmsOrderSetting;
import com.zscat.mallplus.oms.mapper.OmsOrderSettingMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单设置表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsOrderSettingServiceImpl extends ServiceImpl<OmsOrderSettingMapper, OmsOrderSetting> implements IOmsOrderSettingService {

}
