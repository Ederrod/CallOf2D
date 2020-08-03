package edu.utep.cs.cs4381.callof2d.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import edu.utep.cs.cs4381.callof2d.controller.InputController;
import edu.utep.cs.cs4381.callof2d.model.Background;
import edu.utep.cs.cs4381.callof2d.model.BackgroundData;
import edu.utep.cs.cs4381.callof2d.model.levels.LevelCity;
import edu.utep.cs.cs4381.callof2d.model.levels.LevelData;
import edu.utep.cs.cs4381.callof2d.model.objects.*;
import edu.utep.cs.cs4381.callof2d.model.objects.guns.upgrades.AssaultUpgrade;
import edu.utep.cs.cs4381.callof2d.model.objects.guns.upgrades.SmgUpgrade;
import edu.utep.cs.cs4381.callof2d.model.objects.stationary.Concrete;

public class LevelManager {

    public enum Level {
        CITY,
    }

    private Level level;
    private int mapWidth;
    private int mapHeight;
    private int playerIndex;
    private boolean playing;
    private float gravity;
    private LevelData levelData;
    private List<GameObject> gameObjects;
    private List<Rect> currentButtons;
    private Bitmap[] bitmapsArray;

    private int enemySpawnX;
    private int enemySpawnY;

    private List<Background> backgrounds;

    private List<Player> playerObjects;

    public LevelManager(Context context, int pixelsPerMetre, int screenWidth,
                        InputController ic, Level level, float px, float py) {
        this.level = level;
        switch (level) {
            case CITY:
                levelData = new LevelCity();
                break;
        }
        gameObjects = new ArrayList<>();
        playerObjects = new ArrayList<>();
        bitmapsArray = new Bitmap[25];
        loadMapData(context, pixelsPerMetre, px, py);
        setWaypoints();
        playing = true;
        loadBackgrounds(context, pixelsPerMetre, screenWidth);
    }

    public int getBitmapIndex(char blockType) {
        int index = 0;
        switch (blockType) {
            case '.':
                index = 0;
                break;
            case 'p':
                index = 1;
                break;
            case 'a':
                index = 2;
                break;
            case '5':
                index = 3;
                break;
            case 's':
                index = 4;
                break;
            case 'E':
                index = 5;
                break;
        }
        return index;
    }

    public Bitmap getBitmap(char blockType) {
        return bitmapsArray[getBitmapIndex(blockType)];
    }

    public Level getLevel() {
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

    public LevelData getLevelData() {
        return levelData;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Rect> getCurrentButtons() {
        return currentButtons;
    }

    public List<Background> getBackgrounds() {
        return backgrounds;
    }

    public Player getPlayer() {
        return (Player) gameObjects.get(playerIndex);
    }

    public boolean isPlaying() {
        return playing;
    }

    public List<Player> getPlayerObjects() {
        return playerObjects;
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
        mapHeight = levelData.getTiles().size();
        mapWidth = levelData.getTiles().get(0).length();
        for (int i = 0; i < levelData.getTiles().size(); i++) {
            for (int j = 0; j < levelData.getTiles().get(i).length(); j++) {
                char c = levelData.getTiles().get(i).charAt(j);
                if (c != '.') {
                    currentIndex++;
                    switch (c) {
                        case 'p':
                            playerIndex = currentIndex;
                            gameObjects.add(new Player(context, px, py, pixelsPerMeter, 'p'));
                            playerObjects.add(getPlayer());
                            break;
                        case 'E':
                            Enemy enemy = new Enemy(context, j, i, pixelsPerMeter);
                            gameObjects.add(enemy);
                            playerObjects.add(enemy);
                            break;
                        case 'a':
                            gameObjects.add(new AssaultUpgrade(j, i, c));
                            break;
                        case '5':
                            gameObjects.add(new Concrete(j, i, c));
                            break;
                        case 's':
                            gameObjects.add(new SmgUpgrade(j, i, c));
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

    private void loadBackgrounds(Context context, int pixelsPerMetre, int screenWidth) {
        backgrounds = new ArrayList<>();

        for (BackgroundData bgData : levelData.getBackgroundDataList()) {
            backgrounds.add(new Background(context, pixelsPerMetre, screenWidth, bgData));
        }
    }
}
