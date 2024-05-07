
// Playlist class
/**
 * Represents a playlist.
 * This class manages a collection of songs, categorized into different emotional states like heartache, roadtrip, and blissful.
 * It utilizes Max Heaps and AVL Trees to efficiently manage and retrieve songs based on their categories and scores.
 * Songs can be added or removed from the playlist, and the class provides methods to handle these operations and manage the song's category placements.
 * @author Yusuf Anil Yazici
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Playlist {
    private int playlistId; // Unique identifier for the playlist.
    private HashMap<Integer, Song> songs; // HashMap to store songs, key is the song's ID.

    // MaxHeaps for managing songs based on their scores in different categories.
    private MaxHeap heartacheHeap;
    private MaxHeap roadtripHeap;
    private MaxHeap blissfulHeap;

    // Lists to track songs temporarily removed from the heaps.
    private ArrayList<Song> removedHeartacheSongs = new ArrayList<>();
    private ArrayList<Song> removedRoadtripSongs = new ArrayList<>();
    private ArrayList<Song> removedBlissfulSongs = new ArrayList<>();

    // AVL Trees for managing songs not yet added to the heaps.
    public AVLTree heartacheNotAddedSongs;
    public AVLTree roadtripNotAddedSongs;
    public AVLTree blissfulNotAddedSongs;

    // Constructor to initialize the Playlist with an ID and its data structures.
    public Playlist(int playlistId) {
        this.playlistId = playlistId;
        this.songs = new HashMap<>();
        this.heartacheHeap = new MaxHeap(1);
        this.roadtripHeap = new MaxHeap(2);
        this.blissfulHeap = new MaxHeap(3);

        // Initialize AVL Trees for each category.
        this.heartacheNotAddedSongs = new AVLTree(1);
        this.roadtripNotAddedSongs = new AVLTree(2);
        this.blissfulNotAddedSongs = new AVLTree(3);
    }

    // Getters for heaps to access the top-scoring songs
    public MaxHeap getHeartacheHeap() {
        return heartacheHeap;
    }

    public MaxHeap getRoadtripHeap() {
        return roadtripHeap;
    }

    public MaxHeap getBlissfulHeap() {
        return blissfulHeap;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    // Adds a song to the playlist and updates its categorization in heaps and AVL
    // trees.
    public void addSong(Song song, int playlistId) {
        songs.put(song.getSongId(), song); // Assuming getSongId() returns the song's unique ID

        song.setPlaylistId(playlistId);

        heartacheHeap.add(song);
        roadtripHeap.add(song);
        blissfulHeap.add(song);
        for (int i = 1; i <= 3; i++) {
            insertAvl(song, i);
        }
    }

    // Removes a song from the playlist and updates its categorization.
    public void removeSong(Song song) {
        song.setPlaylistId(-1);
        song.resetNotAdded();
        for (int i = 1; i <= 3; i++) {
            deleteAvl(song, i);
        }

        songs.remove(song.getSongId());

    }


    // Peeks at the top-scoring song in a specific category.
    public Song peekSongInCategory(String category) {
        switch (category) {
            case "Heartache":
                return heartacheHeap.isEmpty() ? null : heartacheHeap.peek();
            case "Roadtrip":
                return roadtripHeap.isEmpty() ? null : roadtripHeap.peek();
            case "Blissful":
                return blissfulHeap.isEmpty() ? null : blissfulHeap.peek();
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }

    // Inserts a song into not added songs AVL tree based on its category.
    public void insertAvl(Song song, int category) {

        switch (category) {
            case 1:
                heartacheNotAddedSongs.insert(song);
                song.setNotAdded(category - 1, true);
                break;

            case 2:
                roadtripNotAddedSongs.insert(song);
                song.setNotAdded(category - 1, true);
                break;

            case 3:
                blissfulNotAddedSongs.insert(song);
                song.setNotAdded(category - 1, true);
                break;

            default:
                break;
        }
    }

    // Deletes a song from not added songs AVL tree based on its category.
    public void deleteAvl(Song song, int category) {

        switch (category) {
            case 1:
                heartacheNotAddedSongs.delete(song);
                song.setNotAdded(category - 1, false);
                break;

            case 2:
                roadtripNotAddedSongs.delete(song);
                song.setNotAdded(category - 1, false);
                break;

            case 3:
                blissfulNotAddedSongs.delete(song);
                song.setNotAdded(category - 1, false);
                break;

            default:
                break;
        }
    }

    // Gets the maximum scoring song from not added songs AVL tree for a specific
    // category.
    public Song getMaxAvl(int category) {
        switch (category) {
            case 1:
                return heartacheNotAddedSongs.getMaxSong();

            case 2:
                return roadtripNotAddedSongs.getMaxSong();

            case 3:
                return blissfulNotAddedSongs.getMaxSong();
        }
        return heartacheNotAddedSongs.getMaxSong();
    }

    // Gets the minimum scoring song from not added songs AVL tree for a specific
    // category.
    public Song getMinAvl(int category) {
        switch (category) {
            case 1:
                return heartacheNotAddedSongs.getMinSong();

            case 2:
                return roadtripNotAddedSongs.getMinSong();

            case 3:
                return blissfulNotAddedSongs.getMinSong();
        }
        return heartacheNotAddedSongs.getMaxSong();
    }

    // Removes the top song from a specific category and tracks it.
    public Song removeTopSongInCategory(int categoryNumber) {
        Song removedSong = null;
        switch (categoryNumber) {
            case 1:
                removedSong = heartacheHeap.delete();
                removedHeartacheSongs.add(removedSong);
                break;
            case 2:
                removedSong = roadtripHeap.delete();
                removedRoadtripSongs.add(removedSong);
                break;
            case 3:
                removedSong = blissfulHeap.delete();
                removedBlissfulSongs.add(removedSong);
                break;
        }
        return removedSong;
    }

    // Restores removed songs back to their respective heaps.
    public void restoreRemovedSongs(int categoryNumber) {
        ArrayList<Song> removedSongs = new ArrayList<>();
        MaxHeap categoryHeap = new MaxHeap(categoryNumber);
        switch (categoryNumber) {
            case 1:
                removedSongs = removedHeartacheSongs;
                categoryHeap = heartacheHeap;
                break;
            case 2:
                removedSongs = removedRoadtripSongs;
                categoryHeap = roadtripHeap;
                break;
            case 3:
                removedSongs = removedBlissfulSongs;
                categoryHeap = blissfulHeap;
                break;

        }

        for (Song song : removedSongs) {
            categoryHeap.add(song);
        }
        removedSongs.clear();
    }

    // Checks if a specific category is empty in the playlist.
    public boolean isCategoryEmpty(int categoryNumber) {
        switch (categoryNumber) {
            case 1:
                return heartacheHeap.isEmpty();
            case 2:
                return roadtripHeap.isEmpty();
            case 3:
                return blissfulHeap.isEmpty();
        }
        return false;
    }

}
