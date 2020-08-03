package edu.utep.cs.cs4381.callof2d.model.objects.guns;

public class AssultRifle extends Gun{
    private static final float HEIGHT = 1f;
    private static final float WIDTH = 1.5f;

    public AssultRifle(float playerX, float playerY, float playerWidth, float playerHeight) {
        super(playerX, playerY, playerWidth, playerHeight, "assaultrifle");
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setDamage(30);
        setFireRate(3);
    }
}
