package com.jr.data;

import java.awt.Color;
import java.util.NoSuchElementException;

/**
 * @author Julian Ragan
 *
 */
public class PastelPalette {

    /**
     * Returns a color from pastel palette. Contains 10 colors, use modulo 10
     * for longer lists;
     *
     * @param index of color to return
     * @return java.awt.Color instance
     */
    public static Color getColor(int index) throws NoSuchElementException {
        switch (index) {
            case 0:
                return new Color(209, 234, 163);
            case 1:
                return new Color(255, 203, 203);
            case 2:
                return new Color(166, 177, 225);
            case 3:
                return new Color(234, 176, 217);
            case 4:
                return new Color(167, 233, 175);
            case 5:
                return new Color(255, 224, 172);
            case 6:
                return new Color(238, 249, 191);
            case 7:
                return new Color(129, 245, 255);
            case 8:
                return new Color(166, 255, 230);
            case 9:
                return new Color(229, 138, 138);
            default:
                throw new NoSuchElementException();
        }
    }
}
