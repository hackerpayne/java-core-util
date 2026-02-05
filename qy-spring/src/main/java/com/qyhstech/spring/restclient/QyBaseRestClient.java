package com.qyhstech.spring.restclient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qyhstech.core.QyAssert;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.http.QyMime;
import com.qyhstech.core.http.QyUrl;
import com.qyhstech.core.io.QyFile;
import com.qyhstech.core.json.QyJackson;
import com.qyhstech.core.upload.QyUploadFileDto;
import com.qyhstech.spring.restclient.interceptor.RestClientLoggingRequestInterceptor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 特别注意：
 * 1、retrieve如果是4x/5x开头的状态码，会直接抛出异常，这时候要使用exchange
 */
@Getter
@Setter
@Slf4j
public abstract class QyBaseRestClient {

    protected RestClient restClient;

    /**
     * 认证使用的Token
     */
    protected String authToken;

    /**
     * 下载的基路径
     */
    protected String downloadBaseFolder;

    /**
     * 是否开启Debug模式
     */
    protected Boolean debug = Boolean.FALSE;

    /**
     * 请求的根URL
     */
    protected String baseUrl;

    /**
     * 下载的根网站，会拼接相对下载文件地址，形成完整的URL
     */
    protected String downloadBaseUrl;

    /**
     * 连接超时时间
     */
    protected Integer connectTimeout;

    /**
     * 读取数据超时时间
     */
    protected Integer readTimeout;

    /**
     * 构造函数
     */
    public QyBaseRestClient() {
        this(null, null, null);
    }

    /**
     * 构造函数
     *
     * @param downloadBaseFolder 基础文件夹路径
     * @param authToken          认证令牌
     */
    public QyBaseRestClient(String downloadBaseFolder, String authToken) {
        this(downloadBaseFolder, authToken, null);
    }

    /**
     * 构造函数
     *
     * @param downloadBaseFolder 基础文件夹路径
     * @param authToken          认证令牌
     * @param baseUrl            基础URL
     */
    /**
     * 构造函数
     *
     * @param downloadBaseFolder 基础文件夹路径
     * @param authToken          认证令牌
     * @param baseUrl            基础URL
     */
    public QyBaseRestClient(String downloadBaseFolder, String authToken, String baseUrl) {
        this(downloadBaseFolder, authToken, baseUrl, false);
    }

    /**
     * 构造函数
     *
     * @param lazyInit 是否延迟初始化
     */
    protected QyBaseRestClient(boolean lazyInit) {
        this(null, null, null, lazyInit);
    }

    /**
     * 构造函数
     *
     * @param downloadBaseFolder 基础文件夹路径
     * @param authToken          认证令牌
     * @param baseUrl            基础URL
     * @param lazyInit           是否延迟初始化
     */
    protected QyBaseRestClient(String downloadBaseFolder, String authToken, String baseUrl, boolean lazyInit) {
        this.downloadBaseFolder = downloadBaseFolder;
        this.authToken = authToken;
        this.baseUrl = baseUrl;

        if (!lazyInit) {
            this.restClient = buildRestClient();
        }
    }

    /**
     * 初始化构造方法
     */
    protected synchronized void initRestClientIfNecessary() {
        if (this.restClient == null) {
            this.restClient = this.buildRestClient();
        }
    }

    /**
     * 重新构建
     */
    protected synchronized void rebuildRestClient() {
        this.restClient = this.buildRestClient();
    }

    /**
     * 构建RestClient实例
     * 子类可以重写此方法来自定义RestClient配置
     */
    protected RestClient buildRestClient() {
        RestClient.Builder builder = RestClient.builder();

        if (Objects.nonNull(this.connectTimeout) && Objects.nonNull(this.readTimeout)) {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setReadTimeout(Duration.ofSeconds(this.readTimeout));
            httpRequestFactory.setConnectTimeout(Duration.ofSeconds(this.connectTimeout));
            builder.requestFactory(httpRequestFactory);
        } else {
            builder.requestFactory(new HttpComponentsClientHttpRequestFactory());
        }

        // 配置基础URL
        if (StrUtil.isNotEmpty(this.baseUrl)) {
            builder.baseUrl(this.baseUrl);
        }

        // 配置认证头
        configureAuth(builder);

        // 配置拦截器
        configureInterceptors(builder);

        // 配置其他默认设置
        configureDefaults(builder);

        // 允许子类进一步自定义
        customizeBuilder(builder);

        return builder.build();
    }

    /**
     * 配置认证
     * 子类可以重写此方法来实现不同的认证方式
     */
    protected void configureAuth(RestClient.Builder builder) {
        if (StrUtil.isNotEmpty(this.authToken)) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, this.authToken);
        }
    }

    /**
     * 配置拦截器
     * 子类可以重写此方法来添加自定义拦截器
     */
    protected void configureInterceptors(RestClient.Builder builder) {
        if (this.debug) {
            builder.requestInterceptor(new RestClientLoggingRequestInterceptor());
        }
    }

    /**
     * 配置默认设置
     * 子类可以重写此方法来添加默认配置
     */
    protected void configureDefaults(RestClient.Builder builder) {
        // 子类可以在这里添加默认配置
        // 例如：超时设置、默认头信息等
    }

    /**
     * 允许子类进一步自定义RestClient.Builder
     * 这是一个钩子方法，子类可以重写来添加特定的配置
     */
    protected void customizeBuilder(RestClient.Builder builder) {
        // 默认为空实现，子类可以重写
    }

    /**
     * 判断是否为字节数组类型
     */
    private boolean isByteArrayType(TypeReference<?> typeRef) {
        return Objects.nonNull(typeRef) && "byte[]".equals(typeRef.getType().getTypeName());
    }

    /**
     * 发送请求
     *
     * @param endpoint 请求节点
     * @param method   请求方式
     * @param <T>
     * @return
     */
    public <T> T makeRequest(String endpoint, Method method) {
        return makeRequest(endpoint, method, null, null);
    }

    /**
     * 发送请求
     *
     * @param endpoint
     * @param method
     * @param body
     * @param <T>
     * @return
     */
    public <T> T makeRequest(String endpoint, Method method, Object body) {
        return makeRequest(endpoint, method, body, null);
    }

    /**
     * 发送请求，支持多种不同的方式
     *
     * @param endpoint 节点，请求URL
     * @param method   请求方法
     * @param body     提交Body
     * @param typeRef  转换为结果
     * @param <T>
     * @return
     */
    public <T> T makeRequest(String endpoint, Method method, Object body, TypeReference<T> typeRef) {
        String url = StrUtil.format("{}{}",
                StrUtil.isNotEmpty(baseUrl) ? StrUtil.removeSuffix(baseUrl, "/") : StrUtil.EMPTY, endpoint);

        try {
            // 判断返回类型
            boolean isByteArray = isByteArrayType(typeRef);
            Class<?> responseType = isByteArray ? byte[].class : String.class;

            switch (method) {
                case Method.GET -> {
                    ResponseEntity<?> responseEntity = doGet(url, responseType);
                    return processResponse(responseEntity, typeRef, isByteArray);
                }
                case Method.POST -> {
                    ResponseEntity<?> responseEntity = doPost(url, body, responseType);
                    return processResponse(responseEntity, typeRef, isByteArray);
                }
                case Method.DELETE -> {
                    ResponseEntity<String> responseEntity = doDelete(url, String.class);
                    return processResponse(responseEntity, typeRef, isByteArray);
                }
                case null, default -> throw new RuntimeException("不支持的HTTP方法: " + method);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("timeout")) {
                throw new RuntimeException("请求超时: " + url);
            } else if (e.getMessage().contains("Connection")) {
                throw new RuntimeException("连接失败: " + e.getMessage());
            } else {
                throw new RuntimeException("请求失败: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 将字符串结果转换为指定的类
     *
     * @param response    字符串格式的返回值
     * @param typeRef     转换为的结果
     * @param isByteArray 是否是二进制数据
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T processResponse(ResponseEntity<?> response, TypeReference<T> typeRef, Boolean isByteArray) {
        try {
            Object body = response.getBody();

            if (isByteArray) {
                return (T) body;
            }

            if (Objects.nonNull(typeRef) && body instanceof String jsonString) {
                return QyJackson.parseObject(jsonString, typeRef);
            }

            return (T) body;
        } catch (Exception e) {
            throw new RuntimeException("处理响应失败: " + e.getMessage(), e);
        }
    }

    /**
     * 基础GET请求
     *
     * @param request
     * @param uri
     * @param responseType
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doGet(RestClient.RequestHeadersUriSpec<?> request, String uri,
                                          Class<T> responseType) {
        return request.uri(uri)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * GET请求
     *
     * @param uri          请求URL地址
     * @param responseType 指定返回数据类型
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doGet(String uri, Class<T> responseType) {
        return doGet(restClient.get(), uri, responseType);
    }

    /**
     * 指定Header请求
     *
     * @param uri
     * @param responseType
     * @param headers
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doGet(String uri, Class<T> responseType, Map<String, String> headers) {
        RestClient.RequestHeadersUriSpec<?> request = restClient.get();

        if (headers != null) {
            headers.forEach(request::header);
        }

        return doGet(request, uri, responseType);
    }

    /**
     * GET请求带参数
     *
     * @param uri          请求URL地址
     * @param params       参数列表
     * @param responseType 指定返回数据类型
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doGet(String uri, Map<String, Object> params, Class<T> responseType) {
        RestClient.RequestHeadersUriSpec<?> request = restClient.get();
        if (params != null && !params.isEmpty()) {
            StringBuilder uriBuilder = new StringBuilder(uri);
            if (!uri.contains("?")) {
                uriBuilder.append("?");
            }
            params.forEach((key, value) -> uriBuilder.append("&").append(key).append("=").append(value));
            uri = uriBuilder.toString();
        }

        return doGet(request, uri, responseType);
    }

    /**
     * 自定义请求GET参数
     *
     * @param uri          请求URL
     * @param params       请求参数
     * @param responseType 指定返回数据类型
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doGet(String uri, Function<UriBuilder, URI> params, Class<T> responseType) {
        RestClient.RequestHeadersUriSpec<?> request = restClient.get();
        return request.uri(uri, params)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * 直接发送Get请求
     *
     * @param uri          请求URL
     * @param params       请求参数
     * @param responseType 指定返回数据类型
     * @param <T>
     * @return
     */
    protected <T> T get(String uri, Function<UriBuilder, URI> params, Class<T> responseType) {
        RestClient.RequestHeadersUriSpec<?> request = restClient.get();
        return request.uri(uri, params)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .body(responseType);
    }

    /**
     * 直接发送Get请求，复杂类型可以直接指定数据格式
     *
     * @param uri           请求URL
     * @param params        请求参数
     * @param typeReference 返回值类型
     * @param <T>
     * @return
     */
    protected <T> T get(String uri, Function<UriBuilder, URI> params, ParameterizedTypeReference<T> typeReference) {
        RestClient.RequestHeadersUriSpec<?> request = restClient.get();
        return request.uri(uri, params)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .body(typeReference);
    }

    /**
     * POST请求
     *
     * @param uri          请求URL
     * @param body         请求内容
     * @param responseType 返回值类
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doPost(String uri, Object body, Class<T> responseType) {
        return restClient.post()
                .uri(uri)
                .body(body)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (request, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * Post
     *
     * @param uri           请求URL
     * @param body          请求内容
     * @param typeReference 返回值类型
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doPost(String uri, Object body, ParameterizedTypeReference<T> typeReference) {
        return restClient.post()
                .uri(uri)
                .body(body)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(typeReference);
    }

    /**
     * PostForm表单
     *
     * @param uri
     * @param body
     * @param responseType
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doPostForm(String uri, MultiValueMap<String, Object> body, Class<T> responseType) {
        return restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * PUT请求
     *
     * @param uri
     * @param body
     * @param responseType
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doPut(String uri, Object body, Class<T> responseType) {
        return restClient.put()
                .uri(uri)
                .body(body)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * DELETE请求
     *
     * @param uri
     * @param responseType
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> doDelete(String uri, Class<T> responseType) {
        return restClient.delete()
                .uri(uri)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * 上传文件
     * 上传文件时：builder.part("media_file", new FileSystemResource(filePath));
     *
     * @param uploadUrl    上传URL
     * @param builder      上传的数据对象
     * @param responseType 返回数据类型
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> upload(String uploadUrl, MultipartBodyBuilder builder, Class<T> responseType) {
        // 使用 MultipartBodyBuilder 构建 multipart/form-data
        // MultipartBodyBuilder builder = new MultipartBodyBuilder();
        // builder.part("media_file", new FileSystemResource(filePath)); // 表单字段名为
        // "file"
        // builder.part("action", "upload-media");

        return restClient.post()
                .uri(uploadUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(builder.build())
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, response) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(responseType);
    }

    /**
     * 下载文件到指定目录
     *
     * @param uri
     * @param fileName
     * @param callback
     * @throws IOException
     */
    protected void downloadFile(String uri, String fileName, Consumer<ResponseEntity<byte[]>> callback)
            throws IOException {

        QyAssert.notBlank(downloadBaseFolder, "baseFolder must not be blank");

        ResponseEntity<byte[]> response = restClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        status -> true, // 处理所有状态码（包括 4xx 和 5xx）
                        (req, resp) -> {
                            // 不抛出异常，让响应继续处理
                        })
                .toEntity(byte[].class);

        if (callback != null) {
            callback.accept(response);
        }

        // 创建目录
        File directory = new File(downloadBaseFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 保存文件
        File file = new File(directory, fileName);
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(response.getBody());
        }

        log.info("文件下载完成: {}", file.getAbsolutePath());
    }

    /**
     * 将远程文件下载到本地存储
     *
     * @param fileUrl  要下载的文件路径
     * @param fileName 保存的文件名
     * @return
     */
    public String downloadFile(String fileUrl, String fileName) {
        QyAssert.notBlank(downloadBaseFolder, "baseFolder must not be blank");

        // 本地保存路径 + 指定文件名
        File targetFile = new File(this.downloadBaseFolder, fileName);

        try {
            byte[] fileBytes = restClient.get()
                    .uri(fileUrl)
                    .accept(MediaType.ALL) // 接受所有媒体类型
                    .retrieve()
                    .body(byte[].class); // 直接获取 byte[]

            // 写入文件
            try (OutputStream outputStream = new FileOutputStream(targetFile)) {
                outputStream.write(fileBytes);
            }
            log.info("下载完成：{}", targetFile.getAbsolutePath());
            return targetFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param fileUrl
     * @param relativeSavePath
     * @return
     */
    public QyUploadFileDto downloadFileExt(String fileUrl, String relativeSavePath) {
        return downloadFileExt(fileUrl, relativeSavePath, new ArrayList<>(), null);
    }

    /**
     * 将远程文件下载到本地存储
     *
     * @param fileUrl          要下载的文件路径
     * @param relativeSavePath 保存文件路径
     * @param validExtensions  是否指定扩展名，如果不指定会挨个从源文件，目标文件，content-type中读取
     * @param defaultExtension 最后也没有扩展名，就来个默认值吧
     * @return
     */
    public QyUploadFileDto downloadFileExt(String fileUrl, String relativeSavePath, List<String> validExtensions,
                                           String defaultExtension) {

        QyAssert.notBlank(downloadBaseFolder, "baseFolder must not be blank");

        return restClient.get()
                .uri(fileUrl)
                .accept(MediaType.ALL) // 接受所有媒体类型
                .exchange((request, response) -> {

                    // 没有再使用保存文件的扩展名，再没有，再使用content-type中的类型进行扩展名识别
                    File targetFile = QyFile.file(this.downloadBaseFolder, relativeSavePath);

                    // 1. 获取 Content-Type
                    String contentType = response.getHeaders().getFirst("Content-Type");

                    // 2. 生成获取文件扩展名

                    // 如果保存目标有设置扩展名，则用之
                    String extension = QyFile.getFileExt(targetFile.getPath());// 获取保存文件中的扩展名
                    if (CollUtil.isNotEmpty(validExtensions) && !QyList.containsStr(validExtensions, extension)) {
                        extension = "";
                    }

                    // 保存没指定扩展名，就使用URL中的
                    if (StrUtil.isEmpty(extension)) {
                        extension = QyUrl.getFileExt(fileUrl);
                    }
                    if (CollUtil.isNotEmpty(validExtensions) && !QyList.containsStr(validExtensions, extension)) {
                        extension = "";
                    }

                    // URL中也没有，使用MIME中的
                    if (StrUtil.isEmpty(extension)) {
                        extension = QyMime.MIME_EXTENSION_MAP.getOrDefault(contentType.toLowerCase(), "");
                    }
                    if (CollUtil.isNotEmpty(validExtensions) && !QyList.containsStr(validExtensions, extension)) {
                        extension = "";
                    }

                    // 如果最终还没有扩展名，设置一个默认值给它
                    if (StrUtil.isEmpty(extension) && StrUtil.isNotEmpty(defaultExtension)) {
                        extension = defaultExtension;
                    }

                    String fileName = targetFile.getName();
                    if (StrUtil.isNotEmpty(extension)) {
                        targetFile = new File(targetFile.getParentFile(),
                                fileName + "." + QyStr.removePrefix(extension, "."));
                    }

                    // 3. 获取字节内容
                    byte[] bytes = response.getBody().readAllBytes();

                    // 4. 写入文件
                    try (OutputStream outputStream = new FileOutputStream(targetFile)) {
                        outputStream.write(bytes);
                    }

                    log.info("下载URL:{}，下载完成，本地路径为：{}", fileUrl, targetFile.getAbsolutePath());
                    QyUploadFileDto uploadFileDto = new QyUploadFileDto();
                    uploadFileDto.setStatus("downloaded");
                    uploadFileDto.setSourceUrl(fileUrl);

                    uploadFileDto.setFullPath(targetFile.getAbsolutePath());

                    String relativePath = QyFile.normalize(targetFile.getAbsolutePath().replace(this.downloadBaseFolder, ""));
                    uploadFileDto.setRelativePath(relativePath);
                    if (StrUtil.isNotEmpty(downloadBaseUrl)) {
                        uploadFileDto.setUrl(URLUtil.completeUrl(downloadBaseUrl, StrUtil.removePrefix(relativePath, "/")));
                    }

                    uploadFileDto.setOldName(QyUrl.getFileName(fileUrl));
                    uploadFileDto.setExt(StrUtil.isNotEmpty(extension) ? StrUtil.removePrefix(extension, ".") : ""); // 获取文件扩展名
                    return uploadFileDto;
                }); // 直接获取 byte[]
    }

    /**
     * 设置调试模式
     */
    public void setDebugMode(Boolean debug) {
        if (!this.debug.equals(debug)) {
            this.debug = debug;
            // 重新构建RestClient以应用调试设置
            this.restClient = buildRestClient();
        }
    }

    /**
     * 更新认证令牌
     * @param newToken
     */
    public void updateAuthToken(String newToken) {
        if (!StrUtil.equals(this.authToken, newToken)) {
            this.authToken = newToken;
            // 重新构建RestClient以应用新的认证令牌
            this.restClient = buildRestClient();
        }
    }

    /**
     * 获取当前RestClient实例
     * 供子类直接使用进行更复杂的操作
     */
    protected RestClient getRestClient() {
        return this.restClient;
    }

}
