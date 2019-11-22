package com.mei.zhuang.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.SysDbResource;
import com.mei.zhuang.vo.DictData;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
public interface SysDbResourceMapper extends BaseMapper<SysDbResource> {
    /**
     * 获取数据源字典列表
     *
     * @return
     */
    List<DictData> getDictList();
}
