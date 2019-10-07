package bleezzermusic.bleezzermusicplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    //THE SONG WILL BE STORED IN THIS ARRAYLIST
    ArrayList<songsQuery> songArrayList;
    MusicService musicService;
    //CREATED A GLOBAL VARIABLE OF THE  LIST VIEW
    private ListView listView;
    private Intent playIntent;
    private boolean musicBound = false;
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();

            //TODO if there is problem, check this list

            musicService.setList(songArrayList);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //musicService.setList(songArrayList);
            musicBound = false;
        }
    };

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

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
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

    public void songSelected(View view) {
        musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.playSong();
    }
}
