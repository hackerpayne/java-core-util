package com.qyhstech.core.exception;

import com.qyhstech.core.domain.enums.IStatusCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class QyBizException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 异常消息
     */
    private String msg;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 没有错误码的，默认使用FAIL的错误码1
     *
     * @param msg
     */
    public QyBizException(String msg) {
        super(msg);
        this.msg = msg;
    }

    /**
     * 传入指定的异常错误码进来
     *
     * @param qyErrorCode
     */
    public QyBizException(IStatusCode qyErrorCode) {
        super(qyErrorCode.getDesc());
        this.msg = qyErrorCode.getDesc();
        this.code = qyErrorCode.getCode();
    }

    /**
     * 带异常的返回
     *
     * @param qyErrorCode
     * @param ex
     */
    public QyBizException(IStatusCode qyErrorCode, Throwable ex) {
        super(ex);
        this.msg = qyErrorCode.getDesc();
        this.code = qyErrorCode.getCode();
    }

    /**
     * @param msg
     * @param e
     */
    public QyBizException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    /**
     * @param code
     * @param msg
     */
    public QyBizException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    /**
     * @param msg
     * @param code
     * @param e
     */
    public QyBizException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    /**
     * 指定默认异常和参数
     *
     * @param code
     * @param args
     */
    public QyBizException(int code, Object[] args) {
        this(null, code, args, null);
    }

    /**
     * 完整构造函数
     *
     * @param module
     * @param code
     * @param args
     * @param defaultMessage
     */
    public QyBizException(String module, int code, Object[] args, String defaultMessage) {
        super(defaultMessage);
        this.module = module;
        this.code = code;
        this.args = args;
        this.msg = defaultMessage;
    }

}