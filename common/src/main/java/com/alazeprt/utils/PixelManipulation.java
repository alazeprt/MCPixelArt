package com.alazeprt.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PixelManipulation {
    private final BufferedImage image;
    public PixelManipulation(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    public PixelException setRGB(int x, int y, Color color) {
        try {
            this.image.setRGB(x, y, color.getRGB());
        } catch (Exception e) {
            e.printStackTrace();
            return new PixelException(e);
        }
        return new PixelException(null);
    }
    public PixelException export(String exportPath) {
        try {
            File file = getFile(exportPath);
            ImageIO.write(this.image, "png", file);
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

    private static String getFormattedDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu_M_d_H_m_s");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}
