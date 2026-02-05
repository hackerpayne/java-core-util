package com.qyhstech.spring.i18n;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * 获取请求头国际化信息
 */
public class I18nLocaleResolver implements LocaleResolver {

    /**
     * 从请求头中解析使用的语言来显示内容
     *
     * @param httpServletRequest
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String language = httpServletRequest.getHeader("content-language");
        Locale locale = Locale.getDefault();
        if (StrUtil.isNotEmpty(language)) {
            String[] split = language.split("_");
//            locale = new Locale(split[0], split[1]);
            locale = Locale.of(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
