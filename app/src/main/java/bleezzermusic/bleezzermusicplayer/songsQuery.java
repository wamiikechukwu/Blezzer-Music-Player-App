package bleezzermusic.bleezzermusicplayer;

/**
 * This is the datamodel class
 **/

public class songsQuery {

    private long id;
    private String artist;
    private String title;
    private String image;

    songsQuery(long id, String artist, String title, String image) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.image = image;
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



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
