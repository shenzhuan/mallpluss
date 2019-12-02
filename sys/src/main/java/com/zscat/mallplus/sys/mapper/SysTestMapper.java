package com.zscat.mallplus.sys.mapper;


import com.zscat.mallplus.sys.entity.SysTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author mallplus
* @date 2019-12-02
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysTestMapper extends BaseMapper<SysTest> {
}
