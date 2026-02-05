package com.qyhstech.core.io;

import cn.hutool.core.io.IoUtil;
import com.qyhstech.core.domain.constant.QyConsts;
import com.qyhstech.core.QyStr;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 流处理
 */
@Slf4j
public class QyIoUtil extends IoUtil {

    /**
     * Object转byte[]数组
     * 一般也用作：serialize序列化时使用
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] objToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    /**
     * InputStream转Obj对象
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object inputStreamToObj(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return objectInputStream.readObject();
    }

    /**
     * 根据指定编码读取InputStream到Str字符串内
     *
     * @param inputStream
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String inputStreamToStr(InputStream inputStream, String encoding) throws IOException {
        return inputStreamToStr(inputStream, encoding, QyConsts.LineSeparator);
    }

    /**
     * 根据指定编码读取InputStream到Str字符串内
     *
     * @param inputStream
     * @param encoding
     * @param lineAddStr
     * @return
     */
    public static String inputStreamToStr(InputStream inputStream, String encoding, String lineAddStr) {
        BufferedInputStream bufferedInputStream = null;
        BufferedReader bReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            // 标准输出流、就在当前方法中读取
            bufferedInputStream = new BufferedInputStream(inputStream);

            InputStreamReader inputSourceReader = new InputStreamReader(bufferedInputStream, QyStr.isNotEmpty(encoding) ? encoding : "UTF-8");//设置编码方式

            bReader = new BufferedReader(inputSourceReader);

            String line;
            while ((line = bReader.readLine()) != null) {
                sb.append(line).append(QyStr.isNotEmpty(lineAddStr) ? lineAddStr : "");
            }
            bReader.close();
            inputSourceReader.close();
        } catch (IOException ignored) {
            log.error(ignored.getMessage());
            throw new RuntimeException((ignored));
        } finally {
            QyIoUtil.close(bufferedInputStream);
            QyIoUtil.close(bReader);
        }

        return sb.toString();
    }

    /**
     * 输入流转换为字符串
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String streamToStr(InputStream in) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * Bytes数据转Obj
     * 一般也用作：deserialize反序列化时使用
     *
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object bytesToObj(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

}
