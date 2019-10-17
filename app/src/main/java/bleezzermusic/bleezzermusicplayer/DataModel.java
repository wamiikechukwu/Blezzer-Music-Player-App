package bleezzermusic.bleezzermusicplayer;

/**
 * This is the datamodel class
 **/

public class DataModel {

    private long id;
    private String artist;
    private String title;
    private String album;
    private String folder;
    private String genre;

    DataModel(long id, String artist, String title, String album, String folder, String genre) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.folder = folder;
        this.genre = genre;
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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
