package com.mei.zhuang.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.sys.SysDbResource;
import com.mei.zhuang.vo.DictData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author meizhuang team
 * @since 2019-01-16
 */
public interface ISysDbResourceService extends IService<SysDbResource> {
    /**
     * 获取数据源字典列表
     *
     * @return
     */
    List<DictData> getDictList();
}
