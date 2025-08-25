package datastructures;

@SuppressWarnings("unchecked")
public class MyHeap<T> {
    private T[] data;
    private int size;
    private MyComparator<T> cmp;

    public MyHeap(MyComparator<T> cmp) {
        data = (T[]) new Object[16];
        this.cmp = cmp;
    }

    public void insert(T element) {
        ensureCapacity();
        data[size] = element;
        siftUp(size++);
    }

    public T extractMax() {
        if (size == 0) return null;
        T root = data[0];
        data[0] = data[--size];
        data[size] = null;
        siftDown(0);
        return root;
    }

    public boolean isEmpty() { return size == 0; }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (cmp.compare(data[i], data[parent]) > 0) {
                swap(i, parent);
                i = parent;
            } else break;
        }
    }

    private void siftDown(int i) {
        while (i * 2 + 1 < size) {
            int left = i * 2 + 1, right = i * 2 + 2, largest = left;
            if (right < size && cmp.compare(data[right], data[left]) > 0) largest = right;
            if (cmp.compare(data[largest], data[i]) > 0) {
                swap(largest, i);
                i = largest;
            } else break;
        }
    }

    private void ensureCapacity() {
        if (size == data.length) {
            T[] newData = (T[]) new Object[data.length * 2];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
    }

    private void swap(int i, int j) {
        T tmp = data[i]; data[i] = data[j]; data[j] = tmp;
    }
}
