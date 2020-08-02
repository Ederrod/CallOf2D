package edu.utep.cs.cs4381.callof2d.model;

import android.graphics.PointF;

public class PlayerState {

    private int numCredits;
    private int mgFireRate;
    private int health;
    private float restartX;
    private float restartY;

    public PlayerState() {
        health = 100;
        mgFireRate = 1;
        numCredits = 0;
    }

    public void saveLocation(PointF location) {
        // The location saves each time the player uses a teleport
        restartX = location.x;
        restartY = location.y;
    }
    public PointF loadLocation() {
        // Used every time the player loses a life
        return new PointF(restartX, restartY);
    }

    public int getHealth(){
        return health;
    }

    public int getFireRate(){
        return mgFireRate;
    }

    public void increaseFireRate(){
        mgFireRate += 2;
    }

    public void gotCredit(){
        numCredits ++;
    }

    public int getCredits(){
        return numCredits;
    }

    public void decreaseHealth() {
        health -= 10;
    }

//    public void loseLife(){
//        lives--;
//    }
//
//    public void addLife(){
//        lives++;
//    }

//    public void resetLives(){
//        lives = 3;
//    }
//
//    public void resetCredits(){
//        lives = 0;
//    }
}