package com.activiti7.activiti7imoocdevelop.util;

import com.activiti7.activiti7imoocdevelop.common.Constants;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author debao.yang
 * @since 2022/7/21 10:51
 */
public class DateUtil {

    public static String formatDefault(Date paramDate) {
        LocalDateTime localDateTime = dateToLocalDateTime(paramDate);
        return formatLocalDateTime(localDateTime, null);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String formatLocalDateTime(LocalDateTime dateTime,
                                             String formater) {
        formater = StringUtils.isNoneBlank(formater) ? formater :
                Constants.DEFAULT_FORMATER;
        return DateTimeFormatter.ofPattern(formater).format(dateTime);
    }

    @SneakyThrows
    public static Date stringToDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(date);
    }

}
