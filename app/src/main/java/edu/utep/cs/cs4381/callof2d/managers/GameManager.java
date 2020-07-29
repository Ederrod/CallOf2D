package edu.utep.cs.cs4381.callof2d.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4381.callof2d.controllers.InputController;
import edu.utep.cs.cs4381.callof2d.models.GameMap;
import edu.utep.cs.cs4381.callof2d.models.GameObject;
import edu.utep.cs.cs4381.callof2d.models.Ground;
import edu.utep.cs.cs4381.callof2d.models.MapData;
import edu.utep.cs.cs4381.callof2d.models.Player;

public class GameManager {

    private String level;
    private int mapWidth;
    private int mapHeight;

    private Player player;
    private int playerIndex;

    private boolean playing;
    private float gravity;
    private MapData mapData;
    private List<GameObject> gameObjects;
    private List<Rect> currentButtons;
    private Bitmap[] bitmapsArray;

    public GameManager(Context context, int pixelsPerMetre, int screenWidth,
                        InputController ic, String level, float px, float py) {
        this.level = level;
        switch (level) {
            case "GameMap":
                mapData = new GameMap();
                break;
        }
        gameObjects = new ArrayList<>();
        bitmapsArray = new Bitmap[25];
        loadMapData(context, pixelsPerMetre, px, py);
//        setWaypoints();
        playing = true;
    }

    public int getBitmapIndex(char blockType) {
        int index = 0;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case '1':
                index = 1;
                break;
            case 'p':
                index = 2;
                break;
            case 'c':
                index = 3;
                break;
            case 'u':
                index = 4;
                break;
            case 'e':
                index = 5;
                break;
            case 'd':
                index = 6;
                break;
            case 'g':
                index = 7;
                break;
        }
        return index;
    }

    public Bitmap getBitmap(char blockType) {
        return bitmapsArray[getBitmapIndex(blockType)];
    }

    public String getLevel() {
        return level;
    }

    public Bitmap[] getBitmapsArray() {
        return bitmapsArray;
    }

    public float getGravity() {
        return gravity;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public MapData getMapData() {
        return mapData;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Rect> getCurrentButtons() {
        return currentButtons;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void switchPlayingStatus() {
        playing = !playing;
        if (playing) {
            gravity = 6;
        } else {
            gravity = 0;
        }
    }

    public void setWaypoints() {
        for (GameObject guard: gameObjects) {
            if (guard.getType() == 'g') {
                int feetTileIndex = -1; // index of the tile beneath the guard
                float leftEnd = -1, rightEnd = -1;  // left and right ends of the calculated route
                for (GameObject tile: gameObjects) {
                    feetTileIndex++;
                    if (tile.getWorldLocation().y == guard.getWorldLocation().y + 2
                            &&  tile.getWorldLocation().x == guard.getWorldLocation().x) {
                        leftEnd = gameObjects.get(feetTileIndex - 5).getWorldLocation().x;
                        for (int i = 1; i <= 5; i++) {
                            GameObject left = gameObjects.get(feetTileIndex - i);
                            if (left.getWorldLocation().x != guard.getWorldLocation().x - i
                                    || left.getWorldLocation().y != guard.getWorldLocation().y + 2
                                    || !left.isTraversable()) {
                                leftEnd = gameObjects.get(feetTileIndex - (i - 1)).getWorldLocation().x;
                                break; } }

                        rightEnd = gameObjects.get(feetTileIndex + 5).getWorldLocation().x;
                        for (int i = 1; i <= 5; i++) {
                            GameObject right = gameObjects.get(feetTileIndex + i);
                            if (right.getWorldLocation().x != guard.getWorldLocation().x + i
                                    || right.getWorldLocation().y != guard.getWorldLocation().y + 2
                                    || !right.isTraversable()) {
                                rightEnd = gameObjects.get(feetTileIndex + (i - 1)).getWorldLocation().x;
                                break; } }

//                        ((Guard) guard).setWaypoints(leftEnd, rightEnd);
                    }
                }
            }
        }
    }

    private void loadMapData(Context context, int pixelsPerMeter, float px, float py) {
        int currentIndex = -1;
        mapHeight = mapData.getTiles().size();
        mapWidth = mapData.getTiles().get(0).length();
        for (int i = 0; i < mapData.getTiles().size(); i++) {
            for (int j = 0; j < mapData.getTiles().get(i).length(); j++) {
                char c = mapData.getTiles().get(i).charAt(j);
                if (c != '.') {
                    currentIndex++;
                    switch (c) {
                        case '1':
                            gameObjects.add(new Ground(j, i));
                            break;
                        case 'p':
                            player = new Player(context, px, py, pixelsPerMeter);
                            gameObjects.add(player);
                            playerIndex = currentIndex;
                            break;
                        case 'c':
//                            gameObjects.add(new Coin(j, i, c));
                            break;
                        case 'u':
//                            gameObjects.add(new MachineGunUpgrade(j, i, c));
                            break;
                        case 'e':
//                            gameObjects.add(new ExtraLife(j, i, c));
                            break;
                        case 'd':
//                            gameObjects.add(new Drone(j, i, c));
                            break;
                        case 'g':
//                            gameObjects.add(new Guard(context, j, i, c, pixelsPerMeter));
                            break;
                    }
                    if (bitmapsArray[getBitmapIndex(c)] == null) {
                        GameObject go = gameObjects.get(currentIndex);
                        bitmapsArray[getBitmapIndex(c)] = go.prepareBitmap(context,
                                go.getBitmapName(), pixelsPerMeter);
                    }
                }
            }
        }
    }
}
