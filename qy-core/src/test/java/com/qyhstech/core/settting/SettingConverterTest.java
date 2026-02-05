package com.qyhstech.core.settting;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class SettingConverterTest {

    @Test
    public void testGetAllKeys() {
        Set<String> settings = SettingConverter.getAllKeys(SmsSettingReq.class);
        System.out.println(settings);
    }

    @Test
    public void testConvert() {
        SmsSettingReq smsSettingReq = new SmsSettingReq();
        smsSettingReq.setSmsAppKey("smsAppKey");
        smsSettingReq.setSmsAppSecret("smsAppSecret");
        smsSettingReq.setTimeout(1);

        List<BaseSetting> settings = SettingConverter.toBaseSettings(smsSettingReq);
        System.out.println(settings);
    }

    @Test
    public void testConvertTo() {
        List<BaseSetting> settings = new ArrayList<>();
        settings.add(BaseSetting.builder().key("smsAppKey").value("abc").build());
        settings.add(BaseSetting.builder().key("smsAppSecret").value("def").build());
        settings.add(BaseSetting.builder().key("timeout").value("1").build());

        SmsSettingReq smsSettingReq = SettingConverter.toObject(settings, SmsSettingReq.class);
        System.out.println(smsSettingReq);
    }
}