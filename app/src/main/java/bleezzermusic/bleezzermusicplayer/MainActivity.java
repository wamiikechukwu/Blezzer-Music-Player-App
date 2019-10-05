package bleezzermusic.bleezzermusicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    //THE SONG WILL BE STORED IN THIS ARRAYLIST
    ArrayList<songsQuery> songArrayList;

    //CREATED A GLOBAL VARIABLE OF THE  LIST VIEW
    private ListView listView;

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
        //GETTING THE INSTANCE OF THE LIST VIEW ID
        listView = findViewById(R.id.song_list);

        //INSTANTIATE THE  ARRAYLIST
        songArrayList = new ArrayList<songsQuery>();

        getSongsFromDevice();

        Collections.sort(songArrayList, new Comparator<songsQuery>() {
            public int compare(songsQuery a, songsQuery b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        songAdapter songAdapter = new songAdapter(this, songArrayList);
        listView.setAdapter(songAdapter);
    }

    private void getSongsFromDevice() {

        //CREATED A CONTENT RESOLVER INSTANCE, TO RETRIEVE THE URI FOR EXTERNAL MUSIC FILES
        ContentResolver musicResolver = getContentResolver();

        //CREATED A CURSOR INSTANCE USING THE CONTENT RESOLVER INSTANCE TO QUERY THE MUSIC FILES
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                long songId = musicCursor.getLong(idColumn);
                String songTitle = musicCursor.getString(titleColumn);
                String songArtist = musicCursor.getString(artistColumn);
                String songAlbum = musicCursor.getString(albumColumn);

                songArrayList.add(new songsQuery(songId, songTitle, songArtist, songAlbum));
            }
            while (musicCursor.moveToNext());
        }
    }
}
