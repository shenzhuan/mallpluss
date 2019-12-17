package com.zscat.mallplus.fenxiao.service.impl;

import com.zscat.mallplus.fenxiao.entity.FenxiaoRecords;
import com.zscat.mallplus.fenxiao.mapper.FenxiaoRecordsMapper;
import com.zscat.mallplus.fenxiao.service.IFenxiaoRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author mallplus
* @date 2019-12-17
*/
@Service
public class FenxiaoRecordsServiceImpl extends ServiceImpl
<FenxiaoRecordsMapper, FenxiaoRecords> implements IFenxiaoRecordsService {

@Resource
private  FenxiaoRecordsMapper fenxiaoRecordsMapper;


}
