package com.mei.zhuang.controller.order;

import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Auther: Tiger
 * @Date: 2019-06-14 13:18
 * @Description:
 */
@Slf4j
@Api(value = "读取服务器文件管理", description = "", tags = {"读取服务器文件管理"})
@RestController
@RequestMapping("/api/read/file")
public class ReadFileController {

    @SysLog(MODULE = "读取服务器文件管理", REMARK = "读取文件")
    @ApiOperation("读取文件")
    @PostMapping("/readCss")
    public Object readCssFile(@ApiParam("URL地址") @RequestParam String URL){
       StringBuffer sb = new StringBuffer();
       URLConnection connection = null;
       BufferedReader br = null;
       try {
           //http://sit-admin.ec.widiazine.cn/font/iconfont.css
           if(URL == null || "".equals(URL)){
               return new CommonResult().paramFailed("URL参数不能为空");
           }
           URL url = new URL(URL);
           connection = url.openConnection();
           InputStream is = connection.getInputStream();
           br = new BufferedReader(new InputStreamReader(is,"UTF-8"));//gb2312
           String temp = null;
           while((temp = br.readLine()) != null){
               sb.append(temp);
           }
           System.out.println(sb.toString());
           return new CommonResult().success(sb.toString());
        } catch (Exception e) {
            log.error("读取文件：%s", e.getMessage(), e);
        }finally{
            try{
                if(br != null){
                    br.close();
                }
            }catch(Exception e){
                log.error("关闭流失败");
            }
        }
        return new CommonResult().failed("读取文件失败!");
    }


}
