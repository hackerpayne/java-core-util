package com.qyhstech.core.dates;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class QyDateTest {
    @Test
    public void testNowUnixTimestamp() throws Exception {
    }

    @Test
    public void testGetNowTime1() throws Exception {
    }

    @Test
    public void testGetNowTime2() throws Exception {
    }

    @Test
    public void testToStr() {
        String time = QyDate.toStr(LocalDateTime.now(), QyDatePattern.DATE_TIME_FORMAT, QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(time);
    }

    @Test
    public void testGetNowDate() throws Exception {
    }

    @Test
    public void testToLocalTime() throws Exception {
    }

    @Test
    public void testFormatDate() throws Exception {
    }

    @Test
    public void testParseStr() throws Exception {

        LocalDateTime date = QyDate.toLocalDateTimeFrom("2018-11-04", QyDatePattern.NORMAL_PATTERS);
        System.out.println("解析时间为：" + QyDate.toStr(date));

        date = QyDate.toLocalDateTimeFrom("2018-11-04 23:93:94", QyDatePattern.NORMAL_PATTERS);
        System.out.println("解析时间为：" + QyDate.toStr(date));

        date = QyDate.toLocalDateTimeFrom("20180511233232", QyDatePattern.NORMAL_PATTERS);
        System.out.println("解析时间为：" + QyDate.toStr(date));

        date = QyDate.toLocalDateTimeFrom("2018/03/10", QyDatePattern.NORMAL_PATTERS);
        System.out.println("解析时间为：" + QyDate.toStr(date));
    }

    @Test
    public void testToLocalTime1() throws Exception {
    }

    @Test
    public void testToUnixTime() throws Exception {
    }

    @Test
    public void testToUnixTime1() throws Exception {
    }

    @Test
    public void testFormatTime() throws Exception {
    }

    @Test
    public void testParseStringToLocalDate() throws Exception {
    }

    @Test
    public void testDateDiff() throws Exception {
    }

    @Test
    public void testDateDiffSeconds() throws Exception {
    }

    @Test
    public void testDateDiffNextHour() throws Exception {
    }

    @Test
    public void testMillisToNextHour() throws Exception {
    }

    @Test
    public void testDateDiffNextDay() throws Exception {
    }

    @Test
    public void testDateDiff2() throws Exception {
        //        QyDate.dateDiff("2017-05-18 13:13:13", "2017-05-18 13:14:13", QyDatePattern.DATE_TIME_FORMAT);
        //
        //        int diffNextHourdiffSeconds = QyDate.dateDiffSeconds("2017-05-18 13:13:13", "2017-05-18 14:14:13", QyDatePattern.DATE_TIME_FORMAT);
        //
        //        int diffNextHour = QyDate.dateDiffNextHour();
        //
        //        System.out.println(diffNextHour);
    }

    @Test
    public void testGetDateStart() throws Exception {
        System.out.println("一天开始时间");
        System.out.println(QyDate.toStr(QyDate.getStartOfDay(LocalDate.now())));
    }

    @Test
    public void testGetDateEnd() throws Exception {
        System.out.println("一天结束时间");
        System.out.println(QyDate.toStr(QyDate.getEndOfDay(LocalDate.now())));
    }

    @Test
    public void testGetYesterday() throws Exception {
        System.out.println("昨天时间");
        System.out.println(QyDate.getYesterday());

        System.out.println("指定日期的昨天：");
        System.out.println(QyDate.getYesterday("2018-02-25", "yyyy-MM-dd"));
    }

    @Test
    public void testGetNowTime() throws Exception {
    }

    @Test
    void getTime() {
        LocalDateTime dateTime = QyDate.toLocalDateTimeFromZone("");
        System.out.println(dateTime);
    }

    @Test
    void getTime2() {
        LocalDateTime dateTime = QyDate.toLocalDateTimeFromZone(QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(dateTime);
    }

    @Test
    void getTime3() {
        LocalDateTime dateTime = QyDate.toLocalDateTimeFromZone(LocalDateTime.now(), "");
        System.out.println(dateTime);
    }

    @Test
    void getTime4() {
        LocalDateTime dateTime = QyDate.toLocalDateTimeFromZone(LocalDateTime.now(), QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(dateTime);
    }

    @Test
    void testGetTime() {
    }

    @Test
    void getTimeAgo() {
        // 获取印度时间的指定日期格式显示
        LocalDateTime localDateTime = LocalDateTime.parse("2024-01-14 06:09:00", DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT));
        String description = QyDate.getTimeAgo(localDateTime, QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(description);
    }

    @Test
    void toUnixTime() {
        long unixTimeStamp = QyDate.toUnixTime();
        System.out.println(unixTimeStamp);
    }

    @Test
    void testToUnixTime2() {
        // 本时区转换为印度时区的Unix
        long unixTimeStamp = QyDate.toUnixTime(LocalDateTime.now(), QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(unixTimeStamp);
    }

    @Test
    void testToUnixTime3() {
        long unixTimeStamp = QyDate.toUnixTime(LocalDateTime.now(), QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(unixTimeStamp);
    }

    @Test
    void fromUnixTime() {
    }

    @Test
    void testFromUnixTime() {
        LocalDateTime localDateTime = QyDate.unixTimeStampTo(1705202116622L);
        System.out.println(localDateTime);
    }

    @Test
    void testFromUnixTime2() {
        LocalDateTime localDateTime = QyDate.unixTimeStampTo(1673532540L, QyZoneIdEnum.IST.getZoneIdName());
        System.out.println(localDateTime);
    }

    @Test
    void calculateAge() {
    }

    @Test
    void isValidDateFormat() {
    }

    @Test
    void isValidDateTimeFormat() {
    }

    @Test
    void datePath() {
    }

    @Test
    void toLocalDateTimeFromZone() {
    }

    @Test
    void toLocalDateTime() {
    }

    @Test
    void testToLocalDateTime() {
    }

    @Test
    void toLocalDateTimeFrom() {
    }

    @Test
    void testToLocalDateTimeFromZone() {
    }

    @Test
    void toLocalDate() {
    }

    @Test
    void testToLocalDate() {
    }

    @Test
    void toLocalDateFromZone() {
    }

    @Test
    void toDate() {
    }

    @Test
    void testToDate() {
    }

    @Test
    void toStr() {
    }

    @Test
    void testToStr1() {
        System.out.println(QyDate.toStr("yyyyMMddHHmmss"));
        System.out.println(QyDate.toStr("yyyyMMddHH"));
        System.out.println(QyDate.toStr("yyyyMMdd"));
        System.out.println(QyDate.toStr("yyyy"));
    }

    @Test
    void testToStr2() {
    }

    @Test
    void testToStr3() {
    }

    @Test
    void testToStr4() {
    }

    @Test
    void testToStr5() {
    }

    @Test
    void testToStr6() {
    }

    @Test
    void testToStr7() {
    }

    @Test
    void nowTime() {
    }

    @Test
    void testNowTime() {
    }

    @Test
    void nowDate() {
    }

    @Test
    void testNowDate() {
    }

    @Test
    void getEnDate() {
    }

    @Test
    void testGetEnDate() {
    }

    @Test
    void getYesterdayStr() {
    }

    @Test
    void getYesterday() {
    }

    @Test
    void testGetYesterday1() {
    }

    @Test
    void testGetYesterday2() {
    }

    @Test
    void getStartOfDay() {
    }

    @Test
    void testGetStartOfDay() {
    }

    @Test
    void getStartOfDayStr() {
    }

    @Test
    void testGetStartOfDayStr() {
    }

    @Test
    void getEndOfDay() {
    }

    @Test
    void testGetEndOfDay() {
    }

    @Test
    void getEndOfDayStr() {
    }

    @Test
    void testGetEndOfDayStr() {
    }

    @Test
    void getStartOfMonth() {
        System.out.println(QyDate.getStartOfMonth());
    }

    @Test
    void testGetStartOfMonth() {
        System.out.println(QyDate.getStartOfMonth(LocalDateTime.now().minusMonths(3)));
    }

    @Test
    void unixTimeStampTo() {
    }

    @Test
    void getDatesBetweenUsingJava8() {
    }

    @Test
    void getDatesBetween() {
    }

    @Test
    void testGetDatesBetween() {
    }

    @Test
    void testGetTimeAgo() {
    }

    @Test
    void getZone() {
    }

    @Test
    void getTimeZone() {
    }

    @Test
    void hasTimeOverlap() {
    }

    @Test
    void testHasTimeOverlap() {
    }

    @Test
    void testCalculateAge() {
    }

    @Test
    void testIsValidDateFormat() {
    }

    @Test
    void testIsValidDateTimeFormat() {
    }

    @Test
    void testDatePath() {
    }


    @Test
    void testToLocalDateFromZone() {
    }


    @Test
    void testGetYesterdayStr() {
    }

    @Test
    void getStartOfYear() {
        System.out.println(QyDate.getStartOfYear());
    }

    @Test
    void testGetStartOfYear() {
        System.out.println(QyDate.getStartOfYear(LocalDateTime.now().minusYears(3)));
    }


    @Test
    void testGetDatesBetweenUsingJava8() {
    }


    @Test
    void testGetZone() {
    }

    @Test
    void testGetTimeZone() {
    }


    //    @Test
    //    public void dateTest() {
    //        long current = QyDate.current(false);
    //        Console.log(current);
    //        DateTime date = QyDate.date(current);
    //        Console.log(date);
    //    }
    //
    //    @Test
    //    public void nowTest() {
    //        // 当前时间
    //        Date date = QyDate.date();
    //        Assertions.assertNotNull(date);
    //        // 当前时间
    //        Date date2 = QyDate.date(Calendar.getInstance());
    //        Assertions.assertNotNull(date2);
    //        // 当前时间
    //        Date date3 = QyDate.date(System.currentTimeMillis());
    //        Assertions.assertNotNull(date3);
    //
    //        // 当前日期字符串，格式：yyyy-MM-dd HH:mm:ss
    //        String now = QyDate.now();
    //        Assertions.assertNotNull(now);
    //        // 当前日期字符串，格式：yyyy-MM-dd
    //        String today = QyDate.today();
    //        Assertions.assertNotNull(today);
    //    }
    //
    //    @Test
    //    public void formatAndParseTest() {
    //        String dateStr = "2017-03-01";
    //        Date date = QyDate.parse(dateStr);
    //
    //        String format = QyDate.format(date, "yyyy/MM/dd");
    //        Assertions.assertEquals("2017/03/01", format);
    //
    //        // 常用格式的格式化
    //        String formatDate = QyDate.formatDate(date);
    //        Assertions.assertEquals("2017-03-01", formatDate);
    //        String formatDateTime = QyDate.formatDateTime(date);
    //        Assertions.assertEquals("2017-03-01 00:00:00", formatDateTime);
    //        String formatTime = QyDate.formatTime(date);
    //        Assertions.assertEquals("00:00:00", formatTime);
    //    }
    //
    //    @Test
    //    public void beginAndEndTest() {
    //        String dateStr = "2017-03-01 22:33:23";
    //        Date date = QyDate.parse(dateStr);
    //
    //        // 一天的开始
    //        Date beginOfDay = QyDate.beginOfDay(date);
    //        Assertions.assertEquals("2017-03-01 00:00:00", beginOfDay.toString());
    //        // 一天的结束
    //        Date endOfDay = QyDate.endOfDay(date);
    //        Assertions.assertEquals("2017-03-01 23:59:59", endOfDay.toString());
    //    }
    //
    //    @Test
    //    public void offsetDateTest() {
    //        String dateStr = "2017-03-01 22:33:23";
    //        Date date = QyDate.parse(dateStr);
    //
    //        Date newDate = QyDate.offset(date, DateField.DAY_OF_MONTH, 2);
    //        Assertions.assertEquals("2017-03-03 22:33:23", newDate.toString());
    //
    //        //常用偏移
    //        DateTime newDate2 = QyDate.offsetDay(date, 3);
    //        Assertions.assertEquals("2017-03-04 22:33:23", newDate2.toString());
    //        //常用偏移
    //        DateTime newDate3 = QyDate.offsetHour(date, -3);
    //        Assertions.assertEquals("2017-03-01 19:33:23", newDate3.toString());
    //    }


    //    @Test
    //    public void betweenTest() {
    //        String dateStr1 = "2017-03-01 22:34:23";
    //        Date date1 = QyDate.parse(dateStr1);
    //
    //        String dateStr2 = "2017-04-01 23:56:14";
    //        Date date2 = QyDate.parse(dateStr2);
    //
    //        long betweenDay = QyDate.between(date1, date2, DateUnit.DAY);
    //        Assertions.assertEquals(31, betweenDay);//相差一个月，31天
    //
    //        long between = QyDate.between(date1, date2, DateUnit.MS);
    //        String formatBetween = QyDate.formatBetween(between, BetweenFormater.Level.MINUTE);
    //        Assertions.assertEquals("31天1小时21分", formatBetween);
    //    }
    //
    //    @Test
    //    public void timerTest() {
    //        TimeInterval timer = QyDate.timer();
    //
    //        //---------------------------------
    //        //-------这是执行过程
    //        //---------------------------------
    //
    //        timer.interval();//花费毫秒数
    //        timer.intervalRestart();//返回花费时间，并重置开始时间
    //        timer.intervalMinute();//花费分钟数
    //    }
    //
    //    @Test
    //    public void currentTest() {
    //        long current = QyDate.current(false);
    //        String currentStr = String.valueOf(current);
    //        Assertions.assertEquals(13, currentStr.length());
    //
    //        long currentNano = QyDate.current(true);
    //        String currentNanoStr = String.valueOf(currentNano);
    //        Assertions.assertNotNull(currentNanoStr);
    //    }
}