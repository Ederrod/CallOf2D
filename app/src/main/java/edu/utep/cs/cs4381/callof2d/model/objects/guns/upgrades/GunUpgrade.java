package edu.utep.cs.cs4381.callof2d.model.objects.guns.upgrades;

import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class GunUpgrade extends GameObject {
    private final float HEIGHT = 1f;
    private final float WIDTH = 1f;

    public GunUpgrade(float worldStartX,
                      float worldStartY,
                      char type) {
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);

        // Where does the tile start
        // X and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
