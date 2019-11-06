package bleezzermusic.bleezzermusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    //I DO NOT KNOW THAT THIS ID DOES
    private static final int NOTIFY_ID = 1;

    //TO HAVE ACCESS TO THE AUDIO VOLUME
    private static AudioManager audioManager;

    //USE THIS CLASS NAME TO DEBUG THE APP (THAT IS, TO CHECK FOR ERRORS)
    public final String TAG = getClass().getSimpleName();
    // SAVE INSTANCES IN THE APP
    private final String TAG_SONG_ID = "SONG_ID";
    private final String TAG_POSITION = "POSITION";
    private final String TAG_PAUSE_STATE = "PAUSE_STATE";
    private final String CURRENT_REWIND = "CURRENT_REWIND";
    //ARRAY LIST FOR THE RECYCLER VIEW
    ArrayList<songsQuery> songArrayList;
    //USE TO DISPLAY THE TAB HOST

    //COORDINATE LAYOUT
    CoordinatorLayout coordinator_layout;
    //FIGURE OUT WHAT THIS DOES
    private Notification notification;
    //USE SHOW THE CURRENT SONG BEING PLAYED ON THE NOTIFICATION BAR
    private NotificationManager notificationManager;
    // FOR BOTTOM SHEET
    private ImageView bottom_sheet_album_art;
    private TextView time_stamp_current;
    private TextView time_stamp_duration;
    private TextView bottom_sheet_song_title;
    private TextView bottom_sheet_song_artist;
    private ImageButton bottom_sheet_previous;
    private ImageButton bottom_sheet_rewind_back;
    private ImageButton bottom_sheet_play_stop;
    private ImageButton bottom_sheet_rewind_forward;
    private ImageButton bottom_sheet_next;
    private ImageButton bottom_sheet_control_shuffle;
    private ImageButton bottom_sheet_control_repeat;
    private ImageButton bottom_sheet_close;
    private AppCompatSeekBar seek_bar;
    private ImageButton bottom_sheet_control_volume;
    //FOR BOTTOM BAR
    private RelativeLayout bottom_bar_container;
    private RelativeLayout bottom_bar_open;
    private FrameLayout bottom_bar_reveal;
    private RecyclerViewAdapter songAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView bottom_bar_album_art;
    private ProgressBar song_progress;
    private TextView bottom_bar_song_title;
    private TextView bottom_bar_song_artist;
    private ImageButton bottom_bar_play_stop;
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
    //BUTTOM SHEET VIEW
    private View bottomSheetFrame;
    // I DO NOT KNOW WHAT THIS DOES
    private Timer timer = new Timer();
    private TimerTask updateTask;
    //RECYCLER VIEW PARAMETERS
    private RecyclerView recyclerView;
    private boolean paused = true;
    private boolean repeat = false;
    private boolean muteVolume = false;
    //INSTANCE VARIABLE FOR THE NAVIGATION DRAWER
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    //I THOUGHT I WOULD NEVER USE THIS BUT I ENDED UP USING IT, THIS IS USE TO DESCRIBE HOW THE
    //BOTTOM SHEET WOULD APPEARS WHEN A USER CLICK ON THE BOTTOM BAR
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANTIATE
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initLayout();

        songPosition = 0;
        random = new Random(getRandomSeed());

        initBottomSheet();
        setUpRecyclerView();
        initMusicPlayer();
        checkMute();


        if (savedInstanceState != null) {
            setRewindLength(savedInstanceState.getInt(CURRENT_REWIND, 10));

            setSongPosition(savedInstanceState.getInt(TAG_SONG_ID, 0));
            playSong();
            seek(savedInstanceState.getInt(TAG_POSITION, 0));

            bottom_bar_reveal.setVisibility(View.VISIBLE);
            bottom_bar_container.setVisibility(View.VISIBLE);

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

    //STILL DONT REALLY KNOW WHAT THIS DOES
    private long getRandomSeed() {
        return System.currentTimeMillis();
    }

    private void initLayout() {
        // BOTTOM BAR
        //COORDINATE LAYOUT
        coordinator_layout = findViewById(R.id.coordinator_layout);
        //FRAME LAYOUT
        bottom_bar_reveal = findViewById(R.id.bottom_bar_reveal);
        //RELATIVE LAYOUT FOR THE BOTTOM SHEET
        bottom_bar_container = findViewById(R.id.bottom_bar_container);
        //IMAGE VIEW FOR THE ALBUM ART
        bottom_bar_album_art = findViewById(R.id.bottom_bar_album_art);
        //PROGRESS BAR TO SHOW THE SONG SEEK BAR
        song_progress = findViewById(R.id.song_progress);
        //SONG TITLE FOR THE BOTTOM BAR
        bottom_bar_song_title = findViewById(R.id.bottom_bar_song_title);
        //SONG ARTIST FOR THE BOTTOM BAR
        bottom_bar_song_artist = findViewById(R.id.bottom_bar_song_artist);
        //THE RELATIVE LAYOUT TO OPEN THE BOTTOM SHEET FROM THE BOTTOM BAR
        bottom_bar_open = findViewById(R.id.bottom_bar_open);
        bottom_bar_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // I DONT REALLY KNOW THAT THIS DOES, I BELIEVE FROM THE DESCRIPTION, IT MEANS TO SHOW WHEN
        // AN ITEM (SONG) IS CLICKED
        bottom_bar_song_title.setSelected(true);
        bottom_bar_song_artist.setSelected(true);

        bottom_bar_play_stop = findViewById(R.id.bottom_bar_play_stop);
        bottom_bar_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused) {
                    setPauseState(false);
                } else {
                    setPauseState(true);
                }
            }
        });

        //BOTTOM SHEET
        //ALBUM COVER ART IMAGE
        bottom_sheet_album_art = findViewById(R.id.bottom_sheet_album_art);
        //SONG TITLE
        bottom_sheet_song_title = findViewById(R.id.bottom_sheet_song_title);
        //SONG ARTIST
        bottom_sheet_song_artist = findViewById(R.id.bottom_sheet_song_artist);
        //SONG TIME DURATION
        time_stamp_duration = findViewById(R.id.time_stamp_duration);
        //CURRENT SONG TIME
        time_stamp_current = findViewById(R.id.time_stamp_current);
        //SEEK TIME BAR
        seek_bar = findViewById(R.id.seek_bar);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seek(progress);
                    Log.d(TAG, "onProgressChanged() -> User changed progress" +
                            "\nProgress: " + progress
                    );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //IMAGE BOTTOM TO CLOSE THE BOTTOM SHEET
        bottom_sheet_close = findViewById(R.id.bottom_sheet_close);
        bottom_sheet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        //PREVIOUS BUTTON
        bottom_sheet_previous = findViewById(R.id.bottom_sheet_previous);
        //REWIND BACK BUTTON
        bottom_sheet_rewind_back = findViewById(R.id.bottom_sheet_rewind_back);
        //PLAY/PAUSE BUTTON
        bottom_sheet_play_stop = findViewById(R.id.bottom_sheet_play_stop);
        //REWIND FORE WARD BUTTON
        bottom_sheet_rewind_forward = findViewById(R.id.bottom_sheet_rewind_forward);
        //NEXT BUTTON
        bottom_sheet_next = findViewById(R.id.bottom_sheet_next);

        bottom_sheet_song_title.setSelected(true);
        bottom_sheet_song_artist.setSelected(true);

        //DO THIS WHEN THE PREVIOUS BUTTON IS CLICKED
        bottom_sheet_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevious();
            }
        });
        //DO THIS WHEN THE REWIND BACK BUTTON IS CLICKED
        bottom_sheet_rewind_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewindBack(rewindLenght);
            }
        });

        //DO THIS WHEN THE PLAY/PAUSE BUTTON IS CLICKED
        bottom_sheet_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paused) {
                    setPauseState(false);
                } else {
                    setPauseState(true);
                }
            }
        });

        //DO THIS WHEN THE REWIND FORWARD BUTTON IS CLICKED
        bottom_sheet_rewind_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewindForward(rewindLenght);
            }
        });

        // DO THIS WHEN THE NEXT BUTTON IS CLICKED
        bottom_sheet_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        });

        //DO THIS WHEN THE SHUFFLE BUTTON IS CLICKED
        bottom_sheet_control_shuffle = findViewById(R.id.bottom_sheet_control_shuffle);
        bottom_sheet_control_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShuffleState();
            }
        });

        //DO THIS WHEN THE REPEAT BUTTON IS CLICKED
        bottom_sheet_control_repeat = findViewById(R.id.bottom_sheet_control_repeat);
        bottom_sheet_control_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRepeatState();
            }
        });

        bottom_sheet_control_volume = findViewById(R.id.bottom_sheet_control_volume);
        bottom_sheet_control_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!muteVolume) {
                    muteAudio();
                    Glide.with(getApplicationContext()).load(R.drawable.ic_volume_off).into(bottom_sheet_control_volume);
                    muteVolume = true;
                } else {
                    Glide.with(getApplicationContext()).load(R.drawable.ic_volume_up).into(bottom_sheet_control_volume);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAG_POSITION, getCurrentPosition());
        outState.putInt(TAG_SONG_ID, getSongPosition());
        outState.putBoolean(TAG_PAUSE_STATE, paused);
        outState.putInt(CURRENT_REWIND, rewindLenght);

        super.onSaveInstanceState(outState);
    }

    private void setPauseState(boolean isPaused) {
        Log.d(TAG, "setPauseState() -> " + isPaused);
        if (isPaused) {
            paused = true;

            //FOR THE BOTTOM SHEET
            Glide
                    .with(MainActivity.this)
                    .load(R.drawable.ic_play)
                    .into(bottom_sheet_play_stop);

            //FOR THE BOTTOM BAR
            Glide
                    .with(MainActivity.this)
                    .load(R.drawable.ic_pause)
                    .into(bottom_bar_play_stop);

            makeNotification(R.drawable.ic_pause);
            pause();
        } else {
            paused = false;

            //BOTTOM SHEET
            Glide
                    .with(MainActivity.this)
                    .load(R.drawable.ic_pause)
                    .into(bottom_sheet_play_stop);
            Glide // Bottom Bar
                    .with(MainActivity.this)
                    .load(R.drawable.ic_pause)
                    .into(bottom_bar_play_stop);
            makeNotification(R.drawable.ic_play);
            start();
        }
    }

    private void initBottomSheet() {
        // Bottom Sheet Primary
        bottomSheetFrame = coordinator_layout.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetFrame);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        Log.d(TAG, "onStateChanged() -> New State: STATE_DRAGGING");

                        break;
                    }

                    case BottomSheetBehavior.STATE_SETTLING: {
                        Log.d(TAG, "onStateChanged() -> New State: STATE_SETTLING");

                        break;
                    }

                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Log.d(TAG, "onStateChanged() -> New State: STATE_EXPANDED");

                        break;
                    }

                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Log.d(TAG, "onStateChanged() -> New State: STATE_COLLAPSED");

                    }

                    case BottomSheetBehavior.STATE_HIDDEN: {
                        Log.d(TAG, "onStateChanged() -> New State: STATE_HIDDEN");

                        break;
                    }

                    default: {
                        break;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    //I DO NOT KNOW WHAT THIS DOES DOES, I HOPE TO FIND OUT SOON
    private songsQuery findSongByID(ArrayList<songsQuery> arrayList, long id) {
        for (songsQuery song : arrayList) {
            if (song.getId() == id) {
                return song;
            }
        }
        return songArrayList.get(0);
    }

    //I FINALLY LEARNT HOW THIS WORKED, WHEN THE USER CLICK THE BACK BOTTON, CHECK ID THE BOTTOM SHEET
    //IS OPEN AND IF YES, HIDE IT
    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    private void initMusicPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //START PLAYBACK : I DON'T REALLY UNDERSTAND WHAT THIS MEAN, I PLAN TO UPDATE IT LATER ON
                mp.start();

                //NOTIFICATION : I DON'T REALLY UNDERSTAND WHAT THIS MEAN, I PLAN TO UPDATE IT LATER ON
                makeNotification(R.drawable.default_picture);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                //CHECK IF PLAYBACK HAS REACHED THE END OF A TRACK
                if (repeat) {
                    playSong();
                } else {
                    if (mediaPlayer.getCurrentPosition() > 0) {
                        mp.reset();
                        playNext();
                    }
                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "Playback Error: " + extra);
                mediaPlayer.reset();
                return true;
            }
        });
    }

    private void makeNotification(int icon) {
        if (notification != null) {
            notificationManager.cancel(NOTIFY_ID);
        }

        Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);

        Notification.Builder builder = new Notification.Builder(MainActivity.this);

        builder
                .setSmallIcon(icon)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentIntent(intent)
                .setContentTitle("Playing")
                .setContentText(songTitle);

        notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NOTIFY_ID, notification);
    }

    private void setUpRecyclerView() {
        //GET THE ID OF THE RECYCLER VIEW
        recyclerView = findViewById(R.id.recycler_view);

        //INSTANTIATE THE RECYCLER VIEW
        songArrayList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        songAdapter = new RecyclerViewAdapter(this, songArrayList);

        recyclerView.setHasFixedSize(false); //TODO CAN STILL SET THIS TO TRUE, IF ANY ERROR ARISES
        recyclerView.setLayoutManager(layoutManager);

        //I DO NOT KNOW WHAT THIS DOES, UPDATE THIS TO EXPLAIN WHAT IT DOES
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(songAdapter);


        songAdapter.setRecyclerViewCallbacks(new RecyclerViewAdapter.RecyclerViewCallbacks() {
            @Override
            public void songPicked(int position) {
                songClicked(position);
            }
        });

        //GET/QUERY SONGS AND ALBUM COVER ART FROM THE USER'S DEVICE
        getSongList();

        //THIS METHOD ARRANGE THE SONGS ALPHABETICALLY BY THEIR TITLES ON THE SCREEN
        sortSongList();
    }

    //ARRANGE HOW THE SONGS WILL DISPLAY ON THE PHONE BY THE ALPHABETICAL ORDER OF THE TITLE
    private void sortSongList() {
        Collections.sort(songArrayList, new Comparator<songsQuery>() {
            public int compare(songsQuery a, songsQuery b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    //TODO THIS IS USED IN THE ACTION BAR, I CAN STILL USE THIS IN A FLOATING BAR TO SHUFFLE SONGS
    private void shuffleSongList() {
        Collections.shuffle(songArrayList, new Random(getRandomSeed()));
        songAdapter.notifyDataSetChanged();
    }

    private void playSong() {

        mediaPlayer.reset();

        songsQuery playSong = songArrayList.get(getSongPosition());

        songTitle = playSong.getTitle();
        long currSong = playSong.getId();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception ex) {
            Log.e(TAG, "Error setting data source", ex);
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException ex) {
            Log.e(TAG, "Error setting data source", ex);
        }

        startUpdateTask(playSong);
    }

    public void songClicked(int position) {
        openBottomBar();
        setSongPosition(position);
        playSong();
    }

    private void startUpdateTask(songsQuery song) {
        setPauseState(false);

        prepareUpdateTask(song);

        if (updateTask != null) {
            updateTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }

        updateTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            song_progress.setProgress(getCurrentPosition());
                            seek_bar.setProgress(getCurrentPosition());
                            time_stamp_current.setText(msToString(getCurrentPosition()));

                            Log.d(TAG, "updateTask -> Current position: " + msToString(getCurrentPosition()));
                        } catch (Exception ex) {
                            Log.e(TAG, "updateTask -> ", ex);
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(updateTask, 0, 1000);
    }

    private void stopUpdateTask() {
        if (timer != null)
            timer.cancel();
        if (updateTask != null)
            updateTask.cancel();
    }

    // I BELIEVE THIS OPEN/SHOW THE BOTTOM SHEET/BAR, THERE WAS AN ANIMATION HERE BUT I DISCARDED IT,
    //I WILL TRY RO SEE HOW I CAN ACHIEVE THIS MY SELF
    private void openBottomBar() {
        if (bottom_bar_reveal.getVisibility() == View.GONE) {
            bottom_bar_reveal.setVisibility(View.VISIBLE);

            //THIS IS USE TO MAKE THE BOTTOM BAR CONTAINER VISIBLE
            bottom_bar_container.setVisibility(View.VISIBLE);
        }
    }

    //I DO NOT KNOW THAT THIS DOES I HOPE TO RESOLVE/ UNDERSTAND THEM SOON
    private void prepareUpdateTask(songsQuery song) {
        Log.d(TAG, "prepareUpdateTask() ->" +
                "\nArtist: " + song.getArtist() +
                "\nTitle: " + song.getTitle() +
                "\nAlbum art Uri: " + song.getImage()
        );

        //I BELIEVE THIS HAS TO DO WITH THE BOTTOM BAR
        bottom_bar_album_art.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        try {
            if (song.getImage().equalsIgnoreCase("")) {
                bottom_bar_album_art.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_picture));
            } else {
                //***********************SAFE LOADING WITH PICASSO (instead of Glide)***********************//
                bottom_bar_album_art.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso
                        .get()
                        .load(song.getImage())
                        .into(bottom_bar_album_art, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (bottom_bar_album_art.getDrawable() == null) {
                                    Log.e(TAG, "prepareUpdateTask() -> onSuccess() -> Drawable is null");

                                    bottom_bar_album_art.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                    Picasso
                                            .get()
                                            .load(R.drawable.default_picture)
                                            .into(bottom_bar_album_art);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "prepareUpdateTask() -> onError() -> ");

                                bottom_bar_album_art.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                Picasso
                                        .get()
                                        .load(R.drawable.default_picture)
                                        .into(bottom_bar_album_art);
                            }
                        });
                //********************************************************************************************//
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            bottom_bar_album_art.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_picture));
        }

        song_progress.setProgress(0);
        song_progress.setMax(getDuration());

        bottom_bar_song_title.setText(song.getTitle());
        bottom_bar_song_artist.setText(song.getArtist());

        //I BELIEVE THIS WILL BE USED WHEN THE NEW ACTIVITY THAT WILL SHOW THE MUSIC PLAY BACK
        bottom_sheet_album_art.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        try {
            if (song.getImage().equalsIgnoreCase("")) {
                bottom_sheet_album_art.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_picture));

            } else {
                //***********************SAFE LOADING WITH PICASSO (instead of Glide)***********************//
                bottom_sheet_album_art.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso
                        .get()
                        .load(song.getImage())
                        .into(bottom_sheet_album_art, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (bottom_sheet_album_art.getDrawable() == null) {
                                    Log.e(TAG, "prepareUpdateTask() -> onSuccess() -> Drawable is null");

                                    bottom_sheet_album_art.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                    Picasso
                                            .get()
                                            .load(R.drawable.default_picture)
                                            .into(bottom_sheet_album_art);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "prepareUpdateTask() -> onError() -> ");

                                bottom_sheet_album_art.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                Picasso
                                        .get()
                                        .load(R.drawable.default_picture)
                                        .into(bottom_sheet_album_art);
                            }
                        });
                //********************************************************************************************/
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

            bottom_sheet_album_art.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_picture));

        }

        bottom_sheet_song_title.setText(song.getTitle());
        bottom_sheet_song_artist.setText(song.getArtist());

        time_stamp_duration.setText(msToString(getDuration()));

        seek_bar.setProgress(0);
        seek_bar.setMax(getDuration());
    }

    private void checkMute() {
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            bottom_sheet_control_volume.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_off));
        } else {
            bottom_sheet_control_volume.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_up));
        }
    }

    //QUERY THE USER'S DEVICE TO GET SONGS / ALBUM COVER ART
    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");

        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumIdColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);

            do {
                long songId = musicCursor.getLong(idColumn);
                String songTitle = musicCursor.getString(titleColumn);
                String songArtist = musicCursor.getString(artistColumn);
                long songAlbumId = musicCursor.getLong(albumIdColumn);
                String albumArt = "";

                try {
                    albumArt = (ContentUris.withAppendedId(artworkUri, songAlbumId)).toString();
                    Log.d(TAG, "TEST Album art: " + albumArt);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                songArrayList.add(new songsQuery(songId, songTitle, songArtist, albumArt));
            }
            while (musicCursor.moveToNext());
        }

        if (musicCursor != null)
            musicCursor.close();
    }

    public String msToString(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    //METHODS FOR INDIVIDUAL PLAY BACK
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    private int getSongPosition() {
        return songPosition;
    }

    //SET THE SONG
    private void setSongPosition(int songIndex) {
        songPosition = songIndex;
    }

    public void playNext() {
        if (shuffle) {
            int newSong = songPosition;
            while (newSong == songPosition) {
                newSong = random.nextInt(songArrayList.size());
            }
            songPosition = newSong;
        } else {
            songPosition++;
            if (songPosition >= songArrayList.size()) songPosition = 0;
        }

        playSong();
    }

    public void toggleShuffleState() {
        if (shuffle) {
            setShuffleOff();
        } else {
            setShuffleOn();
        }
    }

    public int getRewindLenght() {
        return rewindLenght;
    }

    public void setRewindLength(int length) {
        this.rewindLenght = length;
    }


    private void seek(int anInt) {
        mediaPlayer.seekTo(anInt);
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void rewindForward(int interval) {
        seek(getCurrentPosition() + interval * 1000);
    }

    public void rewindBack(int interval) {
        seek(getCurrentPosition() - interval * 1000);
    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void playPrevious() {
        songPosition--;
        if (songPosition < 0) songPosition = songArrayList.size() - 1;
        playSong();
    }

    private void toggleRepeatState() {
        if (repeat) {
            setRepeatOff();
        } else {
            setRepeatOn();
        }
    }

    private void setRepeatOn() {
        repeat = true;

        bottom_sheet_control_repeat.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_repeat_one));
    }

    private void setRepeatOff() {
        repeat = false;

        bottom_sheet_control_repeat.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_repeat));
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();

        stopUpdateTask();

        notificationManager.cancel(NOTIFY_ID);

        super.onDestroy();
    }


    private void setShuffleOn() {
        shuffle = true;

        bottom_sheet_control_shuffle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_shuffle));
    }

    private void setShuffleOff() {
        shuffle = false;

        bottom_sheet_control_shuffle.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_shuffle_off));
    }

    public boolean getShuffle() {
        return shuffle;
    }


    private void muteAudio() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        checkMute();
    }

}