package com.qyhstech.core.domain.dto;

import com.qyhstech.core.domain.base.QyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 手机号码归属地查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModelMobileLocation extends QyBaseEntity {

    private String mobile;
    private String province;
    private String city;
    private String areacode;
    private String zip;
    private String isp;
    private String card;

    public ModelMobileLocation() {
    }

    public ModelMobileLocation(String phone) {
        this.mobile = phone;
    }

}
