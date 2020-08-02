package edu.utep.cs.cs4381.callof2d.model;

import android.graphics.Rect;

import edu.utep.cs.cs4381.callof2d.model.Vector2Point5D;

public class Viewport {

    private Vector2Point5D currentViewportWorldCenter;
    private Rect convertedRect;
    private int pixelsPerMeterX;
    private int pixelsPerMeterY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCenterX;
    private int screenCenterY;
    private int metresToShowX;
    private int metresToShowY;
    private int numClipped;

    public Viewport(int width, int height) {
        screenXResolution = width;
        screenYResolution = height;
        screenCenterX = screenXResolution / 2;
        screenCenterY = screenYResolution / 2;
        pixelsPerMeterX = screenXResolution / 32;
        pixelsPerMeterY = screenYResolution / 16; //16;
        metresToShowX = 34;
        metresToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenter = new Vector2Point5D();
    }

    public void setWorldCenter(float x, float y) {
        currentViewportWorldCenter.x = x;
        currentViewportWorldCenter.y = y;
    }

    public int getScreenWidth() {
        return screenXResolution;
    }

    public int getScreenHeight() {
        return screenYResolution;
    }

    public int getPixelsPerMetreX() {
        return pixelsPerMeterX;
    }

    public Rect worldToScreen(float x, float y, float width, float height) {
        int left = (int) (screenCenterX - (currentViewportWorldCenter.x - x) * pixelsPerMeterX);
        int top = (int) (screenCenterY - (currentViewportWorldCenter.y - y) * pixelsPerMeterY);
        int right = (int) (left + width * pixelsPerMeterX);
        int bottom = (int) (top + height * pixelsPerMeterY);
        convertedRect.set(left, top, right, bottom);
        return convertedRect;
    }

    public boolean clipObject(float x, float y, float width, float height) {
        boolean notClipped = (x - width < currentViewportWorldCenter.x + metresToShowX / 2f)
                && (x + width > currentViewportWorldCenter.x - metresToShowX / 2f)
                && (y - height < currentViewportWorldCenter.y + metresToShowY / 2f)
                && (y + height > currentViewportWorldCenter.y - metresToShowY / 2f);
        if (notClipped) {
            numClipped++;
        }
        return !notClipped;
    }

    public int getNumClipped(){
        return numClipped;
    }

    public void resetNumClipped(){
        numClipped = 0;
    }

    public int getPixelsPerMetreY(){
        return pixelsPerMeterY;
    }

    public int getyCentre(){
        return screenCenterY;
    }

    public float getViewportWorldCentreY(){
        return currentViewportWorldCenter.y;
    }

    public void moveViewportRight(int maxWidth){
        if(currentViewportWorldCenter.x < maxWidth -
                (metresToShowX/2f)+3) {
            currentViewportWorldCenter.x += 1;
        }
    }
    public void moveViewportLeft(){
        if(currentViewportWorldCenter.x > (metresToShowX/2f)-3){
            currentViewportWorldCenter.x -= 1;
        }
    }
    public void moveViewportUp(){
        if(currentViewportWorldCenter.y > (metresToShowY /2f)-3) {
            currentViewportWorldCenter.y -= 1;
        }
    }
    public void moveViewportDown(int maxHeight){
        if(currentViewportWorldCenter.y <
                maxHeight - (metresToShowY / 2f)+3) {
            currentViewportWorldCenter.y += 1;
        }
    }
}
