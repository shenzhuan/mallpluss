package com.mei.zhuang.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mei.zhuang.entity.marking.EsShopShareMember;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/27 17:23
 * @Version 1.0
 **/

public interface EsShopShareMemberService extends IService<EsShopShareMember> {

    EsShopShareMember selListOrderBy(EsShopShareMember entity);
}
