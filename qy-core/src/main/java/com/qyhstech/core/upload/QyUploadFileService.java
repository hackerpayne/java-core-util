package com.qyhstech.core.upload;

public interface QyUploadFileService {

    /**
     * 根据ID获取视图对象
     *
     * @param id
     * @return
     */
    QyUploadFileDto getVoById(Integer id);

    /**
     * 文件上传
     *
     * @param content
     * @param originFileName
     * @return
     */
    QyUploadFileDto storeFile(byte[] content, String originFileName);

    /**
     * 存储文件到本地
     *
     * @param content
     * @param path
     * @param fileName
     */
    void storeFileWithFileName(byte[] content, String path, String fileName);

}