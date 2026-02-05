package com.qyhstech.core.io;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * 脱敏数据处理工具类
 */
public class QyMask extends DesensitizedUtil {

    /**
     * 对字符串进行脱敏操作
     *
     * @param origin          原始字符串
     * @param prefixNoMaskLen 左侧需要保留几位明文字段
     * @param suffixNoMaskLen 右侧需要保留几位明文字段
     * @param maskStr         用于遮罩的字符串, 如'*'
     * @return 脱敏后结果
     */
    public static String middleMask(String origin, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
        if (origin == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = origin.length(); i < n; i++) {
            if (i < prefixNoMaskLen) {
                sb.append(origin.charAt(i));
                continue;
            }
            if (i > (n - suffixNoMaskLen - 1)) {
                sb.append(origin.charAt(i));
                continue;
            }
            sb.append(maskStr);
        }
        return sb.toString();
    }

    /**
     * 【密钥】密钥除了最后三位，全部都用*代替，比如：***xdS 脱敏后长度为6，如果明文长度不足三位，则按实际长度显示，剩余位置补*
     * @param key 密钥
     * @return 结果
     */
    public static String key(String key) {
        if (key == null) {
            return null;
        }
        int viewLength = 6;
        StringBuilder tmpKey = new StringBuilder(middleMask(key, 0, 3, "*"));
        if (tmpKey.length() > viewLength) {
            return tmpKey.substring(tmpKey.length() - viewLength);
        }
        else if (tmpKey.length() < viewLength) {
            int buffLength = viewLength - tmpKey.length();
            for (int i = 0; i < buffLength; i++) {
                tmpKey.insert(0, "*");
            }
            return tmpKey.toString();
        }
        else {
            return tmpKey.toString();
        }
    }

    /**
     * 自定义脱敏规则实现
     *
     * @param text 脱敏文字
     * @param rule 脱敏规则
     * @return
     */
    public static String commonMask(String text, QyMaskRule rule) {
        if (StrUtil.isBlank(text) || Objects.isNull(rule)) {
            return text;
        }
        int length = text.length();

        Integer type = rule.getType(); // 0：隐藏，1：显示
        Integer scope = rule.getScope();  // 开头:0 中间:1 末尾: -1 全部: 2 区间：3
        Integer count = rule.getCount();
        Integer start = rule.getStart();
        Integer end = rule.getEnd();

        StringBuilder ms = new StringBuilder();

        switch (scope) {
            case 0: // 开头count位
                for (int i = 0; i < length; i++) {
                    if (i < count) {
                        if (type == 0) {
                            ms.append("*");
                        }
                        if (type == 1) {
                            ms.append(text.charAt(i));
                        }
                    } else {
                        if (type == 0) {
                            ms.append(text.charAt(i));
                        }
                        if (type == 1) {
                            ms.append("*");
                        }
                    }
                }
                break;
            case 1:  // 处理中间count位
                int mid = length / 2;
                int left = count / 2 - (count % 2 == 0 ? 0 : 1);
                int right = count / 2;
                left = mid - left;
                left = Math.max(left, 0);
                right = mid + right - 1;
                for (int i = 0; i < length; i++) {
                    if (i >= left && i <= right) {
                        if (type == 0) {
                            ms.append("*");
                        }
                        if (type == 1) {
                            ms.append(text.charAt(i));
                        }
                    } else {
                        if (type == 0) {
                            ms.append(text.charAt(i));
                        }
                        if (type == 1) {
                            ms.append("*");
                        }
                    }
                }
                break;
            case -1:// 末尾屏蔽count位
                int n = length - count;
                n = Math.max(n, 0);
                for (int i = 0; i < length; i++) {
                    if (i >= n) {
                        if (type == 0) {
                            ms.append("*");
                        }
                        if (type == 1) {
                            ms.append(text.charAt(i));
                        }
                    } else {
                        if (type == 0) ms.append(text.charAt(i));
                        if (type == 1) ms.append("*");
                    }
                }
                break;
            case 2:// 全部
                for (int i = 0; i < length; i++) {
                    if (type == 0) {
                        ms.append("*");
                    }
                    if (type == 1) {
                        ms.append(text.charAt(i));
                    }
                }
                break;
            case 3:// 区间
                for (int i = 0; i < length; i++) {
                    if (i >= start - 1 && i <= end - 1) {
                        if (type == 0) {
                            ms.append("*");
                        }
                        if (type == 1) {
                            ms.append(text.charAt(i));
                        }
                    } else {
                        if (type == 0) {
                            ms.append(text.charAt(i));
                        }
                        if (type == 1) {
                            ms.append("*");
                        }
                    }
                }
                break;
            default:
                break;

        }
        return ms.toString();
    }

}
