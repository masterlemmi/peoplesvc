package com.lemoncode.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class DateConverter {

    public static LocalDate toLocalDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        return LocalDate.parse(date);
    }
}
