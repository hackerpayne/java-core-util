package com.qyhstech.core.domain.param;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 查询条件参数类
 * 请求时：public class LicenceParam extends PageParam
 */
@Getter
@Setter
public class QyParamPage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NO = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNo = DEFAULT_PAGE_NO;

    @Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 排序列
     */
    private String order = "";

    /**
     * 排序的方向：
     * desc降序、asc升序
     * descending降序，ascending升序
     * 0降序，1升序
     */
    private String asc;

    /**
     * 判断是否是升序
     *
     * @return
     */
    @JsonIgnore
    public boolean getIsAsc() {
        return StrUtil.isNotBlank(asc) && "asc".equalsIgnoreCase(asc);
    }

    /**
     * 判断是否有排序
     *
     * @return
     */
    @JsonIgnore
    public boolean hasOrder() {
        return StrUtil.isNotBlank(order) && StrUtil.isNotBlank(asc);
    }

    /**
     * 清空排序并添加新的排序字段
     *
     * @param orderList
     */
    public void clearAddOrder(List<String> orderList) {
        this.order = StrUtil.join(",", orderList);
    }

    /**
     * 获取分页之后的第一条数据位置，比如
     * 1页10条，第10页的数据是：90。
     *
     * @return
     */
    @JsonIgnore
    public Integer getFirstNum() {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 获取数据否则取默认值
     *
     * @return
     */
    @JsonIgnore
    public int getPageNoOrDefault() {
        return pageNo == null || pageNo <= 0 ? DEFAULT_PAGE_NO : pageNo;
    }

    /**
     * 获取数据否则取默认值
     *
     * @return
     */
    @JsonIgnore
    public int getPageSizeOrDefault() {
        return pageSize == null || pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

}
