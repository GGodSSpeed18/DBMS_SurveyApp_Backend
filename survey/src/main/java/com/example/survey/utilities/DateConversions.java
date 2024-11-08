package com.example.survey.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversions {
    private static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(MYSQL_DATE_FORMAT);

    public static String dateToMySQLString(Date date) {
        if (date == null) {
            return dateFormat.format(new Date());
        } else {
            return dateFormat.format(date);
        }
    }

    public static Date mySQLStringToDate(String date) throws ParseException {
        if (date == null || date.trim().isEmpty()) {
            return new Date();
        } else {
            return dateFormat.parse(date);
        }
    }
}
