package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmApiUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author meizhuang team
 * @since 2017-12-05
 */
public interface CrmApiUserMapper extends BaseMapper<CrmApiUser> {

    List<CrmApiUser> list(@Param("account") String account, @Param("firmName") String firmName, @Param("status") String status,
                          @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("deptIds") List<Integer> deptIds);

    int count(@Param("account") String account, @Param("firmName") String firmName, @Param("status") String status, @Param("deptIds") List<Integer> deptIds);

}