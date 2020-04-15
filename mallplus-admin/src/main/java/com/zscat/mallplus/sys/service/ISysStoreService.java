package com.zscat.mallplus.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sys.entity.SysStore;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-05-18
 */
public interface ISysStoreService extends IService<SysStore> {


    boolean saveStore(SysStore entity);

    int updateShowStatus(List<Long> ids, Integer showStatus);

    int audit(List<Long> ids, Integer showStatus);
}
