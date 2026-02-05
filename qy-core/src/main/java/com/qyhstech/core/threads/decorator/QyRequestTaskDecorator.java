package com.qyhstech.core.threads.decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 解决在异步线程中使用Request读取的问题。
 */
@Slf4j
public class QyRequestTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        try {
            // 获取主线程中的请求信息（我们的用户信息也放在里面）
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            //        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
            //        RequestAttributes attributes = QyRequest.getRequestAttributes();
            return () -> {
                try {
                    // 将主线程的请求信息，设置到子线程中
                    RequestContextHolder.setRequestAttributes(attributes, true);

                    // 执行子线程，这一步不要忘了
                    runnable.run();
                } finally {

                    // 线程结束，清空这些信息，否则可能造成内存泄漏
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        } catch (IllegalStateException e) {
            // 状态不对的话，直接执行原线程
            return runnable;
        }

    }

}