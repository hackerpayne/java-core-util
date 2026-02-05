package com.qyhstech.spring.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.qyhstech.spring.QyRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 全局共享Request对象
 */
public class QyRequestHolder {

    public static TransmittableThreadLocal<HttpServletRequest> requestTransmittableThreadLocal = new TransmittableThreadLocal<HttpServletRequest>();

    /**
     * 共享Request对象
     *
     * @param request
     */
    public static void shareRequest(HttpServletRequest request) {
        requestTransmittableThreadLocal.set(request);
    }

    /**
     * 获取对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = requestTransmittableThreadLocal.get();
        if (request != null) {
            return requestTransmittableThreadLocal.get();
        } else {
            //            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes requestAttributes = QyRequest.getRequestAttributes();
            if (requestAttributes != null) {
                return requestAttributes.getRequest();
            } else {
                return null;
            }
        }
    }

    /**
     * 删除对象
     */
    public static void clear() {
        requestTransmittableThreadLocal.remove();
    }

}
