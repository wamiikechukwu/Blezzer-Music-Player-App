package bleezzermusic.bleezzermusicplayer;

import android.Manifest;
import android.app.SearchManager;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    //THE SONG WILL BE STORED IN THIS ARRAYLIST
    ArrayList<songsQuery> songArrayList;
    MusicService musicService;
    //CREATED A GLOBAL VARIABLE OF THE  LIST VIEW
    private ListView listView;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController musicController;

    private boolean pause = false;
    private boolean pausePlayBack = false;

    //LOG TITLE TO BE USED CHECK FOR ANY ERRORS IN LOGCAT
    public static final String LOG = "LOG";
    
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

        //GET THE ACTION BAR AND CUSTOMIZE IT
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setElevation(30);

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

        setMusicController();
    }

    //INFLATING THE MENU INTO THE ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //ADDING THE SEARCH VIEW
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        //ASSOCIATE THE SEARCHABLE CONFIGURATION WITH THE SEARCHVIEW
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
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
        if (pausePlayBack) {
            setMusicController();
            pausePlayBack = false;
        }
        musicController.show(0);
    }

    private void setMusicController() {
        musicController = new MusicController(this);

        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextMusic();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousMusic();
            }
        });

        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.song_list));
        musicController.setEnabled(true);
    }

    public void playNextMusic() {
        MusicService MS = new MusicService();
        MS.playNextMusic();
        if (pausePlayBack) {
            setMusicController();
            pausePlayBack = false;
        }
        musicController.show();
    }

    public void playPreviousMusic() {
        MusicService MS = new MusicService();
        MS.playPreviousMusic();
        if (pausePlayBack) {
            pausePlayBack = false;
        }
        musicController.show();
    }


    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        pausePlayBack = true;
        musicService.pauseSong();
    }

    @Override
    public int getDuration() {
        if (musicService != null && musicBound && musicService.isSongPlaying())
        return musicService.getSongDuration();
        else
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService != null && musicBound && musicService.isSongPlaying())
        return musicService.getSongPosition();
        else
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null && musicBound)
        return musicService.isSongPlaying();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause) {
            setMusicController();
            pause = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicController.hide();
    }
}
