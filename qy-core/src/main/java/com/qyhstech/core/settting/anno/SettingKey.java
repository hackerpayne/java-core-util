package com.qyhstech.core.settting.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SettingKey {
    String value();
}
