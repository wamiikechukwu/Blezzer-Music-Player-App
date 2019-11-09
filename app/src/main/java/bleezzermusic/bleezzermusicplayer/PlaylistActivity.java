package bleezzermusic.bleezzermusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import bleezzermusic.bleezzermusicplayer.adapter.PlaylistRecyclerviewAdapter;
import bleezzermusic.bleezzermusicplayer.dialogs.AddPlayListDialogFragment;
import bleezzermusic.bleezzermusicplayer.loader.PlaylistLoader;
import bleezzermusic.bleezzermusicplayer.models.Playlist;

public class PlaylistActivity extends AppCompatActivity implements AddPlayListDialogFragment.PlaylistListener {


    Button createPlaylist;
    PlaylistRecyclerviewAdapter playlistRecyclerviewAdapter;
    RecyclerView playListRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playListRecyclerview=findViewById(R.id.playlist_recyclerview);
        playListRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Playlist> allPlaylists = PlaylistLoader.getAllPlaylists(this);
        playlistRecyclerviewAdapter=new PlaylistRecyclerviewAdapter(this,allPlaylists );
        playListRecyclerview.setAdapter(playlistRecyclerviewAdapter);

        createPlaylist=findViewById(R.id.create_playlist);
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlayListDialogFragment addPlayListDialogFragment = AddPlayListDialogFragment.create();
                addPlayListDialogFragment.show(getSupportFragmentManager(),"CREATE_PLAYLIST");
                addPlayListDialogFragment.addListener(PlaylistActivity.this);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("TAG", "onActivityResult: DATA");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPlaylistCreated() {
        ArrayList<Playlist> allPlaylists = PlaylistLoader.getAllPlaylists(this);
        playlistRecyclerviewAdapter.setPlaylists(allPlaylists);
    }
}
