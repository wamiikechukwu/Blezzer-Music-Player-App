package bleezzermusic.bleezzermusicplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

public class songAdapter extends RecyclerView.Adapter<songAdapter.viewHolder> {

    private ArrayList<DataModel> mDataModel;

    public songAdapter(ArrayList<DataModel> mDataModel) {
        this.mDataModel = mDataModel;
    }

    @NonNull
    @Override
    public songAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull songAdapter.viewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
