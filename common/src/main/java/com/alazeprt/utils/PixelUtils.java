package com.alazeprt.utils;

import java.awt.*;

public class PixelUtils {
    public static String decimalToHex(long decimal) {
        if (decimal == 0) {
            return "0";
        }

        StringBuilder hexBuilder = new StringBuilder();
        while (decimal > 0) {
            long remainder = decimal % 16;
            hexBuilder.insert(0, getHexDigit(remainder));
            decimal /= 16;
        }

        return hexBuilder.toString();
    }

    public static char getHexDigit(long value) {
        if (value >= 0 && value <= 9) {
            return (char) ('0' + value);
        } else {
            return (char) ('A' + (value - 10));
        }
    }

    public static Color hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }
}
