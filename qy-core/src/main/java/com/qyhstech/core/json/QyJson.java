package com.qyhstech.core.json;

import cn.hutool.core.util.StrUtil;

public class QyJson {

    /**
     * 清理Json格式
     *
     * @param json
     * @return
     */
    public static String getCleanJson(String json) {
        if (StrUtil.isEmpty(json)) {
            return "";
        }
        return json.replaceAll("^```json\n|```$", "");
    }
}
