package bleezzermusic.bleezzermusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //THE SONG WILL BE STORED IN AN ARRAYLIST
    ArrayList<songsQuery> songArrayList;

    //CREATED A GLOBAL VARIABLE OF THE  LISTVIEW
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GETTING THE INSTANCE OF THE LISTVIEW ID
        listView = findViewById(R.id.song_list);

        //INSTANTIATE THE  ARRAYLIST
        songArrayList = new ArrayList<songsQuery>();
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
