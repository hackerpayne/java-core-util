package com.qyhstech.core.settting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseSetting {
    private Long id;

    /**
     * 键名
     */
    private String key;

    /**
     * 键值
     */
    private String value;
}
