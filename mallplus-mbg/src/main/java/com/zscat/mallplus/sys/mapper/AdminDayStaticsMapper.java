package com.zscat.mallplus.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sys.entity.AdminDayStatics;

import java.util.List;

/**
 * @author mallplus
 * @date 2020-04-10
 */
public interface AdminDayStaticsMapper extends BaseMapper<AdminDayStatics> {
    List<AdminDayStatics> selectAdminDayStaticsGroupBySId();
}
