package com.mei.zhuang.service.marking.impl;

import com.arvato.service.marking.api.orm.dao.EsShopShareMemberMapper;
import com.arvato.service.marking.api.service.EsShopShareMemberService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mei.zhuang.entity.marking.EsShopShareMember;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description TODO
 * @Author wanglei
 * @Date 2019/8/27 17:23
 * @Version 1.0
 **/
@Service
public class EsShopShareMemberServiceImpl extends ServiceImpl<EsShopShareMemberMapper, EsShopShareMember> implements EsShopShareMemberService {

    @Resource
    private EsShopShareMemberMapper esShopShareMemberMapper;

    @Override
    public EsShopShareMember selListOrderBy(EsShopShareMember entity) {
        return esShopShareMemberMapper.selListOrderBy(entity);
    }
}
