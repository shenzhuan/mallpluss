package com.mei.zhuang.dao.goods;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.goods.EsCoreAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsCoreAddressMapper extends BaseMapper<EsCoreAddress> {

    List<EsCoreAddress> selEsCoreAddress(@Param("level") Integer level, @Param("parentId") Long parentId);

}
