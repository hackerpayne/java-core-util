package com.qyhstech.spring.advice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qyhstech.core.domain.response.QyResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 1、response.getOutputStream().write(resource.getContentAsByteArray());不会走ResponseBodyAdvice
 * 如果引入了swagger或knife4j的文档生成组件，这里需要仅扫描自己项目的包，否则文档无法正常生成
 */
//@Component
//@ControllerAdvice(annotations = {RestController.class}, basePackages = "com.qyhstech") // 通过注解进行过滤哪些请求响应会被拦截，避免错误拦截。
//@RestControllerAdvice(basePackages = "com.qyhstech")
@Slf4j
public class QyRespAdviceDemo implements ResponseBodyAdvice<Object> {

    /**
     * 此Advice是否使用于该返回类型和Converter类型(意思是可以配置多个哦)
     *
     * @param returnType    返回类型(这里可以获取很多东西, 别被名字误导了)
     * @param converterType 自动选择的转换器类型
     * @return 返回true表示将会走接下来的方法(beforeBodyWrite), 否则不会
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        /**
         * 我们可以选择哪些方法或者类进入beforeBodyWrite方法
         * 从returnType获取类名和方法名
         * 通过returnType.getMethod().getDeclaringClass.getName获取类名
         * converterType 表示当前请求使用的一个数据转换器，根据我们在controller指定返回类型决定，这里有个问题点待会会说
         */
        //        log.info(returnType.getMethod().getDeclaringClass().getName());
        //        log.info(converterType.toString());

        // 如果不需要进行封装的，可以添加一些校验手段，比如添加标记排除的注解
        return true;
    }

    /**
     * HttpMessageConverter转换之前进行的操作
     *
     * @param body                  要转换的body
     * @param returnType            返回类型
     * @param selectedContentType   根据请求头协商的ContentType
     * @param selectedConverterType 自动选择的转换器类型
     * @param request               当前请求
     * @param response              当前响应
     * @return 修改后的响应内容
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // 遇到feign接口之类的请求, 不应该再次包装,应该直接返回
        // 上述问题的解决方案: 可以在feign拦截器中,给feign请求头中添加一个标识字段, 表示是feign请求
        // 在此处拦截到feign标识字段, 则直接放行 返回body.
        log.debug("QyRespAdvice检测到指定的请求：URI:[{}]，请求方式：[{}]", request.getURI(), selectedContentType.getType());

        /**
         * body—请求即将返回给客户端的实体信息
         * body还可能存在出现异常的情况，需要进行处理
         */

        // 提供一定的灵活度，如果body已经被包装了，就不进行包装，如果不是json请求的，也不要包装，这样可以进行自定义
        if (body instanceof QyResp || !selectedContentType.equals(MediaType.APPLICATION_JSON)) {
            return body;
        }

        if (Objects.isNull(body)) {
            return QyResp.success();
        }

        // 我们也可以根据selectConvertorType的类型进行判断
        if (body instanceof String) {
            return QyResp.success(body);
        }

        if (body instanceof IPage<?>) {
            return QyResp.successPage((IPage<?>) body);
        }

        return QyResp.success(body);
    }
}
