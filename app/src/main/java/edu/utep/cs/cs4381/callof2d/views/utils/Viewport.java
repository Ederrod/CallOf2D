package edu.utep.cs.cs4381.callof2d.views.utils;

import android.graphics.Rect;

import edu.utep.cs.cs4381.callof2d.models.Vector2Point5D;

public class Viewport {

    private int screenWidth;
    private int screenHeight;

    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCenterX;
    private int screenCenterY;
    private int metresToShowX;
    private int metresToShowY;
    private int numClipped;

    private Rect convertedRect;

    private Vector2Point5D currentViewportWorldCenter;

    public Viewport(int x, int y) {
        screenWidth = screenXResolution = x;
        screenHeight = screenYResolution = y;

        screenCenterX = screenXResolution / 2;
        screenCenterY = screenYResolution / 2;

        pixelsPerMetreX = screenXResolution / 32;
        pixelsPerMetreY = screenYResolution / 18;

        metresToShowX = 34;
        metresToShowY = 20;

        convertedRect = new Rect();

        currentViewportWorldCenter = new Vector2Point5D();

        numClipped = 0;
    }

    /**
     * Convert the location of an object currently in the visible viewport from
     * world coordinates to pixel coordinates that can actually be drawn to the screen.
     */
    public Rect worldToScreen(float x, float y, float width, float height) {
        int left = (int) (screenCenterX - (currentViewportWorldCenter.x - x) * pixelsPerMetreX);
        int top = (int) (screenCenterY - (currentViewportWorldCenter.y - y) * pixelsPerMetreY);
        int right = (int) (left + width * pixelsPerMetreX);
        int bottom = (int) (top + height * pixelsPerMetreY);
        convertedRect.set(left, top, right, bottom);
        return convertedRect;
    }

    /**  Is the given object outside the viewport. */
    public boolean clipObject(float x, float y, float width, float height) {
        boolean isInside = (x - width < currentViewportWorldCenter.x + metresToShowX / 2)
                && (x + width > currentViewportWorldCenter.x - metresToShowX / 2)
                && (y - height < currentViewportWorldCenter.y + metresToShowY / 2)
                && (y + height > currentViewportWorldCenter.y - metresToShowY / 2);
        if (!isInside) { // for debugging
            numClipped++;
        }
        return !isInside;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getPixelsPerMetreX() {
        return pixelsPerMetreX;
    }

    public int getPixelsPerMetreY() {
        return pixelsPerMetreY;
    }

    public int getYCenter() {
        return screenCenterY;
    }

    public float getViewportWorldCenterY() {
        return currentViewportWorldCenter.y;
    }

    public void setWorldCenter(float x, float y) {
        currentViewportWorldCenter.x = x;
        currentViewportWorldCenter.y = y;
    }
}

