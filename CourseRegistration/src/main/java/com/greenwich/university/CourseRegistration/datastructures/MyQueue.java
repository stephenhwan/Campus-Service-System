package datastructures;

public class MyQueue<T> {
    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T v) { value = v; }
    }

    private Node<T> head, tail;
    private int size;

    public void enqueue(T element) {
        Node<T> n = new Node<>(element);
        if (tail == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public T dequeue() {
        if (head == null) return null;
        T val = head.value;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return val;
    }

    public boolean isEmpty() { return head == null; }

    public int size() { return size; }
}
