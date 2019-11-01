package bleezzermusic.bleezzermusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public static final String TAG = "RecyclerViewAdapter";

    Context context;
    ArrayList<songsQuery> songsRecyclerViewArrayList;
    //TODO dont forget to add RecyclerViewCallbacks recyclerViewCallbacks

    public RecyclerViewAdapter(Context context, ArrayList<songsQuery> songsRecyclerViewArrayList) {
        this.context = context;
        this.songsRecyclerViewArrayList = songsRecyclerViewArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout songLay;
        TextView songView;
        TextView artistView;
        ImageView albumArt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.songLay = itemView.findViewById(R.id.music_layout);
            this.songView = itemView.findViewById(R.id.song_name);
            this.artistView = itemView.findViewById(R.id.song_artist);
            this.albumArt = itemView.findViewById(R.id.circular_image);
        }
    }
}
