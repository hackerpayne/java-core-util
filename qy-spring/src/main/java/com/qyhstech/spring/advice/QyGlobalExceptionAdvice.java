package com.qyhstech.spring.advice;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpStatus;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.collection.QyList;
import com.qyhstech.core.domain.enums.QyErrorCode;
import com.qyhstech.core.domain.response.QyResp;
import com.qyhstech.core.exception.QyBaseException;
import com.qyhstech.core.exception.QyBizException;
import com.qyhstech.core.exception.QyServerException;
import com.qyhstech.spring.controller.QyController;
import com.qyhstech.spring.exception.QyUseStatusCode;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 全局异常统一处理。使用Resp做为统一返回值
 */
@RestControllerAdvice
@ControllerAdvice
//@ControllerAdvice(annotations = {RestController.class, Controller.class})//拦截Controller
@ResponseBody //方便封装成json数据返回
//@Order(Ordered.HIGHEST_PRECEDENCE) // 最小最先处理
@Slf4j
public class QyGlobalExceptionAdvice {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Resource
    private MessageSource messageSource;

    /**
     * 处理自定义的异常信息
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = QyServerException.class)
    public QyResp handleServerException(QyServerException ex) {
        log.error("GlobalHandlerExceptionResolver handelServerException:", ex);
        return QyResp.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理自定义的业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = QyBizException.class)
    public QyResp handleBizException(QyBizException ex) {
        log.error("GlobalHandlerExceptionResolver handleBizException:", ex);
        return QyResp.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(QyBaseException.class)
    public QyResp handleBaseException(QyBaseException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return QyResp.fail(e.getMessage());
    }

    /**
     * ValidationException异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public QyResp handleValidationException(ValidationException ex) {
        List<String> errorMsg = new ArrayList<>();
        if (ex instanceof ConstraintViolationException exs) {
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                errorMsg.add(item.getMessage());
            }
        }
        log.error("GlobalHandlerExceptionResolver ConstraintViolationException参数校验异常", ex);
        return QyResp.fail(QyErrorCode.PARAMETER_ILLEGAL.getCode(), QyStr.join(",", errorMsg));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public QyResp constraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());
        String message = QyList.join(e.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
        return QyResp.fail(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public QyResp handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        String message = QyList.join(e.getBindingResult().getAllErrors(), DefaultMessageSourceResolvable::getDefaultMessage, ", ");
        return QyResp.fail(message);
    }

//    /**
//     * 方法和参数校验异常
//     * 解决 validator 的异常
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public QyResp handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        BindingResult result = ex.getBindingResult();
//        final List<FieldError> fieldErrors = result.getFieldErrors();
//        List<String> errorMsg = new ArrayList<>();
//
//        for (FieldError error : fieldErrors) {
//            errorMsg.add(error.getDefaultMessage());
//        }
//        log.error("GlobalHandlerExceptionResolver MethodArgumentNotValidException参数校验异常", ex);
//        return QyResp.fail(Joiner.on(",").join(errorMsg));
//    }

    /**
     * 检测数据库重复
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public QyResp handelSqlException(SQLIntegrityConstraintViolationException ex) {
        log.error("GlobalHandlerExceptionResolver SQLIntegrityConstraintViolationException:", ex);//异常信息,日志通过日志看到后面的异常中需要的异常重复账号的字段
        //如果这sql异常信息有Duplicate entry字段,就进一步说明是账号重复的异常,Duplicate entry 'Test' for key 'idx_unique'
        if (ex.getMessage().contains("Duplicate entry")) {
            String msg = ReUtil.get("Duplicate entry '(.*?)' for key", ex.getMessage(), 1) + "已存在";
            return QyResp.fail(QyErrorCode.DATA_EXIST.getCode(), msg);//进行返回
        }
        return QyResp.fail("未知错误");
    }

//    /**
//     * 参数验证异常处理
//     *
//     * @param result
//     * @return
//     */
//    @ResponseBody
//    @ExceptionHandler(BindException.class)
//    public QyResp handleBindException(BindingResult result) {
//        log.error("GlobalHandlerExceptionResolver BindingResult 参数验证异常处理...");
//        if (result.hasErrors()) {
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            //            HashMap<String, String> map = new HashMap<>();
//            //            fieldErrors.forEach(x -> {
//            //                String field = x.getField();
//            //                String msg = x.getDefaultMessage();
//            //                map.put(field, msg);
//            //            });
//
//            List<String> errorMsg = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
//
//            return QyResp.fail(QyErrorCode.PARAMETER_ERROR.getCode(), Joiner.on(",").join(errorMsg));
//        }
//
//        return QyResp.fail(QyErrorCode.PARAMETER_ERROR);
//    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public QyResp handleBindException(BindException e) {
        log.error(e.getMessage());
        String message = QyList.join(e.getAllErrors(), DefaultMessageSourceResolvable::getDefaultMessage, ", ");
        return QyResp.fail(message);
    }

    /**
     * 找不到路由
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public QyResp handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}'不存在.", requestURI);
        return QyResp.fail(HttpStatus.HTTP_NOT_FOUND, e.getMessage());
    }

    /**
     * 没有资源被发现
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public QyResp handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error("GlobalHandlerExceptionResolver NoResourceFoundException,请求页面：{}", ex.getResourcePath(), ex);
        return QyResp.fail(QyErrorCode.PAGE_NOT_FOUND);
    }

    /**
     * 重复键值异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public QyResp handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error("GlobalHandlerExceptionResolver DuplicateKeyException", ex);
        if (ex.getMessage().contains("Duplicate entry")) {
            String msg = ReUtil.get("Duplicate entry '(.*?)' for key", ex.getMessage(), 1) + "已存在";
            return QyResp.fail(QyErrorCode.DATA_EXIST.getCode(), msg);//进行返回
        }
        return QyResp.fail();
    }

    /**
     * 空指针异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public QyResp handleNullException(NullPointerException ex) {
        log.error("GlobalHandlerExceptionResolver NullPointerException空指针异常", ex);
        return QyResp.fail(QyErrorCode.NULL);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public QyResp handleNumberFormatException(NumberFormatException ex) {
        log.error("GlobalHandlerExceptionResolver NumberFormatException", ex);
        return QyResp.fail(QyErrorCode.PARAMS_NOT_CONVERT);
    }

    /**
     * 处理RuntimeException
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public QyResp handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("GlobalHandlerExceptionResolver RuntimeException", ex);
        
        // 检查是否为 text/event-stream 类型请求
        String contentType = request.getHeader("Content-Type");
        if (contentType != null && contentType.contains("text/event-stream")) {
            // 对于 SSE 请求，返回简化错误信息
            return QyResp.fail(QyErrorCode.FAIL.getCode(), "Server Error");
        }
        
        return QyResp.fail(QyErrorCode.FAIL, ex.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public QyResp handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return QyResp.fail(HttpStatus.HTTP_BAD_METHOD, e.getMessage());
    }

    /**
     * servlet异常
     */
    @ExceptionHandler(ServletException.class)
    public QyResp handleServletException(ServletException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return QyResp.fail(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public QyResp handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI);
        return QyResp.fail(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public QyResp handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI);
        return QyResp.fail(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), e.getValue()));
    }

    /**
     * 默认全局异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public QyResp handleException(Exception ex) {
        log.error("GlobalHandlerExceptionResolver Exception:", ex);

        //Controller方法和类上的注解
        Integer validateStatusCode = this.findValidationStatusCodeInController();
        if (validateStatusCode != null) {
            return QyResp.fail(validateStatusCode, QyErrorCode.FAIL.getDesc());
        }

        return QyResp.fail(QyErrorCode.FAIL);
    }

    /**
     * 找Controller中的ValidationStatusCode注解
     * 当前方法->当前Controller类
     *
     * @return
     * @throws Exception
     */
    private Integer findValidationStatusCodeInController() {

        QyUseStatusCode validateStatusCode = null;
        try {
            Method method = QyController.getCurrentControllerMethod(requestMappingHandlerMapping);
            //Controller方法上的注解
            validateStatusCode = method.getAnnotation(QyUseStatusCode.class);
            //Controller类上的注解
            if (validateStatusCode == null) {
                validateStatusCode = method.getDeclaringClass().getAnnotation(QyUseStatusCode.class);
            }
        } catch (Exception ex) {
            log.error("", ex);
        }

        if (Objects.nonNull(validateStatusCode)) {
            return validateStatusCode.code();
        }

        return null;
    }

    //    /**
    //     * 默认全局异常处理
    //     *
    //     * @param request
    //     * @param response
    //     * @param handler
    //     * @param ex
    //     * @return
    //     */
    //    @ExceptionHandler(Exception.class)
    //    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    //        String uri = request.getRequestURI();
    //        try {
    //            log.error("未知异常", ex);
    //            QyRequestUtil.renderJson(response, QyResp.fail(ex.getMessage()));
    //        } catch (Exception e) {
    //            log.error(StrUtil.format("未知异常,URI = {}", uri), ex);
    //            return null;
    //        }
    //        return null;
    //    }

}