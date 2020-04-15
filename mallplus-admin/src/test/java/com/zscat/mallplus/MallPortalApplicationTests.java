package com.zscat.mallplus;

import com.zscat.mallplus.sys.entity.SysAdminLog;
import com.zscat.mallplus.sys.mapper.GeneratorMapper;
import com.zscat.mallplus.sys.mapper.SysAdminLogMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest(classes = MallAdminApplication.class)
@RunWith(SpringRunner.class)
@Log4j2
public class MallPortalApplicationTests {

    @Resource
    SysAdminLogMapper sysAdminLogMapper;
    @Resource
    GeneratorMapper generatorMapper;
    @Test
    public void contextLoads() {
        List<Map<String, Object>>  list=  generatorMapper.list();
        for (Map<String, Object> map :list){
            System.out.println(map);
        }
    }

    @Test
    public void delete() {

        List<SysAdminLog> dd = new ArrayList<>();
        SysAdminLog q = new SysAdminLog();
        q.setId(6L);
        q.setCreateTime(new Date());
        dd.add(q);

        SysAdminLog q1 = new SysAdminLog();
        q1.setId(7L);
        q1.setCreateTime(new Date());
        dd.add(q1);

        SysAdminLog q2 = new SysAdminLog();
        q2.setId(8L);
        q2.setCreateTime(new Date());
        dd.add(q2);
        List<SysAdminLog> ll1 = sysAdminLogMapper.selectBatchIds(dd);
        for (SysAdminLog log1 : ll1) {
            System.out.println(log1.getMethod());
        }
        sysAdminLogMapper.deleteBatchIds(dd);
        List<SysAdminLog> ll = sysAdminLogMapper.selectBatchIds(dd);
        for (SysAdminLog log : ll) {
            System.out.println(log.getMethod());
        }
    }
}
