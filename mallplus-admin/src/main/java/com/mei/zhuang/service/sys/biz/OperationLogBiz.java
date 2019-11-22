package com.mei.zhuang.service.sys.biz;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mei.zhuang.dao.member.CrmOperationLogMapper;
import com.mei.zhuang.dao.sys.CrmSysLogTypeMapper;
import com.mei.zhuang.entity.sys.CrmSysLogType;
import com.mei.zhuang.vo.order.ExportParam;
import com.mei.zhuang.vo.sys.SysOpertionLogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/7/9.
 */
@Service
@Slf4j
public class OperationLogBiz {
    @Resource
    private CrmOperationLogMapper operationLogMapper;

    @Resource
    private CrmSysLogTypeMapper logTypeMapper;




    /**
     * 导出日志列表
     *
     * @param entity
     * @param exportParam
     * @param response
     * @return
     */
    public boolean exportLogList(SysOpertionLogParam entity, ExportParam exportParam, HttpServletResponse response) {

        return true;
    }

    /**
     * 获得操作类型集合
     * @return
     */
    public Object getLogTypeList() {
        return logTypeMapper.selectList(new QueryWrapper<>(new CrmSysLogType()));
    }

    public boolean insertModuleName(String[] strs, String belongModule) {

//        List<CrmSysLogType> list = new ArrayList<>();
        for(String item : strs){
            CrmSysLogType logType = new CrmSysLogType();
            logType.setOperationType(item.trim());
            logType.setDesc(item.trim());
            logType.setBelongModule(belongModule);
//            list.add(logType);
            logTypeMapper.insert(logType);

        }
        return true;
    }


}
