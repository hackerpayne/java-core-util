package com.qyhstech.core.crypto;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QySign {


    /**
     * Map排序生成签名列表字符 Popup弹出指定的数据，Map排序
     *
     * @param hashParameters 要处理的参数列表
     * @param excludeList    排除的参数列表，不参与签名的
     * @return
     */
    public static String getSignStr(Map<String, Object> hashParameters, List<String> excludeList) {

        if (MapUtil.isEmpty(hashParameters)) {
            return null;
        }

        List<String> excludeNew = excludeList.stream().map(String::toLowerCase).toList();
        Map<String, Object> newHash = hashParameters.entrySet().stream()
                .filter(item -> StrUtil.isNotEmpty(item.getKey()) && !excludeNew.contains(item.getKey().toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, b) -> s));

        // 对Map进行ASCII字典排序，生成String格式，工具类由HuTool-Core提供
        return MapUtil.join(MapUtil.sort(newHash), "&", "=", false);
    }


}
