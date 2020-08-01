package edu.utep.cs.cs4381.callof2d.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import edu.utep.cs.cs4381.callof2d.controllers.InputController;
import edu.utep.cs.cs4381.callof2d.managers.GameManager;
import edu.utep.cs.cs4381.callof2d.models.Background;
import edu.utep.cs.cs4381.callof2d.models.gameobjects.GameObject;
import edu.utep.cs.cs4381.callof2d.views.utils.Viewport;

public class GameView extends SurfaceView implements Runnable {

    // Game state variables
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    // Android view variables
    private SurfaceHolder holder;

    // Drawing variables
    private Paint paint;
    private Canvas canvas;

    // Application frames calculation variables
    private long startFrameTime;
    private long totalFrameTime;
    private long fps;

    // 2D coordinates to screen coordinates helper
    private Viewport viewport;

    // Game state variables.
    private GameManager manager;

    // Input handler
    private InputController inputCon;


    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context);

        holder = getHolder();
        paint = new Paint();

        viewport = new Viewport(screenWidth, screenHeight);

        loadGame(1,16);

    }

    @Override
    public void run() {
        while (running) {
            startFrameTime = System.currentTimeMillis();
            update();
            draw();
            totalFrameTime = System.currentTimeMillis() - startFrameTime;

            // Calculate frames per second
            if (totalFrameTime >= 1) {
                fps = 1000 / totalFrameTime;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (manager != null) {
            inputCon.handleInput(event, manager, viewport);
        }
        return true;
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("GameView", "Failed to pause thread");
        }
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void loadGame(float px, float py) {
        inputCon = new InputController(viewport.getScreenWidth(), viewport.getScreenHeight());

        manager = new GameManager(
                this.getContext(),
                viewport.getPixelsPerMetreX(),
                viewport.getScreenWidth(),
                inputCon,
                "GameMap",
                px,
                py
        );

        viewport.setWorldCenter(
                manager.getPlayer().getWorldLocation().x,
                manager.getPlayer().getWorldLocation().y
        );
    }

    private void update() {
        for (GameObject go : manager.getGameObjects()) {
            if (go.isActive()) {
                boolean clipped = viewport.clipObject(
                        go.getWorldLocation().x,
                        go.getWorldLocation().y,
                        go.getWidth(),
                        go.getHeight()
                );

                if (!clipped) {
                    go.setVisible(true);

                    // TODO: Check for collisions here
                    int collision = manager.getPlayer().checkCollisions(go.getHitbox());

                    if (collision > 0) {
                        switch (go.getType()) {
                            default:
                                 if (collision == 1) {
                                     manager.getPlayer().setxVelocity(0);
                                     manager.getPlayer().setPressingRight(false);
                                 }
                                 if (collision == 2) {
                                     manager.getPlayer().isFalling = false;
                                 }
                                 break;
                        }
                    }

                    if (manager.isPlaying()) {
                        go.update(fps, manager.getGravity());
                    }

                }else {
                    go.setVisible(false);
                }
            }
        }

        if (manager.isPlaying()) {
            viewport.setWorldCenter(
                    manager.getPlayer().getWorldLocation().x,
                    manager.getPlayer().getWorldLocation().y
            );
        }
    }


    private void draw() {
        if (holder.getSurface().isValid()) {
            // Lock canvas from any other process that might want to use it
            canvas = holder.lockCanvas();

            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawColor(Color.argb(255, 0, 0, 255));

            drawBackground(0, -3);

            Rect toScreen2d = new Rect();
            for (int layer = -1; layer <= 1; layer++) {
                for (GameObject go : manager.getGameObjects()) {
                    if (go.isVisible() && go.getWorldLocation().z == layer) {
                        toScreen2d.set(
                                viewport.worldToScreen(
                                        go.getWorldLocation().x,
                                        go.getWorldLocation().y,
                                        go.getWidth(),
                                        go.getHeight()
                                )
                        );

                        if (go.isAnimated()) {
                            if (go.getFacing() == GameObject.LEFT) {
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1, 1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(
                                        manager.getBitmapsArray()[manager.getBitmapIndex(go.getType())],
                                        r.left,
                                        r.top,
                                        r.width(),
                                        r.height(),
                                        flipper,
                                        true
                                );
                                canvas.drawBitmap(b, toScreen2d.left, toScreen2d.top, paint);
                            } else {
                                canvas.drawBitmap(
                                        manager.getBitmapsArray()[manager.getBitmapIndex(go.getType())],
                                        go.getRectToDraw(System.currentTimeMillis()),
                                        toScreen2d,
                                        paint
                                );
                            }
                        } else {
                            canvas.drawBitmap(
                                    manager.getBitmapsArray()[manager.getBitmapIndex(go.getType())],
                                    toScreen2d.left,
                                    toScreen2d.top,
                                    paint
                            );
                        }
                    }
                }
            }

            // Draw buttons
            paint.setColor(Color.argb(80, 255,255,255));

            List<Rect> buttonsToDraw = inputCon.getButtons();
            for (Rect r : buttonsToDraw) {
                RectF rf = new RectF(r.left, r.top, r.right, r.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }

            // After all operations are done with canvas, release for other processes
            // that might need it.
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawBackground(int start, int stop) {
        Rect fromRect1 = new Rect(), toRect1 = new Rect();
        Rect fromRect2 = new Rect(), toRect2 = new Rect();

        for (Background bg : manager.getBackgrounds()) {

            if (bg.getZ() < start && bg.getZ() > stop) {
                if (!viewport.clipObject(-1, bg.getY(), 100, bg.getHeight())) {
                    int startY = (int) (viewport.getYCenter() - (viewport.getViewportWorldCenterY() - bg.getY()) * viewport.getPixelsPerMetreY());
                    int endY = (int) (viewport.getYCenter() - (viewport.getViewportWorldCenterY() - bg.getEndY()) * viewport.getPixelsPerMetreY());

                    fromRect1 = new Rect(0, 0, bg.getWidth() - bg.getXClip(), bg.getHeight());
                    toRect1 = new Rect(bg.getXClip(), startY, bg.getWidth(), endY);
                    fromRect2 = new Rect(bg.getWidth() - bg.getXClip(), 0, bg.getWidth(), bg.getHeight());
                    toRect2 = new Rect(0, startY, bg.getXClip(), endY);
                }

                // draw backgrounds
                if (!bg.isReversedFirst()) {
                    canvas.drawBitmap(bg.getBitmap(), fromRect1, toRect1, paint);
                    canvas.drawBitmap(bg.getBitmapReversed(), fromRect2, toRect2, paint);
                } else {
                    canvas.drawBitmap(bg.getBitmap(), fromRect2, toRect2, paint);
                    canvas.drawBitmap(bg.getBitmapReversed(), fromRect1, toRect1, paint);
                }

                // calculate the next value for the background's clipping position by modifying xClip
                // and switching which background is drawn first, if necessary.
                bg.setXClip(bg.getXClip() - (int) (manager.getPlayer().getxVelocity() / (20 / bg.getSpeed())));
                if (bg.getXClip() >= bg.getWidth()) {
                    bg.setXClip(0);
                    bg.setReversedFirst(!bg.isReversedFirst());
                } else if (bg.getXClip() <= 0) {
                    bg.setXClip(bg.getWidth());
                    bg.setReversedFirst(!bg.isReversedFirst());
                }
            }
        }
    }
}
