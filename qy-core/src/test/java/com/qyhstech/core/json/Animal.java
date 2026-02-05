package com.qyhstech.core.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serial;
import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // 使用类名作为类型标识
        include = JsonTypeInfo.As.PROPERTY, // 将类型信息作为 JSON 属性
        property = "type"  // JSON 中会有一个 "type" 字段标识具体类型
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat"),
        @JsonSubTypes.Type(value = Fish.class, name = "fish")
})
public abstract class Animal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    public Animal() {
    }

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}




