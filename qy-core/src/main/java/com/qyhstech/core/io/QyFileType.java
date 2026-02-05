package com.qyhstech.core.io;

import cn.hutool.core.io.FileTypeUtil;
import com.qyhstech.core.domain.enums.QyFileTypeEnum;

import java.io.File;
import java.io.InputStream;

/**
 * 文件类型检查
 */
public class QyFileType {

    static {
        FileTypeUtil.putFileType("FFD8FF", "jpeg");
        FileTypeUtil.putFileType("FFD8FF", "jpg");
        FileTypeUtil.putFileType("89504E47", "png");
        FileTypeUtil.putFileType("47494638", "gif");
        FileTypeUtil.putFileType("424D", "bmp");

        FileTypeUtil.putFileType("49492A00", "tiff");
        FileTypeUtil.putFileType("6C657420", "txt");
        FileTypeUtil.putFileType("3C3F786D6C", "xml");
        FileTypeUtil.putFileType("68746D6C3E", "html");
        FileTypeUtil.putFileType("7B5C727466", "rtf");
        FileTypeUtil.putFileType("D0CF11E0", "doc");
        FileTypeUtil.putFileType("504B030414", "docx");
        FileTypeUtil.putFileType("255044462D312E", "pdf");

        FileTypeUtil.putFileType("504B0304", "zip");
        FileTypeUtil.putFileType("52617221", "rar");
        FileTypeUtil.putFileType("1F8B08", "gz");

        FileTypeUtil.putFileType("57415645", "wav");
        FileTypeUtil.putFileType("41564920", "avi");
        FileTypeUtil.putFileType("4D546864", "midi");
        FileTypeUtil.putFileType("000001BA", "mpg");
        FileTypeUtil.putFileType("6D6F6F76", "mov");
        FileTypeUtil.putFileType("2E7261FD", "ram"); // Real Audio
        FileTypeUtil.putFileType("2E524D46", "rm"); // Real Audio
    }

    /**
     * 获取文件类型
     * @param file
     * @return
     */
    public static String getFileType(File file) {
        return FileTypeUtil.getType(file);
    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static String getFileType(String file) {
        return FileTypeUtil.getType(new File(file));
    }

    /**
     * 获取文件类型
     *
     * @param stream
     * @param fileName
     * @return
     */
    public static String getFileType(InputStream stream, String fileName) {
        return FileTypeUtil.getType(stream, fileName);
    }

    /**
     * 检查指定文件的类型
     *
     * @param file
     * @return
     */
    public static QyFileTypeEnum checkFileType(File file) {
        if (!file.exists()) {
            throw new RuntimeException("QyFileType检查文件类型checkFileType，文件不存在异常");
        }
        String fileType = FileTypeUtil.getType(file);
        return matchFileType(fileType);
    }

    /**
     * 返回指定类型的文件枚举
     *
     * @param file
     * @return
     */
    public static QyFileTypeEnum checkFileType(String file) {
        String fileType = FileTypeUtil.getType(new File(file));
        return matchFileType(fileType);
    }

    /**
     * 检查是否是指定类型的文件
     *
     * @param file
     * @return
     */
    public static QyFileTypeEnum checkFileType(InputStream file, String originFileName) {
        String fileType = FileTypeUtil.getType(file, originFileName);
        return matchFileType(fileType);
    }

    /**
     * 检查指定的文件类型是不是满足条件的类型
     *
     * @param fileType
     * @return
     */
    public static QyFileTypeEnum matchFileType(String fileType) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(fileType)) {
            return QyFileTypeEnum.IMAGE;
        } else if (documents.contains(fileType)) {
            return QyFileTypeEnum.DOCUMENT;
        } else if (music.contains(fileType)) {
            return QyFileTypeEnum.MUSIC;
        } else if (video.contains(fileType)) {
            return QyFileTypeEnum.VIDEO;
        } else {
            return QyFileTypeEnum.OTHER;
        }
    }

}
