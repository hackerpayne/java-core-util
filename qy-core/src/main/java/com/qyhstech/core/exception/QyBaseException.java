package com.qyhstech.core.exception;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 基础异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class QyBaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public QyBaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public QyBaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public QyBaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public QyBaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!StrUtil.isEmpty(code)) {
//            message = QyLang.message(code, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }

}
