package com.qyhstech.core.http;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.collection.QyMap;
import com.qyhstech.core.json.QyJackson;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@UtilityClass
public class QyParam {

    /**
     * 解析URL的QueryString到Map中去
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlPara(String url) {
//        Map<String, String> mapResult = new HashMap<String, String>();
//
//        if (QyStr.isEmpty(url)) {
//            return mapResult;
//        }
//
//        String queryStr;
//        if (!url.contains("?")) {
//            queryStr = url;
//        } else {
//            queryStr = getUrlQueryString(url);
//        }
//
//        if (QyStr.isEmpty(queryStr)) {
//            return mapResult;
//        }
//
//        mapResult = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(queryStr);
//        return mapResult;
        Map<String, String> map = new HashMap<>();
        Map<CharSequence, CharSequence> queryMap = UrlQuery.of(url, Charset.defaultCharset()).getQueryMap();
        queryMap.forEach((k, v) -> map.put(k.toString(), v.toString()));
        return map;
    }

    /**
     * 获取URL中的Query中的某个参数，
     * 比如：url=www.ok.com/?sid=1&aid=2 可以直接获取 getUrlPara(url,"sid"); //直接取出某个参数值
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrlPara(String url, String... params) {
        String value = "";
        if (QyStr.isBlank(url)) {
            return value;
        }
        Map<String, String> m = getUrlPara(url);
        for (String param : params) {
            String item = m.get(param);
            if (QyStr.isEmpty(item)) {
                continue;
            }
            value = URLDecoder.decode(item, StandardCharsets.UTF_8);
            break;
        }

        return value;
    }

    /**
     * 去掉url中的路径，留下请求参数部分，只保留？后面的结果
     *
     * @param strUrl url地址
     * @return url请求参数部分
     * @author lzf
     */
    public static String getUrlQueryString(String strUrl) {
        URI uri = URLUtil.toURI(strUrl);
        return uri.getQuery();
    }

    /**
     * 将表单数据加到URL中（用于GET表单提交）
     *
     * @param url       URL
     * @param queryPara 表单数据
     * @return 合成后的URL
     */
    public static String urlWithForm(String url, Map<String, String> queryPara) throws UnsupportedEncodingException {
//        final String queryString = toParams(queryPara, CharsetUtil.UTF_8);
        final String queryString = mapToQueryString(queryPara);
        return urlWithForm(url, queryString);
    }

    /**
     * 将表单数据字符串加到URL中（用于GET表单提交）
     *
     * @param url         URL
     * @param queryString 表单数据字符串
     * @return 拼接后的字符串
     */
    public static String urlWithForm(String url, String queryString) {

        StringBuilder sb = new StringBuilder();

        sb.append(url);
        if (QyStr.isNotBlank(queryString)) {
            sb.append(url.contains("?") ? "&" : "?");
            sb.append(queryString);
        }

        return sb.toString();
    }

    /**
     * 参数为List时，按ASCII排序,连接参数转换为String字符串
     * 常用于签名排序参数时使用
     *
     * @param list List参数列表
     * @return
     */
    public static String sortParm(List<String> list) {

        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 构造签名键值对的格式
        StringBuilder sb = new StringBuilder();
        for (String ss : list) {
            sb.append(ss).append("&");
        }
        return sb.toString();
    }


    /**
     * 将map型转为请求参数型
     *
     * @param data
     * @return
     */
    public static String mapToQueryString(Map<String, String> data) {

//        return mapParams.entrySet().stream().map(p -> {
//            try {
//                return QyUrl.encodeUtf8(p.getKey()) + "=" + QyUrl.encodeUtf8(p.getValue().toString());
//            } catch (UnsupportedEncodingException e) {
//                throw new RuntimeException(e);
//            }
//        }).reduce((p1, p2) -> p1 + "&" + p2).orElse("");

        StringBuilder sb = new StringBuilder();
        for (var i : data.entrySet()) {
            sb.append(i.getKey()).append("=").append(QyUrl.encodeUtf8(i.getValue())).append("&");
        }
        return StrUtil.removeSuffix(sb.toString(), "&");
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr 参数字符串（或者带参数的Path）
     * @param charset   字符集
     * @return 参数Map
     */
    public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
        if (QyStr.isBlank(paramsStr)) {
            return Collections.emptyMap();
        }

        // 去掉Path部分
        int pathEndPos = paramsStr.indexOf('?');
        if (pathEndPos > 0) {
            paramsStr = StrUtil.subSuf(paramsStr, pathEndPos + 1);
        }

        final Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
        final int len = paramsStr.length();
        String name = null;
        int pos = 0; // 未处理字符开始位置
        int i; // 未处理字符结束位置
        char c; // 当前字符
        for (i = 0; i < len; i++) {
            c = paramsStr.charAt(i);
            if (c == '=' && name == null) { // 键值对的分界点
                if (pos != i) {
                    name = paramsStr.substring(pos, i);
                }
                pos = i + 1;
            } else if (c == '&' || c == ';') { // 参数对的分界点
                if (name == null && pos != i) {
                    // 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
                    addParam(params, paramsStr.substring(pos, i), QyStr.EMPTY, charset);
                } else if (name != null) {
                    addParam(params, name, paramsStr.substring(pos, i), charset);
                    name = null;
                }
                pos = i + 1;
            }
        }

        //处理结尾
        if (pos != i) {
            if (name == null) {
                addParam(params, paramsStr.substring(pos, i), QyStr.EMPTY, charset);
            } else {
                addParam(params, name, paramsStr.substring(pos, i), charset);
            }
        } else if (name != null) {
            addParam(params, name, QyStr.EMPTY, charset);
        }

        return params;
    }

    /**
     * Map转换为List Parameter
     * Map转换为List列表的参数
     *
     * @param basicNameValueMap
     * @return
     */
    public static List<BasicNameValuePair> mapToList(Map<String, String> basicNameValueMap) {
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<BasicNameValuePair>();
        // 转换一下map 给list
        if (basicNameValueMap != null && !basicNameValueMap.isEmpty()) {
            Set<String> keySet = basicNameValueMap.keySet();
            BasicNameValuePair basicNameValuePair;
            for (String key : keySet) {
                basicNameValuePair = new BasicNameValuePair(key, basicNameValueMap.get(key));
                basicNameValuePairs.add(basicNameValuePair);
            }
        }
        return basicNameValuePairs;
    }

    /**
     * 将键值对加入到值为List类型的Map中
     *
     * @param params  参数
     * @param name    key
     * @param value   value
     * @param charset 编码
     */
    private static void addParam(Map<String, List<String>> params, String name, String value, String charset) {
        List<String> values = params.get(name);
        if (values == null) {
            values = new ArrayList<String>(1); // 一般是一个参数
            params.put(name, values);
        }
        values.add(QyUrl.urlDecode(value, charset));
    }

    /**
     * 参数为Map时按ASCII字典排序，生成string格式
     *
     * @param map Map参数列表
     * @return
     */
    public static String mapToSortedQueryString(Map map) {

        String result = "";
        try {
            List<Map.Entry<?, ?>> infoIds = new ArrayList<Map.Entry<?, ?>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            infoIds.sort(new Comparator<Map.Entry<?, ?>>() {
                @Override
                public int compare(Map.Entry<?, ?> o1, Map.Entry<?, ?> o2) {
                    return (o1.getKey().toString()).compareTo(o2.getKey().toString());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<?, ?> item : infoIds) {
                if (StrUtil.isNotEmpty(item.getKey().toString())) {
                    String key = item.getKey().toString();
                    String val = item.getValue().toString();
                    if (StrUtil.isNotEmpty(val)) {
                        sb.append(key).append("=").append(val).append("&");
                    }
                }
            }
            if (sb.toString().endsWith("&")) {
                sb.deleteCharAt(sb.length() - 1);
            }

            result = sb.toString();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * List转换为Query查询字符串
     *
     * @param basicNameValuePairs
     * @return
     */
    public static String paramToQueryString(List<BasicNameValuePair> basicNameValuePairs) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        if (basicNameValuePairs != null && !basicNameValuePairs.isEmpty()) {
            for (BasicNameValuePair basicNameValuePair : basicNameValuePairs) {
                if (index == 0) {
                    result.append("?");
                } else {
                    result.append("&");
                }
                result.append(basicNameValuePair.getName()).append("=").append(basicNameValuePair.getValue());
                index++;
            }
        }
        return result.toString();
    }

    /**
     * 将request中的参数转换成Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = QyMap.empty();
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.substring(1));
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * <p>
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersWith(ServletRequest request, String prefix) {
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (pre.isEmpty() || paramName.startsWith(pre)) {
                String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    values = new String[]{};
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
     *
     * @param params
     * @param prefix
     * @return
     */
    public static String encodeParameterWithPrefix(Map<String, Object> params, String prefix) {
        StringBuilder queryStringBuilder = new StringBuilder();

        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append("&");
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 通用请求格式转换，Json和Form统一输出为Map格式
     *
     * @param httpServletRequest
     * @return
     */
    public static Map<String, String> commonHttpRequestParamConvert(HttpServletRequest httpServletRequest) {
        Map<String, String> params = new HashMap<>();
        try {
            Map<String, String[]> requestParams = httpServletRequest.getParameterMap();
            if (requestParams != null && !requestParams.isEmpty()) {
                requestParams.forEach((key, value) -> params.put(key, value[0]));
            } else {
                StringBuilder paramSb = new StringBuilder();
                try {
                    String str = "";
                    BufferedReader br = httpServletRequest.getReader();
                    while ((str = br.readLine()) != null) {
                        paramSb.append(str);
                    }
                } catch (Exception e) {
                    System.out.println("httpServletRequest get requestbody error, cause : " + e);
                }
                if (!paramSb.isEmpty()) {
                    JSONObject paramJsonObject = JSON.parseObject(paramSb.toString());
                    if (paramJsonObject != null && !paramJsonObject.isEmpty()) {
                        paramJsonObject.forEach((key, value) -> params.put(key, String.valueOf(value)));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("commonHttpRequestParamConvert error, cause : " + e);
        }
        return params;
    }

    /**
     * 把实体类转换为MultiValueMap请求参数
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> MultiValueMap<String, Object> buildMultiValueMap(T entity) {
        return buildMultiValueMap(entity, null);
    }

    /**
     * 把实体类转换为MultiValueMap请求参数
     *
     * @param entity    需要处理的对象
     * @param appendMap 追加的Map值，加到最前面
     * @param <T>
     * @return
     */
    public static <T> MultiValueMap<String, Object> buildMultiValueMap(T entity, MultiValueMap<String, Object> appendMap) {
        MultiValueMap<String, Object> multiValueMap = MapUtil.isNotEmpty(appendMap) ? appendMap : new LinkedMultiValueMap<>();

        // 将对象转为 Map<String, Object>
        Map<String, Object> map = QyJackson.beanToMap(entity);

        // 转换成 Map<String, String> 并加入 MultiValueMap
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                multiValueMap.add(entry.getKey(), entry.getValue());
            }
        }

        return multiValueMap;
    }

}
