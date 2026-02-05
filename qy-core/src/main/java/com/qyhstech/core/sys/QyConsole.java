package com.qyhstech.core.sys;

import com.qyhstech.core.QyStr;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 控制台操作辅助类
 */
@Slf4j
public class QyConsole {

    /**
     * 读取控制台内容
     *
     * @param tip 提示文本
     * @return
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (QyStr.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new RuntimeException("请输入正确的" + tip + "！");
    }
}
