package edu.utep.cs.cs4381.callof2d.model.objects;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4381.callof2d.model.RectHitbox;
import edu.utep.cs.cs4381.callof2d.managers.SoundManager;
import edu.utep.cs.cs4381.callof2d.model.Vector2Point5D;
import edu.utep.cs.cs4381.callof2d.model.objects.guns.*;

public class Player extends GameObject {
    private static final String BITMAP_NAME = "soldier";

    private static final float HEIGHT = 2;
    private static final float WIDTH = 1;

    final float INIT_X_VELOCITY = 10;
    final float MAX_X_VELOCITY = 25;

    boolean isPressingRight = false;
    boolean isPressingLeft = false;
    public boolean isFalling;
    private boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700;// jump 7 10ths of second
    private float incrementalVel;


    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    public Gun bfg;

    public Player(Context context, float worldStartX, float worldStartY, int pixelsPerMetre, char type) {
        setHeight(HEIGHT); // 2 meters tall
        setWidth(WIDTH);   // 1 meter wide
        setType(type);
        setBitmapName(BITMAP_NAME);

        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT = 4;
        // Set this object up to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelsPerMetre, true);

        setWorldLocation(worldStartX, worldStartY, 0);

        // Standing still to start with
        setxVelocity(0);
        setyVelocity(0);
        setFacing(RIGHT);
        isFalling = false;

        if (type == 'p') {
            incrementalVel = 0.5f;
        } else {
            incrementalVel = 0.1f;
        }

        // Now for the player's other attributes
        // Our game engine will use these
        setMoves(true);
        setActive(true);
        setVisible(true);

        rectHitboxFeet = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxRight = new RectHitbox();

        bfg = new Smg(worldStartX, worldStartY, getWidth(), getHeight());
    }

    public void setPressingRight(boolean flag) {
        isPressingRight = flag;
    }

    public float incrementVelocity = 0;
    public void update(long fps, float gravity) {
//        if (getType() == 'E') {
//            setPressingLeft(true);
//        }

        if ((incrementVelocity + INIT_X_VELOCITY) < MAX_X_VELOCITY) {
            incrementVelocity += incrementalVel;
        }

        if (isPressingRight) {
            this.setxVelocity(INIT_X_VELOCITY + incrementVelocity);
        } else if (isPressingLeft) {
            this.setxVelocity(-(INIT_X_VELOCITY+ incrementVelocity));
        } else {
            incrementVelocity = 0;
            this.setxVelocity(0);
        }
        //which way is player facing?
        if (this.getxVelocity() > 0) {
            //facing right
            bfg.setFacing(RIGHT);
            setFacing(RIGHT);
        } else if (this.getxVelocity() < 0) {
            //facing left
            bfg.setFacing(LEFT);
            setFacing(LEFT);
        }//if 0 then unchanged

        // Jumping and gravity
        if (isJumping) {
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    this.setyVelocity(-gravity);//on the way up
                } else if (timeJumping > maxJumpTime / 2) {
                    this.setyVelocity(gravity);//going down
                }
            } else {
                isJumping = false;
            }
        } else {
            this.setyVelocity(gravity);
            // Read Me!
            // Remove this next line to make the game easier
            // it means the long jumps are less punishing
            // because the player can take off just after the platform
            // They will also be able to cheat by jumping in thin air
            isFalling = true;
        }

        bfg.update(fps, gravity);

        // Let's go!
        this.move(fps);
        // Update the machine gun bitmap based on the location of the player.
        bfg.updateLocWithPlayer(this.getWorldLocation().x, this.getWorldLocation().y, getWidth(), getHeight());


        // Update all the hitboxes to the new location
        // Get the current world location of the player
        // and save them as local variables we will use next
        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        //update the player feet hitbox
        rectHitboxFeet.setTop(ly + getHeight() * .95f);
        rectHitboxFeet.setLeft(lx + getWidth() * .2f);
        rectHitboxFeet.setBottom(ly + getHeight() * .98f);
        rectHitboxFeet.setRight(lx + getWidth() * .8f);

        // Update player head hitbox
        rectHitboxHead.setTop(ly);
        rectHitboxHead.setLeft(lx + getWidth() * .4f);
        rectHitboxHead.setBottom(ly + getHeight() * .2f);
        rectHitboxHead.setRight(lx + getWidth() * .6f);

        // Update player left hitbox
        rectHitboxLeft.setTop(ly + getHeight() * .2f);
        rectHitboxLeft.setLeft(lx + getWidth() * .2f);
        rectHitboxLeft.setBottom(ly + getHeight() * .8f);
        rectHitboxLeft.setRight(lx + getWidth() * .3f);

        // Update player right hitbox
        rectHitboxRight.setTop(ly + getHeight() * .2f);
        rectHitboxRight.setLeft(lx + getWidth() * .8f);
        rectHitboxRight.setBottom(ly + getHeight() * .8f);
        rectHitboxRight.setRight(lx + getWidth() * .7f);
    }

    public int checkCollisions(RectHitbox rectHitbox) {
        int collided = 0;// No collision
        // The left
        if (this.rectHitboxLeft.intersects(rectHitbox)) {
            // Left has collided
            // Move player just to right of current hitbox
            this.setWorldLocationX(rectHitbox.getRight() - getWidth() * .2f);
            collided = 1;
        }
        // The right
        if (this.rectHitboxRight.intersects(rectHitbox)) {
            // Right has collided
            // Move player just to left of current hitbox
            this.setWorldLocationX(rectHitbox.getLeft() - getWidth() * .8f);
            collided = 1;
        }
        // The feet
        if (this.rectHitboxFeet.intersects(rectHitbox)) {
            // Feet have collided
            // Move feet to just above current hitbox
            this.setWorldLocationY(rectHitbox.getTop() - getHeight());
            collided = 2;
        }
        // Now the head
        if (this.rectHitboxHead.intersects(rectHitbox)) {
            // Head has collided. Ouch!
            // Move head to just below current hitbox bottom
            this.setWorldLocationY(rectHitbox.getBottom());
            collided = 3;
        }
        return collided;
    }

    public void setPressingLeft(boolean isPressingLeft) {
        this.isPressingLeft = isPressingLeft;
    }

    public void startJump(SoundManager sm) {
        if (!isFalling) {//can't jump if falling
            if (!isJumping) {//not already jumping
                isJumping = true;
                jumpTime = System.currentTimeMillis();
//                sm.play(SoundManager.Sound.JUMP);
            }
        }
    }

    public boolean pullTrigger() {
        //Try and fire a shot
        return bfg.shoot(
                this.getWorldLocation().x,
                this.getWorldLocation().y,
                getFacing(),
                getHeight()
        );
    }

    public void restorePreviousVelocity() {
        if (!isJumping && !isFalling) {
            if (getFacing() == LEFT) {
                isPressingLeft = true;
                setxVelocity(-INIT_X_VELOCITY);
            } else {
                isPressingRight = true;
                setxVelocity(INIT_X_VELOCITY);
            }
        }
    }

    public void upgradeGun(Gun gun) {
        bfg = gun;
    }

    public GameObject getGun() {
        return bfg;
    }

    public List<RectHitbox> getHitboxes() {
        List<RectHitbox> hitboxes = new ArrayList<RectHitbox>();
        hitboxes.add(rectHitboxFeet);
        hitboxes.add(rectHitboxHead);
        hitboxes.add(rectHitboxLeft);
        hitboxes.add(rectHitboxRight);
        return hitboxes;
    }
}