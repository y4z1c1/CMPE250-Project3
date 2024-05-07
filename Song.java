// Song class
/**
 * Represents a song with various attributes like song ID, name, play count, and scores in different categories.
 * This class is used to manage and compare songs based on their attributes for different functionalities in a music application.
 * It includes methods to get and set song details and to compare songs based on specified categories.
 * @author Yusuf Anil Yazici
 */
public class Song {
    private int songId; // Unique identifier for the song.
    private String songName; // Name of the song.
    private int playCount; // Number of times the song has been played.
    private int heartacheScore; // Score of the song in the heartache category.
    private int roadtripScore; // Score of the song in the roadtrip category.
    private int blissfulScore; // Score of the song in the blissful category.
    private int playlistId; // ID of the playlist to which the song belongs.
    private boolean[] notAdded = {true, true, true}; // Flags to track if the song has been added to specific categories.

    // Constructor to create a new Song instance with provided attributes.
    public Song(int songId, String songName, int playCount, int heartacheScore, int roadtripScore, int blissfulScore) {
        this.songId = songId;
        this.songName = songName;
        this.playCount = playCount;
        this.heartacheScore = heartacheScore;
        this.roadtripScore = roadtripScore;
        this.blissfulScore = blissfulScore;
    }

    // Getter methods for song attributes.
    public int getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public int getPlayCount() {
        return playCount;
    }

    public int getHeartacheScore() {
        return heartacheScore;
    }

    public int getRoadtripScore() {
        return roadtripScore;
    }

    public int getBlissfulScore() {
        return blissfulScore;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public boolean getNotAdded(int category) {
        return notAdded[category];
    }

    // Resets the 'notAdded' flags for all categories.
    public void resetNotAdded(){
        this.notAdded[0] = true;
        this.notAdded[1] = true;
        this.notAdded[2] = true;
    }

    // Sets the 'notAdded' flag for a specific category.
    public void setNotAdded(int index, boolean value) {
        this.notAdded[index] = value;
    }

    // Setter for playlistId.
    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    // Compares this song with another song based on the specified category.
    public int compare(Song other, int category) {
        int thisScore = getScoreByCategory(category);
        int otherScore = other.getScoreByCategory(category);

        // Compare scores; if equal, compare by song name.
        if (thisScore != otherScore) {
            return Integer.compare(thisScore, otherScore);
        } else {
            return -1 * this.songName.compareTo(other.songName);
        }
    }

    // Helper method to get the score for a specific category.
    private int getScoreByCategory(int category) {
        switch (category) {
            case 0: return playCount;
            case 1: return heartacheScore;
            case 2: return roadtripScore;
            case 3: return blissfulScore;
            default: throw new IllegalArgumentException("Invalid category");
        }
    }
}
