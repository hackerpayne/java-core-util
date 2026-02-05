package com.qyhstech.core.io.tests;

import com.qyhstech.core.dates.QyDate;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 合并多个日志文件
 * Created by kyle on 2017/6/13.
 */
@Slf4j
public class CombileLogFiles {

    private static File saveFile;

    public static void main(String[] args) {

        File dirFiles = new File("/Users/kyle/Downloads/日志分析");

        saveFile = new File("/Users/kyle/Downloads/日志分析/multithreading.txt");

        // 遍历目录下面的文件
//        Collection<File> listFiles = FileUtil.listFileNames(dirFiles, new String[]{"log"}, false);
//
//        for (File file : listFiles) {
        System.out.println(saveFile.getPath());
//            processFile(file);
//        }
    }

    /**
     * 读取和处理文件内容
     *
     * @param file
     * @throws Exception
     */
    public static void processFile(File file) {
        log.info("当前时间：" + QyDate.nowTime());

        log.info("读取文件：" + file.getPath());

//        LineIterator it = null;
//        try {
//            it = FileUtils.lineIterator(file, "UTF-8");
//            while (it.hasNext()) {
//                String line = it.nextLine();
//                if (QyStr.isEmpty(line)) continue;
//
//                if (line.startsWith("#")) continue;//带#开头的行，是注释，跳开
//
//                FileUtils.write(saveFile, line.trim() + QyConsts.LineSeparator, "utf-8", true);
//
//            }
//        } catch (IOException e) {
//            log.error("读取文件：" + file.getPath() + "，发生异常", e);
//        } finally {
//            IoUtil.close(it);
//        }

    }
}
