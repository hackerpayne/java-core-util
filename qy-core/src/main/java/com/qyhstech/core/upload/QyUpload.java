package com.qyhstech.core.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.domain.enums.FileNamePatternEnum;
import com.qyhstech.core.domain.enums.QyFileTypeEnum;
import com.qyhstech.core.io.QyFile;
import com.qyhstech.core.io.QyFileType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 文件上传服务
 */
@Data
@Slf4j
public class QyUpload {

    /**
     * 本地的绝对路径目录
     */
    private String localFolderPath;

    /**
     * 保存文件的基本URL，后面拼接会用到
     */
    private String baseUrl;

    /**
     * 允许的文件扩展名（如 ["jpg", "png", "pdf"]）
     */
    private List<String> allowedExtensions;

    /**
     * 最大文件大小 (单位：字节)
     */
    private Long maxFileSize;

    /**
     * 是否使用原始文件名
     */
    private boolean useOriginalFilename;

    /**
     * 本地的目录路径，留目录即可
     *
     * @param localFolderPath
     */
    public QyUpload(String localFolderPath) {
        this.localFolderPath = localFolderPath;
        this.baseUrl = "/";
    }

    /**
     * @param localFolderPath     本地目录路径
     * @param allowedExtensions   允许的扩展名列表：
     * @param maxFileSize         最大文件大小，限制上传的文件大小用
     * @param useOriginalFilename 是否使用
     * @param baseUrl             网站的基准URL，可以生成完整的路径
     */
    public QyUpload(String localFolderPath, List<String> allowedExtensions, Long maxFileSize, boolean useOriginalFilename, String baseUrl) {
        this.localFolderPath = localFolderPath;
        this.allowedExtensions = allowedExtensions;
        this.maxFileSize = maxFileSize;
        this.useOriginalFilename = useOriginalFilename;
        this.baseUrl = baseUrl;
    }

    /**
     * 检查文件类型是否满足要求
     *
     * @param multipartFile 上传的文件
     * @param fileTypeCheck 要检查的文件类型
     * @return
     */
    public boolean checkFileType(MultipartFile multipartFile, QyFileTypeEnum fileTypeCheck) {
        try {
            if (null != fileTypeCheck) {
                QyFileTypeEnum fileType = QyFileType.checkFileType(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
                return Objects.equals(fileType.getName(), fileTypeCheck.getName());
            }
        } catch (Exception exception) {
            log.error("checkFile发生异常", exception);
        }

        return false;
    }

    /**
     * 默认使用日期上传文件名
     *
     * @param multipartFile
     * @param saveRelativeFolder
     * @return
     * @throws IOException
     */
    public QyUploadFileDto handleUpload(MultipartFile multipartFile, String saveRelativeFolder) throws IOException {
        return handleUpload(multipartFile, saveRelativeFolder, FileNamePatternEnum.DATETIME_RAND, null, false);
    }

    /**
     * 处理上传，不会改变原有的文件扩展名
     *
     * @param multipartFile        上传的文件
     * @param saveRelativeFolder   保存目录，使用相对路径
     * @param fileNamePatternEnum  定义文件名的命名规则
     * @param newFileName          定义保存的自定义文件名名称，可以不带扩展名
     * @param appendOriginFileName 是否把原始文件名添加到前缀中。这样生成的路径就是：原始文件名_日期_rand格式
     * @return UploadFileVo 结果对象
     * @throws IllegalArgumentException 参数校验异常
     */
    public QyUploadFileDto handleUpload(MultipartFile multipartFile, String saveRelativeFolder, FileNamePatternEnum fileNamePatternEnum, String newFileName, Boolean appendOriginFileName) throws IOException {
        // 空文件校验
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 文件大小校验
        if (Objects.nonNull(maxFileSize) && multipartFile.getSize() > maxFileSize) {
            throw new IllegalArgumentException("文件大小超出限制: " + (maxFileSize / 1024 / 1024) + " MB");
        }

        // 文件扩展名校验
        String originalFilename = FileUtil.getName(multipartFile.getOriginalFilename());
        String extName = QyFile.getFileExt(originalFilename);

        // 文件扩展名校验
        if (CollUtil.isNotEmpty(allowedExtensions)) {
            if (!allowedExtensions.contains(extName)) {
                throw new IllegalArgumentException("不允许的文件格式: ." + extName);
            }
        }

        // 生成文件名，优先使用指定的，如果没有指定，使用生成的，如果没有生成，使用原生的文件名
        String fileName;
        if (StrUtil.isNotEmpty(newFileName)) {
            fileName = newFileName;
        } else if (Objects.nonNull(fileNamePatternEnum)) {
            // 如果要把原始文件名加前缀，取出原始文件名，加上新文件名
            fileName = (Boolean.TRUE.equals(appendOriginFileName) ? QyFile.getFileNameWithoutExtension(originalFilename) + "_" : "") + QyFile.generateFileName(fileNamePatternEnum, extName);
        } else {
            fileName = originalFilename;
        }

        // 创建保存目录
        File relativeFolder = FileUtil.file(this.localFolderPath, saveRelativeFolder);
        QyFile.setPermission(relativeFolder); // 对目录权限进行配置

        // 保存文件
        File destFile = FileUtil.file(relativeFolder, fileName);
        multipartFile.transferTo(destFile);
        QyFile.setPermissionPosix(destFile);// 对文件权限进行设置，以防止出现故障，权限不足

        // 返回结果
        QyUploadFileDto file = new QyUploadFileDto();
        file.setName(fileName);// 文件名
        file.setOldName(originalFilename);// 旧文件名，带扩展名
        file.setExt(extName);
        file.setRelativePath(QyFile.normalizeRelativePath(saveRelativeFolder, fileName)); // 把路径转换为相对路径，便于存到库中
        file.setFullPath(destFile.getAbsolutePath());// 完整路径
        file.setSourceUrl(genAccessUrl(saveRelativeFolder, fileName));
        file.setNamePattern(fileNamePatternEnum);
//        file.setMd5(QyMd5.md5(multipartFile.getInputStream()));
        return file;
    }

    /**
     * 生成一个完整访问链接的文件名
     *
     * @param folder 目录
     * @param name   名称
     * @return
     */
    public String genAccessUrl(String folder, String name) {
        String url = baseUrl;
        if (!url.endsWith("/")) {
            url += "/";
        }
        if (folder.startsWith("/")) {
            folder = folder.substring(1);
        }
        if (!folder.endsWith("/") && !folder.isEmpty()) {
            folder += "/";
        }
        return url + folder + name;
    }


}
