package edu.utep.cs.cs4381.callof2d.model.objects.stationary;

import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class Coin extends GameObject {

    public Coin(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = .5f;
        final float WIDTH = .5f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        // Choose a Bitmap
        setBitmapName("coin");
        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }
}