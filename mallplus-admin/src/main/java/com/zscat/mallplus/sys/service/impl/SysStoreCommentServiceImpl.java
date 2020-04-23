package com.zscat.mallplus.sys.service.impl;

import com.zscat.mallplus.sys.entity.SysStoreComment;
import com.zscat.mallplus.sys.mapper.SysStoreCommentMapper;
import com.zscat.mallplus.sys.service.ISysStoreCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
