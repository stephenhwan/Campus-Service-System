package datastructures;

public class MyHashTable<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        Entry(K k, V v) { key = k; value = v; }
    }

    private Entry<K, V>[] buckets;
    private int size;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        buckets = new Entry[16];
        size = 0;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public void put(K key, V value) {
        int idx = hash(key);
        Entry<K, V> e = buckets[idx];
        while (e != null) {
            if (e.key.equals(key)) { e.value = value; return; }
            e = e.next;
        }
        Entry<K, V> newE = new Entry<>(key, value);
        newE.next = buckets[idx];
        buckets[idx] = newE;
        size++;
        if ((double) size / buckets.length > LOAD_FACTOR) resize();
    }

    public V get(K key) {
        int idx = hash(key);
        Entry<K, V> e = buckets[idx];
        while (e != null) {
            if (e.key.equals(key)) return e.value;
            e = e.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] old = buckets;
        buckets = new Entry[old.length * 2];
        size = 0;
        for (Entry<K, V> e : old) {
            while (e != null) {
                put(e.key, e.value);
                e = e.next;
            }
        }
    }
}
