package com.mei.zhuang.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.sys.CrmSysDept;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.vo.ZTreeNode;
import com.mei.zhuang.vo.sys.DeptDictData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arvato team
 * @since 2018-08-22
 */
public interface ICrmSysDeptService extends IService<CrmSysDept> {
    List<ZTreeNode> getDeptTreeByOrgType(int orgType, List<Integer> rowDeptIds);

    /**
     * 获取部门ids
     * @param menuId
     * @param crmSysUser
     */
    List<Integer> getDeptIds(Integer menuId, CrmSysUser crmSysUser);


    /**
     * 初始化用户部门数据
     * @param menuId
     */
    List<ZTreeNode> initDeptDatasByCurrentUser(Integer pid, Integer menuId, CrmSysUser crmSysUser);

    /**
     * 组织架构数据字典
     * @return
     */
    List<DeptDictData> dict();
}
