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

    private ArrayList<DataModel> mDataModel;
    private Context context;

    public songAdapter(ArrayList<DataModel> mDataModel, Context context) {
        this.mDataModel = mDataModel;
        this.context = context;
    }

    @NonNull
    @Override
    public songAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_layout,
                viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull songAdapter.viewHolder viewHolder, int i) {
        DataModel dataModel = mDataModel.get(i);
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public ImageView songImageView;
        public TextView songTitleText;
        public TextView songArtistText;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            songImageView = itemView.findViewById(R.id.song_image);
            songTitleText = itemView.findViewById(R.id.song_title);
            songArtistText = itemView.findViewById(R.id.song_artist);
        }
    }
}
