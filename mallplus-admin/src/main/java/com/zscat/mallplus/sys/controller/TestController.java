package com.zscat.mallplus.sys.controller;


import com.zscat.mallplus.sys.service.GeneratorService;
import com.zscat.mallplus.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/test")
@RestController
public class TestController {
    String prefix = "common/generator";
    @Autowired
    GeneratorService generatorService;

    @ResponseBody
    @GetMapping("/list")
    Object list(String tableName) {
        List<Map<String, Object>> list = generatorService.list(tableName);
        for (Map<String, Object> map: list){
            System.out.println("alter table "+map.get("tableName")+" modify id int auto_increment;");
        }
        return new CommonResult().success(list);
    }


}
