
// AVLTree class
/**
 * Represents an AVL Tree data structure for managing songs based on a specific category.
 * This class supports efficient operations like insertion, deletion, and searching by maintaining a balanced binary tree.
 * It is optimized for operations that are dependent on the properties of songs, such as comparing by category scores.
 * The tree keeps track of the maximum and minimum songs based on the category score.
 * @author Yusuf Anil Yazici
 */
import java.util.ArrayList;

public class AVLTree {
    private AVLNode root; // Root of the AVL Tree.
    private final int category; // Category used for comparing songs.
    public int size = 0; // Number of nodes (songs) in the tree.
    private Song maxSong = null; // Tracks the song with maximum score in the category.
    private Song minSong = null; // Tracks the song with minimum score in the category.

    // Constructor for initializing the AVL Tree with a specific category.
    public AVLTree(int category) {
        this.category = category;
        this.size = 0;
    }

    // Getter methods.
    public Song getRoot() {
        if (root == null) {
            return null;
        }
        return root.song;
    }

    // Get height of the node
    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    public Song getMaxSong() {
        return maxSong;
    }

    public Song getMinSong() {
        return minSong;
    }

    // Public method to insert a song into the tree.
    public void insert(Song song) {
        root = insert(root, song, null);
        this.size++;
        if (root != null) {
            root.parent = null;
        }

        // Update Max and Min song values.
        if (maxSong == null || song.compare(maxSong, category) > 0) {
            maxSong = song;
        }
        if (minSong == null || song.compare(minSong, category) < 0) {
            minSong = song;
        }
    }

    // Internal method to insert a song into the tree.
    private AVLNode insert(AVLNode node, Song song, AVLNode parent) {
        if (node == null) {
            AVLNode newNode = new AVLNode(song);
            newNode.parent = parent;
            return newNode;
        }

        if (song.compare(node.song, category) < 0) {
            node.left = insert(node.left, song, node);
        } else if (song.compare(node.song, category) > 0) {
            node.right = insert(node.right, song, node);
        } else {
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    // Clears the AVL Tree.
    public void clear() {
        root = null;
        this.size = 0;
    }

    public void delete(Song song) {

        root = delete(root, song);
        this.size--;
        if (root != null) {
            root.parent = null; // Ensure the root's parent is null
        }

        if (root == null) {
            this.size = 0;
        }
        // If the deleted song was the max or min, find the new max or min
        if (song.equals(maxSong)) {
            maxSong = (root != null) ? findMax(root).song : null;
        }
        if (song.equals(minSong)) {
            minSong = (root != null) ? findMin(root).song : null;
        }

    }

    // Internal method to delete a song from the tree.
    private AVLNode delete(AVLNode node, Song song) {
        if (node == null) {
            return null;
        }

        if (song.compare(node.song, category) < 0) {
            node.left = delete(node.left, song);
        } else if (song.compare(node.song, category) > 0) {
            node.right = delete(node.right, song);
        } else {
            // Node with only one child or no child
            if ((node.left == null) || (node.right == null)) {
                AVLNode temp = (node.left == null) ? node.right : node.left;

                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    // One child case
                    temp.parent = node.parent; // Set parent
                    node = temp;
                }
            } else {
                // Node with two children: Get the inorder successor (smallest in the right
                // subtree)
                AVLNode temp = minValueNode(node.right);

                // Copy the inorder successor's data to this node and update parent
                node.song = temp.song;
                node.right = delete(node.right, temp.song); // Delete the inorder successor
            }
        }

        // If the tree had only one node then return
        if (node == null) {
            return node;
        }
        node = balance(node);
        // Update height of the current node
        if (node != null) {
            updateParentReferences(node.left, node);
            updateParentReferences(node.right, node);
        }

        return node;
    }

    // Method to update the height of a node
    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    // Method to get the node with minimum value (used in delete operation)
    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
            current.parent = node; // Update parent
        }
        return current;
    }

    // Balance the node if it's unbalanced
    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    // Get Balance factor of node N
    private int getBalanceFactor(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Right rotate the subtree rooted with y
    private void updateParentReferences(AVLNode child, AVLNode parent) {
        if (child != null) {
            child.parent = parent;
        }
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        // Update parent references
        updateParentReferences(y, x);
        updateParentReferences(T2, y);

        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        // Update parent references
        updateParentReferences(x, y);
        updateParentReferences(T2, x);

        return y;
    }

    // Method to find the minimum valued song in the AVL tree.
    public Song findMin() {
        return findMin(root).song;
    }

    private AVLNode findMin(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Method to find the maximum valued song in the AVL tree.
    public Song findMax() {
        if (root == null) {
            return null;
        }
        return findMax(root).song;
    }

    private AVLNode findMax(AVLNode node) {
        if (node.right == null) {
            return node;
        }
        return findMax(node.right);
    }

    // Utility method to get all the songs in the tree in sorted order.
    public ArrayList<Song> getItems() {
        ArrayList<Song> items = new ArrayList<>();
        inOrderTraversal(root, items);
        return items;
    }

    // Helper method to perform in-order traversal
    private void inOrderTraversal(AVLNode node, ArrayList<Song> items) {
        if (node != null) {
            inOrderTraversal(node.left, items); // Visit left subtree
            items.add(node.song); // Visit node
            inOrderTraversal(node.right, items); // Visit right subtree
        }
    }

    // Checks if a particular song is present in the tree.
    public boolean contains(Song target) {
        return contains(root, target);
    }

    private boolean contains(AVLNode node, Song target) {
        if (node == null) {
            return false;
        }

        int comparison = target.compare(node.song, category);
        if (comparison < 0) {
            return contains(node.left, target);
        } else if (comparison > 0) {
            return contains(node.right, target);
        } else {
            return true; // Found the target
        }
    }

}
