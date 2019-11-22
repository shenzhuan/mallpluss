package com.mei.zhuang.service.sys.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mei.zhuang.dao.sys.SysDbResourceMapper;
import com.mei.zhuang.dao.sys.SysTenantMapper;
import com.mei.zhuang.entity.sys.SysTenant;
import com.mei.zhuang.service.sys.ISysTenantService;
import com.mei.zhuang.vo.DictData;
import com.mei.zhuang.vo.sys.SysTenantPagingData;
import com.mei.zhuang.vo.sys.SysTenantPagingParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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

  //  @Resource
   // private SysTenantInitConfig sysTenantInitConfig;
    @Resource
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
        /*ShellUtil shellUtil = new ShellUtil(sysTenantInitConfig.getDbRemoteIp(),sysTenantInitConfig.getDbRemotePort(),sysTenantInitConfig.getDbRemoteAccount(),sysTenantInitConfig.getDbRemotePass());

        try {
            SysDbResource dbResource = sysDbResourceMapper.selectById(sysTenant.getDbResourceId());
            String tenantSchemaBak = String.format("%s/tenant_%s_%s_%s.bak",sysTenantInitConfig.getDbBakPath(),dbResource.getDbName(),sysTenant.getDbSchema(), DateUtil.format(new Date(),DateUtil.YYYYMMDDHHMMSS));
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
        }*/


    }
}
