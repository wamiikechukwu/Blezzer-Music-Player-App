package bleezzermusic.bleezzermusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        RelativeLayout songLay = myViewHolder.songLay;
        TextView songView = myViewHolder.songView;
        TextView artistView = myViewHolder.artistView;
        final ImageView albumArt = myViewHolder.albumArt;

        songsQuery currentSongsQuery = songsRecyclerViewArrayList.get(i);

        if (albumArt != null) {
            if (!currentSongsQuery.getImage().equalsIgnoreCase("")) {
                Picasso.get().load(currentSongsQuery.getImage()).into(albumArt, new Callback() {

                    @Override
                    public void onSuccess() {
                        if (albumArt.getDrawable() == null) {
                            Log.e(TAG, "onBindViewHolder() -> onSuccess() -> Drawable is null");
                            Picasso.get().load(R.drawable.default_picture).into(albumArt);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onBindViewHolder() -> onError() ->");
                        Picasso.get().load(R.drawable.default_picture).into(albumArt);
                    }
                });
            } else {
                Glide.with(context).load(R.drawable.default_picture).into(albumArt);
            }
            
        }
        songView.setText(currentSongsQuery.getTitle());
        artistView.setText(currentSongsQuery.getArtist());
    }

    @Override
    public int getItemCount() {
        return songsRecyclerViewArrayList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_layout, viewGroup, false);
        return new MyViewHolder(view);
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
