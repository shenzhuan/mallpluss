package com.zscat.mallplus.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sys.entity.SysStoreCash;

import java.util.List;

/**
 * @author mallplus
 * @date 2020-04-10
 */

public interface ISysStoreCashService extends IService<SysStoreCash> {

    int audit(List<Long> ids, Integer showStatus);
}
