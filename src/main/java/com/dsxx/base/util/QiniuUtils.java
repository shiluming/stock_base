package com.dsxx.base.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云工具类
 *
 * @author slm
 * @date 2018/9/12
 */
public class QiniuUtils {
    private static final String ACCESS_KEY = "A8SIAnj_MoLaBFRnPlmdCi78eLSUdY57VbMgFJZy";
    private static final String SECRET_KEY = "dMS_sXCxuoRzvQU6eRvk1zETOWH21IQo2p1RRIsS";
    private static final String BUCKET_NAME = "cdt-images";
    private static final String RES_SERVER = "https://qiniu-images.imdsxx.com/";

    private static Auth auth;
    private static Zone zone;
    private static Configuration configuration;
    private static UploadManager uploadManager;

    static {
        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        zone = Zone.autoZone();
        configuration = new Configuration(zone);
        uploadManager = new UploadManager(configuration);
    }

    /**
     * 上传文件到七牛云
     *
     * @param localFilePath
     * @return 访问该文件的链接
     * @throws QiniuException
     * @author slm
     * @date 2018/09/13
     */
    public static String uploadFile(File localFilePath) throws QiniuException {
        String upToken = auth.uploadToken(BUCKET_NAME);
        Response response = uploadManager.put(localFilePath, localFilePath.getName(), upToken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return RES_SERVER + putRet.key;
    }

    /**
     * 上传流到七牛云
     *
     * @param inputStream stream
     * @param fileName    文件名
     * @param params      自定义参数，如 params.put("x:foo", "foo")
     * @param mime        指定文件mimetype
     * @return 访问该文件的链接
     * @throws QiniuException
     * @author slm
     * @date 2018/09/13
     */
    public static String uploadStream(InputStream inputStream, String fileName, StringMap params, String mime)
            throws QiniuException {
        String upToken = auth.uploadToken(BUCKET_NAME);
        Response response = uploadManager.put(inputStream, fileName, upToken, params, mime);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return RES_SERVER + putRet.key;
    }

    /**
     * 上传流到七牛云
     *
     * @param inputStream stream
     * @param fileName    文件名
     * @return 访问该文件的链接
     * @throws QiniuException
     * @author slm
     * @date 2018/09/13
     */
    public static String uploadStream(InputStream inputStream, String fileName)
            throws QiniuException {
        return uploadStream(inputStream, fileName, null, null);
    }

    /**
     * 上传BufferedImage到七牛云
     * @param image BufferedImage
     * @param fileName 文件名
     * @param imgFormat 图片格式
     * @return 访问该图片的链接
     * @throws IOException
     * @author slm
     * @date 2018/09/13
     */
    public static String uploadBufferedImage(BufferedImage image, String fileName, String imgFormat)
            throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
        ImageIO.write(image, imgFormat, imageOutputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return uploadStream(inputStream, fileName);
    }


}
