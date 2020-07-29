package edu.utep.cs.cs4381.callof2d.models;

import java.util.ArrayList;

public class GameMap extends MapData{
    public GameMap() {
        tiles = new ArrayList<String>();

        tiles.add("p................................................");
        tiles.add(".................................................");
        tiles.add(".................................................");
        tiles.add(".................................................");
        tiles.add("1111111111111111111111111111111111111111111111111");
    }
}
