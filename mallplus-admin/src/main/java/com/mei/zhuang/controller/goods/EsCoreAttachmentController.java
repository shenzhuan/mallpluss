package com.mei.zhuang.controller.goods;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.entity.goods.EsCoreAttachment;
import com.mei.zhuang.service.goods.EsCoreAttachmentService;
import com.mei.zhuang.utils.ValidatorUtils;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.Random;

@Slf4j
@Api(value = "文件管理", description = "", tags = {"文件管理"})
@RestController
@RequestMapping("/api/file")
public class EsCoreAttachmentController {


    //设置缩略图的宽高
    private static int thumbnailWidth = 150;
    private static int thumbnailHeight = 100;
    @Resource
    private EsCoreAttachmentService esCoreAttachmentService;
    private String defaultEndpointsProtocol = "https";
    private String blobEndpoint = "https://hrecminiprogramstorage.blob.core.chinacloudapi.cn/";
    private String queueEndpoint = "https://hrecminiprogramstorage.queue.core.chinacloudapi.cn/";
    private String tableEndpoint = "https://hrecminiprogramstorage.table.core.chinacloudapi.cn/";

    private static ByteArrayInputStream getRandomDataStream(int length) {
        return new ByteArrayInputStream(getRandomBuffer(length));
    }

    private static byte[] getRandomBuffer(int length) {
        final Random randGenerator = new Random();
        final byte[] buff = new byte[length];
        randGenerator.nextBytes(buff);
        return buff;
    }

    @SysLog(MODULE = "文件管理", REMARK = "查询文件列表")
    @ApiOperation("查询文件列表")
    @PostMapping("/selPageList")
    public Object selPageList(EsCoreAttachment entity) {
        if (entity.getType() != null && entity.getType().equals("image")) {
            if (entity.getCategoryId() == null) {
                entity.setCategoryId(Long.parseLong("1"));
            }
        }

        PageHelper.startPage(entity.getCurrent(), entity.getSize());
        // List<EsShopFullGift> esShopDiscount = fullGiftService.slelectPurchase2();
        return new CommonResult().success(PageInfo.of(esCoreAttachmentService.list(new QueryWrapper<>(entity))));
    }

    @SysLog(MODULE = "文件管理", REMARK = "删除文件")
    @ApiOperation("删除文件")
    @PostMapping("/delete")
    public Object delete(@RequestParam("id") Long id) {
        if (ValidatorUtils.empty(id)) {
            return new CommonResult().failed("请指定编号");
        }
        return new CommonResult().success("success", esCoreAttachmentService.removeById(id));
    }

    private String getFileExtension(String fileName) {
        int position = fileName.indexOf('.');
        if (position > 0) {
            String temp = fileName.substring(position);
            return temp;
        }
        return "";
    }

    private String getBlobPreName(int fileType, Boolean thumbnail) {
        String afterName = "";
        if (thumbnail) {
            afterName = "thumbnail/";
        }

        switch (fileType) {
            case 1:
                return "logo/" + afterName;
            case 2:
                return "food/" + afterName;
            case 3:
                return "head/" + afterName;
            case 4:
                return "ads/" + afterName;
            case 5:
                return "file/" + afterName;
            default:
                return "";
        }
    }


}
