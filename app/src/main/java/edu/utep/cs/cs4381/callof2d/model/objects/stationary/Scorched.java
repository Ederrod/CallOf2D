package edu.utep.cs.cs4381.callof2d.model.objects.stationary;

import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class Scorched extends GameObject {
    public Scorched(float worldStartX, float worldStartY, char type) {
        setTraversable();
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("scorched");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public void update(long fps, float gravity) {
    }
}