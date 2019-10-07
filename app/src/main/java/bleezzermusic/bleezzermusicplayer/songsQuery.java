package bleezzermusic.bleezzermusicplayer;

/**
 * This is the datamodel class
 **/

public class songsQuery {

    private long id;
    private String artist;
    private String title;
    private String album;

    songsQuery(long id, String artist, String title, String album) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.album = album;
    }

    long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
