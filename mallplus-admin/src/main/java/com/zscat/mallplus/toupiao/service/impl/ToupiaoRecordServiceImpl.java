package com.zscat.mallplus.toupiao.service.impl;

import com.zscat.mallplus.toupiao.entity.ToupiaoRecord;
import com.zscat.mallplus.toupiao.mapper.ToupiaoRecordMapper;
import com.zscat.mallplus.toupiao.service.IToupiaoRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
* @author mallplus
* @date 2019-12-17
*/
@Service
public class ToupiaoRecordServiceImpl extends ServiceImpl
<ToupiaoRecordMapper, ToupiaoRecord> implements IToupiaoRecordService {

@Resource
private  ToupiaoRecordMapper toupiaoRecordMapper;


}
