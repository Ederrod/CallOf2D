package edu.utep.cs.cs4381.callof2d.model.objects.guns.upgrades;

public class AssaultUpgrade extends GunUpgrade {

    public AssaultUpgrade(float worldStartX, float worldStartY, char type) {
        super(worldStartX, worldStartY, type);
        setBitmapName("assaultrifle");
    }
}
