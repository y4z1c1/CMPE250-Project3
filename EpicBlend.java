
// EpicBlend class
/**
 * Represents the EpicBlend functionality in a music application.
 * This class manages the creation and updating of playlists based on various song categories
 * and handles the addition and removal of songs from these playlists.
 * It uses AVL Trees and Max Heaps for efficient data management and retrieval.
 * @author Yusuf Anil Yazici
 */

import java.util.ArrayList;
import java.util.HashSet;

public class EpicBlend {
    public static Playlist[] playlists; // Static array to store all playlists.

    // Category limits for playlist management.
    private int playlistCategoryLimit;
    private int heartacheLimit;
    private int roadtripLimit;
    private int blissfulLimit;

    // AVL Trees for categorizing songs based on different emotions.
    public AVLTree heartacheSongs;
    public AVLTree roadtripSongs;
    public AVLTree blissfulSongs;

    // Lists to track added and removed songs during operations.
    private ArrayList<Integer> added;
    private ArrayList<Integer> removed;

    // AVL Trees for song count management in each playlist by category.
    private AVLTree[] heartachePlaylistSongCount;
    private AVLTree[] roadtripPlaylistSongCount;
    private AVLTree[] blissfulPlaylistSongCount;

    // AVL Trees to manage playlists which are not full for each category.
    private PlaylistAVLTree heartacheNotFullPlaylists;
    private PlaylistAVLTree roadtripNotFullPlaylists;
    private PlaylistAVLTree blissfulNotFullPlaylists;

    // Constructor to initialize the EpicBlend with category limits and playlist
    // number.
    public EpicBlend(int playlistCategoryLimit, int heartacheLimit, int roadtripLimit, int blissfulLimit,
            int playlistNumber) {
        this.playlistCategoryLimit = playlistCategoryLimit;
        this.heartacheLimit = heartacheLimit;
        this.roadtripLimit = roadtripLimit;
        this.blissfulLimit = blissfulLimit;
        this.added = new ArrayList<>();
        this.removed = new ArrayList<>();

        // Initialize AVL Trees for each song category and playlist song count.
        this.heartacheSongs = new AVLTree(1);
        this.roadtripSongs = new AVLTree(2);
        this.blissfulSongs = new AVLTree(3);

        heartachePlaylistSongCount = new AVLTree[playlistNumber];
        roadtripPlaylistSongCount = new AVLTree[playlistNumber];
        blissfulPlaylistSongCount = new AVLTree[playlistNumber];

        // Initialize AVL Trees for managing not full playlists in each category.
        heartacheNotFullPlaylists = new PlaylistAVLTree(1);
        roadtripNotFullPlaylists = new PlaylistAVLTree(2);
        blissfulNotFullPlaylists = new PlaylistAVLTree(3);
    }

    private PlaylistAVLTree getPlaylistTreeByCategory(String category) {
        switch (category) {
            case "Heartache":
                return heartacheNotFullPlaylists;
            case "Roadtrip":
                return roadtripNotFullPlaylists;
            case "Blissful":
                return blissfulNotFullPlaylists;
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }

    private PlaylistAVLTree getPlaylistTreeByCategory(int category) {
        switch (category) {
            case 1:
                return heartacheNotFullPlaylists;
            case 2:
                return roadtripNotFullPlaylists;
            case 3:
                return blissfulNotFullPlaylists;
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }

    private int getCategoryNumber(String category) {
        switch (category) {
            case "Heartache":
                return 1;
            case "Roadtrip":
                return 2;
            case "Blissful":
                return 3;
            default:
                throw new IllegalArgumentException("Invalid category");
        }
    }

    private void resetEpicBlend() {
        heartacheSongs.clear();
        roadtripSongs.clear();
        blissfulSongs.clear();
    }

    // Method to create playlists based on categories.
    public void createPlaylists(Playlist[] playlists) {
        resetEpicBlend();

        updateCategory(playlists, heartacheSongs, heartacheLimit, "Heartache", heartachePlaylistSongCount);
        updateCategory(playlists, roadtripSongs, roadtripLimit, "Roadtrip", roadtripPlaylistSongCount);
        updateCategory(playlists, blissfulSongs, blissfulLimit, "Blissful", blissfulPlaylistSongCount);

    }

    // Method to update the category of playlists based on the songs and limits.
    private void updateCategory(Playlist[] playlists, AVLTree targetList, int totalLimit, String category,
            AVLTree[] playlistSongCountMap) {
        ArrayList<Song> candidateSongs = new ArrayList<>();
        int categoryNumber = getCategoryNumber(category);

        for (Playlist playlist : playlists) {
            int addedFromPlaylist = 0;
            while (addedFromPlaylist < playlistCategoryLimit && !playlist.isCategoryEmpty(categoryNumber)) {
                Song candidateSong = playlist.peekSongInCategory(category);
                if (candidateSong != null) {
                    candidateSongs.add(candidateSong);
                    playlist.removeTopSongInCategory(categoryNumber); // Temporarily remove
                    addedFromPlaylist++;
                }
            }

            playlistSongCountMap[playlist.getPlaylistId() - 1] = new AVLTree(categoryNumber);
            getPlaylistTreeByCategory(category).insert(playlist);
            // Update the PlaylistAVLTrees

        }

        MaxHeap heap = new MaxHeap(categoryNumber);
        heap.buildHeap((ArrayList<Song>) candidateSongs);

        while (!heap.isEmpty() && targetList.size < totalLimit) {
            Song songToAdd = heap.delete();
            targetList.insert(songToAdd);
            playlistSongCountMap[songToAdd.getPlaylistId() - 1].insert(songToAdd);
            if (playlistSongCountMap[songToAdd.getPlaylistId() - 1].size == playlistCategoryLimit) {

            }
            Playlist playlist = playlists[songToAdd.getPlaylistId() - 1];
            playlist.deleteAvl(songToAdd, categoryNumber);
            if (playlistSongCountMap[playlist.getPlaylistId() - 1].size == playlistCategoryLimit) {
                getPlaylistTreeByCategory(category).delete(playlist);
            }
            ;
        }

        // Restore the removed songs back to playlists
        for (Playlist playlist : playlists) {
            playlist.restoreRemovedSongs(categoryNumber);
        }
    }

    // Adds a new song to playlists and updates them accordingly.
    public void addSongAndUpdatePlaylists(Song newSong, Playlist playlist) {
        this.added.clear();

        Song removedFromHeartache = addSongToCategoryIfEligible(newSong, playlist, heartacheSongs, heartacheLimit, 1,
                heartachePlaylistSongCount);
        Song removedFromRoadtrip = addSongToCategoryIfEligible(newSong, playlist, roadtripSongs, roadtripLimit, 2,
                roadtripPlaylistSongCount);
        Song removedFromBlissful = addSongToCategoryIfEligible(newSong, playlist, blissfulSongs, blissfulLimit, 3,
                blissfulPlaylistSongCount);

        printAdditionResults(newSong, removedFromHeartache,
                removedFromRoadtrip,
                removedFromBlissful);

    }

    // Adds a song to a category if it is eligible.
    private Song addSongToCategoryIfEligible(Song song, Playlist playlist, AVLTree categoryAVL, int limit,

            int categoryNumber, AVLTree[] playlistSongCountMap) {

        int playlistId = playlist.getPlaylistId();
        if (categoryAVL.size < limit || song.compare(categoryAVL.getMinSong(), categoryNumber) > 0) {

            if (playlistSongCountMap[playlistId - 1].size >= playlistCategoryLimit) {

                Song smallestSong = playlistSongCountMap[playlistId - 1].getMinSong();

                if (song.compare(smallestSong, categoryNumber) < 0) {

                    return null;
                }

                categoryAVL.insert(song);
                playlistSongCountMap[playlistId - 1].insert(song);
                playlist.deleteAvl(song, categoryNumber);

                this.added.add(categoryNumber);
                categoryAVL.delete(smallestSong);
                if (playlistSongCountMap[playlistId - 1] != null) {
                    playlistSongCountMap[playlistId - 1].delete(smallestSong);

                }
                playlist.insertAvl(smallestSong, categoryNumber);

                return smallestSong;
            }
            categoryAVL.insert(song);
            playlistSongCountMap[playlistId - 1].insert(song);
            if (playlistSongCountMap[playlist.getPlaylistId() - 1].size == playlistCategoryLimit) {
                getPlaylistTreeByCategory(categoryNumber).delete(playlist);
            }
            playlist.deleteAvl(song, categoryNumber);
            this.added.add(categoryNumber);

            if (categoryAVL.size > limit) {

                Song deleted = categoryAVL.getMinSong();
                int deletedPID = deleted.getPlaylistId();
                categoryAVL.delete(deleted);
                playlistSongCountMap[deletedPID - 1].delete(deleted);
                Playlist deletedPlaylist = playlists[deletedPID - 1];
                deletedPlaylist.insertAvl(deleted, categoryNumber);
                if (playlistSongCountMap[deletedPID - 1].size < playlistCategoryLimit) {
                    getPlaylistTreeByCategory(categoryNumber).insert(deletedPlaylist);
                }

                return deleted;
            }
            return null;
        }
        return null;
    }

    // Prints the results of song addition.
    private void printAdditionResults(Song newSong, Song removedFromHeartache,
            Song removedFromRoadtrip,
            Song removedFromBlissful) {

        String addedOutput = "0 0 0";
        String removedOutput = "0 0 0";

        if (added.size() > 0) {
            for (int element : added) {
                addedOutput = replaceCategoryInOutput(addedOutput, newSong.getSongId(), element - 1);
            }

            removedOutput = replaceCategoryInOutput(removedOutput,
                    removedFromHeartache != null ? removedFromHeartache.getSongId() : 0, 0);

            removedOutput = replaceCategoryInOutput(removedOutput,
                    removedFromRoadtrip != null ? removedFromRoadtrip.getSongId() : 0, 1);
            removedOutput = replaceCategoryInOutput(removedOutput,
                    removedFromBlissful != null ? removedFromBlissful.getSongId() : 0, 2);

        }

        FileWrite.writeToFile(addedOutput);
        FileWrite.writeToFile(removedOutput);

    }

    // Removes a song from the playlists and updates them.
    public void removeSongAndUpdatePlaylists(Song songToRemove, Playlist playlist) {

        // Clear the 'added' list to track which categories are affected by the removal
        this.removed.clear();

        // Remove song from each category if it's present
        Song addedToHeartache = removeSongFromCategoryIfPresent(songToRemove, playlist, heartacheSongs, heartacheLimit,
                1,
                heartachePlaylistSongCount);
        Song addedToRoadtrip = removeSongFromCategoryIfPresent(songToRemove, playlist, roadtripSongs, roadtripLimit, 2,
                roadtripPlaylistSongCount);
        Song addedToBlissful = removeSongFromCategoryIfPresent(songToRemove, playlist, blissfulSongs, blissfulLimit, 3,
                blissfulPlaylistSongCount);

        // Print the results of the removal
        printRemovalResults(songToRemove, addedToHeartache, addedToRoadtrip, addedToBlissful);

    }

    // Removes a song from a category if it is present.
    private Song removeSongFromCategoryIfPresent(Song song, Playlist playlist, AVLTree categoryAVL, int limit,
            int categoryNumber, AVLTree[] playlistSongCountMap) {
        int playlistId = playlist.getPlaylistId();

        playlist.removeSong(song);

        if (categoryAVL.contains(song)) {
            // Remove the song from the category AVL
            categoryAVL.delete(song);

            playlistSongCountMap[playlistId - 1].delete(song);
            if (playlistSongCountMap[playlistId - 1].size < playlistCategoryLimit) {
                getPlaylistTreeByCategory(categoryNumber).insert(playlist);
            }

            this.removed.add(categoryNumber);

            // Find a replacement song from the playlist's AVL tree if necessary
            if (categoryAVL.size < limit) {

                Song replacementSong = getPlaylistTreeByCategory(categoryNumber).findMaxSongByMaxAvl();

                if (replacementSong != null) {

                    categoryAVL.insert(replacementSong);
                    playlistSongCountMap[replacementSong.getPlaylistId() - 1].insert(replacementSong);
                    Playlist replacementPlaylist = playlists[replacementSong.getPlaylistId() - 1];
                    replacementPlaylist.deleteAvl(replacementSong,
                            categoryNumber);

                    if (playlistSongCountMap[replacementSong.getPlaylistId() - 1].size == playlistCategoryLimit) {
                        getPlaylistTreeByCategory(categoryNumber).delete(replacementPlaylist);
                    }

                    return replacementSong;
                }

                playlist.removeSong(song);
            }
        }
        return null;
    }

    // Prints the results of song removal.
    private void printRemovalResults(Song removedSong, Song addedToHeartache, Song addedToRoadtrip,
            Song addedToBlissful) {
        String removedOutput = "0 0 0";
        String addedOutput = "0 0 0";

        if (removed.size() > 0) {
            for (int element : removed) {
                removedOutput = replaceCategoryInOutput(removedOutput, removedSong.getSongId(), element - 1);
            }

            addedOutput = replaceCategoryInOutput(addedOutput,
                    addedToHeartache != null ? addedToHeartache.getSongId() : 0, 0);

            addedOutput = replaceCategoryInOutput(addedOutput,
                    addedToRoadtrip != null ? addedToRoadtrip.getSongId() : 0, 1);
            addedOutput = replaceCategoryInOutput(addedOutput,
                    addedToBlissful != null ? addedToBlissful.getSongId() : 0, 2);

        }

        FileWrite.writeToFile(addedOutput);
        FileWrite.writeToFile(removedOutput);

    }

    // Replaces a category in the output with a song ID.
    private String replaceCategoryInOutput(String output, int songId, int categoryIndex) {
        String[] parts = output.split(" ");
        parts[categoryIndex] = Integer.toString(songId);
        return String.join(" ", parts);
    }

    // Prints the current state of the EpicBlend.
    public void printEpicBlend() {
        // Get sorted lists of unique songs from each category
        ArrayList<Song> heartacheList = heartacheSongs.getItems();
        ArrayList<Song> roadtripList = roadtripSongs.getItems();
        ArrayList<Song> blissfulList = blissfulSongs.getItems();

        // Combine and sort all unique songs
        ArrayList<Song> combinedList = mergeSortedLists(heartacheList, roadtripList, blissfulList);

        // Print the combined list
        StringBuilder output = new StringBuilder();
        for (Song song : combinedList) {
            output.append(song.getSongId()).append(" ");
        }

        if (output.length() > 0) {
            String outputStr = output.toString().trim();
            FileWrite.writeToFile(outputStr);
        }

    }

    // Merges sorted lists of songs into a combined sorted list.
    private ArrayList<Song> mergeSortedLists(ArrayList<Song>... lists) {
        // Maxheap to merge sorted lists
        MaxHeap heap = new MaxHeap(0); // Compare based on playCount

        // Add all songs to the priority queue
        for (ArrayList<Song> list : lists) {
            for (Song song : list) {
                heap.add(song);
            }
        }

        // Extract songs from the queue to get them in sorted order
        ArrayList<Song> mergedList = new ArrayList<>();
        HashSet<Integer> addedSongIds = new HashSet<>();
        while (!heap.isEmpty()) {
            Song song = heap.delete();
            if (addedSongIds.add(song.getSongId())) {
                mergedList.add(song);
            }
        }

        return mergedList;
    }

}