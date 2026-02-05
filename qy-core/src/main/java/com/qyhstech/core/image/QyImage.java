package com.qyhstech.core.image;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import com.qyhstech.core.encode.QyBase64;
import com.qyhstech.core.encode.QyCharset;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片资源处理工具类
 */
@Slf4j
public class QyImage {

    /**
     * Image转换为Base64格式
     *
     * @param image
     * @return
     * @throws IOException
     */
    public static String toBase64Str(BufferedImage image) throws IOException {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64.encode(imageBytes);
            bos.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
        return imageString;
    }

    /**
     * 将Base64编码的图像信息转为 {@link BufferedImage}
     *
     * @param base64 图像的Base64表示
     * @return {@link BufferedImage}
     * @throws IORuntimeException IO异常
     */
    public static BufferedImage toImage(String base64) throws IORuntimeException {
        byte[] decode = QyBase64.decode(base64, QyCharset.CHARSET_UTF_8);
        return toImage(decode);
    }

    /**
     * 将Base64编码的图像信息转为 {@link BufferedImage}
     *
     * @param imageBytes 图像bytes
     * @return {@link BufferedImage}
     * @throws IORuntimeException IO异常
     */
    public static BufferedImage toImage(byte[] imageBytes) throws IORuntimeException {
        try {
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
