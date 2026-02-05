package com.qyhstech.core.dates;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 旧版本的Date处理类
 */
public class QyDateOld extends DateUtil {

    /**
     * @param date
     * @param format
     * @return
     */
    public static Date parseDate(String date, String format) {
        return parseDate(date, format, null);
    }

    /**
     * Date转换为指定时间
     *
     * @param date
     * @param format
     * @return
     */
    public static Date parseDate(String date, String format, String timeZone) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (StrUtil.isNotBlank(timeZone)) {
                sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
            }
            return sdf.parse(date);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 把Date格式日期转换为指定格式
     *
     * @param dates
     * @param format
     * @return
     */
    public static String format(Date dates, String format) {
        //        LocalDateTime localDateTime = LocalDateTime.ofInstant(dates.toInstant(), ZoneId.systemDefault());
        //        return DateTimeFormatter.ofPattern(format).format(localDateTime);
        //        return DateFormatUtils.format(dates, format);
        return format(dates, format, null);
    }

    /**
     * 指定时区转换Date
     *
     * @param date
     * @param format
     * @param timzZone
     * @return
     */
    public static String format(Date date, String format, String timzZone) {
        if (null != date && !StrUtil.isBlank(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone(timzZone));
            return format(date, sdf);
        } else {
            return null;
        }
    }

    /**
     * Date转LocalDate
     * 1、将java.token.Date转换为ZonedDateTime。
     * 2、使用它的toLocalDate（）方法从ZonedDateTime获取LocalDate。
     *
     * @param dates
     * @return
     */
    public static LocalDate dateToLocalDate(Date dates) {
        return dates.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date转LocalDateTime
     * 1、从日期获取ZonedDateTime并使用其方法toLocalDateTime（）获取LocalDateTime
     * 2、使用LocalDateTime的Instant（）工厂方法
     *
     * @param dates
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date dates) {

        //        Instant instant = dates.toInstant();
        //        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        //        return localDateTime;

        return LocalDateTime.ofInstant(dates.toInstant(), ZoneId.systemDefault()); // 使用系统的默认时区。
    }

    /**
     * 获取一天的开始
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        Calendar day = Calendar.getInstance();
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        return day.getTime();
    }


}
