package com.mei.zhuang.dao.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmSysDept;
import com.mei.zhuang.vo.ZTreeNode;
import com.mei.zhuang.vo.sys.DeptDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2017-10-20
 */
public interface CrmSysDeptMapper extends BaseMapper<CrmSysDept> {
    List<ZTreeNode> getDeptTree();
    List<ZTreeNode> getDeptTreeByOrgType(@Param("orgType") int orgType, @Param("roleDeptIds") List<Integer> roleDeptIds);

    /**
     * deptChildren 是为 deptId 的child,返回所属child id
     * @param deptId
     * @param deptChildren
     * @return
     */
    List<Integer> isDeptChild(@Param("deptId") int deptId, @Param("deptChildren") List<Integer> deptChildren);

    /**
     * 获取用户的当前部门
     * @param roleDeptIds  权限部门
     */
    List<ZTreeNode> initDeptDatasByCurrentUser(@Param("pid") Integer pid, @Param("roleDeptIds") List<Integer> roleDeptIds);

    List<DeptDictData> dict();

}
