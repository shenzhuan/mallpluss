package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysDataAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CrmSysDataAuthMapper extends BaseMapper<CrmSysDataAuth> {

    Integer getAuthLevelByMenuIdAndRoleIds(Map<String, Object> map);

    List<Integer> selectDeptIdByPid(@Param("deptIds") List<Integer> deptIds);

    List<Integer> getDeptIdById(@Param("deptId") Integer deptId);
}
