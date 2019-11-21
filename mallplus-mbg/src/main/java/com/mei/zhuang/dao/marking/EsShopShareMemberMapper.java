package com.mei.zhuang.dao.marking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mei.zhuang.entity.marking.EsShopShareMember;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/26 15:53
 * @Version 1.0
 **/

public interface EsShopShareMemberMapper extends BaseMapper<EsShopShareMember> {

    EsShopShareMember selListOrderBy(EsShopShareMember entity);
}
