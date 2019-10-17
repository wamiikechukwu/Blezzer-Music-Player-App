package bleezzermusic.bleezzermusicplayer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements songFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
