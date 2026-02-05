package com.qyhstech.core.dates;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.qyhstech.core.QyStr;
import com.qyhstech.core.collection.QyList;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 如果是 Java 8 ，建议使用 DateTimeFormatter 代替 SimpleDateFormat。
 */
public class QyDate extends LocalDateTimeUtil {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

//    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    private static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 计算周岁年龄
     *
     * @param birth   出生日期
     * @param current 计算年龄的基准日期
     * @return 周岁年龄，如果输入为null则返回null
     */
    public static Integer calculateAge(LocalDate birth, LocalDate current) {
        if (Objects.isNull(birth) || Objects.isNull(current)) {
            return null;
        }

        // 检查日期有效性
        if (birth.isAfter(current)) {
            throw new RuntimeException("出生日期不能晚于当前日期");
        }

        // 如果出生年份和当前年份相同，直接返回0
        if (birth.getYear() == current.getYear()) {
            return 0;
        }

        // 计算年份差
//        int years = Period.between(birth, current).getYears();
        int years = current.getYear() - birth.getYear();

        // 检查是否已经过了今年的生日
        LocalDate birthdayThisYear = birth.withYear(current.getYear());

        // 如果今年的生日还没到，年龄减1，但不能小于0
        if (birthdayThisYear.isAfter(current) || birthdayThisYear.isEqual(current)) {
            years = Math.max(0, years - 1);
        }

        return years;
    }

    /**
     * 判断是否是指定格式的数据
     *
     * @param dateString
     * @param format
     * @return
     */
    public static boolean isValidDateFormat(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是指定格式的数据
     *
     * @param dateString
     * @param format
     * @return
     */
    public static boolean isValidDateTimeFormat(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDateTime date = LocalDateTime.parse(dateString, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前日期的路径格式字符串，格式为"yyyy/MM/dd"
     *
     * @return 当前日期的路径格式字符串
     */
    public static String datePath() {
        return toStr(QyDateFormatsType.YYYY_MM_DD_SLASH.getTimeFormat());
    }

    /**
     * 获取指定时区的时间
     *
     * @param timezoneStr
     * @return
     */
    public static LocalDateTime toLocalDateTimeFromZone(String timezoneStr) {
        ZonedDateTime now = ZonedDateTime.now(getTimeZone(timezoneStr));
        return now.toLocalDateTime();
    }

    /**
     * 字符串转LocalDateTime，默认格式为"yyyy-MM-dd HH:mm:ss"
     *
     * @param time
     * @return
     */
    public static LocalDateTime toLocalDateTime(String time) {
        return toLocalDateTime(time, QyDatePattern.DATE_TIME_FORMAT);
    }

    /**
     * 字符串转LocalDateTime，需要指定时间格式
     *
     * @param time   要处理的时间
     * @param format 时间的应该格式
     * @return
     */
    public static LocalDateTime toLocalDateTime(String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 根据指定的格式来解析时间，转换为时间格式
     *
     * @param time       时间字符串
     * @param formatList 可能的时间格式
     * @return
     */
    public static LocalDateTime toLocalDateTimeFrom(String time, String... formatList) {
        for (String format : formatList) {
            try {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
                return LocalDateTime.parse(time, df);
            } catch (Exception exception) {
            }

        }
        return null;
    }

    /**
     * 获取指定时区的时间，先获取系统时间，再转换为指定目标的时间
     *
     * @param localDateTime 指定时间
     * @param timezoneStr   指定时区
     * @return java.time.LocalDateTime
     **/
    public static LocalDateTime toLocalDateTimeFromZone(LocalDateTime localDateTime, String timezoneStr) {
        // 时区转换
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(getTimeZone(timezoneStr)).toLocalDateTime();
    }

    /**
     * 字符串转日期格式
     *
     * @param time 时间字符串
     * @return
     */
    public static LocalDate toLocalDate(String time) {
        return toLocalDate(time, QyDatePattern.DATE_FORMAT);
    }

    /**
     * 字符串转换为LocalDate格式
     *
     * @param time   时间字符串
     * @param format 指定格式
     * @return
     */
    public static LocalDate toLocalDate(String time, String format) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 指定日期转换为指定时区的日期格式
     *
     * @param localDate   指定时间
     * @param timezoneStr 指定时区
     * @return java.time.LocalDateTime
     **/
    public static LocalDate toLocalDateFromZone(LocalDate localDate, String timezoneStr) {

        // 将LocalDate转换为ZonedDateTime
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(getTimeZone(timezoneStr));

        // 将ZonedDateTime转换为LocalDate
        return zonedDateTime.toLocalDate();
    }

    /**
     * LocalDate转Date
     * 1、使用ZonedDateTime将LocalDate转换为Instant。
     * 2、使用from（）方法从Instant对象获取Date的实例
     *
     * @param localDate
     * @return
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转Date格式
     *
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间的，日期yyyy-MM-dd格式
     *
     * @return
     */
    public static String toStr() {
        return toStr(QyDatePattern.DATE_FORMAT);
    }

    /**
     * 使用指定格式，生成今天的日期格式
     *
     * @param format
     * @return
     */
    public static String toStr(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }

    /**
     * 获取LocalDate的普通格式时间，普通格式为：yyyy-MM-dd
     *
     * @param localDate
     * @return
     */
    public static String toStr(LocalDate localDate) {
        return toStr(localDate, QyDatePattern.DATE_FORMAT);
    }

    /**
     * 本地日期转指定格式
     *
     * @param localDate
     * @param pattern
     * @return
     */
    public static String toStr(LocalDate localDate, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    /**
     * 转换为指定时区的日期再显示
     *
     * @param localDate
     * @param pattern
     * @param timezoneStr
     * @return
     */
    public static String toStr(LocalDate localDate, String pattern, String timezoneStr) {
        return DateTimeFormatter.ofPattern(pattern).format(toLocalDateFromZone(localDate, timezoneStr));
    }

    /**
     * 本地时间转普通格式
     *
     * @param localDateTime
     * @return
     */
    public static String toStr(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT).format(localDateTime);
    }

    /**
     * LocalDateTime转指定格式字符串
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String toStr(LocalDateTime localDateTime, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    /**
     * 转指定时区时间为指定格式
     *
     * @param localDateTime
     * @param pattern
     * @param timezoneStr
     * @return
     */
    public static String toStr(LocalDateTime localDateTime, String pattern, String timezoneStr) {
        return DateTimeFormatter.ofPattern(pattern).format(toLocalDateTimeFromZone(localDateTime, timezoneStr));
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String nowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT));
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param timeFormat
     * @return
     */
    public static String nowTime(String timeFormat) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
    }

    /**
     * 获取紧密格式的日期
     *
     * @return
     */
    public static String nowDate() {
        return DateTimeFormatter.ofPattern(QyDatePattern.DATE_SIMPLE_FORMAT).format(LocalDate.now());
    }

    /**
     * 获取紧密格式的日期
     *
     * @param localDate
     * @return
     */
    public static String nowDate(LocalDate localDate) {
        return toStr(localDate, QyDatePattern.DATE_SIMPLE_FORMAT);
    }

    /**
     * 获取英文时间格式 15-Mar-2020
     *
     * @param localDateTime
     * @return
     */
    public static String getEnDate(LocalDateTime localDateTime) {
        return getEnDate(localDateTime, "dd-MMM-uuuu");
    }

    /**
     * 获取英文区的时间格式
     *
     * @param localDateTime
     * @param format
     * @return
     */
    public static String getEnDate(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format, Locale.ENGLISH));
    }

    /**
     * 获取昨天的字符串格式
     *
     * @return
     */
    public static String getYesterdayStr() {
        return LocalDate.now().plusDays(-1).format(DateTimeFormatter.ofPattern(QyDatePattern.DATE_FORMAT));
    }

    /**
     * 获取昨天的时间
     *
     * @return
     */
    public static LocalDate getYesterday() {
        return LocalDate.now().plusDays(-1);
    }

    /**
     * 获取指定日期的昨天
     *
     * @param localDate
     * @return
     */
    public static LocalDate getYesterday(LocalDate localDate) {
        return localDate.plusDays(-1);
    }

    /**
     * 获取指定日期的昨天
     *
     * @param localDate 日期
     * @param pattern   日期格式
     * @return
     */
    public static LocalDate getYesterday(String localDate, String pattern) {
        return toLocalDate(localDate, pattern).plusDays(-1);
    }

    /**
     * 获取一天开始时间
     *
     * @return
     */
    public static LocalDateTime getStartOfDay() {
        return getStartOfDay(LocalDateTime.now());
    }

    /**
     * 获取一天开始时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getStartOfDay(LocalDateTime localDateTime) {
        return localDateTime.with(LocalTime.MIN);
    }

    /**
     * 获取一天开始时间
     *
     * @param localDate
     * @return
     */
    public static LocalDateTime getStartOfDay(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    /**
     * 获取一天开始时间
     *
     * @param localDateTime
     * @return
     */
    public static String getStartOfDayStr(LocalDateTime localDateTime) {
        return getStartOfDay(localDateTime).format(DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT));
    }

    /**
     * 获取一天开始时间
     *
     * @param localDate
     * @return
     */
    public static String getStartOfDayStr(LocalDate localDate) {
        return getStartOfDay(localDate).format(DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT));
    }

    /**
     * 获取一天结束时间
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getEndOfDay(LocalDateTime localDateTime) {
        return localDateTime.with(LocalTime.MAX);
    }

    /**
     * 获取一天结束时间
     *
     * @param localDate
     * @return
     */
    public static LocalDateTime getEndOfDay(LocalDate localDate) {
        return localDate.atTime(LocalTime.MAX);
    }

    /**
     * 获取一天结束时间
     *
     * @param localDateTime
     * @return
     */
    public static String getEndOfDayStr(LocalDateTime localDateTime) {
        return getEndOfDay(localDateTime).format(DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT));
    }

    /**
     * 获取一天结束时间
     *
     * @param localDate
     * @return
     */
    public static String getEndOfDayStr(LocalDate localDate) {
        return getEndOfDay(localDate).format(DateTimeFormatter.ofPattern(QyDatePattern.DATE_TIME_FORMAT));
    }

    /**
     * 本月第一天0点
     *
     * @return
     */
    public static LocalDateTime getStartOfMonth() {
        return getStartOfMonth(LocalDateTime.now());
    }

    /**
     * 获取本月第一天0点
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getStartOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfMonth()) // 调整到本月第一天
                .with(LocalTime.MIDNIGHT); // 设置时间为0点
    }

    /**
     * 获取本年第一天0点
     *
     * @return
     */
    public static LocalDateTime getStartOfYear() {
        return getStartOfYear(LocalDateTime.now());
    }

    /**
     * 获取本年第一天0点
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime getStartOfYear(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfYear()) // 调整到本月第一天
                .with(LocalTime.MIDNIGHT); // 设置时间为0点
    }


    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime unixTimeStampTo(long timestamp) {
        return unixTimeStampTo(timestamp, "");
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp   时间戳
     * @param timezoneStr 时区格式，默认为系统时区
     * @return
     */
    public static LocalDateTime unixTimeStampTo(long timestamp, String timezoneStr) {

        if (String.valueOf(timestamp).length() == 10) { // 适配10位数长度的时间戳
            timestamp = timestamp * 1000;
        }

        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, getTimeZone(timezoneStr));
    }

    /**
     * 当前Unix时间戳
     *
     * @return
     */
    public static long toUnixTime() {
        return toUnixTime(LocalDateTime.now());
    }

    /**
     * LocalDateTime转时间戳
     *
     * @param localDateTime
     * @return
     */
    public static long toUnixTime(LocalDateTime localDateTime) {
        return toUnixTime(localDateTime, null);
    }

    /**
     * 转换为10位数的Unix时间戳
     *
     * @return
     */
    public static long toUnixTimeShort() {
        return toUnixTime(LocalDateTime.now()) / 1000;
    }

    /**
     * 转换指定时间为10位数字的Unix时间戳
     *
     * @param localDateTime
     * @return
     */
    public static long toUnixTimeShort(LocalDateTime localDateTime) {
        return toUnixTime(localDateTime) / 1000;
    }


    /**
     * 计算指定时区的时间对应的Unix时间戳
     *
     * @param localDateTime
     * @param timeZoneStr
     * @return
     */
    public static long toUnixTime(LocalDateTime localDateTime, String timeZoneStr) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(getTimeZone(timeZoneStr));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 转换成新的时间格式
     *
     * @param time
     * @param format
     * @param newFormat
     * @return
     */
    public static String toStr(String time, String format, String newFormat) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime newTime = LocalDateTime.parse(time, df);
        return DateTimeFormatter.ofPattern(newFormat).format(newTime);
    }

    /**
     * Java8获取2个日期的日期中间列表
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<LocalDate> getDatesBetweenUsingJava8(LocalDate startDate, LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween + 1).mapToObj(i -> startDate.plusDays(i)).collect(Collectors.toList());
    }

    /**
     * 获取2个日期之间的值
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getDatesBetween(LocalDate beginDate, LocalDate endDate) {
        return getDatesBetween(beginDate, endDate, "");
    }

    /**
     * 获取2个LocalDate之间的每一天
     *
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @param dateFormat 指定显示的格式
     * @return
     */
    public static List<String> getDatesBetween(LocalDate beginDate, LocalDate endDate, String dateFormat) {

        List<String> listDates = QyList.empty();
        Long distance = ChronoUnit.DAYS.between(beginDate, endDate);
        if (distance < 1) {
            return listDates;
        }

        String format = QyStr.isNotEmpty(dateFormat) ? dateFormat : QyDatePattern.DATE_FORMAT;

        Stream.iterate(beginDate, dates -> {
            return dates.plusDays(1);
        }).limit(distance + 1).forEach(dates -> {
            listDates.add(DateTimeFormatter.ofPattern(format).format(dates));
        });
        return listDates;
    }

    /**
     * 生成多少分钟之前的格式
     *
     * @param time
     * @param timezoneStr
     * @return
     */
    public static String getTimeAgo(LocalDateTime time, String timezoneStr) {
        long compareTime = toUnixTime(time, timezoneStr);

        LocalDateTime now = toLocalDateTimeFromZone(timezoneStr);
        long rightTime = toUnixTime(now, timezoneStr);

        long delta = rightTime - compareTime;
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    /**
     * 获取默认时区
     *
     * @return java.time.ZoneOffset
     **/
    public static ZoneOffset getZone() {
        return OffsetDateTime.now().getOffset();
    }

    /**
     * 获取指定的时区
     *
     * @param timezoneStr
     * @return
     */
    public static ZoneId getTimeZone(String timezoneStr) {
        return StrUtil.isNotBlank(timezoneStr) ? ZoneId.of(timezoneStr) : ZoneId.systemDefault();
    }

    /**
     * 判断一个时间组，是否在另一个时间组范围内
     *
     * @param start1 第一个时间的开始
     * @param end1   第一个时间的结束
     * @param start2 第二个时间的开始
     * @param end2   第二个时间的结尾
     * @return
     */
    public static boolean hasTimeOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        // 如果第一个时间区间的结束时间早于第二个时间区间的开始时间，则无交集
        if (!end1.isAfter(start2)) {
            return false;
        }
        // 如果第二个时间区间的结束时间早于第一个时间区间的开始时间，则无交集
        return end2.isAfter(start1);
        // 否则，两个时间区间有交集
    }

    /**
     * 判断一个时间组，是否在另一个时间组范围内。
     *
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    public static boolean hasTimeOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        // 如果第一个时间区间的结束时间早于第二个时间区间的开始时间，则无交集
        if (!end1.isAfter(start2)) {
            return false;
        }
        // 如果第二个时间区间的结束时间早于第一个时间区间的开始时间，则无交集
        return end2.isAfter(start1);
        // 否则，两个时间区间有交集
    }


}
