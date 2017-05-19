package com.omega.util;

import java.text.DecimalFormat;

/**
 * Utility class related to operations on strings.
 */
public class StringUtils {

    /**
     * Resolve if the string is a decimal.
     *
     * @param value string to parse
     * @return true if decimal, false otherwise
     */
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

    /**
     * Resolve if the string is an integer.
     *
     * @param value string to parse
     * @return true if long, false otherwise
     */
    public static boolean isInteger(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c) && c != '-' && c != '+') {
                return false;
            }
        }

        return true;
    }

    /**
     * Resolve if the string is a boolean.
     *
     * @param value string to parse
     * @return true if boolean, false otherwise
     */
    public static boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");

    }

    /**
     * Parse a string to a corresponding object (Long, Double, Boolean, String)
     *
     * @param value string to parse
     * @return the object parsed from the string
     */
    public static Object parse(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ignored) {
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException ignored) {
        }

        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            return value;
        }
    }

    /**
     * Format a binary size to an human readable string.
     *
     * @param size binary size
     * @return the formatted binary size
     */
    // Thanks to : http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc#5599842
    public static String formatBinarySize(long size) {
        if (size <= 0) {
            return "0";
        }

        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
