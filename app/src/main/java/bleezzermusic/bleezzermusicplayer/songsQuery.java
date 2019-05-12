package bleezzermusic.bleezzermusicplayer;

import java.io.Serializable;
import java.util.LinkedList;

public class songsQuery implements Serializable {

    private final LinkedList<songsQuery> songList = new LinkedList<>();
    private long id;
    private String artist;
    private String title;
    private String genre;
    private String album;

    public songsQuery(long id, String artist, String title, String genre, String album) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
