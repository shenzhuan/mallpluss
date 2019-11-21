package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.arvato.admin.biz.DictBiz;
import com.arvato.admin.orm.dao.CrmSysDictMapper;
import com.arvato.common.vo.returnformat.TableData;
import com.arvato.file_manage.util.BizResult;
import com.arvato.utils.annotation.SysLog;
import com.arvato.utils.constant.CommonConstant;
import com.mei.zhuang.entity.sys.CrmSysDict;
import com.mei.zhuang.entity.sys.CrmSysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "字典管理", description = "", tags = {"数据字典"})
@RestController
@RequestMapping("dict")
public class DictController extends BaseController {

    @Autowired
    private DictBiz dictBiz;
    @Autowired
    private CrmSysDictMapper sysDictMapper;

    @SysLog(MODULE = "字典管理", REMARK = "根据条件查询所有数据字典列表")
    @ApiOperation("根据条件查询所有数据字典列表")
    @GetMapping(value = "/dicts")
    public TableData<CrmSysDict> getUserByPage(CrmSysDict dict) {
        return new TableData<>()
                .total(dictBiz.selectDictCount(dict))
                .rows(dictBiz.selectDictList(dict));
    }

    @SysLog(MODULE = "字典管理", REMARK = "保存数据字典")
    @ApiOperation("保存数据字典")
    @PostMapping(value = "/dicts")
    public BizResult saveUser(CrmSysDict dict) {
        CrmSysUser currentUser = super.getCurrentUser();
        dict.setCreateUserId(currentUser.getId());
        return dictBiz.insertSelective(dict, currentUser);
    }

    @SysLog(MODULE = "字典管理", REMARK = "更新数据字典")
    @ApiOperation("更新数据字典")
    @PutMapping(value = "/dicts/{id}")
    public BizResult updateUser(CrmSysDict dict) {
        CrmSysUser currentUser = super.getCurrentUser();
        dict.setUpdateUserId(currentUser.getId());
        return dictBiz.updateSelectiveById(dict);
    }

    @SysLog(MODULE = "字典管理", REMARK = "删除数据字典")
    @ApiOperation("删除数据字典")
    @DeleteMapping(value = "/dicts/{id}")
    public BizResult deleteUser(@ApiParam("数据字典id") @PathVariable Integer id) {
        return dictBiz.deleteById(id);
    }

    @SysLog(MODULE = "字典管理", REMARK = "查询数据字典明细")
    @ApiOperation("查询数据字典明细")
    @GetMapping(value = "/dicts/{id}")
    public JSONObject getUserById(@ApiParam("数据字典id") @PathVariable Integer id) {
        return dictBiz.getDictById(id);
    }

    @SysLog(MODULE = "字典管理", REMARK = "从字典表查询积分类型")
    @ApiOperation("从字典表查询积分类型")
    @RequestMapping(value = "/pointType", method = RequestMethod.GET)
    public JSONObject pointType() {
        JSONObject result = new JSONObject();
        List<Map<String, String>> typeList = sysDictMapper.getDictList("crm_node_points", "type");
        result.put("code", CommonConstant.CODE_SUCCESS);
        result.put("data", typeList);
        return result;
    }

    @SysLog(MODULE = "字典管理", REMARK = "从字典表查询消息模板类型数据")
    @ApiOperation("从字典表查询消息模板类型数据")
    @RequestMapping(value = "/getMessageTemplateType", method = RequestMethod.GET)
    public JSONObject getMessageTemplateType() {
        JSONObject result = new JSONObject();
        List<Map<String, String>> messageTemplateType = sysDictMapper.getDictList("crm_message_template", "type");
        result.put("code", CommonConstant.CODE_SUCCESS);
        result.put("data", messageTemplateType);
        return result;
    }

    @SysLog(MODULE = "字典管理", REMARK = "从字典表中查询消息发送时间类型")
    @ApiOperation("从字典表中查询消息发送时间类型")
    @RequestMapping(value = "/dateRemindType", method = RequestMethod.GET)
    public JSONObject dateRemindType() {
        JSONObject result = new JSONObject();
        result.put("code", CommonConstant.CODE_SUCCESS);
        result.put("data", sysDictMapper.getDictList("crm_massmessage", "type"));
        return result;
    }

}
