package edu.utep.cs.cs4381.callof2d;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import edu.utep.cs.cs4381.callof2d.views.GameView;

public class MainActivity extends AppCompatActivity {

    private GameView platformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);
        platformView = new GameView(this, resolution.x, resolution.y);

        setContentView(platformView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        platformView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        platformView.resume();
    }
}