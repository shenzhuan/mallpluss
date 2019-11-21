package com.mei.zhuang.service.sys.impl;

import com.arvato.admin.orm.dao.CrmHarassmentRightsMapper;
import com.arvato.admin.orm.dao.CrmPreventHarassmentConfigMapper;
import com.arvato.admin.service.ICrmPreventHarassmentConfigService;
import com.baomidou.mybatisplus.mapper.QueryWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.sys.CrmHarassmentRights;
import com.mei.zhuang.entity.sys.CrmPreventHarassmentConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author arvato team
 * @since 2018-08-28
 */
@Service
public class CrmPreventHarassmentConfigServiceImpl
		extends ServiceImpl<CrmPreventHarassmentConfigMapper, CrmPreventHarassmentConfig>
		implements ICrmPreventHarassmentConfigService {

	@Autowired
	private CrmHarassmentRightsMapper rightsMapper;

	@Override
	public void reset() {
		List<CrmPreventHarassmentConfig> configList = this
				.selectList(new QueryWrapper<>(new CrmPreventHarassmentConfig() {
					{
						this.setStatus("1");
					}
				}));

		Assert.isTrue(configList.size() > 0, "系统中没有启用的防骚扰配置");

		for (CrmPreventHarassmentConfig config : configList) {
			// 无会员 jump
			if (this.rightsMapper.selectCount(new QueryWrapper<>(new CrmHarassmentRights() {
				{
					this.setPreventHarassmentConfigId(config.getId());
				}
			})) == 0)
				continue;

			CrmHarassmentRights rights = new CrmHarassmentRights();
			rights.setResidue(config.getNumber());
			rights.setSmsUsedNum(0);
			rights.setWechatUsedNum(0);
			rights.setMailUsedNum(0);
			rights.setMmsUsedNum(0);
			Integer result = this.rightsMapper.update(rights, new QueryWrapper<>(new CrmHarassmentRights() {
				{
					this.setPreventHarassmentConfigId(config.getId());
				}
			}));

			Assert.isTrue(ObjectUtils.defaultIfNull(result, 0) > 0, "会员防骚扰次数重置失败");
		}
	}

	@Override
	public List<Map<String, Object>> getallstore() {
		List<Map<String, Object>> data = rightsMapper.getallstore();
		return data;
	}
}
