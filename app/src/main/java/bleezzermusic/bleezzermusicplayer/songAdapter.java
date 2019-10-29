package bleezzermusic.bleezzermusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class songAdapter extends BaseAdapter {

    private ArrayList<songsQuery> songs;
    private LayoutInflater layoutInflater;

    public songAdapter(Context context, ArrayList<songsQuery> songs) {
        this.songs = songs;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //GET THE LAYOUT THAT WILL BE USED IN FORMING THE SONG LAYOUT
        RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.music_layout, parent, false);

        //THEN GET THE INSTANCE OF THE TWO VIEWS; THAT IS THE SONG NAME AND SONG ARTIST
        TextView songName = relativeLayout.findViewById(R.id.song_name);
        TextView songArtist = relativeLayout.findViewById(R.id.song_artist);

        // GET THE SONG POSITION, SO THAT WE CAN KNOW WHICH OF THE  SONG WAS CLICKED
        songsQuery getCurrentSongs = songs.get(position);

        //SET THE SONGS INTO THE TEXT VIEW, USING SELECTED MATERIAL
        songName.setText(getCurrentSongs.getDisplayName());
        songArtist.setText(getCurrentSongs.getArtist());

        relativeLayout.setTag(position);


        return relativeLayout;
    }
}
