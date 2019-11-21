package com.mei.zhuang.dao.order;

import com.arvato.ec.common.vo.order.EsCoreLogParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsCoreLog;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-06-21
 */
public interface EsCoreLogMapper extends BaseMapper<EsCoreLog> {

    List<EsCoreLog> selecPageList(EsCoreLogParam param);

    Integer selectLogCount(EsCoreLogParam param);


}
