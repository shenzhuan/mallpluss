package com.mei.zhuang.service.sys;

import com.arvato.common.msg.DictData;
import com.arvato.common.orm.model.SysDbResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
public interface ISysDbResourceService extends IService<SysDbResource> {
    /**
     * 获取数据源字典列表
     * @return
     */
    List<DictData> getDictList();
}
