package edu.utep.cs.cs4381.callof2d.model.objects.stationary;

import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class Grass extends GameObject {
    final private static float HEIGHT = 1;
    final private static float WIDTH = 1;

    public Grass(float worldStartX, float worldStartY, char type) {
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("turf");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();

        setTraversable();
    }

    public void update(long fps, float gravity) {}
}