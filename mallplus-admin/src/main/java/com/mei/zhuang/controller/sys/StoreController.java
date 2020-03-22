package com.mei.zhuang.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.dao.sys.CrmStoreMapper;
import com.mei.zhuang.entity.sys.CrmSysUser;
import com.mei.zhuang.service.sys.biz.StoreBiz;
import com.mei.zhuang.vo.BaseResponse;
import com.mei.zhuang.vo.DictData;
import com.mei.zhuang.vo.sys.StoreDictParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Meng.Liu1@arvato.com
 * @Date: 2018/12/5 14:36
 * @Description: 渠道/门店
 * @version: V1.0
 */
@Api(description = "渠道/门店", tags = {"门店"})
@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController extends BaseController {

    @Resource
    private StoreBiz storeBiz;
    @Resource
    private CrmStoreMapper storeMapper;

    @SysLog(MODULE = "门店", REMARK = "门店列表")
    @ApiOperation("门店列表")
    @GetMapping("/list")
    public JSONObject storeList() {
        JSONObject result = new JSONObject();
        result.put("code", CommonConstant.CODE_SUCCESS);
        result.put("data", storeBiz.getStoreList());
        return result;
    }

    @SysLog(MODULE = "门店", REMARK = "门店字典")
    @ApiOperation("门店字典")
    @GetMapping("/dict")
    public BaseResponse<List<DictData>> storeList(StoreDictParam param) {
        CrmSysUser currentUser = super.getCurrentUser();
        List<Integer> deptIds = null;
        if (currentUser != null) {
            Integer userId = currentUser.getId();
            deptIds = getDeptIds(param.getMenuId(), userId);
        }

        return BaseResponse.successResponnse(storeMapper.getStoreDict(param, deptIds));
    }
}