package com.mei.zhuang.service.sys.biz;

import com.mei.zhuang.dao.sys.CrmApiInterfaceMapper;
import com.mei.zhuang.vo.ApiInterfaceVo;
import com.mei.zhuang.vo.ZTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ApiInterfaceBiz {

    @Resource
    private CrmApiInterfaceMapper apiInterfaceMapper;

    public Map<String, Object> getInterfaceList(String name, Integer typeId, Integer apiUserId, Integer limit, Integer offset, List<Integer> deptIds) {
        Map<String, Object> result = new HashMap<>();
        List<ApiInterfaceVo> list = this.apiInterfaceMapper.list(name, typeId, apiUserId, limit, offset, deptIds);
        int count = this.apiInterfaceMapper.count(name, typeId, apiUserId, deptIds);
        result.put("rows", list);
        result.put("total", count);
        return result;
    }

    /**
     * 获取接口所有类型
     *
     * @return
     */
    public List<ZTreeNode> getAllTypes() {
        return apiInterfaceMapper.getAllTypes();
    }
}
