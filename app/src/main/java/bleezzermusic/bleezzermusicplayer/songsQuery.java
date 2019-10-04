package bleezzermusic.bleezzermusicplayer;

public class songsQuery {

    private long id;
    private String artist;
    private String title;
    private String album;

    public songsQuery(long id, String artist, String title, String album) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.album = album;
    }

    //Getter and Setters are used to effectively protect the id in the variable declared above
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
