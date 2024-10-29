package com.yml.icas.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {
    public static Double toDouble(Object doubleString) {
        double d = 0.0;
        try {
            d = Double.parseDouble(String.valueOf(doubleString));
        } catch (Exception ignored) {
        }
        return d;
    }

    public static Date toDate(Object dateString) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse((String) dateString);
        } catch (Exception ignored) {
        }
        return d;
    }
}
