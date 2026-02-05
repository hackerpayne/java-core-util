package com.qyhstech.core.exception;

import com.qyhstech.core.domain.enums.IStatusCode;
import com.qyhstech.core.domain.enums.QyErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * 默认使用的服务器异常处理类
 *
 * @author kyle
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QyServerException extends RuntimeException {

    private int code;

    private String msg;

    public QyServerException() {
        super();
    }

    /**
     * @param msg
     */
    public QyServerException(String msg) {
        super(msg);
        this.code = QyErrorCode.SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public QyServerException(String message, Integer code) {
        this.msg = message;
        this.code = code;
    }

    /**
     * @param errorCode
     */
    public QyServerException(IStatusCode errorCode) {
        super(errorCode.getDesc());
        this.code = errorCode.getCode();
        this.msg = errorCode.getDesc();
    }

    /**
     * 使用http status构造异常
     *
     * @param httpStatus
     */
    public QyServerException(HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.code = httpStatus.value();
        this.msg = httpStatus.getReasonPhrase();
    }

    /**
     * 使用HttpStatus构造异常，但是使用自定义的错误信息提示
     *
     * @param httpStatus
     * @param message
     */
    public QyServerException(HttpStatus httpStatus, String message) {
        super(message);
        this.code = httpStatus.value();
        this.msg = message;
    }

    /**
     * @param msg
     * @param e
     */
    public QyServerException(String msg, Throwable e) {
        super(msg, e);
        this.code = QyErrorCode.SERVER_ERROR.getCode();
        this.msg = msg;
    }

}