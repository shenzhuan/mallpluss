package com.mei.zhuang.service.sys.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.sys.CrmSysDataAuthMapper;
import com.mei.zhuang.dao.sys.CrmSysDeptMapper;
import com.mei.zhuang.dao.sys.CrmSysUserMapper;
import com.mei.zhuang.dao.sys.CrmSysUserRoleMapper;
import com.mei.zhuang.entity.sys.CrmSysDept;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.service.sys.ICrmSysDeptService;
import com.mei.zhuang.vo.ZTreeNode;
import com.mei.zhuang.vo.sys.DeptDictData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author meizhuang team
 * @since 2018-08-22
 */
@Service
public class CrmSysDeptServiceImpl extends ServiceImpl<CrmSysDeptMapper, CrmSysDept> implements ICrmSysDeptService {

    @Resource
    private CrmSysDeptMapper crmSysDeptMapper;

    @Resource
    private CrmSysUserRoleMapper userRoleMapper;

    @Resource
    private CrmSysDataAuthMapper dataAuthMapper;

    @Resource
    private CrmSysUserMapper sysUserMapper;


    @Override
    public List<ZTreeNode> getDeptTreeByOrgType(int orgType, List<Integer> roleDeptIds) {
        return crmSysDeptMapper.getDeptTreeByOrgType(orgType, roleDeptIds);
    }

//    @Override
//    List<ZTreeNode> getDeptLeafNodsByPid(Integer pid){
//        return null;
//    }

    @Override
    public List<Integer> getDeptIds(Integer menuId, CrmSysUser crmSysUser) {
        List<Integer> roleIds = userRoleMapper.getRoleIdsbyUserId(crmSysUser.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleIds", roleIds);
        map.put("menuId", menuId);
        Integer dataAuthLevel = dataAuthMapper.getAuthLevelByMenuIdAndRoleIds(map);
        List<Integer> deptIdList = new ArrayList<Integer>();
        //数据权限等级为部门，获取当前所属部门和管理部门ID
        Integer deptId = crmSysUser.getDeptId();
        //所属部门
        deptIdList.add(deptId);
        //管理部门
//	    String manageDeptId = crmSysUser.getManageDeptId();
//	    if (StringUtils.isNotEmpty(manageDeptId)) {
//	        String[] manageDeptArr = manageDeptId.split(",");
//			for (String aManageDeptArr : manageDeptArr) {
//				if (StringUtils.isNotEmpty(aManageDeptArr)) {
//					deptIdList.add(Integer.valueOf(aManageDeptArr));
//				}
//			}
//	    }
        if (dataAuthLevel == null) {
            return null;
        }
        if (dataAuthLevel == 1) {
            return null;
        } else if (dataAuthLevel == 2) {
//		    List<Integer> deptIdsList = dataAuthMapper.getDeptIdById(deptId);
//		    if(deptIdsList != null){
//		    	deptIdList.addAll(deptIdsList);
//		    }
//	        //数据权限等级为上下级，获取当前所属部门和管理部门级下级部门ID
//	        List<Integer> returnformat = dataAuthMapper.selectDeptIdByPid(deptIdList);
//	        deptIdList.addAll(returnformat);
            String manageDeptId = crmSysUser.getManageDeptId();
            if (StringUtils.isNotEmpty(manageDeptId)) {
                String[] manageDeptArr = manageDeptId.split(",");
                for (String aManageDeptArr : manageDeptArr) {
                    if (StringUtils.isNotEmpty(aManageDeptArr)) {
                        deptIdList.add(Integer.valueOf(aManageDeptArr));
                    }
                }
            }
        }
        return deptIdList;
    }

    @Override
    public List<ZTreeNode> initDeptDatasByCurrentUser(Integer pid, Integer menuId, CrmSysUser crmSysUser) {
        List<Integer> roleDeptIds = this.getDeptIds(menuId, crmSysUser);
        return crmSysDeptMapper.initDeptDatasByCurrentUser(pid, roleDeptIds);
    }

    @Override
    public List<DeptDictData> dict() {
        return this.crmSysDeptMapper.dict();
    }
}
