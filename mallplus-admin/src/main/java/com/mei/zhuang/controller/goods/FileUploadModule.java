package com.mei.zhuang.controller.goods;


import com.mei.zhuang.controller.SysLog;
import com.mei.zhuang.dao.goods.EsShopGoodsImgMapper;
import com.mei.zhuang.entity.goods.EsCoreAttachment;
import com.mei.zhuang.service.goods.EsCoreAttachmentService;
import com.mei.zhuang.service.goods.EsShopGoodsImgService;
import com.mei.zhuang.util.OssAliyunUtil;
import com.mei.zhuang.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.*;

@Slf4j
@Api(value = "图片上传管理", description = "", tags = {"图片上传管理"})
@RestController
@RequestMapping("file")
public class FileUploadModule {


    @Resource
    private EsShopGoodsImgService esShopGoodsImgService;
    @Autowired
    OssAliyunUtil aliyunOSSUtil;

    @Resource
    private EsCoreAttachmentService esCoreAttachmentService;

    @SysLog(MODULE = "图片上传管理", REMARK = "上传")
    @ApiOperation("上传")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Object uploadImage(Long groupId, Long id, int type, @RequestPart("file") MultipartFile[] multipartFile) {
       List<BlobUpload> list = new ArrayList<>();
        if (multipartFile != null && multipartFile.length > 0) {
            for (int i = 0; i < multipartFile.length; i++) {
                String name =aliyunOSSUtil.upload(multipartFile[i]);
                EsCoreAttachment attachment = new EsCoreAttachment();
                attachment.setUid(id);
                attachment.setCategoryId(groupId);
                attachment.setUploadTime(new Date());
                attachment.setName(multipartFile[i].getOriginalFilename());
                attachment.setShopId(Long.parseLong("1"));
                attachment.setType("image");
                attachment.setImgAddress(name);
                esCoreAttachmentService.save(attachment);
                BlobUpload blobUploadEntity = new BlobUpload();
                blobUploadEntity.setFileName(multipartFile[i].getOriginalFilename());
                blobUploadEntity.setFileUrl(name);
                blobUploadEntity.setThumbnailUrl(name);
                list.add(blobUploadEntity);
            }
        }
        return new CommonResult().success(list);
    }

    @SysLog(MODULE = "图片上传管理", REMARK = "上传文件")
    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Object uploadFile(Long groupId, String id, Long uid, String fileType, int type, @RequestPart("file") MultipartFile multipartFile) {
        List<BlobUpload> list = new ArrayList<>();
        String fileExtension = getFileExtension(multipartFile.getOriginalFilename()).toLowerCase();
        if (multipartFile != null ) {
            if(!(fileType.equals("file") || fileType.equals("video"))){
                return new CommonResult().failed("文件类型错误");
            }
            if(fileType.equals("file")){
                if(!fileExtension.equals(".csv")){
                    return new CommonResult().failed("文件格式错误");
                }
            }
            if(fileType.equals("video")){
                if(!fileExtension.equals(".mp4")){
                    return new CommonResult().failed("视频格式错误");
                }
            }
                String name =aliyunOSSUtil.upload(multipartFile);
                EsCoreAttachment attachment = new EsCoreAttachment();
                attachment.setUid(uid);
                attachment.setCategoryId(groupId);
                attachment.setUploadTime(new Date());
                attachment.setName(multipartFile.getOriginalFilename());
                attachment.setShopId(Long.parseLong("1"));
                attachment.setType(fileType);
                attachment.setImgAddress(name);
                esCoreAttachmentService.save(attachment);
                return new CommonResult().success(attachment);

        }
        return new CommonResult().success(list);
    }




    private static ByteArrayInputStream getRandomDataStream(int length) {
        return new ByteArrayInputStream(getRandomBuffer(length));
    }

    private static byte[] getRandomBuffer(int length) {
        final Random randGenerator = new Random();
        final byte[] buff = new byte[length];
        randGenerator.nextBytes(buff);
        return buff;
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
