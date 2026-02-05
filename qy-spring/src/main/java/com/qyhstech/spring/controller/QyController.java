package com.qyhstech.spring.controller;

import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.request.QyRequestMethodItem;
import com.qyhstech.core.domain.request.QyRequestMethodParameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 控制器相关操作类，比如扫描所有路由等
 */
@Slf4j
public class QyController {

    //private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    private static StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();

    /**
     * 获取URL
     *
     * @param handlerMethod
     * @return
     */
    public static String getUrl(HandlerMethod handlerMethod) {
        String mappingUrl = "";
        String classUrl = "";
        String methodUrl = "";
        RequestMapping classMapping = handlerMethod.getBean().getClass().getAnnotation(RequestMapping.class);
        RequestMapping methodMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);

        if (classMapping != null) {
            classUrl = classMapping.value()[0] == null ? "" : classMapping.value()[0];
        }
        if (methodMapping != null) {
            if (methodMapping.value().length != 0) {
                methodUrl = methodMapping.value()[0] == null ? "" : methodMapping.value()[0];
            }
        }
        mappingUrl = classUrl + methodUrl;
        return mappingUrl;
    }

    /**
     * 获取所有URL列表
     *
     * @return
     */
    public static List<String> getAllUrl(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        List<String> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()) { //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            urlList.addAll(patterns);
        }
        return urlList;
    }

    /**
     * @param requestMappingHandlerMapping
     * @return
     */
    public static List<Map<String, String>> getAllUrlListMap(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        // 打印出更详细的内容
        List<Map<String, String>> list = QyList.empty();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            Map<String, String> map1 = new HashMap<String, String>();
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                map1.put("url", url);
            }
            map1.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
            map1.put("method", method.getMethod().getName()); // 方法名
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map1.put("type", requestMethod.toString());
            }
            list.add(map1);
        }
        return list;
    }

    /**
     * 从类里面解析所有URL
     *
     * @param mapping
     * @return
     */
    public static List<QyRequestMethodItem> getAllUrls(RequestMappingHandlerMapping mapping) {
        List<QyRequestMethodItem> listRequestItems = QyList.empty();

        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = m.getKey(); // 获取URL列表
            HandlerMethod handlerMethod = m.getValue(); // Controller的处理方法

            // 请求路径
            String path = requestMappingInfo.getPatternsCondition() != null ? requestMappingInfo.getPatternsCondition().toString() : "";
            path = path.replace("[", "").replace("]", "");

            // 请求方法
            String requestMethod = requestMappingInfo.getMethodsCondition().toString();
            requestMethod = requestMethod.replace("[", "").replace("]", "");

            // 返回header类型
            String responseFormat = requestMappingInfo.getProducesCondition().toString();
            responseFormat = responseFormat.replace("[", "").replace("]", "");

            // 参数
            boolean hasFileUploadParameter = false;
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            List<QyRequestMethodParameter> parameters = new ArrayList<>(methodParameters.length);
            for (MethodParameter methodParameter : methodParameters) {
                // 参数名称
                // 如果没有discover参数会是null.参考 LocalVariableTableParameterNameDiscoverer
                methodParameter.initParameterNameDiscovery(discoverer);
                String parameterName = methodParameter.getParameterName();

                // 参数类型
                Class<?> parameterType = methodParameter.getParameterType();
                if (parameterType.toString().toLowerCase().contains("MultipartFile".toLowerCase())) {
                    hasFileUploadParameter = true;
                }

                // 参数注解
                Object[] parameterAnnotations = methodParameter.getParameterAnnotations();

                // 注解
                String annoation = Arrays.toString(parameterAnnotations);

                QyRequestMethodParameter parameter = new QyRequestMethodParameter();
                parameter.setAnnoation(annoation);
                parameter.setName(parameterName);
                parameter.setType(parameterType.toString());
                parameters.add(parameter);
            }

            // 可以获取自定 注解里面的内容做处理
//            String descrption = "";
//            Operation documentAnnotation = handlerMethod.getMethodAnnotation(Operation.class);
//            if (documentAnnotation != null) {
//                descrption = documentAnnotation.summary(); // 从Value中获取方法注释
//            }

            // 获取ElCheck权限标记的URL
            //            String preAuthorizeValue = "";
            //            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            //            if (preAuthorize != null) {
            //                preAuthorizeValue = preAuthorize.value(); // 从Value中获取方法注释
            //                preAuthorizeValue = preAuthorizeValue.replace("@el.check()", "").replace("@el.check('", "").replace("')", "");
            //            }

            // 通过ResponseBody检查返回值类型
            ResponseBody responseBody = handlerMethod.getMethodAnnotation(ResponseBody.class); // 检查是否有ResponseBody注解
            if (responseBody != null) {
                responseFormat = "json";
            }
            if (responseFormat.contains("text/html")) {
                responseFormat = "string";
            }

            // 字符串为：com.qyhstech.qydouyin.controller.admin.notice.SysNotifyMessageController#uploadPaymentVoucher(String, MultipartFile)
            //            String handleMethodStr = handlerMethod.toString();

            // 解析Controller路径，解析结果为：com.qyhstech.codegen.controller.TestController
            //            String controllerStr = handlerMethod.getBeanType().getName();

            // 解析完整的方法：解析结果为：com.qyhstech.codegen.controller.TestController.test2
            //            String methodStr = handlerMethod.getMethod().getClass().getName();

            // 返回值类型，比如 java.lang.String
            String responseType = handlerMethod.getMethod().getReturnType().getName();

            QyRequestMethodItem item = new QyRequestMethodItem();

            // 请求信息
            item.setRequestUrl(path); // 请求URL
            item.setRequestMethod(requestMethod); // 请求方式
            item.setRequestHasFile(hasFileUploadParameter); // 是否有上传文件
            item.setRequestParameters(parameters); // 请求参数列表

            // 基本信息
            item.setMethod(handlerMethod.getMethod().getName());// 请求方法
            item.setClassName(handlerMethod.getMethod().getDeclaringClass().getName());//类名
            //            item.setController(controllerStr);
            //            item.setMethodFull(handlerMethod.toString());
//            item.setDesc(descrption);
            //            item.setPreAuthorize(preAuthorizeValue);// 获取PreAuthorize的注解信息

            // 返回值信息
            item.setResponseType(responseType);
            item.setResponseFormat(responseFormat);

            listRequestItems.add(item);
        }

        Collections.sort(listRequestItems);

        return listRequestItems;
    }

    /**
     * 获取Controller所有的URL列表，方法一
     *
     * @param request
     * @return
     */
    public static List<String> getUrlMapping(HttpServletRequest request) {
        WebApplicationContext wc = RequestContextUtils.findWebApplicationContext(request);
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        List<String> urls = new ArrayList<>(map.size());
        for (RequestMappingInfo info : map.keySet()) {
            //System.out.println(info.getPatternsCondition().toString() + "," + map.get(info).getBean().toString());
            urls.add(info.getPatternsCondition().toString());
        }
        return urls;
    }

    /**
     * 当前Controller方法
     *
     * @param requestMappingHandlerMapping
     * @return
     * @throws Exception
     */
    public static Method getCurrentControllerMethod(RequestMappingHandlerMapping requestMappingHandlerMapping) throws Exception {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HandlerExecutionChain handlerChain = requestMappingHandlerMapping.getHandler(sra.getRequest());
        if (Objects.nonNull(handlerChain)) {
            HandlerMethod handler = (HandlerMethod) handlerChain.getHandler();
            return handler.getMethod();
        }
        return null;
    }

}
