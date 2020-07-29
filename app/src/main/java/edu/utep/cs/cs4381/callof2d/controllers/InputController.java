package edu.utep.cs.cs4381.callof2d.controllers;

import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4381.callof2d.managers.GameManager;
//import edu.utep.cs.cs4381.platformer.model.SoundManager;
import edu.utep.cs.cs4381.callof2d.views.utils.Viewport;

public class InputController {
    Rect left;
    Rect right;
    Rect jump;
    Rect shoot;
    Rect pause;

    public InputController(int screenWidth, int screenHeight) {
        //Configure the player buttons
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;
        left = new Rect(buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth,
                screenHeight - buttonPadding);
        right = new Rect(buttonWidth + buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                buttonWidth + buttonPadding + buttonWidth,
                screenHeight - buttonPadding);
        jump = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding -
                        buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding - buttonHeight -
                        buttonPadding);
        shoot = new Rect(screenWidth - buttonWidth - buttonPadding,
                screenHeight - buttonHeight - buttonPadding,
                screenWidth - buttonPadding,
                screenHeight - buttonPadding);
        pause = new Rect(screenWidth - buttonPadding -
                buttonWidth,
                buttonPadding,
                screenWidth - buttonPadding,
                buttonPadding + buttonHeight);
    }

    public List<Rect> getButtons(){
        //create an array of buttons for the draw method
        ArrayList<Rect> currentButtonList = new ArrayList<>();  // FIXME
        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(jump);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);
        return currentButtonList;
    }

    public void handleInput(MotionEvent motionEvent, GameManager manager, /*SoundManager sound,*/ Viewport vp) {
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            if(manager.isPlaying()) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (right.contains(x, y)) {
                            manager.getPlayer().setPressingRight(true);
                            manager.getPlayer().setPressingLeft(false);
                        } else if (left.contains(x, y)) {
                            manager.getPlayer().setPressingLeft(true);
                            manager.getPlayer().setPressingRight(false);
                        } else if (jump.contains(x, y)) {
                            manager.getPlayer().startJump(/*sound*/); // FIXME
                        } else if (shoot.contains(x, y)) {
                            if (manager.getPlayer().pullTrigger()) {
//                                sound.play(SoundManager.Sound.SHOOT);
                            }
                        } else if (pause.contains(x, y)) {
                            manager.switchPlayingStatus();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (right.contains(x, y)) {
                            manager.getPlayer().setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            manager.getPlayer().setPressingLeft(false);
                        }
                        break;
                    //Handle shooting here
                    case MotionEvent.ACTION_POINTER_UP:
                        if (right.contains(x, y)) {
                            manager.getPlayer().setPressingRight(false);
                        } else if (left.contains(x, y)) {
                            manager.getPlayer().setPressingLeft(false);
                        } else if (shoot.contains(x, y)) {
                            //Handle shooting here
                        } else if (jump.contains(x, y)) {
                            //Handle more jumping stuff here later
                        }
                        break;
                }// End if(l.playing)
            } else {// Not playing
                //Move the viewport around to explore the map
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (pause.contains(x, y)) {
                            manager.switchPlayingStatus();
                        }
                        break;
                }
            }
        }
    }
}