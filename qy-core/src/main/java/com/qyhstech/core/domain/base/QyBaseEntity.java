package com.qyhstech.core.domain.base;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础通用类，在使用toString时，能通过Json打印出类结构
 */
public abstract class QyBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

//    @Override
//    public String toString() {
//        try {
//            return this.getClass().getSimpleName() + " = " + JSON.toJSONString(this, JSONWriter.Feature.WriteMapNullValue);
//        } catch (Exception e) {
//            return super.toString();
//        }
//    }

}
