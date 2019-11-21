package com.mei.zhuang.dao.sys;

import com.arvato.admin.vo.ApiInterfaceVo;
import com.arvato.common.vo.ZTreeNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.sys.CrmApiInterface;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author arvato team
 * @since 2017-12-07
 */
public interface CrmApiInterfaceMapper extends BaseMapper<CrmApiInterface> {
    /**
     * 根据uri查找用户接口id
     * @param uri
     * @return
     */
    Integer selectInterfaceByUri(@Param("uri") String uri);

    List<ApiInterfaceVo> list(@Param("name") String name, @Param("typeId") Integer typeId, @Param("apiUserId") Integer apiUserId,
                              @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("deptIds") List<Integer> deptIds);

    int count(@Param("name") String name, @Param("typeId") Integer typeId, @Param("apiUserId") Integer apiUserId, @Param("deptIds") List<Integer> deptIds);

    /**
     * 获取所有接口类型
     * @return
     */
    List<ZTreeNode> getAllTypes();
}
