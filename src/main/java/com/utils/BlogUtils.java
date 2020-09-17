package com.utils;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.*;
import java.util.Date;

public class BlogUtils {
    /**
     * 以 Java8 的方式获取当前时间字符串
     *
     * @return 当前时间格式化之后的字符串
     */
    public static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String formatDatetime(Long gmtCreate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(gmtCreate);
        return sdf.format(date);
    }
}
