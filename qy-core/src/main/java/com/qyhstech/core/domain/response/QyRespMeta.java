package com.qyhstech.core.domain.response;

import com.qyhstech.core.json.QyJackson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 分页头信息，放入meta标签内
 */
@Data
public class QyRespMeta implements Serializable {

    /**
     * 当前页码
     */
    @Setter(AccessLevel.NONE)
    private Integer pageNo = 1;

    /**
     * 当前页记录数量
     */
    @Setter(AccessLevel.NONE)
    private Integer pageSize = 10;

    /**
     * 总页数
     */
    private Integer pageCount;

    /**
     * 总记录数量
     */
    private Integer totalCount;

    public QyRespMeta() {

    }

    public QyRespMeta(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public QyRespMeta(Integer pageNo, Integer pageSize, Integer pageCount, Integer totalCount) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.pageCount = pageCount;
        this.totalCount = totalCount;
    }

    public QyRespMeta(Long pageNo, Long pageSize, Long pageCount, Long totalCount) {
        this.pageSize = pageSize.intValue();
        this.pageNo = pageNo.intValue();
        this.pageCount = pageCount.intValue();
        this.totalCount = totalCount.intValue();
    }

    public void setPageCurrent(Integer pageCurrent) {
        if (Objects.isNull(pageCurrent) || pageCurrent <= 0) {
            pageCurrent = 1;
        }
        this.pageNo = pageCurrent;
    }

    public void setPageSize(Integer pageSize) {
        if (Objects.isNull(pageSize) || pageSize < 0) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
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