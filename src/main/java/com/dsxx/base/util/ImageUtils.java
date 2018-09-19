package com.dsxx.base.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片处理相关类
 *
 * @author slm
 * @date 2018/9/12
 */
public class ImageUtils {
    /**
     * 二维码参数
     */
    private static final Map<EncodeHintType, Object> HINTS = new HashMap<>(16);
    /**
     * 图片格式png
     */
    public static final String IMG_FORMAT_PNG = "png";
    /**
     * 图片格式JPG
     */
    public static final String IMG_FORMAT_JPG = "jpg";

    static {
        // 字符编码
        HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 二维码与图片边距
        HINTS.put(EncodeHintType.MARGIN, 0);
    }

    /**
     * 生成二维码到BufferedImage
     *
     * @param content      二维码内容
     * @param width        宽
     * @param height       高
     * @param margin       内边距（像素）
     * @param cornerRadius 圆角（像素）
     * @return 返回BufferedImage
     * @throws WriterException
     * @author slm
     * @date 2018/09/13
     */
    public static BufferedImage generateQrCodeImg(String content, Integer width, Integer height, Integer margin,
                                                  Integer cornerRadius)
            throws WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, HINTS);
        bitMatrix = deleteWhite(bitMatrix);
        // 删除白边后，缩放回原来大小
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = resultImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.drawImage(bufferedImage, margin, margin, width - (2 * margin), height - (2 * margin), null);
        graphics.dispose();
        // 设置圆角
        if (cornerRadius != null && cornerRadius > 0) {
            resultImage = setRadius(resultImage, cornerRadius);
        }
        return resultImage;
    }


    /**
     * 生成二维码到文件中
     *
     * @param content      二维码内容
     * @param width        宽
     * @param height       高
     * @param margin       内边距（像素）
     * @param cornerRadius 圆角（像素）
     * @param path         文件路径 不包括文件后缀名
     * @param imgFormat    ImageUtils.IMG_FORMAT_PNG 或者 ImageUtils.IMG_FORMAT_JPG
     * @throws WriterException
     * @throws IOException
     * @author slm
     * @date 2018/09/13
     */
    public static void generateQrCodeImg(String content, Integer width, Integer height, Integer margin,
                                         Integer cornerRadius, String path, String imgFormat)
            throws WriterException, IOException {
        BufferedImage bufferedImage = generateQrCodeImg(content, width, height, margin, cornerRadius);
        ImageIO.write(bufferedImage, imgFormat, new File(path + "." + imgFormat));
    }

    /**
     * 删除白边
     *
     * @param matrix
     * @return BitMatrix
     */
    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        // 这个区域是二维码内容区域
        int resWidth = rec[2];
        int resHeight = rec[3];
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * 图片设置圆角
     *
     * @param image        BufferedImage
     * @param cornerRadius 圆角大小
     * @return 返回一个新的BufferedImage
     */
    public static BufferedImage setRadius(BufferedImage image, int cornerRadius) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = resultImage.createGraphics();
        resultImage = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        graphics.dispose();
        graphics = resultImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        // 画一个圆角矩形
        graphics.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        graphics.setComposite(AlphaComposite.SrcIn);
        graphics.drawImage(image, 0, 0, width, height, null);
        graphics.dispose();
        return resultImage;
    }

//    public static void main(String[] args) throws IOException, WriterException {
//        String fileName = UUID.randomUUID().toString() + ".png";
//        BufferedImage bufferedImage = generateQrCodeImg("https://wx.imdsxx.com/single/index.html?qrcode=000016011719", 206, 206, 8, 25);
//        System.out.println(QiniuUtils.uploadBufferedImage(bufferedImage, fileName, IMG_FORMAT_PNG));
//    }
}
