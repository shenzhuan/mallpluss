package com.mei.zhuang.service.sys.impl;

import com.arvato.admin.config.SysTenantInitConfig;
import com.arvato.admin.service.ISysTenantService;
import com.arvato.common.dto.SysTenantPagingData;
import com.arvato.common.dto.SysTenantPagingParam;
import com.arvato.common.msg.DictData;
import com.arvato.common.orm.dao.SysDbResourceMapper;
import com.arvato.common.orm.dao.SysTenantMapper;
import com.arvato.common.orm.model.SysDbResource;
import com.arvato.common.orm.model.SysTenant;
import com.arvato.utils.date.DateUtil;
import com.arvato.utils.util.ShellUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author arvato team
 * @since 2019-01-16
 */
@Service
@Slf4j
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantInitConfig sysTenantInitConfig;
    @Autowired
    private SysDbResourceMapper sysDbResourceMapper;

    @Override
    public int getPagingTotal(SysTenantPagingParam param) {
        return this.baseMapper.getPagingTotal(param);
    }

    @Override
    public List<SysTenantPagingData> getPagingList(SysTenantPagingParam param) {
        return this.baseMapper.getPagingList(param);
    }

    @Override
    public boolean schemaNameCheck(String schema) {
        return Pattern.matches("^[a-zA-Z0-9_]+$",schema);
    }

    @Override
    public List<DictData> getDictList() {
        return this.baseMapper.getDictList();
    }

    @Override
    public void initTenant(SysTenant sysTenant) {
        ShellUtil shellUtil = new ShellUtil(sysTenantInitConfig.getDbRemoteIp(),sysTenantInitConfig.getDbRemotePort(),sysTenantInitConfig.getDbRemoteAccount(),sysTenantInitConfig.getDbRemotePass());

        try {
            SysDbResource dbResource = sysDbResourceMapper.selectById(sysTenant.getDbResourceId());
            String tenantSchemaBak = String.format("%s/tenant_%s_%s_%s.bak",sysTenantInitConfig.getDbBakPath(),dbResource.getDbName(),sysTenant.getDbSchema(),DateUtil.format(new Date(),DateUtil.YYYYMMDDHHMMSS));
            String[] initCmd = new String[]{
                    String.format("cp %s/crm_template.bak %s",sysTenantInitConfig.getDbBakPath(),tenantSchemaBak),
                    String.format("sed -i \"s/#createSchemaCommand#/create schema \\\""+sysTenant.getDbSchema()+"\\\";/g\" %s",tenantSchemaBak),
                    String.format("sed -i \"s/public./"+sysTenant.getDbSchema()+"./g\" %s",tenantSchemaBak),
                    sysTenantInitConfig.getSchemaDbInitCommand()
                            .replace("#tenantDb",dbResource.getDbName())
                            .replace("#tenantSchemaBak",tenantSchemaBak)
            };
            log.info("sysTenant schema db init cmd:\n{}",(Object)initCmd);
            shellUtil.executeCommands(initCmd);
        }finally {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    shellUtil.disconnect();
                }
            },1000*60);
        }


    }
}
