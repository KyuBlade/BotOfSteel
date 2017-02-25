package com.omega.util;

public class StringUtils {

    public static boolean isDecimal(String value) {
        boolean pointFound = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c)) {
                if (c == '.' || c == ',') {
                    pointFound = true;
                } else if (c != '-' && c != '+') {
                    return false;
                }
            }
        }

        return pointFound;
    }

    public static boolean isInteger(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c) && c != '-' && c != '+') {
                return false;
            }
        }

        return true;
    }

    public static boolean isBoolean(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return true;
        }

        return false;
    }

    public static Object parse(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
        }

        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            return value;
        }
    }
}
