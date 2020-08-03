package edu.utep.cs.cs4381.callof2d.model.objects;

import android.content.Context;

public class Enemy extends Player{

    // Only allow a max of 5 respawns per enemy
    private final int MAX_RESPAWN = 5;

    private float startingX;
    private float startingY;

    private int totalRespawn;

    public Enemy(Context context, float worldStartX, float worldStartY, int pixelsPerMetre) {
        super(context, worldStartX, worldStartY, pixelsPerMetre, 'E');
        setIncrementalVel(0.1f);
        startingX = worldStartX;
        startingY = worldStartY;
        totalRespawn = 1;
    }

    public void respawn() {
        if (++totalRespawn <= 2) {
            setWorldLocationX(startingX);
            setWorldLocationY(startingY);
        } else {
            setActive(false);
            setVisible(false);
        }
    }
}
