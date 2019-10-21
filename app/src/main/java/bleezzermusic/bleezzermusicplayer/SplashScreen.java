package bleezzermusic.bleezzermusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    int secondsDelayed = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //HIDE THE TITLE BAR IN THE PHONE
        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        //HIDE THE ACTION BAR FOR THE SPLASH SCREEN
        getSupportActionBar().hide();

        //HIDE THE STATUS BAR IN THE PHONE
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);


    }
}
