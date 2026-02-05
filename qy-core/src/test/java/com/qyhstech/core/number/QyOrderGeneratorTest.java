package com.qyhstech.core.number;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class QyOrderGeneratorTest {

    @Test
    void generate() {
        IntStream.rangeClosed(1, 1000).forEach(item -> {
            System.out.println(QyOrderGenerator.generate());
        });
    }
}