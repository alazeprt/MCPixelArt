package com.alazeprt.mcpixelart.utils;

import net.minecraft.block.MapColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PixelManipulation {
    public static PixelException generateImage(int width, int height, String exportPath, Color[][] matrix) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    Color selectData = matrix[i][j];
                    //System.out.println(i + " " + j + ": " + selectData.getRed() + ", " + selectData.getGreen() + ", " + selectData.getBlue() + ", " + selectData.getAlpha());
                    image.setRGB(i, j, selectData.getRGB());
                }
            }
            File file = getFile(exportPath);
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
            return new PixelException(e);
        }
        return new PixelException(null);
    }

    private static File getFile(String exportPath) {
        File file = new File(exportPath);
        if(file.isDirectory()) {
            file = new File(file.getAbsolutePath() + "/mcpixelart_" + getFormattedDate() + "_paint.png");
        } else if(!file.getName().endsWith("png")) {
            String newFileName;
            if(file.getName().lastIndexOf('.') == -1) {
                newFileName = file.getName() + ".png";
            } else {
                newFileName = file.getName().substring(0, file.getName().lastIndexOf('.')) + ".png";
            }
            file = new File(file.getParent(), newFileName);
        }
        return file;
    }

    public static Color generateColor(MapColor mapColor) {
        long colorCode = mapColor.color;
        System.out.println(colorCode);
        String hexColor = decimalToHex(colorCode);
        return hexToRGB(hexColor);
    }

    private static String decimalToHex(long decimal) {
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

    private static char getHexDigit(long value) {
        if (value >= 0 && value <= 9) {
            return (char) ('0' + value);
        } else {
            return (char) ('A' + (value - 10));
        }
    }

    private static Color hexToRGB(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

    private static String getFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu_M_d_H_m_s");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}
