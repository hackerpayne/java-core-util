package com.qyhstech.core.lang;

import cn.hutool.core.lang.Snowflake;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

/**
 * Snowflake单元测试
 */
public class SnowflakeTest {

    @Test
    public void snowflakeTest() {
        HashSet<Long> hashSet = new HashSet<>();

        //构建Snowflake，提供终端ID和数据中心ID
        Snowflake idWorker = new Snowflake(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();

            System.out.println("生成的ID为：" + id);
            hashSet.add(id);
        }
        //Assertions.assertEquals(1000L, hashSet.size());

    }
}
