
// Project3 class
/**
 * Main class for managing music playlists.
 * This class processes song data from files, organizes songs into playlists,
 * and handles various operations like adding, removing, and querying songs.
 * It demonstrates file handling, object-oriented programming, and use of custom data structures.
 * The class reads song data and test cases from files, then performs operations based on the test cases.
 * It utilizes the EpicBlend class for managing playlists.
 * @author Yusuf Anil Yazici
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Project3 {
    public static void main(String[] args) {
        // File paths provided as command-line arguments.
        String songFilePath = args[0];
        String testCaseFilePath = args[1];
        String outputFile = args[2];


        // Initializes the output file writer with the given file path.
        FileWrite.initWriter(outputFile);

        try {
            // Reads and stores all songs from the file.
            Song[] allSongs = readSongFile(songFilePath);

            // Processes the test case file using the read songs.
            processTestCaseFile(testCaseFilePath, allSongs);

        } catch (Exception e) {
            // Print stack trace in case of an exception.
            e.printStackTrace();
        } finally {
            // Ensure the file writer is closed after processing.
            FileWrite.closeWriter();
        }
    }

    // Reads the song file and returns an array of Song objects.
    private static Song[] readSongFile(String songFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(songFilePath));
        int numberOfSongs = Integer.parseInt(reader.readLine()); // Reads the first line for the number of songs.
        Song[] allSongs = new Song[numberOfSongs]; // Initializes an array for storing songs.

        String line;
        int index = 0;
        // Reads each line and creates a Song object.
        while ((line = reader.readLine()) != null && index < numberOfSongs) {
            String[] parts = line.split(" ");
            int songId = Integer.parseInt(parts[0]);
            String songName = parts[1];
            int playCount = Integer.parseInt(parts[2]);
            int heartacheScore = Integer.parseInt(parts[3]);
            int roadtripScore = Integer.parseInt(parts[4]);
            int blissfulScore = Integer.parseInt(parts[5]);

            allSongs[index++] = new Song(songId, songName, playCount, heartacheScore, roadtripScore, blissfulScore);
        }

        reader.close();
        return allSongs;
    }

    // Processes the test case file and performs operations based on its content.
    private static void processTestCaseFile(String testCaseFilePath, Song[] allSongs) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(testCaseFilePath));
        String line;

        // Reads the first line for category limits and stores them.
        line = getNextLine(reader);
        String[] limits = line.split("\\s+");
        int playlistCategoryLimit = Integer.parseInt(limits[0]);
        int heartacheLimit = Integer.parseInt(limits[1]);
        int roadtripLimit = Integer.parseInt(limits[2]);
        int blissfulLimit = Integer.parseInt(limits[3]);

        // Reads the number of playlists and initializes EpicBlend with limits and
        // number of playlists.
        int numPlaylists = Integer.parseInt(getNextLine(reader));
        EpicBlend epicBlend = new EpicBlend(playlistCategoryLimit, heartacheLimit, roadtripLimit, blissfulLimit,
                numPlaylists);

        Playlist[] playlistsArray = new Playlist[numPlaylists]; // Creates an array for storing Playlists.

        // Reads each playlist and its songs, adding them to the playlists array.
       // Reads each playlist and its songs, adding them to the playlists array.
        for (int i = 0; i < numPlaylists; i++) {
            line = getNextLine(reader);
            String[] playlistInfo = line.split("\\s+");
            int playlistId = Integer.parseInt(playlistInfo[0]);
            int numSongs = Integer.parseInt(playlistInfo[1]);
            Playlist playlist = new Playlist(playlistId);

            line = getNextLine(reader);
            if (!line.isEmpty()) {
                String[] songIds = line.split("\\s+");
                for (int j = 0; j < numSongs; j++) {
                    int songId = Integer.parseInt(songIds[j]);
                    Song song = allSongs[songId - 1];
                    playlist.addSong(song, playlist.getPlaylistId());
                }
            }

            playlistsArray[i] = playlist;
        }
        EpicBlend.playlists = playlistsArray;

        epicBlend.createPlaylists(playlistsArray);

        // Processes each event (add, remove, ask) based on the input file.
        int numEvents = Integer.parseInt(getNextLine(reader));
        for (int i = 0; i < numEvents; i++) {
            line = getNextLine(reader);
            String[] eventParts = line.split("\\s+");
            String eventType = eventParts[0];

            // Handles adding a song to a playlist.
            if ("ADD".equals(eventType)) {
                int songId = Integer.parseInt(eventParts[1]);
                int playlistId = Integer.parseInt(eventParts[2]);
                Song song = allSongs[songId - 1];
                Playlist playlist = playlistsArray[playlistId - 1];

                playlist.addSong(song, playlistId);
                epicBlend.addSongAndUpdatePlaylists(song, playlist);
            }
            // Handles removing a song from a playlist.
            else if ("REM".equals(eventType)) {
                int songId = Integer.parseInt(eventParts[1]);
                Song song = allSongs[songId - 1];
                int playlistId = Integer.parseInt(eventParts[2]);
                Playlist playlist = playlistsArray[playlistId - 1];
                epicBlend.removeSongAndUpdatePlaylists(song, playlist);
            }
            // Handles printing the current state of the EpicBlend.
            else if ("ASK".equals(eventType)) {
                epicBlend.printEpicBlend();
            }
        }

        reader.close();
    }

    // Gets the next line using BufferedReader.
    private static String getNextLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            return line;
        }
        throw new IOException("No non-empty lines found.");
    }
}
