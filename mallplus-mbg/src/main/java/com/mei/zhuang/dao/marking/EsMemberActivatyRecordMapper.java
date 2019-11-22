package com.mei.zhuang.dao.marking;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsMemberActivatyRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsMemberActivatyRecordMapper extends BaseMapper<EsMemberActivatyRecord> {

    List<EsMemberActivatyRecord> selPageList(EsMemberActivatyRecord entity);

    Integer count(EsMemberActivatyRecord entity);

    List<EsMemberActivatyRecord> distinctMember(@Param("actavatyId") Long actavatyId);
}
