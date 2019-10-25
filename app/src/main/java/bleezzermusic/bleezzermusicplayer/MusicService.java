package bleezzermusic.bleezzermusicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private ArrayList<songsQuery> songsQueries;
    private int songPosition;

    //MUSIC INFORMATION
    String songTitle;

    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();

        initializeMusicPlayer();
    }

    public void setList(ArrayList<songsQuery> thesongs) {
        songsQueries = thesongs;
    }

    public void initializeMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void playSong() {
        mediaPlayer.reset();

        //CREATED AN INSTANCE OF THE DATA MODEL CLASS
        songsQuery songsQuery = songsQueries.get(songPosition);

        //GET THE SONG TITLE FROM THE DATA MODEL
        songTitle = songsQuery.getDisplayName();

        long currentSong = songsQuery.getId();
        Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), songUri);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error setting data source", Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.prepareAsync();
    }

    public void setSong(int songIdex) {
        songPosition = songIdex;
    }

    //TODO i will add a resuffle method into this method
    public void playNextMusic() {
        songPosition++;
        if (songPosition >= songsQueries.size())
            songPosition = 0;
        playSong();
    }

    public void playPreviousMusic() {
        songPosition--;
        if (songPosition < 0)
        songPosition = songsQueries.size() - 1;
        playSong();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //TODO we need to make sure the grater sign is the right once from the website
        if (mediaPlayer.getCurrentPosition() >= 0) {
            mp.reset();
            playNextMusic();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_play).setTicker(songTitle).setOngoing(true).setContentTitle("Playing").setContentText(songTitle);

        Notification notification = builder.build();
        startForeground(1, notification);
        
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public int getSongPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getSongDuration() {
        return mediaPlayer.getDuration();
    }

    public boolean isSongPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pauseSong() {
        mediaPlayer.pause();
    }

    public void seek(int position) {
        mediaPlayer.seekTo(position);
    }

    public void go() {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}
