package com.tkm.himalaya.utils;

public class StringUtil {
    public static String formatNumberFriendly(long number) {
        if (number < 10000) return String.valueOf(number);

        float fixNumber = (float) number / 10000;
        return String.format("%.1f万", fixNumber);
    }
}
