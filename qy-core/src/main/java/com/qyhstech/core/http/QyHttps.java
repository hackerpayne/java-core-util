package com.qyhstech.core.http;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.qyhstech.core.httpclient.core.QyHttpClientGenerator;
import com.qyhstech.core.domain.response.QyResp;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QyHttps {

    /**
     *
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static CloseableHttpClient httpClient = QyHttpClientGenerator.custom().build();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        objectMapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        objectMapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, false);
        objectMapper.configure(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION, false);
        objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, true);
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, false);
        objectMapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, false);
        objectMapper.configure(MapperFeature.USE_STD_BEAN_NAMING, false);
        objectMapper.configure(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING, false);
        objectMapper.configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true);
        objectMapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, true);
        objectMapper.configure(MapperFeature.IGNORE_MERGE_FOR_UNMERGEABLE, true);
        objectMapper.configure(MapperFeature.BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES, false);
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false);
        objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false);
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false);
        objectMapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, false);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, false);
        objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);
        objectMapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        objectMapper.configure(SerializationFeature.WRAP_EXCEPTIONS, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true);
        objectMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false);
        objectMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, false);
        objectMapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        objectMapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUM_KEYS_USING_INDEX, false);
        objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        objectMapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, true);
        objectMapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, false);
    }

    /**
     * @param url
     * @param headers
     * @param data
     * @param tClass
     * @return
     * @throws Exception
     */
    public static <T> QyResp httpJsonPost(String url, Map<String, String> headers, Object data, Class<T> tClass) throws Exception {

        HttpPost httppost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        StringEntity s = new StringEntity(data instanceof String ? (String) data : objectMapper.writeValueAsString(data), ContentType.APPLICATION_JSON);
        httppost.setEntity(s);
        return sendAndreceive(tClass, httppost);
    }

    /**
     * @param url
     * @param headers
     * @param param
     * @param tClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> QyResp httpMultipartFormPost(String url, Map<String, String> headers, Map<String, Object> param, Class<T> tClass) throws Exception {
        HttpPost httppost = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (param != null) {
            final MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof File) {
                    multipartEntityBuilder.addBinaryBody(key, (File) value);
                } else if (value instanceof byte[]) {
                    multipartEntityBuilder.addBinaryBody(key, (byte[]) value);
                } else if (value instanceof InputStream) {
                    multipartEntityBuilder.addBinaryBody(key, (InputStream) value);
                } else {
                    StringBody comment = new StringBody(value.toString(), ContentType.TEXT_XML);
                    multipartEntityBuilder.addPart(key, comment);
                }
            }
            httppost.setEntity(multipartEntityBuilder.build());
        }
        return sendAndreceive(tClass, httppost);
    }

    private static <T> QyResp sendAndreceive(Class<T> tClass, ClassicHttpRequest request) throws IOException, ParseException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int code = response.getCode();
            String reasonPhrase = response.getReasonPhrase();
            QyResp responseData = QyResp.success();
            responseData.setCode(code);
            responseData.setMsg(reasonPhrase);
            if (code >= HttpStatus.SC_SUCCESS && code < HttpStatus.SC_REDIRECTION) {
                String data = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                if (tClass.isAssignableFrom(String.class)) {
                    responseData.setData((T) data);
                } else {
                    responseData.setData(objectMapper.readValue(data, tClass));
                }
            }
            EntityUtils.consume(response.getEntity());
            return responseData;
        }
    }

    public static <T> QyResp httpPostForm(String url, Map<String, String> headers, Map<String, Object> param, Class<T> tClass) throws Exception {
        ClassicRequestBuilder build = ClassicRequestBuilder.post().setUri(new URI(url));
        ;
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                build.addParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        return sendAndreceive(tClass, build.build());
    }


    public static <T> QyResp httpGet(String url, Map<String, Object> headers, Class<T> tClass) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return sendAndreceive(tClass, httpGet);
    }

    public static void main(String[] args) throws Exception {
//        try{
//               HttpGet httpGet = new HttpGet("http://cis.vivo.xyz/cis-admin/health");
//            httpGet.addHeader("cistoken","SMZulfWkOkfdapZInhwT.yrNbHCZhuswrtwJZT9HjNcLi_Xj1sGBUL5eaA6JGNMv9cDheVHyPnY*");
//           try ( CloseableHttpResponse response = httpClient.execute(httpGet)){
//               final int code = response.getCode();
//               final String reasonPhrase = response.getReasonPhrase();
//               System.out.println(code +"\t"+ reasonPhrase);
//               final HttpEntity entity = response.getEntity();
//               System.out.println(EntityUtils.toString(entity, StandardCharsets.UTF_8));
//               EntityUtils.consume(entity);
//           }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cistoken", "SMZulfWkOkfdapZInhwT.yrNbHCZhuswrtwJZT9HjNcLi_Xj1sGBUL5eaA6JGNMv9cDheVHyPnY*");
//        Response<String> objectResponse = httpGet("http://cis.vivo.xyz/cis-admin/health", map, String.class);
//        System.out.println(objectResponse)
//        httpClient =  HttpClientConfigUtils.custom()  .addRequestInterceptorFirst(new HttpRequestInterceptor() {
//            @Override
//            public void process(HttpRequest request,EntityDetails entity,HttpContext context) throws HttpException, IOException {
//                request.setHeader("request-id", UUID.randomUUID());
//            }
//        }) .addExecInterceptorAfter(ChainElement.PROTOCOL.name(), "custom", (request, scope, chain) -> {
//                    System.out.println("addExecInterceptorAfter");
//                        return chain.proceed(request, scope);
//                })
//                .build();
//        Response<String> objectResponse = httpPostForm("http://127.0.0.1:9998/web/add", headers,param, String.class);
//        System.out.println(objectResponse);
//        HashMap<String, Object> param = new HashMap<>();
//        param.put("cistoken","SMZulfWkOkfdapZInhwT.yrNbHCZhuswrtwJZT9HjNcLi_Xj1sGBUL5eaA6JGNMv9cDheVHyPnY*");
//        param.put("cistoken1",1);
//        param.put("cistoken2",new Date());
//        param.put("cistoken3",new File("E:\\workspace\\httpcomponents-client-rel-v5.2-alpha1\\httpclient5\\src\\main\\java\\org\\apache\\hc\\client5\\http\\entity\\mime\\MultipartEntityBuilder.java"));
//        param.put("cistoken4","E:\\workspace\\httpcomponents-client-rel-v5.2-awod中文lient5\\http\\entity\\mime\\MultipartEntityBuilder.java".getBytes(StandardCharsets.UTF_8));
//        param.put("cistoken5",new FileInputStream("E:\\workspace\\httpcomponents-client-rel-v5.2-alpha1\\httpclient5\\src\\main\\java\\org\\apache\\hc\\client5\\http\\entity\\mime\\MultipartEntityBuilder.java"));
//        Response<String> objectResponse = httpMultipartFormPost("http://127.0.0.1:9998/web/uploadsinglefile", headers,param, String.class);
//        System.out.println(objectResponse);
//            TarFileInfo tarFileInfo = new TarFileInfo();
//            tarFileInfo.setFilename("hello world");
//            tarFileInfo.setPath("ddddd");
//            Response<String> stringResponse = httpJsonPost("http://127.0.0.1:9998/web/downzipbyZipArchive", headers, tarFileInfo, String.class);
//            System.out.println(stringResponse);
    }

}


