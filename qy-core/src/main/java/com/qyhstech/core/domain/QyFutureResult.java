package com.qyhstech.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取得线程池的所有结果
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QyFutureResult {

    /**
     * 是否成功的标记
     */
    private Boolean flag;

    /**
     * 异常或者提示消息
     */
    private String message;

    /**
     * 执行结果
     */
    private Object data;

    public QyFutureResult(boolean flag) {
        this.flag = flag;
    }

    public QyFutureResult(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public QyFutureResult(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }

}
