package bleezzermusic.bleezzermusicplayer;

/**
 * This is the datamodel class
 **/

public class songsQuery {

    private long id;
    private String artist;
    private String displayName;
    private String album;
    /*
    private String folder;
    private String genre;
    private String image;
    */

    songsQuery(long id, String artist, String displayName, String album /*String folder, String genre, String image*/) {
        this.id = id;
        this.artist = artist;
        this.displayName = displayName;
        this.album = album;
        /*
        this.folder = folder;
        this.genre = genre;
        this.image = image;
         */
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

    String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    /*
    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
     */
}
