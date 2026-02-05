package com.qyhstech.core.threads;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class QyThreadVTest {

    @Test
    void executeVirtual() {
    }

    @Test
    void testExecuteVirtual() {
    }

    @Test
    void testExecuteVirtual1() {
        List<String> lists = new ArrayList<>();
        IntStream.rangeClosed(1, 10000).forEach(i -> {
            lists.add(String.valueOf(i));
        });
        QyThreadV.executeVirtual(lists, item -> {
            log.info(item);
        }, 20);
    }

}