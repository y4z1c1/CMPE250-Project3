// PlaylistNode class
/**
 * Represents a node in an AVL Tree, specifically for storing playlists.
 * This node class is part of the AVL Tree data structure used to manage and organize playlists efficiently.
 * It includes references to its left and right children and its parent, which are essential for maintaining the AVL Tree structure.
 * The height attribute is used to ensure the tree remains balanced.
 */
class PlaylistNode {
    Playlist playlist; // The playlist stored in this node.
    PlaylistNode left, right, parent; // Pointers to left, right, and parent nodes in the AVL Tree.

    // The height of the node, used to maintain balance in the AVL Tree.
    int height;

    // Constructor for creating a new PlaylistNode with a given playlist.
    // Initializes left, right, and parent nodes to null, and height to 1.
    PlaylistNode(Playlist playlist) {
        this.playlist = playlist; // Assigning the playlist to this node.
        left = right = parent = null; // Initializing the left, right, and parent nodes to null.
        height = 1; // Setting the initial height of the node to 1.
    }
}
