package com.qyhstech.core.http;

import com.qyhstech.core.regex.QyRegex;
import com.qyhstech.core.QyStr;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Http请求工具类
 */
@Slf4j
public class QyHtmlUtil {

    /**
     * 删除A标签，只删除简单的A标签，带属性的
     *
     * @param input
     * @return
     */
    public static String clearATag(String input) {
//        return input.replaceAll( "</?a>", "" );
        return input.replaceAll("</?a.*?>", "");
    }

    /**
     * 清理HTML里面的多余标签
     *
     * @param htmlStr
     * @param length
     * @return
     */
    public static String delHtmlTag(String htmlStr, int length) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签
        htmlStr = QyStr.checkStr(htmlStr);
        return htmlStr.replaceAll("\\s*", "").substring(0, length) + "..."; //返回文本字符串
    }

    /**
     * 获取 ：<meta http-equiv="refresh" content="0; url=http://bbs.moonseo.cn/" />的跳转地址
     *
     * @param input
     * @return
     */
    public static String getMetaRefresh(String input) {
        String metaUrl = QyRegex.get("<(?:META|meta|Meta) (?:HTTP-EQUIV|http-equiv)=\"refresh\".*(URL|url)=(.*)\"", input, 2);

        metaUrl = QyStr.removeStart(metaUrl, "'");
        metaUrl = QyStr.removeEnd(metaUrl, "'");

        return metaUrl;
    }

}