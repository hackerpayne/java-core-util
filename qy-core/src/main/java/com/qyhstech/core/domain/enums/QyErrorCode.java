package com.qyhstech.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求返回状态枚举类
 * HTTP状态码：三位数，对应org.springframework.http.HttpStatus中定义的状态码；
 * 公共状态码：四位数，对应编码1XXX开头，枚举COMM_*，对应公共异常如参数错误；
 * 业务状态码：五位数，各业务模块自定义，如用户中心10XXX,枚举USER_XXX,订单中心20XXX,枚举ORDER_XXX等等。
 */
@Getter
@AllArgsConstructor
public enum QyErrorCode implements IStatusCode {

    // 通用
    SUCCESS(0, "操作成功"),
    FAIL(1, "操作失败"),

    // Biz业务端异常
    SERVICE_ERROR(100, "服务异常"),
    CONFIG_ERROR(101, "配置文件错误"),

    // Http状态码 4X类
    BAD_REQUEST(400, "404未找到该资源"),
    FORBIDDEN(403, "没有访问权限"),
    PAGE_NOT_FOUND(404, "页面不存在"),
    METHOD_NOT_SUPPORTED(405, "不允许此方法"),

    // HTTP状态码 5X类
    SERVER_ERROR(500, "服务器内部错误"),
    SERVER_BUSY(503, "服务器正忙，请稍后再试!"),

    // 授权类
    NEED_LOGIN(3000, "请先登录"),
    NO_AUTHORIZE(3001, "无权访问页面"),
    SIGNATURE_NOT_MATCH(3002, "请求的签名不匹配"),
    VERIFY_CODE_ERROR(3003, "验证码错误"),
    VERIFY_CODE_NOT_FOUND(3004, "验证码不存在"),
    BAD_PASSWORD(3005, "账号密码错误"),
    SESSION_EXPIRE(3006, "登陆过期，请重新登录"),
    INVALID_TOKEN(3007, "无效的Token请求"),
    LOGOUT_SUCCESS(3008, "用户已成功退出"),

    // 账号信息
    ACCOUNT_NOT_FOUND(3009, "账号不存在"),
    ACCOUNT_NOT_ACTIVE(3010, "账号未激活"),
    ACCOUNT_DISABLE(3011, "账号被禁用"),

    // 参数类
    PARAMETER_ERROR(4000, "参数异常"),
    PARAMETER_ILLEGAL(4001, "参数不合法"),
    NULL(4002, "参数不能为空"),
    PARAMS_NOT_CONVERT(4003, "格式转换失败"),
    REPEAT_REQUEST(4004, "重复提交数据"),
    RATE_LIMIT(4005, "请求过于频繁，请稍候再试"),

    // 数据库类
    DATA_EXIST(5000, "数据已经存在"),
    ENTITY_NOT_FOUND(4001, "实体不存在"),

    // 业务暂停类
    TMP_STOP(6000, "当前请求人数过多，请稍后重试。");

    /**
     * 提示码
     */
    private final Integer code;

    /**
     * 提示消息
     */
    private final String desc;

}
