package com.mei.zhuang.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.goods.EsCoreAddress;

import java.util.List;

public interface EsCoreAddressService extends IService<EsCoreAddress> {
    /**
     * 查询地址
     *
     * @return
     */
    List<EsCoreAddress> selEsCoreAddress(EsCoreAddress entity);
}
