package com.qyhstech.core.dates;

import org.junit.jupiter.api.Test;

import java.util.Date;

class QyDateTimeTest {

    @Test
    void format() {
        String dates = QyDateOld.format(new Date(), QyDatePattern.DATE_FORMAT);
        String dates2 = QyDateOld.format(new Date(), QyDatePattern.DATE_TIME_FORMAT);
        System.out.println(dates);
        System.out.println(dates2);
    }
}