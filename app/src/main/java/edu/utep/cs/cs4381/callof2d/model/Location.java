package edu.utep.cs.cs4381.callof2d.model;

import edu.utep.cs.cs4381.callof2d.managers.LevelManager.Level;
public class Location {
    Level level;

    float x;
    float y;

    public Location(Level level, float x, float y){
        this.level = level;
        this.x = x;
        this.y = y;
    }

    public Level getLevel() {
        return level;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}