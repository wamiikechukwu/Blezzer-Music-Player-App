package bleezzermusic.bleezzermusicplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bleezzermusic.bleezzermusicplayer.R;
import bleezzermusic.bleezzermusicplayer.models.Playlist;

public class PlaylistRecyclerviewAdapter extends RecyclerView.Adapter<PlaylistRecyclerviewAdapter.MyViewHolder> {
    public static final String TAG = "RecyclerViewAdapter";

    Context context;
    ArrayList<Playlist> playlists;
    RecyclerViewCallbacks recyclerViewCallbacks;

    public PlaylistRecyclerviewAdapter(Context context, ArrayList<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.playlistName.setText(playlists.get(i).name);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.playlist_listview, viewGroup, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView playlistName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.playlistName = itemView.findViewById(R.id.playlist_name);

        }
    }

    public void setPlaylists(ArrayList<Playlist>playlists)
    {
        this.playlists=playlists;
        notifyDataSetChanged();
    }

    //UPDATE THIS TO EXPLAIN WHAT IT DOES
    public void setRecyclerViewCallbacks(RecyclerViewCallbacks recyclerViewCallbacks) {
        this.recyclerViewCallbacks = recyclerViewCallbacks;
    }

    //UPDATE THIS TO EXPLAIN WHAT IT DOES
    public interface RecyclerViewCallbacks {
        void songPicked(int position);
    }
}
