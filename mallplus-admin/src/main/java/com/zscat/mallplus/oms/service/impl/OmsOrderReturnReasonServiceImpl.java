package com.zscat.mallplus.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.oms.entity.OmsOrderReturnReason;
import com.zscat.mallplus.oms.mapper.OmsOrderReturnReasonMapper;
import com.zscat.mallplus.oms.service.IOmsOrderReturnReasonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 退货原因表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsOrderReturnReasonServiceImpl extends ServiceImpl<OmsOrderReturnReasonMapper, OmsOrderReturnReason> implements IOmsOrderReturnReasonService {

    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        OmsOrderReturnReason record = new OmsOrderReturnReason();
        record.setStatus(status);
        this.update(record, new QueryWrapper<>(new OmsOrderReturnReason()).in("id", ids));
        return 1;
    }
}
