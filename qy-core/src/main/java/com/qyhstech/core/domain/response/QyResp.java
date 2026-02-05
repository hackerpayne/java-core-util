package com.qyhstech.core.domain.response;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qyhstech.core.domain.enums.IStatusCode;
import com.qyhstech.core.domain.enums.QyErrorCode;
import com.qyhstech.core.exception.QyBizException;
import com.qyhstech.core.json.QyJackson;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 单条记录返回，其实也是支持多条记录的，只是没有强制要求必须是列表数据
 */
@Data
public class QyResp implements Serializable {

    /**
     * 返回状态码
     */
    private int code;

    /**
     * 返回提示消息
     */
    private String msg;

    /**
     * 返回数据明细
     */
    private Object data;

    /**
     * 分页信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private QyRespMeta meta;

    /**
     * 数据为空的返回
     */
    public QyResp() {
        this(QyErrorCode.SUCCESS);
    }

    /**
     * @param errorCode
     */
    public QyResp(IStatusCode errorCode) {
        this(errorCode.getCode(), errorCode.getDesc(), null);
    }

    /**
     * @param statusCode
     * @param data
     */
    public QyResp(IStatusCode statusCode, Object data) {
        this(statusCode.getCode(), statusCode.getDesc(), data);
    }

    public QyResp(IStatusCode statusCode, String message, Object data) {
        this(statusCode.getCode(), message, data);
    }

    /**
     * @param code
     * @param message
     */
    public QyResp(int code, String message) {
        this(code, message, null);
    }

    /**
     * 全手工指定
     *
     * @param code
     * @param message
     * @param data
     */
    public QyResp(int code, String message, Object data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    /**
     * 返回List列表
     *
     * @param list
     */
    public QyResp(Collection<?> list) {
        this.meta = new QyRespMeta();
        this.meta.setTotalCount(list.size());
        this.data = list;
    }

    /**
     * 适配MyBatisPlus的Long类型
     *
     * @param pageNo
     * @param pageSize
     * @param pageCount
     * @param totalCount
     * @param data
     */
    public QyResp(Long pageNo, Long pageSize, Long pageCount, Long totalCount, Object data) {
        this();
        meta = new QyRespMeta(pageNo.intValue(), pageSize.intValue(), pageCount.intValue(), totalCount.intValue());
        this.data = data;
    }

    /**
     * 带分页返回
     *
     * @param pageNo
     * @param pageSize
     * @param pageCount
     * @param totalCount
     * @param data
     */
    public QyResp(Integer pageNo, Integer pageSize, Integer pageCount, Integer totalCount, Object data) {
        this();
        meta = new QyRespMeta(pageNo, pageSize, pageCount, totalCount);
        this.data = data;
    }

    /**
     * 直接返回数据：例：return new RespPage<>(page, this::toSimpleLicenceDTO);
     *
     * @param page
     * @param <E>
     */
    public <E> QyResp(IPage<E> page) {
        this();
        meta = new QyRespMeta(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal());

        if (CollUtil.isEmpty(page.getRecords())) {
            this.setData(Collections.emptyList());
        } else {
            this.setData(page.getRecords());
        }
    }

    /**
     * 没有错误提示的返回，不建议使用
     *
     * @return
     */
    public static QyResp fail() {
        return new QyResp(QyErrorCode.FAIL);
    }

    /**
     * 以异常为错误提示
     *
     * @param ex
     * @return
     */
    public static QyResp fail(QyBizException ex) {
        return new QyResp(ex.getCode(), ex.getMsg());
    }

    /**
     * 只返回错误提示信息
     *
     * @param message 提示消息
     * @return 对象
     */
    public static QyResp fail(String message) {
        return new QyResp(QyErrorCode.FAIL.getCode(), message);
    }

    /**
     * 接口调用失败,有错误字符串码和描述,没有返回对象
     *
     * @param code
     * @param message
     * @return
     */
    public static QyResp fail(int code, String message) {
        return new QyResp(code, message);
    }

    /**
     * 接口调用失败,有错误字符串码和描述,有返回对象
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static QyResp fail(int code, String message, Object data) {
        return new QyResp(code, message, data);
    }

    /**
     * 直接以枚举返回的信息为准
     *
     * @param statusCode
     * @return
     */
    public static QyResp fail(IStatusCode statusCode) {
        return new QyResp(statusCode);
    }

    /**
     * 用失败的代码和自己的消息
     *
     * @param statusCode
     * @param message
     * @param
     * @return
     */
    public static QyResp fail(IStatusCode statusCode, String message) {
        return new QyResp(statusCode.getCode(), message);
    }

    public static QyResp fail(HttpStatus httpStatus) {
        return new QyResp(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    /**
     * 返回错误消息，可以指定错误内容
     *
     * @param httpStatus
     * @param message
     * @return
     */
    public static QyResp fail(HttpStatus httpStatus, String message) {
        return new QyResp(httpStatus.value(), message);
    }

    /**
     * 根据异常返回信息
     *
     * @param ex
     * @return
     */
    public static QyResp error(QyBizException ex) {
        return new QyResp(ex.getCode(), ex.getMessage());
    }

    /**
     * 直接返回成功的无数据
     *
     * @return
     */
    public static QyResp success() {
        return new QyResp(QyErrorCode.SUCCESS);
    }

    /**
     * 接口调用成功，有返回对象
     *
     * @return
     */
    public static QyResp success(Object data) {
        if (data instanceof IPage<?>) {
            return successPage((IPage<?>) data);
        }
        return new QyResp(QyErrorCode.SUCCESS, data);
    }

    /**
     * @param page
     * @return
     */
    public static QyResp successPage(IPage<?> page) {
        return new QyResp(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), page.getRecords());
    }

    /**
     * 直接返回Page结果，带List
     *
     * @param page
     * @param dataList
     * @param
     * @return
     */
    public static QyResp successPage(IPage<?> page, Collection<?> dataList) {
        return new QyResp(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), dataList);
    }

    /**
     * 接口调用成功,有返回对象
     *
     * @param pageNo
     * @param pageSize
     * @param totalCount
     * @param data
     * @return
     */
    public static QyResp successPage(int pageNo, int pageSize, int pageCount, int totalCount, Object data) {
        return new QyResp(pageNo, pageSize, pageCount, totalCount, data);
    }

    /**
     * 返回列表数据
     *
     * @param listData
     * @return
     */
    public static QyResp successList(Collection<?> listData) {
        return new QyResp(listData);
    }

    /**
     * 只返回成功的提示信息
     *
     * @param msg
     * @return
     */
    public static QyResp successMsg(String msg) {
        QyResp resp = new QyResp();
        resp.setCode(QyErrorCode.SUCCESS.getCode());
        resp.setMsg(msg);
        return resp;
    }

    /**
     * 根据状态返回
     *
     * @param httpStatus
     * @return
     */
    public static QyResp httpStatus(HttpStatus httpStatus) {
        return new QyResp(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    /**
     * 自由设置成功信息的成功结果
     *
     * @param msg
     * @param data
     * @return
     */
    public static QyResp success(String msg, Object data) {
        return new QyResp(QyErrorCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 判断请求是否成功
     *
     * @return
     */
    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, QyErrorCode.SUCCESS.getCode());
    }

    /**
     * @return
     */
    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(this.getCode());
    }

    /**
     * 是否有错误
     *
     * @return
     */
    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 直接转换为Json字符串
     *
     * @return
     */
    public String jsonString() {
        //        return JSON.toJSONString(this);
        return QyJackson.toJsonString(this);
    }

}
