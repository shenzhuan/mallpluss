package com.mei.zhuang.controller.sys;

import com.arvato.admin.orm.dao.CrmSysUserMapper;
import com.arvato.admin.service.ICrmSysDeptService;
import com.arvato.common.dto.DataSourceDto;
import com.arvato.utils.constant.CommonConstant;
import com.arvato.utils.util.UserNameDecodeUtil;
import com.mei.zhuang.entity.sys.CrmSysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @Description 基础Controller
 * @author arvatoTeam
 * @create 2017-06-15 8:48
 */
@Slf4j
public class BaseController {
	@Resource
	protected HttpServletRequest request;
	@Resource
	private ICrmSysDeptService crmSysDeptService;
	@Resource
	protected CrmSysUserMapper sysUserMapper;

	/**
	 * 获取当前用户名
	 * @return
	 */
	public String getCurrentUserName() throws UnsupportedEncodingException {
		String userName = UserNameDecodeUtil.getDecodeUserName(request);
	    return userName;
	}

	public Integer getCurrentPlatUserId(){
		String platUserId = request.getHeader(CommonConstant.CONTEXT_KEY_USER_ID);
		if(!StringUtils.isEmpty(platUserId)){
			return Integer.parseInt(platUserId);
		}
		return  0;
	}

	public String getCurrentSchema(){
		return request.getHeader(CommonConstant.TENANT_SCHEMA);
	}

	public DataSourceDto getDataSourceDto(){
		String dbName = request.getHeader(CommonConstant.TENANT_DB_NAME);
		String schema = request.getHeader(CommonConstant.TENANT_SCHEMA);
		String tenantId = request.getHeader(CommonConstant.CURR_TENANT_ID);
		return new DataSourceDto(dbName,schema).setTenantId(Integer.parseInt(tenantId));
	}

	/**
	 * 获取有数据权限的部门ID
	 * @param menuId 菜单ID
	 * @param userId 用户ID
	 * @return 有数据权限的部门ID集合
	 */
	public List<Integer> getDeptIds(Integer menuId, Integer userId){
		if(menuId == null) return null;
		CrmSysUser crmSysUser = this.getCurrentUser(userId);
		List<Integer> deptIds = crmSysDeptService.getDeptIds(menuId,crmSysUser);
		return deptIds;
	}

	public CrmSysUser getCurrentUser(){
		String userName = UserNameDecodeUtil.getDecodeUserName(request);
		if (StringUtils.isEmpty(userName)) {
			return null;
		}
		return getCurrentUser(userName);
	}

	protected CrmSysUser getCurrentUser(String username){
		if(username==null)return null;
		try{
			username = URLDecoder.decode(username,"utf-8");
		}catch (Exception e){
			log.error("",e);
		}
		return this.sysUserMapper.selectByUsername(username);
	}

	protected CrmSysUser getCurrentUser(Integer id){
		if(id==null)return null;
		return this.sysUserMapper.selectById(id);
	}

	public Integer getCurrentMenuId(HttpServletRequest request){
		String menuId = request.getParameter("currentMenuId");
		if(StringUtils.isEmpty(menuId)){
			return null;
		}
		return Integer.parseInt(menuId);
	}
}
