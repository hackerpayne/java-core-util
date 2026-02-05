# QyBaseRestClient 使用说明

## 概述

`QyBaseRestClient` 是一个基于 Spring Framework 6.x 的 `RestClient` 构建的抽象基础REST客户端类。它提供了完整的HTTP通信功能，包括GET、POST、PUT、DELETE请求，以及文件上传下载等高级功能。该类采用建造者模式设计，支持灵活的认证配置、超时设置和拦截器扩展。

## 核心特性

- ✅ **完整的HTTP方法支持** - GET、POST、PUT、DELETE
- ✅ **文件上传下载** - 支持多部分表单上传和文件下载
- ✅ **灵活的认证方式** - 支持自定义认证头配置
- ✅ **超时控制** - 可配置连接超时和读取超时
- ✅ **调试模式** - 支持请求响应日志拦截
- ✅ **可扩展拦截器** - 支持添加自定义拦截器
- ✅ **文件扩展名智能处理** - 自动识别和处理文件扩展名
- ✅ **基础URL配置** - 支持统一的请求前缀

## 类结构

### 核心字段

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `restClient` | RestClient | Spring WebClient实例 |
| `authToken` | String | 认证令牌 |
| `baseFolder` | String | 文件下载基础目录 |
| `debug` | Boolean | 调试模式开关 |
| `baseUrl` | String | 请求基础URL |
| `connectTimeout` | Integer | 连接超时时间（秒） |
| `readTimeout` | Integer | 读取超时时间（秒） |

## 构造函数

### 1. 无参构造函数
```java
public QyBaseRestClient()
```
创建一个默认配置的REST客户端实例。

### 2. 双参构造函数
```java
public QyBaseRestClient(String baseFolder, String authToken)
```
创建带有基础文件夹路径和认证令牌的客户端。

**参数说明：**
- `baseFolder`: 文件下载的基础目录路径
- `authToken`: HTTP认证令牌（通常为Bearer token）

### 3. 全参构造函数
```java
public QyBaseRestClient(String baseFolder, String authToken, String baseUrl)
```
创建完整配置的REST客户端。

**参数说明：**
- `baseFolder`: 文件下载的基础目录路径
- `authToken`: HTTP认证令牌
- `baseUrl`: 请求的基础URL前缀

## 核心方法详解

### 1. HTTP请求方法

#### 1.1 GET请求 - 基础版本
```java
protected <T> ResponseEntity<T> doGet(String uri, Class<T> responseType)
```

**参数：**
- `uri`: 请求路径（可以是完整URL或相对路径）
- `responseType`: 响应数据类型Class对象

**返回：**
- `ResponseEntity<T>`: 包含响应头和响应体的实体对象

**示例：**
```java
ResponseEntity<String> response = doGet("/api/user", String.class);
```

#### 1.2 GET请求 - 带参数
```java
protected <T> ResponseEntity<T> doGet(String uri, Map<String, Object> params, Class<T> responseType)
```

**参数：**
- `uri`: 请求路径
- `params`: URL查询参数Map
- `responseType`: 响应数据类型

**示例：**
```java
Map<String, Object> params = new HashMap<>();
params.put("page", 1);
params.put("size", 10);
ResponseEntity<User> response = doGet("/api/users", params, User.class);
```

#### 1.3 GET请求 - Function参数构建
```java
protected <T> T get(String uri, Function<UriBuilder, URI> params, Class<T> responseType)
```

**参数：**
- `uri`: 请求路径
- `params`: URI构建函数，支持更复杂的参数构造
- `responseType`: 响应数据类型

**示例：**
```java
// 使用UriBuilder构造复杂参数
T result = get("/api/users/{id}", uriBuilder ->
    uriBuilder.path("/api/users/{id}")
              .queryParam("include", "profile")
              .queryParam("expand", "posts")
              .build(userId)
, User.class);
```

#### 1.4 GET请求 - 参数化类型支持
```java
protected <T> T get(String uri, Function<UriBuilder, URI> params, ParameterizedTypeReference<T> typeReference)
```

**用途：** 支持复杂的泛型类型，如 `List<User>`

**示例：**
```java
ParameterizedTypeReference<List<User>> typeRef =
    new ParameterizedTypeReference<List<User>>() {};

List<User> users = get("/api/users", uri -> uri.build(), typeRef);
```

#### 1.5 POST请求
```java
protected <T> ResponseEntity<T> doPost(String uri, Object body, Class<T> responseType)
protected <T> ResponseEntity<T> doPost(String uri, Object body, ParameterizedTypeReference<T> typeReference)
```

**参数：**
- `uri`: 请求路径
- `body`: 请求体对象（任意Java对象）
- `responseType`: 响应数据类型

**示例：**
```java
// 简单对象
User user = new User();
user.setName("张三");
ResponseEntity<User> response = doPost("/api/users", user, User.class);

// 复杂泛型类型
ParameterizedTypeReference<ApiResponse<User>> typeRef =
    new ParameterizedTypeReference<ApiResponse<User>>() {};
ResponseEntity<ApiResponse<User>> response =
    doPost("/api/users", user, typeRef);
```

#### 1.6 POST表单请求
```java
protected <T> ResponseEntity<T> doPostForm(String uri, MultiValueMap<String, Object> body, Class<T> responseType)
```

**用途：** 提交表单数据（application/x-www-form-urlencoded）

**示例：**
```java
MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
formData.add("username", "user123");
formData.add("password", "password123");
formData.add("remember", "true");

ResponseEntity<LoginResult> response =
    doPostForm("/api/login", formData, LoginResult.class);
```

#### 1.7 PUT请求
```java
protected <T> ResponseEntity<T> doPut(String uri, Object body, Class<T> responseType)
```

**示例：**
```java
User updateUser = new User();
updateUser.setName("李四");
ResponseEntity<User> response = doPut("/api/users/123", updateUser, User.class);
```

#### 1.8 DELETE请求
```java
protected <T> ResponseEntity<T> doDelete(String uri, Class<T> responseType)
```

**示例：**
```java
ResponseEntity<Void> response = doDelete("/api/users/123", Void.class);
```

### 2. 文件上传方法

#### 2.1 文件上传
```java
protected <T> ResponseEntity<T> upload(String uploadUrl, MultipartBodyBuilder builder, Class<T> responseType)
```

**用途：** 上传文件（multipart/form-data）

**示例：**
```java
// 创建多部分构建器
MultipartBodyBuilder builder = new MultipartBodyBuilder();

// 添加文件（从File对象）
builder.part("file", new File("/path/to/document.pdf"));

// 添加其他表单字段
builder.part("action", "upload-document");
builder.part("category", "report");

// 执行上传
ResponseEntity<UploadResult> response =
    upload("/api/upload", builder, UploadResult.class);

// 获取上传结果
UploadResult result = response.getBody();
String fileUrl = result.getUrl();
```

**上传文件进阶示例：**
```java
// 多文件上传
MultipartBodyBuilder builder = new MultipartBodyBuilder();

// 添加多个文件
builder.part("files", new File("/path/to/file1.pdf"));
builder.part("files", new File("/path/to/file2.pdf"));

// 添加元数据
Map<String, Object> metadata = Map.of("tags", Arrays.asList("important", "report"));
builder.part("metadata", metadata, MediaType.APPLICATION_JSON);

ResponseEntity<BatchUploadResult> response =
    upload("/api/upload/batch", builder, BatchUploadResult.class);
```

### 3. 文件下载方法

#### 3.1 基础文件下载
```java
public String downloadFile(String fileUrl, String fileName)
```

**功能：** 下载远程文件到本地基础目录

**参数：**
- `fileUrl`: 远程文件URL
- `fileName`: 本地保存的文件名

**返回：**
- `String`: 下载文件的绝对路径

**示例：**
```java
// 下载文件到配置的baseFolder
String localPath = downloadFile("https://example.com/document.pdf", "document.pdf");
System.out.println("文件下载到: " + localPath);
```

**注意事项：** 使用此方法前必须通过构造函数设置 `baseFolder`

#### 3.2 回调式文件下载
```java
protected void downloadFile(String uri, String fileName, Consumer<ResponseEntity<byte[]>> callback) throws IOException
```

**功能：** 下载文件并提供回调处理响应

**参数：**
- `uri`: 文件URL
- `fileName`: 本地文件名
- `callback`: 响应回调函数

**示例：**
```java
// 下载文件并处理响应
downloadFile("https://example.com/image.jpg", "image.jpg", response -> {
    HttpHeaders headers = response.getHeaders();
    String contentType = headers.getFirst(HttpHeaders.CONTENT_TYPE);
    Long contentLength = headers.getContentLength();

    System.out.println("Content-Type: " + contentType);
    System.out.println("Content-Length: " + contentLength);
});

// 文件会自动保存到baseFolder目录
```

#### 3.3 扩展文件下载
```java
public QyUploadFileDto downloadFileExt(String fileUrl, String relativeSavePath)
public QyUploadFileDto downloadFileExt(String fileUrl, String relativeSavePath,
                                     List<String> validExtensions, String defaultExtension)
```

**功能：** 智能文件下载，自动识别和设置文件扩展名

**参数：**
- `fileUrl`: 远程文件URL
- `relativeSavePath`: 相对于baseFolder的保存路径
- `validExtensions`: 有效扩展名列表（可选）
- `defaultExtension`: 默认扩展名（可选）

**返回：**
- `QyUploadFileDto`: 包含文件信息的传输对象

**扩展名识别优先级：**
1. 保存路径中指定的扩展名
2. URL中的文件扩展名
3. Content-Type头中的MIME类型映射

**示例：**
```java
// 简单下载（自动识别扩展名）
QyUploadFileDto result1 = downloadFileExt(
    "https://example.com/api/file/123",
    "documents/report"
);
System.out.println("文件路径: " + result1.getFullPath());
System.out.println("扩展名: " + result1.getExt());

// 指定有效扩展名和默认值
List<String> validExts = Arrays.asList("pdf", "doc", "docx");
QyUploadFileDto result2 = downloadFileExt(
    "https://example.com/api/file/456",
    "documents/manual",
    validExts,
    "pdf"  // 如果无法识别，使用pdf
);
System.out.println("文件保存到: " + result2.getFullPath());
```

## 配置方法

### 1. 认证配置

#### 自定义认证头
```java
@Override
protected void configureAuth(RestClient.Builder builder) {
    if (StrUtil.isNotEmpty(this.authToken)) {
        // Bearer Token认证
        builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.authToken);

        // 或者其他认证方式
        // builder.defaultHeader("X-API-Key", this.authToken);

        // 多重认证头
        // builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.authToken);
        // builder.defaultHeader("X-Client-ID", clientId);
    }
}
```

#### API Key认证示例
```java
@Override
protected void configureAuth(RestClient.Builder builder) {
    if (StrUtil.isNotEmpty(this.authToken)) {
        builder.defaultHeader("X-API-Key", this.authToken);
        builder.defaultHeader("Accept", "application/json");
    }
}
```

### 2. 拦截器配置

#### 添加自定义拦截器
```java
@Override
protected void configureInterceptors(RestClient.Builder builder) {
    if (this.debug) {
        builder.requestInterceptor(new RestClientLoggingRequestInterceptor());
    }

    // 添加自定义拦截器
    builder.requestInterceptor((request, body, execution) -> {
        // 添加请求前逻辑
        request.getHeaders().add("X-Custom-Header", "custom-value");

        // 执行请求
        ClientHttpResponse response = execution.execute(request, body);

        // 添加响应后逻辑
        return response;
    });
}
```

### 3. 默认配置

```java
@Override
protected void configureDefaults(RestClient.Builder builder) {
    // 设置默认请求头
    builder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

    // 设置默认Content-Type
    // 注意：Content-Type通常在具体请求中设置

    // 设置用户代理
    builder.defaultHeader(HttpHeaders.USER_AGENT, "QyRestClient/1.0");
}
```

### 4. 高级定制

```java
@Override
protected void customizeBuilder(RestClient.Builder builder) {
    // 进一步自定义配置
    // 例如：添加自定义的HTTP客户端配置

    // 配置重试策略（需要额外依赖）
    // builder.requestInterceptor(new RetryInterceptor(...));

    // 配置缓存
    // builder.requestInterceptor(new CacheInterceptor(...));
}
```

## 使用示例

### 1. 基础REST客户端实现

```java
import com.qyhstech.spring.restclient.QyBaseRestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClient extends QyBaseRestClient {

    // 构造函数：推荐使用注解配置
    public UserServiceClient() {
        super("/data/files", "your-auth-token", "https://api.example.com");
    }

    // 获取用户列表
    public ResponseEntity<List<User>> getUsers(Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        return doGet("/api/v1/users", params, getParameterizedType(List.class, User.class));
    }

    // 获取单个用户
    public ResponseEntity<User> getUser(Long userId) {
        return doGet("/api/v1/users/" + userId, User.class);
    }

    // 创建用户
    public ResponseEntity<User> createUser(User user) {
        return doPost("/api/v1/users", user, User.class);
    }

    // 更新用户
    public ResponseEntity<User> updateUser(Long userId, User user) {
        return doPut("/api/v1/users/" + userId, user, User.class);
    }

    // 删除用户
    public ResponseEntity<Void> deleteUser(Long userId) {
        return doDelete("/api/v1/users/" + userId, Void.class);
    }

    // 文件上传示例
    public ResponseEntity<UploadResult> uploadAvatar(Long userId, File avatarFile) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", avatarFile);
        builder.part("userId", userId);
        return upload("/api/v1/users/" + userId + "/avatar", builder, UploadResult.class);
    }

    // 文件下载示例
    public String downloadUserAvatar(Long userId, String fileName) {
        return downloadFile("/api/v1/users/" + userId + "/avatar/" + fileName, fileName);
    }

    // 泛型类型获取辅助方法
    private <T> ParameterizedTypeReference<T> getParameterizedType(Class<?> rawType, Class<?>... actualTypeArguments) {
        return new ParameterizedTypeReference<T>() {
            @Override
            public java.lang.reflect.Type getType() {
                return java.lang.reflect.ParameterizedTypeImpl
                    .get(rawType, actualTypeArguments);
            }
        };
    }
}
```

### 2. 带认证的客户端

```java
@Component
public class SecureApiClient extends QyBaseRestClient {

    public SecureApiClient() {
        super(null, null, "https://secure-api.example.com");
    }

    @Override
    protected void configureAuth(RestClient.Builder builder) {
        // OAuth2 Bearer Token
        String token = obtainTokenFromCache();
        if (StrUtil.isNotEmpty(token)) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
    }

    @Override
    protected void customizeBuilder(RestClient.Builder builder) {
        // 启用调试模式
        if (debug) {
            setDebugMode(true);
        }

        // 设置超时
        this.connectTimeout = 30;
        this.readTimeout = 60;
    }

    private String obtainTokenFromCache() {
        // 从缓存或配置中获取token
        return "cached-token";
    }
}
```

### 3. 文件服务客户端

```java
@Component
public class FileServiceClient extends QyBaseRestClient {

    public FileServiceClient() {
        super("/data/files", "file-service-token", "https://file-api.example.com");
    }

    // 下载图片文件
    public String downloadImage(String imageId) {
        return downloadFileExt(
            "/api/images/" + imageId,
            "images/" + imageId,
            Arrays.asList("jpg", "jpeg", "png", "gif"),
            "jpg"
        ).getFullPath();
    }

    // 批量下载文档
    public List<String> downloadDocuments(List<String> documentIds) {
        return documentIds.stream()
            .map(id -> {
                try {
                    QyUploadFileDto result = downloadFileExt(
                        "/api/documents/" + id,
                        "documents/" + id,
                        Arrays.asList("pdf", "doc", "docx"),
                        "pdf"
                    );
                    return result.getFullPath();
                } catch (Exception e) {
                    log.error("下载文档失败: {}", id, e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // 上传文件
    public ResponseEntity<UploadResult> uploadFile(File file, String category) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);
        builder.part("category", category);
        return upload("/api/files", builder, UploadResult.class);
    }
}
```

### 4. 客户端配置Bean

```java
@Configuration
public class RestClientConfig {

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.auth-token}")
    private String authToken;

    @Value("${api.base-folder}")
    private String baseFolder;

    @Value("${api.connect-timeout:30}")
    private Integer connectTimeout;

    @Value("${api.read-timeout:60}")
    private Integer readTimeout;

    @Bean
    public UserServiceClient userServiceClient() {
        UserServiceClient client = new UserServiceClient();
        client.setConnectTimeout(connectTimeout);
        client.setReadTimeout(readTimeout);
        return client;
    }

    @Bean
    public FileServiceClient fileServiceClient() {
        FileServiceClient client = new FileServiceClient();
        client.setBaseFolder(baseFolder);
        client.setAuthToken(authToken);
        return client;
    }
}
```

### 5. 在Controller中使用

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return userServiceClient.getUsers(page, size);
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<UploadResult> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("avatar", file.getOriginalFilename());
        file.transferTo(tempFile);
        return userServiceClient.uploadAvatar(id, tempFile);
    }
}
```

## 最佳实践

### 1. 错误处理

```java
// 方式1：使用ResponseEntity检查状态
public User getUserSafely(Long userId) {
    try {
        ResponseEntity<User> response = doGet("/api/users/" + userId, User.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            log.error("API返回错误状态: {}", response.getStatusCode());
            throw new ApiException("用户获取失败");
        }
    } catch (Exception e) {
        log.error("调用API异常", e);
        throw new ApiException("网络异常", e);
    }
}

// 方式2：自定义异常处理
@Override
protected <T> T get(String uri, Function<UriBuilder, URI> params, Class<T> responseType) {
    try {
        return super.get(uri, params, responseType);
    } catch (HttpClientErrorException e) {
        log.error("HTTP错误: {}, URI: {}", e.getStatusCode(), uri);
        throw new ApiException("API调用失败: " + e.getMessage());
    } catch (ResourceAccessException e) {
        log.error("网络错误: {}", uri, e);
        throw new ApiException("网络连接失败");
    }
}
```

### 2. 重试机制

```java
@Component
public class ResilientApiClient extends QyBaseRestClient {

    @Override
    protected void customizeBuilder(RestClient.Builder builder) {
        builder.requestInterceptor((request, body, execution) -> {
            int maxRetries = 3;
            int retryCount = 0;
            Exception lastException = null;

            while (retryCount < maxRetries) {
                try {
                    return execution.execute(request, body);
                } catch (Exception e) {
                    lastException = e;
                    retryCount++;

                    if (retryCount >= maxRetries) {
                        break;
                    }

                    // 指数退避
                    try {
                        Thread.sleep(1000 * retryCount);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    log.warn("请求失败，第{}次重试: {}", retryCount, request.getURI());
                }
            }

            throw new ApiException("重试" + maxRetries + "次后仍然失败", lastException);
        });
    }
}
```

### 3. 超时配置

```java
// 构造函数中设置超时
public QyBaseRestClient(String baseFolder, String authToken, String baseUrl) {
    this.baseFolder = baseFolder;
    this.authToken = authToken;
    this.baseUrl = baseUrl;

    // 设置超时（可选）
    this.connectTimeout = 30;  // 连接超时30秒
    this.readTimeout = 60;     // 读取超时60秒

    this.restClient = buildRestClient();
}
```

### 4. 日志记录

```java
// 启用调试模式
public void enableDebugMode() {
    setDebugMode(true);
}

// 自定义日志拦截器
@Override
protected void configureInterceptors(RestClient.Builder builder) {
    builder.requestInterceptor((request, body, execution) -> {
        long startTime = System.currentTimeMillis();

        log.info("发起请求: {} {}", request.getMethod(), request.getURI());

        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);

            long duration = System.currentTimeMillis() - startTime;
            log.info("请求完成: {} {}, 状态: {}, 耗时: {}ms",
                request.getMethod(), request.getURI(),
                response.getStatusCode(), duration);

            return response;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("请求失败: {} {}, 耗时: {}ms, 错误: {}",
                request.getMethod(), request.getURI(),
                duration, e.getMessage(), e);
            throw e;
        }
    });
}
```

## 注意事项

### 1. 必需配置
- **baseFolder**: 使用文件下载功能前必须通过构造函数或setter设置
- **baseUrl**: 建议设置，便于统一管理API地址

### 2. 线程安全
- `QyBaseRestClient` 实例在Spring容器中默认为单例，建议每个服务客户端使用独立实例
- 如果需要每次请求使用不同的认证信息，请避免使用单例

### 3. 资源管理
- 下载大文件时注意内存使用，建议使用回调方式处理
- 临时文件使用后应及时删除

### 4. 异常处理
- 网络异常会抛出 `ResourceAccessException`
- HTTP状态错误会抛出 `HttpClientErrorException`
- 建议自定义异常处理逻辑

### 5. Spring版本要求
- 基于 Spring Framework 6.x 和 Spring Boot 3.x
- 需要 Java 17+ 版本支持

### 6. 依赖项
确保项目中包含以下依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>6.0.0</version>
</dependency>

<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
</dependency>
```

## 扩展建议

1. **继承最佳实践**: 所有自定义客户端都应该继承 `QyBaseRestClient`
2. **接口设计**: 建议定义服务接口，然后通过实现类继承
3. **单元测试**: 为每个客户端编写单元测试
4. **配置外化**: 通过配置文件或环境变量管理认证信息
5. **健康检查**: 添加客户端健康检查方法
6. **监控指标**: 集成监控框架（如Micrometer）记录API调用指标

## 相关类

- `QyUploadFileDto`: 文件上传结果传输对象
- `RestClient`: Spring Framework 6.x 的REST客户端
- `MultipartBodyBuilder`: 多部分请求体构建器
- `HttpHeaders`: HTTP头封装类

---

**版本**: 1.0.0
**作者**: QyTech Team
**更新日期**: 2025-10-30