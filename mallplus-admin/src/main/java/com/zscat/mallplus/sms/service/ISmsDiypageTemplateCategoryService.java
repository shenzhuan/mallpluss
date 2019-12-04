package com.zscat.mallplus.sms.service;

import com.zscat.mallplus.sms.entity.SmsDiypageTemplateCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author mallplus
* @date 2019-12-04
*/

public interface ISmsDiypageTemplateCategoryService extends IService<SmsDiypageTemplateCategory> {

    Object selTemplateCategory();
}
