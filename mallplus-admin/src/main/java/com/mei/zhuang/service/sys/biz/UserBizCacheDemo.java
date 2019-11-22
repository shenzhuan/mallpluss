package com.mei.zhuang.service.sys.biz;

import com.mei.zhuang.dao.sys.CrmSysUserMapper;
import com.mei.zhuang.entity.sys.CrmSysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@CacheConfig(cacheNames = "sysUser")
public class UserBizCacheDemo {

    private static final String KEY_ALL = "'sysUser-*'";
    @Autowired
    private CrmSysUserMapper userMapper;
    @Autowired
    private HttpServletRequest request;

    @Cacheable(key = KEY_ALL)
    public List<CrmSysUser> getAll() {
        try {
            return userMapper.selectAllUser();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Cacheable(key = "#p0")
    public CrmSysUser getById(Integer userId) {
        try {
            return userMapper.selectById(userId);
        } catch (Exception ex) {
            throw ex;
        }
    }


    @CachePut(key = "#user.id")
    @Caching(evict = {@CacheEvict(key = KEY_ALL)})
    public boolean create(CrmSysUser user) {
        try {
            return user.insert();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Caching(evict = {@CacheEvict(key = KEY_ALL)})
    @CacheEvict(key = "#user.id")
    public boolean update(CrmSysUser user) {
        return user.updateById();
    }


    @Caching(evict = {@CacheEvict(key = KEY_ALL)})
    @CacheEvict(key = "#user.id")
    public Integer delete(Integer userId) {
        try {
            return userMapper.deleteById(userId);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
