package com.qyhstech.core.dates;

import org.junit.jupiter.api.Test;

import java.util.Date;

class QyDateOldTest {

    @Test
    void parseDate() {
    }

    @Test
    void formatDate() {
    }

    @Test
    void format() {
        String dateStr = "2023-10-22 22:04:22";

        Date oldDate = QyDateOld.parseDate(dateStr, QyDatePattern.DATE_TIME_FORMAT, "Asia/Kolkata");
        System.out.println(oldDate);// 输出：Mon Oct 22 22:04:22 IST 2

        String newTime = QyDateOld.format(oldDate, QyDatePattern.DATE_TIME_FORMAT, "Asia/Kolkata");
        System.out.println(newTime);
    }

    @Test
    void dateToLocalDateFromZone() {
    }

    @Test
    void dateToLocalDateFromZoneTimeFromZone() {
    }

    @Test
    void getStartOfDay() {
    }
}