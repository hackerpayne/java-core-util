package com.qyhstech.core.io;

import cn.hutool.core.io.FileUtil;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.domain.constant.QyConsts;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;

/**
 * InputStream流操作辅助类
 */
@Slf4j
public class QyInputStream {

    /**
     * 自动关闭资源
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    /**
     * 自动关闭资源
     *
     * @param closeable
     */
    public static void close(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    /**
     * 从相对路径中读取资源文件，兼容Jar包里面的资源文件读取。
     * 但是只能读取InputStream里面。如果要读取对象里面，使用：
     * InputStream inputStream = FileUtil.getFileStream(MODEL_PATH);
     * ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
     * NaiveBayesModel bean = (NaiveBayesModel) objectInputStream.readObject();
     *
     * @param relativePath
     * @return
     */
    public static InputStream getFileStream(String relativePath) {

        InputStream inputStream = null;
        try {
            File destPath = FileUtil.file(relativePath);
            if (destPath.exists()) {
                inputStream = new FileInputStream(destPath);
            }

            destPath = FileUtil.file(QyConsts.CurrentDir, relativePath);
            if (destPath.exists()) {
                inputStream = new FileInputStream(destPath);
            }

            destPath = FileUtil.file(QyConsts.CurrentDir, "config", relativePath);
            if (destPath.exists()) {
                inputStream = new FileInputStream(destPath);
            }

        } catch (Exception ex) {
            log.error("getFileStream发生异常{}", ex.toString());
            return null;
        }
        return inputStream;
    }

    /**
     * InputStream转换为String字符串
     *
     * @param inputStream
     * @param encode
     * @return
     */
    public static String inputStreamToStr(InputStream inputStream, String encode) {

        StringBuilder sb = new StringBuilder();
        try {
            Charset charset = Charset.forName(QyStr.isNotEmpty(encode) ? encode : "utf-8");

            //复制一份，防止流用过后被销毁，可选
            InputStream cloneInputStream = cloneInputStream(inputStream);

            BufferedReader reader = new BufferedReader(new InputStreamReader(cloneInputStream, charset));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException ex) {
            log.error("inputStreamToStr发生异常", ex);
        }

        return sb.toString();
    }

    /**
     * 复制输入流
     *
     * @param inputStream
     * @return
     */
    public static InputStream cloneInputStream(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            log.error("复制输入流失败", e);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * 字符串转换为InputStream
     *
     * @param inStr
     * @return
     */
    public static InputStream strToInputStream(String inStr) {
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(inStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }


}
