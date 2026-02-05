package com.qyhstech.core.settting;

import com.qyhstech.core.settting.anno.SettingKey;
import lombok.Data;

@Data
public class SmsSettingReq {
    @SettingKey("app_key")
    private String smsAppKey;

//    @SettingKey("smsAppSecret")
    private String smsAppSecret;

    private Integer timeout;

}