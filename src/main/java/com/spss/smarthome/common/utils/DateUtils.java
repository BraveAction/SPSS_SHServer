package com.spss.smarthome.common.utils;

import org.springframework.stereotype.Component;

@Component
public class DateUtils {

    //    @Value("${spring.datasource.sqlDatePattern}")
    private static String pattern = "yyyy-MM-dd HH:mm:ss";

//    public static String currentDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        return sdf.format(new Date());
//    }
}
