package com.mei.zhuang.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.order.EsCoreLog;
import com.mei.zhuang.entity.order.EsCoreLogType;
import com.mei.zhuang.vo.order.EsCoreLogParam;
import com.mei.zhuang.vo.order.ExportParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: Tiger
 * @Date: 2019-06-21 15:59
 * @Description:
 */
public interface IShopLogService extends IService<EsCoreLog> {

    Page<EsCoreLog> selecPageList(EsCoreLogParam param);

    boolean exportLogList(EsCoreLogParam entity, ExportParam exportParam, HttpServletResponse response);

    List<EsCoreLogType> getLogTypeList();


}
