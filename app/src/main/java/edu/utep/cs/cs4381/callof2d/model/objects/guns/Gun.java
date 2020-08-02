package edu.utep.cs.cs4381.callof2d.model.objects.guns;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.utep.cs.cs4381.callof2d.model.Bullet;
import edu.utep.cs.cs4381.callof2d.model.objects.GameObject;

public class Gun extends GameObject{

    protected int maxBullets = 10;
    protected int numBullets;
    protected int nextBullet;
    protected int rateOfFire = 1;//bullets per second
    protected long lastShotTime;
    protected List<Bullet> bullets;
    protected int speed = 25;

    public Gun(float playerX, float playerY, float playerWidth, float playerHeight, String bitmap) {
        setBitmapName(bitmap);
        setMoves(true);
        setActive(true);
        setVisible(true);
        setWorldLocation(playerX, playerY, 0);
        setFacing(RIGHT);
        bullets = new CopyOnWriteArrayList<Bullet>();
        lastShotTime = -1;
        nextBullet = -1;
    }

    public void update(long fps, float gravity) {
        //update all the bullets
        for(Bullet bullet: bullets) {
            bullet.update(fps, gravity);
        }
        this.move(fps);
    }

    public int getRateOfFire() {
        return rateOfFire;
    }

    public void setFireRate(int rate) {
        rateOfFire = rate;
    }

    public int getNumBullets() {
        //tell the view how many bullets there are
        return numBullets;
    }

    public float getBulletX(int bulletIndex) {
        if(bullets != null && bulletIndex < numBullets) {
            return bullets.get(bulletIndex).getX();
        }
        return -1f;
    }

    public float getBulletY(int bulletIndex) {
        if(bullets != null) {
            return bullets.get(bulletIndex).getY();
        }
        return -1f;
    }

    public void hideBullet(int index) {
        bullets.get(index).hideBullet();
    }

    public int getDirection(int index) {
        return bullets.get(index).getDirection();
    }

    public boolean shoot(float ownerX, float ownerY,
                         int ownerFacing, float ownerHeight) {
        boolean shotFired = false;
        if(System.currentTimeMillis() - lastShotTime > 1000/rateOfFire){
            //spawn another bullet;
            nextBullet ++;
            if(numBullets >= maxBullets){
                numBullets = maxBullets;
            }
            if(nextBullet == maxBullets){
                nextBullet = 0;
            }
            lastShotTime = System.currentTimeMillis();
            bullets.add(nextBullet,
                    new Bullet(ownerX,
                            (ownerY+ ownerHeight/3), speed, ownerFacing));
            shotFired = true;
            numBullets++;
        }
        return shotFired;
    }

    public void upgradeRateOfFire(){
        rateOfFire += 2;
    }

    // Set the location of the machine gun just a little in front of the player location.
    public void updateLocWithPlayer(float playerX, float playerY, float width, float height) {
        float locx, locy = playerY + (height/4);
        if (getFacing() == GameObject.RIGHT) {
            locx = playerX + (width / 2);
        } else {
            locx = playerX - (width / 2);
        }
        setWorldLocationX(locx);
        setWorldLocationY(locy);
    }
}
