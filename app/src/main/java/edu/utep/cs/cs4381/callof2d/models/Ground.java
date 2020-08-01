package edu.utep.cs.cs4381.callof2d.models;

import edu.utep.cs.cs4381.callof2d.models.gameobjects.GameObject;

public class Ground extends GameObject {
    final private static float HEIGHT = 1f;
    final private static float WIDTH = 1f;

    public Ground(float worldStartX, float worldStartY) {

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType('1');
        setBitmapName("turf");

        setWorldLocation(worldStartX, worldStartY, 0);

        setRectHitbox();

        setTraversable();
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
