package bleezzermusic.bleezzermusicplayer.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



import bleezzermusic.bleezzermusicplayer.R;
import bleezzermusic.bleezzermusicplayer.models.Song;
import bleezzermusic.bleezzermusicplayer.playlist.PlaylistsUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kunal on 27-12-2017.
 */

public class AddPlayListDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.playlist_name)
    EditText playlist_name;
    @BindView(R.id.cancel_button)
    Button cancel_button;
    @BindView(R.id.add_button)
    Button add_button;


    @NonNull
    public static AddPlayListDialogFragment create(Song song) {
        AddPlayListDialogFragment dialog = new AddPlayListDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", song);
        dialog.setArguments(args);
        return dialog;
    }



    @NonNull
    public static AddPlayListDialogFragment create() {
        AddPlayListDialogFragment dialog = new AddPlayListDialogFragment();
        return dialog;
    }


    Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_playlist_dialog_fragment,container,false);
        unbinder= ButterKnife.bind(this,view);
        getDialog().setTitle("Create New PlayList");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_button:
                PlaylistsUtil.createPlaylist(getActivity(),playlist_name.getText().toString());
                playlistListener.onPlaylistCreated();
                this.dismiss();
                break;
            case R.id.cancel_button:
                this.dismiss();
                break;
        }
    }

    PlaylistListener playlistListener;

    public void addListener(PlaylistListener playlistListener)
    {
        this.playlistListener=playlistListener;
    }
    public interface PlaylistListener
    {
        void onPlaylistCreated();
    }

}
