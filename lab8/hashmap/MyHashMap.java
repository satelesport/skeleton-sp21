package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void clear() {
        itemNum = 0;
        keySet.clear();
        buckets = createTable(tableSize);
    }

    @Override
    public boolean containsKey(K key) {
        if(keySet.contains(key)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public V get(K key) {
        if(!keySet.contains(key)){
            return null;
        }
        int index = Math.floorMod(key.hashCode(),tableSize);
        for(Node n : buckets[index]){
            if(n.key.equals(key)){
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return itemNum;
    }

    @Override
    public void put(K key, V value) {
        keySet.add(key);
        int index = Math.floorMod(key.hashCode(),tableSize);
        Node newNode = new Node(key, value);
        if(buckets[index] == null){
            buckets[index] = createBucket();
            buckets[index].add(newNode);
            itemNum += 1;
        }
        else{
            for(Node n : buckets[index]){
                if(n.key.equals(key)){
                    n.value = value;
                    return;
                }
            }
            buckets[index].add(newNode);
            itemNum += 1;
        }
        if(itemNum * 1.0 / tableSize >= loadFactor){
            resize(tableSize * 2);
            tableSize *= 2;
        }
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        if(!keySet.contains(key)){
            return null;
        }
        keySet.remove(key);
        Node removeNode = null;
        int index = Math.floorMod(key.hashCode(),tableSize);
        for(Node n : buckets[index]){
            if(n.key.equals(key)){
                removeNode = n;
                break;
            }
        }
        buckets[index].remove(removeNode);
        return removeNode.value;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove is not supported!");
    }

    @Override
    public Iterator<K> iterator() {
        return new MapIterator();
    }

    private void resize(int size){
        Collection<Node>[] newBuckets = createTable(size);
        for(K key : keySet){
            V value = get(key);
            int index = Math.floorMod(key.hashCode(),tableSize * 2);
            Node newNode = new Node(key, value);
            if(newBuckets[index] == null){
                newBuckets[index] = createBucket();
                newBuckets[index].add(newNode);
            }
            else{
                newBuckets[index].add(newNode);
            }
        }
        buckets = newBuckets;
    }

    private class MapIterator implements Iterator<K>{
        LinkedList<K> keyList = createKeyList();
        int keyNum = 0;

        private LinkedList<K> createKeyList(){
            LinkedList<K> newList = new LinkedList<>();
            for(int i = 0; i < tableSize; i++){
                if(buckets[i] == null){
                    continue;
                }
                for(Node n : buckets[i]){
                    newList.add(n.key);
                }
            }
            return newList;
        }

        @Override
        public boolean hasNext() {
            if(keyNum != itemNum){
                return true;
            }
            return false;
        }

        @Override
        public K next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return keyList.remove();
        }

    }
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int tableSize;
    private double loadFactor;
    private int itemNum;
    private Set<K> keySet = new HashSet<K>();

    /** Constructors */
    public MyHashMap() {
        buckets  = createTable(16);
        itemNum = 0;
        tableSize = 16;
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        itemNum = 0;
        tableSize = initialSize;
        loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        itemNum = 0;
        tableSize = initialSize;
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
