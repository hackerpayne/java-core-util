package com.qyhstech.spring.threads;

import com.qyhstech.spring.QyRequest;
import com.qyhstech.spring.holder.QyRequestHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;

/**
 * 线程池里面共享Request对象的装饰器
 * 使用方法：添加到线程池中
 * executor.setTaskDecorator(new QyCustomTaskDecorator());
 */
@Slf4j
public class QyCustomTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        //        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //        HttpServletRequest request = requestAttributes.getRequest();

        HttpServletRequest request = QyRequest.getRequest();
        log.debug("CustomTaskDecorator执行异步任务共享request");
        return () -> {
            try {
                QyRequestHolder.shareRequest(request);
                runnable.run();
            } finally {
                QyRequestHolder.clear();
            }
        };
    }
}