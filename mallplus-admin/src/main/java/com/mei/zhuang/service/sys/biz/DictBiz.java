package com.mei.zhuang.service.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.dao.sys.CrmSysDictMapper;
import com.mei.zhuang.entity.sys.CrmSysDict;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.utils.DateUtil;
import com.mei.zhuang.vo.BizResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DictBiz {

    private final CrmSysDictMapper crmSysDictMapper;

    @Autowired
    public DictBiz(CrmSysDictMapper sysDictMapper) {
        this.crmSysDictMapper = sysDictMapper;
    }
    /**
     * 新增用户
     * @param entity 新增用户实体
     * @param currentUser 当前登录用户对象
     * @return 操作成功失败信息
     */
    @Transactional
    public BizResult insertSelective(CrmSysDict entity, CrmSysUser currentUser) {
        BizResult bizResult = new BizResult();
        try {
            crmSysDictMapper.insert(entity);
            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("添加字典成功");
        } catch (Exception e) {
            log.info("添加字典失败", e);
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("添加字典失败");
        }
        return bizResult;
    }
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    public JSONObject getDictById(int id) {
        JSONObject json = new JSONObject();
        CrmSysDict crmSysDict = crmSysDictMapper.selectById(id);
        json.put("dictInfo", crmSysDict);
        return json;
    }
    /**
     * 根据ID更新用户信息
     * @param entity 用户实体
     */
    public BizResult updateSelectiveById(CrmSysDict entity) {
        BizResult bizResult = new BizResult();
        try {
            crmSysDictMapper.updateById(entity);
            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("更新字典信息成功");
        } catch (Exception e) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("更新字典信息失败");
        }
        return bizResult;
    }
    /**
     * 根据ID删除用户
     * 删除用户关联的角色数据
     * @param id 用户ID
     */
    @Transactional
    public BizResult deleteById(int id) {
        BizResult bizResult = new BizResult();
        try {
            crmSysDictMapper.deleteById(id);
            bizResult.setCode(CommonConstant.CODE_SUCCESS);
            bizResult.setMsg("删除字典成功");
        } catch (Exception e) {
            bizResult.setCode(CommonConstant.CODE_BIZ_ERROR);
            bizResult.setMsg("删除字典失败");
        }
        return bizResult;
    }
    /**
     * 根据条件获取用户数量
     * @return 用户数量
     */
    public int selectDictCount(CrmSysDict dict) {
        return crmSysDictMapper.selectDictCount(dict);
    }
    /**
     * 根据条件获取用户列表
     * @return 用户集合
     */
    public List<CrmSysDict> selectDictList(CrmSysDict dict) {
        List<CrmSysDict> list = crmSysDictMapper.selectDictList(dict);
        for (CrmSysDict crmSysDict : list) {
        	crmSysDict.setCreateDate(DateUtil.format(crmSysDict.getCreateDate(),DateUtil.YYYYMMDD,DateUtil.YYYY_MM_DD));
        }
        return list;
    }
}
