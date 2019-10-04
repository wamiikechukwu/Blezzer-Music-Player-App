package bleezzermusic.bleezzermusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.music_layout, parent, false);
        TextView songName = linearLayout.findViewById(R.id.song_name);
        TextView songArtist = linearLayout.findViewById(R.id.song_artist);
        songsQuery getCurrentSongs = songs.get(position);
        songName.setText(getCurrentSongs.getTitle());
        songArtist.setText(getCurrentSongs.getArtist());
        linearLayout.setTag(position);
        return linearLayout;
    }
}
