package edu.utep.cs.cs4381.callof2d.model.objects.stationary;

import java.util.Random;

import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class Stalactite extends GameObject {
    public Stalactite(float worldStartX, float worldStartY, char type) {
        final float HEIGHT = 3;
        final float WIDTH = 2;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("stalactite");
        setActive(false);
        Random rand = new Random();
        if(rand.nextInt(2)==0) {
            setWorldLocation(worldStartX, worldStartY, -1);
        }else{
            setWorldLocation(worldStartX, worldStartY, 1);
        }
    }

    public void update(long fps, float gravity) {
    }
}