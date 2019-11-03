package bleezzermusic.bleezzermusicplayer;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final int NOTIFY_ID = 1;
    //TO HAVE ACCESS TO THE AUDIO VOLUME
    //TODO: I HAVE TO MAKE USERS BE ABLE TO MUTE THE SONG BEING PLAYED ON THE PLAYBACK SCREEN
    private static AudioManager audioManager;
    //USE THIS CLASS NAME TO DEBUG THE APP (THAT IS, TO CHECK FOR ERRORS)
    public final String LOG = getClass().getSimpleName();
    // SAVE INSTANCES IN THE APP
    private final String TAG_SONG_ID = "SONG_ID";
    private final String TAG_POSITION = "POSITION";
    private final String TAG_PAUSE_STATE = "PAUSE_STATE";
    private final String CURRENT_REWIND = "CURRENT_REWIND";
    //ARRAYLIST
    ArrayList<songsQuery> songArrayList;
    songAdapter songAdapter = new songAdapter(this, songArrayList);
    TabHost.TabSpec tabSpec;
    //POSSIBLY TO SHOW THE CURRENT SONG BEING PLAYED
    //TODO: UPDATE THE COMMENT SO TELL WHAT IT DOES, WHEN I HAVE FINALLY IMPLEMENTED IT
    private NotificationManager notificationManager;
    private Notification notification;
    //THESE IMAGE BUTTON WILL HOLD THE PLAYBACK IMAGES
    private ImageButton playStop_ImageButton;
    private ImageButton rewindForward_ImageButton;
    private ImageButton rewindBack_ImageButton;
    private ImageButton next_ImageButton;
    private ImageButton previous_ImageButton;
    private ImageButton controlShuffle_ImageButton;
    private ImageButton controlRepeat__ImageButton;

    //TODO: WILL ADD TIMER IN THE LATER VERSION (LINE 139)
    //IMAGE BUTTON FOR THE ALBUM COVER ART
    private ImageView albumCoverArt;
    //RECYCLERVIEW PARAMETERS
    private RecyclerView recyclerView;
    private RecyclerViewAdapter songAdapter;
    private RecyclerView.LayoutManager layoutManager;
    //USE TO CONTROL PLAY BACK FOR THE AUDIO FILES
    //TODO: LATER UPDATE THE COMMENT, TO PROPERLY DESCRIBE WHAT IT DOES IN THE CODE
    private MediaPlayer mediaPlayer;
    //I BELIEVE THIS IS USE TO DISPLAY RANDOM NUMBER FOR THE SHUFFLE BUTTON
    //TODO: DONT FOEGET TO PROPERLY EXPLAIN WHAT THIS DO IN THE CODE
    private Random random;
    //SONG DETAILS/VARIABLES
    private String songTitle = "";
    private int songPosition;
    private int rewindLenght = 5;
    private boolean shuffle = false;

    //INSTANCE VARIABLE FOR THE NAVIGATION DRAWER
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    //INSTANCE VARIABLE FOR THE TAB HOST
    TabHost tabHost;
    private boolean paused = true;
    private boolean repeat = false;

    {
        public int compare (songsQuery a, songsQuery b){
        return a.getTitle().compareTo(b.getTitle());
    }
        }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    songArrayList =new ArrayList<songsQuery>()

    {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int item = menuItem.getItemId();
                switch (item) {
                    case R.id.home:
                        //Toast.makeText(getApplicationContext(), "Account Click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.albums:
                        // Toast.makeText(getApplicationContext(), "Setting Click", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.songs:
                        //Toast.makeText(getApplicationContext(), "My cart Click", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return true;
                }
                return true;
            }
    }

    //ARRANGE HOW THE SONGS WILL DISPLAY ON THE PHONE BY THE ALPHABETICAL ORDER OF THE TITLE
        Collections.sort(songArrayList,new Comparator<songsQuery>()

    getSongsFromDevice();)

    setMusicController();
        listView.setAdapter(songAdapter)

    getSupportActionBar()

    // NAVIGATION DRAWER
    drawerLayout =

    findViewById(R.id.drawer_layout);

    actionBarDrawerToggle =new

    ActionBarDrawerToggle(this,drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.syncState()

    setDisplayHomeAsUpEnabled(true);.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANTIATE
        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initLayout();

        songPosition = 0;
        random = new Random(getRandomSeed());

        setUpRecyclerView();
        initMusicPlayer();

        if (savedInstanceState != null) {
            setRewindLength(savedInstanceState.getInt(CURRENT_REWIND, 10));

            setSongPosition(savedInstanceState.getInt(TAG_SONG_ID, 0));
            playSong();
            seek(savedInstanceState.getInt(TAG_POSITION, 0));

            if (savedInstanceState.getBoolean(TAG_PAUSE_STATE, false)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPauseState(true);
                    }
                }, 50);
            }
        }
    }

    navigationView =

    findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()

    private void initLayout() {
    })

    //SETTING UP THE TAB HOST IN THE MAIN ACTIVITY
        tabHost = findViewById(R.id.tab_host);
        tabHost.setup()

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAG_POSITION, getCurrentPosition());
        outState.putInt(TAG_SONG_ID, getSongPosition());
        outState.putBoolean(TAG_PAUSE_STATE, paused);
        outState.putInt(CURRENT_REWIND, rewindLenght);

        super.onSaveInstanceState(outState);
    }

        //Tab One
        tabSpec = tabHost.newTabSpec("tab one").setContent(R.id.tab_1).setIndicator("Songs");
        tabHost.addTab(tabSpec)

    //Tab Two
        tabSpec = tabHost.newTabSpec("tab two").setContent(R.id.tab_2).setIndicator("Albums");
        tabHost.addTab(tabSpec)

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
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

        // QUERY THE AUDIO FILES IN THE DEVICE
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //QUERY THE ALBUM FILES IN THE DEVICE (IN OTHER TO GET THE ALBUM ART)
        Uri albumUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        //CURSOR FOR THE AUDIO FILES
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        //CURSOR FOR THE ALBUM FILES (INORDER TO GET THE ALBUM ART)
        Cursor albumCursor = musicResolver.query(albumUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst() && albumCursor != null && albumCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumArtColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            do {
                long songId = musicCursor.getLong(idColumn);
                String songTitle = musicCursor.getString(titleColumn);
                String songArtist = musicCursor.getString(artistColumn);
                String songAlbum = musicCursor.getString(albumColumn);


                //THIS IS TO CLEAR OFF THE RECENT ALBUM ART
                String songAlbumArt = "";

                try {
                    //GET THE POSITION OF THE SONG
                    albumCursor.moveToPosition(musicCursor.getPosition());

                    //THEN REPLACE THE ALBUM ART OF SONG
                    songAlbumArt = albumCursor.getString(albumArtColumn);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                songArrayList.add(new songsQuery(songId, songTitle, songArtist, songAlbum, songAlbumArt));
            }
            while (musicCursor.moveToNext());
        }

        // CLOSE THE CURSOR TO SAVE PHONE'S RESOURCES
        if (musicCursor != null) {
            musicCursor.close();
        }

        if (albumCursor != null) {
            albumCursor.close();
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
