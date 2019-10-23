package bleezzermusic.bleezzermusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class songAdapter extends RecyclerView.Adapter<songAdapter.viewHolder> {

    private ArrayList<songsQuery> songs;
    private Context context;

    public songAdapter(Context context, ArrayList<songsQuery> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public songAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(context).inflate(R.layout.music_layout,
                        viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull songAdapter.viewHolder viewHolder, int i) {
        songsQuery songsQuery = songs.get(i);

        viewHolder.song_name.setText(songsQuery.getTitle());
        viewHolder.song_artist.setText(songsQuery.getArtist());
        //Glide.with(context).load(songsQuery.***).into(viewHolder.mImageView);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView song_image;
        TextView song_name;
        TextView song_artist;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            song_image = itemView.findViewById(R.id.song_image);
            song_name = itemView.findViewById(R.id.song_name);
            song_artist = itemView.findViewById(R.id.song_artist);
        }
    }
}
