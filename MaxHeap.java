
// MaxHeap class
/**
 * Implements a max heap data structure specifically for storing and managing songs based on a specified category.
 * This max heap structure ensures that the song with the highest score in the given category is always at the top.
 * It is designed to efficiently handle operations like adding, deleting, and peeking at the top song.
 * @author Yusuf Anil Yazici
 */
import java.util.ArrayList;
import java.util.HashMap;

public class MaxHeap {
    private ArrayList<Song> items; // ArrayList to store the elements (songs) in the heap.
    private int category; // The category based on which songs are compared in the heap.

    // Constructor for initializing the max heap with a specific category.
    public MaxHeap(int category) {
        items = new ArrayList<>();
        this.category = category;
    }

    // Returns the number of items in the heap.
    public int size() {
        return items.size();
    }

    // Checks if the heap is empty.
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Returns the list of items in the heap.
    public ArrayList<Song> getItems() {
        return items;
    }

    // Peeks at the top song in the heap without removing it.
    public Song peek() {
        if (items.size() == 0) {
            return null;
        }
        return items.get(0); // The top song is always at index 0.
    }

    // Internal method to bubble up the element after insertion.
    private void percUp() {
        int k = items.size() - 1; // Start with the last element.
        while (k > 0) {
            int p = (k - 1) / 2; // Parent index.
            Song item = items.get(k);
            Song parent = items.get(p);

            if (item.compare(parent, category) > 0) {
                // Swap child with parent if child is greater.
                items.set(k, parent);
                items.set(p, item);

                // Move up one level in the heap.
                k = p;
            } else {
                break;
            }
        }
    }

    // Adds a new song to the heap.
    public void add(Song item) {
        items.add(item); // Add the new song to the end.
        percUp(); // Adjust the position of the newly added song.
    }

    // Builds the heap from a HashMap of songs.
    public void buildHeap(HashMap<Integer, Song> sourceMap) {
        // Load songs from the HashMap into the items list.
        this.items = new ArrayList<>(sourceMap.values());

        // Adjust the positions of the elements to form a valid max heap.
        for (int i = (items.size() / 2) - 1; i >= 0; i--) {
            percDown(i);
        }
    }

    // Builds the heap from an ArrayList of songs.
    public void buildHeap(ArrayList<Song> sourceItems) {
        this.items = new ArrayList<>(sourceItems);
        // Adjust the positions of the elements to form a valid max heap.
        for (int i = (items.size() / 2) - 1; i >= 0; i--) {
            percDown(i);
        }
    }

    // Internal method to bubble down the element after deletion or during heap
    // building.
    private void percDown(int k) {
        while (k < items.size() / 2) { // Node has at least one child.
            int leftChild = 2 * k + 1;
            int rightChild = leftChild + 1;

            // Determine the larger of the two children.
            int largerChild = leftChild;
            if (rightChild < items.size() && items.get(rightChild).compare(items.get(leftChild), category) > 0) {
                largerChild = rightChild;
            }

            // If the current node is larger or equal to the larger child, it's in the right
            // position.
            if (items.get(k).compare(items.get(largerChild), category) >= 0) {
                break;
            }

            // Swap with the larger child.
            Song temp = items.get(k);
            items.set(k, items.get(largerChild));
            items.set(largerChild, temp);

            // Move down to the child's position.
            k = largerChild;
        }
    }

    // Deletes the top song from the heap and returns it.
    public Song delete() {
        if (items.size() == 0) {
            return null;
        }
        if (items.size() == 1) {
            return items.remove(0);// If there's only one element, simply remove and return it.
        }

        // Save the top item.
        Song hold = items.get(0);
        // Replace the top item with the last item.
        items.set(0, items.remove(items.size() - 1));
        // Reheapify the heap starting from the root.
        percDown(0);
        return hold; // Return the saved top item.
    }

}
