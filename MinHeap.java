import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private List<Song> items;
    private int category;

    public MinHeap(int category) {
        items = new ArrayList<>();
        this.category = category;
    }

    public Song peek() {
        if (items.isEmpty()) {
            return new Song(-1,"null",-1,-1,-1,-1);
        }
        return items.get(0);
    }

    private void percUp() {
        int k = items.size() - 1;
        while (k > 0) {
            int p = (k - 1) / 2; // Parent index
            Song item = items.get(k);
            Song parent = items.get(p);

            if (item.compare(parent, category) < 0) { // Note the change here for MinHeap
                items.set(k, parent);
                items.set(p, item);
                k = p;
            } else {
                break;
            }
        }
    }

    public void add(Song item) {
        items.add(item);
        percUp();
    }

    public void buildHeap(ArrayList<Song> sourceItems) {
        items = new ArrayList<>(sourceItems);
        for (int i = (items.size() / 2) - 1; i >= 0; i--) {
            percDown(i);
        }
    }

    private void percDown(int k) {
        while (k < items.size() / 2) {
            int leftChild = 2 * k + 1;
            int rightChild = leftChild + 1;

            int smallerChild = leftChild; // Start with left child
            if (rightChild < items.size() && items.get(rightChild).compare(items.get(leftChild), category) < 0) {
                smallerChild = rightChild;
            }

            if (items.get(k).compare(items.get(smallerChild), category) <= 0) {
                break;
            }

            Song temp = items.get(k);
            items.set(k, items.get(smallerChild));
            items.set(smallerChild, temp);
            k = smallerChild;
        }
    }

    public Song delete() {
        if (items.isEmpty()) {
            return new Song(-1,"null",-1,-1,-1,-1);
        }
        if (items.size() == 1) {
            return items.remove(0);
        }

        Song hold = items.get(0);
        items.set(0, items.remove(items.size() - 1));
        percDown(0);
        return hold;
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public List<Song> getItems(){
        return items;
    }
}
