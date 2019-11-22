package com.mei.zhuang.service.sys.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.sys.CrmApiUserMapper;
import com.mei.zhuang.dao.sys.CrmApiUserPermissionMapper;
import com.mei.zhuang.entity.sys.CrmApiUser;
import com.mei.zhuang.entity.sys.CrmApiUserPermission;
import com.mei.zhuang.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiUserBiz {
    @Resource
    private CrmApiUserMapper apiUserMapper;

    @Resource
    private CrmApiUserPermissionMapper permissionMapper;


    public Map<String,Object> getApiUserList(String account,String firmName,String status, Integer limit, Integer offset, List<Integer> deptIds) {
        Map<String,Object> result = new HashMap<>();
        List<CrmApiUser> list = apiUserMapper.list(account, firmName, status, limit, offset, deptIds);
        list.forEach((apiUser)->{
            apiUser.setCreateTime(DateUtil.getYYYY_MM_DD_HH_MM_SSFormat(DateUtil.getYYYYMMDDHHMMSSFormat((apiUser.getCreateDate().toString()+apiUser.getCreateTime()))));
        });
        int count = apiUserMapper.count(account, firmName, status, deptIds);
        result.put("rows", list);
        result.put("total", count);
        return  result;
    }

    @Transactional
    public int delete(int id) {
        //查询api权限表里有没有相关数据
        int count = this.permissionMapper.selectCount(new QueryWrapper<>(new CrmApiUserPermission(),"count(0)").eq("api_user_id",id));
        //如果有数据先删除
        if(count > 0) {
            this.permissionMapper.delete(new QueryWrapper<>(new CrmApiUserPermission()).eq("api_user_id",id));
        }
        return apiUserMapper.deleteById(id);
    }

}
