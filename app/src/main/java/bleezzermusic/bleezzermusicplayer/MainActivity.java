package bleezzermusic.bleezzermusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<MediaStore.Audio> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        String sortOrder = MediaStore.Audio.Media.TITLE + "ASC";

        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);


        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();

            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                        .ARTIST));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                        .TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media
                        .ALBUM));

                //Saving to the songs to audio List

                audioList.add(new MediaStore.Audio(data, artist, title, album));
            }

        }

        cursor.close();
    }
}
