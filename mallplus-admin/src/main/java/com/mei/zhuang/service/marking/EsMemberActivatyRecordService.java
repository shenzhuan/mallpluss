package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsMemberActivatyRecord;

import java.util.Map;

public interface EsMemberActivatyRecordService extends IService<EsMemberActivatyRecord> {

    /**
     * 查询用户抽奖记录列表
     *
     * @param entity
     * @return
     */
    Map<String, Object> selPageList(EsMemberActivatyRecord entity);

}
