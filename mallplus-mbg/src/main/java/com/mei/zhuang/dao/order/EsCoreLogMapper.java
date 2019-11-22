package com.mei.zhuang.dao.order;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.order.EsCoreLog;
import com.mei.zhuang.vo.order.EsCoreLogParam;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-06-21
 */
public interface EsCoreLogMapper extends BaseMapper<EsCoreLog> {

    List<EsCoreLog> selecPageList(EsCoreLogParam param);

    Integer selectLogCount(EsCoreLogParam param);


}
