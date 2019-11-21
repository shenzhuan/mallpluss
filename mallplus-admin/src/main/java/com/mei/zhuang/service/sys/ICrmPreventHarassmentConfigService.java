package com.mei.zhuang.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.sys.CrmPreventHarassmentConfig;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author arvato team
 * @since 2018-08-28
 */
public interface ICrmPreventHarassmentConfigService extends IService<CrmPreventHarassmentConfig> {
	/**
	 * 会员防骚扰设置重置
	 */
	void reset();

	List<Map<String, Object>> getallstore();
}
