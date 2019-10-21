package bleezzermusic.bleezzermusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    //TOTAL SECONDS TO DISPLAY THE SPLASH SCREEN
    int secondsDelayed = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //HIDE THE ACTION BAR FOR THE SPLASH SCREEN
        getSupportActionBar().hide();

        //HIDE THE STATUS BAR IN THE PHONE
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        //OPEN ANOTHER THREAD
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //SHPW THE MAIN ACTIVITY AFTER THE SPLASH SCREEN
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);


    }
}
