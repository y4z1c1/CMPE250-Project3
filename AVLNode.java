// AVLNode class
/**
 * Represents a node in an AVL Tree specifically designed for storing songs.
 * Each node in the AVL Tree contains a song and pointers to its left, right, and parent nodes.
 * The height attribute is crucial for balancing the AVL Tree.
 * The class is used within the AVL Tree data structure to manage songs in an ordered manner,
 * ensuring efficient insertions, deletions, and lookups.
 */
class AVLNode {
    Song song; // The song stored in this node.
    AVLNode left, right, parent; // Pointers to the left, right, and parent nodes.

    // The height of the node used for balancing the AVL Tree.
    int height;

    // Constructor to create a new AVLNode with a given song.
    // Initializes left, right, and parent nodes to null and height to 1.
    AVLNode(Song song) {
        this.song = song; // Assigning the song to the node.
        left = right = parent = null; // Initializing child and parent nodes to null.
        height = 1; // Initial height set to 1 for a new node.
    }
}
