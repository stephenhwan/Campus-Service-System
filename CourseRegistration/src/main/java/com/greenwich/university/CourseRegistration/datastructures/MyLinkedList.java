package datastructures;

import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {
    private class Node {
        T value;
        Node next;
        Node(T v) { value = v; }
    }

    private Node head, tail;
    private int size;

    public void add(T element) {
        Node n = new Node(element);
        if (head == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public boolean remove(T element) {
        Node prev = null, cur = head;
        while (cur != null) {
            if (cur.value.equals(element)) {
                if (prev == null) head = cur.next;
                else prev.next = cur.next;
                if (cur == tail) tail = prev;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Node cur = head;
            public boolean hasNext() { return cur != null; }
            public T next() { T v = cur.value; cur = cur.next; return v; }
        };
    }
}
