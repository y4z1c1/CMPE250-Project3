// PlaylistAVLTree class
/**
 * Represents an AVL Tree data structure specifically designed for managing
 * playlists.
 * This class provides a balanced tree structure to store playlists, allowing
 * efficient
 * insertion, deletion, and searching operations. It maintains the balanced
 * nature of the AVL tree
 * through rotations and height updates during modifications.
 * The tree is categorized based on a specific category, useful for playlist
 * management in different contexts.
 * 
 * @author Yusuf Anil Yazici
 */
public class PlaylistAVLTree {
    private PlaylistNode root; // Root node of the AVL Tree.
    private final int category; // Category of the playlists in the tree.
    public int size = 0; // Size of the AVL Tree, representing the number of playlists.

    // Constructor to initialize the AVL Tree with a specific category.
    public PlaylistAVLTree(int category) {
        this.category = category;
    }

    // Nested static class for AVL Tree nodes.
    static class PlaylistNode {
        Playlist playlist; // The playlist stored in this node.
        PlaylistNode left, right, parent; // Pointers to left, right, and parent nodes.
        int height; // The height of the node for AVL balancing.

        // Constructor for creating a new PlaylistNode with a given playlist.
        PlaylistNode(Playlist playlist) {
            this.playlist = playlist;
            left = right = parent = null; // Initializing child and parent nodes to null.
            height = 1; // Initial height is set to 1.
        }
    }

    // Inserts a new playlist into the AVL Tree.
    public void insert(Playlist playlist) {
        root = insert(root, playlist, null); // Internal method to insert the playlist.
        this.size++; // Incrementing the size of the tree.
    }

    // Internal method for inserting a playlist into the tree.
    private PlaylistNode insert(PlaylistNode node, Playlist playlist, PlaylistNode parent) {
        // Recursive method for finding the correct position and inserting the new node.
        if (node == null) {
            PlaylistNode newNode = new PlaylistNode(playlist);
            newNode.parent = parent;
            return newNode;
        }

        int playlistId = playlist.getPlaylistId();
        int nodeId = node.playlist.getPlaylistId();

        if (playlistId < nodeId) {
            node.left = insert(node.left, playlist, node);
        } else if (playlistId > nodeId) {
            node.right = insert(node.right, playlist, node);
        } else {
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    // Updates the height of a node based on its children's heights.
    private void updateHeight(PlaylistNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Get the height of the node
    private int height(PlaylistNode node) {
        return (node == null) ? 0 : node.height;
    }

    // Balances the node to maintain AVL Tree properties.
    private PlaylistNode balance(PlaylistNode node) {
        // Balancing logic based on the balance factor of the node.

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        } else if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    // Get Balance factor of node
    private int getBalanceFactor(PlaylistNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Rotate right
    private PlaylistNode rotateRight(PlaylistNode y) {
        PlaylistNode x = y.left;
        PlaylistNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Rotate left
    private PlaylistNode rotateLeft(PlaylistNode x) {
        PlaylistNode y = x.right;
        PlaylistNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Deletes a playlist from the AVL tree.
    public void delete(Playlist playlist) {
        root = delete(root, playlist); // Internal method to delete the playlist.
        if (root != null) {
            root.parent = null;
        }
        this.size--; // Decrementing the size of the tree.
    }

    // Internal method for deleting a playlist from the tree.
    private PlaylistNode delete(PlaylistNode node, Playlist playlist) {
        // Recursive method for finding and deleting the node.
        if (node == null) {
            return null;
        }

        int playlistId = playlist.getPlaylistId();
        int nodeId = node.playlist.getPlaylistId();

        if (playlistId < nodeId) {
            node.left = delete(node.left, playlist);
        } else if (playlistId > nodeId) {
            node.right = delete(node.right, playlist);
        } else {
            // Node with only one child or no child
            if (node.left == null || node.right == null) {
                PlaylistNode temp = node.left == null ? node.right : node.left;

                // No child case
                if (temp == null) {
                    return null;
                } else {
                    // One child case
                    temp.parent = node.parent;
                    return temp;
                }
            } else {
                // Node with two children: Get the inorder successor (smallest in the right
                // subtree)
                PlaylistNode temp = minValueNode(node.right);

                // Copy the inorder successor's playlist to this node
                node.playlist = temp.playlist;
                node.right = delete(node.right, temp.playlist);
            }
        }

        return balance(node);
    }

    // Method to get the node with minimum value (used in delete operation)
    private PlaylistNode minValueNode(PlaylistNode node) {
        PlaylistNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Check if a playlist exists in the tree
    public boolean contains(Playlist playlist) {
        return contains(root, playlist);
    }

    private boolean contains(PlaylistNode node, Playlist playlist) {
        if (node == null) {
            return false;
        }

        int playlistId = playlist.getPlaylistId();
        int nodeId = node.playlist.getPlaylistId();

        if (playlistId < nodeId) {
            return contains(node.left, playlist);
        } else if (playlistId > nodeId) {
            return contains(node.right, playlist);
        } else {
            return true;
        }
    }

    // Find the minimum playlist in the AVL tree
    public Playlist findMin() {
        if (root == null) {
            return null;
        }
        return findMin(root).playlist;
    }

    private PlaylistNode findMin(PlaylistNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Find the maximum playlist in the AVL tree
    public Playlist findMax() {
        if (root == null) {
            return null; // Tree is empty
        }
        return findMax(root).playlist;
    }

    private PlaylistNode findMax(PlaylistNode node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Method for finding the maximum scoring song by AVL category in the tree.
    public Song findMaxSongByMaxAvl() {
        if (root == null) {
            return null;
        }
        return findMaxSongByMaxAvl(root, null); // Recursive method to find the max scoring song.
    }

    // Recursive method to find the maximum scoring song in the AVL tree.
    private Song findMaxSongByMaxAvl(PlaylistNode node, Song currentMax) {
        // Traverse the tree and find the maximum scoring song.

        if (node == null) {
            return currentMax;
        }

        Song nodeMaxSong = node.playlist.getMaxAvl(category);

        if (currentMax == null || (nodeMaxSong != null && nodeMaxSong.compare(currentMax, category) > 0)) {
            currentMax = nodeMaxSong;
        }

        currentMax = findMaxSongByMaxAvl(node.left, currentMax);
        currentMax = findMaxSongByMaxAvl(node.right, currentMax);

        return currentMax;
    }

}
