package com.mei.zhuang.service.sys.biz;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.annotation.DynamicData;
import com.mei.zhuang.dao.sys.SysPlatformUserMapper;
import com.mei.zhuang.entity.sys.SysPlatformUser;
import com.mei.zhuang.vo.sys.DataSourceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 平台用户操作逻辑，因为需要切换数据源，所以单独出来
 * @Author: qiaoqiao.zhu
 * @Date: Create in 2019/1/21
 */
@Service
public class SysPlatUserBiz {

    @Autowired
    private SysPlatformUserMapper sysPlatformUserMapper;

    @DynamicData
    public boolean checkUserNameValid(String username, DataSourceDto dto) {
        if (sysPlatformUserMapper.selectByUsername(username) == null) {
            return true;
        }
        return false;
    }

    @DynamicData
    public void addPlatFormUser(String username, String password, Integer managerId, Integer[] tenantIds, DataSourceDto dto) {
        SysPlatformUser sysPlatformUser = new SysPlatformUser();
        sysPlatformUser.setUsername(username);
        sysPlatformUser.setPassword(password);
        sysPlatformUser.setManageTenantIds(tenantIds);
        sysPlatformUser.setUserType("1");// 1表示普通用户
        sysPlatformUser.setStatus("0");//0表示启用 //TODO
        sysPlatformUser.insert();
    }

    @DynamicData
    public void updateByUsername(String username, SysPlatformUser sysPlatformUser, DataSourceDto dto) {
        sysPlatformUserMapper.update(
                sysPlatformUser,
                new QueryWrapper<>(new SysPlatformUser().setUsername(username))
        );
    }

    @DynamicData
    public SysPlatformUser getByUsername(String username, DataSourceDto dto) {
        return this.sysPlatformUserMapper.selectByUsername(username);
    }

}
