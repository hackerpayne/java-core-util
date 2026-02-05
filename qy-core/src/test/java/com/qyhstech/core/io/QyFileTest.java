package com.qyhstech.core.io;

import cn.hutool.core.util.URLUtil;
import com.qyhstech.core.dates.QyDate;
import com.qyhstech.core.domain.enums.FileNamePatternEnum;
import com.qyhstech.core.http.QyUrl;
import org.junit.jupiter.api.Test;

class QyFileTest {

    @Test
    void checkFile() {
    }

    @Test
    void check() {
    }

    @Test
    void checkSize() {
        QyFile.mkdir("/backup");
    }

    @Test
    void getFilePath() {
        System.out.println(QyFile.join(null, "upload", null, "/", "info", "ok.txt"));
        System.out.println(QyFile.join(QyDate.nowDate(), "ok.txt"));
    }

    @Test
    void loopFiles() {
    }

    @Test
    void testLoopFiles() {
    }

    @Test
    void readAndSplitToList() {
    }

    @Test
    void writeListToFile() {
    }

    @Test
    void getRelativeFile() {
    }

    @Test
    void getRandOneline() {
    }

    @Test
    void createEmptyFile() {
    }

    @Test
    void readFileByFileInputStream() {
    }

    @Test
    void readFileByBufferReader() {
    }

    @Test
    void writeFileByStringBuffer() {
    }

    @Test
    void writeFileByBufferOutPutStream() {
    }

    @Test
    void writeFileByBufferWriter() {
    }

    @Test
    void copyFile() {
    }

    @Test
    void largeFileIO() {
    }

    @Test
    void rename() {
    }

    @Test
    void getFileEncode() {
    }

    @Test
    void testGetFileEncode() {
    }

    @Test
    void formatBytes() {
    }

    @Test
    void getFile() {
    }

    @Test
    void testGetFile() {
    }

    @Test
    void getParentFile() {
    }

    @Test
    void getParent() {
    }

    @Test
    void saveFile() {
    }

    @Test
    void getFileExt() {
        System.out.println(QyFile.getFileExt("test.txt"));
        System.out.println(QyFile.getFileExt("test.md"));
    }

    @Test
    void testGetFileExt() {
    }

    @Test
    void setPermission() {
    }

    @Test
    void testSetPermission() {
    }

    @Test
    void setPermissionPosix() {
    }

    @Test
    void testSetPermissionPosix() {
    }

    @Test
    void generateFileName() {
        System.out.println("UUID 1:" + QyFile.generateFileName(FileNamePatternEnum.UUID));
        System.out.println("UUID 2:" + QyFile.generateFileName(FileNamePatternEnum.UUID_RAND, "txt"));
        System.out.println("UUID 3:" + QyFile.generateFileName(FileNamePatternEnum.UUID_RAND, ".txt"));
        System.out.println("TIMESTAMP 1:" + QyFile.generateFileName(FileNamePatternEnum.TIMESTAMP));
        System.out.println("TIMESTAMP 2" + QyFile.generateFileName(FileNamePatternEnum.TIMESTAMP_RAND, ".txt"));
        System.out.println("DATE 1:" + QyFile.generateFileName(FileNamePatternEnum.DATE));
        System.out.println("DATE 2:" + QyFile.generateFileName(FileNamePatternEnum.DATE_RAND, ".txt"));
        System.out.println("DATETIME 1:" + QyFile.generateFileName(FileNamePatternEnum.DATETIME));
        System.out.println("DATETIME 2:" + QyFile.generateFileName(FileNamePatternEnum.DATETIME_RAND, ".txt"));

        System.out.println("FILE:" + QyFile.generateFileName(FileNamePatternEnum.DATETIME_RAND, "/data/opt/info.txt"));
        System.out.println("URL:" + QyFile.generateFileName(FileNamePatternEnum.DATETIME_RAND, "https://www.ok.com/ok.png?this=true&that=false"));
    }

    @Test
    void testGenerateFileName() {
    }

    @Test
    void testGenerateFileName1() {
    }

    @Test
    void normalizeRelativePath() {
        System.out.println(QyFile.normalize("/uploads/images/20250618/test\\images.png"));
        System.out.println(QyFile.normalize("/uploads/images/s.png"));
        System.out.println(QyFile.normalize("test/tests.png"));
        System.out.println(QyFile.normalizeRelativePath("/uploads/images", "20250618/test\\images.png"));
        System.out.println(QyFile.normalizeRelativePath("/uploads/images", QyFile.normalize("/20250618/test\\images.png")));
    }

    @Test
    void testCheckFile() {
    }

    @Test
    void testCheck() {
    }

    @Test
    void testCheckSize() {
    }

    @Test
    void getFileNameWithoutExtension() {
        System.out.println(QyFile.getFileNameWithoutExtension("https://www.pexels.com/about/image.pnd?file=a&queyr=b&relative=false"));
        System.out.println(QyFile.getFileName("https://www.pexels.com/about/image.pnd"));
        System.out.println(QyFile.getFileName("https://www.pexels.com/about/image.pnd?file=a&queyr=b&relative=false"));
        System.out.println(URLUtil.getPath("https://www.pexels.com/about/image.pnd?file=a&queyr=b&relative=false"));
        System.out.println(QyUrl.getFileName("https://www.pexels.com/about/image.pnd?file=a&queyr=b&relative=false"));
        System.out.println(QyUrl.getFileExt("https://www.pexels.com/about/image.pnd?file=a&queyr=b&relative=false"));
        System.out.println(QyUrl.getFileNameWithoutExtension("https://www.pexels.com/about/image.pnd?file=a&queyr=b&relative=false"));

        System.out.println(QyUrl.getFileName("https://www.pexels.com/about/image"));
        System.out.println(QyUrl.getFileExt("https://www.pexels.com/about/image/"));
        System.out.println(QyUrl.getFileNameWithoutExtension("https://www.pexels.com/about/image/"));

    }

    @Test
    void testGetFileNameWithoutExtension() {
    }

    @Test
    void testGetFilePath() {
    }

    @Test
    void testLoopFiles1() {
    }

    @Test
    void testLoopFiles2() {
    }

    @Test
    void testReadAndSplitToList() {
    }

    @Test
    void testWriteListToFile() {
    }

    @Test
    void testGetRelativeFile() {
    }

    @Test
    void testGetRandOneline() {
    }

    @Test
    void testCreateEmptyFile() {
    }

    @Test
    void testReadFileByFileInputStream() {
    }

    @Test
    void testReadFileByBufferReader() {
    }

    @Test
    void testWriteFileByStringBuffer() {
    }

    @Test
    void testWriteFileByBufferOutPutStream() {
    }

    @Test
    void testWriteFileByBufferWriter() {
    }

    @Test
    void testCopyFile() {
    }

    @Test
    void testLargeFileIO() {
    }

    @Test
    void testRename() {
    }

    @Test
    void testGetFileEncode1() {
    }

    @Test
    void testGetFileEncode2() {
    }

    @Test
    void testFormatBytes() {
    }

    @Test
    void testGetFile1() {
    }

    @Test
    void testGetFile2() {
    }

    @Test
    void testGetParentFile() {
    }

    @Test
    void testGetParent() {
    }

    @Test
    void testSaveFile() {
    }

    @Test
    void testGetFileExt1() {
    }

    @Test
    void testGetFileExt2() {
    }

    @Test
    void testSetPermission1() {
    }

    @Test
    void testSetPermission2() {
    }

    @Test
    void testSetPermissionPosix1() {
    }

    @Test
    void testSetPermissionPosix2() {
    }

    @Test
    void testGenerateFileName2() {
    }

    @Test
    void testGenerateFileName3() {
    }

    @Test
    void testGenerateFileName4() {
    }

    @Test
    void generateDateFolderName() {
    }

    @Test
    void testNormalizeRelativePath() {
    }

}