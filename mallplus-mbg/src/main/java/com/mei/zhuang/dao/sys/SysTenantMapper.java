package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.vo.DictData;
import com.mei.zhuang.vo.sys.DataSourceDto;
import com.mei.zhuang.vo.sys.SysTenantInfo;
import com.mei.zhuang.vo.sys.SysTenantPagingData;
import com.mei.zhuang.vo.sys.SysTenantPagingParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2019-01-16
 */
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    List<SysTenant> getValidTenantByIds(@Param("ids") List<Integer> ids);

    DataSourceDto selectDataSourceById(@Param("tenantId") Integer tenantId);

    int getPagingTotal(@Param("param") SysTenantPagingParam param);

    List<SysTenantPagingData> getPagingList(@Param("param") SysTenantPagingParam param);

    List<DictData> getDictList();

    SysTenantInfo getTenantInfoById(@Param("id") int id);
}
