package com.zscat.mallplus.ums.service.impl;

import com.zscat.mallplus.sys.entity.SysStoreComment;
import com.zscat.mallplus.sys.mapper.SysStoreCommentMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.ums.service.ISysStoreCommentService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author mallplus
* @date 2020-04-22
*/
@Service
public class SysStoreCommentServiceImpl extends ServiceImpl<SysStoreCommentMapper, SysStoreComment> implements ISysStoreCommentService {

@Resource
private  SysStoreCommentMapper sysStoreCommentMapper;


}
