package com.mei.zhuang.dao.sys;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmOperationLog;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-07-08
 */
public interface CrmOperationLogMapper extends BaseMapper<CrmOperationLog> {

    List<CrmOperationLog> selecPageList(CrmOperationLog entity);

    long selectLogCount(CrmOperationLog entity);

    List<CrmOperationLog> exportLogList(CrmOperationLog entity);



}
