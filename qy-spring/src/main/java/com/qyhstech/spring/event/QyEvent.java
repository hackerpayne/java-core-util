package com.qyhstech.spring.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * 带泛型的多重使用器
 *
 * @param <T>
 */
@Setter
@Getter
public class QyEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    private T data;

    public QyEvent(T data) {
        super(data);
        this.data = data;
    }

    public QyEvent(Object source, T data) {
        super(source);
        this.data = data;
    }

    /**
     * 自动解析类型
     * @return
     */
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(data));
    }
}
