package com.einarvalgeir.bussrapport.util;

import org.joda.time.DateTime;

public class DateUtil {

    public static DateTime createFormattedDate(int year, int month, int day) {
        return new DateTime(year, month, day, 0, 0);
    }
}
