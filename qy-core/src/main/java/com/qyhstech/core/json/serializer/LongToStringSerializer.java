package com.qyhstech.core.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 只在范围外的数据才进行Long转String输出
 * 满足的条件是JavaScript中Number的最大最小值范围
 * Number.MAX_SAFE_INTEGER 和 Number.MIN_SAFE_INTEGER 作为参考，即 2^53 - 1 和 -(2^53 - 1)。
 */
public class LongToStringSerializer extends JsonSerializer<Long> {

    private static final long LOWER_BOUND = -9007199254740991L; // -2^53 + 1
    private static final long UPPER_BOUND = 9007199254740991L;  // 2^53 - 1

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value >= LOWER_BOUND && value <= UPPER_BOUND) {
            gen.writeNumber(value);
        } else {
            gen.writeString(value.toString());
        }
    }
}
