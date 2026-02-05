package com.qyhstech.spring.token;

import com.qyhstech.core.token.QyToken;
import com.qyhstech.spring.QyRequest;
import jakarta.servlet.http.HttpServletRequest;

public class QyTokenSpring extends QyToken {
    /**
     * 从当前上下文中提取Token
     *
     * @return
     */
    public static String resolveToken() {
        HttpServletRequest request = QyRequest.getRequest();
        return resolveToken(request);
    }

}
