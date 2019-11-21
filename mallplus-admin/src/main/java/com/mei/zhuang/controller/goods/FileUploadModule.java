package com.mei.zhuang.controller.goods;

import com.arvato.ec.common.constant.CoreConfigConstant;
import com.arvato.service.goods.api.azureoss.BlobHelper;
import com.arvato.service.goods.api.azureoss.BlobUpload;
import com.arvato.service.goods.api.azureoss.MyUtils;
import com.arvato.service.goods.api.config.StorageConfig;
import com.arvato.service.goods.api.orm.dao.CoreSettingsMapper;
import com.arvato.service.goods.api.service.EsShopGoodsImgService;
import com.arvato.utils.CommonResult;
import com.arvato.utils.annotation.SysLog;
import com.mei.zhuang.entity.order.CoreConfig;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
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

    @Autowired
    private StorageConfig storageConfig;
    @Resource
    private EsShopGoodsImgService esShopGoodsImgService;
    @Resource
    private CoreSettingsMapper settingsMapper;

    //设置缩略图的宽高
    private static int thumbnailWidth = 150;
    private static int thumbnailHeight = 100;

    private String defaultEndpointsProtocol = "https";
    private String blobEndpoint = "https://hrecminiprogramstorage.blob.core.chinacloudapi.cn/";
    private String queueEndpoint = "https://hrecminiprogramstorage.queue.core.chinacloudapi.cn/";
    private String tableEndpoint = "https://hrecminiprogramstorage.table.core.chinacloudapi.cn/";

    @SysLog(MODULE = "图片上传管理", REMARK = "上传")
    @ApiOperation("上传")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Object uploadImage(Long groupId, String id, int type, @RequestPart("file") MultipartFile[] multipartFile) {
        List<BlobUpload> blobUploadEntities = new ArrayList<BlobUpload>();

        if (multipartFile != null) {
            String blobStorageConnectionString = String.format("DefaultEndpointsProtocol=%s;"
                            + "BlobEndpoint=%s;"
                            + "QueueEndpoint=%s;"
                            + "TableEndpoint=%s;"
                            + "AccountName=%s;"
                            + "AccountKey=%s",
                    storageConfig.getDefaultEndpointsProtocol(), storageConfig.getBlobEndpoint(),
                    storageConfig.getQueueEndpoint(), storageConfig.getTableEndpoint(),
                    storageConfig.getAccountName(), storageConfig.getAccountKey());
            CoreConfig coreConfig = settingsMapper.selectByKey(CoreConfigConstant.azureblob);
            if (coreConfig != null) {
                JSONObject jsonObject = JSONObject.fromObject(coreConfig.getValue());
                blobStorageConnectionString = String.format("DefaultEndpointsProtocol=%s;"
                                + "BlobEndpoint=%s;"
                                + "QueueEndpoint=%s;"
                                + "TableEndpoint=%s;"
                                + "AccountName=%s;"
                                + "AccountKey=%s",
                        defaultEndpointsProtocol, blobEndpoint,
                        queueEndpoint, tableEndpoint,
                        jsonObject.get("accountName"), jsonObject.get("accountKey"));
                id = jsonObject.get("block").toString();
            }
            //获取或创建container
            try {
                CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(id.toLowerCase(), blobStorageConnectionString);
                if (multipartFile != null && multipartFile.length > 0) {
                    for (int i = 0; i < multipartFile.length; i++) {
                        MultipartFile tempMultipartFile = multipartFile[i];
                        if (tempMultipartFile.getSize() >= 1048576) {
                            return new CommonResult().failed("文件过大");
                        }
                        if (!tempMultipartFile.isEmpty()) {

                            //过滤非jpg,png格式的文件
                               /* if (!(tempMultipartFile.getContentType().toLowerCase().equals("image/jpg")
                                        || tempMultipartFile.getContentType().toLowerCase().equals("image/jpeg")
                                        || tempMultipartFile.getContentType().toLowerCase().equals("image/png"))) {
                                    return new CommonResult().failed("只能上传jpg，png，jpeg");
                                }*/
                            //拼装blob的名称(前缀名称+文件的md5值+文件扩展名称)
                            String checkSum = MyUtils.getMD5(tempMultipartFile.getInputStream());
                            String fileExtension = getFileExtension(tempMultipartFile.getOriginalFilename()).toLowerCase();
                            String preName = getBlobPreName(type, false).toLowerCase();
                            String blobName = preName + checkSum + fileExtension;

                            //设置文件类型，并且上传到azure blob
                            CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
                            blob.getProperties().setContentType(tempMultipartFile.getContentType());
                            blob.upload(tempMultipartFile.getInputStream(), tempMultipartFile.getSize());

                            //生成缩略图，并上传至AzureStorage
                            BufferedImage img = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
                            img.createGraphics().drawImage(ImageIO.read(tempMultipartFile.getInputStream()).getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0, null);
                            ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                            ImageIO.write(img, "jpg", thumbnailStream);
                            InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());

                            String thumbnailPreName = getBlobPreName(type, true).toLowerCase();
                            String thumbnailCheckSum = MyUtils.getMD5(new ByteArrayInputStream(thumbnailStream.toByteArray()));
                            String blobThumbnail = thumbnailPreName + thumbnailCheckSum + ".jpg";
                            CloudBlockBlob thumbnailBlob = blobContainer.getBlockBlobReference(blobThumbnail);
                            thumbnailBlob.getProperties().setContentType("image/jpeg");
                            thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

                            //将上传后的图片URL返回
                            BlobUpload blobUploadEntity = new BlobUpload();
                            blobUploadEntity.setFileName(tempMultipartFile.getOriginalFilename());
                            blobUploadEntity.setFileUrl(blob.getUri().toString());
                            blobUploadEntity.setThumbnailUrl(thumbnailBlob.getUri().toString());

                            blobUploadEntities.add(blobUploadEntity);

                            for (BlobUpload blobs : blobUploadEntities) {
                                Integer count = esShopGoodsImgService.insImg(groupId, blobs.getFileUrl(), blobs.getFileName());
                            }
                        }
                    }

                    return new CommonResult().success(blobUploadEntities);
                } else {
                    return new CommonResult().failed("文件为空");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new CommonResult().failed(2, "文件上传失败");
            }
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "图片上传管理", REMARK = "上传文件")
    @ApiOperation("上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Object uploadFile(Long groupId, String id, int type, @RequestPart("file") MultipartFile[] multipartFile) {
        List<BlobUpload> blobUploadEntities = new ArrayList<BlobUpload>();
        CloudBlockBlob blob = null;
        if (multipartFile != null) {
            Init init = new Init(id).invoke();
            id = init.getId();
            String blobStorageConnectionString = init.getBlobStorageConnectionString();
            //获取或创建container
            CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(id.toLowerCase(), blobStorageConnectionString);
            if (multipartFile != null && multipartFile.length > 0) {
                for (int i = 0; i < multipartFile.length; i++) {
                    MultipartFile tempMultipartFile = multipartFile[i];
                    if (tempMultipartFile.getSize() >= 1048576) {
                        return new CommonResult().failed("文件过大");
                    }
                    if (!tempMultipartFile.isEmpty()) {
                        try {

                            //拼装blob的名称(前缀名称+文件的md5值+文件扩展名称)
                            String checkSum = MyUtils.getMD5(tempMultipartFile.getInputStream());
                            String fileExtension = getFileExtension(tempMultipartFile.getOriginalFilename()).toLowerCase();
                            String preName = getBlobPreName(5, false).toLowerCase();
                            String blobName = preName + checkSum + fileExtension;

                            //设置文件类型，并且上传到azure blob
                            blob = blobContainer.getBlockBlobReference(blobName);
                            blob.getProperties().setContentType(tempMultipartFile.getContentType());
                            blob.upload(tempMultipartFile.getInputStream(), tempMultipartFile.getSize());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new CommonResult().failed("文件为空");
                        }

                    }
                }
                return new CommonResult().success(blob.getUri());
            } else {
                return new CommonResult().failed("文件为空");
            }
        }
        return new CommonResult().failed();
    }

    // For small size video, we can use this method.
    private static void UploadSmallSize(String videoPath, CloudBlobContainer container) throws URISyntaxException, StorageException, FileNotFoundException, IOException, InvalidKeyException {
        System.out.println("===============Begin Uploading===============");
        File source = new File(videoPath);
        String blobName = source.getName();
        CloudBlockBlob blob = container.getBlockBlobReference(source.getName());

        blob.upload(new FileInputStream(source), source.length());
        System.out.println("===============Uploading Done===============");
        System.out.println("Blob URL: " + GetBlobUrl(container, blobName));
    }

    private static String GetBlobUrl(CloudBlobContainer container, String blobName) throws StorageException, InvalidKeyException, URISyntaxException {
        SharedAccessBlobPolicy policy = new SharedAccessBlobPolicy();
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(new Date());

        // Immediately applicable
        policy.setSharedAccessStartTime(calendar.getTime());

        // Applicable time span is 1 hour
        calendar.add(Calendar.HOUR, 1);
        policy.setSharedAccessExpiryTime(calendar.getTime());

        // SAS grants READ access privileges
        policy.setPermissions(EnumSet.of(SharedAccessBlobPermissions.READ));
        BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

        // Private blob-container with no access for anonymous users
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.OFF);
        container.uploadPermissions(containerPermissions);
        String sas = container.generateSharedAccessSignature(policy, null);

        CloudBlockBlob blob = container.getBlockBlobReference(blobName);
        String blobUri = blob.getUri().toString();

        return blobUri + "?" + sas;
    }

    private static void UploadLargeSize(String videoPath, CloudBlobContainer container) throws URISyntaxException, StorageException, IOException, InvalidKeyException {
        File source = new File(videoPath);
        String blobName = source.getName();
        FileInputStream inputStream = new FileInputStream(source);
        final int blockLength = 1 * 1024 * 1024;
        byte[] bufferBytes = new byte[blockLength];
        int blockCount = (int) (source.length() / blockLength) + 1;
        System.out.println("Total block count：" + blockCount + ", Total size: " + source.length());
        int currentBlockSize = 0;

        CloudBlockBlob blockBlobRef = container.getBlockBlobReference(blobName);

        System.out.println("===============Begin Uploading===============");
        ArrayList<BlockEntry> blockList = new ArrayList<BlockEntry>();

        for (int i = 0; i < blockCount; i++) {

            String blockID = String.format("%08d", i);
            currentBlockSize = blockLength;
            if (i == blockCount - 1) {
                currentBlockSize = (int) (source.length() - blockLength * i);
                bufferBytes = new byte[currentBlockSize];
            }

            inputStream.read(bufferBytes, 0, currentBlockSize);
            blockBlobRef.uploadBlock(blockID, getRandomDataStream(blockLength), blockLength, null, null, null);
            blockList.add(new BlockEntry(blockID, BlockSearchMode.LATEST));
            System.out.println("Submitted block index：" + i + ", BlockIndex:" + blockID);
        }
        blockBlobRef.commitBlockList(blockList);
        System.out.println("===============Uploading Done===============");


        System.out.println("Blob URL: " + GetBlobUrl(container, blobName));
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

    private class Init {
        private String id;
        private String blobStorageConnectionString;

        public Init(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getBlobStorageConnectionString() {
            return blobStorageConnectionString;
        }

        public Init invoke() {
            blobStorageConnectionString = String.format("DefaultEndpointsProtocol=%s;"
                            + "BlobEndpoint=%s;"
                            + "QueueEndpoint=%s;"
                            + "TableEndpoint=%s;"
                            + "AccountName=%s;"
                            + "AccountKey=%s",
                    storageConfig.getDefaultEndpointsProtocol(), storageConfig.getBlobEndpoint(),
                    storageConfig.getQueueEndpoint(), storageConfig.getTableEndpoint(),
                    storageConfig.getAccountName(), storageConfig.getAccountKey());
            CoreConfig coreConfig = settingsMapper.selectByKey(CoreConfigConstant.azureblob);
            if (coreConfig != null) {
                JSONObject jsonObject = JSONObject.fromObject(coreConfig.getValue());
                blobStorageConnectionString = String.format("DefaultEndpointsProtocol=%s;"
                                + "BlobEndpoint=%s;"
                                + "QueueEndpoint=%s;"
                                + "TableEndpoint=%s;"
                                + "AccountName=%s;"
                                + "AccountKey=%s",
                        defaultEndpointsProtocol, blobEndpoint,
                        queueEndpoint, tableEndpoint,
                        jsonObject.get("accountName"), jsonObject.get("accountKey"));
                id = jsonObject.get("block").toString();
            }
            return this;
        }
    }
}
