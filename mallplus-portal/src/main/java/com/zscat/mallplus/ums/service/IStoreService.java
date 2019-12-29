package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.oms.vo.StoreContentResult;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.ums.entity.OmsShip;

/**
 * Created by Administrator on 2019/12/28.
 */
public interface IStoreService extends IService<SysStore> {

    Object applyStore(SysStore entity);

    StoreContentResult singeleContent( Integer id);
}
