package com.qyhstech.core.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具
 */
public class QyException extends ExceptionUtil {
    /**
     * 生成实体类的错误信息
     *
     * @param entity 实体类名
     * @param field  字段
     * @param val    字段值
     * @return
     */
    public static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity) + " with " + field + " " + val;
    }

    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
