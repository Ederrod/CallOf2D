package edu.utep.cs.cs4381.callof2d.model.objects.guns;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.utep.cs.cs4381.callof2d.model.Bullet;
import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class Smg extends Gun {

    private static final float HEIGHT = 1f;
    private static final float WIDTH = 1f;

    public Smg(float playerX, float playerY, float playerWidth, float playerHeight) {
        super(playerX, playerY, playerWidth, playerHeight, "smg");
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setDamage(20);
        setFireRate(5);
    }
}