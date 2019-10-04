package bleezzermusic.bleezzermusicplayer;

public class songsQuery {

    private long data;
    private String artist;
    private String title;
    private String genre;
    private String album;

    public songsQuery(long data, String artist, String title, String genre, String album) {
        this.data = data;
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        this.album = album;
    }

    //Getter and Setters are used to effectively protect the data in the variable declared above
    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
