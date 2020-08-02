package edu.utep.cs.cs4381.callof2d.model.objects.guns.upgrades;

import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class SmgUpgrade extends GunUpgrade {

    public SmgUpgrade(float worldStartX,
                      float worldStartY,
                      char type) {
        super(worldStartX, worldStartY, type);
        setBitmapName("smg");
    }
}