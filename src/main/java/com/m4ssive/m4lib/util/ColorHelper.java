package com.m4ssive.m4lib.util;

/**
 * Utility class for color operations
 */
public class ColorHelper {
    
    /**
     * Blends two ARGB colors
     * @param color1 First color (ARGB)
     * @param color2 Second color (ARGB)
     * @param blendFactor Blend factor (0.0 = color1, 1.0 = color2)
     * @return Blended color (ARGB)
     */
    public static int blendColors(int color1, int color2, float blendFactor) {
        float t = Math.max(0.0f, Math.min(1.0f, blendFactor));
        
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        
        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        
        int a = (int) (a1 + (a2 - a1) * t);
        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);
        
        a = Math.max(0, Math.min(255, a));
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));
        
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    /**
     * Gets the alpha component from an ARGB color
     */
    public static int getAlpha(int color) {
        return (color >> 24) & 0xFF;
    }
    
    /**
     * Gets the red component from an ARGB color
     */
    public static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }
    
    /**
     * Gets the green component from an ARGB color
     */
    public static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }
    
    /**
     * Gets the blue component from an ARGB color
     */
    public static int getBlue(int color) {
        return color & 0xFF;
    }
    
    /**
     * Creates an ARGB color from components
     */
    public static int argb(int alpha, int red, int green, int blue) {
        alpha = Math.max(0, Math.min(255, alpha));
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
    
    /**
     * Creates an RGB color (fully opaque)
     */
    public static int rgb(int red, int green, int blue) {
        return argb(255, red, green, blue);
    }
    
    /**
     * Applies alpha to a color
     */
    public static int withAlpha(int color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (alpha << 24) | (color & 0x00FFFFFF);
    }
    
    /**
     * Multiplies alpha of a color
     */
    public static int multiplyAlpha(int color, float factor) {
        int alpha = getAlpha(color);
        int newAlpha = (int) (alpha * factor);
        newAlpha = Math.max(0, Math.min(255, newAlpha));
        return withAlpha(color, newAlpha);
    }
}


