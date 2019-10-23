package bleezzermusic.bleezzermusicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public songAdapter songAdapter;
    public RecyclerView.LayoutManager mLayout;
    ArrayList<songsQuery> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
                return;
            }
        }

        mData = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_layout);
        songAdapter = new songAdapter(this, mData);
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(songAdapter);

        getSongFromDevice();

        // Collections.sort(mData, new Comparator<songsQuery>() {
        //   public int compare(songsQuery a, songsQuery b) {
        //      return a.getTitle().compareTo(b.getTitle());
        //  }
        // });
    }

    public void getSongFromDevice() {

        mData.add(new songsQuery(1, "hello", "i"));
        mData.add(new songsQuery(2, "hey", "you"));
        mData.add(new songsQuery(3, "me", "i"));
        mData.add(new songsQuery(4, "see", "him"));

    }
}
